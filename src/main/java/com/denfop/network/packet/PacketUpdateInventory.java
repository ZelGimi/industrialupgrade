package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.container.ContainerBase;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.util.List;

public class PacketUpdateInventory implements IPacket {

    private CustomPacketBuffer buffer;


    public PacketUpdateInventory() {

    }

    public PacketUpdateInventory(ContainerBase<IAdvInventory> tContainerBase, ServerPlayer player) {
        CustomPacketBuffer packetBuffer = new CustomPacketBuffer(player.registryAccess());
        packetBuffer.writeByte(this.getId());
        List<Slot> slots = tContainerBase.slots.stream().filter(slot -> slot.container == player.getInventory()).toList();
        packetBuffer.writeShort(slots.size());
        for (int i = 0; i < slots.size(); i++) {
            packetBuffer.writeBoolean(slots.get(i).hasItem());
            if (tContainerBase.slots.get(i).hasItem()) {
                try {
                    EncoderHandler.encode(packetBuffer, tContainerBase.slots.get(i).getItem());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        packetBuffer.flip();
        this.buffer = packetBuffer;
        IUCore.network.getServer().sendPacket(this, packetBuffer, player);
    }

    @Override
    public CustomPacketBuffer getPacketBuffer() {
        return buffer;
    }

    @Override
    public void setPacketBuffer(CustomPacketBuffer customPacketBuffer) {
        buffer = customPacketBuffer;
    }

    @Override
    public byte getId() {
        return 13;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final Player entityPlayer) {
        int size = is.readShort();
        AbstractContainerMenu menu = entityPlayer.containerMenu;
        if (menu != null)
            for (int i = 0; i < size; i++) {
                boolean hasItem = is.readBoolean();
                if (hasItem) {
                    try {
                        menu.getSlot(i).set((ItemStack) DecoderHandler.decode(is));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    menu.getSlot(i).set(ItemStack.EMPTY);
                }
                entityPlayer.containerMenu.broadcastChanges();
            }

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
