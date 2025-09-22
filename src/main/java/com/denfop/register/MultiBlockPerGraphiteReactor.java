package com.denfop.register;

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

import static com.denfop.register.InitMultiBlockSystem.perGraphiteReactorMultiBlock;

public class MultiBlockPerGraphiteReactor {

    public static void init() {

        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos(), IGraphiteReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 3),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 0, 4), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 1, 4), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 2, 4), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 3, 4), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 4, 4), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 7),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(2, 0, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(2, 1, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(2, 2, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(2, 3, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(2, 4, 3), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 7),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 0, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 1, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 2, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 3, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 4, 3), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 7),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 0, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 1, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 2, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 3, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 4, 2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 7),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 0, 3), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 1, 3), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 2, 3), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 3, 3), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 0, 3), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 1, 3), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 2, 3), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 3, 3), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 0, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 1, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 2, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 3, 2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 0, 4), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 1, 4), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 2, 4), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 3, 4), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 0, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 1, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 2, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 3, 2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 4, 2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 7),
                EnumFacing.NORTH
        );

        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 0, 4), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 1, 4), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 2, 4), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 3, 4), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 4, 4), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 7),
                EnumFacing.NORTH
        );

        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-2, 0, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-2, 1, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-2, 2, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-2, 3, 3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-2, 4, 3), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 7),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 1, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 2, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 3, 1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 4, 1), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 7),
                EnumFacing.NORTH
        );

        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 0, 5), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 1, 5), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 2, 5), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 3, 5), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 15),
                EnumFacing.NORTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 4, 5), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 7),
                EnumFacing.NORTH
        );

        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(3, 0, 2), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor, 1, 11),
                EnumFacing.WEST
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(3, 0, 4), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor, 1, 11),
                EnumFacing.WEST
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(3, 2, 2), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor, 1, 11),
                EnumFacing.WEST
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(3, 2, 4), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor, 1, 11),
                EnumFacing.WEST
        );

        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-3, 0, 2), ICooling.class,
                new ItemStack(IUItem.graphite_reactor, 1, 39),
                EnumFacing.EAST
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-3, 0, 4), ICooling.class,
                new ItemStack(IUItem.graphite_reactor, 1, 39),
                EnumFacing.EAST
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-3, 2, 2), ICooling.class,
                new ItemStack(IUItem.graphite_reactor, 1, 39),
                EnumFacing.EAST
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-3, 2, 4), ICooling.class,
                new ItemStack(IUItem.graphite_reactor, 1, 39),
                EnumFacing.EAST
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, -1, 0), ISocket.class,
                new ItemStack(IUItem.graphite_reactor, 1, 35),
                EnumFacing.NORTH
        );

        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 0, 6), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 19),
                EnumFacing.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 1, 6), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 19),
                EnumFacing.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 2, 6), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 19),
                EnumFacing.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(0, 3, 6), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 19),
                EnumFacing.SOUTH
        );


        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 0, 6), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 31),
                EnumFacing.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 1, 6), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 31),
                EnumFacing.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 2, 6), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 31),
                EnumFacing.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(1, 3, 6), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 31),
                EnumFacing.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 0, 6), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 31),
                EnumFacing.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 1, 6), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 31),
                EnumFacing.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 2, 6), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 31),
                EnumFacing.SOUTH
        );
        perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-1, 3, 6), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 31),
                EnumFacing.SOUTH
        );

        for (int x = -2; x < 3; x++) {
            for (int y = -1; y < 5; y++) {
                perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(x, y, 0), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 27),
                        EnumFacing.SOUTH
                );
            }
        }
        for (int x = -2; x < 3; x++) {
            for (int y = -1; y < 5; y++) {
                perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(x, y, 6), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 27),
                        EnumFacing.SOUTH
                );
            }
        }
        for (int z = 1; z < 6; z++) {
            for (int y = -1; y < 5; y++) {
                perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(-3, y, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 27),
                        EnumFacing.SOUTH
                );
            }
        }
        for (int z = 1; z < 6; z++) {
            for (int y = -1; y < 5; y++) {
                perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(3, y, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 27),
                        EnumFacing.SOUTH
                );
            }
        }
        for (int z = 1; z < 6; z++) {
            for (int x = -2; x < 3; x++) {
                perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(x, 4, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 27),
                        EnumFacing.SOUTH
                );
            }
        }
        for (int z = 1; z < 6; z++) {
            for (int x = -2; x < 3; x++) {
                perGraphiteReactorMultiBlock.add(perGraphiteReactorMultiBlock.getPos().add(x, -1, z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 27),
                        EnumFacing.SOUTH
                );
            }
        }
    }

}
