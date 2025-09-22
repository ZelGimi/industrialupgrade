package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IFluidReactor;
import com.denfop.tiles.reactors.water.ICasing;
import com.denfop.tiles.reactors.water.IChamber;
import com.denfop.tiles.reactors.water.IInput;
import com.denfop.tiles.reactors.water.ILevelFuel;
import com.denfop.tiles.reactors.water.IOutput;
import com.denfop.tiles.reactors.water.IReactor;
import com.denfop.tiles.reactors.water.ISecurity;
import com.denfop.tiles.reactors.water.ISocket;
import com.denfop.tiles.reactors.water.ITank;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.ImpWaterReactorMultiBlock;

public class MultiBlockImpWaterReactor {

    public static void init() {
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos(), IFluidReactor.class,
                new ItemStack(IUItem.water_reactors_component, 1, 10),
                EnumFacing.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-1, 0, 0), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 34),
                EnumFacing.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(1, 0, 0), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 34),
                EnumFacing.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-1, 1, 0), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 34),
                EnumFacing.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(1, 1, 0), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 34),
                EnumFacing.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, 1, 2), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 6),
                EnumFacing.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 6),
                EnumFacing.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, 0, 3), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 6),
                EnumFacing.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, 0, 2), IReactor.class,
                new ItemStack(IUItem.water_reactors_component, 1, 38),
                EnumFacing.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(1, 0, 2), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 6),
                EnumFacing.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-1, 0, 2), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 6),
                EnumFacing.NORTH
        );


        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, 0, 1), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 22),
                EnumFacing.WEST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, 1, 1), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 22),
                EnumFacing.WEST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, 1, 2), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 22),
                EnumFacing.WEST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, 0, 2), ILevelFuel.class,
                new ItemStack(IUItem.water_reactors_component, 1, 18),
                EnumFacing.WEST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, 0, 3), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 22),
                EnumFacing.WEST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, 1, 3), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 22),
                EnumFacing.WEST
        );

        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, 0, 1), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 14),
                EnumFacing.EAST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, 1, 1), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 14),
                EnumFacing.EAST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, 1, 2), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 14),
                EnumFacing.EAST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, 0, 3), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 14),
                EnumFacing.EAST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, 1, 3), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 14),
                EnumFacing.EAST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, 0, 2), ILevelFuel.class,
                new ItemStack(IUItem.water_reactors_component, 1, 18),
                EnumFacing.EAST
        );

        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(1, 0, 4), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 34),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(1, 1, 4), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 34),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-1, 1, 4), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 34),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, 1, 4), ISecurity.class,
                new ItemStack(IUItem.water_reactors_component, 1, 26),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, 0, 4), ILevelFuel.class,
                new ItemStack(IUItem.water_reactors_component, 1, 18),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, -1, 4), ISocket.class,
                new ItemStack(IUItem.water_reactors_component, 1, 30),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-1, 0, 4), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 34),
                EnumFacing.SOUTH
        );

        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, 0, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, 0, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, 1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, 1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, -2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, -2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(1, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-1, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, 1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, 2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-1, 2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(1, 2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );

        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, 2, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-1, 2, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(1, 2, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, 2, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-1, 2, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(1, 2, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, 2, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-1, 2, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(1, 2, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, 2, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-1, 2, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(1, 2, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );

        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, -1, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, -1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, -1, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, -1, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, -2, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, 0, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(2, 1, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, -1, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, -1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, -1, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, -1, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, -2, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, 0, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-2, 1, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );

        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-1, -1, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(1, -1, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );


        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, -1, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-1, -1, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(1, -1, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );

        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, -1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-1, -1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(1, -1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(0, -1, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(-1, -1, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().add(1, -1, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 2),
                EnumFacing.SOUTH
        );
    }

}
