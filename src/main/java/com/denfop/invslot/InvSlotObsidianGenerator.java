package com.denfop.invslot;

import com.denfop.api.Recipes;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.tiles.base.TileEntityObsidianGenerator;
import ic2.core.block.TileEntityInventory;
import ic2.core.item.upgrade.ItemUpgradeModule;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class InvSlotObsidianGenerator extends InvSlotProcessable {

    public InvSlotObsidianGenerator(TileEntityInventory base1, String name1, int count) {
        super(base1, name1, count);

    }

    public boolean accepts(ItemStack itemStack) {
        return itemStack.isEmpty() || !(itemStack.getItem() instanceof ItemUpgradeModule);

    }

    protected RecipeOutput getOutput(FluidStack container, FluidStack fill, boolean adjustInput) {

        return Recipes.obsidianGenerator.getOutputFor(container, fill, adjustInput, true);

    }

    protected RecipeOutput getOutputFor(FluidStack input, FluidStack input1, boolean adjustInput) {
        return getOutput(input, input1, adjustInput);
    }

    public RecipeOutput process() {
        FluidStack input = ((TileEntityObsidianGenerator) this.base).fluidTank2.getFluid();
        FluidStack input1 = ((TileEntityObsidianGenerator) this.base).fluidTank1.getFluid();
        if (input == null || input1 == null) {
            return null;
        }
        RecipeOutput output = getOutputFor(input1, input, false);

        if (output == null) {
            return null;
        }
        List<ItemStack> itemsCopy = new ArrayList<>(output.items.size());
        itemsCopy.addAll(output.items);
        return new RecipeOutput(output.metadata, itemsCopy);
    }

    public void consume() {

        FluidStack input = ((TileEntityObsidianGenerator) this.base).fluidTank2.getFluid();
        FluidStack input1 = ((TileEntityObsidianGenerator) this.base).fluidTank1.getFluid();
        getOutputFor(input1, input, true);


    }

}
