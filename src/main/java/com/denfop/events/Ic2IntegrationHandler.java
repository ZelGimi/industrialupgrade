package com.denfop.events;

import com.denfop.tiles.reactors.TileEntityAdvNuclearReactorElectric;
import com.denfop.tiles.reactors.TileEntityAdvReactorChamberElectric;
import com.denfop.tiles.reactors.TileEntityImpNuclearReactor;
import com.denfop.tiles.reactors.TileEntityImpReactorChamberElectric;
import com.denfop.tiles.reactors.TileEntityNuclearReactorElectric;
import com.denfop.tiles.reactors.TileEntityPerNuclearReactor;
import com.denfop.tiles.reactors.TileEntityPerReactorChamberElectric;
import com.denfop.tiles.reactors.TileEntityReactorChamberElectric;
import ic2.core.ref.TeBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Ic2IntegrationHandler {


    public static TeBlock.ITePlaceHandler advReactorChamberPlace = new TeBlock.ITePlaceHandler() {
        public boolean canReplace(World world, BlockPos pos, EnumFacing side, ItemStack stack) {
            int count = 0;
            EnumFacing[] var6 = EnumFacing.values();
            for (EnumFacing dir : var6) {
                TileEntity te = world.getTileEntity(pos.offset(dir));
                if (te instanceof TileEntityAdvNuclearReactorElectric) {
                    ++count;
                }
            }

            return count == 1;
        }
    };
    public static TeBlock.ITePlaceHandler impReactorChamberPlace = new TeBlock.ITePlaceHandler() {
        public boolean canReplace(World world, BlockPos pos, EnumFacing side, ItemStack stack) {
            int count = 0;
            EnumFacing[] var6 = EnumFacing.values();
            for (EnumFacing dir : var6) {
                TileEntity te = world.getTileEntity(pos.offset(dir));
                if (te instanceof TileEntityImpNuclearReactor) {
                    ++count;
                }
            }

            return count == 1;
        }
    };
    public static TeBlock.ITePlaceHandler perReactorChamberPlace = new TeBlock.ITePlaceHandler() {
        public boolean canReplace(World world, BlockPos pos, EnumFacing side, ItemStack stack) {
            int count = 0;
            EnumFacing[] var6 = EnumFacing.values();
            for (EnumFacing dir : var6) {
                TileEntity te = world.getTileEntity(pos.offset(dir));
                if (te instanceof TileEntityPerNuclearReactor) {
                    ++count;
                }
            }

            return count == 1;
        }
    };
    public static TeBlock.ITePlaceHandler ReactorChamberPlace = new TeBlock.ITePlaceHandler() {
        public boolean canReplace(World world, BlockPos pos, EnumFacing side, ItemStack stack) {
            int count = 0;
            EnumFacing[] var6 = EnumFacing.values();
            for (EnumFacing dir : var6) {
                TileEntity te = world.getTileEntity(pos.offset(dir));
                if (te instanceof TileEntityNuclearReactorElectric) {
                    ++count;
                }
            }

            return count == 1;
        }
    };
    public static TeBlock.ITePlaceHandler advReactorPlace = (world, pos, side, stack) -> {
        int count = 0;
        EnumFacing[] var6 = EnumFacing.values();
        for (EnumFacing dir : var6) {
            TileEntity te = world.getTileEntity(pos.offset(dir));
            if (te instanceof TileEntityAdvReactorChamberElectric) {
               return false;
            }
        }

        return true;
    };
    public static TeBlock.ITePlaceHandler impReactorPlace = (world, pos, side, stack) -> {
        int count = 0;
        EnumFacing[] var6 = EnumFacing.values();
        for (EnumFacing dir : var6) {
            TileEntity te = world.getTileEntity(pos.offset(dir));
            if (te instanceof TileEntityImpReactorChamberElectric) {
                return false;
            }
        }

        return true;
    };
    public static TeBlock.ITePlaceHandler perReactorPlace = (world, pos, side, stack) -> {
        int count = 0;
        EnumFacing[] var6 = EnumFacing.values();
        for (EnumFacing dir : var6) {
            TileEntity te = world.getTileEntity(pos.offset(dir));
            if (te instanceof TileEntityPerReactorChamberElectric) {
                return false;
            }
        }

        return true;
    };
    public static TeBlock.ITePlaceHandler ReactorPlace = (world, pos, side, stack) -> {
        EnumFacing[] var6 = EnumFacing.values();
        for (EnumFacing dir : var6) {
            TileEntity te = world.getTileEntity(pos.offset(dir));
            if (te instanceof TileEntityReactorChamberElectric) {
                return false;
            }
        }

        return true;
    };
}
