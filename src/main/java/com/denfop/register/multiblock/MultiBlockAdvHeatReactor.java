package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IHeatReactor;
import com.denfop.tiles.reactors.heat.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.advHeatReactorMultiBlock;

public class MultiBlockAdvHeatReactor {

    public static void init() {
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos(), IHeatReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(5)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(1, 1, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(17)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(0, 1, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(17)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(1, 0, 0), ISocket.class,
                new ItemStack(IUItem.heat_reactor.getItem(29)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(2, 0, 1), IPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(1)),
                Direction.WEST
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(2, 0, 2), IPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(1)),
                Direction.WEST
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(-1, 0, 1), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(37)),
                Direction.EAST
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(-1, 0, 2), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(37)),
                Direction.EAST
        );

        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(0, 0, 3), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(33)),
                Direction.SOUTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(0, 1, 3), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(33)),
                Direction.SOUTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(1, 0, 3), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(33)),
                Direction.SOUTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(1, 1, 3), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(33)),
                Direction.SOUTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(0, 3, 2), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(21)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(1, 3, 1), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(21)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(0, 2, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(9)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(1, 2, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(9)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(0, 1, 2), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(13)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(1, 1, 1), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(13)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(0, 0, 2), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(13)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(1, 0, 1), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(13)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(0, 2, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(9)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(1, 2, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(9)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(0, 1, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(9)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(1, 1, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(9)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(9)),
                Direction.NORTH
        );
        advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(1, 0, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(9)),
                Direction.NORTH
        );
        for (int x = -1; x < 3; x++) {
            for (int y = -1; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    advHeatReactorMultiBlock.add(advHeatReactorMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.heat_reactor.getItem(25)),
                            Direction.SOUTH
                    );
                }
            }
        }
    }

}
