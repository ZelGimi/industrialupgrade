package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.pollution.radiation.Radiation;
import com.denfop.api.pollution.radiation.RadiationSystem;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;

import java.io.IOException;

public class PacketUpdateRadiationValue implements IPacket {

    private CustomPacketBuffer buffer;

    ;

    public PacketUpdateRadiationValue() {
    }

    public PacketUpdateRadiationValue(ChunkPos pos, double radiation, ServerLevel level) {
        for (ServerPlayer player : level.players()) {
            Radiation radiation1 = RadiationSystem.rad_system.getMap().get(pos);
            if (radiation1 == null) {
                radiation1 = new Radiation(pos);
                ;
                RadiationSystem.rad_system.addRadiation(radiation1);
            }

            radiation1.addRadiation(radiation);
            CustomPacketBuffer buffer = new CustomPacketBuffer(64, null);
            try {
                buffer.writeByte(this.getId());
                EncoderHandler.encode(buffer, radiation1);


            } catch (IOException var5) {
                throw new RuntimeException(var5);
            }

            buffer.flip();
            this.buffer = buffer;
            IUCore.network.getServer().sendPacket(this, player, buffer);
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
        return 100;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            Radiation radiation = (Radiation) DecoderHandler.decode(customPacketBuffer);
            Radiation radiation1 = RadiationSystem.rad_system.getMap().get(radiation.getPos());
            if (radiation1 == null) {
                radiation1 = new Radiation(radiation.getPos());
                radiation1.setRadiation(radiation.getRadiation());
                radiation1.setCoef(radiation.getCoef());
                radiation1.setLevel(radiation.getLevel());
                RadiationSystem.rad_system.addRadiation(radiation1);
            } else {
                radiation1.setRadiation(radiation.getRadiation());
                radiation1.setCoef(radiation.getCoef());
                radiation1.setLevel(radiation.getLevel());
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
