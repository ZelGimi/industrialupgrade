package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.container.ContainerModuleMachine;
import com.denfop.gui.GuiModuleMachine;
import com.denfop.invslot.InvSlotModule;
import com.denfop.tiles.base.TileEntityElectricMachine;
import com.denfop.utils.ModUtils;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.IC2;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntityModuleMachine extends TileEntityElectricMachine
        implements INetworkClientTileEntityEventListener {


    public final InvSlotModule inputslot;
    public final InvSlotModule inputslotA;
    public List<String> listItems = new ArrayList<>();

    public TileEntityModuleMachine() {
        super(0, 10, 0);


        this.inputslot = new InvSlotModule(this, "input", 0, 27);
        this.inputslotA = new InvSlotModule(this, "input2", 1, 1);
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        this.inputslot.update();
    }

    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiModuleMachine(new ContainerModuleMachine(entityPlayer, this));
    }

    public ContainerModuleMachine getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerModuleMachine(entityPlayer, this);
    }


    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    public String getStartSoundFile() {
        return "Machines/pen.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/pen.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    @Override
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
                        IUCore.audioManager.playOnce(this, getStartSoundFile());
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

    @Override
    public void onGuiClosed(EntityPlayer arg0) {
    }


    @Override
    public void onNetworkEvent(EntityPlayer player, int event) {
        if (!this.inputslotA.isEmpty()) {
            initiate(1);
            this.inputslotA.get(0).setTagCompound(new NBTTagCompound());
            for (int i = 0; i < this.inputslot.size(); i++) {
                String l = "number_" + i;
                if (i < this.listItems.size()) {
                    ModUtils.NBTSetString(inputslotA.get(), l, this.listItems.get(i));
                }

            }
            final NBTTagCompound nbt = this.inputslotA.get(0).getTagCompound();
            nbt.setInteger("size", this.listItems.size());

        }


    }

}
