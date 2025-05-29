package com.denfop.api.solar;

import com.denfop.items.ItemMain;
import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SolarEnergySystem {

    public static SolarEnergySystem system;
    public static int[][] indexes = new int[][]{{1, 3}, {2, 4}, {5}, {4, 6}, {5, 7}, {8}, {7}, {8}, {}};

    public void calculateCores(ISolarTile solarTile) {
        List<ItemStack> stackList = solarTile.getCoresItems();
        int level = 0;
        for (ItemStack stack : stackList) {
            if (!stack.isEmpty()) {
                level += (((ItemMain<?>)stack.getItem()).getElement().getId() + 1);
            }
        }
        solarTile.setCoreLevel(level);
    }

    public void recalculation(ISolarTile solarTile, EnumTypeParts parts) {
        switch (parts) {
            case OUTPUT:
                List<ItemStack> items = solarTile.getOutputItems();
                int max = 0;
                double average = 0;
                double output = 0;
                for (ItemStack item : items) {
                    if (item == null || item.isEmpty()) {
                        continue;
                    }
                    int meta = ((ItemMain<?>)item.getItem()).getElement().getId();
                    if (meta + 1 > max) {
                        max = meta + 1;
                    }
                    average += meta + 1;
                    output += ((IOutputItem) item.getItem()).getOutput(meta);
                }
                average /= items.size();
                average = Math.floor(average);
                if (average == max) {
                    solarTile.setBonus(EnumTypeParts.OUTPUT, 0.05 * max);
                } else {
                    solarTile.setBonus(EnumTypeParts.OUTPUT, 0);
                }
                solarTile.setOutput(output);
                break;
            case CAPACITY:

                items = solarTile.getCapacityItems();
                max = 0;
                average = 0;
                int capacity = 0;
                for (ItemStack item : items) {
                    if (item == null || item.isEmpty()) {
                        continue;
                    }
                    int meta = ((ItemMain<?>)item.getItem()).getElement().getId();
                    if (meta + 1 > max) {
                        max = meta + 1;
                    }
                    average += meta + 1;
                    capacity += ((IBatteryItem) item.getItem()).getCapacity(meta);
                }
                average /= items.size();
                average = Math.floor(average);
                if (average == max) {
                    solarTile.setBonus(EnumTypeParts.CAPACITY, 0.1 * max);
                } else {
                    solarTile.setBonus(EnumTypeParts.CAPACITY, 0);
                }
                solarTile.setCapacity(capacity);
                break;
            case GENERATION:
                items = solarTile.getGenerationItems();
                max = 0;
                average = 0;
                double day = 0;
                double night = 0;
                double day_night = 0;
                int index = 0;
                boolean canBonus = true;
                EnumSolarType solarType = null;
                for (ItemStack item : items) {
                    if (item == null || item.isEmpty()) {
                        solarTile.setStables(index, Collections.emptyList());
                        index++;
                        continue;
                    }
                    int meta = ((ItemMain<?>)item.getItem()).getElement().getId();
                    if (meta + 1 > max) {
                        max = meta + 1;
                    }
                    average += meta + 1;
                    ISolarItem solarItem = (ISolarItem) item.getItem();
                    if (solarType == null) {
                        solarType = solarItem.getType();
                    } else if (solarType != solarItem.getType()) {
                        canBonus = false;
                    }
                    switch (solarItem.getType()) {
                        case DAY:
                            day += solarItem.getGenerationValue(meta);
                            break;
                        case NIGHT:
                            night += solarItem.getGenerationValue(meta);
                            break;
                        case DAY_NIGHT:
                            day_night += solarItem.getGenerationValue(meta);
                            break;
                    }
                    final int[] indexes1 = indexes[index];
                    List<TileEntityMiniPanels.EnumState> enumStateList = new ArrayList<>();
                    for (int i : indexes1) {
                        final ItemStack stack = items.get(i);
                        if (stack == null || stack.isEmpty()) {
                            enumStateList.add(TileEntityMiniPanels.EnumState.EMPTY);
                        } else {
                            int meta1 = ((ItemMain<?>)stack.getItem()).getElement().getId();
                            int differenceMeta = Math.abs(meta - meta1);
                            if (differenceMeta == 0) {
                                enumStateList.add(TileEntityMiniPanels.EnumState.STABLE);
                            } else if (differenceMeta == 1) {
                                enumStateList.add(TileEntityMiniPanels.EnumState.NORMAL);
                            } else {
                                enumStateList.add(TileEntityMiniPanels.EnumState.UNSTABLE);
                            }
                        }
                    }
                    solarTile.setStables(index, enumStateList);
                    index++;
                }
                average /= items.size();
                double load = (max - average) * max * 15;

                if (load >= 99) {
                    if (max <= solarTile.getCoreLevel()) {
                        load = 99;
                    }
                }

                solarTile.setLoad(load);
                average = Math.floor(average);
                if (average == max && canBonus) {
                    solarTile.setBonus(EnumTypeParts.GENERATION, 0.05 * max);
                } else {
                    solarTile.setBonus(EnumTypeParts.GENERATION, 0);
                }
                solarTile.setGeneration(EnumSolarType.DAY, day);
                solarTile.setGeneration(EnumSolarType.NIGHT, night);
                solarTile.setGeneration(EnumSolarType.DAY_NIGHT, day_night);

                break;
        }

    }

}
