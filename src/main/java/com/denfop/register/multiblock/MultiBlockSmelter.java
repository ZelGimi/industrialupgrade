package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.smeltery.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.SmelterMultiBlock;

public class MultiBlockSmelter {

    public static void init() {
        SmelterMultiBlock.add(SmelterMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.smeltery.getItem(0)),
                Direction.NORTH
        );
        SmelterMultiBlock.add(SmelterMultiBlock.getPos().above(), IFurnace.class,
                new ItemStack(IUItem.smeltery.getItem(4)),
                Direction.NORTH
        );
        SmelterMultiBlock.add(SmelterMultiBlock.getPos().above().offset(-1, 0, 0), IFurnace.class,
                new ItemStack(IUItem.smeltery.getItem(4)),
                Direction.NORTH
        );
        SmelterMultiBlock.add(SmelterMultiBlock.getPos().above().offset(1, 0, 0), IFurnace.class,
                new ItemStack(IUItem.smeltery.getItem(4)),
                Direction.NORTH
        );
        SmelterMultiBlock.add(SmelterMultiBlock.getPos().offset(0, 2, 0), ICasting.class,
                new ItemStack(IUItem.smeltery.getItem(5)),
                Direction.NORTH
        );
        for (int x = -1; x < 2; x++) {
            for (int z = 1; z < 3; z++) {
                for (int y = 0; y < 3; y++) {
                    SmelterMultiBlock.add(SmelterMultiBlock.getPos().offset(x, y, z), ITank.class,
                            new ItemStack(IUItem.smeltery.getItem(3)),
                            Direction.NORTH
                    );
                }
            }
        }
        for (int x = -1; x < 2; x++) {
            SmelterMultiBlock.add(SmelterMultiBlock.getPos().offset(x, 0, 3), IFuelTank.class,
                    new ItemStack(IUItem.smeltery.getItem(2)),
                    Direction.SOUTH
            );
        }
        for (int x = -2; x < 3; x++) {
            for (int z = 0; z < 4; z++) {
                for (int y = -1; y < 4; y++) {
                    SmelterMultiBlock.add(SmelterMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.smeltery.getItem(1)),
                            Direction.NORTH
                    );
                }
            }
        }

    }

}
