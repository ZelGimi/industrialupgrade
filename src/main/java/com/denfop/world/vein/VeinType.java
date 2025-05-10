package com.denfop.world.vein;

import com.denfop.blocks.IMineral;

import java.util.Arrays;
import java.util.List;

public class VeinType {

    private final List<ChanceOre> ores;
    private final IMineral heavyOre;
    private final int deposits_meta;
    private final int meta;
    private TypeVein vein;

    public VeinType(IMineral heavyOre, int meta, int deposits_meta, TypeVein vein, ChanceOre... ores) {
        this.heavyOre = heavyOre;
        this.vein = vein;
        this.meta = meta;
        this.deposits_meta = deposits_meta;
        this.ores = Arrays.asList(ores);
    }

    public VeinType(IMineral heavyOre, int meta, TypeVein vein, ChanceOre... ores) {
        this.heavyOre = heavyOre;
        this.vein = vein;
        this.meta = meta;
        this.deposits_meta = meta;
        this.ores = Arrays.asList(ores);
    }

    public int getMeta() {
        return meta;
    }

    public int getDeposits_meta() {
        return deposits_meta;
    }

    public IMineral getHeavyOre() {
        return heavyOre;
    }

    public List<ChanceOre> getOres() {
        return ores;
    }

    public void addChanceOre(ChanceOre chanceOre) {
        ores.add(chanceOre);
    }

    public TypeVein getVein() {
        return vein;
    }

    public void setVein(final TypeVein vein) {
        this.vein = vein;
    }

}
