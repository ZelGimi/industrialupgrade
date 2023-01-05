package com.denfop.network;

import ic2.api.network.INetworkUpdateListener;
import ic2.core.IC2;
import ic2.core.block.TeBlockRegistry;
import ic2.core.block.TileEntityBlock;
import ic2.core.network.GrowingBuffer;
import ic2.core.util.LogCategory;
import ic2.core.util.ReflectionUtil;
import ic2.core.util.Util;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class TeUpdate {

    static final boolean debug = System.getProperty("ic2.network.debug.teupdate") != null;

    TeUpdate() {
    }

    public static void send(WorldData worldData, NetworkManager network) throws IOException {
        if (!worldData.tesToUpdate.isEmpty()) {
            Map<EntityPlayerMP, GrowingBuffer> buffers = new IdentityHashMap<>();
            List<EntityPlayerMP> playersInRange = new ArrayList<>();
            GrowingBuffer commonBuffer = new GrowingBuffer();
            Iterator var5 = worldData.tesToUpdate.entrySet().iterator();

            label69:
            while (true) {
                Entry entry;
                TileEntity te;
                do {
                    if (!var5.hasNext()) {
                        worldData.tesToUpdate.clear();
                        var5 = buffers.entrySet().iterator();

                        while (var5.hasNext()) {
                            entry = (Entry) var5.next();
                            EntityPlayerMP player = (EntityPlayerMP) entry.getKey();
                            GrowingBuffer playerBuffer = (GrowingBuffer) entry.getValue();
                            playerBuffer.flip();
                            network.sendLargePacket(player, 0, playerBuffer);
                        }

                        return;
                    }

                    entry = (Entry) var5.next();
                    te = (TileEntity) entry.getKey();
                    NetworkManager.getPlayersInRange(te.getWorld(), te.getPos(), playersInRange);
                } while (playersInRange.isEmpty());

                TeUpdateDataServer updateData = (TeUpdateDataServer) entry.getValue();
                DataEncoder.encode(commonBuffer, te.getPos(), false);
                commonBuffer.mark();
                commonBuffer.writeShort(0);
                Iterator var9 = updateData.getGlobalFields().iterator();

                while (var9.hasNext()) {
                    String field = (String) var9.next();
                    NetworkManager.writeFieldData(te, field, commonBuffer);
                }

                commonBuffer.flip();
                var9 = playersInRange.iterator();

                while (true) {
                    Collection playerFields;
                    int fieldCount;
                    EntityPlayerMP player;
                    do {
                        if (!var9.hasNext()) {
                            commonBuffer.clear();
                            playersInRange.clear();
                            continue label69;
                        }

                        player = (EntityPlayerMP) var9.next();
                        playerFields = updateData.getPlayerFields(player);
                        fieldCount = updateData.getGlobalFields().size() + playerFields.size();
                    } while (fieldCount == 0);

                    if (fieldCount > 65535) {
                        throw new RuntimeException("too many fields for " + te + ": " + fieldCount);
                    }

                    commonBuffer.reset();
                    commonBuffer.writeShort(fieldCount);
                    commonBuffer.rewind();
                    GrowingBuffer playerBuffer = buffers.get(player);
                    if (playerBuffer == null) {
                        playerBuffer = new GrowingBuffer(0);
                        buffers.put(player, playerBuffer);
                        playerBuffer.writeInt(player.getEntityWorld().provider.getDimension());
                    }

                    commonBuffer.writeTo(playerBuffer);
                    commonBuffer.rewind();

                    for (final Object playerField : playerFields) {
                        String field = (String) playerField;
                        NetworkManager.writeFieldData(te, field, playerBuffer);
                    }
                }
            }
        }
    }

    static void receive(GrowingBuffer buffer) throws IOException {
        final int dimensionId = buffer.readInt();
        final TeUpdateDataClient updateData = new TeUpdateDataClient();

        while (true) {
            TeUpdateDataClient.TeData teData;
            do {
                if (!buffer.hasAvailable()) {
                    if (debug) {
                        printDebugOutput(dimensionId, updateData);
                    }

                    IC2.platform.requestTick(false, () -> {
                        World world = IC2.platform.getPlayerWorld();
                        if (world != null && world.provider.getDimension() == dimensionId) {

                            for (final TeUpdateDataClient.TeData update : updateData.getTes()) {
                                try {
                                    TeUpdate.apply(update, world);
                                } catch (Throwable var5) {
                                    IC2.log.warn(
                                            LogCategory.Network,
                                            var5,
                                            "TE update at %s failed.",
                                            Util.formatPosition(world, update.pos)
                                    );
                                }
                            }

                        }
                    });
                    return;
                }

                BlockPos pos = DataEncoder.decode(buffer, BlockPos.class);
                int fieldCount = buffer.readUnsignedShort();
                teData = updateData.addTe(pos, fieldCount);

                for (int i = 0; i < fieldCount; ++i) {
                    String fieldName = buffer.readString();
                    Object value = DataEncoder.decode(buffer);
                    if (fieldName.equals("teBlk")) {
                        String name = (String) value;
                        if (name.startsWith("Old-")) {
                            teData.teClass = TeBlockRegistry.getOld(name);
                        } else {
                            teData.teClass = TeBlockRegistry.get(name).getTeClass();
                        }
                    } else {
                        teData.addField(fieldName, value);
                    }
                }
            } while (teData.teClass == null);

            TeUpdateDataClient.FieldData fieldData;
            for (Iterator var10 = teData
                    .getFields()
                    .iterator(); var10.hasNext(); fieldData.field = ReflectionUtil.getFieldRecursive(
                    teData.teClass,
                    fieldData.name
            )) {
                fieldData = (TeUpdateDataClient.FieldData) var10.next();
            }
        }
    }

    private static void printDebugOutput(int dimensionId, TeUpdateDataClient data) {
        StringBuilder out = new StringBuilder();
        out.append("dimension: ");
        out.append(dimensionId);
        out.append(", ");
        out.append(data.getTes().size());
        out.append(" tes:\n");

        for (final TeUpdateDataClient.TeData te : data.getTes()) {
            out.append("  pos: ");
            out.append(te.pos.getX());
            out.append('/');
            out.append(te.pos.getY());
            out.append('/');
            out.append(te.pos.getZ());
            out.append(", ");
            out.append(te.getFields().size());
            out.append(" fields:\n");

            for (Iterator var5 = te.getFields().iterator(); var5.hasNext(); out.append('\n')) {
                TeUpdateDataClient.FieldData field = (TeUpdateDataClient.FieldData) var5.next();
                out.append("    ");
                out.append(field.name);
                out.append(" = ");
                out.append(field.value);
                if (field.value != null) {
                    out.append(" (");
                    out.append(field.value.getClass().getSimpleName());
                    out.append(')');
                }
            }

            if (te.teClass != null) {
                out.append("    TE Class: ");
                out.append(te.teClass.getName());
                out.append('\n');
            } else {
                out.append("    no TE Class\n");
            }
        }

        out.setLength(out.length() - 1);
        IC2.log.info(LogCategory.Network, "Received TE Update:\n" + out);
    }

    private static void apply(TeUpdateDataClient.TeData update, World world) {
        if (!world.isBlockLoaded(update.pos, false)) {
            if (debug) {
                IC2.log.info(LogCategory.Network, "Skipping update at %s, chunk not loaded.",
                        Util.formatPosition(world, update.pos)
                );
            }

        } else {
            TileEntity te = world.getTileEntity(update.pos);
            if (update.teClass != null && (te == null || te.getClass() != update.teClass || te.isInvalid() || te.getWorld() != world)) {
                if (debug) {
                    IC2.log.info(LogCategory.Network, "Instantiating %s with %s.",
                            Util.formatPosition(world, update.pos),
                            update.teClass.getName()
                    );
                }

                te = TileEntityBlock.instantiate(update.teClass);
                world.setTileEntity(update.pos, te);

                assert !te.isInvalid();

                assert te.getWorld() == world;
            } else {
                if (te == null) {
                    if (debug) {
                        IC2.log.info(LogCategory.Network, "Can't apply update at %s, no te and no teClass.",
                                Util.formatPosition(world, update.pos)
                        );
                    }

                    return;
                }

                if (te.isInvalid() || te.getWorld() != world) {
                    if (debug) {
                        IC2.log.warn(LogCategory.Network, "Can't apply update at %s, invalid te and no teClass.",
                                Util.formatPosition(world, update.pos)
                        );
                    }

                    return;
                }

                if (debug) {
                    IC2.log.info(LogCategory.Network, "TE class at %s unchanged.", Util.formatPosition(world, update.pos));
                }
            }

            for (final TeUpdateDataClient.FieldData fieldUpdate : update.getFields()) {
                Object value = DataEncoder.getValue(fieldUpdate.value);
                if (fieldUpdate.field != null) {
                    ReflectionUtil.setValue(te, fieldUpdate.field, value);
                } else {
                    ReflectionUtil.setValueRecursive(te, fieldUpdate.name, value);
                }

                if (te instanceof INetworkUpdateListener) {
                    ((INetworkUpdateListener) te).onNetworkUpdate(fieldUpdate.name);
                }
            }

        }
    }

}
