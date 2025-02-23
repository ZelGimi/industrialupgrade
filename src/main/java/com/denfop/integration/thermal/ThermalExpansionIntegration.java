package com.denfop.integration.thermal;

import cofh.thermalexpansion.util.managers.machine.SmelterManager;
import cofh.thermalfoundation.item.ItemMaterial;
import com.denfop.IUItem;
import com.denfop.utils.CraftManagerUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ThermalExpansionIntegration {

    public static void init() {
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(ItemMaterial.gearElectrum));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(ItemMaterial.gearInvar));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(ItemMaterial.gearIridium));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(ItemMaterial.gearNickel));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(ItemMaterial.gearPlatinum));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(ItemMaterial.gearSilver));
    }

}
