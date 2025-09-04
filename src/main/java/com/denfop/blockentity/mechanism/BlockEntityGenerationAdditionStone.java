package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.*;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipe.IInputItemStack;
import com.denfop.sound.EnumSound;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityGenerationAdditionStone extends BlockEntityBaseAdditionGenStone implements IHasRecipe {


    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityGenerationAdditionStone(BlockPos pos, BlockState state) {
        super(1, 100, 12, BlockBaseMachine3Entity.gen_addition_stone, pos, state);
        this.inputSlotA = new InventoryRecipes(this, "genadditionstone", this);
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.075));
    }

    public static void addGen(IInputItemStack container, IInputItemStack fill, ItemStack output) {
        Recipes.recipes.addRecipe("genadditionstone", new BaseMachineRecipe(
                new Input(container, fill),
                new RecipeOutput(null, output)
        ));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.gen_addition_stone;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
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
        ), this.granite);
        addGen(
                input.getInput(ModUtils.getCellFromFluid(Fluids.WATER)),
                input.getInput(ModUtils.getCellFromFluid(Fluids.LAVA)),
                this.granite
        );

    }


}
