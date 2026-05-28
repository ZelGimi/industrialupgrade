package com.denfop.api.multiblock.preview;

import net.minecraft.util.Mth;

public class PreviewAnimation {

    private final int durationTicks;
    private int tick;

    public PreviewAnimation(int durationTicks) {
        this.durationTicks = Math.max(1, durationTicks);
        this.tick = 0;
    }

    public void reset() {
        this.tick = 0;
    }

    public void tick() {
        if (this.tick < this.durationTicks) {
            this.tick++;
        }
    }

    public float normalized() {
        return Mth.clamp((float) this.tick / (float) this.durationTicks, 0.0F, 1.0F);
    }

    public float easeOutCubic() {
        float t = this.normalized();
        float inv = 1.0F - t;
        return 1.0F - inv * inv * inv;
    }

    public float getDropOffsetBlocks(float maxDropBlocks) {
        return (1.0F - easeOutCubic()) * maxDropBlocks;
    }

    public boolean finished() {
        return this.tick >= this.durationTicks;
    }
}