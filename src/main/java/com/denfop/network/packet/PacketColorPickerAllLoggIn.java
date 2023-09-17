package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.render.streak.EventSpectralSuitEffect;
import com.denfop.render.streak.PlayerStreakInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;
import java.util.Map;

public class PacketColorPickerAllLoggIn implements IPacket {

    public PacketColorPickerAllLoggIn(Object object) {

    }

    public PacketColorPickerAllLoggIn() {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeInt(IUCore.mapStreakInfo.size());
        for (Map.Entry<String, PlayerStreakInfo> playerStreakInfoEntry : IUCore.mapStreakInfo.entrySet()) {
            buffer.writeString( playerStreakInfoEntry.getKey());
            buffer.writeBytes(playerStreakInfoEntry.getValue().writePacket());
        }
        IUCore.network.getServer().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return 3;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final EntityPlayer entityPlayer) {
        int size = is.readInt();
        for (int i = 0; i < size; i++) {

            final String nick = is.readString();
            final PlayerStreakInfo playerStreakInfo = new PlayerStreakInfo(is);
            EventSpectralSuitEffect.mapStreakInfo.remove(nick);
            EventSpectralSuitEffect.mapStreakInfo.put(nick, playerStreakInfo);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
