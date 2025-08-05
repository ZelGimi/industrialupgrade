package com.denfop.tiles.transport.types;

import com.denfop.Constants;
import com.denfop.blocks.ISubEnum;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum NightType implements ISubEnum, ICableItem {
    npipe(0, 0, 0.25F, 0.02D, Integer.MAX_VALUE);

    public static final NightType[] values = values();
    private static final Map<String, NightType> nameMap = new HashMap<>();

    static {

        for (NightType type : values) {
            nameMap.put(type.getName(), type);
        }

    }

    public final int maxInsulation;
    public final int minColoredInsulation;
    public final float thickness;
    public final double loss;
    public final double capacity;
    private final ResourceLocation texture;

    NightType(int maxInsulation, int minColoredInsulation, float thickness, double loss, double capacity) {
        this.maxInsulation = maxInsulation;
        this.minColoredInsulation = minColoredInsulation;
        this.thickness = thickness;
        this.loss = loss;
        this.capacity = capacity;
        this.texture = ResourceLocation.tryBuild(
                Constants.MOD_ID,
                "blocks/wiring/" + this.getMainPath() + "/" + this
                        .getNameCable()
        );
    }

    public static NightType get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_npipe";
    }


    public int getId() {
        return this.ordinal();
    }

    @Override
    public String getNameCable() {
        return this.name() + "_npipe";
    }

    @Override
    public String getMainPath() {
        return "ncable";
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
