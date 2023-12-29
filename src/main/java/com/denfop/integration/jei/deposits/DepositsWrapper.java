package com.denfop.integration.jei.deposits;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.integration.jei.deposits.DepositsHandler;
import com.denfop.utils.ModUtils;
import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.VeinType;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DepositsWrapper implements IRecipeWrapper {


    private final VeinType vein;

    public DepositsWrapper(DepositsHandler container) {

       this.vein = container.getVeinType();
    }

    public VeinType getVein() {
        return vein;
    }

    public List<ItemStack> getInputs() {

        List<ItemStack> stack = new ArrayList<>();
        if(vein.getHeavyOre() != null){
            stack.add(new ItemStack(vein.getHeavyOre(),1, vein.getMeta()));
        }
        for (ChanceOre ore : vein.getOres()){
            stack.add(new ItemStack(ore.getBlock().getBlock(),1,ore.getMeta()));
        }
        return stack;
    }




    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, this.getInputs());
    }




    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int y = 20;
        int x = 25;
        minecraft.fontRenderer.drawSplitString(Localization.translate("deposists.jei1") + (this.vein.getHeavyOre() != null ?
                        new ItemStack(vein.getHeavyOre(),1, vein.getMeta()).getDisplayName() :
                        new ItemStack(vein.getOres().get(0).getBlock().getBlock(),1,
                                vein.getOres().get(0).getMeta()).getDisplayName() ), 5, 3,
                recipeWidth - 5, 4210752);
        if(this.vein.getHeavyOre() != null) {
            minecraft.fontRenderer.drawSplitString(" 50%", x, y, recipeWidth - x, 4210752);
            for (int i =0; i < vein.getOres().size();i++){
                minecraft.fontRenderer.drawSplitString(" " +  vein.getOres().get(i).getChance() + "%", x, y + 19 + 20 * i,
                        recipeWidth - x, 4210752);
            }
        }else{
            for (int i =0; i < vein.getOres().size();i++){
                minecraft.fontRenderer.drawSplitString(" " +  vein.getOres().get(i).getChance() + "%", x, y + 20 * i,
                        recipeWidth - x, 4210752);
            }
        }

    }

}
