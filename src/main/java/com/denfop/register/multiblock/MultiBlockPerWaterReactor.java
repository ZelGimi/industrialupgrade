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

import static com.denfop.register.InitMultiBlockSystem.PerWaterReactorMultiBlock;

public class MultiBlockPerWaterReactor {

    public static void init() {
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos(), IFluidReactor.class,
                new ItemStack(IUItem.water_reactors_component, 1, 11),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(1, 0, 0), ISocket.class,
                new ItemStack(IUItem.water_reactors_component, 1, 31),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-1, 0, 0), ILevelFuel.class,
                new ItemStack(IUItem.water_reactors_component, 1, 19),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-2, 0, 0), ISecurity.class,
                new ItemStack(IUItem.water_reactors_component, 1, 27),
                EnumFacing.NORTH
        );

        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-3, 0, 1), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 15),
                EnumFacing.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-3, 1, 1), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 15),
                EnumFacing.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-3, 2, 1), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 15),
                EnumFacing.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-3, 0, 2), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 15),
                EnumFacing.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-3, 1, 2), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 15),
                EnumFacing.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-3, 2, 2), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 15),
                EnumFacing.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-3, 0, 3), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 15),
                EnumFacing.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-3, 1, 3), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 15),
                EnumFacing.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-3, 2, 3), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 15),
                EnumFacing.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-3, 0, 4), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 15),
                EnumFacing.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-3, 1, 4), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 15),
                EnumFacing.EAST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-3, 2, 4), IInput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 15),
                EnumFacing.EAST
        );


        ///
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(2, 0, 1), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 23),
                EnumFacing.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(2, 1, 1), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 23),
                EnumFacing.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(2, 2, 1), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 23),
                EnumFacing.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(2, 0, 2), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 23),
                EnumFacing.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(2, 1, 2), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 23),
                EnumFacing.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(2, 2, 2), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 23),
                EnumFacing.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(2, 0, 3), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 23),
                EnumFacing.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(2, 1, 3), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 23),
                EnumFacing.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(2, 2, 3), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 23),
                EnumFacing.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(2, 0, 4), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 23),
                EnumFacing.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(2, 1, 4), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 23),
                EnumFacing.WEST
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(2, 2, 4), IOutput.class,
                new ItemStack(IUItem.water_reactors_component, 1, 23),
                EnumFacing.WEST
        );

        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(0, 0, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 35),
                EnumFacing.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(0, 1, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 35),
                EnumFacing.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(0, 2, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 35),
                EnumFacing.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(1, 0, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 35),
                EnumFacing.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(1, 1, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 35),
                EnumFacing.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(1, 2, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 35),
                EnumFacing.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-1, 0, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 35),
                EnumFacing.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-1, 1, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 35),
                EnumFacing.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-1, 2, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 35),
                EnumFacing.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-2, 0, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 35),
                EnumFacing.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-2, 1, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 35),
                EnumFacing.SOUTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-2, 2, 5), ITank.class,
                new ItemStack(IUItem.water_reactors_component, 1, 35),
                EnumFacing.SOUTH
        );


        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(0, 0, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(1, 0, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-1, 0, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-2, 0, 1), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-2, 0, 2), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-2, 0, 3), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-2, 0, 4), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-1, 0, 4), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(0, 0, 4), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(1, 0, 4), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(1, 0, 3), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(1, 0, 2), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );

        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(0, 1, 2), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(0, 1, 3), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-1, 1, 2), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-1, 1, 3), IChamber.class,
                new ItemStack(IUItem.water_reactors_component, 1, 7),
                EnumFacing.NORTH
        );


        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(0, 0, 2), IReactor.class,
                new ItemStack(IUItem.water_reactors_component, 1, 39),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(0, 0, 3), IReactor.class,
                new ItemStack(IUItem.water_reactors_component, 1, 39),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-1, 0, 2), IReactor.class,
                new ItemStack(IUItem.water_reactors_component, 1, 39),
                EnumFacing.NORTH
        );
        PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(-1, 0, 3), IReactor.class,
                new ItemStack(IUItem.water_reactors_component, 1, 39),
                EnumFacing.NORTH
        );

        for (int x = 1; x > -3; x--) {
            for (int y = 1; y < 3; y++) {
                PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(x, y, 0), ICasing.class,
                        new ItemStack(IUItem.water_reactors_component, 1, 3),
                        EnumFacing.NORTH
                );
            }
        }
        for (int x = 2; x > -4; x--) {
            for (int z = 0; z < 6; z++) {
                PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(x, 3, z), ICasing.class,
                        new ItemStack(IUItem.water_reactors_component, 1, 3),
                        EnumFacing.NORTH
                );
            }
        }
        for (int x = 2; x > -4; x--) {
            for (int z = 0; z < 6; z++) {
                PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(x, -1, z), ICasing.class,
                        new ItemStack(IUItem.water_reactors_component, 1, 3),
                        EnumFacing.NORTH
                );
            }
        }
        for (int x = 2; x > -4; x--) {
            for (int z = 0; z < 6; z++) {
                PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(x, -1, z), ICasing.class,
                        new ItemStack(IUItem.water_reactors_component, 1, 3),
                        EnumFacing.NORTH
                );
            }
        }
        for (int x = 2; x > -4; x -= 5) {
            for (int z = 0; z < 6; z += 5) {
                PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(x, -2, z), ICasing.class,
                        new ItemStack(IUItem.water_reactors_component, 1, 3),
                        EnumFacing.NORTH
                );
            }
        }
        for (int x = 2; x > -4; x -= 5) {
            for (int z = 0; z < 6; z += 5) {
                for (int y = 0; y < 3; y += 1) {
                    PerWaterReactorMultiBlock.add(PerWaterReactorMultiBlock.getPos().add(x, y, z), ICasing.class,
                            new ItemStack(IUItem.water_reactors_component, 1, 3),
                            EnumFacing.NORTH
                    );
                }
            }
        }
    }

}
