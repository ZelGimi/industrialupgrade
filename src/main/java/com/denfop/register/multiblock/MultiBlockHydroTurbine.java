package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.hydroturbine.ICasing;
import com.denfop.tiles.hydroturbine.ICasing1;
import com.denfop.tiles.hydroturbine.ICasing2;
import com.denfop.tiles.hydroturbine.IController;
import com.denfop.tiles.hydroturbine.ISocket;
import com.denfop.tiles.hydroturbine.IStabilizer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.HydroTurbineMultiBlock;

public class MultiBlockHydroTurbine {

    public static void init() {
        HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.hydroTurbine, 1, 0),
                EnumFacing.NORTH
        );
        HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().add(0, 0, 1), IStabilizer.class,
                new ItemStack(IUItem.hydroTurbine, 1, 2),
                EnumFacing.NORTH
        );
        HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().add(0, 0, 2), ISocket.class,
                new ItemStack(IUItem.hydroTurbine, 1, 1),
                EnumFacing.SOUTH
        );
        for (int i = 0; i < 4; i++) {

            HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().add(1, -3 + i, 0), ICasing.class,
                    new ItemStack(IUItem.hydroTurbine, 1, 3),
                    EnumFacing.NORTH
            );
        }
        for (int i = 0; i < 4; i++) {

            HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().add(-1, -3 + i, 0), ICasing.class,
                    new ItemStack(IUItem.hydroTurbine, 1, 3),
                    EnumFacing.NORTH
            );
        }
        for (int i = 0; i < 4; i++) {

            HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().add(-1, -3 + i, 2), ICasing.class,
                    new ItemStack(IUItem.hydroTurbine, 1, 3),
                    EnumFacing.NORTH
            );
        }
        for (int i = 0; i < 4; i++) {

            HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().add(1, -3 + i, 2), ICasing.class,
                    new ItemStack(IUItem.hydroTurbine, 1, 3),
                    EnumFacing.NORTH
            );
        }

        for (int i = 0; i < 3; i++) {

            HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().add(1, 1, i), ICasing1.class,
                    new ItemStack(IUItem.hydroTurbine, 1, 4),
                    EnumFacing.WEST
            );
        }
        for (int i = 0; i < 3; i++) {

            HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().add(-1, 1, i), ICasing1.class,
                    new ItemStack(IUItem.hydroTurbine, 1, 4),
                    EnumFacing.EAST
            );
        }
        for (int i = 0; i < 3; i++) {

            HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().add(0, 1, i), ICasing2.class,
                    new ItemStack(IUItem.hydroTurbine, 1, 5),
                    EnumFacing.NORTH
            );
        }
    }

}
