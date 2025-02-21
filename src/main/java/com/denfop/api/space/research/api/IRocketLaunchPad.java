package com.denfop.api.space.research.api;


import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.invslot.InvSlot;
import com.denfop.render.rocketpad.DataRocket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.UUID;

public interface IRocketLaunchPad {

    void addDataRocket(ItemStack roversItem);
    InvSlotOutput getSlotOutput();

    void addFluidStack(FluidStack fluidStack);

    IRoversItem getRover();

    void consumeRover();

    void refuel(ItemStack itemStack, IRoversItem roversItem);

    void charge(ItemStack itemStack);

    ItemStack getRoverStack();

    InvSlot getRoverSlot();

    UUID getPlayer();

    World getWorldPad();

    BlockPos getBlockPos();
}
