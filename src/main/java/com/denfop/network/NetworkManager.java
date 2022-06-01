package com.denfop.network;

import com.denfop.IUCore;
import ic2.api.network.INetworkManager;
import ic2.core.IC2;
import ic2.core.network.GrowingBuffer;
import ic2.core.network.SubPacketType;
import ic2.core.util.LogCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class NetworkManager implements INetworkManager {

    private static FMLEventChannel channel;

    public NetworkManager() {
        if (channel == null) {
            channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("IU");
        }

        channel.register(this);
    }

    private static FMLProxyPacket makePacket(GrowingBuffer buffer) {
        return new FMLProxyPacket(new PacketBuffer(buffer.toByteBuf(true)), "IU");
    }

    protected boolean isClient() {
        return false;
    }

    public final void updateTileEntityField(TileEntity te, String field) {


    }

    public final void initiateTileEntityEvent(TileEntity te, int event, boolean limitRange) {
        assert !this.isClient();


    }

    public final void initiateItemEvent(EntityPlayer player, ItemStack stack, int event, boolean limitRange) {

    }

    public void initiateClientItemEvent(ItemStack stack, int event) {
        assert false;

    }

    public void initiateClientTileEntityEvent(TileEntity te, int event) {
        assert false;

    }

    public final void sendInitialData(TileEntity te) {
        assert !this.isClient();


    }

    @SubscribeEvent
    public void onPacket(ServerCustomPacketEvent event) {
        if (this.getClass() == NetworkManager.class) {
            try {
                this.onPacketData(
                        GrowingBuffer.wrap(event.getPacket().payload()),
                        ((NetHandlerPlayServer) event.getHandler()).player
                );
            } catch (Throwable var3) {
                IC2.log.warn(LogCategory.Network, var3, "Network read failed");
                throw new RuntimeException(var3);
            }

            event.getPacket().payload().release();
        }

    }

    private void onPacketData(GrowingBuffer is, final EntityPlayer player) {
        if (is.hasAvailable()) {
            SubPacketType packetType = SubPacketType.read(is, true);
            if (packetType != null) {
                if (packetType == SubPacketType.KeyUpdate) {
                    final int keyState = is.readInt();
                    IC2.platform.requestTick(true, () -> IUCore.keyboard.processKeyUpdate(player, keyState));
                }

            }
        }
    }

    public void initiateKeyUpdate(int keyState) {
    }

    protected final void sendPacket(GrowingBuffer buffer) {
        if (!this.isClient()) {
            channel.sendToAll(makePacket(buffer));
        } else {
            channel.sendToServer(makePacket(buffer));
        }

    }

}
