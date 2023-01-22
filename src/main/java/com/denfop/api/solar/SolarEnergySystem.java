package com.denfop.api.solar;

import net.minecraft.item.ItemStack;

import java.util.List;

public class SolarEnergySystem {

    public static SolarEnergySystem system;

    public void recalculation(ISolarTile solarTile, EnumTypeParts parts){
        switch (parts){
            case OUTPUT:
                List<ItemStack> items = solarTile.getOutputItems();
                int max = 0;
                double average = 0;
                double output = 0;
                for(ItemStack item : items){
                    if(item.isEmpty())
                        continue;
                 int meta =   item.getItemDamage();
                    if(meta + 1 > max)
                        max = meta + 1;
                    average += meta + 1;
                    output += ((IOutputItem)item.getItem()).getOutput(meta);
                }
                average /= items.size();
                average = Math.floor(average);
                if(average == max)
                    solarTile.setBonus(EnumTypeParts.OUTPUT,0.02 * max);
                else
                    solarTile.setBonus(EnumTypeParts.OUTPUT,0);
                solarTile.setOutput(output);
                break;
            case CAPACITY:

                 items = solarTile.getCapacityItems();
                 max = 0;
                 average = 0;
                 int capacity = 0;
                for(ItemStack item : items){
                    if(item.isEmpty())
                        continue;
                    int meta =   item.getItemDamage();
                    if(meta + 1 > max)
                        max = meta + 1;
                    average += meta + 1;
                    capacity += ((IBatteryItem)item.getItem()).getCapacity(meta);
                }
                average /= items.size();
                average = Math.floor(average);
                if(average == max)
                    solarTile.setBonus(EnumTypeParts.CAPACITY,0.15 * max);
                else
                    solarTile.setBonus(EnumTypeParts.CAPACITY,0);
                solarTile.setCapacity(capacity);
                break;
            case GENERATION:
                items = solarTile.getGenerationItems();
                max = 0;
                average = 0;
                int day = 0;
                int night = 0;
                int day_night = 0;
                for(ItemStack item : items){
                    if(item.isEmpty())
                        continue;
                    int meta =   item.getItemDamage();
                    if(meta + 1 > max)
                        max = meta + 1;
                    average += meta + 1;
                    ISolarItem solarItem = (ISolarItem) item.getItem();
                    switch (solarItem.getType()){
                        case DAY:
                            day+=solarItem.getGenerationValue(meta);
                            break;
                        case NIGHT:
                            night+=solarItem.getGenerationValue(meta);
                            break;
                        case DAY_NIGHT:
                            day_night+=solarItem.getGenerationValue(meta);
                            break;
                    }
                }
                average /= items.size();
                double load = (max - average) * 2 * max;
                solarTile.setLoad(load);
                average = Math.floor(average);
                if(average == max)
                    solarTile.setBonus(EnumTypeParts.GENERATION,0.05 * max);
                else
                    solarTile.setBonus(EnumTypeParts.GENERATION,0);
                solarTile.setGeneration(EnumSolarType.DAY,day);
                solarTile.setGeneration(EnumSolarType.NIGHT,night);
                solarTile.setGeneration(EnumSolarType.DAY_NIGHT,day_night);

                break;
        }

    }
}
