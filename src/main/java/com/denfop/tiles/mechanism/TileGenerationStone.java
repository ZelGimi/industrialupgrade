package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipe.IInputItemStack;
import com.denfop.utils.ModUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class TileGenerationStone extends TileBaseGenStone implements IHasRecipe {


    public TileGenerationStone() {
        super(1, 100, 12);
        this.inputSlotA = new InvSlotRecipes(this, "genstone", this);
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addGen(IInputItemStack container, IInputItemStack fill, ItemStack output) {
        Recipes.recipes.addRecipe("genstone", new BaseMachineRecipe(
                new Input(container, fill),
                new RecipeOutput(null, output)
        ));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.gen_stone;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.gen_cobblectone.getSoundEvent();
    }

    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        addGen(input.getInput(new ItemStack(Items.LAVA_BUCKET), 1), input.getInput(
                new ItemStack(Items.WATER_BUCKET),
                1
        ), new ItemStack(Blocks.COBBLESTONE, 8));
        addGen(
                input.getInput(ModUtils.getCellFromFluid("lava")),
                input.getInput(ModUtils.getCellFromFluid("water")),
                new ItemStack(Blocks.COBBLESTONE, 8)
        );

    }


}
