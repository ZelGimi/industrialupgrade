package com.denfop.network;

import ic2.core.IC2;
import ic2.core.network.GrowingBuffer;
import ic2.core.util.LogCategory;

public enum SubPacketType {
    TileEntityEvent(true, true),
    ItemEvent(true, true),
    PlayerItemData(true, true),
    ContainerData(true, true),
    ContainerEvent(true, true),
    HandHeldInvData(true, true),
    LargePacket(true, false),
    GuiDisplay(true, false),
    ExplosionEffect(true, false),
    TileEntityBlockComponent(true, false),
    TileEntityBlockLandEffect(true, false),
    TileEntityBlockRunEffect(true, false),
    KeyUpdate(false, true),
    TileEntityData(false, true),
    RequestGUI(false, true),
    ColorPicker(false, true),
    ColorPickerAllLoggIn(true, false),
    LevelSystem(true, false),
    LevelSystemAdd(true, false),
    LevelSystemRemove(true, false),
    Radiation(true, false),
    RadiationUpdate(true, false),
    ;

    private static final SubPacketType[] values = values();

    static {
        if (values.length > 255) {
            throw new RuntimeException("too many sub packet types");
        }
    }

    private final boolean serverToClient;
    private final boolean clientToServer;

    SubPacketType(boolean serverToClient, boolean clientToServer) {
        this.serverToClient = serverToClient;
        this.clientToServer = clientToServer;
    }

    public static SubPacketType read(GrowingBuffer in, boolean simulating) {
        int id = in.readUnsignedByte() - 1;
        if (id >= 0 && id < values.length) {
            SubPacketType ret = values[id];
            if ((!simulating || ret.clientToServer) && (simulating || ret.serverToClient)) {
                return ret;
            } else {
                IC2.log.warn(
                        LogCategory.Network,
                        "Invalid sub packet type %s for side %s",
                        ret.name(), simulating ? "server" : "client"
                );
                return null;
            }
        } else {
            IC2.log.warn(LogCategory.Network, "Invalid sub packet type: %d", id);
            return null;
        }
    }

    public void writeTo(GrowingBuffer out) {
        out.writeByte(this.getId());
    }

    public int getId() {
        return this.ordinal() + 1;
    }


}
