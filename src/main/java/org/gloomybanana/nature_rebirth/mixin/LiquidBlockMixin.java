package org.gloomybanana.nature_rebirth.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mixin(LiquidBlock.class)
public abstract class LiquidBlockMixin {

    private static boolean cacheInitialized = false;
    private static Map<String, Double> stoneOreCache = null;
    private static Map<String, Double> deepslateOreCache = null;

    // 方块名称映射（短名称到方块对象）
    private static final Map<String, Block> ORE_BLOCK_MAP = new LinkedHashMap<>();

    static {
        // 石头矿石
        ORE_BLOCK_MAP.put("coal_ore", Blocks.COAL_ORE);
        ORE_BLOCK_MAP.put("iron_ore", Blocks.IRON_ORE);
        ORE_BLOCK_MAP.put("copper_ore", Blocks.COPPER_ORE);
        ORE_BLOCK_MAP.put("gold_ore", Blocks.GOLD_ORE);
        ORE_BLOCK_MAP.put("redstone_ore", Blocks.REDSTONE_ORE);
        ORE_BLOCK_MAP.put("lapis_ore", Blocks.LAPIS_ORE);
        ORE_BLOCK_MAP.put("emerald_ore", Blocks.EMERALD_ORE);
        ORE_BLOCK_MAP.put("diamond_ore", Blocks.DIAMOND_ORE);
        
        // 深板岩矿石
        ORE_BLOCK_MAP.put("deepslate_coal_ore", Blocks.DEEPSLATE_COAL_ORE);
        ORE_BLOCK_MAP.put("deepslate_iron_ore", Blocks.DEEPSLATE_IRON_ORE);
        ORE_BLOCK_MAP.put("deepslate_copper_ore", Blocks.DEEPSLATE_COPPER_ORE);
        ORE_BLOCK_MAP.put("deepslate_gold_ore", Blocks.DEEPSLATE_GOLD_ORE);
        ORE_BLOCK_MAP.put("deepslate_redstone_ore", Blocks.DEEPSLATE_REDSTONE_ORE);
        ORE_BLOCK_MAP.put("deepslate_lapis_ore", Blocks.DEEPSLATE_LAPIS_ORE);
        ORE_BLOCK_MAP.put("deepslate_emerald_ore", Blocks.DEEPSLATE_EMERALD_ORE);
        ORE_BLOCK_MAP.put("deepslate_diamond_ore", Blocks.DEEPSLATE_DIAMOND_ORE);
    }

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

    // 初始化自定义矿石配置缓存
    private static void initializeCustomOreCache() {
        if (cacheInitialized) {
            return;
        }
        cacheInitialized = true;
        
        // 解析石头矿石自定义配置
        var stoneConfig = org.gloomybanana.nature_rebirth.Config.STONE_ORE_CUSTOM_LIST.get();
        stoneOreCache = new LinkedHashMap<>();
        if (!stoneConfig.isEmpty()) {
            stoneOreCache = parseCustomOreList(stoneConfig);
        }
        
        // 解析深板岩矿石自定义配置
        var deepslateConfig = org.gloomybanana.nature_rebirth.Config.DEEPSLATE_ORE_CUSTOM_LIST.get();
        deepslateOreCache = new LinkedHashMap<>();
        if (!deepslateConfig.isEmpty()) {
            deepslateOreCache = parseCustomOreList(deepslateConfig);
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
        
        // 将 mekanism 矿石添加到现有缓存
        if (stoneOreCache != null) {
            Map<String, Double> mekanismStoneMap = parseCustomOreList(java.util.Arrays.asList(mekanismStoneOres));
            stoneOreCache.putAll(mekanismStoneMap);
        }
        
        if (deepslateOreCache != null) {
            Map<String, Double> mekanismDeepslateMap = parseCustomOreList(java.util.Arrays.asList(mekanismDeepslateOres));
            deepslateOreCache.putAll(mekanismDeepslateMap);
        }
    }

    // 解析自定义矿石列表
    private static Map<String, Double> parseCustomOreList(List<? extends String> configList) {
        Map<String, Double> result = new LinkedHashMap<>();
        for (String entry : configList) {
            String[] parts = entry.split(":");
            if (parts.length == 2) {
                String oreName = parts[0].trim().toLowerCase();
                try {
                    double chance = Double.parseDouble(parts[1].trim());
                    result.put(oreName, chance);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return result;
    }

    // 辅助方法：根据概率选择矿石（基于自定义配置列表）
    private BlockState getOreFromCustomList(Level level, Map<String, Double> oreMap) {
        if (oreMap == null || oreMap.isEmpty()) {
            return null;
        }
        
        double rand = level.getRandom().nextDouble();
        double cumulative = 0.0;
        
        for (Map.Entry<String, Double> entry : oreMap.entrySet()) {
            cumulative += entry.getValue();
            if (rand < cumulative) {
                Block block = parseBlock(entry.getKey());
                if (block != null) {
                    return block.defaultBlockState();
                }
            }
        }
        
        return null;
    }

    // 辅助方法：解析方块名称（支持短名称和完整ID）
    private Block parseBlock(String name) {
        name = name.trim().toLowerCase();
        
        // 先尝试在预定义映射中查找（短名称）
        Block block = ORE_BLOCK_MAP.get(name);
        if (block != null) {
            return block;
        }
        
        // 尝试解析完整方块ID格式（modid:block_name）
        if (name.contains(":")) {
            try {
                // 通过注册表动态查找
                Identifier blockId = Identifier.parse(name);
                return BuiltInRegistries.BLOCK.getOptional(blockId).orElse(null);
            } catch (Exception ignored) {
                return null;
            }
        }
        
        return null;
    }

    // 辅助方法：根据概率选择石制矿石（基于配置文件）
    private BlockState getStoneOre(Level level) {
        initializeCustomOreCache();
        
        // 使用自定义配置列表
        if (stoneOreCache != null && !stoneOreCache.isEmpty()) {
            return getOreFromCustomList(level, stoneOreCache);
        }
        
        return null;
    }

    // 辅助方法：根据概率选择深板岩矿石（基于配置文件）
    private BlockState getDeepslateOre(Level level) {
        initializeCustomOreCache();
        
        // 使用自定义配置列表
        if (deepslateOreCache != null && !deepslateOreCache.isEmpty()) {
            return getOreFromCustomList(level, deepslateOreCache);
        }
        
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
