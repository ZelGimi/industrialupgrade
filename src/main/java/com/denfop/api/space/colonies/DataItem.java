package com.denfop.api.space.colonies;

public class DataItem<T> {

    private final short level;
    private final T element;

    public DataItem(short level, T element) {
        this.level = level;
        this.element = element;
    }

    public T getElement() {
        return element;
    }

    public short getLevel() {
        return level;
    }

}
