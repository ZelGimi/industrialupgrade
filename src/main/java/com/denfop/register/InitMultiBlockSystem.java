package com.denfop.register;

import com.denfop.IUItem;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.multiblock.MultiBlockSystem;
import com.denfop.api.reactors.IFluidReactor;
import com.denfop.api.reactors.IGasReactor;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.api.reactors.IHeatReactor;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastHeat;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastInputFluid;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastInputItem;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastMain;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastOutputItem;
import com.denfop.tiles.mechanism.blastfurnace.api.IOtherBlastPart;
import com.denfop.tiles.quarry_earth.IEarthQuarry;
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
import net.minecraft.util.math.BlockPos;

public class InitMultiBlockSystem {


    public static MultiBlockStructure blastFurnaceMultiBlock;
    public static MultiBlockStructure WaterReactorMultiBlock;
    public static MultiBlockStructure AdvWaterReactorMultiBlock;
    public static MultiBlockStructure ImpWaterReactorMultiBlock;
    public static MultiBlockStructure PerWaterReactorMultiBlock;
    public  static MultiBlockStructure GasReactorMultiBlock;
    public static MultiBlockStructure advGasReactorMultiBlock;
    public static MultiBlockStructure impGasReactorMultiBlock;
    public static MultiBlockStructure perGasReactorMultiBlock;


    public  static MultiBlockStructure GraphiteReactorMultiBlock;
    public static MultiBlockStructure advGraphiteReactorMultiBlock;
    public static MultiBlockStructure impGraphiteReactorMultiBlock;
    public static MultiBlockStructure perGraphiteReactorMultiBlock;
    public static MultiBlockStructure HeatReactorMultiBlock;
    public static MultiBlockStructure advHeatReactorMultiBlock;
    public static MultiBlockStructure impHeatReactorMultiBlock;
    public static MultiBlockStructure perHeatReactorMultiBlock;
    public static MultiBlockStructure EarthQuarryMultiBlock;

    public static void init() {
        new MultiBlockSystem();
        blastFurnaceMultiBlock =
                MultiBlockSystem.instance
                        .add("BlastFurnace")
                        .setMain(IBlastMain.class)
                        .setHasActivatedItem(true)
                        .setIgnoreMetadata(true)
                        .setActivateItem(
                                new ItemStack(IUItem.ForgeHammer));

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



        WaterReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("WaterReactorMultiBlock")
                        .setMain(IFluidReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        AdvWaterReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("AdvWaterReactorMultiBlock")
                        .setMain(IFluidReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        ImpWaterReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("ImpWaterReactorMultiBlock")
                        .setMain(IFluidReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        PerWaterReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("PerWaterReactorMultiBlock")
                        .setMain(IFluidReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);


        MultiBlockWaterReactor.init();
        MultiBlockAdvWaterReactor.init();
        MultiBlockImpWaterReactor.init();
        MultiBlockPerWaterReactor.init();

        GasReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("GasReactorMultiBlock")
                        .setMain(IGasReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        advGasReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("AdvGasReactorMultiBlock")
                        .setMain(IGasReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        impGasReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("ImpGasReactorMultiBlock")
                        .setMain(IGasReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        perGasReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("PerGasReactorMultiBlock")
                        .setMain(IGasReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        MultiBlockGasReactor.init();
        MultiBlockAdvGasReactor.init();
        MultiBlockImpGasReactor.init();
        MultiBlockPerGasReactor.init();


        GraphiteReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("GraphiteReactorMultiBlock")
                        .setMain(IGraphiteReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        advGraphiteReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("advGraphiteReactorMultiBlock")
                        .setMain(IGraphiteReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        impGraphiteReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("impGraphiteReactorMultiBlock")
                        .setMain(IGraphiteReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        perGraphiteReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("perGraphiteReactorMultiBlock")
                        .setMain(IGraphiteReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        MultiBlockGraphiteReactor.init();
        MultiBlockAdvGraphiteReactor.init();
        MultiBlockImpGraphiteReactor.init();
        MultiBlockPerGraphiteReactor.init();


        HeatReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("HeatReactorMultiBlock")
                        .setMain(IHeatReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        advHeatReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("advHeatReactorMultiBlock")
                        .setMain(IHeatReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        impHeatReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("impHeatReactorMultiBlock")
                        .setMain(IHeatReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        perHeatReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("perHeatReactorMultiBlock")
                        .setMain(IHeatReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        MultiBlockHeatReactor.init();
        MultiBlockAdvHeatReactor.init();
        MultiBlockImpHeatReactor.init();
        MultiBlockPerHeatReactor.init();

        EarthQuarryMultiBlock =
                MultiBlockSystem.instance
                        .add("EarthQuarryMultiBlock")
                        .setMain(IEarthQuarry.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        MultiBlockEarthQuarry.init();

    }

}
