package com.denfop.tiles.mechanism;

import com.denfop.container.ContainerTunerWireless;
import com.denfop.gui.GUITunerWireless;
import com.denfop.invslot.InvSlotTuner;
import com.denfop.tiles.base.TileEntityElectricMachine;
import com.denfop.utils.ModUtils;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityTunerWireless extends TileEntityElectricMachine
        implements IHasGui, INetworkTileEntityEventListener, INetworkClientTileEntityEventListener {


    public final InvSlotTuner inputslot;
    public AudioSource audioSource;

    public TileEntityTunerWireless() {
        super("", 0, 10, 1);


        this.inputslot = new InvSlotTuner(this, "input");
    }


    public void updateEntityServer() {

        super.updateEntityServer();
        if (getWorld().provider.getWorldTime() % 40 == 0) {
            if (getActive()) {
                setActive(false);
            }
        }
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUITunerWireless(new ContainerTunerWireless(entityPlayer, this));
    }

    public ContainerBase<? extends TileEntityTunerWireless> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerTunerWireless(entityPlayer, this);
    }


    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    @Override
    public void onNetworkEvent(int event) {
        if (this.audioSource == null && getStartSoundFile() != null) {
            this.audioSource = IC2.audioManager.createSource(this, getStartSoundFile());
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
                        IC2.audioManager.playOnce(this, getInterruptSoundFile());
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
    public String getInventoryName() {
        return Localization.translate("iu.blockTuner.name");
    }


    @Override
    public void onNetworkEvent(EntityPlayer player, int event) {
        if (!this.inputslot.isEmpty()) {
            NBTTagCompound nbt = ModUtils.nbt(this.inputslot.get());
            boolean change = nbt.getBoolean("change");
            change = !change;
            nbt.setBoolean("change", change);
            setActive(true);
        }


    }

}
