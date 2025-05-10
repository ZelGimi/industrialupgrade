package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.hydroturbine.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.HydroTurbineMultiBlock;

public class MultiBlockHydroTurbine {

    public static void init() {
        HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.hydroTurbine.getItem(0)),
                        Direction.NORTH
                );
        HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().offset(0, 0, 1), IStabilizer.class,
                new ItemStack(IUItem.hydroTurbine.getItem(2)),
                        Direction.NORTH
                );
        HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().offset(0, 0, 2), ISocket.class,
                new ItemStack(IUItem.hydroTurbine.getItem(1)),
                        Direction.SOUTH
                );
        for (int i = 0; i < 4; i++) {

            HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().offset(1, -3 + i, 0), ICasing.class,
                    new ItemStack(IUItem.hydroTurbine.getItem(3)),
                            Direction.NORTH
                    );
        }
        for (int i = 0; i < 4; i++) {

            HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().offset(-1, -3 + i, 0), ICasing.class,
                    new ItemStack(IUItem.hydroTurbine.getItem(3)),
                            Direction.NORTH
                    );
        }
        for (int i = 0; i < 4; i++) {

            HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().offset(-1, -3 + i, 2), ICasing.class,
                    new ItemStack(IUItem.hydroTurbine.getItem(3)),
                            Direction.NORTH
                    );
        }
        for (int i = 0; i < 4; i++) {

            HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().offset(1, -3 + i, 2), ICasing.class,
                    new ItemStack(IUItem.hydroTurbine.getItem(3)),
                            Direction.NORTH
                    );
        }

        for (int i = 0; i < 3; i++) {

            HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().offset(1, 1, i), ICasing1.class,
                    new ItemStack(IUItem.hydroTurbine.getItem(4)),
                            Direction.WEST
                    );
        }
        for (int i = 0; i < 3; i++) {

            HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().offset(-1, 1, i), ICasing1.class,
                    new ItemStack(IUItem.hydroTurbine.getItem(4)),
                            Direction.EAST
                    );
        }
        for (int i = 0; i < 3; i++) {

            HydroTurbineMultiBlock.add(HydroTurbineMultiBlock.getPos().offset(0, 1, i), ICasing2.class,
                    new ItemStack(IUItem.hydroTurbine.getItem(5)),
                            Direction.NORTH
                    );
        }
    }

}
