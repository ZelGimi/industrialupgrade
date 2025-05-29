package com.denfop.network.packet;

import com.denfop.IUCore;
import net.minecraft.world.entity.player.Player;

public class PacketKeys implements IPacket {

    public PacketKeys() {

    }

    public PacketKeys(int keyState) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(5);
        buffer.writeByte(this.getId());
        buffer.writeInt(keyState);
        buffer.flip();
        IUCore.network.getClient().sendPacket(buffer);
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
