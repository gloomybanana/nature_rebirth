package org.gloomybanana.nature_rebirth.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlock.class)
public abstract class LiquidBlockMixin {

    // 辅助方法：检查下方是否有信标
    private boolean hasBeaconBelow(Level level, BlockPos pos) {
        for (int i = 1; i <= 5; i++) {
            BlockPos belowPos = pos.below(i);
            BlockState belowState = level.getBlockState(belowPos);
            if (belowState.is(Blocks.BEACON)) {
                return true;
            }
        }
        return false;
    }

    // 辅助方法：根据概率选择石制矿石（基于配置文件）
    private BlockState getStoneOre(Level level) {
        double rand = level.getRandom().nextDouble();
        double cumulative = 0.0;
        
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_COAL_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.COAL_ORE.defaultBlockState();
        
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_IRON_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.IRON_ORE.defaultBlockState();
        
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_REDSTONE_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.REDSTONE_ORE.defaultBlockState();
        
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_LAPIS_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.LAPIS_ORE.defaultBlockState();
        
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_GOLD_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.GOLD_ORE.defaultBlockState();
        
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_EMERALD_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.EMERALD_ORE.defaultBlockState();
        
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_DIAMOND_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DIAMOND_ORE.defaultBlockState();
        
        return null;
    }

    // 辅助方法：根据概率选择深板岩矿石（基于配置文件）
    private BlockState getDeepslateOre(Level level) {
        double rand = level.getRandom().nextDouble();
        double cumulative = 0.0;
        
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_COAL_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DEEPSLATE_COAL_ORE.defaultBlockState();
        
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_IRON_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DEEPSLATE_IRON_ORE.defaultBlockState();
        
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_REDSTONE_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DEEPSLATE_REDSTONE_ORE.defaultBlockState();
        
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_LAPIS_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DEEPSLATE_LAPIS_ORE.defaultBlockState();
        
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_GOLD_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState();
        
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_EMERALD_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DEEPSLATE_EMERALD_ORE.defaultBlockState();
        
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_DIAMOND_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState();
        
        return null;
    }

    // 注入到 shouldSpreadLiquid 方法中，在返回前检查是否需要替换方块并添加音效和粒子
    @Inject(method = "shouldSpreadLiquid",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"),
            cancellable = true)
    private void onShouldSpreadLiquid(Level level, BlockPos pos, BlockState state,
                                    CallbackInfoReturnable<Boolean> cir) {
        // 获取目标位置的方块状态（即将被替换的方块）
        BlockState targetState = level.getBlockState(pos);

        // 只处理目标是岩浆的情况
        if (targetState.is(Blocks.LAVA)) {
            // 方解石生成检测：岩浆下方有骨块，且周围有蓝冰
            if (org.gloomybanana.nature_rebirth.Config.CALCITE_GENERATION.get()) {
                BlockPos belowPos = pos.below();
                BlockState belowState = level.getBlockState(belowPos);
                if (belowState.is(Blocks.BONE_BLOCK)) {
                    // 检查岩浆周围（水平方向和顶部）是否有蓝冰
                    boolean hasBlueIce = false;
                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        if (level.getBlockState(pos.relative(direction)).is(Blocks.BLUE_ICE)) {
                            hasBlueIce = true;
                            break;
                        }
                    }
                    if (!hasBlueIce && level.getBlockState(pos.above()).is(Blocks.BLUE_ICE)) {
                        hasBlueIce = true;
                    }
                    
                    if (hasBlueIce) {
                        BlockState resultBlock = Blocks.CALCITE.defaultBlockState();
                        
                        // 信标增强：有概率生成石制矿石
                        if (hasBeaconBelow(level, pos)) {
                            BlockState oreBlock = getStoneOre(level);
                            if (oreBlock != null) {
                                resultBlock = oreBlock;
                            }
                        }
                        
                        level.setBlockAndUpdate(pos, resultBlock);

                        // 播放音效和生成粒子
                        if (level instanceof ServerLevel serverLevel) {
                            serverLevel.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, net.minecraft.sounds.SoundSource.BLOCKS, 0.5f, 2.0f);
                            for (int i = 0; i < 8; ++i) {
                                double offsetX = (serverLevel.getRandom().nextDouble() - 0.5) * 0.5;
                                double offsetY = serverLevel.getRandom().nextDouble() * 0.5;
                                double offsetZ = (serverLevel.getRandom().nextDouble() - 0.5) * 0.5;
                                serverLevel.sendParticles(ParticleTypes.LAVA,
                                        pos.getX() + 0.5 + offsetX,
                                        pos.getY() + 0.5 + offsetY,
                                        pos.getZ() + 0.5 + offsetZ,
                                        1, 0.0, 0.0, 0.0, 0.0);
                            }
                        }

                        cir.setReturnValue(true);
                        return;
                    }
                }
            }

            // 深板岩生成检测：Y坐标低于阈值
            if (pos.getY() < org.gloomybanana.nature_rebirth.Config.DEEPSLATE_Y_THRESHOLD.get()) {
                // 检查岩浆是否是源方块
                FluidState fluidState = level.getFluidState(pos);
                if (!fluidState.isSource()) {
                    BlockState resultBlock = Blocks.COBBLED_DEEPSLATE.defaultBlockState();
                    
                    // 信标增强：有概率生成深板岩矿石
                    if (hasBeaconBelow(level, pos)) {
                        BlockState oreBlock = getDeepslateOre(level);
                        if (oreBlock != null) {
                            resultBlock = oreBlock;
                        }
                    }
                    
                    level.setBlockAndUpdate(pos, resultBlock);

                    // 播放音效和生成粒子
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, net.minecraft.sounds.SoundSource.BLOCKS, 0.5f, 2.0f);
                        for (int i = 0; i < 8; ++i) {
                            double offsetX = (serverLevel.getRandom().nextDouble() - 0.5) * 0.5;
                            double offsetY = serverLevel.getRandom().nextDouble() * 0.5;
                            double offsetZ = (serverLevel.getRandom().nextDouble() - 0.5) * 0.5;
                            serverLevel.sendParticles(ParticleTypes.LAVA,
                                    pos.getX() + 0.5 + offsetX,
                                    pos.getY() + 0.5 + offsetY,
                                    pos.getZ() + 0.5 + offsetZ,
                                    1, 0.0, 0.0, 0.0, 0.0);
                        }
                    }

                    cir.setReturnValue(true);
                    return;
                }
            }

            // 普通圆石生成（岩浆遇水）- 没有信标增强
            FluidState fluidState = level.getFluidState(pos);
            if (!fluidState.isSource()) {
                level.setBlockAndUpdate(pos, Blocks.COBBLESTONE.defaultBlockState());

                // 播放音效和生成粒子
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, net.minecraft.sounds.SoundSource.BLOCKS, 0.5f, 2.0f);
                    for (int i = 0; i < 8; ++i) {
                        double offsetX = (serverLevel.getRandom().nextDouble() - 0.5) * 0.5;
                        double offsetY = serverLevel.getRandom().nextDouble() * 0.5;
                        double offsetZ = (serverLevel.getRandom().nextDouble() - 0.5) * 0.5;
                        serverLevel.sendParticles(ParticleTypes.LAVA,
                                pos.getX() + 0.5 + offsetX,
                                pos.getY() + 0.5 + offsetY,
                                pos.getZ() + 0.5 + offsetZ,
                                1, 0.0, 0.0, 0.0, 0.0);
                    }
                }

                cir.setReturnValue(true);
                return;
            }
        }
    }
}
