package org.gloomybanana.nature_rebirth;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Map;

/**
 * 龙息转化石头机制：
 * - 龙息右键圆石/石头 → 末地石
 * - 龙息右键石砖类方块 → 对应末地石版本
 * - 使用后返还玻璃瓶
 * - 右键点击时播放左键挥手动画
 */
@EventBusSubscriber(modid = NatureRebirth.MOD_ID)
public class DragonBreathStoneConverter {

    // 方块转化映射
    private static final Map<Block, Block> BLOCK_CONVERSION_MAP = Map.of(
            // 基础石头
            Blocks.COBBLESTONE, Blocks.END_STONE,
            Blocks.STONE, Blocks.END_STONE,
            
            // 石砖系列（注意：末地石砖没有变体）
            Blocks.STONE_BRICKS, Blocks.END_STONE_BRICKS,
            Blocks.CRACKED_STONE_BRICKS, Blocks.END_STONE_BRICKS,
            Blocks.MOSSY_STONE_BRICKS, Blocks.END_STONE_BRICKS,
            Blocks.CHISELED_STONE_BRICKS, Blocks.END_STONE_BRICKS
    );

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack itemStack = event.getItemStack();
        Player player = event.getEntity();
        
        // 检查是否手持龙息
        if (itemStack.getItem() != Items.DRAGON_BREATH) {
            return;
        }
        
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        
        // 尝试转化方块
        if (tryConvertBlock(level, pos, state, block, itemStack, player)) {
            // 在客户端播放攻击动画（强制使用主手）
            if (level.isClientSide()) {
                player.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
            }
            event.setCanceled(true);
        }
    }

    private static boolean tryConvertBlock(Level level, BlockPos pos, BlockState state, Block block, ItemStack itemStack, Player player) {
        BlockState newState = null;
        
        // 处理基础方块转化
        if (BLOCK_CONVERSION_MAP.containsKey(block)) {
            Block resultBlock = BLOCK_CONVERSION_MAP.get(block);
            newState = resultBlock.defaultBlockState();
        }
        // 处理楼梯方块
        else if (block instanceof StairBlock && block == Blocks.STONE_BRICK_STAIRS) {
            newState = Blocks.END_STONE_BRICK_STAIRS.defaultBlockState()
                    .setValue(StairBlock.FACING, state.getValue(StairBlock.FACING))
                    .setValue(StairBlock.HALF, state.getValue(StairBlock.HALF))
                    .setValue(StairBlock.SHAPE, state.getValue(StairBlock.SHAPE));
        }
        // 处理台阶方块
        else if (block == Blocks.STONE_BRICK_SLAB) {
            newState = Blocks.END_STONE_BRICK_SLAB.defaultBlockState()
                    .setValue(net.minecraft.world.level.block.SlabBlock.TYPE, state.getValue(net.minecraft.world.level.block.SlabBlock.TYPE))
                    .setValue(net.minecraft.world.level.block.SlabBlock.WATERLOGGED, state.getValue(net.minecraft.world.level.block.SlabBlock.WATERLOGGED));
        }
        // 处理墙方块
        else if (block instanceof WallBlock && block == Blocks.STONE_BRICK_WALL) {
            newState = Blocks.END_STONE_BRICK_WALL.defaultBlockState()
                    .setValue(WallBlock.NORTH, state.getValue(WallBlock.NORTH))
                    .setValue(WallBlock.EAST, state.getValue(WallBlock.EAST))
                    .setValue(WallBlock.SOUTH, state.getValue(WallBlock.SOUTH))
                    .setValue(WallBlock.WEST, state.getValue(WallBlock.WEST))
                    .setValue(WallBlock.UP, state.getValue(WallBlock.UP))
                    .setValue(WallBlock.WATERLOGGED, state.getValue(WallBlock.WATERLOGGED));
        }
        
        // 如果找到了可转化的方块
        if (newState != null && !level.isClientSide()) {
            // 设置新方块
            level.setBlock(pos, newState, 3);
            
            // 消耗龙息
            itemStack.shrink(1);
            
            // 返还玻璃瓶
            giveGlassBottle(player);
            
            // 播放柔和的龙息吹息音效
            level.playSound(null, pos, SoundEvents.ENDER_EYE_DEATH, SoundSource.BLOCKS, 0.3F, 1.2F);
            
            // 生成紫色粒子效果
            spawnPurpleParticles(level, pos);
            
            return true;
        }
        
        return false;
    }

    private static void giveGlassBottle(Player player) {
        ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE);
        if (!player.getInventory().add(glassBottle)) {
            // 如果背包满了，掉落在地上
            player.drop(glassBottle, false);
        }
    }

    private static void spawnPurpleParticles(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        
        RandomSource random = level.getRandom();
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        
        // 生成大量紫色粒子
        for (int i = 0; i < 50; i++) {
            double dx = (random.nextDouble() - 0.5) * 0.8;
            double dy = random.nextDouble() * 0.8;
            double dz = (random.nextDouble() - 0.5) * 0.8;
            
            serverLevel.sendParticles(
                net.minecraft.core.particles.ParticleTypes.REVERSE_PORTAL,
                x,
                y,
                z,
                1,
                dx,
                dy,
                dz,
                0.02
            );
        }
        
        // 再加一圈扩散的紫色粒子
        for (int i = 0; i < 20; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double radius = 0.3 + random.nextDouble() * 0.5;
            double px = x + Math.cos(angle) * radius;
            double pz = z + Math.sin(angle) * radius;
            
            serverLevel.sendParticles(
                net.minecraft.core.particles.ParticleTypes.END_ROD,
                px,
                y,
                pz,
                1,
                (random.nextDouble() - 0.5) * 0.1,
                random.nextDouble() * 0.2,
                (random.nextDouble() - 0.5) * 0.1,
                0.01
            );
        }
    }
}