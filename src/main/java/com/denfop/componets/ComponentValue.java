package com.denfop.componets;

import java.util.function.Supplier;

public class ComponentValue<T> {
    Supplier<T> value;
    public ComponentValue(){

    }

    public ComponentValue<T> setValue(final Supplier<T> value) {
        this.value = value;
        return this;
    }

    public T getValue() {
        return value.get();
    }

}
