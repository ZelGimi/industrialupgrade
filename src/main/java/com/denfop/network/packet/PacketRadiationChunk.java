package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;

public class PacketRadiationChunk implements IPacket {

    public PacketRadiationChunk() {

    }

    public PacketRadiationChunk(Radiation radiation) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(64);
        try {
            buffer.writeByte(this.getId());
            EncoderHandler.encode(buffer, radiation, false);


        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }

        buffer.flip();
        IUCore.network.getServer().sendPacket(buffer);
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
