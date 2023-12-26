package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;

public class PacketUpdateRadiation implements IPacket{
    public PacketUpdateRadiation(){

    }

    public PacketUpdateRadiation(Radiation radiation){
        CustomPacketBuffer buffer = new CustomPacketBuffer(64);
        try {
            buffer.writeByte(this.getId());
            EncoderHandler.encode(buffer, radiation);


        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }

        buffer.flip();
        IUCore.network.getServer().sendPacket(buffer);
    }
    @Override
    public byte getId() {
        return 101;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        try {
            Radiation radiation = (Radiation) DecoderHandler.decode(customPacketBuffer);
            Radiation radiation1 = RadiationSystem.rad_system.getMap().get(radiation.getPos());
            if(radiation1 == null){
                radiation1 = new Radiation(radiation.getPos());
                radiation1.setRadiation(radiation.getRadiation());
                radiation1.setCoef(radiation.getCoef());
                radiation1.setLevel(radiation.getLevel());
                RadiationSystem.rad_system.addRadiation(radiation1);
            }else{
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
