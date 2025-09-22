package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.ItemStackInventory;
import com.denfop.proxy.ClientProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class PacketUpdateFieldContainerItemStack implements IPacket {

    public PacketUpdateFieldContainerItemStack() {

    }

    public PacketUpdateFieldContainerItemStack(ItemStackInventory te, EntityPlayerMP player) {
        IUCore.network.getServer().addTileContainerToUpdate(te, player, te.writeContainer());
    }

    public PacketUpdateFieldContainerItemStack(CustomPacketBuffer customPacketBuffer, EntityPlayerMP entityPlayer) {

        IUCore.network.getServer().sendPacket(customPacketBuffer, entityPlayer);
    }


    @Override
    public byte getId() {
        return (byte) 133;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final EntityPlayer entityPlayer) {
        final int dimensionId = is.readInt();
        String name = is.readString();
        if (!name.equals(entityPlayer.getName())) {
            return;
        }
        final ItemStack stack = entityPlayer.getHeldItemMainhand();
        if (stack.getItem() instanceof IItemStackInventory && ((ClientProxy) IUCore.proxy).invent != null && ((ClientProxy) IUCore.proxy).invent
                .getContainerStack()
                .isItemEqual(stack)) {
            ((ClientProxy) IUCore.proxy).invent.readContainer(is);

        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
