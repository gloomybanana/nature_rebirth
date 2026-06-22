package org.gloomybanana.nature_rebirth;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Collections;
import java.util.List;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Deepslate generation settings
    public static final ModConfigSpec.IntValue DEEPSLATE_Y_THRESHOLD = BUILDER
            .comment("Y coordinate threshold for generating deepslate variants instead of cobblestone/stone",
                    "When lava meets water below this Y level, it will generate deepslate variants",
                    "Default: 0 (generate deepslate below Y=0)")
            .defineInRange("deepslateYThreshold", 0, -64, 319);

    // Calcite generation setting
    public static final ModConfigSpec.BooleanValue CALCITE_GENERATION = BUILDER
            .comment("Enable calcite generation when lava flows above bone block and adjacent to blue ice",
                    "Default: true (enabled)")
            .define("calciteGeneration", true);

    // Tuff generation setting
    public static final ModConfigSpec.BooleanValue TUFF_GENERATION = BUILDER
            .comment("Enable tuff generation when lava flows above andesite and adjacent to blue ice",
                    "Default: true (enabled)")
            .define("tuffGeneration", true);

    // Netherrack generation setting
    public static final ModConfigSpec.BooleanValue NETHERRACK_GENERATION = BUILDER
            .comment("Enable netherrack generation when lava is surrounded by both blue ice and magma blocks",
                    "Default: true (enabled)")
            .define("netherrackGeneration", true);

    // Dripstone generation setting
    public static final ModConfigSpec.BooleanValue DRIPSTONE_GENERATION = BUILDER
            .comment("Enable dripstone generation when lava flows above granite and adjacent to blue ice",
                    "Default: true (enabled)")
            .define("dripstoneGeneration", true);

    // Egg spawn egg setting
    public static final ModConfigSpec.BooleanValue EGG_SPAWN_EGG = BUILDER
            .comment("Enable egg spawn mechanism: throwing eggs at mobs with 1 health will kill them and drop spawn eggs",
                    "Probability decreases by 10% for each additional health point",
                    "Default: true (enabled)")
            .define("eggSpawnEgg", true);

    // Custom ore lists (beacon enhanced)
    public static final ModConfigSpec.ConfigValue<List<? extends String>> STONE_ORE_CUSTOM_LIST = BUILDER
            .comment("Custom stone ore list (beacon enhanced), format: block_name:chance",
                    "Example: coal_ore:0.15,iron_ore:0.15,redstone_ore:0.08",
                    "Supported ores: coal_ore, iron_ore, copper_ore, gold_ore, redstone_ore, lapis_ore, emerald_ore, diamond_ore",
                    "Also supports modded ores with full ID: mekanism:tin_ore:0.12",
                    "Default: coal_ore:0.15,iron_ore:0.15,redstone_ore:0.08,lapis_ore:0.06,gold_ore:0.05,emerald_ore:0.02,diamond_ore:0.01",
                    "Total probability should not exceed 1.0")
            .defineList("stoneOreCustomList", java.util.Arrays.asList(
                    "coal_ore:0.15",
                    "iron_ore:0.15",
                    "redstone_ore:0.08",
                    "lapis_ore:0.06",
                    "gold_ore:0.05",
                    "emerald_ore:0.02",
                    "diamond_ore:0.01"
            ), s -> true);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> DEEPSLATE_ORE_CUSTOM_LIST = BUILDER
            .comment("Custom deepslate ore list (beacon enhanced), format: block_name:chance",
                    "Example: deepslate_coal_ore:0.15,deepslate_iron_ore:0.15",
                    "Supported ores: deepslate_coal_ore, deepslate_iron_ore, deepslate_copper_ore, deepslate_gold_ore, deepslate_redstone_ore, deepslate_lapis_ore, deepslate_emerald_ore, deepslate_diamond_ore",
                    "Also supports modded ores with full ID: mekanism:deepslate_tin_ore:0.12",
                    "Default: deepslate_coal_ore:0.15,deepslate_iron_ore:0.15,deepslate_redstone_ore:0.08,deepslate_lapis_ore:0.06,deepslate_gold_ore:0.05,deepslate_emerald_ore:0.02,deepslate_diamond_ore:0.01",
                    "Total probability should not exceed 1.0")
            .defineList("deepslateOreCustomList", java.util.Arrays.asList(
                    "deepslate_coal_ore:0.15",
                    "deepslate_iron_ore:0.15",
                    "deepslate_redstone_ore:0.08",
                    "deepslate_lapis_ore:0.06",
                    "deepslate_gold_ore:0.05",
                    "deepslate_emerald_ore:0.02",
                    "deepslate_diamond_ore:0.01"
            ), s -> true);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> NETHER_ORE_CUSTOM_LIST = BUILDER
            .comment("Custom nether ore list (beacon enhanced), format: block_name:chance",
                    "Example: nether_quartz_ore:0.40,nether_gold_ore:0.25,ancient_debris:0.03",
                    "Supported ores: nether_quartz_ore, nether_gold_ore, ancient_debris, glowstone, blackstone, basalt",
                    "Also supports modded ores with full ID: mekanism:nether_tin_ore:0.12",
                    "Default: nether_quartz_ore:0.40,nether_gold_ore:0.25,ancient_debris:0.005",
                    "Total probability should not exceed 1.0")
            .defineList("netherOreCustomList", java.util.Arrays.asList(
                    "nether_quartz_ore:0.40",
                    "nether_gold_ore:0.25",
                    "ancient_debris:0.005"
            ), s -> true);

    // Custom generation rules settings
    public static final ModConfigSpec.BooleanValue ENABLE_CUSTOM_GENERATION = BUILDER
            .comment("Enable custom block generation rules (added via customRules)",
                    "Default: true (enabled)")
            .define("enableCustomGeneration", true);
    
    public static final ModConfigSpec.ConfigValue<List<? extends String>> CUSTOM_RULES = BUILDER
            .comment("Custom generation rules list, format: bottom_block->generate_block:adjacent_block1,adjacent_block2",
                    "Example: obsidian->end_stone:soul_sand",
                    "Supports mod blocks with full ID format: modid:block_name",
                    "Example with mod blocks: craton:rhyolite->minecraft:stone:blue_ice",
                    "Default: empty")
            .defineList("customRules", Collections.emptyList(), s -> true);

    // Dragon breath crafting setting
    public static final ModConfigSpec.BooleanValue DRAGON_BREATH_CRAFTING = BUILDER
            .comment("Enable dragon breath crafting with dragon head and glass bottle",
                    "Dragon head will not be consumed when crafting",
                    "Default: true (enabled)")
            .define("dragonBreathCrafting", true);

    // End stone conversion setting
    public static final ModConfigSpec.BooleanValue END_STONE_CONVERSION = BUILDER
            .comment("Enable end stone conversion using dragon breath",
                    "Right-click with dragon breath on stone/cobblestone/bricks to convert them to end stone variants",
                    "Default: true (enabled)")
            .define("endStoneConversion", true);

    // Ancient debris to bedrock conversion setting
    public static final ModConfigSpec.BooleanValue ANCIENT_DEBRIS_TO_BEDROCK = BUILDER
            .comment("Enable ancient debris to bedrock conversion near max level beacon",
                    "When ancient debris is placed near a level 4 beacon, it will be converted to bedrock after a delay",
                    "Default: true (enabled)")
            .define("ancientDebrisToBedrock", true);

    public static final ModConfigSpec.IntValue ANCIENT_DEBRIS_CONVERSION_DELAY = BUILDER
            .comment("Delay in seconds before ancient debris is converted to bedrock",
                    "Default: 10 seconds")
            .defineInRange("ancientDebrisConversionDelay", 10, 1, 60);

    static final ModConfigSpec SPEC = BUILDER.build();
}
