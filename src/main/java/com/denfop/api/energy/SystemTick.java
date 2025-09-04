package com.denfop.api.energy;

import com.denfop.api.otherenergies.common.IConductor;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SystemTick<T, E> {

    private final T source;
    LinkedList<IConductor> conductors = new LinkedList<>();
    private List<E> energyPaths;

    public SystemTick(T source, List<E> list) {
        this.source = source;
        this.energyPaths = list;

    }

    public LinkedList<IConductor> getConductors() {
        return conductors;
    }

    public void setConductors(final LinkedList<IConductor> conductors) {
        this.conductors = conductors;
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
