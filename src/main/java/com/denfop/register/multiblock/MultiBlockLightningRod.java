package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.lightning_rod.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.LightningRodMultiBlock;

public class MultiBlockLightningRod {

    public static void init() {
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.lightning_rod, 1, 0),
                EnumFacing.NORTH
        );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().up(), ICasing.class,
                new ItemStack(IUItem.lightning_rod, 1, 1),
                EnumFacing.NORTH
        );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().add(0,2,0), ICasing.class,
                new ItemStack(IUItem.lightning_rod, 1, 1),
                EnumFacing.NORTH
        );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().add(0,3,0), IGrounding.class,
                new ItemStack(IUItem.lightning_rod, 1, 2),
                EnumFacing.NORTH
        );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().add(0,4,0), IReceiver.class,
                new ItemStack(IUItem.lightning_rod, 1, 5),
                EnumFacing.NORTH
        );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().add(0,5,0), IPoles.class,
                new ItemStack(IUItem.lightning_rod, 1, 3),
                EnumFacing.NORTH
        );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().add(0,6,0), IPoles.class,
                new ItemStack(IUItem.lightning_rod, 1, 3),
                EnumFacing.NORTH
        );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().add(0,7,0), IPoles.class,
                new ItemStack(IUItem.lightning_rod, 1, 3),
                EnumFacing.NORTH
        );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().add(0,8,0), IPoles.class,
                new ItemStack(IUItem.lightning_rod, 1, 3),
                EnumFacing.NORTH
        );
        LightningRodMultiBlock.add(LightningRodMultiBlock.getPos().add(0,9,0), IAntennaMast.class,
                new ItemStack(IUItem.lightning_rod, 1, 4),
                EnumFacing.NORTH
        );

    }
}
