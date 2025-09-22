package com.denfop.register.multiblock;

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

import static com.denfop.register.InitMultiBlockSystem.advGasReactorMultiBlock;

public class MultiBlockAdvGasReactor {

    public static void init() {
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos(), IGasReactor.class,
                new ItemStack(IUItem.gas_reactor, 1, 25),
                EnumFacing.NORTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(0, 1, 0), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor, 1, 29),
                EnumFacing.NORTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(1, 0, 2), ICompressor.class,
                new ItemStack(IUItem.gas_reactor, 1, 17),
                EnumFacing.WEST
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(1, 1, 2), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor, 1, 9),
                EnumFacing.WEST
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(1, 1, 3), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 1),
                EnumFacing.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(1, 0, 3), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 1),
                EnumFacing.SOUTH
        );

        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(0, 1, 3), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor, 1, 29),
                EnumFacing.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(0, 0, 3), ICoolant.class,
                new ItemStack(IUItem.gas_reactor, 1, 5),
                EnumFacing.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(0, -1, 3), ISocket.class,
                new ItemStack(IUItem.gas_reactor, 1, 33),
                EnumFacing.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(-1, 1, 3), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 1),
                EnumFacing.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(-1, 0, 3), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 1),
                EnumFacing.SOUTH
        );

        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(0, 1, 1), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 37),
                EnumFacing.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 37),
                EnumFacing.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(0, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 37),
                EnumFacing.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(0, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 37),
                EnumFacing.SOUTH
        );

        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(-4, 0, 1), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor, 1, 21),
                EnumFacing.NORTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(-4, 1, 1), ICompressor.class,
                new ItemStack(IUItem.gas_reactor, 1, 17),
                EnumFacing.NORTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(-4, 2, 1), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor, 1, 29),
                EnumFacing.NORTH
        );


        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(-4, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 37),
                EnumFacing.NORTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(-4, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 37),
                EnumFacing.NORTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(-4, 2, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 37),
                EnumFacing.NORTH
        );

        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(-4, 0, 3), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor, 1, 9),
                EnumFacing.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(-4, 1, 3), ICoolant.class,
                new ItemStack(IUItem.gas_reactor, 1, 5),
                EnumFacing.SOUTH
        );

        for (int x = -1; x < 2; x++) {
            for (int z = 0; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor, 1, 13),
                            EnumFacing.SOUTH
                    );
                }
            }
        }
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(-2, 0, 2), ICasing.class,
                new ItemStack(IUItem.gas_reactor, 1, 13),
                EnumFacing.SOUTH
        );

        for (int x = -3; x > -6; x--) {
            for (int z = 1; z < 4; z++) {
                for (int y = -1; y < 4; y++) {
                    advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().add(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor, 1, 13),
                            EnumFacing.SOUTH
                    );
                }
            }
        }
    }

}
