---
description: Creating JSON Recipes for IndustrialUpgrade (Forge 1.19.2-1.20.1, NeoForge 1.21.1)
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

| Mechanism Name                                    | Recipe name             | isFluidRecipe?   | isItemRecipe?   | Input Item Slots   | Fluid Item Slots   | params                                                                                                                                                                                                                                                                                         |
|:--------------------------------------------------|:------------------------|:---------|:--------|:-------------------|:-------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Alloy Smelter                                     | alloysmelter            | ❌️       | ✅       | 2                  | ❌️                 | temperature (0-5000)                                                                                                                                                                                                                                                                           |
| Enrichment                                        | enrichment              | ❌️       | ✅       | 2                  | ❌️                 | rad_amount (integer 0-infinity)                                                                                                                                                                                                                                                                |
| Dyeing Machine                                    | painter                 | ❌️       | ✅       | 2                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Solarite Plate Manufacturer                       | sunnuriumpanel          | ❌️       | ✅       | 2                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Nuclear Fusion Reactor                            | synthesis               | ❌️       | ✅       | 2                  | ❌️                 | rad_amount (integer 0-infinity) and percent  (integer 0-100)                                                                                                                                                                                                                                   |
| Modification Station                              | upgradeblock            | ❌️       | ✅       | 2                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Module Removal Station                            | antiupgradeblock        | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Improved Alloy Smelter                            | advalloysmelter         | ❌️       | ✅       | 3                  | ❌️                 | temperature (0-5000)                                                                                                                                                                                                                                                                           |
| Stone Generator                                   | genstone                | ❌️       | ✅       | 2                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Circuit Manufacturer                              | microchip               | ❌️       | ✅       | 5                  | ❌️                 | temperature (0-5000)                                                                                                                                                                                                                                                                           |
| Solarite Transformer                              | sunnurium               | ❌️       | ✅       | 4                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Wither Manufacturer                               | wither                  | ❌️       | ✅       | 7                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Advanced Molecular Transformer                    | doublemolecular         | ❌️       | ✅       | 2                  | ❌️                 | energy (double 0-infinity)                                                                                                                                                                                                                                                                     |
| Molecular Transformer                             | molecular               | ❌️       | ✅       | 1                  | ❌️                 | energy (double 0-infinity)                                                                                                                                                                                                                                                                     |
| Chemical Plant                                    | plastic                 | ❌️       | ✅       | 2                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Plastic Plate Machine                             | plasticplate            | ❌️       | ✅       | 1                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Solid Matter Transformer                          | converter               | ❌️       | ✅       | 1                  | ❌️                 | quantitysolid (array of double, 8 elements, each 0.00-infinity) —  quantitysolid_0: matter quantitysolid_1: sunmatter quantitysolid_2: aquamatterquantitysolid_3: nethermatter quantitysolid_4: nightmatter quantitysolid_5: earthmatter quantitysolid_6: endmatter quantitysolid_7: aermatter |
| Macerator                                         | macerator               | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Compressor                                        | compressor              | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Extractor                                         | extractor               | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Recycler (black list)                             | recycler                | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Electric Furnace (without Furnace from Minecraft) | furnace                 | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Extruder                                          | extruding               | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Cutting Machine                                   | cutting                 | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Rolling machine                                   | rolling                 | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Autonomous Farm                                   | farmer                  | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Scrap Assembler                                   | scrap                   | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Combined Macerator                                | comb_macerator          | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Mineral Separator                                 | handlerho               | ❌️       | ✅       | 1                  | ❌️                 | temperature (0-5000), input0..inputN (int 0-100)                                                                                                                                                                                                                                               |
| Rotor Assembler                                   | rotor_assembler         | ❌️       | ✅       | 5                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Rod Manufacturer                                  | rod_assembler           | ❌️       | ✅       | 6                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Ender Transformer                                 | endcollector            | ❌️       | ✅       | 1                  | ❌️                 | need (double 0-infinity)                                                                                                                                                                                                                                                                       |
| Air Transformer                                   | aercollector            | ❌️       | ✅       | 1                  | ❌️                 | need (double 0-infinity)                                                                                                                                                                                                                                                                       |
| Earth Transformer                                 | earthcollector          | ❌️       | ✅       | 1                  | ❌️                 | need (double 0-infinity)                                                                                                                                                                                                                                                                       |
| Nether Transformer                                | nethercollector         | ❌️       | ✅       | 1                  | ❌️                 | need (double 0-infinity)                                                                                                                                                                                                                                                                       |
| Water Transformer                                 | aquacollector           | ❌️       | ✅       | 1                  | ❌️                 | need (double 0-infinity)                                                                                                                                                                                                                                                                       |
| Crystallizer                                      | defaultcollector        | ❌️       | ✅       | 1                  | ❌️                 | need (double 0-infinity)                                                                                                                                                                                                                                                                       |
| Ore Washing Machine                               | orewashing              | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Heat centrifuge                                   | centrifuge              | ❌️       | ✅       | 1                  | ❌️                 | minHeat ( 0-5000)                                                                                                                                                                                                                                                                              |
| Mechanical Stamp                                  | gearing                 | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Water Rotor Assembler                             | water_rotor_assembler   | ❌️       | ✅       | 5                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Welding Machine                                   | welding                 | ❌️       | ✅       | 2                  | ❌️                 | temperature (0-5000)                                                                                                                                                                                                                                                                           |
| Replicator                                        | replicator              | ❌️       | ✅       | 1                  | 0                  | matter (double 0-infinity, 0.5 -> 500 mb, 0.05 -> 50 mb)                                                                                                                                                                                                                                       |
| Battery Factory                                   | battery_factory         | ❌️       | ✅       | 9                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Socket Factory                                    | socket_factory          | ❌️       | ✅       | 6                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Active Matter Factory                             | active_matter_factory   | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Laser Polisher                                    | laser                   | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Graphite Crystallizer                             | graphite_recipe         | ❌️       | ✅       | 2                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Crystal Grower                                    | silicon_recipe          | ❌️       | ✅       | 2                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Moonlight Infuser                                 | solar_glass_recipe      | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Stamping Machine                                  | stamp_vent              | ❌️       | ✅       | 4                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Stamping Machine                                  | stamp_plate             | ❌️       | ✅       | 4                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Stamping Machine                                  | stamp_exchanger         | ❌️       | ✅       | 4                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Stamping Machine                                  | stamp_coolant           | ❌️       | ✅       | 4                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Stamping Machine                                  | stamp_capacitor         | ❌️       | ✅       | 4                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Reactor Rod Manufacturer                          | reactor_simple_rod      | ❌️       | ✅       | 5                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Reactor Rod Manufacturer                          | reactor_dual_rod        | ❌️       | ✅       | 3                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Reactor Rod Manufacturer                          | reactor_quad_rod        | ❌️       | ✅       | 7                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Radioactive Waste Reprocessor                     | waste_recycler          | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Enchanting Machine                                | enchanter_books         | ❌️       | ✅       | 2                  | ❌️                 | exp (integer 0-infinity)                                                                                                                                                                                                                                                                       |
| Anvil                                             | anvil                   | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Upgrade Kit Manufacturer                          | upgrade_machine         | ❌️       | ✅       | 9                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Item Divider                                      | item_divider            | ✅        | ✅       | 1                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Item-to-Fluid Divider                             | item_divider_fluid      | ✅        | ✅       | 1                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Fluid Adapter                                     | fluid_adapter           | ✅        | ✅       | 2                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Fluid Integrator                                  | fluid_integrator        | ✅        | ✅       | 1                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Solid-State Electrolyzer                          | solid_electrolyzer      | ✅        | ✅       | 1                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Primitive Latex Extractor                         | squeezer                | ✅        | ✅       | 1                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Advanced Alloy Smelter                            | impalloysmelter         | ❌️       | ✅       | 4                  | ❌️                 | temperature (0-8000)                                                                                                                                                                                                                                                                           |
| Perfect Alloy Smelter                             | peralloysmelter         | ❌️       | ✅       | 5                  | ❌️                 | temperature (0-10000)                                                                                                                                                                                                                                                                          |
| Solid-Fluid Mixer                                 | solid_fluid_mixer       | ✅        | ✅       | 1                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Primal Fluid Integrator                           | primal_fluid_integrator | ✅        | ✅       | 1                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Neutronium transformer                            | neutron_separator       | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Electric Wilson Chamber                           | positrons               | ❌️       | ✅       | 2                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Primal Laser Polisher                             | primal_laser_polisher   | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Cyclotron Controller                              | cyclotron               | ❌️       | ✅       | 1                  | ❌️                 | chance (integer 0-100), cryogen (consuming cryogen per tick, integer 1-100, default 1), positrons (consuming positrons per tick, integer 1-100, default 1)                                                                                                                                     |
| Smeltery Furnace                                  | smeltery                | ✅        | ✅       | 1                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Solid Mixer                                       | solid_mixer             | ❌️       | ✅       | 2                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Triple Solid Mixer                                | triple_solid_mixer      | ❌️       | ✅       | 3                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Single Fluid Adapter                              | single_fluid_adapter    | ✅        | ✅       | 1                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Electric Programming Table                        | programming             | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Electric Electronic Assembler                     | electronics             | ❌️       | ✅       | 5                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Steam Sharpener                                   | sharpener               | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Radioactive Ore Handler                           | radioactive_handler     | ❌️       | ✅       | 1                  | ❌️                 | random (additional output (not required), integer 0-100)                                                                                                                                                                                                                                       |
| Industrial Radioactive Element Purifier           | ore_purifier            | ❌️       | ✅       | 1                  | ❌️                 | se (consuming solarium per tick, double 0-100)                                                                                                                                                                                                                                                 |
| Quantum Transformer                               | quantummolecular        | ❌️       | ✅       | 2                  | ❌️                 | energy (integer 0-infinity)                                                                                                                                                                                                                                                                    |
| Electric Cable Insulator                          | wire_insulator          | ❌️       | ✅       | 2                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Crystal Charger/Steam Crystal Charger             | charger                 | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Reinforced Anvil                                  | strong_anvil            | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Refractory Furnace                                | refractory_furnace      | ✅        | ✅       | 1                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Electric Refractory Furnace                       | elec_refractory_furnace | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Electric Brewing Machine                          | brewing                 | ❌️       | ✅       | 2                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Sawmill                                           | sawmill                 | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Expanded Stone Generator                          | genadditionstone        | ❌️       | ✅       | 2                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Incubator                                         | incubator               | ✅        | ✅       | 1                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Insulator                                         | insulator               | ✅        | ✅       | 1                  | 2                  | ❌️                                                                                                                                                                                                                                                                                             |
| RNA Collector                                     | rna_collector           | ✅        | ✅       | 1                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Genetic Stabilizer                                | genetic_stabilizer      | ✅        | ✅       | 1                  | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Bee Product Centrifuge                            | genetic_centrifuge      | ❌️       | ✅       | 1                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Inoculator                                        | inoculator              | ❌️       | ✅       | 2                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Genetic Transposer                                | genetic_transposer      | ❌️       | ✅       | 4                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Genetic Polymerizer                               | genetic_polymerizer     | ❌️       | ✅       | 5                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Rover Upgrade Station                             | roverupgradeblock       | ❌️       | ✅       | 2                  | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Rover Assembler                                   | roverassembler          | ❌️       | ✅       | 40                 | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Probe Assembler                                   | probeassembler          | ❌️       | ✅       | 36                 | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Satellite Assembler                               | satelliteassembler      | ❌️       | ✅       | 36                 | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Rocket Assembler                                  | rocketassembler         | ❌️       | ✅       | 37                 | ❌️                 | ❌️                                                                                                                                                                                                                                                                                             |
| Obsidian Generator                                | obsidian                | ✅        | ❌️      | ❌️                 | 2                  | ❌️                                                                                                                                                                                                                                                                                             |
| Gas Mixer                                         | gas_combiner            | ✅        | ❌️      | ❌️                 | 2                  | ❌️                                                                                                                                                                                                                                                                                             |
| Electrolyzer                                      | electrolyzer            | ✅        | ❌️      | ❌️                 | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Fluid Cooler                                      | refrigerator            | ✅        | ❌️      | ❌️                 | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Fluid Separator                                   | fluid_separator         | ✅        | ❌️      | ❌️                 | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Polymerizer                                       | polymerizer             | ✅        | ❌️      | ❌️                 | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Oil Purifier                                      | oil_purifier            | ✅        | ❌️      | ❌️                 | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Electric Latex Dryer                              | dryer                   | ✅        | ❌️      | ❌️                 | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Oil Refinery                                      | oil_refiner             | ✅        | ❌️      | ❌️                 | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Improved Oil Refinery                             | adv_oil_refiner         | ✅        | ❌️      | ❌️                 | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Advanced Oil Refinery                             | imp_oil_refiner         | ✅        | ❌️      | ❌️                 | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Fluid Mixer                                       | fluid_mixer             | ✅        | ❌️      | ❌️                 | 2                  | ❌️                                                                                                                                                                                                                                                                                             |
| Fluid Heater                                      | heat                    | ✅        | ❌️      | ❌️                 | 1                  | temperature (0-1000)                                                                                                                                                                                                                                                                           |
| Gas Chamber                                       | gas_chamber             | ✅        | ❌️      | ❌️                 | 2                  | ❌️                                                                                                                                                                                                                                                                                             |
| Smeltery Casing                                   | ingot_casting           | ✅        | ❌️      | ❌️                 | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Smeltery Casing                                   | gear_casting            | ✅        | ❌️      | ❌️                 | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Solid-Fluid Integrator                            | solid_fluid_integrator  | ✅        | ❌️      | ❌️                 | 2                  | ❌️                                                                                                                                                                                                                                                                                             |
| Mini Smeltery                                     | mini_smeltery           | ✅        | ❌️      | ❌️                 | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Mutatron                                          | mutatron                | ✅        | ❌️      | ❌️                 | 2                  | ❌️                                                                                                                                                                                                                                                                                             |
| Genetic Reverser                                  | reverse_transcriptor    | ✅        | ❌️      | ❌️                 | 1                  | ❌️                                                                                                                                                                                                                                                                                             |
| Genetic Replicator                                | genetic_replicator      | ✅        | ❌️      | ❌️                 | 2                  | ❌️                                                                                                                                                                                                                                                                                             |


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
   
  }
}
```

* **Inputs:** 3000 mB of `contaminated_oil`.
* **Outputs:** 2500 mB of `clean_oil` + 500 mB of `oil_waste`.
* **Note:** Pure fluid recipe must include `isFluidRecipe: true`.




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
