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

import static com.denfop.register.InitMultiBlockSystem.WaterReactorMultiBlock;

public class MultiBlockWaterReactor {

    public static void init() {
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos(), IFluidReactor.class,
                new ItemStack(IUItem.water_reactors_component, 1, 8),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(-1, 0, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(-1, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(1, 0, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(1, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(-1, 0, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(1, 0, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(-1, 0, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(1, 0, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(-1, -1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(1, -1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(0, 0, 2), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 32),
                EnumFacing.SOUTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(0, 1, 2), ISocket.class,
                new ItemStack(IUItem.water_reactors_component, 1, 28),
                EnumFacing.SOUTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(0, 2, 2), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 32),
                EnumFacing.SOUTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(0, 3, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.SOUTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(0, -1, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 4),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(0, 1, 1), IReactor.class,
                new ItemStack(IUItem.water_reactors_component, 1, 36),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(0, 2, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 4),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(0, 3, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(0, 4, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(0, 1, 0), ILevelFuel.class,
                new ItemStack(IUItem.water_reactors_component, 1, 16),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(0, 2, 0), ISecurity.class,
                new ItemStack(IUItem.water_reactors_component, 1, 24),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(0, 3, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );

        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(-1, 1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(-1, 2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(1, 1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(1, 2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.NORTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(1, 1, 1), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 20),
                EnumFacing.WEST
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(1, 2, 1), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 32),
                EnumFacing.WEST
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(1, 3, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.WEST
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(-1, 1, 1), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 12),
                EnumFacing.EAST
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(-1, 2, 1), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 32),
                EnumFacing.EAST
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(-1, 3, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.WEST
        );

        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(-1, 1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.SOUTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(-1, 2, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.SOUTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(1, 1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.SOUTH
        );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().add(1, 2, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 0),
                EnumFacing.SOUTH
        );
    }

}
