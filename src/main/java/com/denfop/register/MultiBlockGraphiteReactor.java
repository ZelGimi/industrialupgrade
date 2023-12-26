package com.denfop.register;

import com.denfop.IUItem;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.tiles.reactors.graphite.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.GraphiteReactorMultiBlock;

public class MultiBlockGraphiteReactor {

    public static void init() {
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos(), IGraphiteReactor.class,
                new ItemStack(IUItem.graphite_reactor),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(-2,1,2), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 28),
                EnumFacing.EAST
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(0,0,3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 12),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(0,1,3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 12),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(0,2,3), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 4),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(1,0,2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 12),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(1,1,2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 12),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(1,2,2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 4),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(-1,0,2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 12),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(-1,1,2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 12),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(-1,2,2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 4),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(0,0,1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 12),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(0,1,1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 12),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(0,2,1), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 4),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(0,0,2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 20),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(0,1,2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 20),
                EnumFacing.NORTH
        );

        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(0,1,0), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 16),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(0,-1,0), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor, 1, 8),
                EnumFacing.NORTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(2,0,2), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 16),
                EnumFacing.WEST
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(2,1,2), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 28),
                EnumFacing.WEST
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(2,-1,2), ICooling.class,
                new ItemStack(IUItem.graphite_reactor, 1, 36),
                EnumFacing.WEST
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(-2,0,2), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 16),
                EnumFacing.EAST
        );

        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(-2,-1,2), ICooling.class,
                new ItemStack(IUItem.graphite_reactor, 1, 36),
                EnumFacing.EAST
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(0,0,4), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 16),
                EnumFacing.SOUTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(0,1,4), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor, 1, 8),
                EnumFacing.SOUTH
        );
        GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(0,-1,4), ISocket.class,
                new ItemStack(IUItem.graphite_reactor, 1, 32),
                EnumFacing.SOUTH
        );

        for(int x = -1; x < 2;x++){
            for(int y = -1; y < 3;y++){
                GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(x,y,0), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 24),
                        EnumFacing.SOUTH
                );
            }
        }
        for(int x = -1; x < 2;x++){
            for(int y = -1; y < 3;y++){
                GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(x,y,4), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 24),
                        EnumFacing.SOUTH
                );
            }
        }
        for(int z = 1; z < 4;z++){
            for(int y = -1; y < 3;y++){
                GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(-2,y,z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 24),
                        EnumFacing.SOUTH
                );
            }
        }
        for(int z = 1; z < 4;z++){
            for(int y = -1; y < 3;y++){
                GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(2,y,z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 24),
                        EnumFacing.SOUTH
                );
            }
        }
        for(int z = 1; z < 4;z++){
            for(int x = -1; x < 2;x++){
                GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(x,2,z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 24),
                        EnumFacing.SOUTH
                );
            }
        }
        for(int z = 1; z < 4;z++){
            for(int x = -1; x < 2;x++){
                GraphiteReactorMultiBlock.add(GraphiteReactorMultiBlock.getPos().add(x,-1,z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 24),
                        EnumFacing.SOUTH
                );
            }
        }
    }

}
