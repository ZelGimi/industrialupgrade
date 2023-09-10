package com.denfop.network;

import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.EnumTypePacket;
import com.denfop.network.packet.IPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NetworkManagerClient extends NetworkManager {


    public NetworkManagerClient() {
    }


    private void onPacketData(CustomPacketBuffer is, final EntityPlayer player) {
        if (is.writerIndex() > is.readerIndex()) {
            byte type = is.readByte();
            IPacket packet = this.packetMap.get(type);
            if (packet != null && packet.getPacketType() == EnumTypePacket.SERVER) {
                packet.readPacket(is, player);
            }
        }
    }

    protected boolean isClient() {
        return true;
    }


    @SubscribeEvent
    public void onPacket(ClientCustomPacketEvent event) {
        assert !this.getClass().getName().equals(NetworkManager.class.getName());

        try {
            this.onPacketData(new CustomPacketBuffer(event.getPacket().payload()), Minecraft.getMinecraft().player);
        } catch (Throwable var3) {
            throw new RuntimeException(var3);
        }

        event.getPacket().payload().release();
    }


}
