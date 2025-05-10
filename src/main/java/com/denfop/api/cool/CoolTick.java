package com.denfop.api.cool;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CoolTick<T, E> {

    private final T source;
    LinkedList<ICoolConductor> conductors = new LinkedList<>();
    private List<E> energyPaths;

    public CoolTick(T source, List<E> list) {
        this.source = source;
        this.energyPaths = list;

    }

    public LinkedList<ICoolConductor> getConductors() {
        return conductors;
    }

    public void setConductors(final LinkedList<ICoolConductor> conductors) {
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
        CoolTick that = (CoolTick) o;
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
