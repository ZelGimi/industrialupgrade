package com.denfop.invslot;

import com.denfop.api.IPlasticRecipemanager;
import com.denfop.api.Recipes;
import com.denfop.blocks.FluidName;
import com.denfop.tiles.mechanism.TileEntityPlasticCreator;
import ic2.api.recipe.RecipeOutput;
import ic2.api.util.FluidContainerOutputMode;
import ic2.core.block.TileEntityInventory;
import ic2.core.util.LiquidUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvSlotProcessablePlastic extends InvSlotProcessable {

    public InvSlotProcessablePlastic(TileEntityInventory base1, String name1, int count) {
        super(base1, name1, null, count);

    }

    public boolean accepts(ItemStack itemStack) {
        LiquidUtil.FluidOperationResult result = LiquidUtil.drainContainer(itemStack, null, 1000,
                FluidContainerOutputMode.EmptyFullToOutput);

        for (Map.Entry<IPlasticRecipemanager.Input, RecipeOutput> entry : getRecipeList().entrySet()) {
            if (entry.getKey().container.matches(itemStack) || entry.getKey().fill.matches(itemStack)) {
                return itemStack != null;
            }

        }
        if(result != null){
        Fluid fluid = result.fluidChange.getFluid();
        if(fluid.equals(FluidName.fluidpolyeth.getInstance()) || fluid.equals(FluidName.fluidpolyprop.getInstance()))
            return itemStack != null;}

        return false;

    }

    public Map<IPlasticRecipemanager.Input, RecipeOutput> getRecipeList() {
        return Recipes.plastic.getRecipes();
    }

    protected RecipeOutput getOutput(ItemStack container, ItemStack fill, FluidStack fluidStack, boolean adjustInput) {

        return Recipes.plastic.getOutputFor(container, fill, fluidStack, adjustInput, false);

    }

    protected RecipeOutput getOutputFor(ItemStack input, ItemStack input1, FluidStack fluidStack, boolean adjustInput) {
        return getOutput(input, input1, fluidStack, adjustInput);
    }

    public RecipeOutput process() {
        ItemStack input = ((TileEntityPlasticCreator) this.base).inputSlotA.get(0);
        ItemStack input1 = ((TileEntityPlasticCreator) this.base).inputSlotA.get(1);
        FluidStack fluidStack = ((TileEntityPlasticCreator) this.base).fluidTank.getFluid();
        if (fluidStack == null) {
            return null;
        }
        if (input == null) {
            return null;
        }
        if (input1 == null) {
            return null;
        }
        RecipeOutput output = getOutputFor(input, input1, fluidStack, false);
        if (output == null) {
            return null;
        }
        List<ItemStack> itemsCopy = new ArrayList<>(output.items.size());
        itemsCopy.addAll(output.items);
        return new RecipeOutput(output.metadata, itemsCopy);
    }

    public void consume() {

        ItemStack input = ((TileEntityPlasticCreator) this.base).inputSlotA.get(1);
        ItemStack input1 = ((TileEntityPlasticCreator) this.base).inputSlotA.get(0);
        FluidStack fluidStack = ((TileEntityPlasticCreator) this.base).fluidTank.getFluid();

        getOutputFor(input, input1, fluidStack, true);

        if (input != null && input.getCount() <= 0) {
            ((TileEntityPlasticCreator) this.base).inputSlotA.put(0, null);
        }
        if (input1 != null && input1.getCount() <= 0) {
            ((TileEntityPlasticCreator) this.base).inputSlotA.put(1, null);
        }


    }

}
