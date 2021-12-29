package com.denfop.integration.forestry;

import com.denfop.register.RegisterOreDict;
import forestry.api.storage.BackpackManager;
import forestry.api.storage.IBackpackDefinition;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class FIntegration {

    public static void init() {
        IBackpackDefinition definition =BackpackManager.backpackInterface.getBackpackDefinition("forestry.miner");
        if (definition != null) {
            for (int i = 0; i < RegisterOreDict.itemNames().size(); i++) {
                List<ItemStack> Itemstack = OreDictionary.getOres("ore" + RegisterOreDict.itemNames().get(i));
                for(ItemStack stack : Itemstack)
                BackpackManager.backpackInterface.addItemToForestryBackpack("forestry.miner", stack);
            }

            for (int i = 0; i < RegisterOreDict.itemNames2().size(); i++) {
                List<ItemStack> Itemstack = OreDictionary.getOres("ore" + RegisterOreDict.itemNames2().get(i));
                for(ItemStack stack : Itemstack)
                    BackpackManager.backpackInterface.addItemToForestryBackpack("forestry.miner", stack);


            }
            for (int i = 0; i < RegisterOreDict.itemNames3().size(); i++) {
                List<ItemStack> Itemstack = OreDictionary.getOres("ore" + RegisterOreDict.itemNames3().get(i));
                for(ItemStack stack : Itemstack)
                    BackpackManager.backpackInterface.addItemToForestryBackpack("forestry.miner", stack);


            }
        }
    }
}
