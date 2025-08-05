package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IFluidReactor;
import com.denfop.tiles.reactors.water.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.ImpWaterReactorMultiBlock;

public class MultiBlockImpWaterReactor {

    public static void init() {
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos(), IFluidReactor.class,
                new ItemStack(IUItem.water_reactors_component.getItem(10)),
                Direction.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-1, 0, 0), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(34)),
                Direction.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(1, 0, 0), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(34)),
                Direction.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-1, 1, 0), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(34)),
                Direction.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(1, 1, 0), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(34)),
                Direction.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, 1, 2), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(6)),
                Direction.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(6)),
                Direction.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, 0, 3), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(6)),
                Direction.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, 0, 2), IReactor.class,
                new ItemStack(IUItem.water_reactors_component.getItem(38)),
                Direction.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(1, 0, 2), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(6)),
                Direction.NORTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-1, 0, 2), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(6)),
                Direction.NORTH
        );


        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, 0, 1), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(22)),
                Direction.WEST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, 1, 1), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(22)),
                Direction.WEST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, 1, 2), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(22)),
                Direction.WEST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, 0, 2), ILevelFuel.class,
                new ItemStack(IUItem.water_reactors_component.getItem(18)),
                Direction.WEST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, 0, 3), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(22)),
                Direction.WEST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, 1, 3), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(22)),
                Direction.WEST
        );

        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, 0, 1), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(14)),
                Direction.EAST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, 1, 1), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(14)),
                Direction.EAST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, 1, 2), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(14)),
                Direction.EAST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, 0, 3), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(14)),
                Direction.EAST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, 1, 3), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(14)),
                Direction.EAST
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, 0, 2), ILevelFuel.class,
                new ItemStack(IUItem.water_reactors_component.getItem(18)),
                Direction.EAST
        );

        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(1, 0, 4), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(34)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(1, 1, 4), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(34)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-1, 1, 4), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(34)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, 1, 4), ISecurity.class,
                new ItemStack(IUItem.water_reactors_component.getItem(26)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, 0, 4), ILevelFuel.class,
                new ItemStack(IUItem.water_reactors_component.getItem(18)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, -1, 4), ISocket.class,
                new ItemStack(IUItem.water_reactors_component.getItem(30)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-1, 0, 4), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(34)),
                Direction.SOUTH
        );

        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, 0, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, 0, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, 1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, 1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, -2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, -2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(1, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-1, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, 1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, 2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-1, 2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(1, 2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );

        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, 2, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-1, 2, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(1, 2, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, 2, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-1, 2, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(1, 2, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, 2, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-1, 2, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(1, 2, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, 2, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-1, 2, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(1, 2, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );

        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, -1, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, -1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, -1, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, -1, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, -2, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, 0, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(2, 1, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, -1, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, -1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, -1, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, -1, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, -2, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, 0, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-2, 1, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );

        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-1, -1, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(1, -1, 4), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );


        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, -1, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-1, -1, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(1, -1, 3), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );

        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, -1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-1, -1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(1, -1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(0, -1, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(-1, -1, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
        ImpWaterReactorMultiBlock.add(ImpWaterReactorMultiBlock.getPos().offset(1, -1, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(2)),
                Direction.SOUTH
        );
    }

}
