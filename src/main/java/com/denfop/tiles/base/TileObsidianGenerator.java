package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerObsidianGenerator;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiObsidianGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import java.util.EnumSet;
import java.util.Set;

public class TileObsidianGenerator extends TileBaseObsidianGenerator implements IHasRecipe {


    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileObsidianGenerator(BlockPos pos, BlockState state) {
        super(1, 300, 1, BlockBaseMachine2.gen_obsidian, pos, state);
        Recipes.recipes.getRecipeFluid().addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.25));
    }

    public void init() {


        Recipes.recipes.getRecipeFluid().addRecipe("obsidian", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(Fluids.WATER, 1000), new FluidStack(Fluids.LAVA, 1000)
        ), new RecipeOutput(null, new ItemStack(Blocks.OBSIDIAN))));


    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.gen_obsidian;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1.getBlock(this.getTeBlock().getId());
    }

    public String getInventoryName() {

        return Localization.translate("iu.blockObsGen.name");
    }


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiObsidianGenerator((ContainerObsidianGenerator) isAdmin);
    }


    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract,
                UpgradableProperty.FluidInput
        );
    }


}
