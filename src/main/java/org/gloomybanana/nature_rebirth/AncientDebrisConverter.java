package org.gloomybanana.nature_rebirth;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 远古残骸转化基岩机制：
 * - 在最大等级信标周围放置远古残骸时触发
 * - 显示吸收粒子效果
 * - 若干秒后（可配置），远古残骸转化成基岩后被破坏，以物品形式掉落
 */
@EventBusSubscriber(modid = NatureRebirth.MOD_ID)
public class AncientDebrisConverter {

    /**
     * 待转化的远古残骸信息
     */
    private static class PendingConversion {
        final BlockPos pos;
        final ResourceKey<Level> dimension;
        int remainingTicks;

        PendingConversion(BlockPos pos, ResourceKey<Level> dimension, int remainingTicks) {
            this.pos = pos;
            this.dimension = dimension;
            this.remainingTicks = remainingTicks;
        }
    }

    // 等待转化的远古残骸列表
    private static final Map<BlockPos, PendingConversion> pendingConversions = new HashMap<>();

    // 检测范围：检查远古残骸相邻的8个方块（水平方向，不包含垂直方向）
    private static final int BEACON_DETECTION_RANGE_XZ = 1;

    /**
     * 监听方块放置事件
     */
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        // 检查配置是否启用
        if (!Config.ANCIENT_DEBRIS_TO_BEDROCK.get()) {
            return;
        }

        net.minecraft.world.level.Level level = (net.minecraft.world.level.Level) event.getLevel();
        if (level.isClientSide()) {
            return;
        }

        BlockPos pos = event.getPos();
        
        // 使用 level.getBlockState 获取方块状态（与 ConduitCoralGenerator 保持一致）
        BlockState state = level.getBlockState(pos);
        
        // 检查是否放置的是远古残骸
        if (state.getBlock() != Blocks.ANCIENT_DEBRIS) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        ResourceKey<Level> dimension = serverLevel.dimension();

        // 检查周围是否有最大等级的信标
        if (hasMaxLevelBeaconNearby(serverLevel, pos)) {
            // 添加到待转化列表
            int delaySeconds = Config.ANCIENT_DEBRIS_CONVERSION_DELAY.get();
            int delayTicks = delaySeconds * 20; // 转换为游戏刻
            pendingConversions.put(pos.immutable(), new PendingConversion(pos.immutable(), dimension, delayTicks));
        }
    }

    /**
     * 监听服务器tick事件，处理转化逻辑
     */
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (pendingConversions.isEmpty()) {
            return;
        }

        // 使用迭代器安全删除
        Iterator<Map.Entry<BlockPos, PendingConversion>> iterator = pendingConversions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, PendingConversion> entry = iterator.next();
            BlockPos pos = entry.getKey();
            PendingConversion conversion = entry.getValue();

            // 获取正确维度的等级
            ServerLevel serverLevel = event.getServer().getLevel(conversion.dimension);
            if (serverLevel == null) {
                iterator.remove();
                continue;
            }

            int remainingTicks = conversion.remainingTicks - 1;

            // 播放吸收粒子效果
            spawnAbsorbParticles(serverLevel, pos);

            if (remainingTicks <= 0) {
                // 执行转化
                convertAncientDebris(serverLevel, pos);
                iterator.remove();
            } else {
                // 更新剩余tick
                conversion.remainingTicks = remainingTicks;
            }
        }
    }

    /**
     * 检查周围是否有最大等级的信标
     */
    private static boolean hasMaxLevelBeaconNearby(ServerLevel level, BlockPos centerPos) {
        // 在相邻的8个方块中搜索信标（水平方向，不包含垂直方向和中心位置）
        for (int dx = -BEACON_DETECTION_RANGE_XZ; dx <= BEACON_DETECTION_RANGE_XZ; dx++) {
            for (int dz = -BEACON_DETECTION_RANGE_XZ; dz <= BEACON_DETECTION_RANGE_XZ; dz++) {
                // 跳过中心位置
                if (dx == 0 && dz == 0) {
                    continue;
                }
                BlockPos beaconPos = centerPos.offset(dx, 0, dz);
                BlockState state = level.getBlockState(beaconPos);
                
                if (state.is(Blocks.BEACON)) {
                    BlockEntity blockEntity = level.getBlockEntity(beaconPos);
                    if (blockEntity instanceof BeaconBlockEntity beaconEntity) {
                        // 获取信标等级，使用反射
                        try {
                            java.lang.reflect.Field levelsField = BeaconBlockEntity.class.getDeclaredField("levels");
                            levelsField.setAccessible(true);
                            int levels = levelsField.getInt(beaconEntity);
                            if (levels >= 4) {
                                return true;
                            }
                        } catch (NoSuchFieldException e) {
                            // 尝试其他方式获取信标等级
                            return tryGetBeaconLevelAlternative(beaconEntity);
                        } catch (Exception e) {
                            // 尝试其他方式获取信标等级
                            return tryGetBeaconLevelAlternative(beaconEntity);
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * 备用方法：尝试通过其他方式获取信标等级
     */
    private static boolean tryGetBeaconLevelAlternative(BeaconBlockEntity beaconEntity) {
        try {
            // 尝试使用 getLevels() 方法
            java.lang.reflect.Method getLevelsMethod = BeaconBlockEntity.class.getDeclaredMethod("getLevels");
            getLevelsMethod.setAccessible(true);
            int levels = (int) getLevelsMethod.invoke(beaconEntity);
            NatureRebirth.LOGGER.info("Beacon level via getLevels(): {}", levels);
            return levels >= 4;
        } catch (Exception e) {
            NatureRebirth.LOGGER.warn("Failed to get beacon level via getLevels() method: {}", e.getMessage());
        }
        
        // 尝试检查信标是否有 active 状态
        try {
            java.lang.reflect.Field activeField = BeaconBlockEntity.class.getDeclaredField("isActive");
            activeField.setAccessible(true);
            boolean isActive = activeField.getBoolean(beaconEntity);
            NatureRebirth.LOGGER.info("Beacon isActive: {}", isActive);
            // 如果信标激活，假设它可能是最大等级
            return isActive;
        } catch (Exception e) {
            NatureRebirth.LOGGER.warn("Failed to check beacon active status: {}", e.getMessage());
        }
        
        return false;
    }

    /**
     * 生成吸收粒子效果
     */
    private static void spawnAbsorbParticles(ServerLevel level, BlockPos pos) {
        RandomSource random = level.getRandom();
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        // 生成向内吸收的粒子效果（使用紫色粒子模拟能量吸收）- 减少到20%
        for (int i = 0; i < 1; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 2.0;
            double offsetY = (random.nextDouble() - 0.5) * 2.0;
            double offsetZ = (random.nextDouble() - 0.5) * 2.0;
            
            double distance = Math.sqrt(offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ);
            if (distance > 0) {
                // 粒子向中心移动
                double motionX = -offsetX / distance * 0.1;
                double motionY = -offsetY / distance * 0.1;
                double motionZ = -offsetZ / distance * 0.1;

                level.sendParticles(
                    ParticleTypes.REVERSE_PORTAL,
                    x + offsetX, y + offsetY, z + offsetZ,
                    1, motionX, motionY, motionZ, 0
                );
            }
        }

        // 生成一些紫色烟雾粒子（使用 END_ROD 替代 ENTITY_EFFECT）- 减少到20%
        level.sendParticles(
            ParticleTypes.END_ROD,
            x, y + 0.5, z,
            1, 0.3, 0.3, 0.3, 0.05
        );
    }

    /**
     * 执行远古残骸转化基岩并破坏
     */
    private static void convertAncientDebris(ServerLevel level, BlockPos pos) {
        // 检查位置是否仍然是远古残骸（防止被玩家提前破坏）
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() != Blocks.ANCIENT_DEBRIS) {
            return;
        }

        // 1. 播放转化音效
        level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0f, 1.0f);

        // 2. 生成大量粒子效果
        spawnConversionParticles(level, pos);

        // 3. 移除远古残骸
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

        // 4. 直接生成基岩物品掉落（基岩本身不会掉落，需要手动生成）
        net.minecraft.world.item.ItemStack bedrockStack = new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.BEDROCK);
        net.minecraft.world.entity.item.ItemEntity itemEntity = new net.minecraft.world.entity.item.ItemEntity(
            level,
            pos.getX() + 0.5,
            pos.getY() + 0.5,
            pos.getZ() + 0.5,
            bedrockStack
        );
        level.addFreshEntity(itemEntity);

        // 播放破坏音效
        level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
    }

    /**
     * 生成转化时的粒子效果
     */
    private static void spawnConversionParticles(ServerLevel level, BlockPos pos) {
        RandomSource random = level.getRandom();
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        // 生成爆炸式的粒子效果 - 减少到20%
        for (int i = 0; i < 4; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 2.0;
            double offsetY = (random.nextDouble() - 0.5) * 2.0;
            double offsetZ = (random.nextDouble() - 0.5) * 2.0;

            level.sendParticles(
                ParticleTypes.REVERSE_PORTAL,
                x, y, z,
                1, offsetX * 0.5, offsetY * 0.5, offsetZ * 0.5, 0.1
            );
        }

        // 生成末影粒子 - 减少到20%
        for (int i = 0; i < 3; i++) {
            level.sendParticles(
                ParticleTypes.END_ROD,
                x, y, z,
                1,
                (random.nextDouble() - 0.5) * 1.5,
                (random.nextDouble() - 0.5) * 1.5,
                (random.nextDouble() - 0.5) * 1.5,
                0.1
            );
        }

        // 生成烟雾粒子 - 减少到20%
        level.sendParticles(
            ParticleTypes.LARGE_SMOKE,
            x, y + 1, z,
            1, 0.5, 0.5, 0.5, 0.1
        );
    }
}