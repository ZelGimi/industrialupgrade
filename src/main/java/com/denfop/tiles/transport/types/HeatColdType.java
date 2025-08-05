package com.denfop.tiles.transport.types;


import com.denfop.Constants;
import com.denfop.blocks.ISubEnum;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum HeatColdType implements ISubEnum, ICableItem {
    heatcool(0, 0, 0.25F, 0.02D, 4, 1000),
    heatcool1(0, 0, 0.25F, 0.02D, 8, 2000),
    heatcool2(0, 0, 0.25F, 0.2D, 16, 4000),
    heatcool3(0, 0, 0.25F, 0.2D, 32, 8000),
    heatcool4(0, 0, 0.25F, 0.2D, 64, 16000),

    ;

    public static final HeatColdType[] values = values();
    private static final Map<String, HeatColdType> nameMap = new HashMap<>();

    static {

        for (HeatColdType type : values) {
            nameMap.put(type.getName(), type);
        }

    }

    public final int maxInsulation;
    public final int minColoredInsulation;
    public final float thickness;
    public final double loss;
    public final double capacity;
    public final double capacity1;
    private final ResourceLocation texture;

    HeatColdType(int maxInsulation, int minColoredInsulation, float thickness, double loss, double capacity, double capacity1) {
        this.maxInsulation = maxInsulation;
        this.minColoredInsulation = minColoredInsulation;
        this.thickness = thickness;
        this.loss = loss;
        this.capacity = capacity;
        this.capacity1 = capacity1;
        this.texture = ResourceLocation.tryBuild(
                Constants.MOD_ID,
                "blocks/wiring/" + this.getMainPath() + "/" + this
                        .getNameCable()
        );
    }

    public static HeatColdType get(String name) {
        return nameMap.get(name);
    }

    @Override
    public float getThickness() {
        return thickness;
    }

    public String getName() {
        return this.name() + "_pipes";
    }


    public int getId() {
        return this.ordinal();
    }

    @Override
    public String getNameCable() {
        return this.name() + "_pipes";
    }

    @Override
    public String getMainPath() {
        return "heatcold";
    }


    @Override
    public ResourceLocation getRecourse() {
        return texture;
    }
}
