package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.quarry_earth.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.EarthQuarryMultiBlock;

public class MultiBlockEarthQuarry {

    public static void init() {
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos(), IEarthQuarry.class,
                new ItemStack(IUItem.earthQuarry.getItem(0), 1),
                Direction.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(0, -1, 0), IAnalyzer.class,
                new ItemStack(IUItem.earthQuarry.getItem(1)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(0, -1, 2), ITransport.class,
                new ItemStack(IUItem.earthQuarry.getItem(5)),
                        Direction.SOUTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(1, -1, 2), IEarthChest.class,
                new ItemStack(IUItem.earthQuarry.getItem(4)),
                        Direction.SOUTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-1, -1, 2), IEarthChest.class,
                new ItemStack(IUItem.earthQuarry.getItem(4)),
                        Direction.SOUTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(0, -1, 1), ITransport.class,
                new ItemStack(IUItem.earthQuarry.getItem(5)),
                        Direction.SOUTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(0, -2, 1), ITransport.class,
                new ItemStack(IUItem.earthQuarry.getItem(5)),
                        Direction.SOUTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(0, -3, 1), ITransport.class,
                new ItemStack(IUItem.earthQuarry.getItem(5)),
                        Direction.SOUTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(0, -4, 1), IRigDrill.class,
                new ItemStack(IUItem.earthQuarry.getItem(3)),
                        Direction.SOUTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(0, 1, 0), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );

        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-1, 0, 0), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(1, 0, 0), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-1, 0, 1), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(1, 0, 1), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-1, 0, 2), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(1, 0, 2), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(0, 0, 1), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(0, 0, 2), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(0, 1, 1), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(0, 1, 2), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );

        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-1, -1, 0), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(1, -1, 0), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-1, -1, 1), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(1, -1, 1), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-1, -1, 2), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(1, -1, 2), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );


        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-2, -1, 0), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(2, -1, 0), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-2, -1, 1), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(2, -1, 1), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-2, -1, 2), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(2, -1, 2), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );

        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(0, -2, 0), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(0, -3, 0), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );

        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(1, -2, 1), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(1, -3, 1), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );

        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-1, -2, 1), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-1, -3, 1), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(0, -2, 2), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(0, -3, 2), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );


        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-2, -2, 0), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-2, -3, 0), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(2, -2, 0), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(2, -3, 0), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );


        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-2, -2, 2), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(-2, -3, 2), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(2, -2, 2), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().offset(2, -3, 2), ICasing.class,
                new ItemStack(IUItem.earthQuarry.getItem(2)),
                        Direction.NORTH
                );
    }

}
