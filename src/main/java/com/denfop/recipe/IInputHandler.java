package com.denfop.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public interface IInputHandler {

    IInputItemStack getInput(ItemStack var1);

    IInputItemStack getInput(ItemStack var1, int var2);

    IInputItemStack getInput(String var1);

    IInputItemStack getInput(String var1, int var2);

    IInputItemStack getInput(String var1, int var2, int var3);


    IInputItemStack getInput(Fluid o);

    IInputItemStack getInput(Fluid fluid, int amount);

}
