package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.IOException;
import java.util.List;

public class PacketRadiation implements IPacket {

    public PacketRadiation() {
    }

    public PacketRadiation(List<Radiation> radiation, EntityPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
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
        IUCore.network.getServer().sendPacket(buffer, (EntityPlayerMP) player);
    }

    @Override
    public byte getId() {
        return 4;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final EntityPlayer entityPlayer) {


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
