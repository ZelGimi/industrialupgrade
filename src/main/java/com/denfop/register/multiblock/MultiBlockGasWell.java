package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.gaswell.IAnalyzer;
import com.denfop.tiles.gaswell.ICasing;
import com.denfop.tiles.gaswell.IController;
import com.denfop.tiles.gaswell.IDrill;
import com.denfop.tiles.gaswell.ISocket;
import com.denfop.tiles.gaswell.ITank;
import com.denfop.tiles.gaswell.ITransport;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.GasWellMultiBlock;

public class MultiBlockGasWell {

    public static void init() {
        GasWellMultiBlock.add(GasWellMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.gas_well, 1, 0),
                EnumFacing.NORTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().add(0, 1, 0), IAnalyzer.class,
                new ItemStack(IUItem.gas_well, 1, 2),
                EnumFacing.NORTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().add(0, 0, 2), ITank.class,
                new ItemStack(IUItem.gas_well, 1, 6),
                EnumFacing.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().add(0, 0, 1), ITransport.class,
                new ItemStack(IUItem.gas_well, 1, 5),
                EnumFacing.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().add(0, -1, 1), ITransport.class,
                new ItemStack(IUItem.gas_well, 1, 5),
                EnumFacing.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().add(0, -1, 2), ISocket.class,
                new ItemStack(IUItem.gas_well, 1, 1),
                EnumFacing.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().add(0, -2, 1), IDrill.class,
                new ItemStack(IUItem.gas_well, 1, 3),
                EnumFacing.SOUTH
        );
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = 0; z < 3; z++) {
                    GasWellMultiBlock.add(GasWellMultiBlock.getPos().add(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_well, 1, 4),
                            EnumFacing.SOUTH
                    );
                }
            }
        }

        GasWellMultiBlock.add(GasWellMultiBlock.getPos().add(0, 2, 0), ICasing.class,
                new ItemStack(IUItem.gas_well, 1, 4),
                EnumFacing.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().add(0, 2, 1), ICasing.class,
                new ItemStack(IUItem.gas_well, 1, 4),
                EnumFacing.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().add(-1, 2, 1), ICasing.class,
                new ItemStack(IUItem.gas_well, 1, 4),
                EnumFacing.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().add(1, 2, 1), ICasing.class,
                new ItemStack(IUItem.gas_well, 1, 4),
                EnumFacing.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().add(0, 3, 1), ICasing.class,
                new ItemStack(IUItem.gas_well, 1, 4),
                EnumFacing.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().add(0, 2, 2), ICasing.class,
                new ItemStack(IUItem.gas_well, 1, 4),
                EnumFacing.SOUTH
        );
    }

}
