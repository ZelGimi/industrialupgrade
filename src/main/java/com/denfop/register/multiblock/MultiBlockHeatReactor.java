package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IHeatReactor;
import com.denfop.tiles.reactors.heat.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.HeatReactorMultiBlock;

public class MultiBlockHeatReactor {

    public static void init() {
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos(), IHeatReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(4)),
                Direction.NORTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().offset(1, 0, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(16)),
                Direction.NORTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().offset(-1, 0, 0), ICoolant.class,
                new ItemStack(IUItem.heat_reactor.getItem(16)),
                Direction.NORTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().offset(1, 0, 1), IPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(), 1),
                Direction.WEST
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().offset(-1, 0, 1), ICirculationPump.class,
                new ItemStack(IUItem.heat_reactor.getItem(36)),
                Direction.EAST
        );

        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().offset(1, 0, 2), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(32)),
                Direction.SOUTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().offset(-1, 0, 2), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(32)),
                Direction.SOUTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().offset(1, 1, 2), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(32)),
                Direction.SOUTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().offset(-1, 1, 2), ITank.class,
                new ItemStack(IUItem.heat_reactor.getItem(32)),
                Direction.SOUTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().offset(0, 0, 2), ISocket.class,
                new ItemStack(IUItem.heat_reactor.getItem(28)),
                Direction.SOUTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().offset(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(8)),
                Direction.SOUTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().offset(0, 1, 1), IReactor.class,
                new ItemStack(IUItem.heat_reactor.getItem(12)),
                Direction.SOUTH
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().offset(0, 3, 1), IGraphiteController.class,
                new ItemStack(IUItem.heat_reactor.getItem(20)),
                Direction.UP
        );
        HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().offset(0, 2, 1), IChamber.class,
                new ItemStack(IUItem.heat_reactor.getItem(8)),
                Direction.SOUTH
        );
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 4; y++) {
                for (int z = 0; z < 3; z++) {
                    HeatReactorMultiBlock.add(HeatReactorMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.heat_reactor.getItem(24)),
                            Direction.SOUTH
                    );
                }
            }
        }
    }

}
