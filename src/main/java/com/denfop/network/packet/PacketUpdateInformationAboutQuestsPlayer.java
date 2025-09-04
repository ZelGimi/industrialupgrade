package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.guidebook.GuideBookCore;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class PacketUpdateInformationAboutQuestsPlayer implements IPacket {
    public PacketUpdateInformationAboutQuestsPlayer() {

    }

    public PacketUpdateInformationAboutQuestsPlayer(Map<String, List<String>> map, Player player) {
        final CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeUUID(player.getUUID());
        buffer.writeVarInt(map.size());
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            buffer.writeString(entry.getKey());
            List<String> list = entry.getValue();
            buffer.writeVarInt(list.size());
            for (String str : list) {
                buffer.writeString(str);
            }
        }
        IUCore.network.getServer().sendPacket(buffer, (ServerPlayer) player);
    }

    @Override
    public byte getId() {
        return 70;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        final UUID uuid = customPacketBuffer.readUUID();
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
        GuideBookCore.instance.setData(uuid, map);
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
