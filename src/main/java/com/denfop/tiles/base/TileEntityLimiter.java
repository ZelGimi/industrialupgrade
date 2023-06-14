package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.api.inv.IHasGui;
import com.denfop.audio.AudioSource;
import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerBlockLimiter;
import com.denfop.gui.GuiBlockLimiter;
import com.denfop.invslot.InvSlotLimiter;
import ic2.api.energy.EnergyNet;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.IC2;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;

public class TileEntityLimiter extends TileEntityInventory implements IHasGui, INetworkClientTileEntityEventListener,
        INetworkTileEntityEventListener {

    private final AdvEnergy energy;
    public InvSlotLimiter slot;
    public double max_value;
    private AudioSource audioSource;

    public TileEntityLimiter() {
        this.energy = this.addComponent(new AdvEnergy(
                this,
                Double.MAX_VALUE,
                EnumSet.complementOf(EnumSet.of(this.getFacing())), EnumSet.of(this.getFacing()),
                14,
                0,
                false
        ));
        this.energy.setLimit(true);
        this.slot = new InvSlotLimiter(this);

    }

    public void initiate(int soundEvent) {

        IUCore.network.get(true).initiateTileEntityEvent(this, soundEvent, true);

    }

    public String getStartSoundFile() {
        return "Machines/pen.ogg";
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

    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }

    }

    @Override
    public List<String> getNetworkFields() {
        final List<String> list = super.getNetworkFields();
        list.add("energy");
        list.add("max_value");
        list.add("slot");
        return list;
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        if (this.slot.isEmpty()) {
            setTier(0);
        } else {
            setTier(this.slot.get().getItemDamage() - 205);
        }
        this.energy.setDirections(EnumSet.complementOf(EnumSet.of(this.getFacing())), EnumSet.of(this.getFacing()));
        this.energy.setCapacity(this.energy.limit_amount);

    }

    public void setTier(int tier) {
        this.energy.setSourceTier(tier);
        if (tier != 0) {
            this.energy.setCapacity(EnergyNet.instance.getPowerFromTier(tier));
        } else {
            this.energy.setCapacity(0);
            this.energy.limit_amount = 0;
        }
        if (tier != 0) {
            this.max_value = EnergyNet.instance.getPowerFromTier(tier);
        } else {
            this.max_value = 0;
        }

    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
      }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("max_value", max_value);
        return nbt;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.max_value = nbtTagCompound.getDouble("max_value");
    }

    public AdvEnergy getEnergy() {
        return energy;
    }

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {

        this.energy.limit_amount = (int) Math.min(i, this.max_value);
        this.energy.setDirections(EnumSet.complementOf(EnumSet.of(this.getFacing())), EnumSet.of(this.getFacing()));
        this.energy.setCapacity(this.energy.limit_amount);
        initiate(0);

    }

    @Override
    public ContainerBlockLimiter getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerBlockLimiter(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiBlockLimiter(getGuiContainer(entityPlayer));
    }

    @Override
    public void onGuiClosed(final EntityPlayer entityPlayer) {

    }

}
