package com.denfop.integration.forestry;

import com.denfop.register.RegisterOreDictionary;
import forestry.api.storage.BackpackManager;
import forestry.api.storage.IBackpackDefinition;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class FIntegration {

    public static void init() {
        if (BackpackManager.backpackInterface != null) {
            IBackpackDefinition definition = BackpackManager.backpackInterface.getBackpackDefinition("forestry.miner");
            if (definition != null) {
                for (int i = 0; i < RegisterOreDictionary.itemNames().size(); i++) {
                    List<ItemStack> Itemstack = OreDictionary.getOres("ore" + RegisterOreDictionary.itemNames().get(i));
                    for (ItemStack stack : Itemstack) {
                        BackpackManager.backpackInterface.addItemToForestryBackpack("forestry.miner", stack);
                    }
                }

                for (int i = 0; i < RegisterOreDictionary.itemNames2().size(); i++) {
                    List<ItemStack> Itemstack = OreDictionary.getOres("ore" + RegisterOreDictionary.itemNames2().get(i));
                    for (ItemStack stack : Itemstack) {
                        BackpackManager.backpackInterface.addItemToForestryBackpack("forestry.miner", stack);
                    }


                }
                for (int i = 0; i < RegisterOreDictionary.itemNames3().size(); i++) {
                    List<ItemStack> Itemstack = OreDictionary.getOres("ore" + RegisterOreDictionary.itemNames3().get(i));
                    for (ItemStack stack : Itemstack) {
                        BackpackManager.backpackInterface.addItemToForestryBackpack("forestry.miner", stack);
                    }


                }
            }
        }
    }

}
