package com.denfop.register;

import com.denfop.IUItem;
import com.denfop.api.reactors.IGasReactor;
import com.denfop.api.reactors.IHeatReactor;
import com.denfop.tiles.reactors.heat.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.GraphiteReactorMultiBlock;
import static com.denfop.register.InitMultiBlockSystem.HeatReactorMultiBlock;

public class MultiBlockHeatReactor {

    public static void init() {
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos(), IHeatReactor.class,
                new ItemStack(IUItem.heat_reactor, 1, 4),
                EnumFacing.NORTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().add(1,0,0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor, 1, 16),
                EnumFacing.NORTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().add(-1,0,0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor, 1, 16),
                EnumFacing.NORTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().add(1,0,1), IPump.class,
                new ItemStack(IUItem.heat_reactor, 1),
                EnumFacing.WEST
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().add(-1,0,1), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 36),
                EnumFacing.EAST
        );

        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().add(1,0,2), ITank.class,
                new ItemStack(IUItem.heat_reactor, 1,32),
                EnumFacing.SOUTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().add(-1,0,2), ITank.class,
                new ItemStack(IUItem.heat_reactor, 1, 32),
                EnumFacing.SOUTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().add(1,1,2), ITank.class,
                new ItemStack(IUItem.heat_reactor, 1,32),
                EnumFacing.SOUTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().add(-1,1,2), ITank.class,
                new ItemStack(IUItem.heat_reactor, 1, 32),
                EnumFacing.SOUTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().add(0,0,2), ISocket.class,
                new ItemStack(IUItem.heat_reactor, 1, 28),
                EnumFacing.SOUTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().add(0,0,1), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 8),
                EnumFacing.SOUTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().add(0,1,1), IReactor.class,
                new ItemStack(IUItem.heat_reactor, 1, 12),
                EnumFacing.SOUTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().add(0,3,1), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor, 1, 20),
                EnumFacing.UP
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().add(0,2,1), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 8),
                EnumFacing.SOUTH
        );
        for(int x = -1; x < 2;x++){
            for(int y = -1; y < 4;y++){
                for(int z = 0; z < 3;z++){
                    HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().add(x,y,z), ICasing.class,
                        new ItemStack(IUItem.heat_reactor, 1, 24),
                        EnumFacing.SOUTH
                );
            }
            }
        }
    }

}
