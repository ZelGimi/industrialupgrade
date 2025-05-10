package com.denfop.network.packet;

import com.denfop.IUCore;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketRadiationUpdateValue implements IPacket {

    public PacketRadiationUpdateValue() {

    }

    public PacketRadiationUpdateValue(Player player, double value) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(64);
        buffer.writeByte(this.getId());
        buffer.writeDouble(value);
        buffer.flip();
        IUCore.network.getServer().sendPacket(buffer, (ServerPlayer) player);
    }

    @Override
    public byte getId() {
        return 99;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        if (entityPlayer == null) {
            customPacketBuffer.readDouble();
            return;
        }
        entityPlayer.getPersistentData().putDouble("radiation", (float) customPacketBuffer.readDouble());
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
