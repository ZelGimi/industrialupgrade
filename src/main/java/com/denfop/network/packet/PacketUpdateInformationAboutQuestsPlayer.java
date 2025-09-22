package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.guidebook.GuideBookCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PacketUpdateInformationAboutQuestsPlayer implements IPacket{
    public PacketUpdateInformationAboutQuestsPlayer(){

    }
    public PacketUpdateInformationAboutQuestsPlayer(Map<String, List<String>> map, EntityPlayer player) {
        final CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeUniqueId(player.getUniqueID());
        buffer.writeVarInt(map.size());
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            buffer.writeString(entry.getKey());
            List<String> list = entry.getValue();
            buffer.writeVarInt(list.size());
            for (String str : list) {
                buffer.writeString(str);
            }
        }
        IUCore.network.getServer().sendPacket(buffer, (EntityPlayerMP) player);
    }
    @Override
    public byte getId() {
        return 70;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        final UUID uuid = customPacketBuffer.readUniqueId();
        int mapSize = customPacketBuffer.readVarInt();
        Map<String, List<String>> map = new HashMap<>();
        for (int i = 0; i < mapSize; i++) {
            String key = customPacketBuffer.readString();
            int listSize = customPacketBuffer.readVarInt();
            List<String> list = new ArrayList<>();
            for (int j = 0; j < listSize; j++) {
                list.add(customPacketBuffer.readString());
            }
            map.put(key, list);
        }
        GuideBookCore.instance.setData(uuid,map);
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
