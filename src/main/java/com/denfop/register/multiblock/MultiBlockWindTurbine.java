package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.windturbine.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.WindTurbineMultiBlock;

public class MultiBlockWindTurbine {

    public static void init() {
        WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.windTurbine.getItem(0)),
                        Direction.NORTH
                );
        WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().offset(0, 0, 1), IStabilizer.class,
                new ItemStack(IUItem.windTurbine.getItem(2)),
                        Direction.NORTH
                );
        WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().offset(0, 0, 2), ISocket.class,
                new ItemStack(IUItem.windTurbine.getItem(1)),
                        Direction.SOUTH
                );
        for (int i = 0; i < 4; i++) {

            WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().offset(1, -3 + i, 0), ICasing.class,
                    new ItemStack(IUItem.windTurbine.getItem(3)),
                            Direction.NORTH
                    );
        }
        for (int i = 0; i < 4; i++) {

            WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().offset(-1, -3 + i, 0), ICasing.class,
                    new ItemStack(IUItem.windTurbine.getItem(3)),
                            Direction.NORTH
                    );
        }
        for (int i = 0; i < 4; i++) {

            WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().offset(-1, -3 + i, 2), ICasing.class,
                    new ItemStack(IUItem.windTurbine.getItem(3)),
                            Direction.NORTH
                    );
        }
        for (int i = 0; i < 4; i++) {

            WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().offset(1, -3 + i, 2), ICasing.class,
                    new ItemStack(IUItem.windTurbine.getItem(3)),
                            Direction.NORTH
                    );
        }

        for (int i = 0; i < 3; i++) {

            WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().offset(1, 1, i), ICasing1.class,
                    new ItemStack(IUItem.windTurbine.getItem(4)),
                            Direction.WEST
                    );
        }
        for (int i = 0; i < 3; i++) {

            WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().offset(-1, 1, i), ICasing1.class,
                    new ItemStack(IUItem.windTurbine.getItem(4)),
                            Direction.EAST
                    );
        }
        for (int i = 0; i < 3; i++) {
            WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().offset(0, 1, i), ICasing2.class,
                    new ItemStack(IUItem.windTurbine.getItem(5)),
                            Direction.NORTH
                    );
        }
    }

}
