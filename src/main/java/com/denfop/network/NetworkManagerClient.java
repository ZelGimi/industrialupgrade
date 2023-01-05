package com.denfop.network;

import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.research.ResearchSystem;
import com.denfop.api.research.main.BaseLevelSystem;
import com.denfop.api.research.main.EnumLeveling;
import com.mojang.authlib.GameProfile;
import ic2.api.network.INetworkItemEventListener;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.ExplosionIC2;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioPosition;
import ic2.core.audio.PositionSpec;
import ic2.core.block.ITeBlock;
import ic2.core.block.TeBlockRegistry;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.Components;
import ic2.core.block.comp.TileEntityComponent;
import ic2.core.item.IHandHeldInventory;
import ic2.core.item.IHandHeldSubInventory;
import ic2.core.network.GrowingBuffer;
import ic2.core.util.LogCategory;
import ic2.core.util.ParticleUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
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

    private static void processChatPacket(GrowingBuffer buffer) {
        final String messages = buffer.readString();
        IC2.platform.requestTick(false, () -> {
            String[] var1 = messages.split("[\\r\\n]+");

            for (String line : var1) {
                IC2.platform.messagePlayer(null, line);
            }

        });
    }

    private static void processConsolePacket(GrowingBuffer buffer) {
        buffer.readString();
        PrintStream console = new PrintStream(new FileOutputStream(FileDescriptor.out));


        console.flush();
    }

    private void onPacketData(GrowingBuffer is, final EntityPlayer player) throws IOException {
        if (is.hasAvailable()) {
            SubPacketType packetType = SubPacketType.read(is, false);
            if (packetType != null) {
                final Object worldDeferred;
                final double x;
                final double y;
                final double z;
                final int state;
                final int windowId;
                final int currentItemPosition;
                final int dataLen;
                switch (packetType) {
                    case LevelSystem:
                        final String name = is.readString();
                        if (player == null) {
                            break;
                        }
                        if (player.getName().equals(name)) {
                            final NBTTagCompound nbt = (NBTTagCompound) DataEncoder.decode(is);
                            final BaseLevelSystem baselevelsystem = new BaseLevelSystem(player, nbt);
                            ResearchSystem.instance.getMap_level().clear();
                            ResearchSystem.instance.getUUIDs().clear();
                            ResearchSystem.instance.getMap_level().put(player.getName(), baselevelsystem);
                            ResearchSystem.instance.getUUIDs().add(player.getName());

                        }
                        break;
                    case LevelSystemAdd:
                        final String name_player = is.readString();
                        if (player.getName().equals(name_player)) {
                            final int id = is.readInt();
                            final int add = is.readInt();
                            ResearchSystem.instance.getMap_level().get(name_player).addLevel(EnumLeveling.values()[id], add);
                        }
                        break;
                    case Radiation:
                        final String name_player2 = is.readString();
                        if (player != null) {
                            if (player.getName().equals(name_player2)) {
                                RadiationSystem.rad_system.getRadiationList().clear();
                                RadiationSystem.rad_system.getMap().clear();
                                final int size = is.readInt();
                                for (int i = 0; i < size; i++) {
                                    Radiation radiation = (Radiation) DataEncoder.decode(is);
                                    RadiationSystem.rad_system.addRadiation(radiation);
                                }
                            }
                        }
                        break;
                    case RadiationUpdate:
                        Radiation radiation = (Radiation) DataEncoder.decode(is);
                        assert radiation != null;
                        RadiationSystem.rad_system.addRadiation(radiation);
                        break;
                    case LevelSystemRemove:
                        final String name_player1 = is.readString();
                        if (player.getName().equals(name_player1)) {
                            final BaseLevelSystem baselevelsystem = ResearchSystem.instance.getMap_level().get(name_player1);
                            baselevelsystem.setLevel(EnumLeveling.PVP, 0);
                            baselevelsystem.setLevel(EnumLeveling.PRACTICE, 0);
                            baselevelsystem.setLevel(EnumLeveling.THEORY, 0);
                            ResearchSystem.instance.getMap_level().clear();
                            ResearchSystem.instance.getUUIDs().clear();

                        }
                        break;

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
                                case 0:
                                    TeUpdate.receive(decompBuffer);
                                    return;
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
                    case TileEntityEvent:
                        worldDeferred = DataEncoder.decodeDeferred(is, TileEntity.class);
                        currentItemPosition = is.readInt();
                        IC2.platform.requestTick(false, () -> {
                            TileEntity te = DataEncoder.getValue(worldDeferred);
                            if (te instanceof INetworkTileEntityEventListener) {
                                ((INetworkTileEntityEventListener) te).onNetworkEvent(currentItemPosition);
                            }

                        });
                        break;
                    case ItemEvent:
                        final GameProfile profile = DataEncoder.decode(is, GameProfile.class);
                        final ItemStack stack = DataEncoder.decode(is, ItemStack.class);
                        windowId = is.readInt();
                        IC2.platform.requestTick(false, () -> {
                            World world = Minecraft.getMinecraft().world;

                            for (final EntityPlayer obj : world.playerEntities) {
                                if (profile.getId() != null && profile.getId().equals(obj
                                        .getGameProfile()
                                        .getId()) || profile.getId() == null && profile.getName().equals(obj
                                        .getGameProfile()
                                        .getName())) {
                                    if (stack.getItem() instanceof INetworkItemEventListener) {
                                        ((INetworkItemEventListener) stack.getItem()).onNetworkEvent(stack, obj, windowId);
                                    }
                                    break;
                                }
                            }

                        });
                        break;
                    case GuiDisplay:
                        final boolean isAdmin = is.readBoolean();
                        switch (is.readByte()) {
                            case 0:
                                final Object teDeferred = DataEncoder.decodeDeferred(is, TileEntity.class);
                                windowId = is.readInt();
                                IC2.platform.requestTick(false, () -> {
                                    EntityPlayer player1 = IC2.platform.getPlayerInstance();
                                    TileEntity te = DataEncoder.getValue(teDeferred);
                                    if (te instanceof IHasGui) {
                                        IC2.platform.launchGuiClient(player1, (IHasGui) te, isAdmin);
                                        player1.openContainer.windowId = windowId;
                                    } else if (player1 instanceof EntityPlayerSP) {
                                        ((EntityPlayerSP) player1).connection.sendPacket(new CPacketCloseWindow(windowId));
                                    }

                                });
                                return;
                            case 1:
                                currentItemPosition = is.readInt();
                                final boolean subGUI = is.readBoolean();
                                final short ID = subGUI ? is.readShort() : 0;
                                dataLen = is.readInt();
                                IC2.platform.requestTick(false, () -> {
                                    EntityPlayer player12 = IC2.platform.getPlayerInstance();
                                    ItemStack currentItem;
                                    if (currentItemPosition < 0) {
                                        int actualItemPosition = ~currentItemPosition;
                                        if (actualItemPosition > player12.inventory.offHandInventory.size() - 1) {
                                            return;
                                        }

                                        currentItem = player12.inventory.offHandInventory.get(actualItemPosition);
                                    } else {
                                        if (currentItemPosition != player12.inventory.currentItem) {
                                            return;
                                        }

                                        currentItem = player12.inventory.getCurrentItem();
                                    }

                                    if (currentItem.getItem() instanceof IHandHeldInventory) {
                                        if (subGUI && currentItem.getItem() instanceof IHandHeldSubInventory) {
                                            IC2.platform.launchGuiClient(
                                                    player12,
                                                    ((IHandHeldSubInventory) currentItem.getItem()).getSubInventory(
                                                            player12,
                                                            currentItem,
                                                            ID
                                                    ),
                                                    isAdmin
                                            );
                                        } else {
                                            IC2.platform.launchGuiClient(
                                                    player12,
                                                    ((IHandHeldInventory) currentItem.getItem()).getInventory(
                                                            player12,
                                                            currentItem
                                                    ),
                                                    isAdmin
                                            );
                                        }
                                    } else if (player12 instanceof EntityPlayerSP) {
                                        ((EntityPlayerSP) player12).connection.sendPacket(new CPacketCloseWindow(dataLen));
                                    }

                                    player12.openContainer.windowId = dataLen;
                                });
                                return;
                            default:
                                return;
                        }
                    case ExplosionEffect:
                        worldDeferred = DataEncoder.decodeDeferred(is, World.class);
                        final Vec3d pos = DataEncoder.decode(is, Vec3d.class);
                        final ExplosionIC2.Type type = DataEncoder.decodeEnum(is, ExplosionIC2.Type.class);
                        IC2.platform.requestTick(false, () -> {
                            World world = DataEncoder.getValue(worldDeferred);
                            if (world != null) {
                                switch (type) {
                                    case Normal:
                                        world.playSound(
                                                player,
                                                new BlockPos(pos),
                                                SoundEvents.ENTITY_GENERIC_EXPLODE,
                                                SoundCategory.BLOCKS,
                                                4.0F,
                                                (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F
                                        );
                                        world.spawnParticle(
                                                EnumParticleTypes.EXPLOSION_HUGE,
                                                pos.x,
                                                pos.y,
                                                pos.z,
                                                0.0D,
                                                0.0D,
                                                0.0D
                                        );
                                        break;
                                    case Electrical:
                                        IC2.audioManager.playOnce(
                                                new AudioPosition(
                                                        world,
                                                        (float) pos.x,
                                                        (float) pos.y,
                                                        (float) pos.z
                                                ),
                                                PositionSpec.Center,
                                                "Machines/MachineOverload.ogg",
                                                true,
                                                IC2.audioManager.getDefaultVolume()
                                        );
                                        world.spawnParticle(
                                                EnumParticleTypes.EXPLOSION_HUGE,
                                                pos.x,
                                                pos.y,
                                                pos.z,
                                                0.0D,
                                                0.0D,
                                                0.0D
                                        );
                                        break;
                                    case Heat:
                                        world.playSound(
                                                player,
                                                new BlockPos(pos),
                                                SoundEvents.BLOCK_FIRE_EXTINGUISH,
                                                SoundCategory.BLOCKS,
                                                4.0F,
                                                (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F
                                        );
                                        world.spawnParticle(
                                                EnumParticleTypes.EXPLOSION_HUGE,
                                                pos.x,
                                                pos.y,
                                                pos.z,
                                                0.0D,
                                                0.0D,
                                                0.0D
                                        );
                                        break;
                                    case Nuclear:
                                        IC2.audioManager.playOnce(
                                                new AudioPosition(
                                                        world,
                                                        (float) pos.x,
                                                        (float) pos.y,
                                                        (float) pos.z
                                                ),
                                                PositionSpec.Center,
                                                "Tools/NukeExplosion.ogg",
                                                true,
                                                IC2.audioManager.getDefaultVolume()
                                        );
                                        world.spawnParticle(
                                                EnumParticleTypes.EXPLOSION_HUGE,
                                                pos.x,
                                                pos.y,
                                                pos.z,
                                                0.0D,
                                                0.0D,
                                                0.0D
                                        );
                                }
                            }

                        });
                        break;
                    case Rpc:
                        throw new RuntimeException("Received unexpected RPC packet");
                    case TileEntityBlockComponent:
                        state = is.readInt();
                        BlockPos pos1 = DataEncoder.decode(is, BlockPos.class);
                        String componentName = is.readString();
                        final Class<? extends TileEntityComponent> componentCls = Components.getClass(componentName);
                        if (componentCls == null) {
                            throw new IOException("invalid component: " + componentName);
                        }

                        dataLen = is.readVarInt();
                        if (dataLen > 65536) {
                            throw new IOException("data length limit exceeded");
                        }

                        final byte[] data = new byte[dataLen];
                        is.readFully(data);
                        IC2.platform.requestTick(false, () -> {
                            World world = Minecraft.getMinecraft().world;
                            if (world.provider.getDimension() == state) {
                                TileEntity teRaw = world.getTileEntity(pos1);
                                if (teRaw instanceof TileEntityBlock) {
                                    TileEntityComponent component = ((TileEntityBlock) teRaw).getComponent(componentCls);
                                    if (component != null) {
                                        DataInputStream dataIs = new DataInputStream(new ByteArrayInputStream(data));

                                        try {
                                            component.onNetworkUpdate(dataIs);
                                        } catch (IOException var6) {
                                            throw new RuntimeException(var6);
                                        }
                                    }
                                }
                            }
                        });
                        break;
                    case TileEntityBlockLandEffect:
                        worldDeferred = DataEncoder.decodeDeferred(is, World.class);
                        if (is.readBoolean()) {
                            pos1 = (BlockPos) DataEncoder.decode(is, DataEncoder.EncodedType.BlockPos);
                        } else {
                            pos1 = null;
                        }

                        x = is.readDouble();
                        y = is.readDouble();
                        z = is.readDouble();
                        final int count = is.readInt();
                        ITeBlock teBlock = TeBlockRegistry.get(is.readString());
                        IC2.platform.requestTick(false, () -> {
                            World world = DataEncoder.getValue(worldDeferred);
                            if (world != null) {
                                ParticleUtil.spawnBlockLandParticles(world, pos1, x, y, z, count, teBlock);
                            }
                        });
                        break;
                    case TileEntityBlockRunEffect:
                        worldDeferred = DataEncoder.decodeDeferred(is, World.class);
                        if (is.readBoolean()) {
                            pos1 = (BlockPos) DataEncoder.decode(is, DataEncoder.EncodedType.BlockPos);
                        } else {
                            pos1 = null;
                        }

                        x = is.readDouble();
                        y = is.readDouble();
                        z = is.readDouble();
                        final double xSpeed = is.readDouble();
                        final double zSpeed = is.readDouble();
                        teBlock = TeBlockRegistry.get(is.readString());
                        IC2.platform.requestTick(false, new Runnable() {
                            public void run() {
                                World world = DataEncoder.getValue(worldDeferred);
                                if (world != null) {
                                    ParticleUtil.spawnBlockRunParticles(world, pos1, x, y, z, xSpeed, zSpeed, teBlock);
                                }
                            }
                        });
                        break;
                    default:
                        this.onCommonPacketData(packetType, false, is, player);
                }

            }
        }
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

    private void onPacketData(GrowingBuffer is) throws IOException {
        if (is.hasAvailable()) {
            SubPacketType packetType = SubPacketType.read(is, false);
            if (packetType != null) {
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
                                IC2.platform.requestTick(false, () -> {
                                    EntityPlayer player = IC2.platform.getPlayerInstance();
                                    TileEntity te = DataEncoder.getValue(teDeferred);
                                    if (te instanceof IHasGui) {
                                        IC2.platform.launchGuiClient(player, (IHasGui) te, isAdmin);
                                        player.openContainer.windowId = windowId;
                                    } else if (player instanceof EntityPlayerSP) {
                                        ((EntityPlayerSP) player).connection.sendPacket(new CPacketCloseWindow(windowId));
                                    }

                                });
                                return;
                            case 1:
                                currentItemPosition = is.readInt();
                                final boolean subGUI = is.readBoolean();
                                final short ID = subGUI ? is.readShort() : 0;
                                dataLen = is.readInt();
                                IC2.platform.requestTick(false, () -> {
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
                                                    ((IHandHeldSubInventory) currentItem.getItem()).getSubInventory(
                                                            player,
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
                                });
                                return;
                            default:
                        }

                }

            }
        }
    }

    public void updateColorPicker(NBTTagCompound entityData) {
        GrowingBuffer buffer = new GrowingBuffer();
        SubPacketType.ColorPicker.writeTo(buffer);
        Color colorRGB = new Color((int) entityData.getDouble("Red"), (int) entityData.getDouble("Blue"),
                (int) entityData.getDouble("Green")
        );
        if (!entityData.getBoolean("RGB")) {
            buffer.writeInt(colorRGB.getRGB());
        } else {
            buffer.writeInt(-1);
        }
        buffer.flip();
        sendPacket(buffer);
    }

}
