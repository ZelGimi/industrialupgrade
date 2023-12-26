package com.denfop.tiles.reactors.graphite.graphite_controller;

import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.container.ContainerExchanger;
import com.denfop.container.ContainerGraphiteController;
import com.denfop.container.ContainerGraphiteReactor;
import com.denfop.gui.GuiExchanger;
import com.denfop.gui.GuiGraphiteGraphiteController;
import com.denfop.invslot.InvSlot;
import com.denfop.items.resource.ItemCraftingElements;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.graphite.IGraphiteController;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityGraphiteController extends TileEntityMultiBlockElement implements IGraphiteController,
        IUpdatableTileEvent {

    private final int level;
    public final InvSlot slot;
    public double fuel;
    public int levelGraphite = 1;
    private int index;

    public TileEntityGraphiteController(int level) {
        this.level = level;
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                if (!(stack.getItem() instanceof ItemCraftingElements)) {
                    return false;
                }
                final int itemDamage = stack.getItemDamage();
                switch (itemDamage) {
                    case 357:
                        return ((TileEntityGraphiteController) this.base).getLevel() >= 0;
                    case 410:
                        return ((TileEntityGraphiteController) this.base).getLevel() >= 1;
                    case 310:
                        return ((TileEntityGraphiteController) this.base).getLevel() >= 2;
                    case 368:
                        return ((TileEntityGraphiteController) this.base).getLevel() >= 3;
                }
                return false;
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (content.isEmpty()) {
                    ((TileEntityGraphiteController) this.base).fuel = 0;
                }
            }
        };
    }

    public int getIndex() {
        return index;
    }

    public double getFuel() {
        return fuel;
    }

    public InvSlot getSlot() {
        return slot;
    }

    @Override
    public ContainerGraphiteController getGuiContainer(final EntityPlayer var1) {
        return new ContainerGraphiteController(this,var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiGraphiteGraphiteController(getGuiContainer(var1));
    }


    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeInt(this.index);
        customPacketBuffer.writeDouble(this.fuel);
        customPacketBuffer.writeInt(this.levelGraphite);
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.index = customPacketBuffer.readInt();
        this.fuel = customPacketBuffer.readDouble();
        this.levelGraphite = customPacketBuffer.readInt();
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        fuel = nbtTagCompound.getDouble("fuel");
        levelGraphite = nbtTagCompound.getInteger("levelGraphite");
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setDouble("fuel", fuel);
        nbtTagCompound.setInteger("levelGraphite", levelGraphite);
        return nbtTagCompound;
    }
    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public ItemStack getGraphite() {
        return this.slot.get();
    }

    @Override
    public int getLevelGraphite() {
        return this.levelGraphite;
    }

    @Override
    public double getFuelGraphite() {
        return this.fuel;
    }

    @Override
    public void consumeFuelGraphite(double col) {
        this.fuel -= col ;
    }

    @Override
    public void consumeGraphite() {
        if (!this.slot.get().isEmpty()) {
            final int itemDamage = this.slot.get().getItemDamage();
            this.slot.get().shrink(1);
            switch (itemDamage) {
                case 357:
                    this.fuel = 100;
                    break;
                case 410:
                    this.fuel = 500;
                    break;
                case 310:
                    this.fuel = 2500;
                    break;
                case 368:
                    this.fuel = 5000;
                    break;
            }
        } else {
            this.fuel = 0;
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.levelGraphite = Math.max(1, levelGraphite);
    }

    @Override
    public void setIndex(final int i) {
        this.index = i;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if(var2 == 0){
            this.levelGraphite = Math.min(this.levelGraphite + 1, 5);
        }else{
            this.levelGraphite = Math.max(1, levelGraphite-1);
        }
        if(this.getMain() != null){
            IGraphiteReactor graphiteReactor = (IGraphiteReactor) this.getMain();
            graphiteReactor.updateDataReactor();
        }
    }

}
