package com.denfop.network;

import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.item.IHandHeldInventory;
import ic2.core.item.IHandHeldSubInventory;
import ic2.core.network.DataEncoder;
import ic2.core.network.GrowingBuffer;
import ic2.core.network.IRpcProvider;
import ic2.core.network.SubPacketType;
import ic2.core.util.LogCategory;
import ic2.core.util.StackUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.zip.InflaterOutputStream;

@SideOnly(Side.CLIENT)
public class NetworkManagerClient extends NetworkManager {

    private GrowingBuffer largePacketBuffer;

    public NetworkManagerClient() {
    }

    protected boolean isClient() {
        return true;
    }

    public void initiateClientItemEvent(ItemStack stack, int event) {
        try {
            GrowingBuffer buffer = new GrowingBuffer(256);
            SubPacketType.ItemEvent.writeTo(buffer);
            DataEncoder.encode(buffer, stack, false);
            buffer.writeInt(event);
            buffer.flip();
            this.sendPacket(buffer);
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }

    public void initiateKeyUpdate(int keyState) {
        GrowingBuffer buffer = new GrowingBuffer(5);
        SubPacketType.KeyUpdate.writeTo(buffer);
        buffer.writeInt(keyState);
        buffer.flip();
        this.sendPacket(buffer);
    }

    public void initiateClientTileEntityEvent(TileEntity te, int event) {
        try {
            GrowingBuffer buffer = new GrowingBuffer(32);
            SubPacketType.TileEntityEvent.writeTo(buffer);
            DataEncoder.encode(buffer, te, false);
            buffer.writeInt(event);
            buffer.flip();
            this.sendPacket(buffer);
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }

    public void initiateRpc(int id, Class<? extends IRpcProvider<?>> provider, Object[] args) {
        try {
            GrowingBuffer buffer = new GrowingBuffer(256);
            SubPacketType.Rpc.writeTo(buffer);
            buffer.writeInt(id);
            buffer.writeString(provider.getName());
            DataEncoder.encode(buffer, args);
            buffer.flip();
            this.sendPacket(buffer);
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }
    }

    public void requestGUI(IHasGui inventory) {
        try {
            GrowingBuffer buffer = new GrowingBuffer(32);
            SubPacketType.RequestGUI.writeTo(buffer);
            if (inventory instanceof TileEntity) {
                TileEntity te = (TileEntity) inventory;
                buffer.writeBoolean(false);
                DataEncoder.encode(buffer, te, false);
            } else {
                EntityPlayer player = Minecraft.getMinecraft().player;
                if ((StackUtil.isEmpty(player.inventory.getCurrentItem()) || !(player.inventory
                        .getCurrentItem()
                        .getItem() instanceof IHandHeldInventory)) && (StackUtil.isEmpty(player.getHeldItemOffhand()) || !(player
                        .getHeldItemOffhand()
                        .getItem() instanceof IHandHeldInventory))) {
                    IC2.platform.displayError(
                            "An unknown GUI type was attempted to be displayed.\nThis could happen due to corrupted data from a player or a bug.\n\n(Technical information: " + inventory + ")"
                    );
                } else {
                    buffer.writeBoolean(true);
                }
            }

            buffer.flip();
            this.sendPacket(buffer);
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }

    @SubscribeEvent
    public void onPacket(ClientCustomPacketEvent event) {
        assert !this.getClass().getName().equals(NetworkManager.class.getName());

        try {
            this.onPacketData(GrowingBuffer.wrap(event.getPacket().payload()), Minecraft.getMinecraft().player);
        } catch (Throwable var3) {
            IC2.log.warn(LogCategory.Network, var3, "Network read failed");
            throw new RuntimeException(var3);
        }

        event.getPacket().payload().release();
    }

    private void onPacketData(GrowingBuffer is, final EntityPlayer player) throws IOException {
        if (is.hasAvailable()) {
            SubPacketType packetType = SubPacketType.read(is, false);
            if (packetType != null) {
                final Object worldDeferred;
                final BlockPos pos;
                final double x;
                final double y;
                final double z;
                final int state;
                final int windowId;
                final int currentItemPosition;
                final int dataLen;
                switch (packetType) {
                    case LargePacket:
                        state = is.readUnsignedByte();
                        if ((state & 2) != 0) {
                            GrowingBuffer input;
                            if ((state & 1) != 0) {
                                input = is;
                            } else {
                                input = this.largePacketBuffer;
                                if (input == null) {
                                    throw new IOException("unexpected large packet continuation");
                                }

                                is.writeTo(input);
                                input.flip();
                                this.largePacketBuffer = null;
                            }

                            GrowingBuffer decompBuffer = new GrowingBuffer(input.available() * 2);
                            InflaterOutputStream inflate = new InflaterOutputStream(decompBuffer);
                            input.writeTo(inflate);
                            inflate.close();
                            decompBuffer.flip();
                            switch (state >> 2) {
                                case 1:
                                    processChatPacket(decompBuffer);
                                    return;
                                case 2:
                                    processConsolePacket(decompBuffer);
                            }
                        } else {
                            if ((state & 1) != 0) {
                                assert this.largePacketBuffer == null;

                                this.largePacketBuffer = new GrowingBuffer(32752);
                            }

                            if (this.largePacketBuffer == null) {
                                throw new IOException("unexpected large packet continuation");
                            }

                            is.writeTo(this.largePacketBuffer);
                        }
                        break;
                    case GuiDisplay:
                        final boolean isAdmin = is.readBoolean();
                        switch (is.readByte()) {
                            case 0:
                                final Object teDeferred = DataEncoder.decodeDeferred(is, TileEntity.class);
                                windowId = is.readInt();
                                IC2.platform.requestTick(false, new Runnable() {
                                    public void run() {
                                        EntityPlayer player = IC2.platform.getPlayerInstance();
                                        TileEntity te = DataEncoder.getValue(teDeferred);
                                        if (te instanceof IHasGui) {
                                            IC2.platform.launchGuiClient(player, (IHasGui) te, isAdmin);
                                            player.openContainer.windowId = windowId;
                                        } else if (player instanceof EntityPlayerSP) {
                                            ((EntityPlayerSP) player).connection.sendPacket(new CPacketCloseWindow(windowId));
                                        }

                                    }
                                });
                                return;
                            case 1:
                                currentItemPosition = is.readInt();
                                final boolean subGUI = is.readBoolean();
                                final short ID = subGUI ? is.readShort() : 0;
                                dataLen = is.readInt();
                                IC2.platform.requestTick(false, new Runnable() {
                                    public void run() {
                                        EntityPlayer player = IC2.platform.getPlayerInstance();
                                        ItemStack currentItem;
                                        if (currentItemPosition < 0) {
                                            int actualItemPosition = ~currentItemPosition;
                                            if (actualItemPosition > player.inventory.offHandInventory.size() - 1) {
                                                return;
                                            }

                                            currentItem = player.inventory.offHandInventory.get(actualItemPosition);
                                        } else {
                                            if (currentItemPosition != player.inventory.currentItem) {
                                                return;
                                            }

                                            currentItem = player.inventory.getCurrentItem();
                                        }

                                        if (!currentItem.isEmpty() && currentItem.getItem() instanceof IHandHeldInventory) {
                                            if (subGUI && currentItem.getItem() instanceof IHandHeldSubInventory) {
                                                IC2.platform.launchGuiClient(
                                                        player,
                                                        ((IHandHeldSubInventory) currentItem.getItem()).getSubInventory(player,
                                                                currentItem,
                                                                ID
                                                        ),
                                                        isAdmin
                                                );
                                            } else {
                                                IC2.platform.launchGuiClient(
                                                        player,
                                                        ((IHandHeldInventory) currentItem.getItem()).getInventory(
                                                                player,
                                                                currentItem
                                                        ),
                                                        isAdmin
                                                );
                                            }
                                        } else if (player instanceof EntityPlayerSP) {
                                            ((EntityPlayerSP) player).connection.sendPacket(new CPacketCloseWindow(dataLen));
                                        }

                                        player.openContainer.windowId = dataLen;
                                    }
                                });
                                return;
                            default:
                        }

                }

            }
        }
    }

    private static void processChatPacket(GrowingBuffer buffer) {
        final String messages = buffer.readString();
        IC2.platform.requestTick(false, new Runnable() {
            public void run() {
                String[] var1 = messages.split("[\\r\\n]+");
                int var2 = var1.length;

                for (int var3 = 0; var3 < var2; ++var3) {
                    String line = var1[var3];
                    IC2.platform.messagePlayer(null, line);
                }

            }
        });
    }

    private static void processConsolePacket(GrowingBuffer buffer) {
        String messages = buffer.readString();
        PrintStream console = new PrintStream(new FileOutputStream(FileDescriptor.out));
        String[] var3 = messages.split("[\\r\\n]+");
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            String line = var3[var5];
            console.println(line);
        }

        console.flush();
    }

}
