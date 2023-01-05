package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerElectrolyzer;
import com.denfop.gui.GuiElectrolyzer;
import com.denfop.invslot.InvSlotElectrolyzer;
import ic2.core.ContainerBase;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class TileEntityElectrolyzer extends TileEntityBaseLiquedMachine implements IManufacturerBlock {

    public final InvSlotElectrolyzer cathodeslot;
    public final InvSlotElectrolyzer anodeslot;

    public TileEntityElectrolyzer() {
        super(24000, 14, 2, 3, new boolean[]{false, true, true}, new boolean[]{true, false, false},
                new Fluid[]{FluidRegistry.WATER, FluidName.fluidhyd.getInstance(),
                        FluidName.fluidoxy.getInstance()}
        );
        this.cathodeslot = new InvSlotElectrolyzer(this, "input5", 1);
        this.anodeslot = new InvSlotElectrolyzer(this, "input6", 0);

    }


    @Override
    public ContainerBase<TileEntityElectrolyzer> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerElectrolyzer(entityPlayer, this);

    }

    public String getStartSoundFile() {
        return "Machines/electrolyzer.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public void onNetworkEvent(int event) {
        if (this.audioSource == null && getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, getStartSoundFile());
        }
        switch (event) {
            case 0:
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (getInterruptSoundFile() != null) {
                        IUCore.audioManager.playOnce(this, getInterruptSoundFile());
                    }
                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
                break;


        }
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiElectrolyzer(new ContainerElectrolyzer(entityPlayer, this));

    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 25 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip, advanced);

    }

    public void updateEntityServer() {
        super.updateEntityServer();


        if (this.cathodeslot.isEmpty() || this.anodeslot.isEmpty()) {
            if (this.getActive()) {
                this.setActive(false);
                initiate(2);
            }
            return;
        }


        boolean drain = false;
        boolean drain1 = false;
        if (this.getFluidTank(0).getFluidAmount() >= 3 && this.energy.getEnergy() >= 25) {
            int size = this.getFluidTank(0).getFluidAmount() / 3;
            size = Math.min(this.level + 1, size);
            int cap = this.fluidTank[1].getCapacity() - this.fluidTank[1].getFluidAmount();
            cap /= 2;
            cap = Math.min(cap, size);
            int cap1 = this.fluidTank[2].getCapacity() - this.fluidTank[2].getFluidAmount();
            cap1 = Math.min(cap1, size);
            if (this.fluidTank[1].getCapacity() - this.fluidTank[1].getFluidAmount() >= 3) {
                fill(new FluidStack(FluidName.fluidhyd.getInstance(), cap * 2), true);
                drain = true;

            }
            if (this.fluidTank[2].getCapacity() - this.fluidTank[2].getFluidAmount() >= 2) {
                fill(new FluidStack(FluidName.fluidoxy.getInstance(), cap1), true);
                drain1 = true;
            }
            if (drain || drain1) {
                int drains = 0;
                drains = drain ? drains + 2 * cap : drains;
                drains = drain1 ? drains + cap1 : drains;
                this.getFluidTank(0).drain(drains, true);
                if (!this.getActive()) {
                    this.setActive(true);
                    initiate(0);
                }
                this.useEnergy(25);


                setActive(true);
                ItemStack cathode = this.cathodeslot.get();
                ItemStack anode = this.anodeslot.get();
                if (cathode.getItemDamage() < cathode.getMaxDamage()) {
                    cathode.setItemDamage(cathode.getItemDamage() + 1);
                }
                if (anode.getItemDamage() < anode.getMaxDamage()) {
                    anode.setItemDamage(anode.getItemDamage() + 1);
                }
                if (cathode.getItemDamage() == cathode.getMaxDamage()) {
                    this.cathodeslot.consume(1);
                }
                if (anode.getItemDamage() == anode.getMaxDamage()) {
                    this.anodeslot.consume(1);
                }
            } else {
                setActive(false);
            }


        } else {
            setActive(false);
        }


    }


    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(final int level) {
        this.level = level;
    }

    @Override
    public void removeLevel(final int level) {
        this.level -= level;
    }


}
