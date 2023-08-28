package com.denfop.integration.oc;

import com.denfop.api.IStorage;
import com.denfop.api.windsystem.IWindMechanism;
import com.denfop.tiles.base.TileMultiMachine;
import com.denfop.tiles.mechanism.quarry.TileBaseQuantumQuarry;
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
        Driver.add(new OCWindWaterAdapter());
        Driver.add(new OCQuantumQuarryAdapter());
        Driver.add(new OCMultiMachineAdapter());
    }

    public static class OCMultiMachineAdapter extends DriverSidedTileEntity {

        public OCMultiMachineAdapter() {
        }

        public Class<?> getTileEntityClass() {
            return TileMultiMachine.class;
        }

        public AbstractManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side) {
            return new MultiMachineAdapter((TileMultiMachine) world.getTileEntity(pos));
        }

    }

    public static class OCQuantumQuarryAdapter extends DriverSidedTileEntity {

        public OCQuantumQuarryAdapter() {
        }

        public Class<?> getTileEntityClass() {
            return TileBaseQuantumQuarry.class;
        }

        public AbstractManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side) {
            return new QuantumQuarryAdapter((TileBaseQuantumQuarry) world.getTileEntity(pos));
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
