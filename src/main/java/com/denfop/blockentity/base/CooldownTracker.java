package com.denfop.blockentity.base;

import com.denfop.config.ModConfig;

public class CooldownTracker {

    int tick;

    public CooldownTracker() {
        this.tick = 0;
    }

    public void removeTick() {
        if (this.tick > 0) {
            this.tick -= 1;
        }
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {

        this.tick = tick;
        if (!ModConfig.COMMON.cooldownEnabled.get()) {
            this.tick = 0;
        }
    }

}
