package com.denfop.tiles.base;

public class CooldownTracker {

    int tick;

    public CooldownTracker() {
        this.tick = 0;
    }

    public void setTick(int tick) {

        this.tick = tick;

    }

    public void removeTick() {
        if (this.tick > 0) {
            this.tick -= 1;
        }
    }

    public int getTick() {
        return tick;
    }

}
