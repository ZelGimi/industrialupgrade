package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.blockentity.gaswell.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.GasWellMultiBlock;

public class MultiBlockGasWell {

    public static void init() {
        GasWellMultiBlock.add(GasWellMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.gas_well.getItem(0)),
                Direction.NORTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().offset(0, 1, 0), IAnalyzer.class,
                new ItemStack(IUItem.gas_well.getItem(2)),
                Direction.NORTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().offset(0, 0, 2), ITank.class,
                new ItemStack(IUItem.gas_well.getItem(6)),
                Direction.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().offset(0, 0, 1), ITransport.class,
                new ItemStack(IUItem.gas_well.getItem(5)),
                Direction.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().offset(0, -1, 1), ITransport.class,
                new ItemStack(IUItem.gas_well.getItem(5)),
                Direction.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().offset(0, -1, 2), ISocket.class,
                new ItemStack(IUItem.gas_well.getItem(1)),
                Direction.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().offset(0, -2, 1), IDrill.class,
                new ItemStack(IUItem.gas_well.getItem(3)),
                Direction.SOUTH
        );
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = 0; z < 3; z++) {
                    GasWellMultiBlock.add(GasWellMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.gas_well.getItem(4)),
                            Direction.SOUTH
                    );
                }
            }
        }

        GasWellMultiBlock.add(GasWellMultiBlock.getPos().offset(0, 2, 0), ICasing.class,
                new ItemStack(IUItem.gas_well.getItem(4)),
                Direction.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().offset(0, 2, 1), ICasing.class,
                new ItemStack(IUItem.gas_well.getItem(4)),
                Direction.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().offset(-1, 2, 1), ICasing.class,
                new ItemStack(IUItem.gas_well.getItem(4)),
                Direction.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().offset(1, 2, 1), ICasing.class,
                new ItemStack(IUItem.gas_well.getItem(4)),
                Direction.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().offset(0, 3, 1), ICasing.class,
                new ItemStack(IUItem.gas_well.getItem(4)),
                Direction.SOUTH
        );
        GasWellMultiBlock.add(GasWellMultiBlock.getPos().offset(0, 2, 2), ICasing.class,
                new ItemStack(IUItem.gas_well.getItem(4)),
                Direction.SOUTH
        );
    }

}
