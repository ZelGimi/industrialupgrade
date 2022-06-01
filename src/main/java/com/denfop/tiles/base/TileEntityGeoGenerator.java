package com.denfop.tiles.base;

import com.denfop.container.ContainerGeoGenerator;
import com.denfop.gui.GuiGeoGenerator;
import ic2.core.ContainerBase;
import ic2.core.block.comp.Energy;
import ic2.core.block.comp.Fluids;
import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;
import ic2.core.block.invslot.InvSlot.Access;
import ic2.core.block.invslot.InvSlot.InvSide;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquid.OpType;
import ic2.core.block.invslot.InvSlotConsumableLiquidByTank;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidEvent.FluidSpilledEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityGeoGenerator extends TileEntityBaseGenerator {

    public final InvSlotConsumableLiquid fluidSlot;
    public final InvSlotOutput outputSlot;

    public final FluidTank fluidTank;
    public final Fluids fluids = this.addComponent(new Fluids(this));


    public TileEntityGeoGenerator(int size, double coef) {
        super(20.0D * coef, 1, (int) (2400 * coef));
        this.fluidTank = this.fluids.addTankInsert("fluid", size * 1000, Fluids.fluidPredicate(FluidRegistry.LAVA));
        this.production = Math.round(20.0F * coef * ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/geothermal"));
        this.fluidSlot = new InvSlotConsumableLiquidByTank(
                this,
                "fluidSlot",
                Access.I,
                1,
                InvSide.ANY,
                OpType.Drain,
                this.fluidTank
        );
        this.outputSlot = new InvSlotOutput(this, "output", 1);
    }

    public Energy getEnergy() {
        return energy;
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.fluidSlot.processIntoTank(this.fluidTank, this.outputSlot)) {
            this.markDirty();
        }

    }

    public ContainerBase<TileEntityGeoGenerator> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerGeoGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiGeoGenerator(new ContainerGeoGenerator(entityPlayer, this));
    }

    public boolean gainFuel() {
        boolean dirty = false;
        FluidStack ret = this.fluidTank.drainInternal(2, false);
        if (ret != null && ret.amount >= 2) {
            this.fluidTank.drainInternal(2, true);
            ++this.fuel;
            dirty = true;
        }

        return dirty;
    }

    public String getOperationSoundFile() {
        return "Generators/GeothermalLoop.ogg";
    }

    protected void onBlockBreak() {
        super.onBlockBreak();
        FluidEvent.fireEvent(new FluidSpilledEvent(new FluidStack(FluidRegistry.LAVA, 1000), this.getWorld(), this.pos));
    }

}
