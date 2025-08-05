package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.render.streak.PlayerStreakInfo;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class PacketColorPickerAllLoggIn implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketColorPickerAllLoggIn() {

    }

    public PacketColorPickerAllLoggIn(Player player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
        buffer.writeByte(this.getId());
        buffer.writeInt(IUCore.mapStreakInfo.size());
        for (Map.Entry<String, PlayerStreakInfo> playerStreakInfoEntry : new HashMap<>(IUCore.mapStreakInfo).entrySet()) {
            buffer.writeString(playerStreakInfoEntry.getKey());
            buffer.writeBytes(playerStreakInfoEntry.getValue().writePacket(buffer.registryAccess()));
        }
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this, player, buffer);
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
        return 3;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final Player entityPlayer) {
        int size = is.readInt();
        for (int i = 0; i < size; i++) {

            final String nick = is.readString();
            final PlayerStreakInfo playerStreakInfo = new PlayerStreakInfo(is);
            IUCore.mapStreakInfo.remove(nick);
            IUCore.mapStreakInfo.put(nick, playerStreakInfo);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
