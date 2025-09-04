package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.blockentity.geothermalpump.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.GeoThermalPumpMultiBlock;

public class MultiBlockGeothermalPump {

    public static void init() {
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.geothermalpump.getItem(0)),
                Direction.NORTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().offset(-1, 0, 0), ICasing.class,
                new ItemStack(IUItem.geothermalpump.getItem(2)),
                Direction.NORTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().offset(1, 0, 0), ICasing.class,
                new ItemStack(IUItem.geothermalpump.getItem(2)),
                Direction.NORTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().offset(-1, 0, 1), IWaste.class,
                new ItemStack(IUItem.geothermalpump.getItem(6)),
                Direction.EAST
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().offset(1, 0, 1), IWaste.class,
                new ItemStack(IUItem.geothermalpump.getItem(6)),
                Direction.WEST
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().offset(0, 0, 1), ICasing.class,
                new ItemStack(IUItem.geothermalpump.getItem(2)),
                Direction.NORTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().offset(0, -1, 1), ICasing.class,
                new ItemStack(IUItem.geothermalpump.getItem(2)),
                Direction.NORTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().offset(-1, 0, 2), IGenerator.class,
                new ItemStack(IUItem.geothermalpump.getItem(4)),
                Direction.SOUTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().offset(1, 0, 2), IGenerator.class,
                new ItemStack(IUItem.geothermalpump.getItem(4)),
                Direction.SOUTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().offset(0, 0, 2), IExchanger.class,
                new ItemStack(IUItem.geothermalpump.getItem(1)),
                Direction.SOUTH
        );
        for (int i = -1; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().offset(i, -1, j), ITransport.class,
                        new ItemStack(IUItem.geothermalpump.getItem(5)),
                        Direction.NORTH
                );
            }
        }

        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().offset(-1, -2, 2), IRig.class,
                new ItemStack(IUItem.geothermalpump.getItem(3)),
                Direction.SOUTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().offset(1, -2, 2), IRig.class,
                new ItemStack(IUItem.geothermalpump.getItem(3)),
                Direction.SOUTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().offset(-1, -2, 0), IRig.class,
                new ItemStack(IUItem.geothermalpump.getItem(3)),
                Direction.SOUTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().offset(1, -2, 0), IRig.class,
                new ItemStack(IUItem.geothermalpump.getItem(3)),
                Direction.SOUTH
        );
    }

}
