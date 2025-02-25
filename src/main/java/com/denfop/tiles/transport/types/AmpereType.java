package com.denfop.tiles.transport.types;

import com.denfop.Constants;
import com.denfop.blocks.ISubEnum;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum AmpereType implements ISubEnum, ICableItem {
    acable(0, 0, 0.25F, 0.02D, Integer.MAX_VALUE);

    public static final AmpereType[] values = values();
    private static final Map<String, AmpereType> nameMap = new HashMap<>();

    static {

        for (AmpereType type : values) {
            nameMap.put(type.getName(), type);
        }

    }

    public final int maxInsulation;
    public final int minColoredInsulation;
    public final float thickness;
    public final double loss;
    public final double capacity;
    private final ResourceLocation texture;

    AmpereType(int maxInsulation, int minColoredInsulation, float thickness, double loss, double capacity) {
        this.maxInsulation = maxInsulation;
        this.minColoredInsulation = minColoredInsulation;
        this.thickness = thickness;
        this.loss = loss;
        this.capacity = capacity;
        this.texture = new ResourceLocation(
                Constants.MOD_ID,
                "blocks/wiring/" + this.getMainPath() + "/" + this
                        .getNameCable()
        );
    }

    public static AmpereType get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_acable";
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
        return this.name() + "_acable";
    }

    @Override
    public String getMainPath() {
        return "acable";
    }


    @Override
    public ResourceLocation getRecourse() {
        return texture;
    }
}
