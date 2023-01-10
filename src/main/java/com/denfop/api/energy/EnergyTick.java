package com.denfop.api.energy;

import ic2.api.energy.tile.IEnergySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnergyTick {

    private final IEnergySource source;
    private final boolean isAdv;
    private final boolean isDual;
    private IAdvEnergySource advSource = null;
    private List<EnergyNetLocal.EnergyPath> energyPaths;

    public EnergyTick(IEnergySource source, List<EnergyNetLocal.EnergyPath> list) {
        this.source = source;
        this.energyPaths = list;
        this.isAdv = source instanceof IAdvEnergySource;
        this.isDual = source instanceof IAdvDual;
        if (this.isAdv) {
            this.advSource = (IAdvEnergySource) source;
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
                ((IAdvDual) this.advSource).setPastEnergy1(((IAdvDual) this.advSource).getPerEnergy1());

            }
        }
    }

    public void addEnergy(double amount) {
        if (this.isAdv) {
            if (!this.isDual && this.advSource.isSource()) {
                this.advSource.addPerEnergy(amount);
            } else if (this.isDual && this.advSource.isSource()) {
                ((IAdvDual) advSource).addPerEnergy1(amount);
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

    public IAdvEnergySource getAdvSource() {
        return advSource;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source);
    }

    public List<EnergyNetLocal.EnergyPath> getList() {
        return energyPaths;
    }

    public void setList(final List<EnergyNetLocal.EnergyPath> energyPaths) {
        this.energyPaths = energyPaths;
    }

    public void rework() {
        List<EnergyNetLocal.EnergyPath> energyPaths1 = new ArrayList<>();
        int i = 1;
        while (!energyPaths.isEmpty()) {
            if (i < 14) {
                final int finalI = i;
                energyPaths.removeIf(energyPath -> {
                    if (energyPath.target.getSinkTier() == finalI) {
                        energyPaths1.add(energyPath);
                        return true;
                    }
                    return false;
                });
            } else {
                energyPaths1.addAll(energyPaths);
                energyPaths.clear();
                break;
            }
            i++;
        }
        this.energyPaths = energyPaths1;
    }

}
