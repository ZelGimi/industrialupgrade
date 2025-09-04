package com.denfop.blockentity.mechanism.generator.energy;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.widget.IType;
import com.denfop.componets.Energy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuGeoGenerator;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryTank;
import com.denfop.screen.ScreenGeoGenerator;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.List;

public class BlockEntityGeoGenerator extends BlockEntityBaseGenerator implements IType {

    public final InventoryFluid fluidSlot;
    public final InventoryOutput outputSlot;

    public final FluidTank fluidTank;
    public final Fluids fluids = this.addComponent(new Fluids(this));
    private final double coef;


    public BlockEntityGeoGenerator(int size, double coef, int tier, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(20.0D * coef, tier, (int) (2400 * coef), block, pos, state);
        this.fluidTank = this.fluids.addTankInsert("fluid", size * 1000, Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.LAVA));
        this.production = Math.round(20.0F * coef * 1);
        this.fluidSlot = new InventoryTank(
                this,
                Inventory.TypeItemSlot.INPUT,
                1,
                InventoryFluid.TypeFluidSlot.INPUT,
                this.fluidTank
        ) {
            @Override
            protected boolean acceptsLiquid(Fluid fluid) {
                return fluid == Fluids.LAVA;
            }
        };
        this.outputSlot = new InventoryOutput(this, 1);
        this.coef = coef;
    }


    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.info_upgrade_energy") + this.coef);
        }

        super.addInformation(stack, tooltip);

    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    public Energy getEnergy() {
        return energy;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        this.fluidSlot.processIntoTank(this.fluidTank, this.outputSlot);

    }

    public ContainerMenuGeoGenerator getGuiContainer(Player entityPlayer) {
        return new ContainerMenuGeoGenerator(entityPlayer, this);
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenGeoGenerator((ContainerMenuGeoGenerator) isAdmin);
    }

    public boolean gainFuel() {
        boolean dirty = false;
        FluidStack ret = this.fluidTank.drain(2, IFluidHandler.FluidAction.SIMULATE);
        if (!ret.isEmpty() && ret.getAmount() >= 2) {
            this.fluidTank.drain(2, IFluidHandler.FluidAction.EXECUTE);
            ++this.fuel;
            dirty = true;
        }

        return dirty;
    }

    public String getOperationSoundFile() {
        return "Generators/GeothermalLoop.ogg";
    }

    public void onBlockBreak(boolean wrench) {
        super.onBlockBreak(false);

    }

}
