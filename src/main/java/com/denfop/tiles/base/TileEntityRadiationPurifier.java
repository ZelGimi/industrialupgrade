package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.audio.AudioSource;
import com.denfop.container.ContainerBase;
import ic2.core.IC2;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityRadiationPurifier extends TileEntityElectricMachine {


    private final int type;
    public AudioSource audioSource;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();

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


    public void updateEntityServer() {

        super.updateEntityServer();

        if (this.energy.canUseEnergy(10 * type)) {
            this.energy.useEnergy(10 * type);
            if (!this.getActive()) {
                setActive(true);
            }
            initiate(0);
        } else {
            if (this.getActive()) {
                setActive(false);
            }
            initiate(2);
        }
        if (getWorld().provider.getWorldTime() % 300 == 0) {
            initiate(2);
        }
    }

    public float getWrenchDropRate() {
        return 0.85F;
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
    public boolean onActivated(
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
    public ContainerBase<?> getGuiContainer(final EntityPlayer entityPlayer) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return null;
    }

}
