package com.denfop.blockentity.transport.types;

import com.denfop.Constants;
import com.denfop.blocks.ISubEnum;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum SteamType implements ISubEnum, ICableItem {
    spipe(0, 0, 0.375F, 0.02D, Integer.MAX_VALUE);

    public static final SteamType[] values = values();
    private static final Map<String, SteamType> nameMap = new HashMap<>();

    static {

        for (SteamType type : values) {
            nameMap.put(type.getName(), type);
        }

    }

    public final int maxInsulation;
    public final int minColoredInsulation;
    public final float thickness;
    public final double loss;
    public final double capacity;
    private final ResourceLocation texture;

    SteamType(int maxInsulation, int minColoredInsulation, float thickness, double loss, double capacity) {
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

    public static SteamType get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_spipe";
    }


    public int getId() {
        return this.ordinal();
    }

    @Override
    public String getNameCable() {
        return this.name() + "_spipe";
    }

    @Override
    public String getMainPath() {
        return "spipe";
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
