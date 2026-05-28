package com.denfop.api.storage;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public interface TypeMechanismStorage extends ElectricStorage {
    List<ItemStack> getStacks();

    List<FluidStack> getFluidStacks();

    BlockEntity getBlockEntityNeighbor();

    EnumTypeSlots getTypeSlots();

    Direction getDirection();
}
