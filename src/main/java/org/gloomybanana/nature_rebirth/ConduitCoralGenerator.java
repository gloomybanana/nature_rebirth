package org.gloomybanana.nature_rebirth;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * 珊瑚生成和复活机制
 * - 在潮涌核心范围内使用骨粉生成珊瑚
 * - 失活珊瑚在潮涌核心范围内自动复活
 * - 性能优化：只在珊瑚放置时检测复活条件
 */
@EventBusSubscriber(modid = NatureRebirth.MOD_ID)
public class ConduitCoralGenerator {
    
    // 珊瑚方块类型（用于生成）
    private static final Block[] CORALS = {
        Blocks.BRAIN_CORAL,
        Blocks.TUBE_CORAL,
        Blocks.BUBBLE_CORAL,
        Blocks.HORN_CORAL,
        Blocks.FIRE_CORAL
    };
    
    // 珊瑚扇类型
    private static final Block[] CORAL_FANS = {
        Blocks.BRAIN_CORAL_FAN,
        Blocks.TUBE_CORAL_FAN,
        Blocks.BUBBLE_CORAL_FAN,
        Blocks.HORN_CORAL_FAN,
        Blocks.FIRE_CORAL_FAN
    };
    
    // 墙上珊瑚扇类型
    private static final Block[] WALL_CORAL_FANS = {
        Blocks.BRAIN_CORAL_WALL_FAN,
        Blocks.TUBE_CORAL_WALL_FAN,
        Blocks.BUBBLE_CORAL_WALL_FAN,
        Blocks.HORN_CORAL_WALL_FAN,
        Blocks.FIRE_CORAL_WALL_FAN
    };
    
    // 失活珊瑚到活珊瑚的映射
    private static final Map<Block, Block> DEAD_CORAL_TO_LIVE = new HashMap<>();
    
    static {
        // 失活珊瑚方块 -> 活珊瑚方块
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_BRAIN_CORAL, Blocks.BRAIN_CORAL);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_TUBE_CORAL, Blocks.TUBE_CORAL);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_BUBBLE_CORAL, Blocks.BUBBLE_CORAL);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_HORN_CORAL, Blocks.HORN_CORAL);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_FIRE_CORAL, Blocks.FIRE_CORAL);
        
        // 失活珊瑚方块 -> 活珊瑚方块
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.BRAIN_CORAL_BLOCK);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_TUBE_CORAL_BLOCK, Blocks.TUBE_CORAL_BLOCK);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.BUBBLE_CORAL_BLOCK);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.HORN_CORAL_BLOCK);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.FIRE_CORAL_BLOCK);
        
        // 失活珊瑚扇 -> 活珊瑚扇
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.BRAIN_CORAL_FAN);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_TUBE_CORAL_FAN, Blocks.TUBE_CORAL_FAN);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.BUBBLE_CORAL_FAN);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_HORN_CORAL_FAN, Blocks.HORN_CORAL_FAN);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_FIRE_CORAL_FAN, Blocks.FIRE_CORAL_FAN);
        
        // 失活墙上珊瑚扇 -> 活墙上珊瑚扇
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_BRAIN_CORAL_WALL_FAN, Blocks.BRAIN_CORAL_WALL_FAN);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_TUBE_CORAL_WALL_FAN, Blocks.TUBE_CORAL_WALL_FAN);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_BUBBLE_CORAL_WALL_FAN, Blocks.BUBBLE_CORAL_WALL_FAN);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_HORN_CORAL_WALL_FAN, Blocks.HORN_CORAL_WALL_FAN);
        DEAD_CORAL_TO_LIVE.put(Blocks.DEAD_FIRE_CORAL_WALL_FAN, Blocks.FIRE_CORAL_WALL_FAN);
    }
    
    // 正在复活的珊瑚（位置 -> 剩余tick数）
    private static final Map<BlockPos, Integer> REVIVING_CORALS = new HashMap<>();
    
    /**
     * 玩家使用骨粉生成珊瑚 / 放置珊瑚时启动复活
     */
    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos clickedPos = event.getPos();
        
        if (level.isClientSide()) {
            return;
        }
        
        // 1. 处理骨粉生成珊瑚
        if (event.getItemStack().is(net.minecraft.world.item.Items.BONE_MEAL)) {
            // 检查玩家是否在水下
            if (!level.getFluidState(player.blockPosition()).is(net.minecraft.world.level.material.Fluids.WATER)) {
                return;
            }
            
            // 检查是否在潮涌核心效果范围内
            if (!isInConduitRange(level, clickedPos)) {
                return;
            }
            
            // 获取点击的方块状态
            BlockState targetState = level.getBlockState(clickedPos);
            
            // 检查是否是完整方块
            if (!isFullBlock(targetState)) {
                return;
            }
            
            // 生成珊瑚
            RandomSource random = level.getRandom();
            int generated = 0;
            
            // 在3x3范围内生成
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    // 根据距离中心的距离计算概率
                    int distance = Math.abs(dx) + Math.abs(dz);
                    double chance = switch (distance) {
                        case 0 -> 0.9;
                        case 1 -> 0.6;
                        case 2 -> 0.3;
                        default -> 0.0;
                    };
                    
                    if (random.nextDouble() >= chance) {
                        continue;
                    }
                    
                    BlockPos targetPos = clickedPos.offset(dx, 0, dz);
                    BlockPos abovePos = targetPos.above();
                    
                    // 检查目标位置是否是完整方块
                    if (!isFullBlock(level.getBlockState(targetPos))) {
                        continue;
                    }
                    
                    // 检查上方是否可以放置
                    if (!canPlaceCoral(level, abovePos)) {
                        continue;
                    }
                    
                    // 50%概率生成珊瑚，50%概率生成珊瑚扇
                    Block coral;
                    if (random.nextBoolean()) {
                        coral = CORALS[random.nextInt(CORALS.length)];
                    } else {
                        coral = CORAL_FANS[random.nextInt(CORAL_FANS.length)];
                    }
                    
                    level.setBlockAndUpdate(abovePos, coral.defaultBlockState());
                    generated++;
                }
            }
            
            // 如果生成了珊瑚，添加音效和粒子特效
            if (generated > 0) {
                // 消耗骨粉
                if (!player.isCreative()) {
                    event.getItemStack().shrink(1);
                }
                
                // 播放音效
                level.playSound(null, clickedPos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                
                // 生成粒子效果
                spawnBoneMealParticles(level, clickedPos);
                
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
        
        // 2. 处理放置墙上珊瑚扇（通过拉弓取消实现）
        // 检查是否是墙壁珊瑚扇方块
        Block heldBlock = Block.byItem(event.getItemStack().getItem());
        if (heldBlock != null) {
            for (Block wallFan : WALL_CORAL_FANS) {
                if (heldBlock == wallFan) {
                    // 检查是否在潮涌核心范围内
                    if (!isInConduitRange(level, clickedPos)) {
                        return;
                    }
                    
                    // 检查目标方块是否是完整方块
                    BlockState targetState = level.getBlockState(clickedPos);
                    if (!isFullBlock(targetState)) {
                        return;
                    }
                    
                    // 获取点击方向（朝向）
                    net.minecraft.core.Direction facing = event.getFace();
                    if (facing == null) {
                        return;
                    }
                    
                    // 生成墙上珊瑚扇
                    level.setBlockAndUpdate(clickedPos.relative(facing), wallFan.defaultBlockState());
                    
                    // 消耗物品
                    if (!player.isCreative()) {
                        event.getItemStack().shrink(1);
                    }
                    
                    // 播放音效和粒子
                    level.playSound(null, clickedPos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    spawnBoneMealParticles(level, clickedPos);
                    
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    return;
                }
            }
        }
    }
    
    /**
     * 玩家放置方块后检测
     */
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        
        if (level.isClientSide()) {
            return;
        }
        
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        
        // 检查是否是失活珊瑚
        if (!DEAD_CORAL_TO_LIVE.containsKey(block)) {
            return;
        }
        
        // 检查是否在潮涌核心范围内且不在复活中
        if (REVIVING_CORALS.containsKey(pos)) {
            return;
        }
        
        if (isInConduitRange(level, pos)) {
            RandomSource random = level.getRandom();
            int ticks = 20 + random.nextInt(41); // 1-3秒
            REVIVING_CORALS.put(pos.immutable(), ticks);
        }
    }
    
    /**
     * 每tick更新复活中的珊瑚
     */
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Pre event) {
        Level level = event.getLevel();
        
        if (level.isClientSide() || REVIVING_CORALS.isEmpty()) {
            return;
        }
        
        RandomSource random = level.getRandom();
        
        // 使用迭代器安全删除
        REVIVING_CORALS.entrySet().removeIf(entry -> {
            BlockPos pos = entry.getKey();
            int remainingTicks = entry.getValue() - 1;
            
            if (remainingTicks <= 0) {
                // 复活完成
                Block deadCoral = level.getBlockState(pos).getBlock();
                Block liveCoral = DEAD_CORAL_TO_LIVE.get(deadCoral);
                
                if (liveCoral != null) {
                    BlockState newState = liveCoral.defaultBlockState();
                    
                    // 复制状态属性（如朝向）
                    BlockState oldState = level.getBlockState(pos);
                    if (oldState.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING)) {
                        newState = newState.setValue(
                            net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING,
                            oldState.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING)
                        );
                    }
                    
                    level.setBlockAndUpdate(pos, newState);
                    
                    // 播放复活音效
                    level.playSound(null, pos, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS, 0.5F, 2.0F);
                    
                    // 生成粒子效果（服务端发送）
                    if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                        for (int i = 0; i < 15; i++) {
                            double dx = (random.nextDouble() - 0.5) * 0.8;
                            double dy = random.nextDouble() * 0.8;
                            double dz = (random.nextDouble() - 0.5) * 0.8;
                            serverLevel.sendParticles(
                                ParticleTypes.HAPPY_VILLAGER,
                                pos.getX() + 0.5,
                                pos.getY() + 0.5,
                                pos.getZ() + 0.5,
                                1, dx, dy, dz, 0.1
                            );
                        }
                    }
                }
                return true;
            }
            
            entry.setValue(remainingTicks);
            
            // 复活过程中生成粒子效果（服务端发送）
            if (random.nextDouble() < 0.5) {
                if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                    double dx = (random.nextDouble() - 0.5) * 0.4;
                    double dy = random.nextDouble() * 0.3;
                    double dz = (random.nextDouble() - 0.5) * 0.4;
                    serverLevel.sendParticles(
                        ParticleTypes.BUBBLE,
                        pos.getX() + 0.5,
                        pos.getY() + 0.3,
                        pos.getZ() + 0.5,
                        1, dx, dy, dz, 0.05
                    );
                }
            }
            
            return false;
        });
    }
    
    /**
     * 检查是否在潮涌核心范围内
     */
    private static boolean isInConduitRange(Level level, BlockPos pos) {
        BlockPos nearbyConduit = findNearbyConduit(level, pos);
        return nearbyConduit != null;
    }
    
    /**
     * 查找附近是否有激活的潮涌核心
     */
    private static BlockPos findNearbyConduit(Level level, BlockPos pos) {
        // 在玩家/珊瑚位置附近搜索（潮涌核心范围96格）
        int searchRadius = 96;
        
        for (int x = pos.getX() - searchRadius; x <= pos.getX() + searchRadius; x++) {
            for (int y = pos.getY() - searchRadius; y <= pos.getY() + searchRadius; y++) {
                for (int z = pos.getZ() - searchRadius; z <= pos.getZ() + searchRadius; z++) {
                    BlockPos checkPos = new BlockPos(x, y, z);
                    if (level.getBlockState(checkPos).is(Blocks.CONDUIT)) {
                        // 检查是否在 96 格范围内
                        if (checkPos.closerThan(pos, 96)) {
                            return checkPos;
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * 检查方块是否是完整方块
     */
    private static boolean isFullBlock(BlockState state) {
        return state.isSolid() && !state.is(Blocks.WATER) && !state.is(Blocks.LAVA);
    }
    
    /**
     * 检查位置是否可以放置珊瑚
     */
    private static boolean canPlaceCoral(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return !state.isSolid() && level.getFluidState(pos).is(net.minecraft.world.level.material.Fluids.WATER);
    }
    
    /**
     * 生成骨粉粒子效果
     */
    private static void spawnBoneMealParticles(Level level, BlockPos pos) {
        RandomSource random = level.getRandom();
        
        for (int i = 0; i < 30; i++) {
            level.addParticle(
                ParticleTypes.COMPOSTER,
                pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 1.5,
                pos.getY() + 0.5 + random.nextDouble() * 2.0,
                pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 1.5,
                (random.nextDouble() - 0.5) * 0.4,
                random.nextDouble() * 0.4 + 0.1,
                (random.nextDouble() - 0.5) * 0.4
            );
        }
    }
}