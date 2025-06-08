package com.denfop.api.guidebook;

public enum TypeObject {
    FLUID,
    ITEM,
    FLUID_ITEM;

    public boolean isItem() {
      return   this == ITEM || this == FLUID_ITEM;
    }
    public boolean isFluid() {
        return   this == FLUID || this == FLUID_ITEM;
    }
}
