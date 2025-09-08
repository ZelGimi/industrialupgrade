---
---
# Smeltery Recipes Documentation

This document explains how to define and add recipes for the **Smeltery** system using JSON configuration files.

---

## JSON Structure

```json
{
  "type": "industrialupgrade:smeltery",
  "operation": "<operation_type>",
  "inputs": [
    {
      "type": "<item|fluid|tag>",
      "id": "<namespace:id>",
      "amount": <integer>
    }
  ],
  "outputs": [
    {
      "type": "<item|fluid|tag>",
      "id": "<namespace:id>",
      "amount": <integer>
    }
  ]
}
```

### Fields

* **operation** – Defines the recipe type.

  * `furnace`
  * `castings_ingot`
  * `castings_gear`
  * `mix`

* **inputs** – A list of recipe inputs. Each input can be:

  * `item` – Requires an item ID from the `item` registry.
  * `fluid` – Requires a fluid ID from the `fluid` registry.
  * `tag` – Requires an ore dictionary tag.

* **outputs** – A list of recipe outputs. Same format as inputs.

* **amount** – Stack size for items, or millibuckets (mB) for fluids.

---

## Recipe Types

### 1. Furnace Recipe (`furnace`)

Converts an **item input** into a **fluid output**.

**Example:** Smelting Iron Ore into Molten Iron.

```json
{
  "type": "industrialupgrade:smeltery",
  "operation": "furnace",
  "inputs": [
    {
      "type": "item",
      "id": "minecraft:iron_ore",
      "amount": 1
    }
  ],
  "outputs": [
    {
      "type": "fluid",
      "id": "iu:molten_iron",
      "amount": 144
    }
  ]
}
```

---

### 2. Ingot Casting (`castings_ingot`)

Casts a **fluid input** into an **item output (ingot)**.

**Example:** Casting Molten Iron into Iron Ingot.

```json
{
  "type": "industrialupgrade:smeltery",
  "operation": "castings_ingot",
  "inputs": [
    {
      "type": "fluid",
      "id": "iu:molten_iron",
      "amount": 144
    }
  ],
  "outputs": [
    {
      "type": "item",
      "id": "minecraft:iron_ingot",
      "amount": 1
    }
  ]
}
```

---

### 3. Gear Casting (`castings_gear`)

Casts a **fluid input** into an **item output (gear)**.

**Example:** Casting Molten Iron into Iron Gear.

```json
{
  "type": "industrialupgrade:smeltery",
  "operation": "castings_gear",
  "inputs": [
    {
      "type": "fluid",
      "id": "iu:molten_iron",
      "amount": 576
    }
  ],
  "outputs": [
    {
      "type": "item",
      "id": "iu:iron_gear",
      "amount": 1
    }
  ]
}
```

---

### 4. Fluid Mixing (`mix`)

Mixes **two or more fluid inputs** into a new **fluid output**.

**Example:** Mixing Molten Copper and Molten Tin into Molten Bronze.

```json
{
  "type": "industrialupgrade:smeltery",
  "operation": "mix",
  "inputs": [
    {
      "type": "fluid",
      "id": "iu:molten_copper",
      "amount": 144
    },
    {
      "type": "fluid",
      "id": "iu:molten_tin",
      "amount": 48
    }
  ],
  "outputs": [
    {
      "type": "fluid",
      "id": "iu:molten_bronze",
      "amount": 192
    }
  ]
}
```

---

## Notes

* **144 mB** = 1 ingot (standard unit).
* Tags can be used as inputs to generalize recipes.
* Only valid combinations of `operation` + `inputs`/`outputs` will be registered.

---

## Template

```json
{
  "type": "industrialupgrade:smeltery",
  "operation": "<furnace|castings_ingot|castings_gear|mix>",
  "inputs": [
    {
      "type": "<item|fluid|tag>",
      "id": "<namespace:id>",
      "amount": <integer>
    }
  ],
  "outputs": [
    {
      "type": "<item|fluid|tag>",
      "id": "<namespace:id>",
      "amount": <integer>
    }
  ]
}
```
