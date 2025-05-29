package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IFluidReactor;
import com.denfop.tiles.reactors.water.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.WaterReactorMultiBlock;

public class MultiBlockWaterReactor {

    public static void init() {
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos(), IFluidReactor.class,
                new ItemStack(IUItem.water_reactors_component.getItem(8)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(-1, 0, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(-1, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(1, 0, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(1, -1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(-1, 0, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(1, 0, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(-1, 0, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(1, 0, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(-1, -1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(1, -1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(0, 0, 2), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(32)),
                        Direction.SOUTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(0, 1, 2), ISocket.class,
                new ItemStack(IUItem.water_reactors_component.getItem(28)),
                        Direction.SOUTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(0, 2, 2), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(32)),
                        Direction.SOUTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(0, 3, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.SOUTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(0, -1, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(4)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(0, 1, 1), IReactor.class,
                new ItemStack(IUItem.water_reactors_component.getItem(36)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(0, 2, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(4)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(0, 3, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(0, 4, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(0, 1, 0), ILevelFuel.class,
                new ItemStack(IUItem.water_reactors_component.getItem(16)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(0, 2, 0), ISecurity.class,
                new ItemStack(IUItem.water_reactors_component.getItem(24)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(0, 3, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );

        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(-1, 1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(-1, 2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(1, 1, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(1, 2, 0), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.NORTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(1, 1, 1), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(20)),
                        Direction.WEST
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(1, 2, 1), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(32)),
                        Direction.WEST
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(1, 3, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.WEST
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(-1, 1, 1), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(12)),
                        Direction.EAST
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(-1, 2, 1), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(32)),
                        Direction.EAST
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(-1, 3, 1), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.WEST
                );

        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(-1, 1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.SOUTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(-1, 2, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.SOUTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(1, 1, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.SOUTH
                );
        WaterReactorMultiBlock.add(WaterReactorMultiBlock.getPos().offset(1, 2, 2), ICasing.class,
                new ItemStack(IUItem.water_reactors_component.getItem(0)),
                        Direction.SOUTH
                );
    }

}
