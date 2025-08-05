package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IGasReactor;
import com.denfop.tiles.reactors.gas.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.impGasReactorMultiBlock;

public class MultiBlockImpGasReactor {

    public static void init() {
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos(), IGasReactor.class,
                new ItemStack(IUItem.gas_reactor.getItem(26)),
                Direction.NORTH
        );
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(0, 1, 0), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor.getItem(30)),
                Direction.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(0, 1, 1), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(38)),
                Direction.NORTH
        );
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(38)),
                Direction.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(0, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(38)),
                Direction.NORTH
        );
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(0, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(38)),
                Direction.NORTH
        );
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(1, 1, 2), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor.getItem(10)),
                Direction.WEST
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(-1, 1, 2), ICoolant.class,
                new ItemStack(IUItem.gas_reactor.getItem(6)),
                Direction.EAST
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(0, 0, 3), ICompressor.class,
                new ItemStack(IUItem.gas_reactor.getItem(18)),
                Direction.SOUTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(0, 1, 3), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor.getItem(22)),
                Direction.SOUTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(0, -1, 3), ISocket.class,
                new ItemStack(IUItem.gas_reactor.getItem(34)),
                Direction.SOUTH
        );


        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(-4, 0, 3), ICompressor.class,
                new ItemStack(IUItem.gas_reactor.getItem(18)),
                Direction.SOUTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(-4, 1, 3), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor.getItem(22)),
                Direction.SOUTH
        );
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(4, 0, 3), ICompressor.class,
                new ItemStack(IUItem.gas_reactor.getItem(18)),
                Direction.SOUTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(4, 1, 3), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor.getItem(22)),
                Direction.SOUTH
        );


        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(4, 0, 1), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor.getItem(30)),
                Direction.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(4, 1, 1), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor.getItem(10)),
                Direction.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(-4, 0, 1), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor.getItem(30)),
                Direction.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(-4, 1, 1), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor.getItem(10)),
                Direction.NORTH
        );


        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(4, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(38)),
                Direction.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(4, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(38)),
                Direction.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(-4, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(38)),
                Direction.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(-4, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(38)),
                Direction.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(-5, 0, 2), ICoolant.class,
                new ItemStack(IUItem.gas_reactor.getItem(6)),
                Direction.EAST
        );
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(-5, 1, 2), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(2)),
                Direction.EAST
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(5, 0, 2), ICoolant.class,
                new ItemStack(IUItem.gas_reactor.getItem(6)),
                Direction.WEST
        );
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(5, 1, 2), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(2)),
                Direction.WEST
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(3, 1, 2), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(2)),
                Direction.WEST
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(-3, 1, 2), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(2)),
                Direction.EAST
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(2, 0, 2), ICasing.class,
                new ItemStack(IUItem.gas_reactor.getItem(14)),
                Direction.SOUTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(-2, 0, 2), ICasing.class,
                new ItemStack(IUItem.gas_reactor.getItem(14)),
                Direction.SOUTH
        );

        for (int x = -1; x < 2; x++) {
            for (int z = 0; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor.getItem(14)),
                            Direction.SOUTH
                    );
                }
            }
        }

        for (int x = -5; x < -2; x++) {
            for (int z = 1; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor.getItem(14)),
                            Direction.SOUTH
                    );
                }
            }
        }

        for (int x = 5; x > 2; x--) {
            for (int z = 1; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor.getItem(14)),
                            Direction.SOUTH
                    );
                }
            }
        }
    }

}
