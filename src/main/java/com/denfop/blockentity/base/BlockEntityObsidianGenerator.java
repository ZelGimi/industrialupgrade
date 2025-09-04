package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine2Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuObsidianGenerator;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenObsidianGenerator;
import com.denfop.utils.Localization;
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

public class BlockEntityObsidianGenerator extends BlockEntityBaseObsidianGenerator implements IHasRecipe {


    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityObsidianGenerator(BlockPos pos, BlockState state) {
        super(1, 300, 1, BlockBaseMachine2Entity.gen_obsidian, pos, state);
        Recipes.recipes.getRecipeFluid().addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.25));
    }

    public void init() {


        Recipes.recipes.getRecipeFluid().addRecipe("obsidian", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(Fluids.WATER, 1000), new FluidStack(Fluids.LAVA, 1000)
        ), new RecipeOutput(null, new ItemStack(Blocks.OBSIDIAN))));


    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine2Entity.gen_obsidian;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1.getBlock(this.getTeBlock().getId());
    }

    public String getInventoryName() {

        return Localization.translate("iu.blockObsGen.name");
    }


    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenObsidianGenerator((ContainerMenuObsidianGenerator) isAdmin);
    }


    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract,
                UpgradableProperty.FluidInput
        );
    }


}
