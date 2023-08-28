package com.denfop.network.packet;

import com.denfop.IUCore;
import net.minecraft.entity.player.EntityPlayer;

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
    public void readPacket(final CustomPacketBuffer is, final EntityPlayer player) {
        final int keyState = is.readInt();
        IUCore.proxy.requestTick(true, () -> IUCore.keyboard.processKeyUpdate(player, keyState));

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
