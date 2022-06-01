package com.denfop.audio;

public class FutureSound {

    private final Runnable onFinish;
    private boolean run;
    private boolean cancelled;

    public FutureSound(Runnable onFinish) {
        this.onFinish = onFinish;
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
