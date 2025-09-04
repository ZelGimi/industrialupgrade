package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.pollution.radiation.Radiation;
import com.denfop.api.pollution.radiation.RadiationSystem;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.util.List;

public class PacketRadiation implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketRadiation() {
    }

    public PacketRadiation(List<Radiation> radiation, Player player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
        buffer.writeByte(this.getId());
        buffer.writeInt(radiation.size());
        radiation.forEach(radiation1 -> {
            try {
                EncoderHandler.encode(buffer, radiation1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        buffer.flip();
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this, buffer, (ServerPlayer) player);
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
        return 4;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final Player entityPlayer) {


        RadiationSystem.rad_system.getRadiationList().clear();
        RadiationSystem.rad_system.getMap().clear();
        final int size = is.readInt();
        for (int i = 0; i < size; i++) {
            Radiation radiation;
            try {
                radiation = (Radiation) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            RadiationSystem.rad_system.addRadiationWihoutUpdate(radiation);
        }


    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
