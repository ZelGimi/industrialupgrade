package com.denfop.register;

import com.denfop.IUItem;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.multiblock.MultiBlockSystem;
import com.denfop.api.reactors.IFluidReactor;
import com.denfop.api.reactors.IGasReactor;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.api.reactors.IHeatReactor;
import com.denfop.register.multiblock.*;
import com.denfop.tiles.cokeoven.*;
import com.denfop.tiles.geothermalpump.IController;
import com.denfop.tiles.mechanism.blastfurnace.api.*;
import com.denfop.tiles.quarry_earth.IEarthQuarry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

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
                                new ItemStack(IUItem.ForgeHammer.getItem())).setUniqueModel();
        advCokeOvenMultiBlock =
                MultiBlockSystem.instance
                        .add("AdvCokeOven")
                        .setMain(com.denfop.tiles.adv_cokeoven.IMain.class)
                        .setHasActivatedItem(true)
                        .setIgnoreMetadata(true)
                        .setActivateItem(
                                new ItemStack(IUItem.ForgeHammer.getItem()));
        blastFurnaceMultiBlock.add(blastFurnaceMultiBlock.getPos(), IBlastMain.class, new ItemStack(IUItem.blastfurnace.getBlock(0)),
                Direction.NORTH
        );
        blastFurnaceMultiBlock.add(blastFurnaceMultiBlock.getPos().offset(0, 1, 1), IBlastInputItem.class,
                new ItemStack(IUItem.blastfurnace.getBlock(1))
        );
        blastFurnaceMultiBlock.add(blastFurnaceMultiBlock.getPos().offset(0, 0, 2), IBlastInputFluid.class,
                new ItemStack(IUItem.blastfurnace.getBlock(4)), Direction.SOUTH
        );
        blastFurnaceMultiBlock.add(blastFurnaceMultiBlock.getPos().offset(-1, 0, 1), IBlastOutputItem.class,
                new ItemStack(IUItem.blastfurnace.getBlock(3)), Direction.EAST
        );


        blastFurnaceMultiBlock.add(blastFurnaceMultiBlock.getPos().offset(1, 0, 1), IBlastHeat.class,
                new ItemStack(IUItem.blastfurnace.getBlock(2)), Direction.WEST
        );
        BlockPos pos1 = blastFurnaceMultiBlock.getPos().offset(0, 0, 1);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    blastFurnaceMultiBlock.add(
                            pos1.offset(i, j, k),
                            IOtherBlastPart.class,
                            new ItemStack(IUItem.blastfurnace.getBlock(5)), Direction.NORTH
                    );
                }
            }
        }


        blastFurnaceMultiBlock.addReport(IBlastInputItem.class, "industrialupgrade.blastfurnace.blast_furnace_input");
        blastFurnaceMultiBlock.addReport(IBlastInputFluid.class, "industrialupgrade.blastfurnace.blast_furnace_input_fluid");
        blastFurnaceMultiBlock.addReport(IBlastOutputItem.class, "industrialupgrade.blastfurnace.blast_furnace_output");
        blastFurnaceMultiBlock.addReport(IBlastHeat.class, "industrialupgrade.blastfurnace.blast_furnace_heat");
        blastFurnaceMultiBlock.addReport(IOtherBlastPart.class, "industrialupgrade.blastfurnace.blast_furnace_part");

        cokeOvenMultiBlock =
                MultiBlockSystem.instance
                        .add("CokeOven")
                        .setMain(IMain.class)
                        .setHasActivatedItem(true)
                        .setIgnoreMetadata(true)
                        .setActivateItem(
                                new ItemStack(IUItem.ForgeHammer.getItem()));
        cokeOvenMultiBlock.add(cokeOvenMultiBlock.getPos(), IMain.class, new ItemStack(IUItem.cokeoven.getItem(0)),
                Direction.NORTH
        );
        cokeOvenMultiBlock.add(cokeOvenMultiBlock.getPos().offset(0, 1, 1), IInputItem.class,
                new ItemStack(IUItem.cokeoven.getItem(1))
        );
        cokeOvenMultiBlock.add(cokeOvenMultiBlock.getPos().offset(0, 0, 2), IInputFluid.class,
                new ItemStack(IUItem.cokeoven.getItem(4)), Direction.SOUTH
        );
        cokeOvenMultiBlock.add(cokeOvenMultiBlock.getPos().offset(-1, 0, 1), IOutputFluid.class,
                new ItemStack(IUItem.cokeoven.getItem(3)), Direction.EAST
        );


        cokeOvenMultiBlock.add(cokeOvenMultiBlock.getPos().offset(1, 0, 1), IHeat.class,
                new ItemStack(IUItem.cokeoven.getItem(2)), Direction.WEST
        );
        pos1 = cokeOvenMultiBlock.getPos().offset(0, 0, 1);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    cokeOvenMultiBlock.add(
                            pos1.offset(i, j, k),
                            IPart.class,
                            new ItemStack(IUItem.cokeoven.getItem(5))
                    );
                }
            }
        }
        advCokeOvenMultiBlock.add(advCokeOvenMultiBlock.getPos(),
                com.denfop.tiles.adv_cokeoven.IMain.class,
                new ItemStack(IUItem.adv_cokeoven.getItem(0)),
                Direction.NORTH
        );
        advCokeOvenMultiBlock.add(advCokeOvenMultiBlock.getPos().offset(0, 2, 1), com.denfop.tiles.adv_cokeoven.IInputItem.class,
                new ItemStack(IUItem.adv_cokeoven.getItem(1))
        );
        advCokeOvenMultiBlock.add(advCokeOvenMultiBlock.getPos().offset(0, 0, 2), com.denfop.tiles.adv_cokeoven.IInputFluid.class,
                new ItemStack(IUItem.adv_cokeoven.getItem(4)), Direction.SOUTH
        );
        advCokeOvenMultiBlock.add(advCokeOvenMultiBlock.getPos().offset(-1, 0, 1), com.denfop.tiles.adv_cokeoven.IOutputFluid.class,
                new ItemStack(IUItem.adv_cokeoven.getItem(3)), Direction.EAST
        );


        advCokeOvenMultiBlock.add(advCokeOvenMultiBlock.getPos().offset(1, 0, 1), com.denfop.tiles.adv_cokeoven.IHeat.class,
                new ItemStack(IUItem.adv_cokeoven.getItem(2)), Direction.WEST
        );
        pos1 = advCokeOvenMultiBlock.getPos().offset(0, 0, 1);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 3; j++) {
                for (int k = -1; k < 2; k++) {
                    advCokeOvenMultiBlock.add(
                            pos1.offset(i, j, k),
                            com.denfop.tiles.adv_cokeoven.IPart.class,
                            new ItemStack(IUItem.adv_cokeoven.getItem(5))
                    );
                }
            }
        }


        ChemicalPlantMultiBlock = MultiBlockSystem.instance
                .add("ChemicalPlantMultiBlock")
                .setMain(com.denfop.tiles.chemicalplant.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
        MultiBlockChemicalPlant.init();
        CyclotronMultiBlock =
                MultiBlockSystem.instance
                        .add("CyclotronMultiBlock")
                        .setMain(com.denfop.tiles.cyclotron.IController.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);

        MultiBlockCyclotron.init();
        EarthQuarryMultiBlock =
                MultiBlockSystem.instance
                        .add("EarthQuarryMultiBlock")
                        .setMain(IEarthQuarry.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        MultiBlockEarthQuarry.init();
        GeoThermalPumpMultiBlock =
                MultiBlockSystem.instance
                        .add("GeoThermalPumpMultiBlock")
                        .setMain(IController.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);

        MultiBlockGeothermalPump.init();
        GasTurbineMultiBlock = MultiBlockSystem.instance
                .add("GasTurbineMultiBlock")
                .setMain(com.denfop.tiles.gasturbine.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
        MultiBlockGasTurbine.init();
        GasWellMultiBlock = MultiBlockSystem.instance
                .add("GasWellMultiBlock")
                .setMain(com.denfop.tiles.gaswell.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
        MultiBlockGasWell.init();
        SmelterMultiBlock = MultiBlockSystem.instance
                .add("SmelterMultiBlock")
                .setMain(com.denfop.tiles.smeltery.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
        MultiBlockSmelter.init();
        LightningRodMultiBlock = MultiBlockSystem.instance
                .add("LightningRodMultiBlock")
                .setMain(com.denfop.tiles.lightning_rod.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
        MultiBlockLightningRod.init();
        SteamBoilerMultiBlock = MultiBlockSystem.instance
                .add("SteamBoilerMultiBlock")
                .setMain(com.denfop.tiles.mechanism.steamboiler.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
        MultiBlockSteamBoiler.init();
        WindTurbineMultiBlock = MultiBlockSystem.instance
                .add("WindTurbineMultiBlock")
                .setMain(com.denfop.tiles.windturbine.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
        MultiBlockWindTurbine.init();
        HydroTurbineMultiBlock = MultiBlockSystem.instance
                .add("HydroTurbineMultiBlock")
                .setMain(com.denfop.tiles.hydroturbine.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
        MultiBlockHydroTurbine.init();
        SteamTurbineMultiBlock = MultiBlockSystem.instance
                .add("SteamTurbineMultiBlock")
                .setMain(com.denfop.tiles.mechanism.steamturbine.IController.class)
                .setHasActivatedItem(false)
                .setIgnoreMetadata(true);
        MultiBlockSteamTurbine.init();
        WaterReactorMultiBlock =
                MultiBlockSystem.instance
                        .add("WaterReactorMultiBlock")
                        .setMain(IFluidReactor.class)
                        .setHasActivatedItem(false)
                        .setIgnoreMetadata(true);
        MultiBlockWaterReactor.init();
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

    }

}
