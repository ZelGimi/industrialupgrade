package com.denfop.tiles.mechanism.generator.energy;

import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerGeoGenerator;
import com.denfop.gui.GuiGeoGenerator;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotTank;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidEvent.FluidSpilledEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class TileEntityGeoGenerator extends TileEntityBaseGenerator implements IType {

    public final InvSlotFluid fluidSlot;
    public final InvSlotOutput outputSlot;

    public final FluidTank fluidTank;
    public final Fluids fluids = this.addComponent(new Fluids(this));
    private final double coef;


    public TileEntityGeoGenerator(int size, double coef, int tier) {
        super(20.0D * coef, tier, (int) (2400 * coef));
        this.fluidTank = this.fluids.addTankInsert("fluid", size * 1000, Fluids.fluidPredicate(FluidRegistry.LAVA));
        this.production = Math.round(20.0F * coef * 1);
        this.fluidSlot = new InvSlotTank(
                this,
                InvSlot.TypeItemSlot.INPUT,
                1,
                InvSlotFluid.TypeFluidSlot.INPUT,
                this.fluidTank
        );
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.coef = coef;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.info_upgrade_energy") + this.coef);
        }
        if (this.getComp(AdvEnergy.class) != null) {
            AdvEnergy energy = this.getComp(AdvEnergy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }


    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    public AdvEnergy getEnergy() {
        return energy;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        this.fluidSlot.processIntoTank(this.fluidTank, this.outputSlot);

    }

    public ContainerGeoGenerator getGuiContainer(EntityPlayer entityPlayer) {
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

    public void onBlockBreak(boolean wrench) {
        super.onBlockBreak(false);
        FluidEvent.fireEvent(new FluidSpilledEvent(new FluidStack(FluidRegistry.LAVA, 1000), this.getWorld(), this.pos));
    }

}
