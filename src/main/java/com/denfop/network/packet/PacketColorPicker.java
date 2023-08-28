package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.render.streak.PlayerStreakInfo;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;

public class PacketColorPicker implements IPacket {

    public PacketColorPicker() {

    }

    public PacketColorPicker(PlayerStreakInfo playerStreakInfo, String nick) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeString(nick);
        try {
            EncoderHandler.encode(buffer, playerStreakInfo.writeNBT());
            buffer.flip();
            IUCore.network.getClient().sendPacket(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte getId() {
        return 9;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final EntityPlayer entityPlayer) {
        final String nick = is.readString();
        PlayerStreakInfo playerStreakInfo;
        try {
            playerStreakInfo = DecoderHandler.decode(is, PlayerStreakInfo.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IUCore.mapStreakInfo.remove(nick);
        IUCore.mapStreakInfo.put(nick, playerStreakInfo);
        new PacketColorPickerAllLoggIn();
    }

    @Override
    public EnumTypePacket getPacketType() {
        return null;
    }

}
