package com.denfop.blockentity.transport.types;

import com.denfop.Constants;
import com.denfop.blocks.ISubEnum;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum RadTypes implements ISubEnum, ICableItem {
    rad_cable(0, 0, 0.25F, 0.02D, Integer.MAX_VALUE);

    public static final RadTypes[] values = values();
    private static final Map<String, RadTypes> nameMap = new HashMap<>();

    static {

        for (RadTypes type : values) {
            nameMap.put(type.getName(), type);
        }

    }

    public final int maxInsulation;
    public final int minColoredInsulation;
    public final float thickness;
    public final double loss;
    public final double capacity;
    private final ResourceLocation texture;

    RadTypes(int maxInsulation, int minColoredInsulation, float thickness, double loss, double capacity) {
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

    public static RadTypes get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_radcable";
    }


    public int getId() {
        return this.ordinal();
    }

    @Override
    public String getNameCable() {
        return this.name() + "_radcable";
    }

    @Override
    public String getMainPath() {
        return "radcable";
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
