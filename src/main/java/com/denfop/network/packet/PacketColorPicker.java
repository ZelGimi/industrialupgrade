package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.render.streak.PlayerStreakInfo;
import net.minecraft.entity.player.EntityPlayer;

public class PacketColorPicker implements IPacket {

    public PacketColorPicker() {

    }

    public PacketColorPicker(PlayerStreakInfo playerStreakInfo, String nick) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeString(nick);
        buffer.writeBytes(playerStreakInfo.writePacket());
        buffer.flip();
        IUCore.network.getClient().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return 9;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final EntityPlayer entityPlayer) {
        final String nick = is.readString();
        PlayerStreakInfo playerStreakInfo = new PlayerStreakInfo(is);
        IUCore.mapStreakInfo.remove(nick);
        IUCore.mapStreakInfo.put(nick, playerStreakInfo);
        new PacketColorPickerAllLoggIn();
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
