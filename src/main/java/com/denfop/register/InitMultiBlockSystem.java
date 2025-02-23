package com.denfop.register;

import com.denfop.IUItem;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.multiblock.MultiBlockSystem;
import com.denfop.api.reactors.IFluidReactor;
import com.denfop.api.reactors.IGasReactor;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.api.reactors.IHeatReactor;
import com.denfop.register.multiblock.MultiBlockAdvGasReactor;
import com.denfop.register.multiblock.MultiBlockAdvGraphiteReactor;
import com.denfop.register.multiblock.MultiBlockAdvHeatReactor;
import com.denfop.register.multiblock.MultiBlockAdvWaterReactor;
import com.denfop.register.multiblock.MultiBlockChemicalPlant;
import com.denfop.register.multiblock.MultiBlockCyclotron;
import com.denfop.register.multiblock.MultiBlockEarthQuarry;
import com.denfop.register.multiblock.MultiBlockGasReactor;
import com.denfop.register.multiblock.MultiBlockGasTurbine;
import com.denfop.register.multiblock.MultiBlockGasWell;
import com.denfop.register.multiblock.MultiBlockGeothermalPump;
import com.denfop.register.multiblock.MultiBlockGraphiteReactor;
import com.denfop.register.multiblock.MultiBlockHeatReactor;
import com.denfop.register.multiblock.MultiBlockHydroTurbine;
import com.denfop.register.multiblock.MultiBlockImpGasReactor;
import com.denfop.register.multiblock.MultiBlockImpGraphiteReactor;
import com.denfop.register.multiblock.MultiBlockImpHeatReactor;
import com.denfop.register.multiblock.MultiBlockImpWaterReactor;
import com.denfop.register.multiblock.MultiBlockLightningRod;
import com.denfop.register.multiblock.MultiBlockPerGasReactor;
import com.denfop.register.multiblock.MultiBlockPerGraphiteReactor;
import com.denfop.register.multiblock.MultiBlockPerHeatReactor;
import com.denfop.register.multiblock.MultiBlockPerWaterReactor;
import com.denfop.register.multiblock.MultiBlockSmelter;
import com.denfop.register.multiblock.MultiBlockSteamBoiler;
import com.denfop.register.multiblock.MultiBlockSteamTurbine;
import com.denfop.register.multiblock.MultiBlockWaterReactor;
import com.denfop.register.multiblock.MultiBlockWindTurbine;
import com.denfop.tiles.cokeoven.IHeat;
import com.denfop.tiles.cokeoven.IInputFluid;
import com.denfop.tiles.cokeoven.IInputItem;
import com.denfop.tiles.cokeoven.IMain;
import com.denfop.tiles.cokeoven.IOutputFluid;
import com.denfop.tiles.cokeoven.IPart;
import com.denfop.tiles.geothermalpump.IController;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastHeat;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastInputFluid;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastInputItem;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastMain;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastOutputItem;
import com.denfop.tiles.mechanism.blastfurnace.api.IOtherBlastPart;
import com.denfop.tiles.quarry_earth.IEarthQuarry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class InitMultiBlockSystem {


    public static MultiBlockStructure blastFurnaceMultiBlock;
    public static MultiBlockStructure WaterReactorMultiBlock;
    public static MultiBlockStructure AdvWaterReactorMultiBlock;
    public static MultiBlockStructure ImpWaterReactorMultiBlock;
    public static MultiBlockStructure PerWaterReactorMultiBlock;
    public static MultiBlockStructure GasReactorMultiBlock;
    public static MultiBlockStructure advGasReactorMultiBlock;
    public static MultiBlockStructure impGasReactorMultiBlock;
    public static MultiBlockStructure perGasReactorMultiBlock;


    public static MultiBlockStructure GraphiteReactorMultiBlock;
    public static MultiBlockStructure advGraphiteReactorMultiBlock;
    public static MultiBlockStructure impGraphiteReactorMultiBlock;
    public static MultiBlockStructure perGraphiteReactorMultiBlock;
    public static MultiBlockStructure HeatReactorMultiBlock;
    public static MultiBlockStructure advHeatReactorMultiBlock;
    public static MultiBlockStructure impHeatReactorMultiBlock;
    public static MultiBlockStructure perHeatReactorMultiBlock;
    public static MultiBlockStructure EarthQuarryMultiBlock;
    public static MultiBlockStructure GeoThermalPumpMultiBlock;
    public static MultiBlockStructure ChemicalPlantMultiBlock;
    public static MultiBlockStructure cokeOvenMultiBlock;
    public static MultiBlockStructure advCokeOvenMultiBlock;
    public static MultiBlockStructure CyclotronMultiBlock;
    public static MultiBlockStructure SmelterMultiBlock;
    public static MultiBlockStructure SteamBoilerMultiBlock;
    public static MultiBlockStructure WindTurbineMultiBlock;
    public static MultiBlockStructure HydroTurbineMultiBlock;
    public static MultiBlockStructure GasTurbineMultiBlock;
    public static MultiBlockStructure SteamTurbineMultiBlock;
    public static MultiBlockStructure GasWellMultiBlock;
    public static MultiBlockStructure LightningRodMultiBlock;

    public static void init() {
        new MultiBlockSystem();

        blastFurnaceMultiBlock =
                MultiBlockSystem.instance
                        .add("BlastFurnace")
                        .setMain(IBlastMain.class)
                        .setHasActivatedItem(true)
                        .setIgnoreMetadata(true)
                        .setActivateItem(
                                new ItemStack(IUItem.ForgeHammer)).setUniqueModel();
        cokeOvenMultiBlock =
                MultiBlockSystem.instance
                        .add("CokeOven")
                        .setMain(IMain.class)
                        .setHasActivatedItem(true)
                        .setIgnoreMetadata(true)
                        .setActivateItem(
                                new ItemStack(IUItem.ForgeHammer));
        advCokeOvenMultiBlock =
                MultiBlockSystem.instance
                        .add("AdvCokeOven")
                        .setMain(com.denfop.tiles.adv_cokeoven.IMain.class)
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

        cokeOvenMultiBlock.add(cokeOvenMultiBlock.getPos(), IMain.class, new ItemStack(IUItem.cokeoven, 1, 0),
                EnumFacing.NORTH
        );
        cokeOvenMultiBlock.add(cokeOvenMultiBlock.getPos().add(0, 1, 1), IInputItem.class,
                new ItemStack(IUItem.cokeoven, 1, 1)
        );
        cokeOvenMultiBlock.add(cokeOvenMultiBlock.getPos().add(0, 0, 2), IInputFluid.class,
                new ItemStack(IUItem.cokeoven, 1, 4), EnumFacing.SOUTH
        );
        cokeOvenMultiBlock.add(cokeOvenMultiBlock.getPos().add(-1, 0, 1), IOutputFluid.class,
                new ItemStack(IUItem.cokeoven, 1, 3), EnumFacing.EAST
        );


        cokeOvenMultiBlock.add(cokeOvenMultiBlock.getPos().add(1, 0, 1), IHeat.class,
                new ItemStack(IUItem.cokeoven, 1, 2), EnumFacing.WEST
        );
        pos1 = cokeOvenMultiBlock.getPos().add(0, 0, 1);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    cokeOvenMultiBlock.add(
                            pos1.add(i, j, k),
                            IPart.class,
                            new ItemStack(IUItem.cokeoven, 1, 5)
                    );
                }
            }
        }

        advCokeOvenMultiBlock.add(advCokeOvenMultiBlock.getPos(), com.denfop.tiles.adv_cokeoven.IMain.class, new ItemStack(IUItem.adv_cokeoven, 1, 0),
                EnumFacing.NORTH
        );
        advCokeOvenMultiBlock.add(advCokeOvenMultiBlock.getPos().add(0, 2, 1),  com.denfop.tiles.adv_cokeoven.IInputItem.class,
                new ItemStack(IUItem.adv_cokeoven, 1, 1)
        );
        advCokeOvenMultiBlock.add(advCokeOvenMultiBlock.getPos().add(0, 0, 2),  com.denfop.tiles.adv_cokeoven.IInputFluid.class,
                new ItemStack(IUItem.adv_cokeoven, 1, 4), EnumFacing.SOUTH
        );
        advCokeOvenMultiBlock.add(advCokeOvenMultiBlock.getPos().add(-1, 0, 1),  com.denfop.tiles.adv_cokeoven.IOutputFluid.class,
                new ItemStack(IUItem.adv_cokeoven, 1, 3), EnumFacing.EAST
        );


        advCokeOvenMultiBlock.add(advCokeOvenMultiBlock.getPos().add(1, 0, 1),  com.denfop.tiles.adv_cokeoven.IHeat.class,
                new ItemStack(IUItem.adv_cokeoven, 1, 2), EnumFacing.WEST
        );
        pos1 = advCokeOvenMultiBlock.getPos().add(0, 0, 1);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 3; j++) {
                for (int k = -1; k < 2; k++) {
                    advCokeOvenMultiBlock.add(
                            pos1.add(i, j, k),
                            com.denfop.tiles.adv_cokeoven.IPart.class,
                            new ItemStack(IUItem.adv_cokeoven, 1, 5)
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
        CyclotronMultiBlock =
                MultiBlockSystem.instance
                        .add("CyclotronMultiBlock")
                        .setMain(com.denfop.tiles.cyclotron.IController.class)
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
        GeoThermalPumpMultiBlock =
                MultiBlockSystem.instance
                        .add("GeoThermalPumpMultiBlock")
                        .setMain(IController.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);

        ChemicalPlantMultiBlock = MultiBlockSystem.instance
                .add("ChemicalPlantMultiBlock")
                .setMain(com.denfop.tiles.chemicalplant.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
        SteamBoilerMultiBlock = MultiBlockSystem.instance
                .add("SteamBoilerMultiBlock")
                .setMain(com.denfop.tiles.mechanism.steamboiler.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);

        WindTurbineMultiBlock = MultiBlockSystem.instance
                .add("WindTurbineMultiBlock")
                .setMain(com.denfop.tiles.windturbine.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
        HydroTurbineMultiBlock = MultiBlockSystem.instance
                .add("HydroTurbineMultiBlock")
                .setMain(com.denfop.tiles.hydroturbine.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
       GasTurbineMultiBlock = MultiBlockSystem.instance
                .add("GasTurbineMultiBlock")
                .setMain(com.denfop.tiles.gasturbine.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
        SmelterMultiBlock = MultiBlockSystem.instance
                .add("SmelterMultiBlock")
                .setMain(com.denfop.tiles.smeltery.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);

       SteamTurbineMultiBlock = MultiBlockSystem.instance
                .add("SteamTurbineMultiBlock")
                .setMain(com.denfop.tiles.mechanism.steamturbine.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
       GasWellMultiBlock = MultiBlockSystem.instance
                .add("GasWellMultiBlock")
                .setMain(com.denfop.tiles.gaswell.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
        LightningRodMultiBlock = MultiBlockSystem.instance
                .add("LightningRodMultiBlock")
                .setMain(com.denfop.tiles.lightning_rod.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
        MultiBlockEarthQuarry.init();
        MultiBlockGeothermalPump.init();
        MultiBlockChemicalPlant.init();
        MultiBlockCyclotron.init();
        MultiBlockSmelter.init();
        MultiBlockSteamBoiler.init();
        MultiBlockWindTurbine.init();
        MultiBlockHydroTurbine.init();
        MultiBlockGasTurbine.init();
        MultiBlockSteamTurbine.init();
        MultiBlockGasWell.init();
        MultiBlockLightningRod.init();
    }

}
