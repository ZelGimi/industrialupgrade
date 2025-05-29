package com.denfop;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ConfigReader {
    private final List<ForgeConfigSpec.ConfigValue> configValues;
    private final int maxIndex;
    private int index;

    public ConfigReader(List<ForgeConfigSpec.ConfigValue> configValues) {
        this.configValues = configValues;
        this.maxIndex = configValues.size();
        this.index = 0;
    }

    public Object next() {

        Object o = configValues.get(index).get();
        index++;
        return o;
    }

    public Object get(int index) {
        Object o = configValues.get(index).get();
        return o;
    }
}
