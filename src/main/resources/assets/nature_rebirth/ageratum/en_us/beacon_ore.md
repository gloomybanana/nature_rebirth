---
title: "Beacon Enhanced Ore Generation"
category: "Featured Features"
---

# Beacon Enhanced Ore Generation

::: tip
Place a beacon below the position where lava and water generate stone/deepslate, and there's a chance to generate ores!
:::

## Mechanism

When lava and water meet to generate stone or deepslate, if there is a beacon below that position, the system has a chance to replace the generated block with the corresponding ore.

## Default Generation Probability

### Stone Ores

| Ore Type | Default Probability |
|----------|---------------------|
| Coal Ore | 20% |
| Iron Ore | 15% |
| Redstone Ore | 10% |
| Lapis Ore | 8% |
| Gold Ore | 5% |
| Emerald Ore | 3% |
| Diamond Ore | 2% |

### Deepslate Ores

| Ore Type | Default Probability |
|----------|---------------------|
| Deepslate Coal Ore | 20% |
| Deepslate Iron Ore | 15% |
| Deepslate Redstone Ore | 10% |
| Deepslate Lapis Ore | 8% |
| Deepslate Gold Ore | 5% |
| Deepslate Emerald Ore | 3% |
| Deepslate Diamond Ore | 2% |

### Nether Ores

| Ore Type | Default Probability |
|----------|---------------------|
| Nether Quartz Ore | 25% |
| Nether Gold Ore | 10% |
| Ancient Debris | 1% |

## Configuration Options

| Option | Description | Default |
|--------|-------------|---------|
| stoneOreCustomList | Stone ore enhancement list | See config file |
| deepslateOreCustomList | Deepslate ore enhancement list | See config file |
| netherOreCustomList | Nether ore enhancement list | See config file |

## Custom Configuration

You can customize ore generation probability and types through the config file, supporting modded ores!

::: danger
Changes to the config file require restarting the game to take effect!
:::
