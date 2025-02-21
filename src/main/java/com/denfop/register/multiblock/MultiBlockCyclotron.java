package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.cyclotron.IBombardmentChamber;
import com.denfop.tiles.cyclotron.ICasing;
import com.denfop.tiles.cyclotron.IController;
import com.denfop.tiles.cyclotron.ICoolant;
import com.denfop.tiles.cyclotron.ICryogen;
import com.denfop.tiles.cyclotron.IElectrostaticDeflector;
import com.denfop.tiles.cyclotron.IParticleAccelerator;
import com.denfop.tiles.cyclotron.IPositrons;
import com.denfop.tiles.cyclotron.IQuantum;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.CyclotronMultiBlock;

public class MultiBlockCyclotron {

    public static void init() {
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.cyclotron, 1, 0),
                EnumFacing.NORTH
        );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().down(), IBombardmentChamber.class,
                new ItemStack(IUItem.cyclotron, 1, 4),
                EnumFacing.NORTH
        );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().down().add(0, 0, 3), IParticleAccelerator.class,
                new ItemStack(IUItem.cyclotron, 1, 7),
                EnumFacing.NORTH
        );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().down().add(0, 0, 4), IParticleAccelerator.class,
                new ItemStack(IUItem.cyclotron, 1, 7),
                EnumFacing.NORTH
        );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().down().add(1, 0, 3), IQuantum.class,
                new ItemStack(IUItem.cyclotron, 1, 5),
                EnumFacing.WEST
        );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().down().add(-1, 0, 3), IPositrons.class,
                new ItemStack(IUItem.cyclotron, 1, 1),
                EnumFacing.EAST
        );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().down().add(1, 0, 4), ICryogen.class,
                new ItemStack(IUItem.cyclotron, 1, 2),
                EnumFacing.WEST
        );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().down().add(-1, 0, 4), ICoolant.class,
                new ItemStack(IUItem.cyclotron, 1, 3),
                EnumFacing.EAST
        );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().down().add(0, 0, 7), IElectrostaticDeflector.class,
                new ItemStack(IUItem.cyclotron, 1, 8),
                EnumFacing.SOUTH
        );
        for (int x = -1; x < 2; x++) {
            for (int z = 1; z < 7; z++) {
                CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().add(x, 0, z), ICasing.class,
                        new ItemStack(IUItem.cyclotron, 1, 6),
                        EnumFacing.NORTH
                );
            }
        }
        for (int x = -1; x < 2; x++) {
            for (int z = 0; z < 8; z++) {
                CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().add(x, -1, z), ICasing.class,
                        new ItemStack(IUItem.cyclotron, 1, 6),
                        EnumFacing.NORTH
                );
            }
        }

        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().add(0, 0, 7), ICasing.class,
                new ItemStack(IUItem.cyclotron, 1, 6),
                EnumFacing.NORTH
        );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().down().down().add(-1, 0, 7), ICasing.class,
                new ItemStack(IUItem.cyclotron, 1, 6),
                EnumFacing.NORTH
        );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().down().down().add(1, 0, 7), ICasing.class,
                new ItemStack(IUItem.cyclotron, 1, 6),
                EnumFacing.NORTH
        );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().down().down().add(-1, 0, 0), ICasing.class,
                new ItemStack(IUItem.cyclotron, 1, 6),
                EnumFacing.NORTH
        );
        CyclotronMultiBlock.add(CyclotronMultiBlock.getPos().down().down().add(1, 0, 0), ICasing.class,
                new ItemStack(IUItem.cyclotron, 1, 6),
                EnumFacing.NORTH
        );
    }

}
