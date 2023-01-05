package com.denfop.blocks;

import com.denfop.Constants;
import ic2.core.block.state.IIdProvider;
import ic2.core.profile.NotClassic;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

@NotClassic
public enum FluidName implements IIdProvider {

    fluidNeutron,
    fluidHelium,
    fluidbenz,
    fluiddizel,
    fluidneft,
    fluidpolyeth,
    fluidpolyprop,
    fluidoxy,
    fluidhyd,
    fluidazot,
    fluidco2;

    public static final FluidName[] values = values();
    private final boolean hasFlowTexture;
    private Fluid instance;

    FluidName() {
        this(true);
    }

    FluidName(boolean hasFlowTexture) {
        this.hasFlowTexture = hasFlowTexture;
    }

    public String getName() {
        return "iu" + this.name();
    }

    public int getId() {
        throw new UnsupportedOperationException();
    }

    public ResourceLocation getTextureLocation(boolean flowing) {
        if (this.name().startsWith("molten_")) {
            return new ResourceLocation(Constants.MOD_ID, "blocks/fluid/molten_metal");
        } else {
            String type = flowing && this.hasFlowTexture ? "flow" : "still";
            return new ResourceLocation(Constants.MOD_ID, "blocks/fluid/" + this.name().substring(5) + "_" + type);
        }
    }

    public boolean hasInstance() {
        return this.instance != null;
    }

    public Fluid getInstance() {
        if (this.instance == null) {
            throw new IllegalStateException("the requested fluid instance for " + this.name() + " isn't set (yet)");
        } else {
            return this.instance;
        }
    }

    public void setInstance(Fluid fluid) {
        if (fluid == null) {
            throw new NullPointerException("null fluid");
        } else if (this.instance != null) {
            throw new IllegalStateException("conflicting instance");
        } else {
            this.instance = fluid;
        }
    }
}
