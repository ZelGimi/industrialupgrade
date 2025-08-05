package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IGasReactor;
import com.denfop.tiles.reactors.gas.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.perGasReactorMultiBlock;

public class MultiBlockPerGasReactor {

    public static void init() {

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(0, 0, 0), IGasReactor.class,
                new ItemStack(IUItem.gas_reactor.getItem(27)),
                Direction.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(0, 1, 0), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor.getItem(11)),
                Direction.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(0, -1, 0), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(3)),
                Direction.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(4, 0, 1), ICompressor.class,
                new ItemStack(IUItem.gas_reactor.getItem(19)),
                Direction.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(4, 1, 1), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor.getItem(23)),
                Direction.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(-4, 0, 1), ICompressor.class,
                new ItemStack(IUItem.gas_reactor.getItem(19)),
                Direction.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(-4, 1, 1), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor.getItem(23)),
                Direction.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(5, 0, 2), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor.getItem(31)),
                Direction.WEST
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(5, 1, 2), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor.getItem(11)),
                Direction.WEST
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(-5, 0, 2), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor.getItem(31)),
                Direction.EAST
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(-5, 1, 2), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor.getItem(11)),
                Direction.EAST
        );


        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(4, 0, 3), ICoolant.class,
                new ItemStack(IUItem.gas_reactor.getItem(7)),
                Direction.SOUTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(4, 1, 3), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(3)),
                Direction.SOUTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(-4, 0, 3), ICoolant.class,
                new ItemStack(IUItem.gas_reactor.getItem(7)),
                Direction.SOUTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(-4, 1, 3), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(3)),
                Direction.SOUTH
        );


        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(1, 1, 2), ICompressor.class,
                new ItemStack(IUItem.gas_reactor.getItem(19)),
                Direction.WEST
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(-1, 1, 2), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor.getItem(31)),
                Direction.EAST
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(0, 1, 3), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor.getItem(23)),
                Direction.SOUTH
        );


        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(1, 1, 6), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(3)),
                Direction.WEST
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(1, 0, 6), ICoolant.class,
                new ItemStack(IUItem.gas_reactor.getItem(7)),
                Direction.WEST
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(-1, 1, 6), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor.getItem(11)),
                Direction.EAST
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(-1, 0, 6), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor.getItem(31)),
                Direction.EAST
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(0, 1, 7), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor.getItem(23)),
                Direction.SOUTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(0, 0, 7), ICompressor.class,
                new ItemStack(IUItem.gas_reactor.getItem(19)),
                Direction.SOUTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(0, 1, 1), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(39)),
                Direction.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(39)),
                Direction.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(0, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(39)),
                Direction.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(0, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(39)),
                Direction.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(4, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(39)),
                Direction.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(4, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(39)),
                Direction.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(-4, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(39)),
                Direction.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(-4, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(39)),
                Direction.NORTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(0, 1, 6), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(39)),
                Direction.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(0, 0, 6), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(39)),
                Direction.NORTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(0, -1, 3), ISocket.class,
                new ItemStack(IUItem.gas_reactor.getItem(35)),
                Direction.SOUTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(2, 0, 2), ICasing.class,
                new ItemStack(IUItem.gas_reactor.getItem(15)),
                Direction.SOUTH
        );

        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(-2, 0, 2), ICasing.class,
                new ItemStack(IUItem.gas_reactor.getItem(15)),
                Direction.SOUTH
        );
        perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(0, 0, 4), ICasing.class,
                new ItemStack(IUItem.gas_reactor.getItem(15)),
                Direction.SOUTH
        );
        for (int x = -1; x < 2; x++) {
            for (int z = 0; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor.getItem(15)),
                            Direction.SOUTH
                    );
                }
            }
        }

        for (int x = -5; x < -2; x++) {
            for (int z = 1; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor.getItem(15)),
                            Direction.SOUTH
                    );
                }
            }
        }

        for (int x = 5; x > 2; x--) {
            for (int z = 1; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor.getItem(15)),
                            Direction.SOUTH
                    );
                }
            }
        }

        for (int x = -1; x < 2; x++) {
            for (int z = 5; z < 8; z++) {
                for (int y = -1; y < 3; y++) {
                    perGasReactorMultiBlock.add(perGasReactorMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor.getItem(15)),
                            Direction.SOUTH
                    );
                }
            }
        }
    }

}
