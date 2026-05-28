package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.items.space.teleport.SpaceTeleportController;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PacketPlanetaryReturnRequest implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketPlanetaryReturnRequest() {
    }

    public PacketPlanetaryReturnRequest(final boolean ignored, Level level) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(level.registryAccess());
        buffer.writeByte(getId());
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
        return 92;
    }

    @Override
    public void readPacket(final CustomPacketBuffer buffer, final Player entityPlayer) {
        if (entityPlayer instanceof ServerPlayer serverPlayer) {
            SpaceTeleportController.requestReturn(serverPlayer);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }
}