package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IHeatReactor;
import com.denfop.tiles.reactors.heat.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.impHeatReactorMultiBlock;

public class MultiBlockImpHeatReactor {

    public static void init() {
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos(), IHeatReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(6)),
                Direction.NORTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(1, 0, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(18)),
                Direction.NORTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(1, 1, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(18)),
                Direction.NORTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-1, 0, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(18)),
                Direction.NORTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-1, 1, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(18)),
                Direction.NORTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(2, 0, 1), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(38)),
                Direction.WEST
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(2, 1, 1), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(38)),
                Direction.WEST
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(2, 0, 3), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(38)),
                Direction.WEST
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(2, 1, 3), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(38)),
                Direction.WEST
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-2, 0, 1), IPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(2)),
                Direction.EAST
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-2, 1, 1), IPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(2)),
                Direction.EAST
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-2, 0, 3), IPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(2)),
                Direction.EAST
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-2, 1, 3), IPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(2)),
                Direction.EAST
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-1, 1, 4), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(34)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-1, 0, 4), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(34)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(1, 1, 4), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(34)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(1, 0, 4), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(34)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(0, -1, 4), ISocket.class,
                new ItemStack(IUItem.heat_reactor.getItem(30)),
                Direction.SOUTH
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(1, 0, 1), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(14)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(1, 1, 1), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(14)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(1, 2, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(1, 3, 1), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(22)),
                Direction.SOUTH
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-1, 0, 1), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(14)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-1, 1, 1), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(14)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-1, 2, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-1, 3, 1), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(22)),
                Direction.SOUTH
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(0, 0, 2), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(14)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(0, 1, 2), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(14)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(0, 2, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(0, 3, 2), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(22)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(1, 0, 3), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(14)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(1, 1, 3), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(14)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(1, 2, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(1, 3, 3), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(22)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-1, 0, 3), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(14)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-1, 1, 3), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(14)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-1, 2, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-1, 3, 3), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(22)),
                Direction.SOUTH
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(0, 1, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(0, 2, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );

        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-1, 0, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-1, 1, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(-1, 2, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(1, 0, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(1, 1, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(1, 2, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(0, 0, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(0, 1, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(0, 2, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(10)),
                Direction.SOUTH
        );
        for (int x = -2; x < 3; x++) {
            for (int y = -1; y < 4; y++) {
                for (int z = 0; z < 5; z++) {
                    impHeatReactorMultiBlock.add(impHeatReactorMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.heat_reactor.getItem(26)),
                            Direction.SOUTH
                    );
                }
            }
        }
    }

}
