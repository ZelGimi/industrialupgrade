package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.space.fakebody.*;
import com.denfop.api.space.research.api.IResearchTable;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.mechanism.BlockEntityResearchTableSpace;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.io.IOException;

public class PacketUpdateFakeBody implements IPacket {

    private CustomPacketBuffer buffer;

    ;

    public PacketUpdateFakeBody() {

    }

    public PacketUpdateFakeBody(IResearchTable tileEntityResearchTableSpace, IFakeBody fakeBody) {
        ServerLevel serverLevel = (ServerLevel) ((BlockEntityBase) tileEntityResearchTableSpace).getWorld();

        for (ServerPlayer player : serverLevel.players()) {
            CustomPacketBuffer packetBuffer = new CustomPacketBuffer(((BlockEntityBase) tileEntityResearchTableSpace).getWorld().registryAccess());
            packetBuffer.writeByte(getId());
            try {
                EncoderHandler.encode(packetBuffer, ((BlockEntityBase) tileEntityResearchTableSpace).getWorld());
                EncoderHandler.encode(packetBuffer, ((BlockEntityBase) tileEntityResearchTableSpace).getPos());
                if (fakeBody instanceof IFakePlanet) {
                    packetBuffer.writeByte(0);
                }
                if (fakeBody instanceof IFakeSatellite) {
                    packetBuffer.writeByte(1);
                }
                if (fakeBody instanceof IFakeAsteroid) {
                    packetBuffer.writeByte(2);
                }
                if (fakeBody == null) {
                    packetBuffer.writeByte(3);
                }
                if (fakeBody != null) {
                    EncoderHandler.encode(packetBuffer, fakeBody.writeNBTTagCompound(new CompoundTag(), packetBuffer.registryAccess()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.buffer = packetBuffer;

            IUCore.network.getServer().sendPacket(this, player, packetBuffer);
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
        return 41;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            Level world = (Level) DecoderHandler.decode(customPacketBuffer);
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            byte id = customPacketBuffer.readByte();
            assert world != null;
            if (entityPlayer.level().dimension() == world.dimension()) {
                BlockEntity tile = world.getBlockEntity(pos);
                if (tile instanceof BlockEntityResearchTableSpace) {

                    if (id == 0) {
                        CompoundTag nbt = (CompoundTag) DecoderHandler.decode(customPacketBuffer);
                        FakePlanet fakePlanet = new FakePlanet(nbt, customPacketBuffer.registryAccess());
                        ((BlockEntityResearchTableSpace) tile).fakeBody = fakePlanet;
                    }
                    if (id == 1) {
                        CompoundTag nbt = (CompoundTag) DecoderHandler.decode(customPacketBuffer);
                        FakeSatellite fakePlanet = new FakeSatellite(nbt, customPacketBuffer.registryAccess());
                        ((BlockEntityResearchTableSpace) tile).fakeBody = fakePlanet;
                    }
                    if (id == 2) {
                        CompoundTag nbt = (CompoundTag) DecoderHandler.decode(customPacketBuffer);
                        FakeAsteroid fakePlanet = new FakeAsteroid(nbt, customPacketBuffer.registryAccess());
                        ((BlockEntityResearchTableSpace) tile).fakeBody = fakePlanet;
                    }
                    if (id == 3) {
                        ((BlockEntityResearchTableSpace) tile).fakeBody = null;
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
