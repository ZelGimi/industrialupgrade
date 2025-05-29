package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.cyclotron.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.CyclotronMultiBlock;

public class MultiBlockCyclotron {

    public static void init() {
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.cyclotron.getItem(0)),
                        Direction.NORTH
                );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().below(), IBombardmentChamber.class,
                new ItemStack(IUItem.cyclotron.getItem(4)),
                        Direction.NORTH
                );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().below().offset(0, 0, 3), IParticleAccelerator.class,
                new ItemStack(IUItem.cyclotron.getItem(7)),
                        Direction.NORTH
                );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().below().offset(0, 0, 4), IParticleAccelerator.class,
                new ItemStack(IUItem.cyclotron.getItem(7)),
                        Direction.NORTH
                );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().below().offset(1, 0, 3), IQuantum.class,
                new ItemStack(IUItem.cyclotron.getItem(5)),
                        Direction.WEST
                );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().below().offset(-1, 0, 3), IPositrons.class,
                new ItemStack(IUItem.cyclotron.getItem(1)),
                        Direction.EAST
                );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().below().offset(1, 0, 4), ICryogen.class,
                new ItemStack(IUItem.cyclotron.getItem(2)),
                        Direction.WEST
                );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().below().offset(-1, 0, 4), ICoolant.class,
                new ItemStack(IUItem.cyclotron.getItem(3)),
                        Direction.EAST
                );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().below().offset(0, 0, 7), IElectrostaticDeflector.class,
                new ItemStack(IUItem.cyclotron.getItem(8)),
                        Direction.SOUTH
                );
        for (int x = -1; x < 2; x++) {
            for (int z = 1; z < 7; z++) {
                CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().offset(x, 0, z), ICasing.class,
                        new ItemStack(IUItem.cyclotron.getItem(6)),
                                Direction.NORTH
                        );
            }
        }
        for (int x = -1; x < 2; x++) {
            for (int z = 0; z < 8; z++) {
                CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().offset(x, -1, z), ICasing.class,
                        new ItemStack(IUItem.cyclotron.getItem(6)),
                                Direction.NORTH
                        );
            }
        }

        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().offset(0, 0, 7), ICasing.class,
                new ItemStack(IUItem.cyclotron.getItem(6)),
                        Direction.NORTH
                );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().below().below().offset(-1, 0, 7), ICasing.class,
                new ItemStack(IUItem.cyclotron.getItem(6)),
                        Direction.NORTH
                );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().below().below().offset(1, 0, 7), ICasing.class,
                new ItemStack(IUItem.cyclotron.getItem(6)),
                        Direction.NORTH
                );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().below().below().offset(-1, 0, 0), ICasing.class,
                new ItemStack(IUItem.cyclotron.getItem(6)),
                        Direction.NORTH
                );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().below().below().offset(1, 0, 0), ICasing.class,
                new ItemStack(IUItem.cyclotron.getItem(6)),
                        Direction.NORTH
                );
    }

}
