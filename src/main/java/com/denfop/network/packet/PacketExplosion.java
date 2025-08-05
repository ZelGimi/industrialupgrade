package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.mixin.access.ExplosionAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;


public class PacketExplosion implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketExplosion() {
    }

    public PacketExplosion(Explosion explosion, int power, boolean flaming, boolean damage) {
        for (Player player : ((ExplosionAccessor) explosion).getLevel().players()) {
            CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
            buffer.writeByte(this.getId());

            Vec3 vec = explosion.center();
            BlockPos pos = new BlockPos((int) vec.x, (int) vec.y, (int) vec.z);
            buffer.writeBlockPos(pos);
            buffer.writeInt(power);
            buffer.writeBoolean(flaming);
            buffer.writeBoolean(damage);


            if (!(player instanceof ServerPlayer)) continue;


            double dx = explosion.center().x - player.getX();
            double dy = explosion.center().y - player.getY();
            double dz = explosion.center().z - player.getZ();
            double distanceSq = dx * dx + dy * dy + dz * dz;
            if (distanceSq > 1024) continue;
            this.buffer = buffer;
            IUCore.network.getServer().sendPacket(this, buffer, (ServerPlayer) player);
        }
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
        return 15;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        BlockPos pos = customPacketBuffer.readBlockPos();
        Explosion explosion = new Explosion(entityPlayer.level(), entityPlayer, pos.getX(), pos.getY(), pos.getZ(), customPacketBuffer.readInt(),
                customPacketBuffer.readBoolean(), customPacketBuffer.readBoolean() ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP
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
