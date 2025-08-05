package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.tiles.reactors.graphite.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.GraphiteReactorMultiBlock;

public class MultiBlockGraphiteReactor {

    public static void init() {
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos(), IGraphiteReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem()),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(-2, 1, 2), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(28)),
                Direction.EAST
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(0, 0, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(12)),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(0, 1, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(12)),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(0, 2, 3), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(4)),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(1, 0, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(12)),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(1, 1, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(12)),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(1, 2, 2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(4)),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(-1, 0, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(12)),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(-1, 1, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(12)),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(-1, 2, 2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(4)),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(12)),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(0, 1, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(12)),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(0, 2, 1), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(4)),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(0, 0, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(20)),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(0, 1, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(20)),
                Direction.NORTH
        );

        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(0, 1, 0), ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(16)),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(0, -1, 0), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor.getItem(8)),
                Direction.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(2, 0, 2), ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(16)),
                Direction.WEST
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(2, 1, 2), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(28)),
                Direction.WEST
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(2, -1, 2), ICooling.class,
                new ItemStack(IUItem.graphite_reactor.getItem(36)),
                Direction.WEST
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(-2, 0, 2), ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(16)),
                Direction.EAST
        );

        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(-2, -1, 2), ICooling.class,
                new ItemStack(IUItem.graphite_reactor.getItem(36)),
                Direction.EAST
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(0, 0, 4), ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(16)),
                Direction.SOUTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(0, 1, 4), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor.getItem(8)),
                Direction.SOUTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(0, -1, 4), ISocket.class,
                new ItemStack(IUItem.graphite_reactor.getItem(32)),
                Direction.SOUTH
        );

        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 3; y++) {
                GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(x, y, 0), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(24)),
                        Direction.SOUTH
                );
            }
        }
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 3; y++) {
                GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(x, y, 4), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(24)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int y = -1; y < 3; y++) {
                GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(-2, y, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(24)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int y = -1; y < 3; y++) {
                GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(2, y, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(24)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int x = -1; x < 2; x++) {
                GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(x, 2, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(24)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int x = -1; x < 2; x++) {
                GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().offset(x, -1, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(24)),
                        Direction.SOUTH
                );
            }
        }
    }

}
