package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.items.space.teleport.SpaceTeleportController;
import com.denfop.items.space.teleport.SpaceTeleportScreenData;
import com.denfop.screen.ScreenPlanetaryTranslocator;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PacketOpenPlanetaryTranslocatorScreen implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketOpenPlanetaryTranslocatorScreen() {
    }

    public PacketOpenPlanetaryTranslocatorScreen(final ServerPlayer player, final ItemStack stack) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
        buffer.writeByte(getId());

        SpaceTeleportScreenData data = SpaceTeleportController.buildScreenData(player, stack);
        data.write(buffer);
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this, buffer, player);
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
        return 90;
    }

    @Override
    public void readPacket(final CustomPacketBuffer buffer, final Player entityPlayer) {
        SpaceTeleportScreenData data = new SpaceTeleportScreenData(buffer);
        data.setScreen();

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}