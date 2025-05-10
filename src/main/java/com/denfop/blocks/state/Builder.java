package com.denfop.blocks.state;

import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;

public class Builder {
    private final MaterialColor color;
    private PushReaction pushReaction = PushReaction.NORMAL;
    private boolean blocksMotion = true;
    private boolean flammable;
    private boolean liquid;
    private boolean replaceable;
    private boolean solid = true;
    private boolean solidBlocking = true;

    public Builder(MaterialColor p_76349_) {
        this.color = p_76349_;
    }

    public Builder liquid() {
        this.liquid = true;
        return this;
    }

    public Builder nonSolid() {
        this.solid = false;
        return this;
    }

    public Builder noCollider() {
        this.blocksMotion = false;
        return this;
    }

    public Builder notSolidBlocking() {
        this.solidBlocking = false;
        return this;
    }

    public Builder flammable() {
        this.flammable = true;
        return this;
    }

    public Builder replaceable() {
        this.replaceable = true;
        return this;
    }

    public Builder destroyOnPush() {
        this.pushReaction = PushReaction.DESTROY;
        return this;
    }

    public Builder notPushable() {
        this.pushReaction = PushReaction.BLOCK;
        return this;
    }

    public Material build() {
        return new Material(this.color, this.liquid, this.solid, this.blocksMotion, this.solidBlocking, this.flammable, this.replaceable, this.pushReaction);
    }
}