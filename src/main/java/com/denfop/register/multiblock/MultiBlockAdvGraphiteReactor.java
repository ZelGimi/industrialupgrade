package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.blockentity.reactors.graphite.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.advGraphiteReactorMultiBlock;

public class MultiBlockAdvGraphiteReactor {

    public static void init() {
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos(), IGraphiteReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(1)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(0, 1, 0), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor.getItem(9)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(1, 1, 0), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor.getItem(9)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(1, 0, 0), ISocket.class,
                new ItemStack(IUItem.graphite_reactor.getItem(33)),
                Direction.NORTH
        );

        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-2, 0, 1), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(29)),
                Direction.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-2, 1, 1), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(29)),
                Direction.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-2, 2, 1), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(29)),
                Direction.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-2, 0, 2), ICooling.class,
                new ItemStack(IUItem.graphite_reactor.getItem(37)),
                Direction.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-2, 1, 2), ICooling.class,
                new ItemStack(IUItem.graphite_reactor.getItem(37)),
                Direction.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-2, 2, 2), ICooling.class,
                new ItemStack(IUItem.graphite_reactor.getItem(37)),
                Direction.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-2, 0, 3), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(29)),
                Direction.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-2, 1, 3), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(29)),
                Direction.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-2, 2, 3), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(29)),
                Direction.EAST
        );

        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(0, 0, 4), ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(17)),
                Direction.SOUTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(1, 0, 4), ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(17)),
                Direction.SOUTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(0, 1, 4), ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(17)),
                Direction.SOUTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(1, 1, 4), ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(17)),
                Direction.SOUTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(1, 0, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(1, 1, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(1, 2, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(1, 3, 3), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(5)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(2, 0, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(2, 1, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(2, 2, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(2, 3, 2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(5)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(0, 0, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(0, 1, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(0, 2, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(0, 3, 2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(5)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(1, 0, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(1, 1, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(1, 2, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(1, 3, 1), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(5)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(1, 0, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(21)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(1, 1, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(21)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(1, 2, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(21)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-1, 0, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(21)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-1, 1, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(21)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-1, 2, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(21)),
                Direction.NORTH
        );

        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-1, 0, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-1, 1, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-1, 2, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-1, 3, 1), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(5)),
                Direction.NORTH
        );

        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-1, 0, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-1, 1, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-1, 2, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(13)),
                Direction.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-1, 3, 3), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(5)),
                Direction.NORTH
        );
        for (int x = -1; x < 3; x++) {
            for (int y = -1; y < 4; y++) {
                advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(x, y, 0), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(25)),
                        Direction.SOUTH
                );
            }
        }
        for (int x = -1; x < 3; x++) {
            for (int y = -1; y < 4; y++) {
                advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(x, y, 4), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(25)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int y = -1; y < 4; y++) {
                advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(-2, y, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(25)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int y = -1; y < 4; y++) {
                advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(3, y, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(25)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int x = -1; x < 3; x++) {
                advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(x, 3, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(25)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int x = -1; x < 3; x++) {
                advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().offset(x, -1, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(25)),
                        Direction.SOUTH
                );
            }
        }
    }

}
