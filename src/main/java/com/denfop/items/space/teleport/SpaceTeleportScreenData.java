package com.denfop.items.space.teleport;

import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenPlanetaryTranslocator;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class SpaceTeleportScreenData {

    public final List<Entry> entries;
    public final String selectedBodyName;
    public final double currentCharge;
    public final double maxCharge;
    public final boolean sessionActive;
    public final SpaceTeleportPhase phase;
    public final String activeBodyName;
    public final long ticksUntilCritical;
    public final long returnCountdownTicks;
    public final SpaceTeleportReason reason;
    public final boolean itemPresent;
    public SpaceTeleportScreenData(
            final List<Entry> entries,
            final String selectedBodyName,
            final double currentCharge,
            final double maxCharge,
            final boolean sessionActive,
            final SpaceTeleportPhase phase,
            final String activeBodyName,
            final long ticksUntilCritical,
            final long returnCountdownTicks,
            final SpaceTeleportReason reason,
            final boolean itemPresent
    ) {
        this.entries = entries;
        this.selectedBodyName = selectedBodyName;
        this.currentCharge = currentCharge;
        this.maxCharge = maxCharge;
        this.sessionActive = sessionActive;
        this.phase = phase;
        this.activeBodyName = activeBodyName;
        this.ticksUntilCritical = ticksUntilCritical;
        this.returnCountdownTicks = returnCountdownTicks;
        this.reason = reason;
        this.itemPresent = itemPresent;
    }

    public SpaceTeleportScreenData(final CustomPacketBuffer buffer) {
        int size = buffer.readInt();
        this.entries = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.entries.add(new Entry(buffer));
        }
        this.selectedBodyName = buffer.readString();
        this.currentCharge = buffer.readDouble();
        this.maxCharge = buffer.readDouble();
        this.sessionActive = buffer.readBoolean();
        this.phase = SpaceTeleportPhase.byOrdinal(buffer.readInt());
        this.activeBodyName = buffer.readString();
        this.ticksUntilCritical = buffer.readLong();
        this.returnCountdownTicks = buffer.readLong();
        this.reason = SpaceTeleportReason.byOrdinal(buffer.readInt());
        this.itemPresent = buffer.readBoolean();
    }

    public void write(final CustomPacketBuffer buffer) {
        buffer.writeInt(this.entries.size());
        for (Entry entry : this.entries) {
            entry.write(buffer);
        }
        buffer.writeString(this.selectedBodyName == null ? "" : this.selectedBodyName);
        buffer.writeDouble(this.currentCharge);
        buffer.writeDouble(this.maxCharge);
        buffer.writeBoolean(this.sessionActive);
        buffer.writeInt(this.phase.ordinal());
        buffer.writeString(this.activeBodyName == null ? "" : this.activeBodyName);
        buffer.writeLong(this.ticksUntilCritical);
        buffer.writeLong(this.returnCountdownTicks);
        buffer.writeInt(this.reason.ordinal());
        buffer.writeBoolean(this.itemPresent);
    }
    @OnlyIn(Dist.CLIENT)
    public void setScreen() {
        Minecraft.getInstance().setScreen(new ScreenPlanetaryTranslocator(this));
    }

    public static class Entry {
        public final String bodyName;
        public final String displayTitle;
        public final double researchPercent;
        public final boolean dimensionExists;
        public final boolean canTeleport;
        public final boolean selectable;
        public final int indentLevel;
        public final boolean systemHeader;

        public Entry(
                final String bodyName,
                final String displayTitle,
                final double researchPercent,
                final boolean dimensionExists,
                final boolean canTeleport,
                final boolean selectable,
                final int indentLevel,
                final boolean systemHeader
        ) {
            this.bodyName = bodyName;
            this.displayTitle = displayTitle;
            this.researchPercent = researchPercent;
            this.dimensionExists = dimensionExists;
            this.canTeleport = canTeleport;
            this.selectable = selectable;
            this.indentLevel = indentLevel;
            this.systemHeader = systemHeader;
        }

        public Entry(final CustomPacketBuffer buffer) {
            this.bodyName = buffer.readString();
            this.displayTitle = buffer.readString();
            this.researchPercent = buffer.readDouble();
            this.dimensionExists = buffer.readBoolean();
            this.canTeleport = buffer.readBoolean();
            this.selectable = buffer.readBoolean();
            this.indentLevel = buffer.readInt();
            this.systemHeader = buffer.readBoolean();
        }

        public void write(final CustomPacketBuffer buffer) {
            buffer.writeString(this.bodyName == null ? "" : this.bodyName);
            buffer.writeString(this.displayTitle == null ? "" : this.displayTitle);
            buffer.writeDouble(this.researchPercent);
            buffer.writeBoolean(this.dimensionExists);
            buffer.writeBoolean(this.canTeleport);
            buffer.writeBoolean(this.selectable);
            buffer.writeInt(this.indentLevel);
            buffer.writeBoolean(this.systemHeader);
        }

        public boolean isBodyEntry() {
            return !this.systemHeader && this.selectable && this.bodyName != null && !this.bodyName.isEmpty();
        }
    }
}