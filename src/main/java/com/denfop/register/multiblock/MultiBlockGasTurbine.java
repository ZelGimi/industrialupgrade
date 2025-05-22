package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.gasturbine.IAirBearings;
import com.denfop.tiles.gasturbine.IBlower;
import com.denfop.tiles.gasturbine.ICasing;
import com.denfop.tiles.gasturbine.IController;
import com.denfop.tiles.gasturbine.IRecuperator;
import com.denfop.tiles.gasturbine.ISocket;
import com.denfop.tiles.gasturbine.ITank;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.GasTurbineMultiBlock;

public class MultiBlockGasTurbine {

    public static void init() {
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.gasTurbine, 1, 0),
                EnumFacing.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().add(0, 1, 0), ITank.class,
                new ItemStack(IUItem.gasTurbine, 1, 6),
                EnumFacing.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().add(0, -1, 0), ISocket.class,
                new ItemStack(IUItem.gasTurbine, 1, 1),
                EnumFacing.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().add(0, 0, 1), IBlower.class,
                new ItemStack(IUItem.gasTurbine, 1, 3),
                EnumFacing.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().add(0, 0, 2), IBlower.class,
                new ItemStack(IUItem.gasTurbine, 1, 3),
                EnumFacing.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().add(0, 1, 1), IRecuperator.class,
                new ItemStack(IUItem.gasTurbine, 1, 5),
                EnumFacing.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().add(0, 1, 2), IRecuperator.class,
                new ItemStack(IUItem.gasTurbine, 1, 5),
                EnumFacing.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().add(0, -1, 1), IRecuperator.class,
                new ItemStack(IUItem.gasTurbine, 1, 5),
                EnumFacing.NORTH
        );
        GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().add(0, -1, 2), IRecuperator.class,
                new ItemStack(IUItem.gasTurbine, 1, 5),
                EnumFacing.NORTH
        );
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().add(x, y, 3), IAirBearings.class,
                        new ItemStack(IUItem.gasTurbine, 1, 2),
                        EnumFacing.SOUTH
                );
            }
        }
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = 0; z < 4; z++) {
                    GasTurbineMultiBlock.add(GasTurbineMultiBlock.getPos().add(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gasTurbine, 1, 4),
                            EnumFacing.SOUTH
                    );
                }
            }
        }
    }

}
