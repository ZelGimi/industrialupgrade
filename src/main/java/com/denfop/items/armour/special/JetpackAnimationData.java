package com.denfop.items.armour.special;

final class JetpackAnimationData {

    JetpackVisualState state = JetpackVisualState.IDLE;

    boolean lastActive;
    boolean loopSoundPlaying;

    int startTicks;
    int stateTicks;
    int inactiveTicks;
    int lastSeenTick;

    float thrust;
    float length;
    float width;
    float glow;
    float jitter;

    float flameRate;
    float smokeRate;
    float sparkRate;
    float glowRate;

    float flameAccumulator;
    float smokeAccumulator;
    float sparkAccumulator;
    float glowAccumulator;
}
