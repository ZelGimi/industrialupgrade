package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.container.ContainerPlasticCreator;
import com.denfop.gui.GuiPlasticCreator;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileBasePlasticCreator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TilePlasticCreator extends TileBasePlasticCreator implements IHasRecipe {

    public TilePlasticCreator() {
        super(1, 300, 1);
        this.inputSlotA = new InvSlotRecipes(this, "plastic", this, this.fluidTank);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.inputSlotA.setInvSlotConsumableLiquidByList(this.fluidSlot);
        Recipes.recipes.addInitRecipes(this);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.plastic_creator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1;
    }

    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidRegistry.WATER, 1000),
                        input.getInput(IUItem.PolyethCell),
                        input.getInput(IUItem.PolypropCell)
                ),
                new RecipeOutput(null, new ItemStack(IUItem.plast))
        ));

        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidazot.getInstance(), 12000),
                        input.getInput("blockVitalium"),
                        input.getInput(new ItemStack(IUItem.crafting_elements, 1, 269))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.crafting_elements, 1, 270))
        ));
    }

    public String getInventoryName() {

        return Localization.translate("iu.blockPlasticCreator.name");
    }

    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiPlasticCreator(new ContainerPlasticCreator(entityPlayer, this));

    }

    public String getStartSoundFile() {
        return "Machines/plastic.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing,
                UpgradableProperty.FluidConsuming
        );
    }

}
