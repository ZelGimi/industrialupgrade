package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSimpleMachine;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileMultiMatter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileMatter extends TileMultiMatter implements IHasRecipe {

    public TileMatter() {
        super(1000000F, 10, 5000000);
        com.denfop.api.Recipes.recipes.addInitRecipes(this);
    }

    public static void addAmplifier(ItemStack stack, int amplification) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("amount", amplification);
        com.denfop.api.Recipes.recipes.addRecipe("matterAmplifier", new BaseMachineRecipe(
                new Input(input.getInput(stack)),
                new RecipeOutput(nbt, stack)
        ));

    }

    public IMultiTileBlock getTeBlock() {
        return BlockSimpleMachine.generator_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.simplemachine;
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    @Override
    public void init() {
        addAmplifier(new ItemStack(IUItem.doublescrapBox), 405000);
        addAmplifier(IUItem.scrap, 5000);
        addAmplifier(IUItem.scrapBox, 45000);
    }

}
