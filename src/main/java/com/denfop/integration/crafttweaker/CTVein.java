package com.denfop.integration.crafttweaker;


import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.TypeVein;
import com.denfop.world.vein.VeinType;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.block.Block;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ZenClass("mods.industrialupgrade.vein")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTVein {

    public static Map<String, VeinType> veinTypeMap = new HashMap<>();
    public static List<Block> veinList = new ArrayList<>();

    @ZenMethod
    public static void addVein(String vein) {
        veinTypeMap.put(vein, null);
    }

    @ZenMethod
    public static void addChanceOre(String vein, Block block, int meta, int chance) {
        VeinType type = veinTypeMap.get(vein);
        if (type != null) {
            type.addChanceOre(new ChanceOre(block.getStateFromMeta(meta), chance, meta));
        } else {
            VeinType veinType = new VeinType(null, 0, TypeVein.SMALL, new ChanceOre(block.getStateFromMeta(meta), chance, meta));
            veinTypeMap.replace(vein, veinType);
        }

    }

    @ZenMethod
    public static void removeOre(Block block) {
        veinList.add(block);

    }

}
