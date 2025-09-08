---

description: Adding Items to Colonies and Expeditions (Space Objects)
---
# Adding Resources to Space Objects

This guide explains how to configure **items and fluids** that can be obtained through **colonies** or **exploration missions** (rovers, probes, satellites, rockets) on planets, satellites, and asteroids.

---

## 1. General Notes

* Recipes are stored under:

  ```
  data/<namespace>/recipes/space/<colony|exploration>/<object_name>.json
  ```

* Supported input types:

  * `item` → Normal Minecraft item.
  * `fluid` → Fluid resource.
  * `tag` → Item tag (ore dictionary style).

* The `inputs` array can hold multiple items or fluids.

* Quantities are integers (`amount`).

* If the `body` (planet, satellite, asteroid) does not exist, loading will fail.

---

## 2. Colony Resources

Colonies allow production of base resources once established on a celestial body.

```json
{
  "type": "industrialupgrade:colony_resource_add",
  "body": "mars",
  "level": 2,
  "inputs": [
    { "type": "item", "id": "minecraft:iron_ingot", "amount": 4 },
    { "type": "fluid", "id": "minecraft:water", "amount": 1000 }
  ]
}
```

**Fields:**

| Key      | Type   | Description                                                           |
| -------- | ------ | --------------------------------------------------------------------- |
| `body`   | string | Name of the planet, satellite, or asteroid where the colony is built. |
| `level`  | short  | Colony level required for these resources.                            |
| `inputs` | array  | List of resources (items, fluids, or tags).                           |

**Inputs format:**

| Key      | Type   | Description                             |
| -------- | ------ | --------------------------------------- |
| `type`   | string | `item`, `fluid`, or `tag`.              |
| `id`     | string | Resource identifier (namespace\:path).  |
| `amount` | int    | Quantity (items or fluid millibuckets). |

---

## 3. Exploration Resources (Expeditions)

Exploration missions yield resources when sending rovers, probes, satellites, or rockets to bodies.

```json
{
  "type": "industrialupgrade:body_resource",
  "body": "mars",
  "percent": 50,
  "chance": 20,
  "typeRover": "rover",
  "typeOperation": "add",
  "inputs": [
    { "type": "item", "id": "minecraft:redstone", "amount": 3 },
    { "type": "fluid", "id": "minecraft:lava", "amount": 500 }
  ]
}
```

**Fields:**

| Key             | Type   | Description                                               |
| --------------- | ------ | --------------------------------------------------------- |
| `body`          | string | Name of the celestial body being explored.                |
| `percent`       | int    | Percent progress value for resource availability.         |
| `chance`        | int    | Chance (%) of successfully finding the resource.          |
| `typeRover`     | string | Type of mission: `rover`, `probe`, `satellite`, `rocket`. |
| `typeOperation` | string | Operation type (`add`).                                   |
| `inputs`        | array  | List of resources gained if successful.                   |

---

## 4. Supported Rover Types

| Rover Type  | Description                                 |
| ----------- | ------------------------------------------- |
| `rover`     | Ground exploration rover.                   |
| `probe`     | Automated probe missions.                   |
| `satellite` | Satellite resource scanning.                |
| `rocket`    | Heavy exploration missions. |

---

## 5. Examples

### Example Colony Recipe (Moon Base)

```json
{
  "type": "industrialupgrade:colony_resource_add",
  "body": "moon",
  "level": 1,
  "inputs": [
    { "type": "item", "id": "minecraft:stone", "amount": 32 },
    { "type": "fluid", "id": "minecraft:oxygen", "amount": 500 }
  ]
}
```

### Example Exploration Recipe (Asteroid Mining)

```json
{
  "type": "industrialupgrade:body_resource",
  "body": "asteroid",
  "percent": 70,
  "chance": 35,
  "typeRover": "probe",
  "typeOperation": "add",
  "inputs": [
    { "type": "tag", "id": "forge:ores/iron", "amount": 5 }
  ]
}
```

---

## 6. Tips & Best Practices

1. Colonies unlock more resources at **higher levels**.
2. Use `percent` and `chance` in exploration recipes to balance rarity.
3. Choose rover types logically: satellites for scanning, rovers for mining, rockets for rare finds.
4. Use `tag` inputs to make recipes more flexible.

---

## 7. Template

```json
{
  "type": "indudstrialupgrade:body_resource",
  "body": "<celestial_body>",
  // colony specific
  "level": <short>,
  // exploration specific
  "percent": <int>,
  "chance": <int>,
  "typeRover": "<rover|probe|satellite|rocket>",
  "typeOperation": "add",
  "inputs": [
    { "type": "<item|fluid|tag>", "id": "<namespace:path>", "amount": <int> }
  ]
}
```

---

## 8. Conclusion

By configuring colony and exploration recipes, you can:

* Make colonies generate items and fluids as players upgrade them.
* Reward exploration missions with rare resources based on chance and rover type.

This adds depth and progression to space gameplay, making every mission and colony expansion meaningful.

---
