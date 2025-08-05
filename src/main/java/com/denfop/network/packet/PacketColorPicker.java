package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.render.streak.PlayerStreakInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.player.Player;

public class PacketColorPicker implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketColorPicker() {

    }

    public PacketColorPicker(PlayerStreakInfo playerStreakInfo, String nick, RegistryAccess registryAccess) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(Minecraft.getInstance().player.registryAccess());
        buffer.writeByte(this.getId());
        buffer.writeString(nick);
        buffer.writeBytes(playerStreakInfo.writePacket(buffer.registryAccess()));
        buffer.flip();
        this.buffer = buffer;
        IUCore.network.getClient().sendPacket(this, buffer);
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
        return 9;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final Player entityPlayer) {
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
