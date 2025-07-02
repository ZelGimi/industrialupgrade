## Quantum Quarry Recipe Guide

This guide explains how to write a JSON script for registering **Quantum Quarry** modifications in IndustrialUpgrade. Players can define which items are added or removed for various processing types.

---

### JSON Structure

```json
{
  "type": "industrialupgrade:quantum_quarry",
  "recipe_type": "<machine>",              // e.g. "default", "furnace", "macerator", "comb_macerator"
  "recipeOperation": "<operation>",         // "add" or "remove"
  "inputs": [                                // List of input definitions
    {
      "type":  "item" | "tag",           // Input kind
      "id":    "<item_or_tag_name>",       // e.g. "minecraft:iron_ingot" or "forge:ingots/iron"
      "amount": <integer>                   // Optional, defaults to 1
    }
    // ... more inputs
  ]
}
```

| Field             | Description                                                                    |
| ----------------- | ------------------------------------------------------------------------------ |
| `id`              | Recipe ID, used internally (e.g. `industrialupgrade:quarry_default_add`).      |
| `recipe_type`     | Determines which processing list is targeted:                                  |
|                   | - `default`       → general quarry recipes                                     |
|                   | - `furnace`       → furnace-specific quarry recipes                            |
|                   | - `macerator`     → macerator quarry recipes                                   |
|                   | - `comb_macerator`→ combined macerator recipes                                 |
| `recipeOperation` | Action on the list:                                                            |
|                   | - `add`    → items will be **added** to the chosen recipe list                 |
|                   | - `remove` → items will be **removed** from the chosen recipe list             |
| `inputs`          | Array of inputs. Each object must specify `type`, `id`, and optional `amount`. |

---

### Input Types

* **`type: "item"`**

  * `id` is a full item resource name, e.g. `minecraft:diamond`.
  * Adds an `ItemStack` of given `amount`.

* **`type: "tag"`**

  * `id` is a Forge tag name, e.g. `forge:ores/gold`.
  * Uses `InputOreDict` to find matching items; only the first match is used.

* **`amount`**

  * Number of items in the stack. Defaults to **1** if omitted.

---

### Internal Behavior Mapping

After parsing inputs, the code executes one of these branches based on `recipe_type` and `recipeOperation`:

| recipe\_type     | recipeOperation | 
| ---------------- | --------------- | 
| `default`        | `add`           | 
| `default`        | `remove`        | 
| `furnace`        | `add`           | 
| `furnace`        | `remove`        |
| `macerator`      | `add`           | 
| `macerator`      | `remove`        | 
| `comb_macerator` | `add`           | 
| `comb_macerator` | `remove`        | 

If no matching branch is found, the recipe has no effect or may throw an exception for invalid values.

---

### Example: Adding to Macerator List

```json
{
  "type": "industrialupgrade:quantum_quarry",
  "recipe_type": "macerator",
  "recipeOperation": "add",
  "inputs": [
    {
      "type": "item",
      "id": "minecraft:gold_ingot",
      "amount": 2
    },
    {
      "type": "tag",
      "id": "forge:ingots/iron",
      "amount": 1
    }
  ]
}
```

* **Meaning**:

  1. Target the **macerator** quarry recipes.
  2. **Add** two gold ingots and one iron ingot (via tag) to the processing list.

**File path**:

```
data/industrialupgrade/recipes/quantum_quarry/quarry_macerator_add_gold.json
```

---

### Example: Removing from Furnace List

```json
{
  "type": "industrialupgrade:quantum_quarry",
  "recipe_type": "furnace",
  "recipeOperation": "remove",
  "inputs": [
    { "type": "item", "id": "minecraft:coal", "amount": 1 }
  ]
}
```

* **Meaning**: Remove coal from furnace quarry processing.

**File path**:

```
data/industrialupgrade/recipes/quantum_quarry/quarry_furnace_remove_coal.json
```

---

With these examples and mappings, players can easily customize their Quantum Quarry item lists by writing simple JSON files. Place each file under `data/industrialupgrade/recipes/quantum_quarry/` and reload your data pack or restart the game to apply changes.

---

