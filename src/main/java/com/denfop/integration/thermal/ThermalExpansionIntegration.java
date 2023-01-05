package com.denfop.integration.thermal;

import cofh.thermalfoundation.item.ItemMaterial;
import com.denfop.Ic2Items;
import com.denfop.utils.CraftManagerUtils;

public class ThermalExpansionIntegration {

    public static void init(){
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(ItemMaterial.gearElectrum));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(ItemMaterial.gearInvar));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(ItemMaterial.gearIridium));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(ItemMaterial.gearNickel));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(ItemMaterial.gearPlatinum));
        CraftManagerUtils.removeCrafting(CraftManagerUtils.getRecipe(ItemMaterial.gearSilver));
    }
}
