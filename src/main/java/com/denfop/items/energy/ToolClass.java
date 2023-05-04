package com.denfop.items.energy;

import ic2.core.ref.IC2Material;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum ToolClass implements IToolClass {
    Axe("axe", Material.WOOD, Material.PLANTS, Material.VINE),
    Pickaxe("pickaxe", Material.IRON, Material.ANVIL, Material.ROCK),
    Shears("shears", Blocks.WEB, Blocks.WOOL, Blocks.REDSTONE_WIRE, Blocks.TRIPWIRE, Material.LEAVES),
    Shovel("shovel", Blocks.SNOW_LAYER, Blocks.SNOW),
    Sword("sword", Blocks.WEB, Material.PLANTS, Material.VINE, Material.CORAL, Material.LEAVES, Material.GOURD),
    Hoe(null, Blocks.DIRT, Blocks.GRASS, Blocks.MYCELIUM),
    Wrench("wrench", IC2Material.MACHINE, IC2Material.PIPE),
    WireCutter("wire_cutter", IC2Material.CABLE),
    Crowbar("crowbar", Blocks.RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL, Blocks.GOLDEN_RAIL);

    public final String name;
    public final Set<Object> whitelist;
    public final Set<Object> blacklist;

    ToolClass(String name, Object... whitelist) {
        this(name, whitelist, new Object[0]);
    }

    ToolClass(String name, Object[] whitelist, Object[] blacklist) {
        this.name = name;
        this.whitelist = new HashSet<>(Arrays.asList(whitelist));
        this.blacklist = new HashSet<>(Arrays.asList(blacklist));
    }

    public String getName() {
        return this.name;
    }

    public Set<Object> getWhitelist() {
        return this.whitelist;
    }

    public Set<Object> getBlacklist() {
        return this.blacklist;
    }
}
