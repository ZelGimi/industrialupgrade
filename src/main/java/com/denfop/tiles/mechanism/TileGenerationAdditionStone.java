package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipe.IInputItemStack;
import com.denfop.utils.ModUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class TileGenerationAdditionStone extends TileBaseAdditionGenStone implements IHasRecipe {


    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileGenerationAdditionStone() {
        super(1, 100, 12);
        this.inputSlotA = new InvSlotRecipes(this, "genadditionstone", this);
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

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.gen_addition_stone;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
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
                input.getInput(ModUtils.getCellFromFluid("lava")),
                input.getInput(ModUtils.getCellFromFluid("water")),
                this.granite
        );

    }


}
