package com.denfop.render.tank;


import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.material.Fluid;

public class DataFluid {

    BakedModel state;
    private Fluid fluid;

    public DataFluid(Fluid fluid) {
        this.fluid = fluid;
    }

    public BakedModel getState() {
        return state;
    }

    public void setState(final BakedModel state) {
        this.state = state;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public void setFluid(final Fluid fluid) {
        this.fluid = fluid;
    }

}
