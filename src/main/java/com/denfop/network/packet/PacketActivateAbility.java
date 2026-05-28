package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.ability.AbilityManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketActivateAbility implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketActivateAbility() {
    }

    public PacketActivateAbility(final Player player) {
        if (player == null) {
            return;
        }

        final CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(player.registryAccess());
        customPacketBuffer.writeByte(getId());
        this.buffer = customPacketBuffer;
        IUCore.network.getClient().sendPacket(this, customPacketBuffer);
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
        return 62;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        if (entityPlayer instanceof ServerPlayer serverPlayer) {
            AbilityManager.handleActivationPacket(serverPlayer);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }
}
