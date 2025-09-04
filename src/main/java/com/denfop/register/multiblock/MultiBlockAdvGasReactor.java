package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IGasReactor;
import com.denfop.blockentity.reactors.gas.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.advGasReactorMultiBlock;

public class MultiBlockAdvGasReactor {

    public static void init() {
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos(), IGasReactor.class,
                new ItemStack(IUItem.gas_reactor.getItem(25)),
                Direction.NORTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(0, 1, 0), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor.getItem(29)),
                Direction.NORTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(1, 0, 2), ICompressor.class,
                new ItemStack(IUItem.gas_reactor.getItem(17)),
                Direction.WEST
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(1, 1, 2), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor.getItem(9)),
                Direction.WEST
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(1, 1, 3), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(1)),
                Direction.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(1, 0, 3), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(1)),
                Direction.SOUTH
        );

        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(0, 1, 3), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor.getItem(29)),
                Direction.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(0, 0, 3), ICoolant.class,
                new ItemStack(IUItem.gas_reactor.getItem(5)),
                Direction.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(0, -1, 3), ISocket.class,
                new ItemStack(IUItem.gas_reactor.getItem(33)),
                Direction.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(-1, 1, 3), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(1)),
                Direction.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(-1, 0, 3), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(1)),
                Direction.SOUTH
        );

        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(0, 1, 1), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(37)),
                Direction.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(37)),
                Direction.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(0, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(37)),
                Direction.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(0, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(37)),
                Direction.SOUTH
        );

        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(-4, 0, 1), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor.getItem(21)),
                Direction.NORTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(-4, 1, 1), ICompressor.class,
                new ItemStack(IUItem.gas_reactor.getItem(17)),
                Direction.NORTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(-4, 2, 1), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor.getItem(29)),
                Direction.NORTH
        );


        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(-4, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(37)),
                Direction.NORTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(-4, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(37)),
                Direction.NORTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(-4, 2, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(37)),
                Direction.NORTH
        );

        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(-4, 0, 3), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor.getItem(9)),
                Direction.SOUTH
        );
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(-4, 1, 3), ICoolant.class,
                new ItemStack(IUItem.gas_reactor.getItem(5)),
                Direction.SOUTH
        );

        for (int x = -1; x < 2; x++) {
            for (int z = 0; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor.getItem(13)),
                            Direction.SOUTH
                    );
                }
            }
        }
        advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(-2, 0, 2), ICasing.class,
                new ItemStack(IUItem.gas_reactor.getItem(13)),
                Direction.SOUTH
        );

        for (int x = -3; x > -6; x--) {
            for (int z = 1; z < 4; z++) {
                for (int y = -1; y < 4; y++) {
                    advGasReactorMultiBlock.add(advGasReactorMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor.getItem(13)),
                            Direction.SOUTH
                    );
                }
            }
        }
    }

}
