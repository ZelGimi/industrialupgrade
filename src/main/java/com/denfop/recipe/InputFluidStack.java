package com.denfop.recipe;


import com.denfop.componets.Fluids;
import com.denfop.utils.FluidHandlerFix;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class InputFluidStack implements IInputItemStack {

    private static volatile FluidHandlerInfo fluidHandlerInfo;

    static {
        fluidHandlerInfo = new FluidHandlerInfo(Collections.emptyList());
    }

    private final Fluid fluid;
    private final int amount;

    public InputFluidStack(CompoundTag compoundTag) {
        boolean exist = compoundTag.getBoolean("exist");
        if (exist) {
            ResourceLocation fluidId = new ResourceLocation(compoundTag.getString("Fluid"));
            this.fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
            this.amount = compoundTag.getInt("Amount");
        } else {
            this.fluid = Fluids.EMPTY;
            this.amount = 1;
        }
    }

    public InputFluidStack(Fluid fluid) {
        this(fluid, 1000);
    }

    public InputFluidStack(FluidStack fluid) {
        this(fluid.getFluid(), fluid.getAmount());
    }

    public InputFluidStack(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
    }

    public InputFluidStack(FriendlyByteBuf buffer) {
        this(buffer.readFluidStack());
    }

    public static List<ItemStack> getFluidContainer(Fluid fluid) {
        FluidHandlerInfo info = fluidHandlerInfo;
        ArrayList<ItemStack> ret;
        Iterator<Item> var3;
        ret = new ArrayList<>();
        var3 = ForgeRegistries.ITEMS.iterator();

        while (var3.hasNext()) {
            Item item = var3.next();
            ItemStack stack = new ItemStack(item);
            IFluidHandlerItem handler = FluidHandlerFix.getFluidHandler(stack);
            if (handler != null) {
                handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
                ItemStack container = handler.getContainer();
                if (FluidUtil.getFluidContained(container).orElse(null) == null) {
                    ret.add(stack);
                }
            }
        }


        fluidHandlerInfo = info = new FluidHandlerInfo(Collections.unmodifiableList(ret));

        if (fluid == null) {
            return info.items;
        } else {
            ret = new ArrayList<>();

            for (final ItemStack stack : info.items) {
                IFluidHandlerItem handler = FluidHandlerFix.getFluidHandler(stack);
                if (handler != null && handler.fill(new FluidStack(fluid, Integer.MAX_VALUE), IFluidHandler.FluidAction.EXECUTE) > 0) {
                    ItemStack container = handler.getContainer();
                    if (!handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE).isEmpty()) {
                        ret.add(container);
                    }
                }
            }

            return ret;
        }
    }

    @Override
    public CompoundTag writeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putByte("id", (byte) 2);
        compoundTag.putBoolean("exist", fluid != null && !fluid.equals(Fluids.EMPTY) && amount != 0);
        if (fluid != null && !fluid.equals(Fluids.EMPTY)) {
            ResourceLocation fluidId = ForgeRegistries.FLUIDS.getKey(fluid);
            if (fluidId != null) {
                compoundTag.putString("Fluid", fluidId.toString());
                compoundTag.putInt("Amount", amount);
            }
        }
        return null;
    }

    public boolean matches(ItemStack subject) {
        Optional<FluidStack> fs1 = FluidUtil.getFluidContained(subject);
        FluidStack fs = fs1.orElse(null);
        return fs == null && this.fluid == null || fs != null && fs.getFluid() == this.fluid && fs.getAmount() >= this.amount;
    }

    public int getAmount() {
        return 1;
    }

    public List<ItemStack> getInputs() {
        return getFluidContainer(this.fluid);
    }

    @Override
    public boolean hasTag() {
        return false;
    }

    @Override
    public TagKey<Item> getTag() {
        return null;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(2);
        buffer.writeFluidStack(new FluidStack(this.fluid, 1000));
    }


    @Override
    public void growAmount(int count) {

    }

    public boolean equals(Object obj) {
        InputFluidStack other;
        return obj != null && this.getClass() == obj.getClass() && (other = (InputFluidStack) obj).fluid == this.fluid && other.amount == this.amount;
    }

    public FluidStack getFluid() {
        return new FluidStack(fluid, amount);
    }

    private static class FluidHandlerInfo {

        final List<ItemStack> items;

        FluidHandlerInfo(List<ItemStack> items) {
            this.items = items;
        }

    }

}
