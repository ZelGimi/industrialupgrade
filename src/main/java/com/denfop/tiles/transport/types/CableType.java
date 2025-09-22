package com.denfop.tiles.transport.types;

import com.denfop.Constants;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum CableType implements IStringSerializable, ICableItem {
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
    copper(0.25F, 0.015D, 32),
    copper1(0.375F, 0.015D, 128, 1),
    glass_1(0.25F, 0.05D, 2048, 0),
    gold(0.25F, 0.02D, 512),
    gold1(0.375F, 0.02D, 1024, 1),
    iron(0.25F, 0.04D, 256),
    iron1(0.375F, 0.04D, 512, 1),
    tin(0.25F, 0.02D, 32),
    tin1(0.375F, 0.02D, 128, 1),
    ;

    public static final CableType[] values = values();
    private static final Map<String, CableType> nameMap = new HashMap<>();

    static {

        for (CableType type : values) {
            nameMap.put(type.getName(), type);
        }

    }


    public final float thickness;
    public final double loss;
    public final double capacity;
    public final int insulation;

    private final ResourceLocation texture;

    CableType(float thickness, double loss, double capacity) {

        this.thickness = thickness;
        this.loss = loss;
        this.capacity = capacity;
        this.insulation = 0;
        this.texture = new ResourceLocation(
                Constants.MOD_ID,
                "blocks/wiring/" + this.getMainPath() + "/" + this
                        .getNameCable()
        );
    }

    CableType(float thickness, double loss, double capacity, int insulation) {

        this.thickness = thickness;
        this.loss = loss;
        this.capacity = capacity;
        this.insulation = insulation;

        this.texture = new ResourceLocation(
                Constants.MOD_ID,
                "blocks/wiring/" + this.getMainPath() + "/" + this
                        .getNameCable()
        );
    }

    public static CableType get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_cable";
    }

    @Override
    public float getThickness() {
        return thickness;
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
        return "cable";
    }


    @Override
    public ResourceLocation getRecourse() {
        return texture;
    }
}
