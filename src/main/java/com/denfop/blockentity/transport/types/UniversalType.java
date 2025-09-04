package com.denfop.blockentity.transport.types;

import com.denfop.Constants;
import com.denfop.blocks.ISubEnum;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum UniversalType implements ISubEnum, ICableItem {
    glass(0.25F, 0.06D, 4096),
    glass1(0.25F, 0.07D, 8192),
    glass2(0.25F, 0.08D, 32768),
    glass3(0.25F, 0.09D, 131072),
    glass4(0.25F, 0.1D, 524288),
    glass5(0.25F, 0.11D, 2097152),
    glass6(0.25F, 0.12D, 8388608),
    glass7(0.25F, 0.15D, 33554432),
    glass8(0.25F, 0.18D, 134217728),
    glass9(0.25F, 0.2D, 536870912),
    glass10(0.25F, 0.25D, 8589934590D),

    ;

    public static final UniversalType[] values = values();
    private static final Map<String, UniversalType> nameMap = new HashMap<>();

    static {

        for (UniversalType type : values) {
            nameMap.put(type.getName(), type);
        }

    }


    public final float thickness;
    public final double loss;
    public final double capacity;
    private final ResourceLocation texture;

    UniversalType(float thickness, double loss, double capacity) {

        this.thickness = thickness;
        this.loss = loss;
        this.capacity = capacity;
        this.texture = ResourceLocation.tryBuild(
                Constants.MOD_ID,
                "blocks/wiring/" + this.getMainPath() + "/" + this
                        .getNameCable()
        );
    }

    public static UniversalType get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_cable";
    }


    public int getId() {
        return this.ordinal();
    }

    @Override
    public String getNameCable() {
        return this.name() + "_cable";
    }

    @Override
    public String getMainPath() {
        return "universal_cable";
    }

    @Override
    public float getThickness() {
        return thickness;
    }

    @Override
    public ResourceLocation getRecourse() {
        return texture;
    }
}
