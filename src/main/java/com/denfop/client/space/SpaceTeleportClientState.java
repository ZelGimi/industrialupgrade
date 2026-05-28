package com.denfop.client.space;


import com.denfop.items.space.teleport.SpaceTeleportFxType;
import com.denfop.items.space.teleport.SpaceTeleportPhase;
import com.denfop.items.space.teleport.SpaceTeleportReason;
import net.minecraft.util.Mth;

public final class SpaceTeleportClientState {

    public static final SpaceTeleportClientState INSTANCE = new SpaceTeleportClientState();

    private boolean active;
    private SpaceTeleportPhase phase = SpaceTeleportPhase.INACTIVE;
    private String bodyName = "";
    private long ticksUntilCritical;
    private long countdownTicks;
    private double charge;
    private double maxCharge;
    private SpaceTeleportReason reason = SpaceTeleportReason.NONE;
    private boolean itemPresent;
    private long syncReceivedAtMs;

    private SpaceTeleportFxType fxType = SpaceTeleportFxType.NONE;
    private int fxDurationTicks = 0;
    private long fxStartedAtMs = 0L;
    private String fxBodyName = "";
    private boolean fxReverse;

    private SpaceTeleportClientState() {
    }

    public void applySync(
            final boolean active,
            final SpaceTeleportPhase phase,
            final String bodyName,
            final long ticksUntilCritical,
            final long countdownTicks,
            final double charge,
            final double maxCharge,
            final SpaceTeleportReason reason,
            final boolean itemPresent
    ) {
        this.active = active;
        this.phase = phase;
        this.bodyName = bodyName == null ? "" : bodyName;
        this.ticksUntilCritical = ticksUntilCritical;
        this.countdownTicks = countdownTicks;
        this.charge = charge;
        this.maxCharge = maxCharge;
        this.reason = reason;
        this.itemPresent = itemPresent;
        this.syncReceivedAtMs = System.currentTimeMillis();

        if (!active && fxType == SpaceTeleportFxType.NONE) {
            this.phase = SpaceTeleportPhase.INACTIVE;
            this.reason = SpaceTeleportReason.NONE;
            this.bodyName = "";
        }
    }

    public void startFx(final SpaceTeleportFxType type, final int durationTicks, final String bodyName, final boolean reverse) {
        this.fxType = type;
        this.fxDurationTicks = Math.max(1, durationTicks);
        this.fxStartedAtMs = System.currentTimeMillis();
        this.fxBodyName = bodyName == null ? "" : bodyName;
        this.fxReverse = reverse;
    }

    public boolean isActive() {
        return active;
    }

    public SpaceTeleportPhase getPhase() {
        return phase;
    }

    public String getBodyName() {
        return bodyName;
    }

    public boolean isItemPresent() {
        return itemPresent;
    }

    public double getCharge() {
        return charge;
    }

    public double getMaxCharge() {
        return maxCharge;
    }

    public SpaceTeleportReason getReason() {
        return reason;
    }

    public long getTicksUntilCritical() {
        if (!active) {
            return 0L;
        }
        long elapsedTicks = Math.max(0L, (System.currentTimeMillis() - syncReceivedAtMs) / 50L);
        return Math.max(0L, ticksUntilCritical - elapsedTicks);
    }

    public long getCountdownTicks() {
        if (!active || phase != SpaceTeleportPhase.RETURN_PREP) {
            return 0L;
        }
        long elapsedTicks = Math.max(0L, (System.currentTimeMillis() - syncReceivedAtMs) / 50L);
        return Math.max(0L, countdownTicks - elapsedTicks);
    }

    public boolean hasFx() {
        if (fxType == SpaceTeleportFxType.NONE) {
            return false;
        }
        if (getFxProgress() >= 1F) {
            fxType = SpaceTeleportFxType.NONE;
            fxDurationTicks = 0;
            fxStartedAtMs = 0L;
            fxBodyName = "";
            fxReverse = false;
            return false;
        }
        return true;
    }

    public float getFxProgress() {
        if (fxType == SpaceTeleportFxType.NONE || fxDurationTicks <= 0) {
            return 1F;
        }
        float totalMs = fxDurationTicks * 50F;
        float elapsed = System.currentTimeMillis() - fxStartedAtMs;
        return Mth.clamp(elapsed / totalMs, 0F, 1F);
    }

    public SpaceTeleportFxType getFxType() {
        return fxType;
    }

    public boolean isFxReverse() {
        return fxReverse;
    }

    public String getFxBodyName() {
        return fxBodyName;
    }
}