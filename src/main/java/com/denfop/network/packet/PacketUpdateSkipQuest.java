package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.guidebook.GuideBookCore;
import com.denfop.utils.ExperienceUtils;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class PacketUpdateSkipQuest implements IPacket {

    public PacketUpdateSkipQuest() {

    }

    public PacketUpdateSkipQuest(Player player, String tab, String quest) {
        GuideBookCore.instance.remove(player.getUUID(), tab, quest);
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());
        customPacketBuffer.writeUUID(player.getUUID());
        customPacketBuffer.writeString(tab);
        customPacketBuffer.writeString(quest);
        IUCore.network.getClient().sendPacket(customPacketBuffer);
    }

    @Override
    public byte getId() {
        return 72;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        final UUID uuid = customPacketBuffer.readUUID();
        String tab = customPacketBuffer.readString();
        String quest = customPacketBuffer.readString();
        if (entityPlayer.getUUID().equals(uuid)) {
            GuideBookCore.instance.remove(uuid, tab, quest);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
