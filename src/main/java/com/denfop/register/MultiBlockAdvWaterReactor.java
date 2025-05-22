package com.denfop.register;

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

import static com.denfop.register.InitMultiBlockSystem.AdvWaterReactorMultiBlock;

public class MultiBlockAdvWaterReactor {

    public static void init() {
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos(), IFluidReactor.class,
                new ItemStack(IUItem.water_reactors_component, 1, 9),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(0, 1, 0), ILevelFuel.class,
                new ItemStack(IUItem.water_reactors_component, 1, 17),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(0, 2, 0), ISecurity.class,
                new ItemStack(IUItem.water_reactors_component, 1, 25),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(0, 1, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 5),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(0, 2, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 5),
                EnumFacing.NORTH
        );

        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(0, 1, 2), IReactor.class,
                new ItemStack(IUItem.water_reactors_component, 1, 37),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(0, 2, 2), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 5),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(0, 0, 3), ISocket.class,
                new ItemStack(IUItem.water_reactors_component, 1, 29),
                EnumFacing.SOUTH
        );

        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(0, 2, 3), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 33),
                EnumFacing.SOUTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(0, 1, 3), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 33),
                EnumFacing.SOUTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 2, 3), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 33),
                EnumFacing.SOUTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 1, 3), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 33),
                EnumFacing.SOUTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 2, 3), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 33),
                EnumFacing.SOUTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 1, 3), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 33),
                EnumFacing.SOUTH
        );


        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 1, 1), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 13),
                EnumFacing.WEST
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 1, 2), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 13),
                EnumFacing.WEST
        );

        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 1, 1), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 21),
                EnumFacing.EAST
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 1, 2), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 21),
                EnumFacing.EAST
        );

        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 0, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 0, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 3, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(0, 3, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 3, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 3, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(0, 3, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 3, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 3, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(0, 3, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 3, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 3, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(0, 3, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 3, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );

        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 1, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );


        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 2, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 2, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 2, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 2, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );

        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 0, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 0, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 0, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 0, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, 0, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, 0, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(-1, -1, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(1, -1, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(0, 0, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().add(0, 0, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component, 1, 1),
                EnumFacing.NORTH
        );
    }

}
