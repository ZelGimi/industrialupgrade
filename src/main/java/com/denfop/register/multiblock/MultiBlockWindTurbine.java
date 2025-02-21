package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.windturbine.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.WindTurbineMultiBlock;

public class MultiBlockWindTurbine {
    public static void init() {
        WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.windTurbine, 1, 0),
                EnumFacing.NORTH
        );
        WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().add(0,0,1), IStabilizer.class,
                new ItemStack(IUItem.windTurbine, 1, 2),
                EnumFacing.NORTH
        );
        WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().add(0,0,2), ISocket.class,
                new ItemStack(IUItem.windTurbine, 1, 1),
                EnumFacing.SOUTH
        );
        for (int i = 0; i < 4;i++){

            WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().add(1,-3 + i,0), ICasing.class,
                    new ItemStack(IUItem.windTurbine, 1, 3),
                    EnumFacing.NORTH
            );
        }
        for (int i = 0; i < 4;i++){

            WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().add(-1,-3 + i,0), ICasing.class,
                    new ItemStack(IUItem.windTurbine, 1, 3),
                    EnumFacing.NORTH
            );
        }
        for (int i = 0; i < 4;i++){

            WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().add(-1,-3 + i,2), ICasing.class,
                    new ItemStack(IUItem.windTurbine, 1, 3),
                    EnumFacing.NORTH
            );
        }
        for (int i = 0; i < 4;i++){

            WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().add(1,-3 + i,2), ICasing.class,
                    new ItemStack(IUItem.windTurbine, 1, 3),
                    EnumFacing.NORTH
            );
        }

        for (int i = 0; i < 3;i++){

            WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().add(1,1,i), ICasing1.class,
                    new ItemStack(IUItem.windTurbine, 1, 4),
                    EnumFacing.WEST
            );
        }
        for (int i = 0; i < 3;i++){

            WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().add(-1,1,i), ICasing1.class,
                    new ItemStack(IUItem.windTurbine, 1, 4),
                    EnumFacing.EAST
            );
        }
        for (int i = 0; i < 3;i++){
            WindTurbineMultiBlock.add(WindTurbineMultiBlock.getPos().add(0,1,i), ICasing2.class,
                    new ItemStack(IUItem.windTurbine, 1, 5),
                    EnumFacing.NORTH
            );
        }
    }
}
