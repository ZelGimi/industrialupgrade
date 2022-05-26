package com.denfop.network;

import com.denfop.IUCore;
import ic2.api.network.ClientModifiable;
import ic2.api.network.INetworkManager;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.network.DataEncoder;
import ic2.core.network.GrowingBuffer;
import ic2.core.network.SubPacketType;
import ic2.core.util.LogCategory;
import ic2.core.util.ReflectionUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.zip.DeflaterOutputStream;

public class NetworkManager implements INetworkManager {

    public static final String channelName = "ic2";
    private static final Field playerInstancePlayers = ReflectionUtil.getField(PlayerChunkMapEntry.class, List.class);
    private static final int maxPacketDataLength = 32766;
    private static FMLEventChannel channel;

    public NetworkManager() {
        if (channel == null) {
            channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("IU");
        }

        channel.register(this);
    }

    static void writeFieldData(Object object, String fieldName, GrowingBuffer out) throws IOException {
        int pos = fieldName.indexOf(61);
        if (pos != -1) {
            out.writeString(fieldName.substring(0, pos));
            DataEncoder.encode(out, fieldName.substring(pos + 1));
        } else {
            out.writeString(fieldName);

            try {
                DataEncoder.encode(out, ReflectionUtil.getValueRecursive(object, fieldName));
            } catch (NoSuchFieldException var5) {
                throw new RuntimeException("Can't find field " + fieldName + " in " + object.getClass().getName(), var5);
            }
        }

    }

    private static FMLProxyPacket makePacket(GrowingBuffer buffer, boolean advancePos) {
        return new FMLProxyPacket(new PacketBuffer(buffer.toByteBuf(advancePos)), "IU");
    }

    protected boolean isClient() {
        return false;
    }

    public final void updateTileEntityField(TileEntity te, String field) {


    }

    private Field getClientModifiableField(Class<?> cls, String fieldName) {
        Field field = ReflectionUtil.getFieldRecursive(cls, fieldName);
        if (field == null) {
            IC2.log.warn(LogCategory.Network, "Can't find field %s in %s.", fieldName, cls.getName());
            return null;
        } else if (field.getAnnotation(ClientModifiable.class) == null) {
            IC2.log.warn(LogCategory.Network, "The field %s in %s is not modifiable.", fieldName, cls.getName());
            return null;
        } else {
            return field;
        }
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

    public void requestGUI(IHasGui inventory) {
        assert false;

    }

    public final void sendInitialData(TileEntity te) {
        assert !this.isClient();


    }

    final void sendLargePacket(EntityPlayerMP player, int id, GrowingBuffer data) {
        GrowingBuffer buffer = new GrowingBuffer(16384);
        buffer.writeShort(0);

        try {
            DeflaterOutputStream deflate = new DeflaterOutputStream(buffer);
            data.writeTo(deflate);
            deflate.close();
        } catch (IOException var8) {
            throw new RuntimeException(var8);
        }

        buffer.flip();
        boolean firstPacket = true;

        boolean lastPacket;
        do {
            lastPacket = buffer.available() <= 32766;
            if (!firstPacket) {
                buffer.skipBytes(-2);
            }

            SubPacketType.LargePacket.writeTo(buffer);
            int state = 0;
            if (firstPacket) {
                state |= 1;
            }

            if (lastPacket) {
                state |= 2;
            }

            state |= id << 2;
            buffer.write(state);
            buffer.skipBytes(-2);
            if (lastPacket) {
                this.sendPacket(buffer, true, player);

                assert !buffer.hasAvailable();
            } else {
                this.sendPacket(buffer.copy(32766), true, player);
            }

            firstPacket = false;
        } while (!lastPacket);

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

    private void onPacketData(GrowingBuffer is, final EntityPlayer player) throws IOException {
        if (is.hasAvailable()) {
            SubPacketType packetType = SubPacketType.read(is, true);
            if (packetType != null) {
                if (packetType == SubPacketType.KeyUpdate) {
                    final int keyState = is.readInt();
                    IC2.platform.requestTick(true, new Runnable() {
                        public void run() {
                            IUCore.keyboard.processKeyUpdate(player, keyState);
                        }
                    });
                }

            }
        }
    }

    public void initiateKeyUpdate(int keyState) {
    }

    public void sendLoginData() {
    }

    protected final void sendPacket(GrowingBuffer buffer) {
        if (!this.isClient()) {
            channel.sendToAll(makePacket(buffer, true));
        } else {
            channel.sendToServer(makePacket(buffer, true));
        }

    }

    protected final void sendPacket(GrowingBuffer buffer, boolean advancePos, EntityPlayerMP player) {
        assert !this.isClient();

        channel.sendTo(makePacket(buffer, advancePos), player);
    }

}
