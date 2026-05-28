package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.client.space.SpaceTeleportClientState;
import com.denfop.items.space.teleport.SpaceTeleportFxType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketSpaceTeleportFx implements IPacket {

    public PacketSpaceTeleportFx() {
    }

    public PacketSpaceTeleportFx(
            final ServerPlayer player,
            final SpaceTeleportFxType type,
            final int durationTicks,
            final String bodyName,
            final boolean reverse
    ) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(getId());
        buffer.writeInt(type.ordinal());
        buffer.writeInt(durationTicks);
        buffer.writeString(bodyName == null ? "" : bodyName);
        buffer.writeBoolean(reverse);
        IUCore.network.getServer().sendPacket(buffer, player);
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