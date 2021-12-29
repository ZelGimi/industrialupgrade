package com.denfop.audio;

public class AudioManager {

    public AudioManager() {
    }

    public void initialize() {
    }

    public void playOnce(Object obj, String soundFile) {
    }

    public String playOnce(Object obj, PositionSpec positionSpec, String soundFile, boolean priorized, float volume) {
        return null;
    }

    public void chainSource(String source, FutureSound onFinish) {
    }

    public void removeSource(String source) {
    }

    public void removeSources(Object obj) {
    }

    public AudioSource createSource(Object obj, String initialSoundFile) {
        return null;
    }

    public AudioSource createSource(Object obj, String initialSoundFile, PositionSpec spec) {
        return null;
    }

    public AudioSource createSource(
            Object obj,
            PositionSpec positionSpec,
            String initialSoundFile,
            boolean loop,
            boolean priorized,
            float volume
    ) {
        return null;
    }

    public void onTick() {
    }

    public float getMasterVolume() {
        return 0.0F;
    }

    public float getDefaultVolume() {
        return 0.0F;
    }

    protected boolean valid() {
        return false;
    }

}
