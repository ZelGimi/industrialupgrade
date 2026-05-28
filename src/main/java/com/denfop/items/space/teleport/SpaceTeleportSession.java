package com.denfop.items.space.teleport;

import net.minecraft.nbt.CompoundTag;

public class SpaceTeleportSession {

    public static final String TAG_NAME = "space_teleport_session";

    public String trackedItemUuid = "";
    public String targetBodyName = "";
    public String originDimension = "";
    public double originX;
    public double originY;
    public double originZ;
    public float originYaw;
    public float originPitch;
    public SpaceTeleportPhase phase = SpaceTeleportPhase.INACTIVE;
    public SpaceTeleportReason reason = SpaceTeleportReason.NONE;
    public long phaseEndGameTime = 0L;

    public SpaceTeleportSession() {
    }

    public SpaceTeleportSession(final CompoundTag tag) {
        this.read(tag);
    }

    public boolean isActive() {
        return this.phase != SpaceTeleportPhase.INACTIVE;
    }

    public long getRemainingTicks(final long currentGameTime) {
        return Math.max(0L, this.phaseEndGameTime - currentGameTime);
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putString("trackedItemUuid", this.trackedItemUuid);
        tag.putString("targetBodyName", this.targetBodyName);
        tag.putString("originDimension", this.originDimension);
        tag.putDouble("originX", this.originX);
        tag.putDouble("originY", this.originY);
        tag.putDouble("originZ", this.originZ);
        tag.putFloat("originYaw", this.originYaw);
        tag.putFloat("originPitch", this.originPitch);
        tag.putInt("phase", this.phase.ordinal());
        tag.putInt("reason", this.reason.ordinal());
        tag.putLong("phaseEndGameTime", this.phaseEndGameTime);
        return tag;
    }

    public void read(final CompoundTag tag) {
        this.trackedItemUuid = tag.getString("trackedItemUuid");
        this.targetBodyName = tag.getString("targetBodyName");
        this.originDimension = tag.getString("originDimension");
        this.originX = tag.getDouble("originX");
        this.originY = tag.getDouble("originY");
        this.originZ = tag.getDouble("originZ");
        this.originYaw = tag.getFloat("originYaw");
        this.originPitch = tag.getFloat("originPitch");
        this.phase = SpaceTeleportPhase.byOrdinal(tag.getInt("phase"));
        this.reason = SpaceTeleportReason.byOrdinal(tag.getInt("reason"));
        this.phaseEndGameTime = tag.getLong("phaseEndGameTime");
    }

    public void clear() {
        this.trackedItemUuid = "";
        this.targetBodyName = "";
        this.originDimension = "";
        this.originX = 0D;
        this.originY = 0D;
        this.originZ = 0D;
        this.originYaw = 0F;
        this.originPitch = 0F;
        this.phase = SpaceTeleportPhase.INACTIVE;
        this.reason = SpaceTeleportReason.NONE;
        this.phaseEndGameTime = 0L;
    }
}