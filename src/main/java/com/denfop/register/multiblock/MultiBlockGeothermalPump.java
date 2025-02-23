package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.geothermalpump.ICasing;
import com.denfop.tiles.geothermalpump.IController;
import com.denfop.tiles.geothermalpump.IExchanger;
import com.denfop.tiles.geothermalpump.IGenerator;
import com.denfop.tiles.geothermalpump.IRig;
import com.denfop.tiles.geothermalpump.ITransport;
import com.denfop.tiles.geothermalpump.IWaste;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.GeoThermalPumpMultiBlock;

public class MultiBlockGeothermalPump {

    public static void init() {
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.geothermalpump, 1, 0),
                EnumFacing.NORTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().add(-1, 0, 0), ICasing.class,
                new ItemStack(IUItem.geothermalpump, 1, 2),
                EnumFacing.NORTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().add(1, 0, 0), ICasing.class,
                new ItemStack(IUItem.geothermalpump, 1, 2),
                EnumFacing.NORTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().add(-1, 0, 1), IWaste.class,
                new ItemStack(IUItem.geothermalpump, 1, 6),
                EnumFacing.EAST
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().add(1, 0, 1), IWaste.class,
                new ItemStack(IUItem.geothermalpump, 1, 6),
                EnumFacing.WEST
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().add(0, 0, 1), ICasing.class,
                new ItemStack(IUItem.geothermalpump, 1, 2),
                EnumFacing.NORTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().add(0, -1, 1), ICasing.class,
                new ItemStack(IUItem.geothermalpump, 1, 2),
                EnumFacing.NORTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().add(-1, 0, 2), IGenerator.class,
                new ItemStack(IUItem.geothermalpump, 1, 4),
                EnumFacing.SOUTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().add(1, 0, 2), IGenerator.class,
                new ItemStack(IUItem.geothermalpump, 1, 4),
                EnumFacing.SOUTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().add(0, 0, 2), IExchanger.class,
                new ItemStack(IUItem.geothermalpump, 1, 1),
                EnumFacing.SOUTH
        );
        for (int i = -1; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().add(i, -1, j), ITransport.class,
                        new ItemStack(IUItem.geothermalpump, 1, 5),
                        EnumFacing.NORTH
                );
            }
        }

        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().add(-1, -2, 2), IRig.class,
                new ItemStack(IUItem.geothermalpump, 1, 3),
                EnumFacing.SOUTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().add(1, -2, 2), IRig.class,
                new ItemStack(IUItem.geothermalpump, 1, 3),
                EnumFacing.SOUTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().add(-1, -2, 0), IRig.class,
                new ItemStack(IUItem.geothermalpump, 1, 3),
                EnumFacing.SOUTH
        );
        GeoThermalPumpMultiBlock.add(GeoThermalPumpMultiBlock.getPos().add(1, -2, 0), IRig.class,
                new ItemStack(IUItem.geothermalpump, 1, 3),
                EnumFacing.SOUTH
        );
    }

}
