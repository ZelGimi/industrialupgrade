package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerElectrolyzer;
import com.denfop.gui.GUIElectrolyzer;
import com.denfop.invslot.InvSlotElectrolyzer;
import ic2.core.ContainerBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityElectrolyzer extends TileEntityBaseLiquedMachine {

    public final InvSlotElectrolyzer cathodeslot;
    public final InvSlotElectrolyzer anodeslot;

    public TileEntityElectrolyzer() {
        super("", 24000, 14, 2, 3, new boolean[]{false, true, true}, new boolean[]{true, false, false},
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
        return new GUIElectrolyzer(new ContainerElectrolyzer(entityPlayer, this));

    }

    public void updateEntityServer() {
        super.updateEntityServer();


        if (this.cathodeslot.isEmpty() || this.anodeslot.isEmpty()) {
            setActive(false);
            return;
        }

        boolean drain = false;
        if (getWorld().provider.getWorldTime() % 200 == 0) {
            initiate(2);
        }
        if (this.getFluidTank(0).getFluidAmount() >= 4 && this.energy.getEnergy() >= 25) {

            if (this.fluidTank[1].getFluidAmount() + 2 <= this.fluidTank[1].getCapacity() && this.fluidTank[2].getFluidAmount() + 1 <= this.fluidTank[2].getCapacity()) {
                fill(new FluidStack(FluidName.fluidhyd.getInstance(), 2), true);
                fill(new FluidStack(FluidName.fluidoxy.getInstance(), 1), true);
                this.getFluidTank(0).drain(4, true);
                initiate(0);
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


}
