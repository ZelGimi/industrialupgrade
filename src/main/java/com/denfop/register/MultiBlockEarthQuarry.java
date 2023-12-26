package com.denfop.register;

import com.denfop.IUItem;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.tiles.quarry_earth.IAnalyzer;
import com.denfop.tiles.quarry_earth.ICasing;
import com.denfop.tiles.quarry_earth.IEarthChest;
import com.denfop.tiles.quarry_earth.IEarthQuarry;
import com.denfop.tiles.quarry_earth.IRigDrill;
import com.denfop.tiles.quarry_earth.ITransport;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.EarthQuarryMultiBlock;

public class MultiBlockEarthQuarry {

    public static void init() {
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos(), IEarthQuarry.class,
                new ItemStack(IUItem.earthQuarry,1),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(0,-1,0), IAnalyzer.class,
                new ItemStack(IUItem.earthQuarry,1,1),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(0,-1,2), ITransport.class,
                new ItemStack(IUItem.earthQuarry,1,5),
                EnumFacing.SOUTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(1,-1,2), IEarthChest.class,
                new ItemStack(IUItem.earthQuarry,1,4),
                EnumFacing.SOUTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-1,-1,2), IEarthChest.class,
                new ItemStack(IUItem.earthQuarry,1,4),
                EnumFacing.SOUTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(0,-1,1), ITransport.class,
                new ItemStack(IUItem.earthQuarry,1,5),
                EnumFacing.SOUTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(0,-2,1), ITransport.class,
                new ItemStack(IUItem.earthQuarry,1,5),
                EnumFacing.SOUTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(0,-3,1), ITransport.class,
                new ItemStack(IUItem.earthQuarry,1,5),
                EnumFacing.SOUTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(0,-4,1), IRigDrill.class,
                new ItemStack(IUItem.earthQuarry,1,3),
                EnumFacing.SOUTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(0,1,0), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );

        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-1,0,0), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(1,0,0), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-1,0,1), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(1,0,1), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-1,0,2), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(1,0,2), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(0,0,1), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(0,0,2), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(0,1,1), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(0,1,2), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );

        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-1,-1,0), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(1,-1,0), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-1,-1,1), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(1,-1,1), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-1,-1,2), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(1,-1,2), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );


        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-2,-1,0), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(2,-1,0), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-2,-1,1), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(2,-1,1), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-2,-1,2), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(2,-1,2), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );

        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(0,-2,0), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(0,-3,0), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );

        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(1,-2,1), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(1,-3,1), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );

        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-1,-2,1), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-1,-3,1), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(0,-2,2), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(0,-3,2), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );


        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-2,-2,0), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-2,-3,0), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(2,-2,0), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(2,-3,0), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );


        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-2,-2,2), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(-2,-3,2), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(2,-2,2), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
        EarthQuarryMultiBlock.add(EarthQuarryMultiBlock.getPos().add(2,-3,2), ICasing.class,
                new ItemStack(IUItem.earthQuarry,1,2),
                EnumFacing.NORTH
        );
    }

}
