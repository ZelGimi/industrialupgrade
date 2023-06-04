package com.denfop.register;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.multiblock.MultiBlockSystem;
import com.denfop.tiles.mechanism.blastfurnace.api.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class InitMultiBlockSystem {


    public static MultiBlockStructure blastFurnaceMultiBlock;

    public static void init() {
        new MultiBlockSystem();
        blastFurnaceMultiBlock =
                MultiBlockSystem.instance
                        .add("BlastFurnace")
                        .setMain(IBlastMain.class)
                        .setHasActivatedItem(true)
                        .setIgnoreMetadata(true)
                        .setActivateItem(
                                Ic2Items.ForgeHammer);

        blastFurnaceMultiBlock.add(blastFurnaceMultiBlock.getPos(), IBlastMain.class, new ItemStack(IUItem.blastfurnace, 1, 0),
                EnumFacing.NORTH
        );
        blastFurnaceMultiBlock.add(blastFurnaceMultiBlock.getPos().add(0, 1, 1), IBlastInputItem.class,
                new ItemStack(IUItem.blastfurnace, 1, 1)
        );
        blastFurnaceMultiBlock.add(blastFurnaceMultiBlock.getPos().add(0, 0, 2), IBlastInputFluid.class,
                new ItemStack(IUItem.blastfurnace, 1, 4), EnumFacing.SOUTH
        );
        blastFurnaceMultiBlock.add(blastFurnaceMultiBlock.getPos().add(-1, 0, 1), IBlastOutputItem.class,
                new ItemStack(IUItem.blastfurnace, 1, 3), EnumFacing.EAST
        );

        blastFurnaceMultiBlock.add(blastFurnaceMultiBlock.getPos().add(1, 0, 1), IBlastHeat.class,
                new ItemStack(IUItem.blastfurnace, 1, 2), EnumFacing.WEST
        );
        BlockPos pos1 = blastFurnaceMultiBlock.getPos().add(0, 0, 1);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    blastFurnaceMultiBlock.add(
                            pos1.add(i, j, k),
                            IOtherBlastPart.class,
                            new ItemStack(IUItem.blastfurnace, 1, 5)
                    );
                }
            }
        }
        blastFurnaceMultiBlock.addReport(IBlastInputItem.class, "industrialupgrade.blastfurnace.blast_furnace_input");
        blastFurnaceMultiBlock.addReport(IBlastInputFluid.class, "industrialupgrade.blastfurnace.blast_furnace_input_fluid");
        blastFurnaceMultiBlock.addReport(IBlastOutputItem.class, "industrialupgrade.blastfurnace.blast_furnace_output");
        blastFurnaceMultiBlock.addReport(IBlastHeat.class, "industrialupgrade.blastfurnace.blast_furnace_heat");
        blastFurnaceMultiBlock.addReport(IOtherBlastPart.class, "industrialupgrade.blastfurnace.blast_furnace_part");

    }

}
