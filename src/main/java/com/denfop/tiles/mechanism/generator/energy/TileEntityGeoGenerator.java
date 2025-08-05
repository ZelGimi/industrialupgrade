package com.denfop.tiles.mechanism.generator.energy;

import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.componets.Energy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerGeoGenerator;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiGeoGenerator;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotTank;
import com.denfop.utils.Keyboard;
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

public class TileEntityGeoGenerator extends TileEntityBaseGenerator implements IType {

    public final InvSlotFluid fluidSlot;
    public final InvSlotOutput outputSlot;

    public final FluidTank fluidTank;
    public final Fluids fluids = this.addComponent(new Fluids(this));
    private final double coef;


    public TileEntityGeoGenerator(int size, double coef, int tier, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(20.0D * coef, tier, (int) (2400 * coef), block, pos, state);
        this.fluidTank = this.fluids.addTankInsert("fluid", size * 1000, Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.LAVA));
        this.production = Math.round(20.0F * coef * 1);
        this.fluidSlot = new InvSlotTank(
                this,
                InvSlot.TypeItemSlot.INPUT,
                1,
                InvSlotFluid.TypeFluidSlot.INPUT,
                this.fluidTank
        ){
            @Override
            protected boolean acceptsLiquid(Fluid fluid) {
                return fluid == Fluids.LAVA;
            }
        };
        this.outputSlot = new InvSlotOutput(this, 1);
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

    public ContainerGeoGenerator getGuiContainer(Player entityPlayer) {
        return new ContainerGeoGenerator(entityPlayer, this);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiGeoGenerator((ContainerGeoGenerator) isAdmin);
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
