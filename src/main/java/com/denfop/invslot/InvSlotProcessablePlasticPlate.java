package com.denfop.invslot;

import com.denfop.api.IPlasticPlateRecipemanager;
import com.denfop.api.Recipes;
import com.denfop.tiles.mechanism.TileEntityPlasticPlateCreator;
import ic2.api.recipe.RecipeOutput;
import ic2.core.block.TileEntityInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvSlotProcessablePlasticPlate extends InvSlotProcessable {

    public InvSlotProcessablePlasticPlate(TileEntityInventory base1, String name1, int oldStartIndex1, int count) {
        super(base1, name1, null, count);

    }

    public boolean accepts(ItemStack itemStack) {
        for (Map.Entry<IPlasticPlateRecipemanager.Input, RecipeOutput> entry : getRecipeList().entrySet()) {
            if (entry.getKey().container.matches(itemStack)) {
                return itemStack != null;
            }

        }
        return false;
    }

    public Map<IPlasticPlateRecipemanager.Input, RecipeOutput> getRecipeList() {
        return Recipes.plasticplate.getRecipes();
    }

    protected RecipeOutput getOutput(ItemStack container, FluidStack fluidStack, boolean adjustInput) {

        return Recipes.plasticplate.getOutputFor(container, fluidStack, adjustInput, false);

    }

    protected RecipeOutput getOutputFor(ItemStack input, FluidStack fluidStack, boolean adjustInput) {
        return getOutput(input, fluidStack, adjustInput);
    }

    public RecipeOutput process() {
        ItemStack input = ((TileEntityPlasticPlateCreator) this.base).inputSlotA.get(0);
        FluidStack fluidStack = ((TileEntityPlasticPlateCreator) this.base).fluidTank.getFluid();
        if (fluidStack == null) {
            return null;
        }
        if (input == null) {
            return null;
        }

        RecipeOutput output = getOutputFor(input, fluidStack, false);
        if (output == null) {
            return null;
        }
        List<ItemStack> itemsCopy = new ArrayList<>(output.items.size());
        itemsCopy.addAll(output.items);
        return new RecipeOutput(output.metadata, itemsCopy);
    }

    public void consume() {

        ItemStack input = ((TileEntityPlasticPlateCreator) this.base).inputSlotA.get(0);
        FluidStack fluidStack = ((TileEntityPlasticPlateCreator) this.base).fluidTank.getFluid();

        getOutputFor(input, fluidStack, true);

        if (input != null && input.getCount() <= 0) {
            ((TileEntityPlasticPlateCreator) this.base).inputSlotA.put(0, null);
        }


    }


}
