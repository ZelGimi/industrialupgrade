package com.denfop.blocks.state;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedProperty<T> implements IUnlistedProperty<T> {

    private final String name;
    private final Class<T> cls;

    public UnlistedProperty(String name, Class<T> cls) {
        this.name = name;
        this.cls = cls;
    }

    public String getName() {
        return this.name;
    }

    public boolean isValid(T value) {
        return value == null || this.cls.isInstance(value);
    }

    public Class<T> getType() {
        return this.cls;
    }

    public String valueToString(T value) {
        return value.toString();
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{name=" + this.name + ", cls=" + this.cls.getName() + "}";
    }

}
