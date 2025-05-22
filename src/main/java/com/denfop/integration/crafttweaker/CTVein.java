package com.denfop.integration.crafttweaker;


import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.TypeVein;
import com.denfop.world.vein.VeinType;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.item.IItemStack;
import crafttweaker.mc1120.block.MCItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
    public static ItemStack getItemStack(IItemStack item) {
        if (item == null) {
            return null;
        } else {
            Object internal = item.getInternal();
            if (!(internal instanceof ItemStack)) {
                CraftTweakerAPI.logError("Not a valid item stack: " + item);
            }

            assert internal instanceof ItemStack;
            return new ItemStack(((ItemStack) internal).getItem(), item.getAmount(), item.getDamage());
        }
    }
    @ZenMethod
    public static void addChanceOre(String vein, IItemStack block, int meta, int chance) {
        VeinType type = veinTypeMap.get(vein);
        if (! block.isItemBlock())
            return;
        ItemStack stack = getItemStack(block);
        ItemBlock itemBlock = (ItemBlock) stack.getItem();
        if (type != null) {
            type.addChanceOre(new ChanceOre(itemBlock.getBlock().getStateFromMeta(meta), chance, meta));
        } else {
            VeinType veinType = new VeinType(null, 0, TypeVein.SMALL, new ChanceOre(itemBlock.getBlock().getStateFromMeta(meta), chance, meta));
            veinTypeMap.replace(vein, veinType);
        }

    }

    @ZenMethod
    public static void removeOre(Block block) {
        veinList.add(block);

    }

}
