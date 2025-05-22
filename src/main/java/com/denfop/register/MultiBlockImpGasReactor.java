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

import static com.denfop.register.InitMultiBlockSystem.impGasReactorMultiBlock;

public class MultiBlockImpGasReactor {

    public static void init() {
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos(), IGasReactor.class,
                new ItemStack(IUItem.gas_reactor, 1, 26),
                EnumFacing.NORTH
        );
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(0, 1, 0), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor, 1, 30),
                EnumFacing.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(0, 1, 1), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 38),
                EnumFacing.NORTH
        );
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 38),
                EnumFacing.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(0, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 38),
                EnumFacing.NORTH
        );
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(0, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 38),
                EnumFacing.NORTH
        );
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(1, 1, 2), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor, 1, 10),
                EnumFacing.WEST
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(-1, 1, 2), ICoolant.class,
                new ItemStack(IUItem.gas_reactor, 1, 6),
                EnumFacing.EAST
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(0, 0, 3), ICompressor.class,
                new ItemStack(IUItem.gas_reactor, 1, 18),
                EnumFacing.SOUTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(0, 1, 3), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor, 1, 22),
                EnumFacing.SOUTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(0, -1, 3), ISocket.class,
                new ItemStack(IUItem.gas_reactor, 1, 34),
                EnumFacing.SOUTH
        );


        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(-4, 0, 3), ICompressor.class,
                new ItemStack(IUItem.gas_reactor, 1, 18),
                EnumFacing.SOUTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(-4, 1, 3), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor, 1, 22),
                EnumFacing.SOUTH
        );
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(4, 0, 3), ICompressor.class,
                new ItemStack(IUItem.gas_reactor, 1, 18),
                EnumFacing.SOUTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(4, 1, 3), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor, 1, 22),
                EnumFacing.SOUTH
        );


        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(4, 0, 1), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor, 1, 30),
                EnumFacing.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(4, 1, 1), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor, 1, 10),
                EnumFacing.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(-4, 0, 1), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor, 1, 30),
                EnumFacing.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(-4, 1, 1), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor, 1, 10),
                EnumFacing.NORTH
        );


        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(4, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 38),
                EnumFacing.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(4, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 38),
                EnumFacing.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(-4, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 38),
                EnumFacing.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(-4, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 38),
                EnumFacing.NORTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(-5, 0, 2), ICoolant.class,
                new ItemStack(IUItem.gas_reactor, 1, 6),
                EnumFacing.EAST
        );
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(-5, 1, 2), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 2),
                EnumFacing.EAST
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(5, 0, 2), ICoolant.class,
                new ItemStack(IUItem.gas_reactor, 1, 6),
                EnumFacing.WEST
        );
        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(5, 1, 2), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 2),
                EnumFacing.WEST
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(3, 1, 2), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 2),
                EnumFacing.WEST
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(-3, 1, 2), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 2),
                EnumFacing.EAST
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(2, 0, 2), ICasing.class,
                new ItemStack(IUItem.gas_reactor, 1, 14),
                EnumFacing.SOUTH
        );

        impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(-2, 0, 2), ICasing.class,
                new ItemStack(IUItem.gas_reactor, 1, 14),
                EnumFacing.SOUTH
        );

        for (int x = -1; x < 2; x++) {
            for (int z = 0; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor, 1, 14),
                            EnumFacing.SOUTH
                    );
                }
            }
        }

        for (int x = -5; x < -2; x++) {
            for (int z = 1; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor, 1, 14),
                            EnumFacing.SOUTH
                    );
                }
            }
        }

        for (int x = 5; x > 2; x--) {
            for (int z = 1; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    impGasReactorMultiBlock.add(impGasReactorMultiBlock.getPos().add(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor, 1, 14),
                            EnumFacing.SOUTH
                    );
                }
            }
        }
    }

}
