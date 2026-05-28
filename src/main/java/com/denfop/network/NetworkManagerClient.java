package com.denfop.network;

import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.EnumTypePacket;
import com.denfop.network.packet.IPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
public class NetworkManagerClient extends NetworkManager {


    public NetworkManagerClient() {
        super();

    }

    @Override
    public void sendPacket(CustomPacketBuffer buffer) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getConnection() == null) {
            return;
        }

        mc.getConnection().send(makePacket(NetworkDirection.PLAY_TO_SERVER, buffer));
    }

    public void sendPacket(PacketDistributor.PacketTarget ignored, CustomPacketBuffer buffer) {
        this.sendPacket(buffer);
    }

    public void onPacketData(CustomPacketBuffer is, byte type) {
        Player player = Minecraft.getInstance().player;
        IPacket packet = this.packetMap.get(type);
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
