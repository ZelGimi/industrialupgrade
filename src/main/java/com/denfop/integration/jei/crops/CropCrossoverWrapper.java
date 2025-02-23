package com.denfop.integration.jei.crops;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.agriculture.ICrop;
import com.denfop.integration.jei.crops.CropCrossoverHandler;
import com.denfop.utils.ModUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CropCrossoverWrapper implements IRecipeWrapper {


    public final ICrop output;
    public final List<ICrop> inputs;
    public CropCrossoverWrapper(CropCrossoverHandler container) {


        this.output = container.output;
        this.inputs = container.inputs;

    }




    public ItemStack getOutputs() {
        ItemStack stack = new ItemStack(IUItem.crops);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setInteger("crop_id",output.getId());
        return stack;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutput(VanillaTypes.ITEM, this.getOutputs());
        ingredients.setInputs(VanillaTypes.ITEM, this.getInputs());
    }

    public List<ItemStack> getInputs() {
        List<ItemStack> itemStackList = new ArrayList<>();
        inputs.forEach(crop -> {
            ItemStack stack = new ItemStack(IUItem.crops);
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            nbt.setInteger("crop_id",crop.getId());
            itemStackList.add(stack);
        });
        return itemStackList;
    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawString("+", 45, 48, 4210752);
        minecraft.fontRenderer.drawString("->", 65, 48, 4210752);
        minecraft.fontRenderer.drawSplitString(Localization.translate("iu.cop.crossing"), 15, 78, recipeWidth - 5,4210752);
    }

}
