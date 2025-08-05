package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IGasReactor;
import com.denfop.tiles.reactors.gas.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.GasReactorMultiBlock;

public class MultiBlockGasReactor {

    public static void init() {
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos(), IGasReactor.class,
                new ItemStack(IUItem.gas_reactor.getItem(24)),
                Direction.NORTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(1, 0, 0), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(0)),
                Direction.NORTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(-1, 0, 0), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(0)),
                Direction.NORTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(1, 1, 0), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(0)),
                Direction.NORTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(-1, 1, 0), ICell.class,
                new ItemStack(IUItem.gas_reactor.getItem(0)),
                Direction.NORTH
        );

        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(0, 1, 1), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(36)),
                Direction.NORTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(36)),
                Direction.NORTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(0, 1, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(36)),
                Direction.NORTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(0, 0, 2), IChamber.class,
                new ItemStack(IUItem.gas_reactor.getItem(36)),
                Direction.NORTH
        );

        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(1, 0, 1), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor.getItem(28)),
                Direction.WEST
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(-1, 0, 1), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor.getItem(28)),
                Direction.EAST
        );

        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(1, 0, 2), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor.getItem(8)),
                Direction.WEST
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(-1, 0, 2), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor.getItem(8)),
                Direction.EAST
        );

        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(-1, 0, 3), ICompressor.class,
                new ItemStack(IUItem.gas_reactor.getItem(16)),
                Direction.SOUTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(1, 0, 3), ICompressor.class,
                new ItemStack(IUItem.gas_reactor.getItem(16)),
                Direction.SOUTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(-1, -1, 3), ICoolant.class,
                new ItemStack(IUItem.gas_reactor.getItem(4)),
                Direction.SOUTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(1, -1, 3), ICoolant.class,
                new ItemStack(IUItem.gas_reactor.getItem(4)),
                Direction.SOUTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(0, 0, 3), ICompressor.class,
                new ItemStack(IUItem.gas_reactor.getItem(16)),
                Direction.SOUTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(0, 1, 3), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor.getItem(20)),
                Direction.SOUTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(0, -1, 3), ISocket.class,
                new ItemStack(IUItem.gas_reactor.getItem(32)),
                Direction.SOUTH
        );
        for (int x = -1; x < 2; x++) {
            for (int z = 0; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_reactor.getItem(12)),
                            Direction.SOUTH
                    );
                }
            }
        }
    }

}
