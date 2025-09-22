package com.denfop.tiles.reactors.heat;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.invslot.Inventory;

public interface IPump extends IMultiElement {

    int getEnergy();

    int getPower();

    Inventory getSlot();

}
