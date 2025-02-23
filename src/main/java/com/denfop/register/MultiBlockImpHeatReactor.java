package com.denfop.register;

import com.denfop.IUItem;
import com.denfop.api.reactors.IHeatReactor;
import com.denfop.tiles.reactors.heat.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.advHeatReactorMultiBlock;
import static com.denfop.register.InitMultiBlockSystem.impHeatReactorMultiBlock;

public class MultiBlockImpHeatReactor {

    public static void init() {
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos(), IHeatReactor.class,
                new ItemStack(IUItem.heat_reactor,1,6),
                EnumFacing.NORTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(1,0,0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor,1,18),
                EnumFacing.NORTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(1,1,0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor,1,18),
                EnumFacing.NORTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-1,0,0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor,1,18),
                EnumFacing.NORTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-1,1,0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor,1,18),
                EnumFacing.NORTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(2,0,1), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor,1,38),
                EnumFacing.WEST
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(2,1,1), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor,1,38),
                EnumFacing.WEST
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(2,0,3), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor,1,38),
                EnumFacing.WEST
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(2,1,3), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor,1,38),
                EnumFacing.WEST
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-2,0,1), IPump.class,
                new ItemStack(IUItem.heat_reactor,1,2),
                EnumFacing.EAST
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-2,1,1), IPump.class,
                new ItemStack(IUItem.heat_reactor,1,2),
                EnumFacing.EAST
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-2,0,3), IPump.class,
                new ItemStack(IUItem.heat_reactor,1,2),
                EnumFacing.EAST
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-2,1,3), IPump.class,
                new ItemStack(IUItem.heat_reactor,1,2),
                EnumFacing.EAST
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-1,1,4), ITank.class,
                new ItemStack(IUItem.heat_reactor,1,34),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-1,0,4), ITank.class,
                new ItemStack(IUItem.heat_reactor,1,34),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(1,1,4), ITank.class,
                new ItemStack(IUItem.heat_reactor,1,34),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(1,0,4), ITank.class,
                new ItemStack(IUItem.heat_reactor,1,34),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(0,-1,4), ISocket.class,
                new ItemStack(IUItem.heat_reactor,1,30),
                EnumFacing.SOUTH
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(1,0,1), IReactor.class,
                new ItemStack(IUItem.heat_reactor,1,14),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(1,1,1), IReactor.class,
                new ItemStack(IUItem.heat_reactor,1,14),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(1,2,1), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(1,3,1), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor,1,22),
                EnumFacing.SOUTH
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-1,0,1), IReactor.class,
                new ItemStack(IUItem.heat_reactor,1,14),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-1,1,1), IReactor.class,
                new ItemStack(IUItem.heat_reactor,1,14),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-1,2,1), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-1,3,1), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor,1,22),
                EnumFacing.SOUTH
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(0,0,2), IReactor.class,
                new ItemStack(IUItem.heat_reactor,1,14),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(0,1,2), IReactor.class,
                new ItemStack(IUItem.heat_reactor,1,14),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(0,2,2), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(0,3,2), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor,1,22),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(1,0,3), IReactor.class,
                new ItemStack(IUItem.heat_reactor,1,14),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(1,1,3), IReactor.class,
                new ItemStack(IUItem.heat_reactor,1,14),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(1,2,3), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(1,3,3), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor,1,22),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-1,0,3), IReactor.class,
                new ItemStack(IUItem.heat_reactor,1,14),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-1,1,3), IReactor.class,
                new ItemStack(IUItem.heat_reactor,1,14),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-1,2,3), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-1,3,3), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor,1,22),
                EnumFacing.SOUTH
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(0,0,1), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(0,1,1), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(0,2,1), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-1,0,2), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-1,1,2), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(-1,2,2), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(1,0,2), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(1,1,2), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(1,2,2), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(0,0,3), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(0,1,3), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(0,2,3), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,10),
                EnumFacing.SOUTH
        );
        for(int x = -2; x < 3;x++){
            for(int y = -1; y < 4;y++){
                for(int z = 0; z < 5;z++){
                    impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().add(x,y,z), ICasing.class,
                            new ItemStack(IUItem.heat_reactor, 1, 26),
                            EnumFacing.SOUTH
                    );
                }
            }
        }
    }

}
