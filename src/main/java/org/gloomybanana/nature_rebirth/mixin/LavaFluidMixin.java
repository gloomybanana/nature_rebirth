package org.gloomybanana.nature_rebirth.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin {

    // 自定义矿石配置（直接存储方块对象）
    private static List<ConfiguredOre> stoneOreList = null;
    private static List<ConfiguredOre> deepslateOreList = null;
    private static List<ConfiguredOre> netherOreList = null;
    private static boolean cacheInitialized = false;

    // 矿石配置类
    private static class ConfiguredOre {
        final Block block;
        final double cumulativeChance;
        
        ConfiguredOre(Block block, double cumulativeChance) {
            this.block = block;
            this.cumulativeChance = cumulativeChance;
        }
    }

    // 辅助方法：解析方块名称（支持短名称和完整ID）
    private static Block parseBlock(String name) {
        name = name.trim().toLowerCase();
        
        // 尝试解析完整方块ID格式（modid:block_name）
        try {
            Identifier blockId = Identifier.parse(name);
            return BuiltInRegistries.BLOCK.getOptional(blockId).orElse(null);
        } catch (Exception e) {
            // 如果解析失败，尝试作为 Minecraft 方块解析
            try {
                Identifier blockId = Identifier.parse("minecraft:" + name);
                return BuiltInRegistries.BLOCK.getOptional(blockId).orElse(null);
            } catch (Exception e2) {
                return null;
            }
        }
    }

    // 初始化自定义矿石配置
    private static void initializeCustomOreCache() {
        if (cacheInitialized) {
            return;
        }
        cacheInitialized = true;
        
        // 解析石头矿石配置
        var stoneConfig = org.gloomybanana.nature_rebirth.Config.STONE_ORE_CUSTOM_LIST.get();
        stoneOreList = new ArrayList<>();
        if (!stoneConfig.isEmpty()) {
            stoneOreList = parseOreConfig(stoneConfig);
        }
        
        // 解析深板岩矿石配置
        var deepslateConfig = org.gloomybanana.nature_rebirth.Config.DEEPSLATE_ORE_CUSTOM_LIST.get();
        deepslateOreList = new ArrayList<>();
        if (!deepslateConfig.isEmpty()) {
            deepslateOreList = parseOreConfig(deepslateConfig);
        }
        
        // 解析下界矿石配置
        var netherConfig = org.gloomybanana.nature_rebirth.Config.NETHER_ORE_CUSTOM_LIST.get();
        netherOreList = new ArrayList<>();
        if (!netherConfig.isEmpty()) {
            netherOreList = parseOreConfig(netherConfig);
        }
        
        // 如果加载了 mekanism 模组，添加预设的矿石配置
        if (ModList.get().isLoaded("mekanism")) {
            addMekanismOres();
        }
    }
    
    // 添加 mekanism 模组预设矿石
    private static void addMekanismOres() {
        // 石头版本的 mekanism 矿石
        String[] mekanismStoneOres = {
            "mekanism:fluorite_ore:0.07",
            "mekanism:tin_ore:0.12",
            "mekanism:uranium_ore:0.01",
            "mekanism:lead_ore:0.04",
            "mekanism:osmium_ore:0.03"
        };
        
        // 深板岩版本的 mekanism 矿石
        String[] mekanismDeepslateOres = {
            "mekanism:deepslate_fluorite_ore:0.07",
            "mekanism:deepslate_tin_ore:0.12",
            "mekanism:deepslate_uranium_ore:0.01",
            "mekanism:deepslate_lead_ore:0.04",
            "mekanism:deepslate_osmium_ore:0.03"
        };
        
        // 将 mekanism 矿石添加到现有列表
        if (stoneOreList != null) {
            List<ConfiguredOre> mekanismStoneList = parseOreConfig(java.util.Arrays.asList(mekanismStoneOres));
            stoneOreList.addAll(mekanismStoneList);
        }
        
        if (deepslateOreList != null) {
            List<ConfiguredOre> mekanismDeepslateList = parseOreConfig(java.util.Arrays.asList(mekanismDeepslateOres));
            deepslateOreList.addAll(mekanismDeepslateList);
        }
    }

    // 解析矿石配置列表
    private static List<ConfiguredOre> parseOreConfig(List<? extends String> configList) {
        List<ConfiguredOre> result = new ArrayList<>();
        double cumulative = 0.0;
        
        for (String entry : configList) {
            // 找到最后一个冒号，前面的部分是方块ID，后面的部分是概率
            int lastColonIndex = entry.lastIndexOf(":");
            if (lastColonIndex > 0) {
                String oreName = entry.substring(0, lastColonIndex).trim().toLowerCase();
                String chanceStr = entry.substring(lastColonIndex + 1).trim();
                try {
                    double chance = Double.parseDouble(chanceStr);
                    cumulative += chance;
                    
                    Block block = parseBlock(oreName);
                    if (block != null) {
                        result.add(new ConfiguredOre(block, cumulative));
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        
        return result;
    }

    // 辅助方法：检查岩浆周围（包括顶部）是否有蓝冰
    private boolean hasAdjacentBlueIce(LevelAccessor level, BlockPos pos) {
        return hasAdjacentBlock(level, pos, Blocks.BLUE_ICE);
    }
    
    // 辅助方法：检查岩浆周围（包括顶部）是否有指定方块
    private boolean hasAdjacentBlock(LevelAccessor level, BlockPos pos, Block targetBlock) {
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
    private boolean checkCustomGeneration(LevelAccessor level, BlockPos pos, Block bottomBlock, CallbackInfo ci) {
        var rules = org.gloomybanana.nature_rebirth.CustomGenerationConfig.getRules();
        
        for (var rule : rules) {
            if (rule.bottomBlock == bottomBlock) {
                // 检查相邻方块是否满足要求
                boolean allAdjacentPresent = true;
                for (Block requiredBlock : rule.requiredAdjacentBlocks) {
                    if (!hasAdjacentBlock(level, pos, requiredBlock)) {
                        allAdjacentPresent = false;
                        break;
                    }
                }
                
                if (allAdjacentPresent) {
                    BlockState finalBlock = rule.generateBlock.defaultBlockState();
                    
                    level.setBlock(pos, finalBlock, 3);
                    playEffect(level, pos);
                    ci.cancel();
                    return true;
                }
            }
        }
        return false;
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
        initializeCustomOreCache();
        
        if (stoneOreList != null && !stoneOreList.isEmpty()) {
            return selectOreFromList(level, stoneOreList);
        }
        
        return null;
    }

    // 辅助方法：根据概率选择深板岩矿石（基于配置文件）
    private BlockState getDeepslateOre(LevelAccessor level) {
        initializeCustomOreCache();
        
        if (deepslateOreList != null && !deepslateOreList.isEmpty()) {
            return selectOreFromList(level, deepslateOreList);
        }
        
        return null;
    }

    // 辅助方法：根据概率选择下界矿石（基于配置文件）
    private BlockState getNetherOre(LevelAccessor level) {
        initializeCustomOreCache();
        
        if (netherOreList != null && !netherOreList.isEmpty()) {
            return selectOreFromList(level, netherOreList);
        }
        
        return null;
    }

    // 从矿石列表中根据概率选择矿石
    private BlockState selectOreFromList(LevelAccessor level, List<ConfiguredOre> oreList) {
        double rand = level.getRandom().nextDouble();
        
        for (ConfiguredOre ore : oreList) {
            if (rand < ore.cumulativeChance) {
                return ore.block.defaultBlockState();
            }
        }
        
        // 如果没有选中任何矿石（概率总和不足1.0），返回null表示生成普通方块
        return null;
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
        if (checkCustomGeneration(level, pos, belowState.getBlock(), ci)) {
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

    // 注入到 spreadTo 方法中，在放置石头前检查深板岩条件
    @Inject(method = "spreadTo",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/level/LevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"),
            cancellable = true)
    private void onSpreadToSetBlock(LevelAccessor level, BlockPos pos, BlockState state,
                                    Direction direction, FluidState fluidState, CallbackInfo ci) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        // 检查是否满足原版玄武岩生成条件（灵魂土 + 蓝冰），如果是则不干预，让原版处理
        if (belowState.is(Blocks.SOUL_SOIL)) {
            boolean hasBlueIceForBasalt = false;
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                if (level.getBlockState(pos.relative(dir)).is(Blocks.BLUE_ICE)) {
                    hasBlueIceForBasalt = true;
                    break;
                }
            }
            if (!hasBlueIceForBasalt && level.getBlockState(pos.above()).is(Blocks.BLUE_ICE)) {
                hasBlueIceForBasalt = true;
            }
            if (hasBlueIceForBasalt) {
                // 满足玄武岩生成条件，不干预，让原版处理
                return;
            }
        }

        // 深板岩生成：Y坐标低于阈值，且下方是自然岩石方块
        if (pos.getY() < org.gloomybanana.nature_rebirth.Config.DEEPSLATE_Y_THRESHOLD.get()) {
            // 检查下方是否是自然岩石方块（石头、深板岩、花岗岩、安山岩、闪长岩）
            Block belowBlock = belowState.getBlock();
            boolean isNaturalRock = belowBlock == Blocks.STONE ||
                                   belowBlock == Blocks.DEEPSLATE ||
                                   belowBlock == Blocks.GRANITE ||
                                   belowBlock == Blocks.ANDESITE ||
                                   belowBlock == Blocks.DIORITE;
            if (isNaturalRock) {
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
