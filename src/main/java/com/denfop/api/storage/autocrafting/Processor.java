package com.denfop.api.storage.autocrafting;

import com.denfop.api.storage.ElectricStorage;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Processor extends ElectricStorage {
    int getMaxSize();

    Collection<AutoCraftSystem> getAutoCrafts();

    AutoCraftSystem getAutoCraft(int id);

    Set<Integer> getIndexes();

    void cancelAutoCraft(int idAutoCraft);

    boolean canAddAutoCraft();

    void addAutoCraft(AutoCraftSystem autoCraftSystem);

    int getId();

    void setId(int id);

    Map<String, Map<SameStack, Integer>> getItemsForCraft(int idAutoCraft);

    Map<Integer, Map<String, Map<SameStack, Integer>>> getAllItemsForCraft();

    void removeCraft(Integer b);

    void removeNetworkCraft(Integer b);
}
