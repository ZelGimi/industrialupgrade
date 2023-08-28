package com.denfop.api.energy;

import java.util.List;
import java.util.Objects;

public class SystemTick<T, E> {

    private final T source;
    private List<E> energyPaths;

    public SystemTick(T source, List<E> list) {
        this.source = source;
        this.energyPaths = list;

    }

    public T getSource() {
        return source;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemTick that = (SystemTick) o;
        return source == that.source;
    }


    @Override
    public int hashCode() {
        return Objects.hash(source);
    }

    public List<E> getList() {
        return energyPaths;
    }

    public void setList(final List<E> energyPaths) {
        this.energyPaths = energyPaths;
    }


}
