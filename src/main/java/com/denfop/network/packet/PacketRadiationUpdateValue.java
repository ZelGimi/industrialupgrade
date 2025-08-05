package com.denfop.network.packet;

import com.denfop.IUCore;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketRadiationUpdateValue implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketRadiationUpdateValue() {

    }

    public PacketRadiationUpdateValue(Player player, double value) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(64, player.registryAccess());
        buffer.writeByte(this.getId());
        buffer.writeDouble(value);
        buffer.flip();
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this, buffer, (ServerPlayer) player);
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
