package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.ability.AbilityClientState;
import com.denfop.ability.EnumPlayerAbility;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.EnumMap;
import java.util.Map;

public class PacketSyncAbilityCooldowns implements IPacket {

    public CustomPacketBuffer buffer;

    public PacketSyncAbilityCooldowns() {
    }

    public PacketSyncAbilityCooldowns(final ServerPlayer player, final Map<EnumPlayerAbility, Integer> remainingTicks) {
        if (player == null) {
            return;
        }

        final CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(player.registryAccess());
        customPacketBuffer.writeByte(getId());
        customPacketBuffer.writeVarInt(remainingTicks.size());

        for (final Map.Entry<EnumPlayerAbility, Integer> entry : remainingTicks.entrySet()) {
            customPacketBuffer.writeEnum(entry.getKey());
            customPacketBuffer.writeVarInt(entry.getValue());
        }
        this.buffer = customPacketBuffer;
        IUCore.network.getServer().sendPacket(this, customPacketBuffer, player);
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
        return 63;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        final int size = customPacketBuffer.readVarInt();
        final EnumMap<EnumPlayerAbility, Integer> result = new EnumMap<>(EnumPlayerAbility.class);

        for (int i = 0; i < size; i++) {
            result.put(customPacketBuffer.readEnum(EnumPlayerAbility.class), customPacketBuffer.readVarInt());
        }

        AbilityClientState.apply(result);
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}
