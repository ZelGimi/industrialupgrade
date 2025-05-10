package com.denfop.api.windsystem;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IWindSystem {

    double getPower(Level world, BlockPos pos, boolean min, IWindMechanism rotor);

    double getSpeedFromPower(BlockPos pos, IWindMechanism rotor, double power);

    double getSpeedFromWaterPower(BlockPos pos, IWindMechanism rotor, double power);

    double getPowerFromWindRotor(Level world, BlockPos pos, IWindMechanism rotor, ItemStack stack);

    EnumRotorSide getRotorSide(Direction facing);

    double getPowerFromWaterRotor(final Level world, final IWindMechanism windMechanism, ItemStack stack);

    void changeRotorSide(IWindMechanism windMechanism, Direction facing);

    Direction getNewFacing();

    void getNewPositionOfMechanism(IWindMechanism windMechanism);

    double getWind_Strength();

    double getSpeed();

    double getSpeed(double speed);

    EnumWindSide getWindSide();

    void getNewFacing(Direction facing, IWindMechanism windMechanism);

    int getTime();

    int getLevelWind();

    EnumTypeWind getEnumTypeWind();


}
