package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.guidebook.GuideBookCore;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class PacketUpdateCompleteQuest implements IPacket {

    public PacketUpdateCompleteQuest() {

    }

    public PacketUpdateCompleteQuest(EntityPlayer player, String tab, String quest) {
        GuideBookCore.instance.remove(player.getUniqueID(), tab, quest);
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());
        customPacketBuffer.writeUniqueId(player.getUniqueID());
        customPacketBuffer.writeString(tab);
        customPacketBuffer.writeString(quest);
        IUCore.network.getClient().sendPacket(customPacketBuffer);
    }

    @Override
    public byte getId() {
        return 71;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        final UUID uuid = customPacketBuffer.readUniqueId();
        String tab = customPacketBuffer.readString();
        String quest = customPacketBuffer.readString();
        if (entityPlayer.getUniqueID().equals(uuid)) {

            GuideBookCore.instance.remove(uuid, tab, quest);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
