package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blockentity.base.BlockEntityMultiMatter;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSimpleMachineEntity;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.recipe.IInputHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityMatter extends BlockEntityMultiMatter implements IHasRecipe {

    public BlockEntityMatter(BlockPos pos, BlockState state) {
        super(1000000F, 10, 5000000, BlockSimpleMachineEntity.generator_matter, pos, state);
        com.denfop.api.Recipes.recipes.addInitRecipes(this);
    }

    public static void addAmplifier(ItemStack stack, int amplification) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        final CompoundTag nbt = new CompoundTag();
        nbt.putInt("amount", amplification);
        com.denfop.api.Recipes.recipes.addRecipe("matterAmplifier", new BaseMachineRecipe(
                new Input(input.getInput(stack)),
                new RecipeOutput(nbt, stack)
        ));

    }

    public MultiBlockEntity getTeBlock() {
        return BlockSimpleMachineEntity.generator_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.simplemachine.getBlock(getTeBlock().getId());
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    @Override
    public void init() {
        addAmplifier(new ItemStack(IUItem.doublescrapBox.getItem()), 405000);
        addAmplifier(IUItem.scrap, 5000);
        addAmplifier(IUItem.scrapBox, 45000);
    }

}
