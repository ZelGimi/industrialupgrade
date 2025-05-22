package com.denfop.api.heat;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class HeatTick<T, E> {

    private final T source;
    LinkedList<IHeatConductor> conductors = new LinkedList<>();
    private List<E> energyPaths;

    public HeatTick(T source, List<E> list) {
        this.source = source;
        this.energyPaths = list;

    }

    public LinkedList<IHeatConductor> getConductors() {
        return conductors;
    }

    public void setConductors(final LinkedList<IHeatConductor> conductors) {
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
        HeatTick that = (HeatTick) o;
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
