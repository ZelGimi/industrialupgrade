package com.denfop.tiles.transport.types;

import com.denfop.Constants;
import com.denfop.blocks.ISubEnum;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum CoolType implements ISubEnum, ICableItem {
    cool(0, 0, 0.25F, 0.02D, 4),
    cool1(0, 0, 0.25F, 0.02D, 8),
    cool2(0, 0, 0.25F, 0.2D, 16),
    cool3(0, 0, 0.25F, 0.2D, 32),
    cool4(0, 0, 0.25F, 0.2D, 64),

    ;

    public static final CoolType[] values = values();
    private static final Map<String, CoolType> nameMap = new HashMap<>();

    static {

        for (CoolType type : values) {
            nameMap.put(type.getName(), type);
        }

    }

    public final int maxInsulation;
    public final int minColoredInsulation;
    public final float thickness;
    public final double loss;
    public final double capacity;
    private final ResourceLocation texture;

    CoolType(int maxInsulation, int minColoredInsulation, float thickness, double loss, double capacity) {
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

    public static CoolType get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_pipes";
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
        return this.name() + "_pipes";
    }

    @Override
    public String getMainPath() {
        return "cool";
    }


    @Override
    public ResourceLocation getRecourse() {
        return texture;
    }
}
