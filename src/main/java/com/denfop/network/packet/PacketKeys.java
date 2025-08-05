package com.denfop.network.packet;

import com.denfop.IUCore;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.player.Player;

public class PacketKeys implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketKeys() {

    }

    public PacketKeys(int keyState, RegistryAccess registryAccess) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(5, registryAccess);
        buffer.writeByte(this.getId());
        buffer.writeInt(keyState);
        buffer.flip();
        this.buffer = buffer;
        IUCore.network.getClient().sendPacket(this, buffer);
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
        return 6;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final Player player) {
        final int keyState = is.readInt();
        IUCore.keyboard.processKeyUpdate(player, keyState);

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
