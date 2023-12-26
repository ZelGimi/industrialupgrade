package com.denfop.register;

import com.denfop.IUItem;
import com.denfop.api.reactors.IHeatReactor;
import com.denfop.tiles.reactors.heat.ICasing;
import com.denfop.tiles.reactors.heat.IChamber;
import com.denfop.tiles.reactors.heat.ICirculationPump;
import com.denfop.tiles.reactors.heat.ICoolant;
import com.denfop.tiles.reactors.heat.IGraphiteController;
import com.denfop.tiles.reactors.heat.IPump;
import com.denfop.tiles.reactors.heat.IReactor;
import com.denfop.tiles.reactors.heat.ISocket;
import com.denfop.tiles.reactors.heat.ITank;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.advHeatReactorMultiBlock;

public class MultiBlockAdvHeatReactor {

    public static void init() {
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos(), IHeatReactor.class,
                new ItemStack(IUItem.heat_reactor,1,5),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(1,1,0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor,1,17),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(0,1,0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor,1,17),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(1,0,0), ISocket.class,
                new ItemStack(IUItem.heat_reactor,1,29),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(2,0,1), IPump.class,
                new ItemStack(IUItem.heat_reactor,1,1),
                EnumFacing.WEST
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(2,0,2), IPump.class,
                new ItemStack(IUItem.heat_reactor,1,1),
                EnumFacing.WEST
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(-1,0,1), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor,1,37),
                EnumFacing.EAST
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(-1,0,2), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor,1,37),
                EnumFacing.EAST
        );

        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(0,0,3), ITank.class,
                new ItemStack(IUItem.heat_reactor,1,33),
                EnumFacing.SOUTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(0,1,3), ITank.class,
                new ItemStack(IUItem.heat_reactor,1,33),
                EnumFacing.SOUTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(1,0,3), ITank.class,
                new ItemStack(IUItem.heat_reactor,1,33),
                EnumFacing.SOUTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(1,1,3), ITank.class,
                new ItemStack(IUItem.heat_reactor,1,33),
                EnumFacing.SOUTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(0,3,2), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor,1,21),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(1,3,1), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor,1,21),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(0,2,2), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,9),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(1,2,1), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,9),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(0,1,2), IReactor.class,
                new ItemStack(IUItem.heat_reactor,1,13),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(1,1,1), IReactor.class,
                new ItemStack(IUItem.heat_reactor,1,13),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(0,0,2), IReactor.class,
                new ItemStack(IUItem.heat_reactor,1,13),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(1,0,1), IReactor.class,
                new ItemStack(IUItem.heat_reactor,1,13),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(0,2,1), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,9),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(1,2,2), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,9),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(0,1,1), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,9),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(1,1,2), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,9),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(0,0,1), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,9),
                EnumFacing.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(1,0,2), IChamber.class,
                new ItemStack(IUItem.heat_reactor,1,9),
                EnumFacing.NORTH
        );
        for(int x = -1; x < 3;x++){
            for(int y = -1; y < 4;y++){
                for(int z = 0; z < 4;z++){
                    advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().add(x,y,z), ICasing.class,
                            new ItemStack(IUItem.heat_reactor, 1, 25),
                            EnumFacing.SOUTH
                    );
                }
            }
        }
    }

}
