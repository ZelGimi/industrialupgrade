package com.denfop.api.energy;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnergyTick {

    private final IEnergySource source;
    private final boolean isAdv;
    private final boolean isDual;
    private IEnergySource advSource = null;
    private List<Path> energyPaths;

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
        return Objects.hash(source);
    }

    public List<Path> getList() {
        return energyPaths;
    }

    public void setList(final List<Path> energyPaths) {
        this.energyPaths = energyPaths;
    }

    public void rework() {
        List<Path> energyPaths1 = new ArrayList<>();
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
