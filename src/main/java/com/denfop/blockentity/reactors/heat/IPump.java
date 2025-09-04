package com.denfop.blockentity.reactors.heat;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.inventory.Inventory;

public interface IPump extends IMultiElement {

    int getEnergy();

    int getPower();

    Inventory getSlot();

}
