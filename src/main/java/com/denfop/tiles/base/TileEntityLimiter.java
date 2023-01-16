package com.denfop.tiles.base;

import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerBlockLimiter;
import com.denfop.gui.GuiBlockLimiter;
import com.denfop.invslot.InvSlotLimiter;
import ic2.api.energy.EnergyNet;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.IHasGui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;

public class TileEntityLimiter extends TileEntityInventory implements IHasGui, INetworkClientTileEntityEventListener {

    private final AdvEnergy energy;
    public final InvSlotLimiter slot;
    private double max_value;

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

    @Override
    public List<String> getNetworkedFields() {
        final List<String> list = super.getNetworkedFields();
        list.add("energy");
        list.add("max_value");
        list.add("slot");
        return list;
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        if(this.slot.isEmpty())
        setTier(0);
        else
            setTier(this.slot.get().getItemDamage() - 205);
        this.energy.setDirections( EnumSet.complementOf(EnumSet.of(this.getFacing())), EnumSet.of(this.getFacing()));

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

        this.energy.limit_amount = Math.min(i, this.max_value);
         this.energy.setDirections( EnumSet.complementOf(EnumSet.of(this.getFacing())), EnumSet.of(this.getFacing()));

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
