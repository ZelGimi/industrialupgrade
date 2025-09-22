package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.tiles.reactors.graphite.ICapacitor;
import com.denfop.tiles.reactors.graphite.ICasing;
import com.denfop.tiles.reactors.graphite.IChamber;
import com.denfop.tiles.reactors.graphite.ICooling;
import com.denfop.tiles.reactors.graphite.IExchanger;
import com.denfop.tiles.reactors.graphite.IGraphiteController;
import com.denfop.tiles.reactors.graphite.IReactor;
import com.denfop.tiles.reactors.graphite.ISocket;
import com.denfop.tiles.reactors.graphite.ITank;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.impGraphiteReactorMultiBlock;

public class MultiBlockImpGraphiteReactor {

    public static void init() {

        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos(), IGraphiteReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 2),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(0, 0, 4), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 18),
                EnumFacing.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(0, 1, 4), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 18),
                EnumFacing.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 0, 4), ICooling.class,
                new ItemStack(IUItem.graphite_reactor, 1, 38),
                EnumFacing.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 1, 4), ICooling.class,
                new ItemStack(IUItem.graphite_reactor, 1, 38),
                EnumFacing.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 0, 4), ICooling.class,
                new ItemStack(IUItem.graphite_reactor, 1, 38),
                EnumFacing.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 1, 4), ICooling.class,
                new ItemStack(IUItem.graphite_reactor, 1, 38),
                EnumFacing.SOUTH
        );

        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(0, 2, 4), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 30),
                EnumFacing.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(0, 3, 4), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 30),
                EnumFacing.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 2, 4), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 30),
                EnumFacing.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 3, 4), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 30),
                EnumFacing.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 2, 4), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 30),
                EnumFacing.SOUTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 3, 4), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 30),
                EnumFacing.SOUTH
        );


        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(0, 1, 0), ISocket.class,
                new ItemStack(IUItem.graphite_reactor, 1, 34),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-3, 0, 2), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 18),
                EnumFacing.EAST
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-3, 1, 2), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor, 1, 10),
                EnumFacing.EAST
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-3, 2, 2), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor, 1, 10),
                EnumFacing.EAST
        );

        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-3, 3, 2), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 30),
                EnumFacing.EAST
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(3, 0, 2), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 18),
                EnumFacing.EAST
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(3, 1, 2), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor, 1, 10),
                EnumFacing.WEST
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(3, 2, 2), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor, 1, 10),
                EnumFacing.WEST
        );

        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(3, 3, 2), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 30),
                EnumFacing.EAST
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 0, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 1, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 2, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 3, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 4, 3), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 6),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(2, 0, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(2, 1, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(2, 2, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(2, 3, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(2, 4, 2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 6),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(0, 0, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(0, 1, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(0, 2, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(0, 3, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(0, 4, 2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 6),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 0, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 1, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 2, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 3, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 4, 1), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 6),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 0, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 22),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 1, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 22),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 2, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 22),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(1, 3, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 22),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 0, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 22),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 1, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 22),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 2, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 22),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 3, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 22),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 0, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 1, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 2, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 3, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 4, 1), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 6),
                EnumFacing.NORTH
        );

        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 0, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 1, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 2, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 3, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-1, 4, 3), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 6),
                EnumFacing.NORTH
        );

        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-2, 0, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-2, 1, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-2, 2, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-2, 3, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 14),
                EnumFacing.NORTH
        );
        impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-2, 4, 2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 6),
                EnumFacing.NORTH
        );


        for (int x = -2; x < 3; x++) {
            for (int y = -1; y < 5; y++) {
                impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(x, y, 0), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 26),
                        EnumFacing.SOUTH
                );
            }
        }
        for (int x = -2; x < 3; x++) {
            for (int y = -1; y < 5; y++) {
                impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(x, y, 4), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 26),
                        EnumFacing.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int y = -1; y < 5; y++) {
                impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(-3, y, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 26),
                        EnumFacing.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int y = -1; y < 5; y++) {
                impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(3, y, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 26),
                        EnumFacing.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int x = -2; x < 3; x++) {
                impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(x, 4, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 26),
                        EnumFacing.SOUTH
                );
            }
        }
        for (int z = 1; z < 4; z++) {
            for (int x = -2; x < 3; x++) {
                impGraphiteReactorMultiBlock.add(impGraphiteReactorMultiBlock.getPos().add(x, -1, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 26),
                        EnumFacing.SOUTH
                );
            }
        }
    }

}
