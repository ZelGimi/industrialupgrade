---
description: Creating JSON Space Objects (Systems, Stars, Planets, Satellites, Asteroids)
---

# Creating Custom Space Objects

This guide explains how players can create their **own space systems and celestial bodies** using JSON files.  
Each object type (system, star, planet, satellite, asteroid) has its own format with required and optional fields.  

---

## 1. General Notes

* JSON files must be placed in your data pack or mod under:

  ```
  data/<namespace>/recipes/space/<object_type>/<object_name>.json
  ```

  Example for a planet:

  ```
  data/myspace/recipes/space/planet/mars.json
  ```

* All resource identifiers (`id`, `texture`, `system`, `star`, `planet`) are case-sensitive.
* Texture paths are written **with `.png`**
* If a required object (system, star, planet) is not found, the game will throw an error when loading.

---

## 2. System

A **system** is the root container for stars and planets.

```json
{
  "name": "сentauri",
  "distance": 1
}
```

**Fields:**

| Key       | Type    | Description                                                                 |
|-----------|---------|-----------------------------------------------------------------------------|
| `name`    | string  | Unique name of the system.                                                  |
| `distance`| int     | Distance of this system from the solar system |

---

## 3. Star

Stars must belong to a system and are defined with angle and size.

```json
{
  "name": "сentauri_a",
  "system": "сentauri",
  "texture": "modid:textures/star/сentauri_a.png",
  "angle": 0,
  "size": 1.0
}
```

**Fields:**

| Key       | Type    | Description                                                                 |
|-----------|---------|-----------------------------------------------------------------------------|
| `name`    | string  | Star name.                                                                  |
| `system`  | string  | System name where this star belongs.                                        |
| `texture` | string  | Path to texture (with `.png`).                                           |
| `angle`   | int     | Orbital angle position in system layout.                                   |
| `size`    | double  | Size of the star (relative scale).                                         |

---

## 4. Planet

Planets orbit stars inside a system.

```json
{
  "name": "proxima_b",
  "system": "сentauri",
  "texture": "modid:textures/planet/proxima_b.png",
  "level": "TWO",
  "star": "сentauri_a",
  "temperature": -60,
  "pressure": false,
  "distance": 1.5,
  "type": "NEUTRAL",
  "oxygen": false,
  "colonies": true,
  "angle": 45,
  "time": 687.0,
  "size": 6.8,
  "rotation": 24.6
}
```

**Fields:**

| Key          | Type     | Description                                                                 |
|--------------|----------|-----------------------------------------------------------------------------|
| `name`       | string   | Planet name.                                                               |
| `system`     | string   | Parent system.                                                             |
| `texture`    | string   | Path to texture.                                                           |
| `level`      | Enum     | level (e.g., `ONE`, `TWO` to `EIGHT`).                        |
| `star`       | string   | The star this planet orbits.                                               |
| `temperature`| int      | Average surface temperature (°C).                                          |
| `pressure`   | boolean  | Whether the planet has atmospheric pressure.                               |
| `distance`   | double   | Distance from its star (relative units).                                   |
| `type`       | Enum     | Planet type (`DANGEROUS`, `NEUTRAL`, `SAFE`).                                        |
| `oxygen`     | boolean  | Does the planet have breathable oxygen?                                    |
| `colonies`   | boolean  | Can colonies exist here?                                                   |
| `angle`      | int      | Orbital angle relative to star.                                            |
| `time`       | double   | Orbital period (days).                                                     |
| `size`       | double   | Relative size.                                                             |
| `rotation`   | double   | Rotation speed (hours).                                                    |

---

## 5. Satellite (Moons)

Satellites orbit planets.

```json
{
  "name": "proxima_b_a",
  "system": "сentauri",
  "texture": "modid:textures/satellite/proxima_b_a.png",
  "level": "THREE",
  "planet": "proxima_b",
  "temperature": -40,
  "pressure": false,
  "distance": 0.05,
  "type": "DANGEROUS",
  "oxygen": false,
  "colonies": false,
  "angle": 30,
  "time": 0.3,
  "size": 1.2,
  "rotation": 10.0
}
```

**Fields:** same as planet, except:

* `planet` — parent planet name instead of `star`.

---

## 6. Asteroid Belt

Asteroids orbit stars and may spawn in belts.

```json
{
  "name": "asteroid_belt",
  "system": "сentauri",
  "texture": "modid:textures/celestial/asteroid_belt",
  "level": "FOUR",
  "star": "сentauri_a",
  "temperature": -150,
  "distance": 2.8,
  "type": "NEUTRAL",
  "colonies": false,
  "angle": 180,
  "time": 1500.0,
  "size": 0.5,
  "rotation": 5.0,
  "minLocation": 2.5,
  "maxLocation": 3.5,
  "amount": 1000
}
```

**Fields:**

| Key          | Type     | Description                                                                 |
|--------------|----------|-----------------------------------------------------------------------------|
| `name`       | string   | Asteroid name.                                                               |
| `system`     | string   | Parent system.                                                             |
| `texture`    | string   | Path to texture.                                                           |
| `level`      | Enum     | level (e.g., `ONE`, `TWO` to `EIGHT`).                        |
| `star`       | string   | The star this planet orbits.                                               |
| `temperature`| int      | Average surface temperature (°C).                                          |
| `distance`   | double   | Distance from its star (relative units).                                   |
| `type`       | Enum     | Planet type (`DANGEROUS`, `NEUTRAL`, `SAFE`).                                        |
| `colonies`   | boolean  | Can colonies exist here?                                                   |
| `angle`      | int      | Orbital angle relative to star.                                            |
| `time`       | double   | Orbital period (days).                                                     |
| `size`       | double   | Relative size.                                                             |
| `rotation`   | double   | Rotation speed (hours).                                                    |
| `minLocation`   | double   | Min distance from its star for render                                                      |
| `maxLocation`   | double   | Max distance from its star for render                                                    |
| `amount`   | int   | number of asteroids for render                                                    |

---

## 7. Tips & Best Practices

1. Always create a **system first**, then stars, then planets, satellites, and asteroid belts.  
2. Use consistent lowercase names for `system`, `star`, and `planet` references.  
3. Textures must exist in your resource pack. Example path:

   ```
   assets/modid/textures/<system|star|planet|satellite|asteroid>/<unique_name>.png
   ```

4. Match `level` and `type` to existing enums (`ONE`, `TWO`to `EIGHT`; `DANGEROUS`, `NEUTRAL`, `SAFE`).  
5. Distances, times, and sizes are **relative**, not fixed real units. Balance them for gameplay.  




---

## 8. Conclusion

With this system, you can build your own galaxies:  

* Define **systems** as containers.  
* Add **stars** inside them.  
* Place **planets** orbiting stars.  
* Attach **satellites** to planets.  
* Create **asteroid belts** around stars.  

Carefully configure values for distance, time, size, and rotation to shape your custom space world.  

---

