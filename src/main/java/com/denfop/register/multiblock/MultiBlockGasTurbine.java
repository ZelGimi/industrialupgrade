package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.blockentity.gasturbine.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.GasTurbineMultiBlock;

public class MultiBlockGasTurbine {

    public static void init() {
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.gasTurbine.getItem(0)),
                Direction.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().offset(0, 1, 0), ITank.class,
                new ItemStack(IUItem.gasTurbine.getItem(6)),
                Direction.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().offset(0, -1, 0), ISocket.class,
                new ItemStack(IUItem.gasTurbine.getItem(1)),
                Direction.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().offset(0, 0, 1), IBlower.class,
                new ItemStack(IUItem.gasTurbine.getItem(3)),
                Direction.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().offset(0, 0, 2), IBlower.class,
                new ItemStack(IUItem.gasTurbine.getItem(3)),
                Direction.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().offset(0, 1, 1), IRecuperator.class,
                new ItemStack(IUItem.gasTurbine.getItem(5)),
                Direction.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().offset(0, 1, 2), IRecuperator.class,
                new ItemStack(IUItem.gasTurbine.getItem(5)),
                Direction.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().offset(0, -1, 1), IRecuperator.class,
                new ItemStack(IUItem.gasTurbine.getItem(5)),
                Direction.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().offset(0, -1, 2), IRecuperator.class,
                new ItemStack(IUItem.gasTurbine.getItem(5)),
                Direction.NORTH
        );
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().offset(x, y, 3), IAirBearings.class,
                        new ItemStack(IUItem.gasTurbine.getItem(2)),
                        Direction.SOUTH
                );
            }
        }
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = 0; z < 4; z++) {
                    GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gasTurbine.getItem(4)),
                            Direction.SOUTH
                    );
                }
            }
        }
    }

}
