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
        NBTTagCompound tagCompound = new NBTTagCompound();
        buffer.writeInt(IUCore.mapStreakInfo.size());
        int i = 0;
        for (Map.Entry<String, PlayerStreakInfo> playerStreakInfoEntry : IUCore.mapStreakInfo.entrySet()) {
            NBTTagCompound tag4 = new NBTTagCompound();
            tag4.setString("nick", playerStreakInfoEntry.getKey());
            tag4.setTag("streak", playerStreakInfoEntry.getValue().writeNBT());
            tagCompound.setTag(String.valueOf(i), tag4);
            i++;

        }
        try {
            EncoderHandler.encode(buffer, tagCompound);
            buffer.flip();
            IUCore.network.getServer().sendPacket(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte getId() {
        return 3;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final EntityPlayer entityPlayer) {
        int size = is.readInt();
        NBTTagCompound tagCompound;
        try {
            tagCompound = (NBTTagCompound) DecoderHandler.decode(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (tagCompound == null) {
            return;
        }
        for (int i = 0; i < size; i++) {
            final NBTTagCompound tag2 = tagCompound.getCompoundTag(String.valueOf(i));
            final String nick = tag2.getString("nick");
            final PlayerStreakInfo playerStreakInfo = new PlayerStreakInfo(tag2.getCompoundTag("streak"));
            EventSpectralSuitEffect.mapStreakInfo.remove(nick);
            EventSpectralSuitEffect.mapStreakInfo.put(nick, playerStreakInfo);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
