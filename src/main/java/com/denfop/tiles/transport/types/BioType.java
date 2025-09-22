package com.denfop.tiles.transport.types;

import com.denfop.Constants;
import com.denfop.blocks.ISubEnum;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum BioType implements ISubEnum, ICableItem {
    bpipe(0, 0, 0.375F, 0.02D, Integer.MAX_VALUE);

    public static final BioType[] values = values();
    private static final Map<String, BioType> nameMap = new HashMap<>();

    static {

        for (BioType type : values) {
            nameMap.put(type.getName(), type);
        }

    }

    public final int maxInsulation;
    public final int minColoredInsulation;
    public final float thickness;
    public final double loss;
    public final double capacity;
    private final ResourceLocation texture;

    BioType(int maxInsulation, int minColoredInsulation, float thickness, double loss, double capacity) {
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

    public static BioType get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_bpipe";
    }


    public int getId() {
        return this.ordinal();
    }

    @Override
    public String getNameCable() {
        return this.name() + "_bpipe";
    }

    @Override
    public String getMainPath() {
        return "bpipe";
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
