package org.gloomybanana.nature_rebirth;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Collections;
import java.util.List;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // 深板岩生成设置
    public static final ModConfigSpec.IntValue DEEPSLATE_Y_THRESHOLD = BUILDER
            .comment("生成深板岩变种而非圆石/石头时，岩浆遇水的Y坐标阈值",
                    "当岩浆在低于此Y等级与水相遇时，将生成深板岩变种",
                    "默认值为0（Y=0以下生成深板岩）")
            .defineInRange("deepslateYThreshold", 0, -64, 319);

    // 方解石生成设置
    public static final ModConfigSpec.BooleanValue CALCITE_GENERATION = BUILDER
            .comment("当岩浆在骨块上方流动且相邻蓝冰时，启用方解石生成",
                    "默认：true（启用）")
            .define("calciteGeneration", true);

    // 凝灰岩生成设置
    public static final ModConfigSpec.BooleanValue TUFF_GENERATION = BUILDER
            .comment("当岩浆在安山岩上方流动且相邻蓝冰时，启用凝灰岩生成",
                    "默认：true（启用）")
            .define("tuffGeneration", true);

    // 下界岩生成设置
    public static final ModConfigSpec.BooleanValue NETHERRACK_GENERATION = BUILDER
            .comment("当岩浆周围同时存在蓝冰和岩浆块时，启用下界岩生成",
                    "默认：true（启用）")
            .define("netherrackGeneration", true);

    // 滴水石生成设置
    public static final ModConfigSpec.BooleanValue DRIPSTONE_GENERATION = BUILDER
            .comment("当岩浆在花岗岩上方流动且相邻蓝冰时，启用滴水石生成",
                    "默认：true（启用）")
            .define("dripstoneGeneration", true);

    // 鸡蛋生成设置
    public static final ModConfigSpec.BooleanValue EGG_SPAWN_EGG = BUILDER
            .comment("启用鸡蛋生成机制：向生命值为1的生物投掷鸡蛋会击杀并掉落刷怪蛋",
                    "每超过1点生命值，概率降低10%",
                    "默认：true（启用）")
            .define("eggSpawnEgg", true);

    // 石制矿石生成概率（信标增强）
    public static final ModConfigSpec.DoubleValue STONE_COAL_ORE_CHANCE = BUILDER
            .comment("信标增强石头生成时，煤矿石的生成概率",
                    "默认：0.15（15%）")
            .defineInRange("stoneCoalOreChance", 0.15, 0.0, 1.0);
    
    public static final ModConfigSpec.DoubleValue STONE_IRON_ORE_CHANCE = BUILDER
            .comment("信标增强石头生成时，铁矿石的生成概率",
                    "默认：0.15（15%）")
            .defineInRange("stoneIronOreChance", 0.15, 0.0, 1.0);
    
    public static final ModConfigSpec.DoubleValue STONE_REDSTONE_ORE_CHANCE = BUILDER
            .comment("信标增强石头生成时，红石矿石的生成概率",
                    "默认：0.08（8%）")
            .defineInRange("stoneRedstoneOreChance", 0.08, 0.0, 1.0);
    
    public static final ModConfigSpec.DoubleValue STONE_LAPIS_ORE_CHANCE = BUILDER
            .comment("信标增强石头生成时，青金石矿石的生成概率",
                    "默认：0.06（6%）")
            .defineInRange("stoneLapisOreChance", 0.06, 0.0, 1.0);
    
    public static final ModConfigSpec.DoubleValue STONE_GOLD_ORE_CHANCE = BUILDER
            .comment("信标增强石头生成时，金矿石的生成概率",
                    "默认：0.05（5%）")
            .defineInRange("stoneGoldOreChance", 0.05, 0.0, 1.0);
    
    public static final ModConfigSpec.DoubleValue STONE_EMERALD_ORE_CHANCE = BUILDER
            .comment("信标增强石头生成时，绿宝石矿石的生成概率",
                    "默认：0.02（2%）")
            .defineInRange("stoneEmeraldOreChance", 0.02, 0.0, 1.0);
    
    public static final ModConfigSpec.DoubleValue STONE_DIAMOND_ORE_CHANCE = BUILDER
            .comment("信标增强石头生成时，钻石矿石的生成概率",
                    "默认：0.01（1%）")
            .defineInRange("stoneDiamondOreChance", 0.01, 0.0, 1.0);

    // 下界矿石生成概率（信标增强）
    public static final ModConfigSpec.DoubleValue NETHER_QUARTZ_ORE_CHANCE = BUILDER
            .comment("信标增强下界岩生成时，下界石英矿石的生成概率",
                    "默认：0.40（40%）")
            .defineInRange("netherQuartzOreChance", 0.40, 0.0, 1.0);
    
    public static final ModConfigSpec.DoubleValue NETHER_GOLD_ORE_CHANCE = BUILDER
            .comment("信标增强下界岩生成时，下界金矿石的生成概率",
                    "默认：0.25（25%）")
            .defineInRange("netherGoldOreChance", 0.25, 0.0, 1.0);
    
    public static final ModConfigSpec.DoubleValue ANCIENT_DEBRIS_CHANCE = BUILDER
            .comment("信标增强下界岩生成时，远古残骸的生成概率",
                    "默认：0.03（3%）")
            .defineInRange("ancientDebrisChance", 0.03, 0.0, 1.0);

    // 自定义生成规则设置
    public static final ModConfigSpec.BooleanValue ENABLE_CUSTOM_GENERATION = BUILDER
            .comment("启用自定义方块生成规则（通过 customRules 添加）",
                    "默认：true（启用）")
            .define("enableCustomGeneration", true);
    
    public static final ModConfigSpec.ConfigValue<List<? extends String>> CUSTOM_RULES = BUILDER
            .comment("自定义生成规则列表，格式：输入方块->输出方块:相邻方块1,相邻方块2",
                    "示例：obsidian->end_stone:soul_sand",
                    "支持的原版方块：bone_block, andesite, granite, obsidian, netherrack 等",
                    "默认：空")
            .define("customRules", Collections.emptyList());

    static final ModConfigSpec SPEC = BUILDER.build();
}
