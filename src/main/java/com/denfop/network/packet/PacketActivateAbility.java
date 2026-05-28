package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.ability.AbilityManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketActivateAbility implements IPacket {

    public PacketActivateAbility() {
    }

    public PacketActivateAbility(final Player player) {
        if (player == null) {
            return;
        }

        final CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());
        IUCore.network.getClient().sendPacket(customPacketBuffer);
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
