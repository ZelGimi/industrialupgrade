package com.denfop.api.storage;

import com.denfop.api.storage.cell.ICell;
import com.denfop.inventory.Inventory;

import java.util.List;

public interface StorageDeviceCell extends ElectricStorage {

    List<ICell> getItemCells();

    List<ICell> getFluidCells();

    Inventory getSlots();
}
