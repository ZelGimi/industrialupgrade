package com.denfop.api.space.research.api;


import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.invslot.InvSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

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

    Level getWorldPad();

    BlockPos getPos();
}
