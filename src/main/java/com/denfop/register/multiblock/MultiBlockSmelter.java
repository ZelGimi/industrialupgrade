package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.smeltery.ICasing;
import com.denfop.tiles.smeltery.ICasting;
import com.denfop.tiles.smeltery.IController;
import com.denfop.tiles.smeltery.IFuelTank;
import com.denfop.tiles.smeltery.IFurnace;
import com.denfop.tiles.smeltery.ITank;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.SmelterMultiBlock;

public class MultiBlockSmelter {

    public static void init() {
        SmelterMultiBlock.add(SmelterMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.smeltery, 1, 0),
                EnumFacing.NORTH
        );
        SmelterMultiBlock.add(SmelterMultiBlock.getPos().up(), IFurnace.class,
                new ItemStack(IUItem.smeltery, 1, 4),
                EnumFacing.NORTH
        );
        SmelterMultiBlock.add(SmelterMultiBlock.getPos().up().add(-1, 0, 0), IFurnace.class,
                new ItemStack(IUItem.smeltery, 1, 4),
                EnumFacing.NORTH
        );
        SmelterMultiBlock.add(SmelterMultiBlock.getPos().up().add(1, 0, 0), IFurnace.class,
                new ItemStack(IUItem.smeltery, 1, 4),
                EnumFacing.NORTH
        );
        SmelterMultiBlock.add(SmelterMultiBlock.getPos().add(0, 2, 0), ICasting.class,
                new ItemStack(IUItem.smeltery, 1, 5),
                EnumFacing.NORTH
        );
        for (int x = -1; x < 2; x++) {
            for (int z = 1; z < 3; z++) {
                for (int y = 0; y < 3; y++) {
                    SmelterMultiBlock.add(SmelterMultiBlock.getPos().add(x, y, z), ITank.class,
                            new ItemStack(IUItem.smeltery, 1, 3),
                            EnumFacing.NORTH
                    );
                }
            }
        }
        for (int x = -1; x < 2; x++) {
            SmelterMultiBlock.add(SmelterMultiBlock.getPos().add(x, 0, 3), IFuelTank.class,
                    new ItemStack(IUItem.smeltery, 1, 2),
                    EnumFacing.SOUTH
            );
        }
        for (int x = -2; x < 3; x++) {
            for (int z = 0; z < 4; z++) {
                for (int y = -1; y < 4; y++) {
                    SmelterMultiBlock.add(SmelterMultiBlock.getPos().add(x, y, z), ICasing.class,
                            new ItemStack(IUItem.smeltery, 1, 1),
                            EnumFacing.NORTH
                    );
                }
            }
        }

    }

}
