package com.denfop.tiles.reactors.graphite.exchanger;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.container.ContainerExchanger;
import com.denfop.gui.GuiExchanger;
import com.denfop.invslot.InvSlot;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.graphite.IExchanger;
import com.denfop.tiles.reactors.graphite.IExchangerItem;
import com.denfop.tiles.reactors.graphite.controller.TileEntityMainController;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityExchanger extends TileEntityMultiBlockElement implements IExchanger, IUpdatableTileEvent {

    public final int level;
    private final InvSlot slot;
    public double percent = 1;
    private int x = 0;
    private IExchangerItem item;

    public TileEntityExchanger(int level) {
        this.level = level;
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.EXCHANGE;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof IExchangerItem && ((IExchangerItem) stack.getItem()).getLevel() <= ((TileEntityExchanger) this.base).getLevel();
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (!world.isRemote)
                    if (content.isEmpty()) {
                    ((TileEntityExchanger) this.base).percent = 1;
                } else {
                    ((TileEntityExchanger) this.base).percent = 1 - ((IExchangerItem) content.getItem()).getPercent();
                    item = (IExchangerItem) content.getItem();
                }
            }
        };
        slot.setStackSizeLimit(1);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getMain() != null) {
            TileEntityMainController controller = (TileEntityMainController) this.getMain();
            if (controller.work && !this.slot.isEmpty() && world.provider.getWorldTime() % 20 == 0) {
                if (item == null) {
                    this.item = (IExchangerItem) this.slot.get().getItem();
                }
                this.item.damageItem(this.slot.get(), -1);
            }
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            if (this.getSlot().get().isEmpty()) {
                this.percent = 1;
            } else {
                this.percent = 1 - ((IExchangerItem) this.getSlot().get().getItem()).getPercent();
            }
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeInt(this.x);
        customPacketBuffer.writeDouble(this.percent);
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.x = customPacketBuffer.readInt();
        this.percent = customPacketBuffer.readDouble();
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        x = nbtTagCompound.getInteger("exchanger_x");
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setInteger("exchanger_x", x);
        return nbtTagCompound;
    }

    @Override
    public int getLevel() {
        return -1;
    }


    public InvSlot getSlot() {
        return slot;
    }

    @Override
    public double getPercent(final int x) {
        if (this.getMain() == null || x != this.x || this.getSlot().isEmpty()) {
            return 1;
        }
        return this.percent;

    }

    @Override
    public ContainerExchanger getGuiContainer(final EntityPlayer var1) {
        return new ContainerExchanger(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiExchanger(getGuiContainer(var1));
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (this.getMain() == null) {
            return;
        }
        IGraphiteReactor reactor = (IGraphiteReactor) this.getMain();
        if (var2 == 0) {
            this.x = Math.min(this.x + 1, reactor.getWidth() - 1);
            reactor.updateDataReactor();
        } else {
            this.x = Math.max(0, this.x - 1);
            reactor.updateDataReactor();
        }
    }

    public int getX() {
        return x;
    }

}
