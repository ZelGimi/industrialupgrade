package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.pollution.client.PollutionClientRenderRefresh;
import com.denfop.api.pollution.radiation.Radiation;
import com.denfop.api.pollution.radiation.RadiationSystem;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;

import java.io.IOException;
import java.util.*;

public class PacketRadiation implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketRadiation() {
    }

    public PacketRadiation(List<Radiation> radiation, Player player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
        buffer.writeByte(this.getId());
        buffer.writeInt(radiation.size());
        radiation.forEach(radiation1 -> {
            try {
                EncoderHandler.encode(buffer, radiation1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        buffer.flip();
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this, buffer, (ServerPlayer) player);
    }

    private static boolean sameRadiation(Radiation a, Radiation b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (!a.getPos().equals(b.getPos())) {
            return false;
        }
        if (a.getLevel() != b.getLevel()) {
            return false;
        }
        if (a.getCoef() != b.getCoef()) {
            return false;
        }
        return Math.abs(a.getRadiation() - b.getRadiation()) < 0.0001D;
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
        return 4;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final Player entityPlayer) {
        Map<ChunkPos, Radiation> oldMap = new HashMap<>(RadiationSystem.rad_system.getMap());
        Map<ChunkPos, Radiation> newMap = new HashMap<>();

        final int size = is.readInt();
        for (int i = 0; i < size; i++) {
            Radiation radiation;
            try {
                radiation = (Radiation) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            newMap.put(radiation.getPos(), radiation);
        }

        RadiationSystem.rad_system.getRadiationList().clear();
        RadiationSystem.rad_system.getMap().clear();

        for (Radiation radiation : newMap.values()) {
            RadiationSystem.rad_system.addRadiationWihoutUpdate(radiation);
        }

        Set<ChunkPos> changedChunks = new HashSet<>();
        Set<ChunkPos> keys = new HashSet<>(oldMap.keySet());
        keys.addAll(newMap.keySet());

        for (ChunkPos pos : keys) {
            Radiation oldValue = oldMap.get(pos);
            Radiation newValue = newMap.get(pos);

            if (!sameRadiation(oldValue, newValue)) {
                changedChunks.add(pos);
            }
        }

        PollutionClientRenderRefresh.queueFullRadiationSnapshotApplied(changedChunks);
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}