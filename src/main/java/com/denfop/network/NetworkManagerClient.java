package com.denfop.network;

import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.EnumTypePacket;
import com.denfop.network.packet.IPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

import java.lang.reflect.InvocationTargetException;

@OnlyIn(Dist.CLIENT)
public class NetworkManagerClient extends NetworkManager {


    public NetworkManagerClient() {


    }

    @Override
    public void sendPacket(IPacket buffer) {
        PacketDistributor.sendToServer(makePacket(buffer));

    }

    public void sendPacket(IPacket packet, Player player, CustomPacketBuffer buffer) {
        PacketDistributor.sendToServer(makePacket(packet, buffer));
    }

    public void onPacketData(CustomPacketBuffer is, byte type) {
        Player player = Minecraft.getInstance().player;
        IPacket packet = this.packetMap.get(type);
        try {
            packet = packet.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        if (packet != null && packet.getPacketType() == EnumTypePacket.SERVER) {
            packet.readPacket(is, player);
        }
    }

    protected boolean isClient() {
        return true;
    }


    public void onTickEnd(WorldData worldData) {
    }
}
