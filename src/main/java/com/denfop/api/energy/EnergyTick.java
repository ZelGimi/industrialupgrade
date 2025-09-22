package com.denfop.api.energy;


import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class EnergyTick {

    private final IEnergySource source;
    private final boolean isAdv;
    private final boolean isDual;
    public boolean hasHash = false;
    List<Integer> conductors = new LinkedList<>();
    private IEnergySource advSource = null;
    private List<Path> energyPaths;
    private int hash;


    public EnergyTick(IEnergySource source, List<Path> list) {
        this.source = source;
        this.energyPaths = list;
        this.isAdv = source != null;
        this.isDual = source instanceof IDual;
        if (this.isAdv) {
            this.advSource = source;
        }
    }

    public IEnergySource getSource() {
        return source;
    }

    public boolean isAdv() {
        return isAdv;
    }

    public boolean isDual() {
        return isDual;
    }

    public void tick() {
        if (this.isAdv) {
            if (!this.isDual && this.advSource.isSource()) {
                this.advSource.setPastEnergy(this.advSource.getPerEnergy());
            } else if (this.isDual && (this.advSource.isSource())) {
                ((IDual) this.advSource).setPastEnergy1(((IDual) this.advSource).getPerEnergy1());

            }
        }
    }

    public void addEnergy(double amount) {
        if (this.isAdv) {
            if (!this.isDual && this.advSource.isSource()) {
                this.advSource.addPerEnergy(amount);
            } else if (this.isDual && this.advSource.isSource()) {
                ((IDual) advSource).addPerEnergy1(amount);
            }
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnergyTick that = (EnergyTick) o;
        return source == that.source;
    }

    public IEnergySource getAdvSource() {
        return advSource;
    }

    @Override
    public int hashCode() {
        if (source != null && !hasHash) {
            this.hash = Objects.hash(source);
            hasHash = true;
            return hash;
        } else if (hasHash) {
            return hash;
        }
        return Objects.hash(source);
    }

    public List<Path> getList() {
        return energyPaths;
    }

    public void setList(final List<Path> energyPaths) {
        this.energyPaths = energyPaths;
        if (this.energyPaths == null) {
            this.conductors.clear();
        }
    }

    public void rework() {
        energyPaths.sort(Comparator.comparingInt(path -> path.target.getSinkTier()));
    }

    public List<Integer> getConductors() {
        return conductors;
    }

    public void setConductors(final List<Integer> conductors) {
        this.conductors = conductors;
    }

}
