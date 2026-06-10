package org.gloomybanana.nature_rebirth;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.*;

public class CustomGenerationConfig {
    
    // 生成规则类
    public static class GenerationRule {
        public final Block inputBlock;
        public final Block outputBlock;
        public final Set<Block> requiredAdjacentBlocks;
        
        public GenerationRule(Block inputBlock, Block outputBlock, Set<Block> requiredAdjacentBlocks) {
            this.inputBlock = inputBlock;
            this.outputBlock = outputBlock;
            this.requiredAdjacentBlocks = requiredAdjacentBlocks;
        }
    }
    
    // 预定义的方块映射（方块名称 -> Block引用）
    private static final Map<String, Block> BLOCK_MAP = new HashMap<>();
    
    static {
        // 初始化方块映射
        BLOCK_MAP.put("air", Blocks.AIR);
        BLOCK_MAP.put("stone", Blocks.STONE);
        BLOCK_MAP.put("granite", Blocks.GRANITE);
        BLOCK_MAP.put("diorite", Blocks.DIORITE);
        BLOCK_MAP.put("andesite", Blocks.ANDESITE);
        BLOCK_MAP.put("deepslate", Blocks.DEEPSLATE);
        BLOCK_MAP.put("cobblestone", Blocks.COBBLESTONE);
        BLOCK_MAP.put("cobbled_deepslate", Blocks.COBBLED_DEEPSLATE);
        BLOCK_MAP.put("oak_planks", Blocks.OAK_PLANKS);
        BLOCK_MAP.put("spruce_planks", Blocks.SPRUCE_PLANKS);
        BLOCK_MAP.put("birch_planks", Blocks.BIRCH_PLANKS);
        BLOCK_MAP.put("jungle_planks", Blocks.JUNGLE_PLANKS);
        BLOCK_MAP.put("acacia_planks", Blocks.ACACIA_PLANKS);
        BLOCK_MAP.put("dark_oak_planks", Blocks.DARK_OAK_PLANKS);
        BLOCK_MAP.put("mangrove_planks", Blocks.MANGROVE_PLANKS);
        BLOCK_MAP.put("cherry_planks", Blocks.CHERRY_PLANKS);
        BLOCK_MAP.put("bamboo_planks", Blocks.BAMBOO_PLANKS);
        BLOCK_MAP.put("bamboo_mosaic", Blocks.BAMBOO_MOSAIC);
        BLOCK_MAP.put("oak_log", Blocks.OAK_LOG);
        BLOCK_MAP.put("spruce_log", Blocks.SPRUCE_LOG);
        BLOCK_MAP.put("birch_log", Blocks.BIRCH_LOG);
        BLOCK_MAP.put("jungle_log", Blocks.JUNGLE_LOG);
        BLOCK_MAP.put("acacia_log", Blocks.ACACIA_LOG);
        BLOCK_MAP.put("dark_oak_log", Blocks.DARK_OAK_LOG);
        BLOCK_MAP.put("mangrove_log", Blocks.MANGROVE_LOG);
        BLOCK_MAP.put("cherry_log", Blocks.CHERRY_LOG);
        BLOCK_MAP.put("oak_wood", Blocks.OAK_WOOD);
        BLOCK_MAP.put("spruce_wood", Blocks.SPRUCE_WOOD);
        BLOCK_MAP.put("birch_wood", Blocks.BIRCH_WOOD);
        BLOCK_MAP.put("jungle_wood", Blocks.JUNGLE_WOOD);
        BLOCK_MAP.put("acacia_wood", Blocks.ACACIA_WOOD);
        BLOCK_MAP.put("dark_oak_wood", Blocks.DARK_OAK_WOOD);
        BLOCK_MAP.put("mangrove_wood", Blocks.MANGROVE_WOOD);
        BLOCK_MAP.put("cherry_wood", Blocks.CHERRY_WOOD);
        BLOCK_MAP.put("stripped_oak_log", Blocks.STRIPPED_OAK_LOG);
        BLOCK_MAP.put("stripped_spruce_log", Blocks.STRIPPED_SPRUCE_LOG);
        BLOCK_MAP.put("stripped_birch_log", Blocks.STRIPPED_BIRCH_LOG);
        BLOCK_MAP.put("stripped_jungle_log", Blocks.STRIPPED_JUNGLE_LOG);
        BLOCK_MAP.put("stripped_acacia_log", Blocks.STRIPPED_ACACIA_LOG);
        BLOCK_MAP.put("stripped_dark_oak_log", Blocks.STRIPPED_DARK_OAK_LOG);
        BLOCK_MAP.put("stripped_mangrove_log", Blocks.STRIPPED_MANGROVE_LOG);
        BLOCK_MAP.put("stripped_cherry_log", Blocks.STRIPPED_CHERRY_LOG);
        BLOCK_MAP.put("stripped_oak_wood", Blocks.STRIPPED_OAK_WOOD);
        BLOCK_MAP.put("stripped_spruce_wood", Blocks.STRIPPED_SPRUCE_WOOD);
        BLOCK_MAP.put("stripped_birch_wood", Blocks.STRIPPED_BIRCH_WOOD);
        BLOCK_MAP.put("stripped_jungle_wood", Blocks.STRIPPED_JUNGLE_WOOD);
        BLOCK_MAP.put("stripped_acacia_wood", Blocks.STRIPPED_ACACIA_WOOD);
        BLOCK_MAP.put("stripped_dark_oak_wood", Blocks.STRIPPED_DARK_OAK_WOOD);
        BLOCK_MAP.put("stripped_mangrove_wood", Blocks.STRIPPED_MANGROVE_WOOD);
        BLOCK_MAP.put("stripped_cherry_wood", Blocks.STRIPPED_CHERRY_WOOD);
        BLOCK_MAP.put("sand", Blocks.SAND);
        BLOCK_MAP.put("red_sand", Blocks.RED_SAND);
        BLOCK_MAP.put("gravel", Blocks.GRAVEL);
        BLOCK_MAP.put("sandstone", Blocks.SANDSTONE);
        BLOCK_MAP.put("cut_sandstone", Blocks.CUT_SANDSTONE);
        BLOCK_MAP.put("chiseled_sandstone", Blocks.CHISELED_SANDSTONE);
        BLOCK_MAP.put("red_sandstone", Blocks.RED_SANDSTONE);
        BLOCK_MAP.put("cut_red_sandstone", Blocks.CUT_RED_SANDSTONE);
        BLOCK_MAP.put("chiseled_red_sandstone", Blocks.CHISELED_RED_SANDSTONE);
        BLOCK_MAP.put("clay", Blocks.CLAY);
        BLOCK_MAP.put("dirt", Blocks.DIRT);
        BLOCK_MAP.put("coarse_dirt", Blocks.COARSE_DIRT);
        BLOCK_MAP.put("rooted_dirt", Blocks.ROOTED_DIRT);
        BLOCK_MAP.put("grass_block", Blocks.GRASS_BLOCK);
        BLOCK_MAP.put("podzol", Blocks.PODZOL);
        BLOCK_MAP.put("mycelium", Blocks.MYCELIUM);
        BLOCK_MAP.put("snow_block", Blocks.SNOW_BLOCK);
        BLOCK_MAP.put("ice", Blocks.ICE);
        BLOCK_MAP.put("packed_ice", Blocks.PACKED_ICE);
        BLOCK_MAP.put("blue_ice", Blocks.BLUE_ICE);
        BLOCK_MAP.put("snow", Blocks.SNOW);
        BLOCK_MAP.put("grass", Blocks.SHORT_GRASS);
        BLOCK_MAP.put("fern", Blocks.FERN);
        BLOCK_MAP.put("dead_bush", Blocks.DEAD_BUSH);
        BLOCK_MAP.put("seagrass", Blocks.SEAGRASS);
        BLOCK_MAP.put("kelp", Blocks.KELP);
        BLOCK_MAP.put("vine", Blocks.VINE);
        BLOCK_MAP.put("glow_lichen", Blocks.GLOW_LICHEN);
        BLOCK_MAP.put("lily_pad", Blocks.LILY_PAD);
        BLOCK_MAP.put("sugar_cane", Blocks.SUGAR_CANE);
        BLOCK_MAP.put("cactus", Blocks.CACTUS);
        BLOCK_MAP.put("bamboo", Blocks.BAMBOO);
        BLOCK_MAP.put("oak_sapling", Blocks.OAK_SAPLING);
        BLOCK_MAP.put("spruce_sapling", Blocks.SPRUCE_SAPLING);
        BLOCK_MAP.put("birch_sapling", Blocks.BIRCH_SAPLING);
        BLOCK_MAP.put("jungle_sapling", Blocks.JUNGLE_SAPLING);
        BLOCK_MAP.put("acacia_sapling", Blocks.ACACIA_SAPLING);
        BLOCK_MAP.put("dark_oak_sapling", Blocks.DARK_OAK_SAPLING);
        BLOCK_MAP.put("mangrove_propagule", Blocks.MANGROVE_PROPAGULE);
        BLOCK_MAP.put("cherry_sapling", Blocks.CHERRY_SAPLING);
        BLOCK_MAP.put("bedrock", Blocks.BEDROCK);
        BLOCK_MAP.put("netherrack", Blocks.NETHERRACK);
        BLOCK_MAP.put("nether_bricks", Blocks.NETHER_BRICK_STAIRS);
        BLOCK_MAP.put("cracked_nether_bricks", Blocks.CRACKED_NETHER_BRICKS);
        BLOCK_MAP.put("chiseled_nether_bricks", Blocks.CHISELED_NETHER_BRICKS);
        BLOCK_MAP.put("nether_brick_fence", Blocks.NETHER_BRICK_FENCE);
        BLOCK_MAP.put("quartz_block", Blocks.QUARTZ_BLOCK);
        BLOCK_MAP.put("chiseled_quartz_block", Blocks.CHISELED_QUARTZ_BLOCK);
        BLOCK_MAP.put("quartz_pillar", Blocks.QUARTZ_PILLAR);
        BLOCK_MAP.put("smooth_quartz", Blocks.SMOOTH_QUARTZ);
        BLOCK_MAP.put("quartz_bricks", Blocks.QUARTZ_BRICKS);
        BLOCK_MAP.put("basalt", Blocks.BASALT);
        BLOCK_MAP.put("polished_basalt", Blocks.POLISHED_BASALT);
        BLOCK_MAP.put("blackstone", Blocks.BLACKSTONE);
        BLOCK_MAP.put("polished_blackstone", Blocks.POLISHED_BLACKSTONE);
        BLOCK_MAP.put("polished_blackstone_bricks", Blocks.POLISHED_BLACKSTONE_BRICKS);
        BLOCK_MAP.put("cracked_polished_blackstone_bricks", Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
        BLOCK_MAP.put("chiseled_polished_blackstone", Blocks.CHISELED_POLISHED_BLACKSTONE);
        BLOCK_MAP.put("gilded_blackstone", Blocks.GILDED_BLACKSTONE);
        BLOCK_MAP.put("soul_sand", Blocks.SOUL_SAND);
        BLOCK_MAP.put("soul_soil", Blocks.SOUL_SOIL);
        BLOCK_MAP.put("bone_block", Blocks.BONE_BLOCK);
        BLOCK_MAP.put("dried_kelp_block", Blocks.DRIED_KELP_BLOCK);
        BLOCK_MAP.put("moss_block", Blocks.MOSS_BLOCK);
        BLOCK_MAP.put("moss_carpet", Blocks.MOSS_CARPET);
        BLOCK_MAP.put("mud", Blocks.MUD);
        BLOCK_MAP.put("clay", Blocks.CLAY);
        BLOCK_MAP.put("packed_mud", Blocks.PACKED_MUD);
        BLOCK_MAP.put("mud_bricks", Blocks.MUD_BRICKS);
        BLOCK_MAP.put("mud_brick_slab", Blocks.MUD_BRICK_SLAB);
        BLOCK_MAP.put("mud_brick_stairs", Blocks.MUD_BRICK_STAIRS);
        BLOCK_MAP.put("calcite", Blocks.CALCITE);
        BLOCK_MAP.put("tuff", Blocks.TUFF);
        BLOCK_MAP.put("tuff_bricks", Blocks.TUFF_BRICKS);
        BLOCK_MAP.put("chiseled_tuff", Blocks.CHISELED_TUFF);
        BLOCK_MAP.put("polished_tuff", Blocks.POLISHED_TUFF);
        BLOCK_MAP.put("chiseled_polished_tuff", Blocks.CHISELED_TUFF_BRICKS);
        BLOCK_MAP.put("dripstone_block", Blocks.DRIPSTONE_BLOCK);
        BLOCK_MAP.put("pointed_dripstone", Blocks.POINTED_DRIPSTONE);
        BLOCK_MAP.put("cobblestone_stairs", Blocks.COBBLESTONE_STAIRS);
        BLOCK_MAP.put("cobblestone_slab", Blocks.COBBLESTONE_SLAB);
        BLOCK_MAP.put("cobblestone_wall", Blocks.COBBLESTONE_WALL);
        BLOCK_MAP.put("mossy_cobblestone", Blocks.MOSSY_COBBLESTONE);
        BLOCK_MAP.put("mossy_cobblestone_stairs", Blocks.MOSSY_COBBLESTONE_STAIRS);
        BLOCK_MAP.put("mossy_cobblestone_slab", Blocks.MOSSY_COBBLESTONE_SLAB);
        BLOCK_MAP.put("mossy_cobblestone_wall", Blocks.MOSSY_COBBLESTONE_WALL);
        BLOCK_MAP.put("smooth_stone", Blocks.SMOOTH_STONE);
        BLOCK_MAP.put("smooth_sandstone", Blocks.SMOOTH_SANDSTONE);
        BLOCK_MAP.put("smooth_red_sandstone", Blocks.SMOOTH_RED_SANDSTONE);
        BLOCK_MAP.put("smooth_quartz", Blocks.SMOOTH_QUARTZ);
        BLOCK_MAP.put("brick", Blocks.BRICKS);
        BLOCK_MAP.put("bricks", Blocks.BRICKS);
        BLOCK_MAP.put("brick_stairs", Blocks.BRICK_STAIRS);
        BLOCK_MAP.put("brick_slab", Blocks.BRICK_SLAB);
        BLOCK_MAP.put("brick_wall", Blocks.BRICK_WALL);
        BLOCK_MAP.put("prismarine", Blocks.PRISMARINE);
        BLOCK_MAP.put("prismarine_bricks", Blocks.PRISMARINE_BRICKS);
        BLOCK_MAP.put("dark_prismarine", Blocks.DARK_PRISMARINE);
        BLOCK_MAP.put("prismarine_stairs", Blocks.PRISMARINE_STAIRS);
        BLOCK_MAP.put("prismarine_brick_stairs", Blocks.PRISMARINE_BRICK_STAIRS);
        BLOCK_MAP.put("dark_prismarine_stairs", Blocks.DARK_PRISMARINE_STAIRS);
        BLOCK_MAP.put("prismarine_slab", Blocks.PRISMARINE_SLAB);
        BLOCK_MAP.put("prismarine_brick_slab", Blocks.PRISMARINE_BRICK_SLAB);
        BLOCK_MAP.put("dark_prismarine_slab", Blocks.DARK_PRISMARINE_SLAB);
        BLOCK_MAP.put("prismarine_wall", Blocks.PRISMARINE_WALL);
        BLOCK_MAP.put("iron_block", Blocks.IRON_BLOCK);
        BLOCK_MAP.put("raw_iron_block", Blocks.RAW_IRON_BLOCK);
        BLOCK_MAP.put("gold_block", Blocks.GOLD_BLOCK);
        BLOCK_MAP.put("raw_gold_block", Blocks.RAW_GOLD_BLOCK);
        BLOCK_MAP.put("diamond_block", Blocks.DIAMOND_BLOCK);
        BLOCK_MAP.put("emerald_block", Blocks.EMERALD_BLOCK);
        BLOCK_MAP.put("lapis_block", Blocks.LAPIS_BLOCK);
        BLOCK_MAP.put("redstone_block", Blocks.REDSTONE_BLOCK);
        BLOCK_MAP.put("coal_block", Blocks.COAL_BLOCK);
        BLOCK_MAP.put("amethyst_block", Blocks.AMETHYST_BLOCK);
        BLOCK_MAP.put("copper_block", Blocks.COPPER_BLOCK);
        BLOCK_MAP.put("raw_copper_block", Blocks.RAW_COPPER_BLOCK);
        BLOCK_MAP.put("cut_copper", Blocks.CUT_COPPER);
        BLOCK_MAP.put("cut_copper_stairs", Blocks.CUT_COPPER_STAIRS);
        BLOCK_MAP.put("cut_copper_slab", Blocks.CUT_COPPER_SLAB);
        BLOCK_MAP.put("exposed_cut_copper", Blocks.EXPOSED_CUT_COPPER);
        BLOCK_MAP.put("exposed_cut_copper_stairs", Blocks.EXPOSED_CUT_COPPER_STAIRS);
        BLOCK_MAP.put("exposed_cut_copper_slab", Blocks.EXPOSED_CUT_COPPER_SLAB);
        BLOCK_MAP.put("weathered_cut_copper", Blocks.WEATHERED_CUT_COPPER);
        BLOCK_MAP.put("weathered_cut_copper_stairs", Blocks.WEATHERED_CUT_COPPER_STAIRS);
        BLOCK_MAP.put("weathered_cut_copper_slab", Blocks.WEATHERED_CUT_COPPER_SLAB);
        BLOCK_MAP.put("oxidized_cut_copper", Blocks.OXIDIZED_CUT_COPPER);
        BLOCK_MAP.put("oxidized_cut_copper_stairs", Blocks.OXIDIZED_CUT_COPPER_STAIRS);
        BLOCK_MAP.put("oxidized_cut_copper_slab", Blocks.OXIDIZED_CUT_COPPER_SLAB);
        BLOCK_MAP.put("waxed_cut_copper", Blocks.WAXED_CUT_COPPER);
        BLOCK_MAP.put("waxed_cut_copper_stairs", Blocks.WAXED_CUT_COPPER_STAIRS);
        BLOCK_MAP.put("waxed_cut_copper_slab", Blocks.WAXED_CUT_COPPER_SLAB);
        BLOCK_MAP.put("waxed_exposed_cut_copper", Blocks.WAXED_EXPOSED_CUT_COPPER);
        BLOCK_MAP.put("waxed_exposed_cut_copper_stairs", Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS);
        BLOCK_MAP.put("waxed_exposed_cut_copper_slab", Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB);
        BLOCK_MAP.put("waxed_weathered_cut_copper", Blocks.WAXED_WEATHERED_CUT_COPPER);
        BLOCK_MAP.put("waxed_weathered_cut_copper_stairs", Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS);
        BLOCK_MAP.put("waxed_weathered_cut_copper_slab", Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB);
        BLOCK_MAP.put("waxed_oxidized_cut_copper", Blocks.WAXED_OXIDIZED_CUT_COPPER);
        BLOCK_MAP.put("waxed_oxidized_cut_copper_stairs", Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
        BLOCK_MAP.put("waxed_oxidized_cut_copper_slab", Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB);
        BLOCK_MAP.put("netherite_block", Blocks.NETHERITE_BLOCK);
        BLOCK_MAP.put("ancient_debris", Blocks.ANCIENT_DEBRIS);
        BLOCK_MAP.put("obsidian", Blocks.OBSIDIAN);
        BLOCK_MAP.put("crying_obsidian", Blocks.CRYING_OBSIDIAN);
        BLOCK_MAP.put("magma_block", Blocks.MAGMA_BLOCK);
        BLOCK_MAP.put("warped_nylium", Blocks.WARPED_NYLIUM);
        BLOCK_MAP.put("crimson_nylium", Blocks.CRIMSON_NYLIUM);
        BLOCK_MAP.put("soul_soil", Blocks.SOUL_SOIL);
        BLOCK_MAP.put("shroomlight", Blocks.SHROOMLIGHT);
        BLOCK_MAP.put("warped_wart_block", Blocks.WARPED_WART_BLOCK);
        BLOCK_MAP.put("weeping_vines", Blocks.WEEPING_VINES);
        BLOCK_MAP.put("weeping_vines_plant", Blocks.WEEPING_VINES_PLANT);
        BLOCK_MAP.put("twisting_vines", Blocks.TWISTING_VINES);
        BLOCK_MAP.put("twisting_vines_plant", Blocks.TWISTING_VINES_PLANT);
        BLOCK_MAP.put("blackstone_stairs", Blocks.BLACKSTONE_STAIRS);
        BLOCK_MAP.put("blackstone_slab", Blocks.BLACKSTONE_SLAB);
        BLOCK_MAP.put("blackstone_wall", Blocks.BLACKSTONE_WALL);
        BLOCK_MAP.put("polished_blackstone_stairs", Blocks.POLISHED_BLACKSTONE_STAIRS);
        BLOCK_MAP.put("polished_blackstone_slab", Blocks.POLISHED_BLACKSTONE_SLAB);
        BLOCK_MAP.put("polished_blackstone_wall", Blocks.POLISHED_BLACKSTONE_WALL);
        BLOCK_MAP.put("polished_blackstone_brick_stairs", Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
        BLOCK_MAP.put("polished_blackstone_brick_slab", Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
        BLOCK_MAP.put("polished_blackstone_brick_wall", Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
        BLOCK_MAP.put("end_stone", Blocks.END_STONE);
        BLOCK_MAP.put("end_stone_bricks", Blocks.END_STONE_BRICKS);
        BLOCK_MAP.put("end_stone_brick_stairs", Blocks.END_STONE_BRICK_STAIRS);
        BLOCK_MAP.put("end_stone_brick_slab", Blocks.END_STONE_BRICK_SLAB);
        BLOCK_MAP.put("end_stone_brick_wall", Blocks.END_STONE_BRICK_WALL);
        BLOCK_MAP.put("purpur_block", Blocks.PURPUR_BLOCK);
        BLOCK_MAP.put("purpur_pillar", Blocks.PURPUR_PILLAR);
        BLOCK_MAP.put("purpur_stairs", Blocks.PURPUR_STAIRS);
        BLOCK_MAP.put("purpur_slab", Blocks.PURPUR_SLAB);
        BLOCK_MAP.put("cobbled_deepslate_stairs", Blocks.COBBLED_DEEPSLATE_STAIRS);
        BLOCK_MAP.put("cobbled_deepslate_slab", Blocks.COBBLED_DEEPSLATE_SLAB);
        BLOCK_MAP.put("cobbled_deepslate_wall", Blocks.COBBLED_DEEPSLATE_WALL);
        BLOCK_MAP.put("polished_deepslate", Blocks.POLISHED_DEEPSLATE);
        BLOCK_MAP.put("polished_deepslate_stairs", Blocks.POLISHED_DEEPSLATE_STAIRS);
        BLOCK_MAP.put("polished_deepslate_slab", Blocks.POLISHED_DEEPSLATE_SLAB);
        BLOCK_MAP.put("polished_deepslate_wall", Blocks.POLISHED_DEEPSLATE_WALL);
        BLOCK_MAP.put("deepslate_bricks", Blocks.DEEPSLATE_BRICKS);
        BLOCK_MAP.put("deepslate_brick_stairs", Blocks.DEEPSLATE_BRICK_STAIRS);
        BLOCK_MAP.put("deepslate_brick_slab", Blocks.DEEPSLATE_BRICK_SLAB);
        BLOCK_MAP.put("deepslate_brick_wall", Blocks.DEEPSLATE_BRICK_WALL);
        BLOCK_MAP.put("cracked_deepslate_bricks", Blocks.CRACKED_DEEPSLATE_BRICKS);
        BLOCK_MAP.put("deepslate_tiles", Blocks.DEEPSLATE_TILES);
        BLOCK_MAP.put("deepslate_tile_stairs", Blocks.DEEPSLATE_TILE_STAIRS);
        BLOCK_MAP.put("deepslate_tile_slab", Blocks.DEEPSLATE_TILE_SLAB);
        BLOCK_MAP.put("deepslate_tile_wall", Blocks.DEEPSLATE_TILE_WALL);
        BLOCK_MAP.put("cracked_deepslate_tiles", Blocks.CRACKED_DEEPSLATE_TILES);
        BLOCK_MAP.put("chiseled_deepslate", Blocks.CHISELED_DEEPSLATE);
        BLOCK_MAP.put("reinforced_deepslate", Blocks.REINFORCED_DEEPSLATE);
        BLOCK_MAP.put("deepslate", Blocks.DEEPSLATE);
        BLOCK_MAP.put("oak_stairs", Blocks.OAK_STAIRS);
        BLOCK_MAP.put("oak_slab", Blocks.OAK_SLAB);
        BLOCK_MAP.put("oak_fence", Blocks.OAK_FENCE);
        BLOCK_MAP.put("oak_fence_gate", Blocks.OAK_FENCE_GATE);
        BLOCK_MAP.put("spruce_stairs", Blocks.SPRUCE_STAIRS);
        BLOCK_MAP.put("spruce_slab", Blocks.SPRUCE_SLAB);
        BLOCK_MAP.put("spruce_fence", Blocks.SPRUCE_FENCE);
        BLOCK_MAP.put("spruce_fence_gate", Blocks.SPRUCE_FENCE_GATE);
        BLOCK_MAP.put("birch_stairs", Blocks.BIRCH_STAIRS);
        BLOCK_MAP.put("birch_slab", Blocks.BIRCH_SLAB);
        BLOCK_MAP.put("birch_fence", Blocks.BIRCH_FENCE);
        BLOCK_MAP.put("birch_fence_gate", Blocks.BIRCH_FENCE_GATE);
        BLOCK_MAP.put("jungle_stairs", Blocks.JUNGLE_STAIRS);
        BLOCK_MAP.put("jungle_slab", Blocks.JUNGLE_SLAB);
        BLOCK_MAP.put("jungle_fence", Blocks.JUNGLE_FENCE);
        BLOCK_MAP.put("jungle_fence_gate", Blocks.JUNGLE_FENCE_GATE);
        BLOCK_MAP.put("acacia_stairs", Blocks.ACACIA_STAIRS);
        BLOCK_MAP.put("acacia_slab", Blocks.ACACIA_SLAB);
        BLOCK_MAP.put("acacia_fence", Blocks.ACACIA_FENCE);
        BLOCK_MAP.put("acacia_fence_gate", Blocks.ACACIA_FENCE_GATE);
        BLOCK_MAP.put("dark_oak_stairs", Blocks.DARK_OAK_STAIRS);
        BLOCK_MAP.put("dark_oak_slab", Blocks.DARK_OAK_SLAB);
        BLOCK_MAP.put("dark_oak_fence", Blocks.DARK_OAK_FENCE);
        BLOCK_MAP.put("dark_oak_fence_gate", Blocks.DARK_OAK_FENCE_GATE);
        BLOCK_MAP.put("mangrove_stairs", Blocks.MANGROVE_STAIRS);
        BLOCK_MAP.put("mangrove_slab", Blocks.MANGROVE_SLAB);
        BLOCK_MAP.put("mangrove_fence", Blocks.MANGROVE_FENCE);
        BLOCK_MAP.put("mangrove_fence_gate", Blocks.MANGROVE_FENCE_GATE);
        BLOCK_MAP.put("cherry_stairs", Blocks.CHERRY_STAIRS);
        BLOCK_MAP.put("cherry_slab", Blocks.CHERRY_SLAB);
        BLOCK_MAP.put("cherry_fence", Blocks.CHERRY_FENCE);
        BLOCK_MAP.put("cherry_fence_gate", Blocks.CHERRY_FENCE_GATE);
        BLOCK_MAP.put("bamboo_stairs", Blocks.BAMBOO_STAIRS);
        BLOCK_MAP.put("bamboo_slab", Blocks.BAMBOO_SLAB);
        BLOCK_MAP.put("bamboo_fence", Blocks.BAMBOO_FENCE);
        BLOCK_MAP.put("bamboo_fence_gate", Blocks.BAMBOO_FENCE_GATE);
        BLOCK_MAP.put("bamboo_mosaic_stairs", Blocks.BAMBOO_MOSAIC_STAIRS);
        BLOCK_MAP.put("bamboo_mosaic_slab", Blocks.BAMBOO_MOSAIC_SLAB);
        BLOCK_MAP.put("stone_stairs", Blocks.STONE_STAIRS);
        BLOCK_MAP.put("stone_slab", Blocks.STONE_SLAB);
        BLOCK_MAP.put("smooth_stone_slab", Blocks.SMOOTH_STONE_SLAB);
        BLOCK_MAP.put("granite_stairs", Blocks.GRANITE_STAIRS);
        BLOCK_MAP.put("granite_slab", Blocks.GRANITE_SLAB);
        BLOCK_MAP.put("granite_wall", Blocks.GRANITE_WALL);
        BLOCK_MAP.put("diorite_stairs", Blocks.DIORITE_STAIRS);
        BLOCK_MAP.put("diorite_slab", Blocks.DIORITE_SLAB);
        BLOCK_MAP.put("diorite_wall", Blocks.DIORITE_WALL);
        BLOCK_MAP.put("andesite_stairs", Blocks.ANDESITE_STAIRS);
        BLOCK_MAP.put("andesite_slab", Blocks.ANDESITE_SLAB);
        BLOCK_MAP.put("andesite_wall", Blocks.ANDESITE_WALL);
        BLOCK_MAP.put("polished_granite", Blocks.POLISHED_GRANITE);
        BLOCK_MAP.put("polished_granite_stairs", Blocks.POLISHED_GRANITE_STAIRS);
        BLOCK_MAP.put("polished_granite_slab", Blocks.POLISHED_GRANITE_SLAB);
        BLOCK_MAP.put("polished_diorite", Blocks.POLISHED_DIORITE);
        BLOCK_MAP.put("polished_diorite_stairs", Blocks.POLISHED_DIORITE_STAIRS);
        BLOCK_MAP.put("polished_diorite_slab", Blocks.POLISHED_DIORITE_SLAB);
        BLOCK_MAP.put("polished_andesite", Blocks.POLISHED_ANDESITE);
        BLOCK_MAP.put("polished_andesite_stairs", Blocks.POLISHED_ANDESITE_STAIRS);
        BLOCK_MAP.put("polished_andesite_slab", Blocks.POLISHED_ANDESITE_SLAB);
    }
    
    // 缓存的规则（避免每次都重新解析）
    private static List<GenerationRule> cachedRules = null;
    
    // 获取所有启用的生成规则
    public static List<GenerationRule> getRules() {
        if (!Config.ENABLE_CUSTOM_GENERATION.get()) {
            return Collections.emptyList();
        }
        
        // 检查是否需要重新解析
        if (cachedRules == null) {
            cachedRules = parseRules();
        }
        
        return cachedRules;
    }
    
    // 解析规则
    private static List<GenerationRule> parseRules() {
        List<GenerationRule> rules = new ArrayList<>();
        
        // 添加预定义的规则（使用主配置开关）
        if (Config.CALCITE_GENERATION.get()) {
            Block input = BLOCK_MAP.get("bone_block");
            Block output = BLOCK_MAP.get("calcite");
            Set<Block> adjacent = new HashSet<>();
            adjacent.add(BLOCK_MAP.get("blue_ice"));
            
            if (input != null && output != null) {
                rules.add(new GenerationRule(input, output, adjacent));
            }
        }
        
        if (Config.TUFF_GENERATION.get()) {
            Block input = BLOCK_MAP.get("andesite");
            Block output = BLOCK_MAP.get("tuff");
            Set<Block> adjacent = new HashSet<>();
            adjacent.add(BLOCK_MAP.get("blue_ice"));
            
            if (input != null && output != null) {
                rules.add(new GenerationRule(input, output, adjacent));
            }
        }
        
        if (Config.DRIPSTONE_GENERATION.get()) {
            Block input = BLOCK_MAP.get("granite");
            Block output = BLOCK_MAP.get("dripstone_block");
            Set<Block> adjacent = new HashSet<>();
            adjacent.add(BLOCK_MAP.get("blue_ice"));
            
            if (input != null && output != null) {
                rules.add(new GenerationRule(input, output, adjacent));
            }
        }
        
        // 解析自定义规则
        List<? extends String> customRulesList = Config.CUSTOM_RULES.get();
        if (customRulesList != null) {
            for (String rule : customRulesList) {
                GenerationRule customRule = parseSingleRule(rule);
                if (customRule != null) {
                    rules.add(customRule);
                }
            }
        }
        
        return rules;
    }
    
    // 解析单条自定义规则
    private static GenerationRule parseSingleRule(String ruleStr) {
        try {
            // 格式: input_block->output_block:adjacent_block1,adjacent_block2
            String[] parts = ruleStr.split("->");
            if (parts.length != 2) {
                return null;
            }
            
            String inputName = parts[0].trim().toLowerCase();
            String[] outputAndAdjacent = parts[1].split(":");
            
            if (outputAndAdjacent.length != 2) {
                return null;
            }
            
            String outputName = outputAndAdjacent[0].trim().toLowerCase();
            String[] adjacentNames = outputAndAdjacent[1].split(",");
            
            Block inputBlock = BLOCK_MAP.get(inputName);
            Block outputBlock = BLOCK_MAP.get(outputName);
            Set<Block> adjacentBlocks = new HashSet<>();
            
            if (inputBlock == null || outputBlock == null) {
                return null;
            }
            
            for (String adj : adjacentNames) {
                Block block = BLOCK_MAP.get(adj.trim().toLowerCase());
                if (block != null) {
                    adjacentBlocks.add(block);
                }
            }
            
            if (!adjacentBlocks.isEmpty()) {
                return new GenerationRule(inputBlock, outputBlock, adjacentBlocks);
            }
        } catch (Exception e) {
            // 忽略解析错误
        }
        
        return null;
    }
    
    // 清除缓存（当配置重新加载时调用）
    public static void clearCache() {
        cachedRules = null;
    }
}
