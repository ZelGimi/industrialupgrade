package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.client.space.SpaceTeleportClientState;
import com.denfop.items.space.teleport.SpaceTeleportFxType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketSpaceTeleportFx implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketSpaceTeleportFx() {
    }

    public PacketSpaceTeleportFx(
            final ServerPlayer player,
            final SpaceTeleportFxType type,
            final int durationTicks,
            final String bodyName,
            final boolean reverse
    ) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
        buffer.writeByte(getId());
        buffer.writeInt(type.ordinal());
        buffer.writeInt(durationTicks);
        buffer.writeString(bodyName == null ? "" : bodyName);
        buffer.writeBoolean(reverse);
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
        return 94;
    }

    @Override
    public void readPacket(final CustomPacketBuffer buffer, final Player entityPlayer) {
        SpaceTeleportClientState.INSTANCE.startFx(
                SpaceTeleportFxType.byOrdinal(buffer.readInt()),
                buffer.readInt(),
                buffer.readString(),
                buffer.readBoolean()
        );
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}