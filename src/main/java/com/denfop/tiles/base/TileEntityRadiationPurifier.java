package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.audio.AudioSource;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityRadiationPurifier extends TileEntityElectricMachine
        implements INetworkTileEntityEventListener {


    private final int type;
    public AudioSource audioSource;


    public TileEntityRadiationPurifier() {
        super(50000, 14, 1);
        this.type = 1;
    }

    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }

    public void updateEntityServer() {

        super.updateEntityServer();

        if (this.energy.canUseEnergy(10 * type)) {
            this.energy.useEnergy(10 * type);
            setActive(true);
            initiate(0);
        } else {
            initiate(2);
            setActive(false);
        }
        if (getWorld().provider.getWorldTime() % 300 == 0) {
            initiate(2);
        }
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    public void initiate(int soundEvent) {
        IC2.network.get(true).initiateTileEntityEvent(this, soundEvent, true);
    }

    public String getStartSoundFile() {
        return "Machines/radiation.ogg";
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

    @Override
    protected boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        return false;
    }

    @Override
    public String getInventoryName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ContainerBase<?> getGuiContainer(final EntityPlayer entityPlayer) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return null;
    }

}
