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

import static com.denfop.register.InitMultiBlockSystem.GasReactorMultiBlock;

public class MultiBlockGasReactor {

    public static void init() {
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos(), IGasReactor.class,
                new ItemStack(IUItem.gas_reactor, 1, 24),
                EnumFacing.NORTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(1,0,0), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 0),
                EnumFacing.NORTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(-1,0,0), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 0),
                EnumFacing.NORTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(1,1,0), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 0),
                EnumFacing.NORTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(-1,1,0), ICell.class,
                new ItemStack(IUItem.gas_reactor, 1, 0),
                EnumFacing.NORTH
        );

        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(0,1,1), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 36),
                EnumFacing.NORTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(0,0,1), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 36),
                EnumFacing.NORTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(0,1,2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 36),
                EnumFacing.NORTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(0,0,2), IChamber.class,
                new ItemStack(IUItem.gas_reactor, 1, 36),
                EnumFacing.NORTH
        );

        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(1,0,1), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor, 1, 28),
                EnumFacing.WEST
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(-1,0,1), IRecirculationPump.class,
                new ItemStack(IUItem.gas_reactor, 1, 28),
                EnumFacing.EAST
        );

        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(1,0,2), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor, 1, 8),
                EnumFacing.WEST
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(-1,0,2), IInterCooler.class,
                new ItemStack(IUItem.gas_reactor, 1, 8),
                EnumFacing.EAST
        );

        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(-1,0,3), ICompressor.class,
                new ItemStack(IUItem.gas_reactor, 1, 16),
                EnumFacing.SOUTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(1,0,3), ICompressor.class,
                new ItemStack(IUItem.gas_reactor, 1, 16),
                EnumFacing.SOUTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(-1,-1,3), ICoolant.class,
                new ItemStack(IUItem.gas_reactor, 1, 4),
                EnumFacing.SOUTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(1,-1,3), ICoolant.class,
                new ItemStack(IUItem.gas_reactor, 1, 4),
                EnumFacing.SOUTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(0,0,3), ICompressor.class,
                new ItemStack(IUItem.gas_reactor, 1, 16),
                EnumFacing.SOUTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(0,1,3), IRegenerator.class,
                new ItemStack(IUItem.gas_reactor, 1, 20),
                EnumFacing.SOUTH
        );
        GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(0,-1,3), ISocket.class,
                new ItemStack(IUItem.gas_reactor, 1, 32),
                EnumFacing.SOUTH
        );
        for(int x = -1; x <2;x++)
            for(int z = 0; z < 4;z++){
                for(int y = -1; y < 3;y++){
                GasReactorMultiBlock.add(GasReactorMultiBlock.getPos().add(x,y,z), ICasing.class,
                        new ItemStack(IUItem.gas_reactor, 1, 12),
                        EnumFacing.SOUTH
                );
            }
            }
    }

}
