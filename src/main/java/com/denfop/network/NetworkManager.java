package com.denfop.network;

import com.denfop.IUCore;
import com.denfop.api.inv.IHasGui;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.research.main.BaseLevelSystem;
import com.denfop.api.research.main.EnumLeveling;
import com.denfop.container.ContainerBase;
import com.denfop.items.HandHeldInventory;
import com.denfop.items.IHandHeldInventory;
import com.denfop.items.IHandHeldSubInventory;
import com.denfop.render.streak.PlayerStreakInfo;
import ic2.api.network.ClientModifiable;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkManager;
import ic2.core.IC2;
import ic2.core.block.TileEntityBlock;
import ic2.core.network.GrowingBuffer;
import ic2.core.network.IPlayerItemDataListener;
import ic2.core.util.LogCategory;
import ic2.core.util.ReflectionUtil;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;

public class NetworkManager implements INetworkManager {

    private static final Field playerInstancePlayers = ReflectionUtil.getField(PlayerChunkMapEntry.class, List.class);
    private static FMLEventChannel channel;

    public NetworkManager() {
        if (channel == null) {
            channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("IU");
        }

        channel.register(this);
    }

    private static FMLProxyPacket makePacket(GrowingBuffer buffer, boolean advancePos) {
        return new FMLProxyPacket(new PacketBuffer(buffer.toByteBuf(advancePos)), "IU");
    }

    private static FMLProxyPacket makePacket(GrowingBuffer buffer) {
        return new FMLProxyPacket(new PacketBuffer(buffer.toByteBuf(true)), "IU");
    }

    static <T extends Collection<EntityPlayerMP>> T getPlayersInRange(World world, BlockPos pos, T result) {
        if (world instanceof WorldServer) {
            PlayerChunkMap playerManager = ((WorldServer) world).getPlayerChunkMap();
            PlayerChunkMapEntry instance = playerManager.getEntry(pos.getX() >> 4, pos.getZ() >> 4);
            if (instance != null) {
                result.addAll(ReflectionUtil.getFieldValue(playerInstancePlayers, instance));
            }
        }
        return result;
    }

    private static TeUpdateDataServer getTeUpdateData(TileEntity te) {
        assert IC2.platform.isSimulating();

        if (te == null) {
            throw new NullPointerException();
        } else {
            WorldData worldData = WorldData.get(te.getWorld());
            TeUpdateDataServer ret = worldData.tesToUpdate.get(te);
            if (ret == null) {
                ret = new TeUpdateDataServer();
                worldData.tesToUpdate.put(te, ret);
            }

            return ret;
        }
    }

    private static TeUpdateDataServer getTeUpdateData(World te) {
        assert IC2.platform.isSimulating();

        if (te == null) {
            throw new NullPointerException();
        } else {
            WorldData worldData = WorldData.get(te);
            TeUpdateDataServer ret;
            ret = new TeUpdateDataServer();
            worldData.tesToUpdate.put(null, ret);

            return ret;
        }
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

    public void onTickEnd(WorldData worldData) {
        try {
            TeUpdate.send(worldData, this);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    protected boolean isClient() {
        return false;
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

    protected void onCommonPacketData(
            SubPacketType packetType,
            boolean simulating,
            GrowingBuffer is,
            final EntityPlayer player
    ) throws IOException {
        final String fieldName;
        final Object value;
        final int windowId;
        switch (packetType) {
            case PlayerItemData:
                final int slot = is.readByte();
                final Item item = DataEncoder.decode(is, Item.class);
                int dataCount = is.readVarInt();
                final Object[] subData = new Object[dataCount];

                for (int i = 0; i < dataCount; ++i) {
                    subData[i] = DataEncoder.decode(is);
                }

                if (slot >= 0 && slot < 9) {
                    IC2.platform.requestTick(simulating, () -> {
                        for (int i = 0; i < subData.length; ++i) {
                            subData[i] = DataEncoder.getValue(subData[i]);
                        }

                        ItemStack stack = player.inventory.mainInventory.get(slot);
                        if (!StackUtil.isEmpty(stack) && stack.getItem() == item && item instanceof IPlayerItemDataListener) {
                            ((IPlayerItemDataListener) item).onPlayerItemNetworkData(player, slot, subData);
                        }

                    });
                }
                break;
            case ContainerData:
                windowId = is.readInt();
                fieldName = is.readString();
                value = DataEncoder.decode(is);
                IC2.platform.requestTick(simulating, () -> {
                    if (player.openContainer instanceof ContainerBase && player.openContainer.windowId == windowId && (NetworkManager.this.isClient() || NetworkManager.this.getClientModifiableField(
                            player.openContainer.getClass(),
                            fieldName
                    ) != null)) {
                        ReflectionUtil.setValueRecursive(player.openContainer, fieldName, DataEncoder.getValue(value));
                    }

                });
                break;
            case ContainerEvent:
                windowId = is.readInt();
                fieldName = is.readString();
                IC2.platform.requestTick(simulating, () -> {
                    if (player.openContainer instanceof ContainerBase && player.openContainer.windowId == windowId) {
                        ((ContainerBase) player.openContainer).onContainerEvent(fieldName);
                    }

                });
                break;
            case HandHeldInvData:
                windowId = is.readInt();
                fieldName = is.readString();
                value = DataEncoder.decode(is);
                IC2.platform.requestTick(simulating, () -> {
                    if (player.openContainer instanceof ContainerBase && player.openContainer.windowId == windowId) {
                        ContainerBase<?> container = (ContainerBase) player.openContainer;
                        if (container.base instanceof HandHeldInventory && (NetworkManager.this.isClient() || NetworkManager.this.getClientModifiableField(
                                container.base.getClass(),
                                fieldName
                        ) != null)) {
                            ReflectionUtil.setValueRecursive(container.base, fieldName, DataEncoder.getValue(value));
                        }
                    }

                });
                break;
            case TileEntityData:
                Object teDeferred = DataEncoder.decodeDeferred(is, TileEntity.class);
                fieldName = is.readString();
                value = DataEncoder.decode(is);
                IC2.platform.requestTick(simulating, () -> {
                    TileEntity te = DataEncoder.getValue(teDeferred);
                    if (te != null && (NetworkManager.this.isClient() || NetworkManager.this.getClientModifiableField(
                            te.getClass(),
                            fieldName
                    ) != null)) {

                        ReflectionUtil.setValueRecursive(te, fieldName, DataEncoder.getValue(value));
                    }

                });
                break;
            case TileEntityEvent:
                teDeferred = DataEncoder.decodeDeferred(is, TileEntity.class);
                final int event = is.readInt();
                IC2.platform.requestTick(true, () -> {
                    TileEntity te = DataEncoder.getValue(teDeferred);
                    if (te instanceof INetworkClientTileEntityEventListener) {
                        ((INetworkClientTileEntityEventListener) te).onNetworkEvent(player, event);
                    }

                });
                break;
            default:
                IC2.log.warn(LogCategory.Network, "Unhandled packet type: %s", packetType.name());
        }

    }

    public final void updateTileEntityField(TileEntity te, String field) {
        if (!this.isClient()) {
            getTeUpdateData(te).addGlobalField(field);
        } else if (this.getClientModifiableField(te.getClass(), field) == null) {
            IC2.log.warn(LogCategory.Network, "Field update for %s failed.", te);
        } else {
            GrowingBuffer buffer = new GrowingBuffer(64);

            try {
                SubPacketType.TileEntityData.writeTo(buffer);
                DataEncoder.encode(buffer, te, false);
                writeFieldData(te, field, buffer);
            } catch (IOException var5) {
                throw new RuntimeException(var5);
            }

            buffer.flip();
            this.sendPacket(buffer);
        }

    }

    public final void initiateTileEntityEvent(TileEntity te, int event, boolean limitRange) {
        assert !this.isClient();

        if (!te.getWorld().playerEntities.isEmpty()) {
            GrowingBuffer buffer = new GrowingBuffer(32);

            try {
                SubPacketType.TileEntityEvent.writeTo(buffer);
                DataEncoder.encode(buffer, te, false);
                buffer.writeInt(event);
            } catch (IOException var9) {
                throw new RuntimeException(var9);
            }

            buffer.flip();
            Iterator<EntityPlayerMP> var5 =
                    getPlayersInRange(te.getWorld(), te.getPos(), new ArrayList<>()).iterator();

            while (true) {
                EntityPlayerMP target;
                int dX;
                int dZ;
                do {
                    if (!var5.hasNext()) {
                        return;
                    }

                    target = var5.next();
                    if (!limitRange) {
                        break;
                    }

                    dX = (int) ((double) te.getPos().getX() + 0.5D - target.posX);
                    dZ = (int) ((double) te.getPos().getZ() + 0.5D - target.posZ);
                } while (dX * dX + dZ * dZ > 400);

                this.sendPacket(buffer, false, target);
            }
        }


    }

    public final void initiateItemEvent(EntityPlayer player, ItemStack stack, int event, boolean limitRange) {
        if (StackUtil.isEmpty(stack)) {
            throw new NullPointerException("invalid stack: " + StackUtil.toStringSafe(stack));
        } else {
            assert !this.isClient();

            GrowingBuffer buffer = new GrowingBuffer(256);

            try {
                SubPacketType.ItemEvent.writeTo(buffer);
                DataEncoder.encode(buffer, player.getGameProfile(), false);
                DataEncoder.encode(buffer, stack, false);
                buffer.writeInt(event);
            } catch (Exception var10) {
                throw new RuntimeException(var10);
            }

            buffer.flip();
            Iterator<EntityPlayerMP> var6 = getPlayersInRange(
                    player.getEntityWorld(),
                    player.getPosition(),
                    new ArrayList<>()
            ).iterator();

            while (true) {
                EntityPlayerMP target;
                int dX;
                int dZ;
                do {
                    if (!var6.hasNext()) {
                        return;
                    }

                    target = var6.next();
                    if (!limitRange) {
                        break;
                    }

                    dX = (int) (player.posX - target.posX);
                    dZ = (int) (player.posZ - target.posZ);
                } while (dX * dX + dZ * dZ > 400);

                this.sendPacket(buffer, false, target);
            }
        }
    }

    public void initiateClientItemEvent(ItemStack stack, int event) {
        assert false;

    }

    public void initiateClientTileEntityEvent(TileEntity te, int event) {
        assert false;

    }

    public final void sendInitialData(TileEntity te, EntityPlayerMP player) {
        assert !this.isClient();

        if (te instanceof INetworkDataProvider) {
            TeUpdateDataServer updateData = getTeUpdateData(te);

            for (final String field : ((INetworkDataProvider) te).getNetworkFields()) {
                updateData.addPlayerField(field, player);
            }
        }

    }

    public final void sendInitialData(TileEntity te) {
        assert !this.isClient();
        if (te instanceof INetworkDataProvider) {
            TeUpdateDataServer updateData = getTeUpdateData(te);
            List<String> fields = ((INetworkDataProvider) te).getNetworkFields();

            for (final String field : fields) {
                updateData.addGlobalField(field);
            }

            if (TeUpdate.debug) {
                IC2.log.info(
                        LogCategory.Network,
                        "Sending initial TE data for %s (%s).",
                        Util.formatPosition(te), fields
                );
            }
        }

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
                    IC2.platform.requestTick(true, () -> IUCore.keyboard.processKeyUpdate(player, keyState));
                    return;
                } else if (packetType == SubPacketType.ColorPicker) {
                    final String nick = is.readString();
                    PlayerStreakInfo playerStreakInfo = DataEncoder.decode(is, PlayerStreakInfo.class);
                    IUCore.mapStreakInfo.remove(nick);
                    IUCore.mapStreakInfo.put(nick, playerStreakInfo);
                    updateColorPickerAll();
                    return;
                }
                onCommonPacketData(packetType, true, is, player);
            }

        }

    }

    public void initiateKeyUpdate(int keyState) {
    }

    protected final void sendPacket(GrowingBuffer buffer, boolean advancePos, EntityPlayerMP player) {
        assert !this.isClient();

        channel.sendTo(makePacket(buffer, advancePos), player);
    }

    protected final void sendPacket(GrowingBuffer buffer) {
        if (!this.isClient()) {
            channel.sendToAll(makePacket(buffer));
        } else {
            channel.sendToServer(makePacket(buffer));
        }

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

    public final void updateTileEntityFieldTo(TileEntity te, String field, EntityPlayerMP player) {
        assert !this.isClient();

        getTeUpdateData(te).addPlayerField(field, player);
    }

    public final void updateTileEntityFieldTo(World te, String field, EntityPlayerMP player) {
        assert !this.isClient();

        getTeUpdateData(te).addPlayerField(field, player);
    }

    public void initiateResearchSystem(BaseLevelSystem levelSystem) {
        GrowingBuffer buffer = new GrowingBuffer(64);
        try {
            SubPacketType.LevelSystem.writeTo(buffer);
            DataEncoder.encode(buffer, levelSystem, false);
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }

        buffer.flip();
        this.sendPacket(buffer);
    }

    public void initiateResearchSystemAdd(EnumLeveling level, int add, String name) {
        GrowingBuffer buffer = new GrowingBuffer(64);
        try {
            SubPacketType.LevelSystemAdd.writeTo(buffer);
            DataEncoder.encode(buffer, name, false);
            DataEncoder.encode(buffer, level.ordinal(), false);
            DataEncoder.encode(buffer, add, false);

        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }

        buffer.flip();
        this.sendPacket(buffer);
    }

    public void initiateResearchSystemDelete(String name) {
        GrowingBuffer buffer = new GrowingBuffer(64);
        try {
            SubPacketType.LevelSystemRemove.writeTo(buffer);
            DataEncoder.encode(buffer, name, false);


        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }

        buffer.flip();
        this.sendPacket(buffer);
    }

    public void initiateRadiation(List<Radiation> radiation, EntityPlayer player) {
        GrowingBuffer buffer = new GrowingBuffer();

        SubPacketType.Radiation.writeTo(buffer);
        buffer.writeString(player.getName());
        buffer.writeInt(radiation.size());
        radiation.forEach(radiation1 -> {
            try {
                DataEncoder.encode(buffer, radiation, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        buffer.flip();
        this.sendPacket(buffer);
    }

    public void initiateRadiation(Radiation radiation) {
        GrowingBuffer buffer = new GrowingBuffer(64);
        try {
            SubPacketType.RadiationUpdate.writeTo(buffer);
            DataEncoder.encode(buffer, radiation, false);


        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }

        buffer.flip();
        this.sendPacket(buffer);
    }

    public final void sendComponentUpdate(TileEntityBlock te, String componentName, EntityPlayerMP player, GrowingBuffer data) {
        assert !this.isClient();

        if (player.getEntityWorld() != te.getWorld()) {
            throw new IllegalArgumentException("mismatched world (te " + te.getWorld() + ", player " + player.getEntityWorld() + ")");
        } else {
            GrowingBuffer buffer = new GrowingBuffer(64);

            try {
                SubPacketType.TileEntityBlockComponent.writeTo(buffer);
                DataEncoder.encode(buffer, te, false);
                buffer.writeString(componentName);
                buffer.writeVarInt(data.available());
                data.writeTo(buffer);
            } catch (IOException var7) {
                throw new RuntimeException(var7);
            }

            buffer.flip();
            this.sendPacket(buffer, true, player);
        }
    }

    public final void initiateGuiDisplay(EntityPlayerMP player, IHasGui inventory, int windowId) {
        this.initiateGuiDisplay(player, inventory, windowId, null);
    }

    public final void initiateGuiDisplay(EntityPlayerMP player, IHasGui inventory, int windowId, Integer ID) {
        assert !this.isClient();

        try {
            GrowingBuffer buffer = new GrowingBuffer(32);
            SubPacketType.GuiDisplay.writeTo(buffer);
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            boolean isAdmin = server.getPlayerList().canSendCommands(player.getGameProfile());
            buffer.writeBoolean(isAdmin);
            if (inventory instanceof TileEntity) {
                TileEntity te = (TileEntity) inventory;
                buffer.writeByte(0);
                DataEncoder.encode(buffer, te, false);
            } else {
                player.inventory.getCurrentItem();
                if (player.inventory.getCurrentItem().getItem() instanceof IHandHeldInventory) {
                    buffer.writeByte(1);
                    buffer.writeInt(player.inventory.currentItem);
                    this.handleSubData(buffer, player.inventory.getCurrentItem(), ID);
                } else {
                    player.getHeldItemOffhand();
                    if (player.getHeldItemOffhand().getItem() instanceof IHandHeldInventory) {
                        buffer.writeByte(1);
                        buffer.writeInt(-1);
                        this.handleSubData(buffer, player.getHeldItemOffhand(), ID);
                    } else {
                        IC2.platform.displayError(
                                "An unknown GUI type was attempted to be displayed.\nThis could happen due to corrupted data from a player or a bug.\n\n(Technical information: " + inventory + ")");
                    }
                }
            }

            buffer.writeInt(windowId);
            buffer.flip();
            this.sendPacket(buffer, true, player);
        } catch (IOException var9) {
            throw new RuntimeException(var9);
        }
    }

    private void handleSubData(GrowingBuffer buffer, ItemStack stack, Integer ID) {
        boolean subInv = ID != null && stack.getItem() instanceof IHandHeldSubInventory;
        buffer.writeBoolean(subInv);
        if (subInv) {
            buffer.writeShort(ID);
        }

    }

    public final void sendContainerFields(ContainerBase<?> container, String... fieldNames) {
        int var4 = fieldNames.length;

        for (String fieldName : fieldNames) {
            this.sendContainerField(container, fieldName);
        }

    }

    public final void sendContainerField(ContainerBase<?> container, String fieldName) {
        if (this.isClient() && this.getClientModifiableField(container.getClass(), fieldName) == null) {
            IC2.log.warn(LogCategory.Network, "Field update for %s failed.", container);
        } else {
            GrowingBuffer buffer = new GrowingBuffer(256);

            try {
                SubPacketType.ContainerData.writeTo(buffer);
                buffer.writeInt(container.windowId);
                writeFieldData(container, fieldName, buffer);
            } catch (IOException var6) {
                throw new RuntimeException(var6);
            }

            buffer.flip();
            if (!this.isClient()) {
                for (final IContainerListener listener : container.getListeners()) {
                    if (listener instanceof EntityPlayerMP) {
                        this.sendPacket(buffer, false, (EntityPlayerMP) listener);
                    }
                }
            } else {
                this.sendPacket(buffer);
            }

        }
    }

    public final void sendContainerEvent(ContainerBase<?> container, String event) {
        GrowingBuffer buffer = new GrowingBuffer(64);
        SubPacketType.ContainerEvent.writeTo(buffer);
        buffer.writeInt(container.windowId);
        buffer.writeString(event);
        buffer.flip();
        if (!this.isClient()) {

            for (final IContainerListener listener : container.getListeners()) {
                if (listener instanceof EntityPlayerMP) {
                    this.sendPacket(buffer, false, (EntityPlayerMP) listener);
                }
            }
        } else {
            this.sendPacket(buffer);
        }

    }

    public void updateColorPicker(PlayerStreakInfo colorPicker, String name) {
    }

    public void updateColorPickerAll() {
        GrowingBuffer buffer = new GrowingBuffer();
        SubPacketType.ColorPickerAllLoggIn.writeTo(buffer);
        NBTTagCompound tagCompound = new NBTTagCompound();
        buffer.writeInt(IUCore.mapStreakInfo.size());
        int i = 0;
        for (Map.Entry<String, PlayerStreakInfo> playerStreakInfoEntry : IUCore.mapStreakInfo.entrySet()) {
            NBTTagCompound tag4 = new NBTTagCompound();
            tag4.setString("nick", playerStreakInfoEntry.getKey());
            tag4.setTag("streak", playerStreakInfoEntry.getValue().writeNBT());
            tagCompound.setTag(String.valueOf(i), tag4);
            i++;

        }
        try {
            DataEncoder.encode(buffer, tagCompound);
            buffer.flip();
            sendPacket(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
