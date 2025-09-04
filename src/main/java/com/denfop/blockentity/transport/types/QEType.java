package com.denfop.blockentity.transport.types;

import com.denfop.Constants;
import com.denfop.blocks.ISubEnum;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum QEType implements ISubEnum, ICableItem {
    qcable(0, 0, 0.25F, 0.02D, Integer.MAX_VALUE);

    public static final QEType[] values = values();
    private static final Map<String, QEType> nameMap = new HashMap<>();

    static {

        for (QEType type : values) {
            nameMap.put(type.getName(), type);
        }

    }

    public final int maxInsulation;
    public final int minColoredInsulation;
    public final float thickness;
    public final double loss;
    public final double capacity;
    private final ResourceLocation texture;

    QEType(int maxInsulation, int minColoredInsulation, float thickness, double loss, double capacity) {
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

    public static QEType get(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name() + "_qcable";
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
        return this.name() + "_qcable";
    }

    @Override
    public String getMainPath() {
        return "qcable";
    }


    @Override
    public ResourceLocation getRecourse() {
        return texture;
    }
}
