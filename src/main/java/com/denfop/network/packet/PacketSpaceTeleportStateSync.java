package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.client.space.SpaceTeleportClientState;
import com.denfop.items.space.teleport.SpaceTeleportPhase;
import com.denfop.items.space.teleport.SpaceTeleportReason;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketSpaceTeleportStateSync implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketSpaceTeleportStateSync() {
    }

    public PacketSpaceTeleportStateSync(
            final ServerPlayer player,
            final boolean active,
            final SpaceTeleportPhase phase,
            final String bodyName,
            final long ticksUntilCritical,
            final long countdownTicks,
            final double charge,
            final double maxCharge,
            final SpaceTeleportReason reason,
            final boolean itemPresent
    ) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
        buffer.writeByte(getId());
        buffer.writeBoolean(active);
        buffer.writeInt(phase.ordinal());
        buffer.writeString(bodyName == null ? "" : bodyName);
        buffer.writeLong(ticksUntilCritical);
        buffer.writeLong(countdownTicks);
        buffer.writeDouble(charge);
        buffer.writeDouble(maxCharge);
        buffer.writeInt(reason.ordinal());
        buffer.writeBoolean(itemPresent);
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this, buffer, player);
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
        return 93;
    }

    @Override
    public void readPacket(final CustomPacketBuffer buffer, final Player entityPlayer) {
        SpaceTeleportClientState.INSTANCE.applySync(
                buffer.readBoolean(),
                SpaceTeleportPhase.byOrdinal(buffer.readInt()),
                buffer.readString(),
                buffer.readLong(),
                buffer.readLong(),
                buffer.readDouble(),
                buffer.readDouble(),
                SpaceTeleportReason.byOrdinal(buffer.readInt()),
                buffer.readBoolean()
        );
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}