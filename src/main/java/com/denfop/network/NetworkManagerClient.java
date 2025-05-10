package com.denfop.network;

import com.denfop.IUCore;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.EnumTypePacket;
import com.denfop.network.packet.IPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class NetworkManagerClient extends NetworkManager {


    public NetworkManagerClient() {
        super();

    }

    @Override
    public void sendPacket(CustomPacketBuffer buffer) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            this.sendPacket(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(Minecraft.getInstance().player.getX(),Minecraft.getInstance().player.getY(),Minecraft.getInstance().player.getZ(),2,Minecraft.getInstance().player.getLevel().dimension()))
                    , buffer);
            return;
        }
        UUID playerUUID = IUCore.proxy.getPlayerInstance().getUUID();
        ServerPlayer serverPlayer = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerUUID);
        this.sendPacket(PacketDistributor.PLAYER.with(() -> serverPlayer), buffer);

    }

    public void sendPacket(PacketDistributor.PacketTarget packetDistributor, CustomPacketBuffer buffer) {
        Minecraft.getInstance().getConnection().getConnection().send(makePacket(NetworkDirection.PLAY_TO_SERVER, buffer));
    }
    public void onPacketData(CustomPacketBuffer is,  byte type) {
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
