package com.denfop.api.windsystem;


import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface IWindMechanism {

    EnumRotorSide getRotorSide();

    void setRotorSide(EnumRotorSide rotorSide);

    double getCoefficient();

    void setCoefficient(double coefficient);

    IWindRotor getRotor();

    ItemStack getItemStack();

    EnumLevelGenerators getLevelGenerator();

    float getAngle();

    void setRotationSpeed(float speed);

    int getRotorDiameter();

    ResourceLocation getRotorRenderTexture();

    Direction getFacing();

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
