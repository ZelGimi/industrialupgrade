package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.mixin.access.ExplosionAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;


public class PacketExplosion implements IPacket {

    public PacketExplosion() {
    }

    public PacketExplosion(Explosion explosion, int power, boolean flaming, boolean damage) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());


        BlockPos pos = new BlockPos(explosion.getPosition());
        buffer.writeBlockPos(pos);
        buffer.writeInt(power);
        buffer.writeBoolean(flaming);
        buffer.writeBoolean(damage);

        for (Player player : ((ExplosionAccessor) explosion).getLevel().players()) {
            if (!(player instanceof ServerPlayer)) continue;


            double dx = explosion.getPosition().x - player.getX();
            double dy = explosion.getPosition().y - player.getY();
            double dz = explosion.getPosition().z - player.getZ();
            double distanceSq = dx * dx + dy * dy + dz * dz;
            if (distanceSq > 1024) continue;
            IUCore.network.getServer().sendPacket(buffer, (ServerPlayer) player);
        }
    }

    @Override
    public byte getId() {
        return 15;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        BlockPos pos = customPacketBuffer.readBlockPos();
        Explosion explosion = new Explosion(entityPlayer.level, entityPlayer, pos.getX(), pos.getY(), pos.getZ(), customPacketBuffer.readInt(),
                customPacketBuffer.readBoolean(), customPacketBuffer.readBoolean() ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.NONE
        );
        explosion.explode();
        explosion.finalizeExplosion(true);
        explosion.clearToBlow();

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
