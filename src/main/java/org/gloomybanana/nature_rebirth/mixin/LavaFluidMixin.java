package org.gloomybanana.nature_rebirth.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin {

    // 辅助方法：检查岩浆周围（包括顶部）是否有蓝冰
    private boolean hasAdjacentBlueIce(LevelAccessor level, BlockPos pos) {
        return hasAdjacentBlock(level, pos, Blocks.BLUE_ICE);
    }
    
    // 辅助方法：检查岩浆周围（包括顶部）是否有指定方块
    private boolean hasAdjacentBlock(LevelAccessor level, BlockPos pos, net.minecraft.world.level.block.Block targetBlock) {
        // 检查水平方向
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos adjacentPos = pos.relative(dir);
            BlockState adjacentState = level.getBlockState(adjacentPos);
            if (adjacentState.is(targetBlock)) {
                return true;
            }
        }
        // 检查顶部
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        if (aboveState.is(targetBlock)) {
            return true;
        }
        return false;
    }
    
    // 辅助方法：检查自定义生成规则
    private boolean checkCustomGeneration(LevelAccessor level, BlockPos pos, Block inputBlock, BlockState resultBlock, CallbackInfo ci) {
        var rules = org.gloomybanana.nature_rebirth.CustomGenerationConfig.getRules();
        
        for (var rule : rules) {
            if (rule.inputBlock == inputBlock) {
                // 检查相邻方块是否满足要求
                boolean allAdjacentPresent = true;
                for (Block requiredBlock : rule.requiredAdjacentBlocks) {
                    if (!hasAdjacentBlock(level, pos, requiredBlock)) {
                        allAdjacentPresent = false;
                        break;
                    }
                }
                
                if (allAdjacentPresent) {
                    BlockState finalBlock = rule.outputBlock.defaultBlockState();
                    
                    // 信标增强
                    if (hasBeaconBelow(level, pos)) {
                        BlockState oreBlock = getStoneOre(level);
                        if (oreBlock != null) {
                            finalBlock = oreBlock;
                        }
                    }
                    
                    level.setBlock(pos, finalBlock, 3);
                    playEffect(level, pos);
                    ci.cancel();
                    return true;
                }
            }
        }
        return false;
    }

    // 辅助方法：检查下方是否有信标（检查多个方块距离）
    private boolean hasBeaconBelow(LevelAccessor level, BlockPos pos) {
        // 检查下方1-5格是否有信标
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
    private BlockState getStoneOre(LevelAccessor level) {
        double rand = level.getRandom().nextDouble();
        double cumulative = 0.0;
        
        // 煤矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_COAL_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.COAL_ORE.defaultBlockState();
        
        // 铁矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_IRON_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.IRON_ORE.defaultBlockState();
        
        // 红石矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_REDSTONE_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.REDSTONE_ORE.defaultBlockState();
        
        // 青金石矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_LAPIS_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.LAPIS_ORE.defaultBlockState();
        
        // 金矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_GOLD_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.GOLD_ORE.defaultBlockState();
        
        // 绿宝石矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_EMERALD_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.EMERALD_ORE.defaultBlockState();
        
        // 钻石矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_DIAMOND_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DIAMOND_ORE.defaultBlockState();
        
        return null; // 不生成矿石
    }

    // 辅助方法：根据概率选择深板岩矿石（基于配置文件）
    private BlockState getDeepslateOre(LevelAccessor level) {
        double rand = level.getRandom().nextDouble();
        double cumulative = 0.0;
        
        // 深板岩煤矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_COAL_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DEEPSLATE_COAL_ORE.defaultBlockState();
        
        // 深板岩铁矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_IRON_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DEEPSLATE_IRON_ORE.defaultBlockState();
        
        // 深板岩红石矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_REDSTONE_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DEEPSLATE_REDSTONE_ORE.defaultBlockState();
        
        // 深板岩青金石矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_LAPIS_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DEEPSLATE_LAPIS_ORE.defaultBlockState();
        
        // 深板岩金矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_GOLD_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState();
        
        // 深板岩绿宝石矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_EMERALD_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DEEPSLATE_EMERALD_ORE.defaultBlockState();
        
        // 深板岩钻石矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.STONE_DIAMOND_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState();
        
        return null; // 不生成矿石
    }

    // 辅助方法：根据概率选择下界矿石（基于配置文件）
    private BlockState getNetherOre(LevelAccessor level) {
        double rand = level.getRandom().nextDouble();
        double cumulative = 0.0;
        
        // 下界石英矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.NETHER_QUARTZ_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.NETHER_QUARTZ_ORE.defaultBlockState();
        
        // 下界金矿石
        cumulative += org.gloomybanana.nature_rebirth.Config.NETHER_GOLD_ORE_CHANCE.get();
        if (rand < cumulative) return Blocks.NETHER_GOLD_ORE.defaultBlockState();
        
        // 远古残骸
        cumulative += org.gloomybanana.nature_rebirth.Config.ANCIENT_DEBRIS_CHANCE.get();
        if (rand < cumulative) return Blocks.ANCIENT_DEBRIS.defaultBlockState();
        
        return null; // 不生成矿石
    }

    // 注入到 spreadTo 方法开始处，在岩浆放置前检查条件
    @Inject(method = "spreadTo",
            at = @At("HEAD"),
            cancellable = true)
    private void onSpreadTo(LevelAccessor level, BlockPos pos, BlockState state,
                           Direction direction, FluidState fluidState, CallbackInfo ci) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        
        // 先检查自定义生成规则
        if (checkCustomGeneration(level, pos, belowState.getBlock(), null, ci)) {
            return;
        }
        
        // 下界岩生成：流动的岩浆周围6个面同时存在蓝冰和岩浆块
        if (org.gloomybanana.nature_rebirth.Config.NETHERRACK_GENERATION.get()) {
            if (hasAdjacentBlueIceAndMagmaBlock(level, pos)) {
                BlockState resultBlock = Blocks.NETHERRACK.defaultBlockState();

                // 信标增强：有概率生成下界矿石
                if (hasBeaconBelow(level, pos)) {
                    BlockState oreBlock = getNetherOre(level);
                    if (oreBlock != null) {
                        resultBlock = oreBlock;
                    }
                }

                level.setBlock(pos, resultBlock, 3);
                playEffect(level, pos);
                ci.cancel();
                return;
            }
        }
    }

    // 辅助方法：检查岩浆周围6个面是否同时存在蓝冰和岩浆块
    private boolean hasAdjacentBlueIceAndMagmaBlock(LevelAccessor level, BlockPos pos) {
        boolean hasBlueIce = false;
        boolean hasMagmaBlock = false;
        
        // 检查6个方向
        for (Direction dir : Direction.values()) {
            BlockPos adjacentPos = pos.relative(dir);
            BlockState adjacentState = level.getBlockState(adjacentPos);
            
            if (adjacentState.is(Blocks.BLUE_ICE)) {
                hasBlueIce = true;
            } else if (adjacentState.is(Blocks.MAGMA_BLOCK)) {
                hasMagmaBlock = true;
            }
            
            // 提前退出
            if (hasBlueIce && hasMagmaBlock) {
                return true;
            }
        }
        
        return hasBlueIce && hasMagmaBlock;
    }

    // 注入到 spreadTo 方法中，在放置石头前检查深板岩条件
    @Inject(method = "spreadTo",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/level/LevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"),
            cancellable = true)
    private void onSpreadToSetBlock(LevelAccessor level, BlockPos pos, BlockState state,
                                    Direction direction, FluidState fluidState, CallbackInfo ci) {
        // 深板岩生成：Y坐标低于阈值
        if (pos.getY() < org.gloomybanana.nature_rebirth.Config.DEEPSLATE_Y_THRESHOLD.get()) {
            BlockState resultBlock = Blocks.DEEPSLATE.defaultBlockState();
            
            // 信标增强：有概率生成深板岩矿石
            if (hasBeaconBelow(level, pos)) {
                BlockState oreBlock = getDeepslateOre(level);
                if (oreBlock != null) {
                    resultBlock = oreBlock;
                }
            }
            
            level.setBlock(pos, resultBlock, 3);
            playEffect(level, pos);
            ci.cancel();
            return;
        }
        
        // 普通石头生成（岩浆向下流动遇水）
        BlockState resultBlock = Blocks.STONE.defaultBlockState();
        
        // 信标增强：有概率生成石制矿石
        if (hasBeaconBelow(level, pos)) {
            BlockState oreBlock = getStoneOre(level);
            if (oreBlock != null) {
                resultBlock = oreBlock;
            }
        }
        
        level.setBlock(pos, resultBlock, 3);
        playEffect(level, pos);
        ci.cancel();
    }

    // 播放音效和粒子的辅助方法
    private void playEffect(LevelAccessor level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, net.minecraft.sounds.SoundSource.BLOCKS, 0.5f, 2.0f);
            for (int i = 0; i < 8; ++i) {
                double offsetX = (serverLevel.getRandom().nextDouble() - 0.5) * 0.5;
                double offsetY = (serverLevel.getRandom().nextDouble() - 0.5) * 0.5;
                double offsetZ = (serverLevel.getRandom().nextDouble() - 0.5) * 0.5;
                serverLevel.sendParticles(ParticleTypes.LAVA,
                        pos.getX() + 0.5 + offsetX,
                        pos.getY() + 0.5 + offsetY,
                        pos.getZ() + 0.5 + offsetZ,
                        1, 0.0, 0.0, 0.0, 0.0);
            }
        }
    }
}
