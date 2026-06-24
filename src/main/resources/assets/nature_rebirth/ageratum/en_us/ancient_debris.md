---
title: "Ancient Debris Conversion"
category: "Featured Features"
---

# Ancient Debris to Bedrock Conversion Mechanism

::: info
Place ancient debris near a level 4 beacon, and after some time it will convert to bedrock and drop as an item!
:::

## Structure Layout

<structure id="nature_rebirth:structure/ancient_debris_conversion.snbt"/>

## Conversion Conditions

- **Beacon Level**: Must reach level 4 beacon
- **Placement Range**: Ancient debris must be placed within 8 horizontal adjacent blocks of the beacon
- **Multiple ancient debris can be placed simultaneously**

## Conversion Process

1. Build a Level 4 beacon (pyramid base)
2. Place ancient debris within 8 horizontal adjacent blocks of the beacon
3. Wait for the configured delay time
4. Ancient debris converts to bedrock
5. Bedrock breaks and drops as an item

## Configuration Options

| Option | Description | Default |
|--------|-------------|---------|
| ancientDebrisToBedrock | Enable ancient debris conversion | true |
| ancientDebrisConversionDelay | Conversion delay (seconds) | 10 |

::: tip
The conversion delay can be adjusted in the config file, range is 1-60 seconds!
:::

::: warning
Make sure ancient debris is placed within the correct range, otherwise conversion will not trigger!
:::
