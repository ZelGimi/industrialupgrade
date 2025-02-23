package com.denfop.api.space.rovers.enums;

import com.denfop.api.space.EnumLevels;
import com.denfop.blocks.FluidName;

import java.util.Arrays;
import java.util.List;

public enum EnumRoversLevelFluid {
    ONE(FluidName.fluidhydrazine),
    TWO(FluidName.fluidhydrazine,FluidName.fluiddimethylhydrazine),
    THREE(FluidName.fluidhydrazine,FluidName.fluiddimethylhydrazine,FluidName.fluiddecane),
    FOUR(FluidName.fluidhydrazine,FluidName.fluiddimethylhydrazine,FluidName.fluiddecane,FluidName.fluidxenon);
    List<FluidName> levelsList;
    EnumRoversLevelFluid(FluidName... levels){
        levelsList = Arrays.asList(levels);
    }

    public List<FluidName> getLevelsList() {
        return levelsList;
    }
}
