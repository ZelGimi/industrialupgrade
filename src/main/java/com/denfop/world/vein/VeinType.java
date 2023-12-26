package com.denfop.world.vein;

import com.denfop.blocks.BlockHeavyOre;

import java.util.Arrays;
import java.util.List;

public class VeinType {

    private final List<ChanceOre> ores;
    private final BlockHeavyOre heavyOre;
    private TypeVein vein;
    private final int meta;

    public VeinType(BlockHeavyOre heavyOre,int meta, TypeVein vein, ChanceOre... ores){
        this.heavyOre=heavyOre;
        this.vein = vein;
        this.meta = meta;
        this.ores =  Arrays.asList(ores);
    }

    public void setVein(final TypeVein vein) {
        this.vein = vein;
    }

    public int getMeta() {
        return meta;
    }

    public BlockHeavyOre getHeavyOre() {
        return heavyOre;
    }

    public List<ChanceOre> getOres() {
        return ores;
    }

    public TypeVein getVein() {
        return vein;
    }

}
