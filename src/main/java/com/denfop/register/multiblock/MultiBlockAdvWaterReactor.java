package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IFluidReactor;
import com.denfop.tiles.reactors.water.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.AdvWaterReactorMultiBlock;

public class MultiBlockAdvWaterReactor {

    public static void init() {
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos(),IFluidReactor.class,
                new ItemStack(IUItem.water_reactors_component.getItem(9)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(0, 1, 0),ILevelFuel.class,
                new ItemStack(IUItem.water_reactors_component.getItem(17)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(0, 2, 0),ISecurity.class,
                new ItemStack(IUItem.water_reactors_component.getItem(25)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(0, 1, 1),IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(5)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(0, 2, 1),IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(5)),
                Direction.NORTH
        );

        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(0, 1, 2),IReactor.class,
                new ItemStack(IUItem.water_reactors_component.getItem(37)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(0, 2, 2),IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(5)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(0, 0, 3),ISocket.class,
                new ItemStack(IUItem.water_reactors_component.getItem(29)),
                Direction.SOUTH
        );

        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(0, 2, 3),ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(33)),
                Direction.SOUTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(0, 1, 3),ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(33)),
                Direction.SOUTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 2, 3),ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(33)),
                Direction.SOUTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 1, 3),ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(33)),
                Direction.SOUTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 2, 3),ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(33)),
                Direction.SOUTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 1, 3),ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(33)),
                Direction.SOUTH
        );


        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 1, 1),IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(13)),
                Direction.WEST
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 1, 2),IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(13)),
                Direction.WEST
        );

        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 1, 1),IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(21)),
                Direction.EAST
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 1, 2),IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(21)),
                Direction.EAST
        );

        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 0, 0),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 0, 0),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, -1, 0),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, -1, 0),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 1, 0),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 1, 0),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 3, 0),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(0, 3, 0),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 3, 0),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 3, 1),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(0, 3, 1),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 3, 1),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 3, 2),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(0, 3, 2),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 3, 2),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 3, 3),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(0, 3, 3),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 3, 3),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );

        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 1, 1),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 1, 2),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );


        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 2, 0),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 2, 0),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 2, 1),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 2, 1),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 2, 2),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 2, 2),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );

        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 0, 1),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 0, 1),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 0, 2),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 0, 2),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, 0, 3),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, 0, 3),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(-1, -1, 3),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(1, -1, 3),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(0, 0, 1),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
        AdvWaterReactorMultiBlock.add(AdvWaterReactorMultiBlock.getPos().offset(0, 0, 2),ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(1)),
                Direction.NORTH
        );
    }

}
