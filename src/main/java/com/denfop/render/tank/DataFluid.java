package com.denfop.render.tank;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.fluids.Fluid;

public class DataFluid {

    IBakedModel state;
    private Fluid fluid;

    public DataFluid(Fluid fluid) {
        this.fluid = fluid;
    }

    public IBakedModel getState() {
        return state;
    }

    public void setState(final IBakedModel state) {
        this.state = state;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public void setFluid(final Fluid fluid) {
        this.fluid = fluid;
    }

}
