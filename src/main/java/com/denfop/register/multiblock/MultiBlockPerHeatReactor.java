package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IHeatReactor;
import com.denfop.blockentity.reactors.heat.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.perHeatReactorMultiBlock;

public class MultiBlockPerHeatReactor {

    public static void init() {
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos(), IHeatReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(7)),
                Direction.NORTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 0, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(19)),
                Direction.NORTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 1, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(19)),
                Direction.NORTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 0, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(19)),
                Direction.NORTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 1, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(19)),
                Direction.NORTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 0, 0), ISocket.class,
                new ItemStack(IUItem.heat_reactor.getItem(31)),
                Direction.NORTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(2, 0, 1), IPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(3)),
                Direction.WEST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(2, 1, 1), IPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(3)),
                Direction.WEST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(2, 0, 2), IPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(3)),
                Direction.WEST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(2, 1, 2), IPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(3)),
                Direction.WEST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(2, 0, 3), IPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(3)),
                Direction.WEST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(2, 1, 3), IPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(3)),
                Direction.WEST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(2, 0, 4), IPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(3)),
                Direction.WEST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(2, 1, 4), IPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(3)),
                Direction.WEST
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-3, 0, 1), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(39)),
                Direction.EAST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-3, 1, 1), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(39)),
                Direction.EAST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-3, 0, 2), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(39)),
                Direction.EAST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-3, 1, 2), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(39)),
                Direction.EAST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-3, 0, 3), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(39)),
                Direction.EAST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-3, 1, 3), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(39)),
                Direction.EAST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-3, 0, 4), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(39)),
                Direction.EAST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-3, 1, 4), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(39)),
                Direction.EAST
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 0, 5), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(35)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 1, 5), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(35)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 0, 5), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(35)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 1, 5), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(35)),
                Direction.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 0, 5), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(19)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 1, 5), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(19)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 0, 5), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(19)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 1, 5), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(19)),
                Direction.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 0, 1), null,
                ItemStack.EMPTY,
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 1, 1), null,
                ItemStack.EMPTY,
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 2, 1), null,
                ItemStack.EMPTY,
                Direction.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 0, 1), null,
                ItemStack.EMPTY,
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 1, 1), null,
                ItemStack.EMPTY,
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 2, 1), null,
                ItemStack.EMPTY,
                Direction.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 0, 4), null,
                ItemStack.EMPTY,
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 1, 4), null,
                ItemStack.EMPTY,
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 2, 4), null,
                ItemStack.EMPTY,
                Direction.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 0, 4), null,
                ItemStack.EMPTY,
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 1, 4), null,
                ItemStack.EMPTY,
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 2, 4), null,
                ItemStack.EMPTY,
                Direction.SOUTH
        );


        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 1, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 2, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 3, 1), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(23)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 0, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 1, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 2, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 3, 1), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(23)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 0, 4), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 1, 4), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 2, 4), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 3, 4), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(23)),
                Direction.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 0, 4), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 1, 4), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 2, 4), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 3, 4), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(23)),
                Direction.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 0, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 1, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 2, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 3, 2), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(23)),
                Direction.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 0, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 1, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 2, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(1, 3, 3), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(23)),
                Direction.SOUTH
        );


        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 0, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 1, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 2, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 3, 2), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(23)),
                Direction.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 0, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 1, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 2, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-2, 3, 3), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(23)),
                Direction.SOUTH
        );


        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 0, 2), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(15)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 0, 3), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(15)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 0, 2), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(15)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 0, 3), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(15)),
                Direction.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 1, 2), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(15)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 1, 3), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(15)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 1, 2), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(15)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 1, 3), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(15)),
                Direction.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 2, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(0, 2, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 2, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(-1, 2, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(11)),
                Direction.SOUTH
        );

        for (int x = -3; x < 3; x++) {
            for (int y = -1; y < 4; y++) {
                for (int z = 0; z < 6; z++) {
                    perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.heat_reactor.getItem(27)),
                            Direction.SOUTH
                    );
                }
            }
        }
    }

}
