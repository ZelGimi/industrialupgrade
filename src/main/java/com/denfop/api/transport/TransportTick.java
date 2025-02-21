package com.denfop.api.transport;

import com.denfop.api.sytem.IConductor;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TransportTick<T, E> {

    private final T source;
    private List<E> energyPaths;
    private List<E> energyPaths1;

    public TransportTick(T source, List<E> list) {
        this.source = source;
        this.energyPaths = list;

    }

    public void setList(final List<E> energyPaths) {
        this.energyPaths = energyPaths;
    }

    public T getSource() {
        return source;
    }
    LinkedList<ITransportConductor> conductors = new LinkedList<>();
    public LinkedList<ITransportConductor> getConductors() {
        return conductors;
    }

    public void setConductors(final LinkedList<ITransportConductor> conductors) {
        this.conductors = conductors;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransportTick that = (TransportTick) o;
        return source == that.source;
    }


    @Override
    public int hashCode() {
        return Objects.hash(source);
    }

    public List<E> getList() {
        return energyPaths;
    }

    public void setItemList(final List<E> energyPaths) {
        this.energyPaths = energyPaths;
    }

    public List<E> getEnergyItemPaths() {
        return energyPaths;
    }

    public List<E> getEnergyFluidPaths() {
        return energyPaths1;
    }

    public void setFluidList(final List<E> energyPaths) {
        this.energyPaths1 = energyPaths;
    }

}
