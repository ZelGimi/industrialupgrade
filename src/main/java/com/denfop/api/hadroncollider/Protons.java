package com.denfop.api.hadroncollider;

public class Protons implements IProtons {

    private final TypeProtons type;
    private final int tick;
    private int y;
    private int x;
    private int z;
    private int percent;

    public Protons(TypeProtons type) {
        this.percent = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.type = type;
        this.tick = 0;
    }

    public Protons(TypeProtons type, boolean new_) {
        this.percent = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.type = TypeProtons.values()[type.ordinal() % TypeProtons.values().length];
        this.tick = 0;
    }

    public void addCoodination(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void addPercent(int procent) {

        this.percent += procent;
        this.percent = Math.min(this.percent, 100);
    }

    @Override
    public TypeProtons getType() {
        return this.type;
    }

    @Override
    public int getTick() {
        return this.tick;
    }

    @Override
    public int setTick(int tick) {
        return this.tick + tick;
    }

    @Override
    public int getPercent() {
        return this.percent;
    }

}
