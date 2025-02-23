package com.denfop.register;

import com.denfop.IUItem;
import com.denfop.api.reactors.IGasReactor;
import com.denfop.tiles.reactors.gas.ICasing;
import com.denfop.tiles.reactors.gas.ICell;
import com.denfop.tiles.reactors.gas.IChamber;
import com.denfop.tiles.reactors.gas.ICompressor;
import com.denfop.tiles.reactors.gas.ICoolant;
import com.denfop.tiles.reactors.gas.IInterCooler;
import com.denfop.tiles.reactors.gas.IRecirculationPump;
import com.denfop.tiles.reactors.gas.IRegenerator;
import com.denfop.tiles.reactors.gas.ISocket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.perGasReactorMultiBlock;

public class MultiBlockPerGasReactor {

    public static void init() {

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(0, 0, 0), IGasReactor.class,
                new ItemStack(IUItem.gas_reactor, 1, 27),
                EnumFacing.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(0, 1, 0), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor, 1, 11),
                EnumFacing.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(0, -1, 0), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 3),
                EnumFacing.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(4, 0, 1), ICompressor.class,
                new ItemStack(IUItem.gas_reactor, 1, 19),
                EnumFacing.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(4, 1, 1), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor, 1, 23),
                EnumFacing.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(-4, 0, 1), ICompressor.class,
                new ItemStack(IUItem.gas_reactor, 1, 19),
                EnumFacing.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(-4, 1, 1), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor, 1, 23),
                EnumFacing.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(5, 0, 2), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor, 1, 31),
                EnumFacing.WEST
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(5, 1, 2), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor, 1, 11),
                EnumFacing.WEST
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(-5, 0, 2), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor, 1, 31),
                EnumFacing.EAST
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(-5, 1, 2), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor, 1, 11),
                EnumFacing.EAST
        );


        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(4, 0, 3), ICoolant.class,
                new ItemStack(IUItem.gas_reactor, 1, 7),
                EnumFacing.SOUTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(4, 1, 3), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 3),
                EnumFacing.SOUTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(-4, 0, 3), ICoolant.class,
                new ItemStack(IUItem.gas_reactor, 1, 7),
                EnumFacing.SOUTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(-4, 1, 3), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 3),
                EnumFacing.SOUTH
        );


        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(1, 1, 2), ICompressor.class,
                new ItemStack(IUItem.gas_reactor, 1, 19),
                EnumFacing.WEST
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(-1, 1, 2), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor, 1, 31),
                EnumFacing.EAST
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(0, 1, 3), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor, 1, 23),
                EnumFacing.SOUTH
        );


        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(1, 1, 6), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 3),
                EnumFacing.WEST
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(1, 0, 6), ICoolant.class,
                new ItemStack(IUItem.gas_reactor, 1, 7),
                EnumFacing.WEST
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(-1, 1, 6), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor, 1, 11),
                EnumFacing.EAST
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(-1, 0, 6), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor, 1, 31),
                EnumFacing.EAST
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(0, 1, 7), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor, 1, 23),
                EnumFacing.SOUTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(0, 0, 7), ICompressor.class,
                new ItemStack(IUItem.gas_reactor, 1, 19),
                EnumFacing.SOUTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(0, 1, 1), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 39),
                EnumFacing.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 39),
                EnumFacing.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(0, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 39),
                EnumFacing.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(0, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 39),
                EnumFacing.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(4, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 39),
                EnumFacing.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(4, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 39),
                EnumFacing.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(-4, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 39),
                EnumFacing.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(-4, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 39),
                EnumFacing.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(0, 1, 6), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 39),
                EnumFacing.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(0, 0, 6), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 39),
                EnumFacing.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(0, -1, 3), ISocket.class,
                new ItemStack(IUItem.gas_reactor, 1, 35),
                EnumFacing.SOUTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(2, 0, 2), ICasing.class,
                new ItemStack(IUItem.gas_reactor, 1, 15),
                EnumFacing.SOUTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(-2, 0, 2), ICasing.class,
                new ItemStack(IUItem.gas_reactor, 1, 15),
                EnumFacing.SOUTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(0, 0, 4), ICasing.class,
                new ItemStack(IUItem.gas_reactor, 1, 15),
                EnumFacing.SOUTH
        );
        for (int x = -1; x < 2; x++) {
            for (int z = 0; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor, 1, 15),
                            EnumFacing.SOUTH
                    );
                }
            }
        }

        for (int x = -5; x < -2; x++) {
            for (int z = 1; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor, 1, 15),
                            EnumFacing.SOUTH
                    );
                }
            }
        }

        for (int x = 5; x > 2; x--) {
            for (int z = 1; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor, 1, 15),
                            EnumFacing.SOUTH
                    );
                }
            }
        }

        for (int x = -1; x < 2; x++) {
            for (int z = 5; z < 8; z++) {
                for (int y = -1; y < 3; y++) {
                    perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().add(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor, 1, 15),
                            EnumFacing.SOUTH
                    );
                }
            }
        }
    }

}
