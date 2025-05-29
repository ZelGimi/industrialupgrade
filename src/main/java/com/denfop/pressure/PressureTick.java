package com.denfop.pressure;

import com.denfop.api.pressure.IPressureConductor;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class PressureTick<T, E> {

    private final T source;
    LinkedList<IPressureConductor> conductors = new LinkedList<>();
    private List<E> energyPaths;

    public PressureTick(T source, List<E> list) {
        this.source = source;
        this.energyPaths = list;

    }

    public LinkedList<IPressureConductor> getConductors() {
        return conductors;
    }

    public void setConductors(final LinkedList<IPressureConductor> conductors) {
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
        PressureTick that = (PressureTick) o;
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
