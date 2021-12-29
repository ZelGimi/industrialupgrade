//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.audio;

public class FutureSound {

    private boolean run;
    private boolean cancelled;
    private final Runnable onFinish;

    public FutureSound(Runnable onFinish) {
        this.onFinish = onFinish;
    }

    public void cancel() {
        if (this.run) {
            throw new IllegalStateException("Tried to cancel completed sound");
        } else {
            this.cancelled = true;
        }
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    void onFinish() {
        if (this.run) {
            throw new IllegalStateException("Tried to run completed sound");
        } else if (!this.cancelled) {
            this.run = true;
            this.onFinish.run();
        }
    }

    public boolean isComplete() {
        return this.run;
    }

}
