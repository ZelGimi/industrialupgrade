package com.denfop.tiles.mechanism.steamturbine.exchanger;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.container.ContainerBaseSteamTurbineExchanger;
import com.denfop.gui.GuiBaseSteamTurbineExchanger;
import com.denfop.invslot.InvSlot;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.steamturbine.IController;
import com.denfop.tiles.mechanism.steamturbine.IExchanger;
import com.denfop.tiles.reactors.graphite.IExchangerItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBaseSteamTurbineExchanger extends TileEntityMultiBlockElement implements IExchanger {

    private final int level;
    private final InvSlot slot;
    public double percent = 1;
    private IExchangerItem item;

    public TileEntityBaseSteamTurbineExchanger(int level) {
        this.level = level;
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.EXCHANGE;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof IExchangerItem && ((IExchangerItem) stack.getItem()).getLevel() <= ((IController) getMain()).getLevel();
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (!world.isRemote) {
                    if (content.isEmpty()) {
                        ((TileEntityBaseSteamTurbineExchanger) this.base).percent = 0;
                    } else {
                        ((TileEntityBaseSteamTurbineExchanger) this.base).percent = ((IExchangerItem) content.getItem()).getPercent();
                        item = (IExchangerItem) content.getItem();
                    }
                }
            }
        };
        slot.setStackSizeLimit(1);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            if (this.getSlot().get().isEmpty()) {
                this.percent = 0;
                this.item = null;
            } else {
                this.percent = ((IExchangerItem) this.getSlot().get().getItem()).getPercent();
                this.item = ((IExchangerItem) this.getSlot().get().getItem());
            }
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeDouble(this.percent);
        return customPacketBuffer;
    }

    @Override
    public ContainerBaseSteamTurbineExchanger getGuiContainer(final EntityPlayer var1) {
        return new ContainerBaseSteamTurbineExchanger(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiBaseSteamTurbineExchanger(getGuiContainer(var1));
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.percent = customPacketBuffer.readDouble();
    }

    @Override
    public boolean hasOwnInventory() {
        return this.getMain() != null;
    }

    @Override
    public int getLevel() {
        return -1;
    }

    @Override
    public InvSlot getSlot() {
        return slot;
    }

    @Override
    public double getPower() {
        if (this.getMain() == null || this.getSlot().isEmpty()) {
            return 1;
        }

        return this.percent * 2;
    }

    @Override
    public IExchangerItem getExchanger() {
        return item;
    }


}
