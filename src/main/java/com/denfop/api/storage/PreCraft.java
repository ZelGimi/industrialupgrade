package com.denfop.api.storage;

import com.denfop.api.storage.autocrafting.SameStack;

import java.util.List;

public interface PreCraft extends ElectricStorage {

    List<SameStack> getPreCrafts();
}
