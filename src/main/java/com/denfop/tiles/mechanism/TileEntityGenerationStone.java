package com.denfop.tiles.mechanism;

import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.core.item.type.CellType;
import ic2.core.ref.ItemName;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TileEntityGenerationStone extends TileEntityBaseGenStone implements IHasRecipe {


    public TileEntityGenerationStone() {
        super(1, 100, 12);
        this.inputSlotA = new InvSlotRecipes(this, "genstone", this);
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addGen(IRecipeInput container, IRecipeInput fill, ItemStack output) {
        Recipes.recipes.addRecipe("genstone", new BaseMachineRecipe(
                new Input(container, fill),
                new RecipeOutput(null, output)
        ));
    }

    public void init() {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        addGen(input.forStack(new ItemStack(Items.LAVA_BUCKET), 1), input.forStack(
                new ItemStack(Items.WATER_BUCKET),
                1
        ), new ItemStack(Blocks.COBBLESTONE, 8));
        addGen(
                input.forStack(ItemName.cell.getItemStack(CellType.lava)),
                input.forStack(ItemName.cell.getItemStack(CellType.water)),
                new ItemStack(Blocks.COBBLESTONE, 8)
        );
        addGen(
                input.forStack(ModUtils.getCellFromFluid("lava")),
                input.forStack(ModUtils.getCellFromFluid("water")),
                new ItemStack(Blocks.COBBLESTONE, 8)
        );

    }


}
