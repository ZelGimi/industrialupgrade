---
description: Creating JSON Recipes for IndustrialUpgrade (Forge 1.20.1)
---

# KubeJS Integration

Below is a comprehensive guide for players on how to create custom JSON recipes for the `industrialupgrade:universal_recipe` system. This document focuses exclusively on the recipe format and usage—no internal algorithm details are included.

---

## 1. Basic JSON Structure

Every Universal Recipe JSON file must follow this format:

```json
{
  "type": "industrialupgrade:universal_recipe",
  "recipe_type": "<machine_name>",
  "isFluidRecipe": <true|false>,       // optional; defaults to false
  "inputs": [
    // array of input components: item, fluid, or tag
  ],
  "outputs": [
    // array of output components: item or fluid
  ],
  "params": {
    // optional additional parameters (numbers, booleans, strings)
  }
}
```

* `type` must always be `"industrialupgrade:universal_recipe"`.
* `recipe_type` defines the **machine or processing type** to which this recipe belongs (e.g., `"macerator"`, `"fluid_integrator"`, `"crusher"`, `"alloy_smelter"`). The mod registers recipes under this name.
* `isFluidRecipe` (boolean) indicates whether the recipe should also be registered as a fluid-centric recipe. If omitted, it defaults to `false`.
* `inputs` is an array of input definitions (items, fluids, or tags).
* `outputs` is an array of output definitions (items or fluids).
* `params` is an optional object for any additional metadata (e.g., heat value, processing time, flags).

---

## 2. Field Descriptions

### 2.1. `recipe_type`

* **Type:** string
* **Purpose:** Specifies the machine or processing category. This string must match exactly the identifier registered in the mod’s code (e.g., `"macerator"` or `"fluid_integrator"`).

**Example:**

```json
"recipe_type": "fluid_integrator"
```

Registers the recipe under the `fluid_integrator` machine.

---

### 2.2. `isFluidRecipe`

* **Type:** boolean (`true` or `false`)

* **Purpose:** If `true`, the mod will attempt to register the recipe in both the standard machine registry and the fluid‐only registry. Useful when a recipe involves fluids as primary inputs or outputs.

* **When to use `true`:**

    * If you want the recipe to function as **pure‐fluid processing** (only fluid inputs or outputs).
    * If you want **both** a standard item‐based version and a fluid‐based version available. In mixed recipes (items + fluids), setting `isFluidRecipe: true` ensures the mod creates two registrations:

        1. A standard version using item slots (plus one fluid slot if present).
        2. A fluid‐driven version (all fluids plus one item slot if present).

* **When to omit or set `false`:**

    * Pure item‐to‐item recipes.
    * Mixed recipes where you only want the item‐driven variant.
    * Fluid‐only recipes that should NOT register as pure‐fluid.

**Note:** Fluid‐only recipes **require** `isFluidRecipe: true`. Otherwise, they will not appear in the fluid registry.

---

### 2.3. `inputs`

An array of objects, each describing exactly one required input. Each object must include:

| Key      | Description                                                                                 |
| :------- | :------------------------------------------------------------------------------------------ |
| `type`   | One of: `"item"`, `"fluid"`, or `"tag"`.                                                    |
| `id`     | For `item`: a ResourceLocation of a Minecraft or mod item (e.g., `"minecraft:iron_ingot"`). |
|          | For `fluid`: a ResourceLocation of a fluid (e.g., `"minecraft:water"`).                     |
|          | For `tag`: a Forge tag name (e.g., `"forge:ingots/iron"`).                                  |
| `amount` | A positive integer. Defaults to `1` if omitted. For fluids, this is in millibuckets (mB).   |

* **`type: "item"`** creates an `ItemStack` with the specified count.
* **`type: "fluid"`** creates a `FluidStack` (number in mB).
* **`type: "tag"`** creates an ore‐dictionary‐style entry that matches any item in that tag.

**Example:**

```json
"inputs": [
  { "type": "item",  "id": "minecraft:iron_ingot", "amount": 1 },
  { "type": "fluid", "id": "minecraft:water",      "amount": 1000 }
]
```

* Requires 1× iron ingot and 1000 mB (1 bucket) of water.

---

### 2.4. `outputs`

An array of objects describing each output. Each object must include:

| Key      | Description                                                     |
| :------- | :-------------------------------------------------------------- |
| `type`   | One of: `"item"` or `"fluid"`.                                  |
| `id`     | ResourceLocation of the output item or fluid.                   |
| `amount` | A positive integer. Defaults to `1`. For fluids, this is in mB. |

* `tag` is not allowed in `outputs`, only `item` or `fluid`.

**Example:**

```json
"outputs": [
  { "type": "fluid", "id": "minecraft:lava",       "amount": 250 },
  { "type": "item",  "id": "minecraft:gold_nugget", "amount": 3 }
]
```

* Produces 250 mB lava and 3× gold nuggets.

---

### 2.5. `params` (optional)

An object of arbitrary key‐value pairs. Values can be:

* **Boolean:** `true` or `false`.
* **Number:** integer or decimal.
* **String:** plain text.

These parameters become metadata for the machine’s processing logic. Typical uses:

* Processing heat temperature (`heat`) as a number.
* Flags such as `unstable: true` to mark a dangerous recipe.
* Custom strings like `tier: "high"`.

Example:

```json
"params": {
  "heat":     3000,
  "unstable": true,
  "note":     "high_tier_recipe"
}
```

* `heat: 3000` → processed as a number.
* `unstable: true` → boolean flag.
* `note: "high_tier_recipe"` → custom string, available if your machine logic reads it.

---

## 3. Recommended Recipe Types

You can create recipes for **any** machine or processing type as long as that name matches a registered identifier in the mod’s code. Common examples include:

* `macerator`
* `crusher`
* `alloy_smelter`
* `fluid_integrator`
* `fluid_purifier`
* `fusion_reactor`
* `miner`

If you add a new machine in your mod and register it under name `"my_custom_machine"`, then set `"recipe_type": "my_custom_machine"` in your JSON to register there.

---

## 4. Example Recipes

### 4.1. Fluid Integrator (Item + Fluid → Fluid + Item)

```json
{
  "type": "industrialupgrade:universal_recipe",
  "recipe_type": "fluid_integrator",
  "isFluidRecipe": true,
  "inputs": [
    { "type": "item",  "id": "minecraft:iron_ingot", "amount": 1 },
    { "type": "fluid", "id": "minecraft:water",      "amount": 5000 }
  ],
  "outputs": [
    { "type": "fluid", "id": "minecraft:lava",      "amount": 1000 },
    { "type": "item",  "id": "minecraft:gold_ingot", "amount": 1 }
  ],
  "params": { }
}
```

* **Inputs:** 1× iron ingot + 5000 mB water.
* **Outputs:** 1000 mB lava + 1× gold ingot.
* **Note:** `isFluidRecipe: true` ensures both a standard and fluid‐driven registration.

### 4.2. Macerator (Tag → Item) with Parameters

```json
{
  "type": "industrialupgrade:universal_recipe",
  "recipe_type": "macerator",
  "inputs": [
    { "type": "tag", "id": "forge:ingots/iron", "amount": 1 }
  ],
  "outputs": [
    { "type": "item", "id": "minecraft:gold_ingot", "amount": 1 }
  ],
  "params": {
    "heat":     3000,
    "unstable": true
  }
}
```

* **Inputs:** 1× any iron ingot (from tag `forge:ingots/iron`).
* **Outputs:** 1× gold ingot.
* **Parameters:**

    * `heat = 3000` (machine can read required temperature).
    * `unstable = true` (flag for potential failure/explosion).
* **isFluidRecipe omitted** → defaults to `false` (standard item‐based recipe).

### 4.3. Fluid Purifier (Fluid → Fluid Only)

```json
{
  "type": "industrialupgrade:universal_recipe",
  "recipe_type": "fluid_purifier",
  "isFluidRecipe": true,
  "inputs": [
    { "type": "fluid", "id": "industrialupgrade:contaminated_oil", "amount": 3000 }
  ],
  "outputs": [
    { "type": "fluid", "id": "industrialupgrade:clean_oil", "amount": 2500 },
    { "type": "fluid", "id": "industrialupgrade:oil_waste", "amount": 500  }
  ],
  "params": {
    "pressure":  150.5,
    "requires":  "high_temperature"
  }
}
```

* **Inputs:** 3000 mB of `contaminated_oil`.
* **Outputs:** 2500 mB of `clean_oil` + 500 mB of `oil_waste`.
* **Note:** Pure fluid recipe must include `isFluidRecipe: true`.

### 4.4. Mixed Alloy Smelter (Items + Fluid → Fluid + Items)

```json
{
  "type": "industrialupgrade:universal_recipe",
  "recipe_type": "alloy_smelter",
  "isFluidRecipe": true,
  "inputs": [
    { "type": "item",  "id": "minecraft:iron_ingot", "amount": 2 },
    { "type": "item",  "id": "minecraft:gold_ingot", "amount": 1 },
    { "type": "fluid", "id": "industrialupgrade:molten_copper", "amount": 1000 }
  ],
  "outputs": [
    { "type": "fluid", "id": "industrialupgrade:molten_brass", "amount": 1500 },
    { "type": "item",  "id": "industrialupgrade:brass_nugget", "amount": 4 }
  ],
  "params": {
    "temperature": 1600,
    "time":        200,
    "note":        "pressurized_alloy"
  }
}
```

* **Inputs:** 2× iron ingots, 1× gold ingot, and 1000 mB molten copper.
* **Outputs:** 1500 mB molten brass + 4× brass nuggets.
* **Parameters:**

    * `temperature = 1600` (required smelting heat).
    * `time = 200` (processing duration).
    * `note = "pressurized_alloy"` (custom string for machine logic).

---

## 5. Tips & Best Practices

1. **Units for Fluids:** Always specify fluid `amount` in millibuckets (mB). 1 bucket = 1000 mB.
2. **Forge Tags:** When using `type: "tag"`, confirm the tag exists (e.g., `forge:ingots/iron`). Tags allow recipes to accept any item matching that category.
3. **Parameters (`params`):** Use numeric values for temperatures, speeds, or durations. Boolean flags (true/false) can control special behaviors. Strings are for custom markers—ensure your machine code reads them if needed.
4. **Mixed Recipes (Items + Fluids):** Set `isFluidRecipe: true` to generate both item‐driven and fluid‐driven variants. Without this flag, a fluid‐only recipe will fail to register correctly.
5. **File Path & Naming Convention:** Place each recipe under:

   ```
   data/industrialupgrade/recipes/<machine_name>/<recipe_name>.json
   ```

   For example:

   ```
   data/industrialupgrade/recipes/macerator/iron_to_gold.json
   ```
6. **Default Amount:** If an `amount` field is omitted, it defaults to `1`. However, it’s best practice to always specify amounts explicitly—especially for fluids.
7. **No `tag` in Outputs:** `type: "tag"` is valid only in `inputs`, not in `outputs`.
8. **Case Sensitivity:** ResourceLocations and tag names are case‐sensitive. Use all lowercase and the correct namespace (e.g., `minecraft:`, `forge:`, or your mod’s namespace).
9. **Debugging Missing Recipes:** If a recipe does not appear in‐game, check:

    * The JSON path matches `data/industrialupgrade/recipes/<machine_name>/...`.
    * No typos in keys (`type`, `recipe_type`, `inputs`, `outputs`).
    * The mod’s `.log` file for any JSON parsing errors or `IllegalArgumentException: Unknown input type`.

---

## 6. Recipe Template (English)

```json
{
  "type": "industrialupgrade:universal_recipe",
  "recipe_type": "<your_machine_name>",
  "isFluidRecipe": <true|false>,      // remove if no fluids involved
  "inputs": [
    {
      "type":   "<item|fluid|tag>",
      "id":     "<mod_or_minecraft:resource_or_tag>",
      "amount": <integer>
    }
    // ...add as many inputs as needed
  ],
  "outputs": [
    {
      "type":   "<item|fluid>",
      "id":     "<mod_or_minecraft:resource>",
      "amount": <integer>
    }
    // ...add as many outputs as needed
  ],
  "params": {
    // optional key:value pairs for your machine logic
    "customSpeed": 150,
    "requireHeat":  true,
    "description": "tier2_recipe"
  }
}
```

**Guidance:**

* Replace `<your_machine_name>` with the exact identifier used in the mod’s code.
* Omit `isFluidRecipe` if you only need a standard item‐to‐item recipe.
* Include at least one `type: "fluid"` input if you set `isFluidRecipe: true` for fluid processing.
* Always validate `id` strings (e.g., `minecraft:iron_ingot` or `modid:custom_fluid`).

---

## 7. Conclusion

With this guide, you now have a clear, step‐by‐step reference for creating:

* Pure item recipes.
* Pure fluid recipes.
* Mixed item + fluid recipes.

Use tags for flexibility, specify fluid amounts in mB, and leverage `params` for custom machine behavior. Place your JSON files under the correct `data/industrialupgrade/recipes/<machine>/` path, and your new recipes will work seamlessly in IndustrialUpgrade. Happy crafting!
