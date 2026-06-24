---
title: "Configuration"
category: "Advanced Features"
---

# Configuration

All features can be adjusted through the configuration file.

## Config File Location

```
.minecraft/config/nature_rebirth-common.toml
```

## Main Configuration Options

### Basic Configuration

| Option | Description | Default |
|-------|-------------|---------|
| deepslateYThreshold | Deepslate generation Y threshold | 0 |

### Rock Generation Configuration

| Option | Description | Default |
|-------|-------------|---------|
| calciteGeneration | Enable calcite generation | true |
| tuffGeneration | Enable tuff generation | true |
| netherrackGeneration | Enable netherrack generation | true |
| dripstoneGeneration | Enable dripstone generation | true |

### Featured Features Configuration

| Option | Description | Default |
|-------|-------------|---------|
| eggSpawnEgg | Enable egg capture mechanism | true |
| dragonBreathCrafting | Enable dragon breath crafting | true |
| endStoneConversion | Enable end stone conversion | true |
| ancientDebrisToBedrock | Enable ancient debris conversion | true |
| ancientDebrisConversionDelay | Conversion delay (seconds) | 10 |

### Custom Generation Rules

```toml
# Custom lava transformation rules
# Format: bottom_block->generate_block:adjacent_block
customRockGenerationRules = [
    "bone_block->calcite:blue_ice",
    "andesite->tuff:blue_ice",
    "granite->dripstone_block:blue_ice",
    "magma_block->netherrack:blue_ice"
]

# Stone version ore configuration
stoneOreCustomList = [
    "coal_ore",
    "iron_ore",
    "redstone_ore",
    "lapis_ore",
    "gold_ore",
    "emerald_ore",
    "diamond_ore"
]

# Deepslate version ore configuration
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

## Modifying Configuration

1. Close the game
2. Edit the configuration file
3. Restart the game for changes to take effect