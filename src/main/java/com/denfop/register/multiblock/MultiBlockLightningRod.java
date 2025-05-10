package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.lightning_rod.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.LightningRodMultiBlock;

public class MultiBlockLightningRod {

    public static void init() {
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.lightning_rod.getItem(0)),
                        Direction.NORTH
                );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().above(), ICasing.class,
                new ItemStack(IUItem.lightning_rod.getItem(1)),
                        Direction.NORTH
                );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().offset(0, 2, 0), ICasing.class,
                new ItemStack(IUItem.lightning_rod.getItem(1)),
                        Direction.NORTH
                );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().offset(0, 3, 0), IGrounding.class,
                new ItemStack(IUItem.lightning_rod.getItem(2)),
                        Direction.NORTH
                );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().offset(0, 4, 0), IReceiver.class,
                new ItemStack(IUItem.lightning_rod.getItem(5)),
                        Direction.NORTH
                );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().offset(0, 5, 0), IPoles.class,
                new ItemStack(IUItem.lightning_rod.getItem(3)),
                        Direction.NORTH
                );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().offset(0, 6, 0), IPoles.class,
                new ItemStack(IUItem.lightning_rod.getItem(3)),
                        Direction.NORTH
                );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().offset(0, 7, 0), IPoles.class,
                new ItemStack(IUItem.lightning_rod.getItem(3)),
                        Direction.NORTH
                );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().offset(0, 8, 0), IPoles.class,
                new ItemStack(IUItem.lightning_rod.getItem(3)),
                        Direction.NORTH
                );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().offset(0, 9, 0), IAntennaMast.class,
                new ItemStack(IUItem.lightning_rod.getItem(4)),
                        Direction.NORTH
                );

    }

}
