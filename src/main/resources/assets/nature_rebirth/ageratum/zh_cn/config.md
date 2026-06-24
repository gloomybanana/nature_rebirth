---
title: "配置说明"
category: "高级功能"
---

# 配置说明

所有功能均可通过配置文件调整。

## 配置文件位置

```
.minecraft/config/nature_rebirth-common.toml
```

## 主要配置项

### 基础配置

| 配置项 | 说明 | 默认值 |
|-------|------|--------|
| deepslateYThreshold | 深板岩生成 Y 阈值 | 0 |

### 岩石生成配置

| 配置项 | 说明 | 默认值 |
|-------|------|--------|
| calciteGeneration | 启用方解石生成 | true |
| tuffGeneration | 启用凝灰岩生成 | true |
| netherrackGeneration | 启用下界岩生成 | true |
| dripstoneGeneration | 启用滴水石生成 | true |

### 特殊机制配置

| 配置项 | 说明 | 默认值 |
|-------|------|--------|
| eggSpawnEgg | 启用鸡蛋捕捉机制 | true |
| dragonBreathCrafting | 启用龙息合成 | true |
| endStoneConversion | 启用末地石转换 | true |
| ancientDebrisToBedrock | 启用远古残骸转化 | true |
| ancientDebrisConversionDelay | 转化延迟（秒） | 10 |

### 自定义生成规则

```toml
# 自定义岩浆转化规则
# 格式: bottom_block->generate_block:adjacent_block
customRockGenerationRules = [
    "bone_block->calcite:blue_ice",
    "andesite->tuff:blue_ice",
    "granite->dripstone_block:blue_ice",
    "magma_block->netherrack:blue_ice"
]

# 石头版本矿石配置
stoneOreCustomList = [
    "coal_ore",
    "iron_ore",
    "redstone_ore",
    "lapis_ore",
    "gold_ore",
    "emerald_ore",
    "diamond_ore"
]

# 深板岩版本矿石配置
deepslateOreCustomList = [
    "deepslate_coal_ore",
    "deepslate_iron_ore",
    "deepslate_redstone_ore",
    "deepslate_lapis_ore",
    "deepslate_gold_ore",
    "deepslate_emerald_ore",
    "deepslate_diamond_ore"
]
```

## 修改配置

1. 关闭游戏
2. 编辑配置文件
3. 重启游戏使配置生效