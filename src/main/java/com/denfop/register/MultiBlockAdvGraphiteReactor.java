package com.denfop.register;

import com.denfop.IUItem;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.tiles.reactors.graphite.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.GraphiteReactorMultiBlock;
import static com.denfop.register.InitMultiBlockSystem.advGraphiteReactorMultiBlock;

public class MultiBlockAdvGraphiteReactor {

    public static void init() {
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos(), IGraphiteReactor.class,
                new ItemStack(IUItem.graphite_reactor,1,1),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(0,1,0), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor, 1, 9),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(1,1,0), IExchanger.class,
                new ItemStack(IUItem.graphite_reactor, 1, 9),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(1,0,0), ISocket.class,
                new ItemStack(IUItem.graphite_reactor, 1, 33),
                EnumFacing.NORTH
        );

        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-2,0,1), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 29),
                EnumFacing.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-2,1,1), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 29),
                EnumFacing.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-2,2,1), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 29),
                EnumFacing.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-2,0,2), ICooling.class,
                new ItemStack(IUItem.graphite_reactor, 1, 37),
                EnumFacing.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-2,1,2), ICooling.class,
                new ItemStack(IUItem.graphite_reactor, 1, 37),
                EnumFacing.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-2,2,2), ICooling.class,
                new ItemStack(IUItem.graphite_reactor, 1, 37),
                EnumFacing.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-2,0,3), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 29),
                EnumFacing.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-2,1,3), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 29),
                EnumFacing.EAST
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-2,2,3), ICapacitor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 29),
                EnumFacing.EAST
        );

        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(0,0,4), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 17),
                EnumFacing.SOUTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(1,0,4), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 17),
                EnumFacing.SOUTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(0,1,4), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 17),
                EnumFacing.SOUTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(1,1,4), ITank.class,
                new ItemStack(IUItem.graphite_reactor, 1, 17),
                EnumFacing.SOUTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(1,0,3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(1,1,3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(1,2,3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(1,3,3), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 5),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(2,0,2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(2,1,2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(2,2,2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(2,3,2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 5),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(0,0,2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(0,1,2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(0,2,2), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(0,3,2), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 5),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(1,0,1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(1,1,1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(1,2,1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(1,3,1), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 5),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(1,0,2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 21),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(1,1,2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 21),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(1,2,2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 21),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-1,0,2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 21),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-1,1,2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 21),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-1,2,2), IReactor.class,
                new ItemStack(IUItem.graphite_reactor, 1, 21),
                EnumFacing.NORTH
        );

        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-1,0,1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-1,1,1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-1,2,1), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-1,3,1), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 5),
                EnumFacing.NORTH
        );

        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-1,0,3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-1,1,3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-1,2,3), IChamber.class,
                new ItemStack(IUItem.graphite_reactor, 1, 13),
                EnumFacing.NORTH
        );
        advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-1,3,3), IGraphiteController.class,
                new ItemStack(IUItem.graphite_reactor, 1, 5),
                EnumFacing.NORTH
        );
        for(int x = -1; x < 3;x++){
            for(int y = -1; y < 4;y++){
                advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(x,y,0), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 25),
                        EnumFacing.SOUTH
                );
            }
        }
        for(int x = -1; x < 3;x++){
            for(int y = -1; y < 4;y++){
                advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(x,y,4), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 25),
                        EnumFacing.SOUTH
                );
            }
        }
        for(int z = 1; z < 4;z++){
            for(int y = -1; y < 4;y++){
                advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(-2,y,z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 25),
                        EnumFacing.SOUTH
                );
            }
        }
        for(int z = 1; z < 4;z++){
            for(int y = -1; y < 4;y++){
                advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(3,y,z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 25),
                        EnumFacing.SOUTH
                );
            }
        }
        for(int z = 1; z < 4;z++){
            for(int x = -1; x < 3;x++){
                advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(x,3,z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 25),
                        EnumFacing.SOUTH
                );
            }
        }
        for(int z = 1; z < 4;z++){
            for(int x = -1; x < 3;x++){
                advGraphiteReactorMultiBlock.add(advGraphiteReactorMultiBlock.getPos().add(x,-1,z), ICasing.class,
                        new ItemStack(IUItem.graphite_reactor, 1, 25),
                        EnumFacing.SOUTH
                );
            }
        }
    }

}
