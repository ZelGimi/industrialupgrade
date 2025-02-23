package com.denfop.register.multiblock;

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

import static com.denfop.register.InitMultiBlockSystem.perHeatReactorMultiBlock;

public class MultiBlockPerHeatReactor {

    public static void init() {
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos(), IHeatReactor.class,
                new ItemStack(IUItem.heat_reactor, 1, 7),
                EnumFacing.NORTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 0, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor, 1, 19),
                EnumFacing.NORTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 1, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor, 1, 19),
                EnumFacing.NORTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 0, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor, 1, 19),
                EnumFacing.NORTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 1, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor, 1, 19),
                EnumFacing.NORTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 0, 0), ISocket.class,
                new ItemStack(IUItem.heat_reactor, 1, 31),
                EnumFacing.NORTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(2, 0, 1), IPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 3),
                EnumFacing.WEST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(2, 1, 1), IPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 3),
                EnumFacing.WEST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(2, 0, 2), IPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 3),
                EnumFacing.WEST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(2, 1, 2), IPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 3),
                EnumFacing.WEST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(2, 0, 3), IPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 3),
                EnumFacing.WEST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(2, 1, 3), IPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 3),
                EnumFacing.WEST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(2, 0, 4), IPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 3),
                EnumFacing.WEST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(2, 1, 4), IPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 3),
                EnumFacing.WEST
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-3, 0, 1), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 39),
                EnumFacing.EAST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-3, 1, 1), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 39),
                EnumFacing.EAST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-3, 0, 2), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 39),
                EnumFacing.EAST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-3, 1, 2), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 39),
                EnumFacing.EAST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-3, 0, 3), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 39),
                EnumFacing.EAST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-3, 1, 3), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 39),
                EnumFacing.EAST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-3, 0, 4), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 39),
                EnumFacing.EAST
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-3, 1, 4), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor, 1, 39),
                EnumFacing.EAST
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 0, 5), ITank.class,
                new ItemStack(IUItem.heat_reactor, 1, 35),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 1, 5), ITank.class,
                new ItemStack(IUItem.heat_reactor, 1, 35),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 0, 5), ITank.class,
                new ItemStack(IUItem.heat_reactor, 1, 35),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 1, 5), ITank.class,
                new ItemStack(IUItem.heat_reactor, 1, 35),
                EnumFacing.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 0, 5), ICoolant.class,
                new ItemStack(IUItem.heat_reactor, 1, 19),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 1, 5), ICoolant.class,
                new ItemStack(IUItem.heat_reactor, 1, 19),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 0, 5), ICoolant.class,
                new ItemStack(IUItem.heat_reactor, 1, 19),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 1, 5), ICoolant.class,
                new ItemStack(IUItem.heat_reactor, 1, 19),
                EnumFacing.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 0, 1), null,
                ItemStack.EMPTY,
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 1, 1), null,
                ItemStack.EMPTY,
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 2, 1), null,
                ItemStack.EMPTY,
                EnumFacing.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 0, 1), null,
                ItemStack.EMPTY,
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 1, 1), null,
                ItemStack.EMPTY,
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 2, 1), null,
                ItemStack.EMPTY,
                EnumFacing.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 0, 4), null,
                ItemStack.EMPTY,
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 1, 4), null,
                ItemStack.EMPTY,
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 2, 4), null,
                ItemStack.EMPTY,
                EnumFacing.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 0, 4), null,
                ItemStack.EMPTY,
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 1, 4), null,
                ItemStack.EMPTY,
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 2, 4), null,
                ItemStack.EMPTY,
                EnumFacing.SOUTH
        );


        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 1, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 2, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 3, 1), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor, 1, 23),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 0, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 1, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 2, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 3, 1), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor, 1, 23),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 0, 4), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 1, 4), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 2, 4), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 3, 4), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor, 1, 23),
                EnumFacing.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 0, 4), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 1, 4), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 2, 4), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 3, 4), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor, 1, 23),
                EnumFacing.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 0, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 1, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 2, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 3, 2), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor, 1, 23),
                EnumFacing.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 0, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 1, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 2, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(1, 3, 3), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor, 1, 23),
                EnumFacing.SOUTH
        );


        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 0, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 1, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 2, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 3, 2), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor, 1, 23),
                EnumFacing.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 0, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 1, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 2, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-2, 3, 3), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor, 1, 23),
                EnumFacing.SOUTH
        );


        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 0, 2), IReactor.class,
                new ItemStack(IUItem.heat_reactor, 1, 15),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 0, 3), IReactor.class,
                new ItemStack(IUItem.heat_reactor, 1, 15),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 0, 2), IReactor.class,
                new ItemStack(IUItem.heat_reactor, 1, 15),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 0, 3), IReactor.class,
                new ItemStack(IUItem.heat_reactor, 1, 15),
                EnumFacing.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 1, 2), IReactor.class,
                new ItemStack(IUItem.heat_reactor, 1, 15),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 1, 3), IReactor.class,
                new ItemStack(IUItem.heat_reactor, 1, 15),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 1, 2), IReactor.class,
                new ItemStack(IUItem.heat_reactor, 1, 15),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 1, 3), IReactor.class,
                new ItemStack(IUItem.heat_reactor, 1, 15),
                EnumFacing.SOUTH
        );

        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 2, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(0, 2, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 2, 2), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );
        perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(-1, 2, 3), IChamber.class,
                new ItemStack(IUItem.heat_reactor, 1, 11),
                EnumFacing.SOUTH
        );

        for (int x = -3; x < 3; x++) {
            for (int y = -1; y < 4; y++) {
                for (int z = 0; z < 6; z++) {
                    perHeatReactorMultiBlock.add(perHeatReactorMultiBlock.getPos().add(x, y, z), ICasing.class,
                            new ItemStack(IUItem.heat_reactor, 1, 27),
                            EnumFacing.SOUTH
                    );
                }
            }
        }
    }

}
