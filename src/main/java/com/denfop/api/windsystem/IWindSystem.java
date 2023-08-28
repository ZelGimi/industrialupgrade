package com.denfop.api.windsystem;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IWindSystem {

    double getPower(World world, BlockPos pos, boolean min, IWindMechanism rotor);

    double getSpeedFromPower(BlockPos pos, IWindMechanism rotor, double power);

    double getSpeedFromWaterPower(BlockPos pos, IWindMechanism rotor, double power);

    double getPowerFromWindRotor(World world, BlockPos pos, IWindMechanism rotor, ItemStack stack);

    EnumRotorSide getRotorSide(EnumFacing facing);

    double getPowerFromWaterRotor(final World world, final IWindMechanism windMechanism, ItemStack stack);

    void changeRotorSide(IWindMechanism windMechanism, EnumFacing facing);

    EnumFacing getNewFacing();

    void getNewPositionOfMechanism(IWindMechanism windMechanism);

    double getWind_Strength();

    double getSpeed();

    double getSpeed(double speed);

    EnumWindSide getWindSide();

    void getNewFacing(EnumFacing facing, IWindMechanism windMechanism);

    int getTime();

    int getLevelWind();

    EnumTypeWind getEnumTypeWind();


}
