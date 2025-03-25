package com.denfop.api.windsystem;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public interface IWindMechanism {

    EnumRotorSide getRotorSide();

    void setRotorSide(EnumRotorSide rotorSide);

    double getCoefficient();

    void setCoefficient(double coefficient);

    void update();

    IWindRotor getRotor();

    ItemStack getItemStack();

    EnumLevelGenerators getLevelGenerator();

    float getAngle();

    void setRotationSpeed(float speed);

    int getRotorDiameter();

    ResourceLocation getRotorRenderTexture();

    EnumFacing getFacing();

    void change();

    double getAdditionalCoefficient();

    double getAdditionalPower();

    boolean getAuto();

    boolean getMin();

    boolean getSpace();

    int getTime();

    boolean need_repair();

    boolean can_repair();

    int getMinWind();

    int getMinWindSpeed();

    void setWork(boolean work);
}
