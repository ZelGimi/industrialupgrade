package com.denfop.network.packet;

import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.network.DecoderHandler;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;

public class PacketRadiationChunk implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketRadiationChunk() {

    }

    public PacketRadiationChunk(Radiation radiation) {

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
        return 7;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final Player entityPlayer) {
        Radiation radiation;
        try {
            radiation = (Radiation) DecoderHandler.decode(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assert radiation != null;
        RadiationSystem.rad_system.addRadiation(radiation);
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
