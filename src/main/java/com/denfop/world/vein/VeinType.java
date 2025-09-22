package com.denfop.world.vein;

import com.denfop.blocks.IMineral;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VeinType {

    private final List<ChanceOre> ores;
    private final IMineral heavyOre;
    private final int deposits_meta;
    private final int meta;
    private final boolean radiation;
    private TypeVein vein;
    public static Map<Integer, VeinType> veinTypeMap = new HashMap<>();
    public static int maxId = 0;
    private int id;

    public VeinType(IMineral heavyOre, int meta, int deposits_meta, TypeVein vein, ChanceOre... ores) {
        this(heavyOre, meta, deposits_meta, false, vein, ores);
    }

    public VeinType(IMineral heavyOre, int meta, int deposits_meta, boolean radiation, TypeVein vein, ChanceOre... ores) {
        this.heavyOre = heavyOre;
        this.vein = vein;
        this.meta = meta;
        this.deposits_meta = deposits_meta;
        this.ores = Arrays.asList(ores);
        this.radiation = radiation;
        this.id = maxId;
        veinTypeMap.put(id, this);
        maxId++;
    }

    public VeinType(IMineral heavyOre, int meta, TypeVein vein, ChanceOre... ores) {
        this(heavyOre, meta, meta, false, vein, ores);
    }

    public VeinType(IMineral heavyOre, int meta, boolean radiation, TypeVein vein, ChanceOre... ores) {
        this(heavyOre, meta, meta, radiation, vein, ores);
    }

    public boolean isRadiation() {
        return radiation;
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
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
