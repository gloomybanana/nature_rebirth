---
title: "Egg Capture Mechanism"
category: "Featured Features"
---

# Egg Capture Mechanism

::: warning
Throwing eggs at a mob with 1 health will kill it and drop the corresponding spawn egg!
:::

## Mechanism Details

### Success Rate Calculation

| Health | Success Rate |
|--------|--------------|
| 1 | 100% |
| 2 | 90% |
| 3 | 80% |
| 4 | 70% |
| 5 | 60% |
| ... | ... |

**Formula:** Success Rate = max(0, 100% - (Health - 1) × 10%)

### How to Use

1. Find a low-health mob
2. Hold an egg in hand
3. Throw the egg at the mob
4. If successful, the mob will be killed and drop a spawn egg

## Configuration

| Option | Description | Default |
|--------|-------------|---------|
| eggSpawnEgg | Enable egg capture mechanism | true |