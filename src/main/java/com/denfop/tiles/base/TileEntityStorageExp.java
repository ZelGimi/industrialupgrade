package com.denfop.tiles.base;


import com.denfop.IUCore;
import com.denfop.audio.AudioSource;
import com.denfop.container.ContainerStorageExp;
import com.denfop.gui.GUIStorageExp;
import com.denfop.invslot.InvSlotExpStorage;
import com.denfop.utils.ExperienceUtils;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.network.INetworkUpdateListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityStorageExp extends TileEntityInventory implements IHasGui, INetworkUpdateListener, INetworkDataProvider,
        INetworkClientTileEntityEventListener, INetworkTileEntityEventListener {

    public final int maxStorage;
    public final InvSlotExpStorage inputSlot;
    public int storage;
    public int storage1;
    public int expirencelevel;
    public int expirencelevel1;
    public AudioSource audioSource;

    public TileEntityStorageExp() {
        this.maxStorage = 2000000000;
        this.storage = 0;
        this.storage1 = 0;
        this.inputSlot = new InvSlotExpStorage(this);

    }

    public String getStartSoundFile() {
        return "Machines/zab.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/zap.ogg";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.storage = nbttagcompound.getInteger("storage");
        this.storage1 = nbttagcompound.getInteger("storage1");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("storage", this.storage);
        nbttagcompound.setInteger("storage1", this.storage1);
        return nbttagcompound;
    }

    public void updateEntityServer() {

        super.updateEntityServer();
        storage = Math.min(storage, 2000000000);
        storage1 = Math.min(storage1, 2000000000);
        if (this.inputSlot.isEmpty()) {
            storage1 = 0;
        }
        this.expirencelevel = ExperienceUtils.getLevelForExperience(storage);
        this.expirencelevel1 = ExperienceUtils.getLevelForExperience(storage1);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIStorageExp(new ContainerStorageExp(entityPlayer, this));
    }

    public ContainerBase<? extends TileEntityStorageExp> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerStorageExp(entityPlayer, this);
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {

    }

    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }

    }

    protected void initiate(int soundEvent) {
        IC2.network.get(true).initiateTileEntityEvent(this, soundEvent, true);
    }

    @Override
    public void onNetworkEvent(EntityPlayer player, int event) {
        // 0 убрать с меха опыт
        // 1 добавить в мех опыт
        if (event == 1) {
            if (storage < this.maxStorage && (storage1 == 0)) {
                storage = ExperienceUtils.removePlayerXP(player, maxStorage, storage);
                initiate(1);
            } else if (storage1 < this.maxStorage && !this.inputSlot.isEmpty()) {
                storage1 = ExperienceUtils.removePlayerXP(player, maxStorage, storage1);
                initiate(1);
            }

        }
        if (event == 0) {
            if (storage1 > 0 && !this.inputSlot.isEmpty()) {
                storage1 = ExperienceUtils.addPlayerXP1(player, storage1);
                initiate(0);
            } else if (storage > 0) {
                storage = ExperienceUtils.addPlayerXP1(player, storage);
                initiate(0);
            }
        }


    }

    @Override
    public void onNetworkEvent(int event) {
        if (this.audioSource == null && getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, getStartSoundFile());
        }
        switch (event) {
            case 0:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    IUCore.audioManager.playOnce(this, getStartSoundFile());
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    IUCore.audioManager.playOnce(this, getInterruptSoundFile());

                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
                break;
        }
    }

}
