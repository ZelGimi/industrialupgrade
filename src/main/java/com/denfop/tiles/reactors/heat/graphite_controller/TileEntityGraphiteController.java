package com.denfop.tiles.reactors.heat.graphite_controller;

import com.denfop.api.reactors.IHeatReactor;
import com.denfop.container.ContainerHeatGraphiteController;
import com.denfop.gui.GuiHeatGraphiteGraphiteController;
import com.denfop.invslot.InvSlot;
import com.denfop.items.resource.ItemCraftingElements;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.heat.IGraphiteController;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityGraphiteController extends TileEntityMultiBlockElement implements IGraphiteController,
        IUpdatableTileEvent {

    public final InvSlot slot;
    private final int level;
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


    public double getFuel() {
        return fuel;
    }

    public InvSlot getSlot() {
        return slot;
    }

    @Override
    public ContainerHeatGraphiteController getGuiContainer(final EntityPlayer var1) {
        return new ContainerHeatGraphiteController(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiHeatGraphiteGraphiteController(getGuiContainer(var1));
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
        index = nbtTagCompound.getInteger("index");
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setDouble("fuel", fuel);
        nbtTagCompound.setInteger("index", index);
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
        this.fuel -= col;
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
    public int getIndex() {
        return this.index;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (this.getMain() != null) {
            IHeatReactor heatReactor = (IHeatReactor) this.getMain();
            if (var2 == 0) {
                this.levelGraphite = Math.min(this.levelGraphite + 1, 5);
            } else if (var2 == 1) {
                this.levelGraphite = Math.max(1, levelGraphite - 1);
            } else if (var2 == 2) {
                this.index = Math.min(this.index + 1, heatReactor.getWidth() - 1);
            } else if (var2 == 3) {
                this.index = Math.max(this.index - 1, 0);
            }


            heatReactor.updateDataReactor();
        }
    }

}
