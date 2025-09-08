---
description: Creating JSON Vein 
---

# Creating Vein 

This document describes how to create **custom ore veins** in IndustrialUpgrade using JSON files. These veins define where ores generate, their probabilities, colors, and associated deposit blocks.

---

## Basic JSON Structure

Each vein is described in a JSON file located under the `data/modid/vein/` directory.

```json
{
  "inputs": [
    {
      "id": "minecraft:iron_ore",
      "chance": 80
    },
    {
      "id": "minecraft:coal_ore",
      "chance": 20
    }
  ],
  "colors": [ // Additionally, not necessarily
    {
      "id": "minecraft:coal_ore",
      "color": 16777215
    },
    {
      "id": "minecraft:iron_ore",
      "color": 13421772
    }
  ],
  "deposit": [
    {
      "id": "minecraft:stone"
    }
  ]
}
```

---

## Field Descriptions

### **`inputs`** *(List of Objects)*
Defines which ores appear in the vein and their probability.
- **`id`** *(string)* → Block ID of the ore (e.g., `minecraft:iron_ore`).
- **`chance`** *(int, default = 100)* → Probability of this ore being selected when generating.

### **`colors`** *(List of Objects)*
Defines vein coloring for the minimap or visualization systems in Vein Sencor.
- **`id`** *(string)* → Block ID associated with this color.
- **`color`** *(int, default = white)* → Integer RGBA color value (use converter if needed).

### **`deposit`** *(List of Objects)*
Defines which blocks make up the deposit around the ores.
- **`id`** *(string)* → Block ID of the deposit material (e.g., `minecraft:stone`).

---

## Example Vein JSON

```json
{
  "inputs": [
    { "id": "minecraft:diamond_ore", "chance": 5 },
    { "id": "minecraft:emerald_ore", "chance": 2 },
    { "id": "minecraft:lapis_ore", "chance": 10 }
  ],
  "colors": [
    { "id": "minecraft:diamond_ore", "color": 65535 },
    { "id": "minecraft:emerald_ore", "color": 65280 },
    { "id": "minecraft:lapis_ore", "color": 255 }
  ],
  "deposit": [
    { "id": "minecraft:deepslate" }
  ]
}
```

This creates a vein that generates diamonds, emeralds, and lapis within stone or deepslate deposits.

---

## Table of Parameters

| Field       | Type    | Description |
|-------------|---------|-------------|
| `inputs`    | List    | Ores with probabilities. |
| `id`        | String  | Block ID (ore/deposit). |
| `chance`    | Int     | Probability (default 100). |
| `colors`    | List    | Colors associated with blocks. |
| `color`     | Int     | RGBA color as integer. |
| `deposit`   | List    | Base blocks of the vein. |

---

## Tips & Best Practices
- Always include at least one **deposit** block (e.g., `minecraft:stone`).
- Keep `chance` values balanced to avoid veins generating only rare ores.
- Use contrasting `colors` for better visibility on minimaps.
- Multiple JSON files can define multiple vein types.
- Invalid IDs will cause the loader to throw an error.

---

## Template

```json
{
  "inputs": [
    { "id": "minecraft:iron_ore", "chance": 70 },
    { "id": "minecraft:coal_ore", "chance": 30 }
  ],
  "colors": [
    { "id": "minecraft:stone", "color": 16777215 },
    { "id": "minecraft:iron_ore", "color": 1234567 }
  ],
  "deposit": [
    { "id": "minecraft:stone" }
  ]
}
```

---

## Conclusion
By defining JSON files under `vein/`, players and modpack makers can create unique **ore veins** with custom probabilities, colors, and deposits. This system allows for flexible and immersive resource generation in IndustrialUpgrade.

