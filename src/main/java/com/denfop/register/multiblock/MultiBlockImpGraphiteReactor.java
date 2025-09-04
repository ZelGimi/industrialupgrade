package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.blockentity.reactors.graphite.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.impGraphiteReactorMultiBlock;

public class MultiBlockImpGraphiteReactor {

    public static void init() {

        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos(), IGraphiteReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(2)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(0, 0, 4), ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(18)),
                Direction.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(0, 1, 4), ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(18)),
                Direction.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 0, 4), ICooling.class,
                new ItemStack(IUItem.graphite_reactor.getItem(38)),
                Direction.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 1, 4), ICooling.class,
                new ItemStack(IUItem.graphite_reactor.getItem(38)),
                Direction.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 0, 4), ICooling.class,
                new ItemStack(IUItem.graphite_reactor.getItem(38)),
                Direction.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 1, 4), ICooling.class,
                new ItemStack(IUItem.graphite_reactor.getItem(38)),
                Direction.SOUTH
        );

        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(0, 2, 4), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(30)),
                Direction.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(0, 3, 4), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(30)),
                Direction.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 2, 4), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(30)),
                Direction.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 3, 4), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(30)),
                Direction.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 2, 4), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(30)),
                Direction.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 3, 4), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(30)),
                Direction.SOUTH
        );


        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(0, 1, 0), ISocket.class,
                new ItemStack(IUItem.graphite_reactor.getItem(34)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-3, 0, 2), ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(18)),
                Direction.EAST
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-3, 1, 2), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor.getItem(10)),
                Direction.EAST
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-3, 2, 2), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor.getItem(10)),
                Direction.EAST
        );

        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-3, 3, 2), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(30)),
                Direction.EAST
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(3, 0, 2), ITank.class,
                new ItemStack(IUItem.graphite_reactor.getItem(18)),
                Direction.EAST
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(3, 1, 2), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor.getItem(10)),
                Direction.WEST
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(3, 2, 2), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor.getItem(10)),
                Direction.WEST
        );

        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(3, 3, 2), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(30)),
                Direction.EAST
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 0, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 1, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 2, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 3, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 4, 3), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(6)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(2, 0, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(2, 1, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(2, 2, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(2, 3, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(2, 4, 2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(6)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(0, 0, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(0, 1, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(0, 2, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(0, 3, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(0, 4, 2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(6)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 0, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 1, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 2, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 3, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 4, 1), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(6)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 0, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(22)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 1, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(22)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 2, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(22)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(1, 3, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(22)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 0, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(22)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 1, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(22)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 2, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(22)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 3, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor.getItem(22)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 0, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 1, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 2, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 3, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 4, 1), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(6)),
                Direction.NORTH
        );

        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 0, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 1, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 2, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 3, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-1, 4, 3), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(6)),
                Direction.NORTH
        );

        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-2, 0, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-2, 1, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-2, 2, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-2, 3, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor.getItem(14)),
                Direction.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-2, 4, 2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor.getItem(6)),
                Direction.NORTH
        );


        for (int x = -2; x < 3; x++) {
            for (int y = -1; y < 5; y++) {
                impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(x, y, 0), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(26)),
                        Direction.SOUTH
                );
            }
        }
        for (int x = -2; x < 3; x++) {
            for (int y = -1; y < 5; y++) {
                impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(x, y, 4), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(26)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int y = -1; y < 5; y++) {
                impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(-3, y, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(26)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int y = -1; y < 5; y++) {
                impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(3, y, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(26)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int x = -2; x < 3; x++) {
                impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(x, 4, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(26)),
                        Direction.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int x = -2; x < 3; x++) {
                impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().offset(x, -1, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor.getItem(26)),
                        Direction.SOUTH
                );
            }
        }
    }

}
