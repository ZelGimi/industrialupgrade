package com.denfop.integration.oc;

import com.denfop.api.IStorage;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.api.windsystem.IWindMechanism;
import com.denfop.tiles.base.TileEntityMultiMachine;
import com.denfop.tiles.mechanism.quarry.TileEntityBaseQuantumQuarry;
import com.denfop.tiles.reactors.IChamber;
import li.cil.oc.api.Driver;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional.Method;

public class OCIntegration {

    public OCIntegration() {
    }

    public static void init() {
        initOC();
    }


    @Method(
            modid = "opencomputers"
    )
    public static void initOC() {
        Driver.add(new OCStorageAdapter());
        Driver.add(new OCReactorAdapter());
        Driver.add(new OCWindWaterAdapter());
        Driver.add(new OCReactorChamberAdapter());
        Driver.add(new OCQuantumQuarryAdapter());
        Driver.add(new OCMultiMachineAdapter());
    }

    public static class OCMultiMachineAdapter extends DriverSidedTileEntity {

        public OCMultiMachineAdapter() {
        }

        public Class<?> getTileEntityClass() {
            return TileEntityMultiMachine.class;
        }

        public AbstractManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side) {
            return new MultiMachineAdapter((TileEntityMultiMachine) world.getTileEntity(pos));
        }

    }

    public static class OCQuantumQuarryAdapter extends DriverSidedTileEntity {

        public OCQuantumQuarryAdapter() {
        }

        public Class<?> getTileEntityClass() {
            return TileEntityBaseQuantumQuarry.class;
        }

        public AbstractManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side) {
            return new QuantumQuarryAdapter((TileEntityBaseQuantumQuarry) world.getTileEntity(pos));
        }

    }

    public static class OCStorageAdapter extends DriverSidedTileEntity {

        public OCStorageAdapter() {
        }

        public Class<?> getTileEntityClass() {
            return IStorage.class;
        }

        public AbstractManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side) {
            return new Storage((IStorage) world.getTileEntity(pos));
        }

    }

    public static class OCReactorAdapter extends DriverSidedTileEntity {

        public OCReactorAdapter() {
        }

        public Class<?> getTileEntityClass() {
            return IAdvReactor.class;
        }

        public AbstractManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side) {
            return new ReactorAdapter((IAdvReactor) world.getTileEntity(pos));
        }

    }

    public static class OCReactorChamberAdapter extends DriverSidedTileEntity {

        public OCReactorChamberAdapter() {
        }

        public Class<?> getTileEntityClass() {
            return IChamber.class;
        }

        public AbstractManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side) {
            return new ReactorChamberAdapter((IChamber) world.getTileEntity(pos));
        }

    }

    public static class OCWindWaterAdapter extends DriverSidedTileEntity {

        public OCWindWaterAdapter() {
        }

        public Class<?> getTileEntityClass() {
            return IWindMechanism.class;
        }

        public AbstractManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side) {
            return new WindWaterAdapter((IWindMechanism) world.getTileEntity(pos));
        }

    }

}
