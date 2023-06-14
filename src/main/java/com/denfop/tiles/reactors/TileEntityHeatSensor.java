package com.denfop.tiles.reactors;

import com.denfop.IUCore;
import com.denfop.api.inv.IHasGui;
import com.denfop.audio.AudioSource;
import com.denfop.container.ContainerHeatLimiter;
import com.denfop.gui.GuiHeatLimiter;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityHeatSensor extends TileEntityInventory implements IHasGui, INetworkClientTileEntityEventListener,
        INetworkTileEntityEventListener {

    public int limit;
    public boolean update = false;
    public TileEntityBaseNuclearReactorElectric reactor;
    private AudioSource audioSource;

    public void checkEntity() {
        BlockPos pos1 = pos.add(this.getFacing().getOpposite().getDirectionVec());


        TileEntity tile = this.getWorld().getTileEntity(pos1);
        if (tile instanceof IChamber) {
            reactor = ((IChamber) tile).getReactor();
        } else if (tile instanceof TileEntityBaseNuclearReactorElectric) {
            reactor = (TileEntityBaseNuclearReactorElectric) tile;
        }

    }


    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }

    }

    @Override
    protected void onBlockBreak() {
        if (reactor != null) {
            reactor.isLimit = false;
            reactor.limit = 10000;
        }

        super.onBlockBreak();
    }

    public String getStartSoundFile() {
        return "Machines/pen.ogg";
    }



    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        if (i == -1) {
            checkEntity();
        } else if (i <= 10000) {
            if (reactor != null) {
                reactor.limit = i;
                reactor.isLimit = true;
                limit = i;
                IUCore.network.get(true).updateTileEntityField(this, "limit");
            }
        } else {
            if (reactor != null) {
                reactor.isLimit = false;
            }
        }

        initiate(0);
    }

    public void onNetworkEvent(int event) {
        if (this.audioSource == null && this.getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, this.getStartSoundFile());
        }

        if (event == 0) {
            if (this.audioSource != null) {
                this.audioSource.stop();
                this.audioSource.play();
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("limit", this.limit);
        return nbt;
    }

    public void initiate(int soundEvent) {

        IUCore.network.get(true).initiateTileEntityEvent(this, soundEvent, true);

    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.limit = nbtTagCompound.getInteger("limit");
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        checkEntity();
        if (reactor != null) {
            reactor.limit = this.limit;
            reactor.isLimit = true;
        }
        IUCore.network.get(true).updateTileEntityField(this, "limit");
    }

    @Override
    public ContainerHeatLimiter getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerHeatLimiter(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiHeatLimiter getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiHeatLimiter(getGuiContainer(entityPlayer));
    }

    @Override
    public void onGuiClosed(final EntityPlayer entityPlayer) {

    }

}
