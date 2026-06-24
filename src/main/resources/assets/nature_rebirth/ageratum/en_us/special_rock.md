---
title: "Special Rock Generation"
category: "Core Mechanics"
---

# Special Rock Generation Mechanism

This mod enhances the interaction between lava and water, as well as special transformation mechanisms when lava flows.

## Deepslate Generation

### Mechanism

When lava meets water, different blocks are generated based on the Y coordinate:

- **Y >= 0**: Regular stone is generated
- **Y < 0**: Deepslate is generated

### Configuration

| Option | Description | Default |
|-------|-------------|---------|
| deepslateYThreshold | Deepslate generation Y threshold | 0 |

## Calcite Generation

::: tip
Place lava above bone block and diorite beside the lava, the lava will transform into calcite!
:::

**Legend:**
- 🔍 Glass Pane (visual base)
- 🟫 Bone Block (bottom block)
- 🔴 Lava (flowing above bone block)
- 🟪 Diorite (trigger condition, beside lava)
- ⬜ Calcite (conversion result)

## Tuff Generation

::: tip
Place lava above andesite and blue ice around the lava, the lava will transform into tuff!
:::

**Legend:**
- 🟫 Andesite (bottom block)
- 🔵 Blue Ice (adjacent condition)
- ⚪ Lava → Tuff (conversion result)

## Dripstone Generation

::: tip
Place lava above granite and blue ice around the lava, the lava will transform into dripstone!
:::

**Legend:**
- 🟫 Granite (bottom block)
- 🔵 Blue Ice (adjacent condition)
- ⚪ Lava → Dripstone Block (conversion result)

## Netherrack Generation

::: warning
Netherrack generation requires both blue ice and magma block to be present around the lava!
:::

**Legend:**
- 🔵 Blue Ice (adjacent condition)
- 🟠 Magma Block (adjacent condition)
- ⚪ Lava → Netherrack (conversion result)

## Custom Generation Rules

This mod supports custom lava transformation rules!

**Format:** `bottom_block->generate_block:adjacent_block1,adjacent_block2`

**Examples:**
- `obsidian->end_stone:soul_sand` - Lava on obsidian with adjacent soul sand generates end stone
- `diorite->craton:rhyolite:blue_ice` - Supports modded blocks

::: danger
Changes to the config file require restarting the game to take effect!
:::
