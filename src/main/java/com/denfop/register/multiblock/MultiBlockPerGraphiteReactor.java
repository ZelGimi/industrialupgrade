package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.tiles.reactors.graphite.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.perGraphiteReactorMultiBlock;

public class MultiBlockPerGraphiteReactor {

    public static void init() {

        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos(),IGraphiteReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(3)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 0, 4),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 1, 4),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 2, 4),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 3, 4),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 4, 4),IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(7)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(2, 0, 3),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(2, 1, 3),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(2, 2, 3),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(2, 3, 3),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(2, 4, 3),IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(7)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 0, 3),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 1, 3),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 2, 3),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 3, 3),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 4, 3),IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(7)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 0, 2),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 1, 2),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 2, 2),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 3, 2),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 4, 2),IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(7)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 0, 3),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 1, 3),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 2, 3),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 3, 3),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 0, 3),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 1, 3),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 2, 3),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 3, 3),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 0, 2),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 1, 2),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 2, 2),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 3, 2),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 0, 4),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 1, 4),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 2, 4),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 3, 4),IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(23)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 0, 2),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 1, 2),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 2, 2),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 3, 2),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 4, 2),IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(7)),
                Direction.NORTH
        );

        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 0, 4),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 1, 4),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 2, 4),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 3, 4),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 4, 4),IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(7)),
                Direction.NORTH
        );

        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-2, 0, 3),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-2, 1, 3),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-2, 2, 3),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-2, 3, 3),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-2, 4, 3),IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(7)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 0, 1),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 1, 1),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 2, 1),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 3, 1),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 4, 1),IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(7)),
                Direction.NORTH
        );

        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 0, 5),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 1, 5),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 2, 5),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 3, 5),IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(15)),
                Direction.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 4, 5),IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(7)),
                Direction.NORTH
        );

        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(3, 0, 2),IExchanger.class,
                new ItemStack(IUItem.graphite_reactor.getItem(11)),
                Direction.WEST
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(3, 0, 4),IExchanger.class,
                new ItemStack(IUItem.graphite_reactor.getItem(11)),
                Direction.WEST
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(3, 2, 2),IExchanger.class,
                new ItemStack(IUItem.graphite_reactor.getItem(11)),
                Direction.WEST
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(3, 2, 4),IExchanger.class,
                new ItemStack(IUItem.graphite_reactor.getItem(11)),
                Direction.WEST
        );

        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-3, 0, 2),ICooling.class,
                new ItemStack(IUItem.graphite_reactor.getItem(39)),
                Direction.EAST
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-3, 0, 4),ICooling.class,
                new ItemStack(IUItem.graphite_reactor.getItem(39)),
                Direction.EAST
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-3, 2, 2),ICooling.class,
                new ItemStack(IUItem.graphite_reactor.getItem(39)),
                Direction.EAST
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-3, 2, 4),ICooling.class,
                new ItemStack(IUItem.graphite_reactor.getItem(39)),
                Direction.EAST
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, -1, 0),ISocket.class,
                new ItemStack(IUItem.graphite_reactor.getItem(35)),
                Direction.NORTH
        );

        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 0, 6),ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(19)),
                Direction.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 1, 6),ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(19)),
                Direction.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 2, 6),ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(19)),
                Direction.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(0, 3, 6),ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(19)),
                Direction.SOUTH
        );


        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 0, 6),ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(31)),
                Direction.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 1, 6),ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(31)),
                Direction.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 2, 6),ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(31)),
                Direction.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(1, 3, 6),ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(31)),
                Direction.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 0, 6),ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(31)),
                Direction.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 1, 6),ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(31)),
                Direction.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 2, 6),ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(31)),
                Direction.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-1, 3, 6),ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(31)),
                Direction.SOUTH
        );

        for (int x = -2; x < 3; x++) {
            for (int y = -1; y < 5; y++) {
                perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(x, y, 0),ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(27)),
                        Direction.SOUTH
                );
            }
        }
        for (int x = -2; x < 3; x++) {
            for (int y = -1; y < 5; y++) {
                perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(x, y, 6),ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(27)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 6; z++) {
            for (int y = -1; y < 5; y++) {
                perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(-3, y, z),ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(27)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 6; z++) {
            for (int y = -1; y < 5; y++) {
                perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(3, y, z),ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(27)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 6; z++) {
            for (int x = -2; x < 3; x++) {
                perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(x, 4, z),ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(27)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 6; z++) {
            for (int x = -2; x < 3; x++) {
                perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().offset(x, -1, z),ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(27)),
                        Direction.SOUTH
                );
            }
        }
    }

}
