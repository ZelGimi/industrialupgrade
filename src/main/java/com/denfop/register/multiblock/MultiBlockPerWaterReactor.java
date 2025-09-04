package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.api.reactors.IFluidReactor;
import com.denfop.blockentity.reactors.water.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.PerWaterReactorMultiBlock;

public class MultiBlockPerWaterReactor {

    public static void init() {
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos(), IFluidReactor.class,
                new ItemStack(IUItem.water_reactors_component.getItem(11)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(1, 0, 0), ISocket.class,
                new ItemStack(IUItem.water_reactors_component.getItem(31)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-1, 0, 0), ILevelFuel.class,
                new ItemStack(IUItem.water_reactors_component.getItem(19)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-2, 0, 0), ISecurity.class,
                new ItemStack(IUItem.water_reactors_component.getItem(27)),
                Direction.NORTH
        );

        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-3, 0, 1), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(15)),
                Direction.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-3, 1, 1), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(15)),
                Direction.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-3, 2, 1), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(15)),
                Direction.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-3, 0, 2), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(15)),
                Direction.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-3, 1, 2), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(15)),
                Direction.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-3, 2, 2), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(15)),
                Direction.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-3, 0, 3), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(15)),
                Direction.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-3, 1, 3), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(15)),
                Direction.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-3, 2, 3), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(15)),
                Direction.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-3, 0, 4), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(15)),
                Direction.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-3, 1, 4), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(15)),
                Direction.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-3, 2, 4), IInput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(15)),
                Direction.EAST
        );


        ///
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(2, 0, 1), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(23)),
                Direction.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(2, 1, 1), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(23)),
                Direction.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(2, 2, 1), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(23)),
                Direction.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(2, 0, 2), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(23)),
                Direction.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(2, 1, 2), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(23)),
                Direction.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(2, 2, 2), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(23)),
                Direction.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(2, 0, 3), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(23)),
                Direction.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(2, 1, 3), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(23)),
                Direction.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(2, 2, 3), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(23)),
                Direction.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(2, 0, 4), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(23)),
                Direction.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(2, 1, 4), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(23)),
                Direction.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(2, 2, 4), IOutput.class,
                new ItemStack(IUItem.water_reactors_component.getItem(23)),
                Direction.WEST
        );

        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(0, 0, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(35)),
                Direction.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(0, 1, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(35)),
                Direction.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(0, 2, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(35)),
                Direction.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(1, 0, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(35)),
                Direction.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(1, 1, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(35)),
                Direction.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(1, 2, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(35)),
                Direction.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-1, 0, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(35)),
                Direction.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-1, 1, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(35)),
                Direction.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-1, 2, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(35)),
                Direction.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-2, 0, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(35)),
                Direction.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-2, 1, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(35)),
                Direction.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-2, 2, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component.getItem(35)),
                Direction.SOUTH
        );


        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(1, 0, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-1, 0, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-2, 0, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-2, 0, 2), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-2, 0, 3), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-2, 0, 4), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-1, 0, 4), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(0, 0, 4), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(1, 0, 4), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(1, 0, 3), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(1, 0, 2), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );

        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(0, 1, 2), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(0, 1, 3), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-1, 1, 2), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-1, 1, 3), IChamber.class,
                new ItemStack(IUItem.water_reactors_component.getItem(7)),
                Direction.NORTH
        );


        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(0, 0, 2), IReactor.class,
                new ItemStack(IUItem.water_reactors_component.getItem(39)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(0, 0, 3), IReactor.class,
                new ItemStack(IUItem.water_reactors_component.getItem(39)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-1, 0, 2), IReactor.class,
                new ItemStack(IUItem.water_reactors_component.getItem(39)),
                Direction.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(-1, 0, 3), IReactor.class,
                new ItemStack(IUItem.water_reactors_component.getItem(39)),
                Direction.NORTH
        );

        for (int x = 1; x > -3; x--) {
            for (int y = 1; y < 3; y++) {
                PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(x, y, 0), ICasing.class,
                        new ItemStack(IUItem.water_reactors_component.getItem(3)),
                        Direction.NORTH
                );
            }
        }
        for (int x = 2; x > -4; x--) {
            for (int z = 0; z < 6; z++) {
                PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(x, 3, z), ICasing.class,
                        new ItemStack(IUItem.water_reactors_component.getItem(3)),
                        Direction.NORTH
                );
            }
        }
        for (int x = 2; x > -4; x--) {
            for (int z = 0; z < 6; z++) {
                PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(x, -1, z), ICasing.class,
                        new ItemStack(IUItem.water_reactors_component.getItem(3)),
                        Direction.NORTH
                );
            }
        }
        for (int x = 2; x > -4; x--) {
            for (int z = 0; z < 6; z++) {
                PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(x, -1, z), ICasing.class,
                        new ItemStack(IUItem.water_reactors_component.getItem(3)),
                        Direction.NORTH
                );
            }
        }
        for (int x = 2; x > -4; x -= 5) {
            for (int z = 0; z < 6; z += 5) {
                PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(x, -2, z), ICasing.class,
                        new ItemStack(IUItem.water_reactors_component.getItem(3)),
                        Direction.NORTH
                );
            }
        }
        for (int x = 2; x > -4; x -= 5) {
            for (int z = 0; z < 6; z += 5) {
                for (int y = 0; y < 3; y += 1) {
                    PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().offset(x, y, z), ICasing.class,
                            new ItemStack(IUItem.water_reactors_component.getItem(3)),
                            Direction.NORTH
                    );
                }
            }
        }
    }

}
