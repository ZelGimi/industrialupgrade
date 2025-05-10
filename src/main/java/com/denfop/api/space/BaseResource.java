package com.denfop.api.space;

import com.denfop.api.space.rovers.enums.EnumTypeRovers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class BaseResource implements IBaseResource {

    private final FluidStack fluidStack;
    private final EnumTypeRovers typeRovers;
    private ItemStack stack;
    private int max;
    private int min;
    private IBody body;
    private int percentplanet;

    public BaseResource(ItemStack stack, int minchance, int maxchance, int percentplanet, IBody body, EnumTypeRovers typeRovers) {
        this.stack = stack;
        this.fluidStack = null;
        this.max = maxchance;
        this.typeRovers = typeRovers;
        this.min = minchance;
        this.body = body;
        this.percentplanet = percentplanet;
        SpaceNet.instance.addResource(this);
    }

    public BaseResource(FluidStack fluidStack, int minchance, int maxchance, int percentplanet, IBody body, EnumTypeRovers typeRovers) {
        this.stack = null;
        this.fluidStack = fluidStack;
        this.max = maxchance;
        this.min = minchance;
        this.body = body;
        this.typeRovers = typeRovers;
        this.percentplanet = percentplanet;
        SpaceNet.instance.addResource(this);
    }

    public BaseResource(CompoundTag tagCompound) {
        percentplanet = tagCompound.getByte("percentplanet");
        min = tagCompound.getByte("min");
        max = tagCompound.getByte("max");
        if (tagCompound.getBoolean("hasItem")) {
            stack = ItemStack.of(tagCompound.getCompound("stack"));
        } else {
            stack = null;
        }
        if (tagCompound.getBoolean("hasFluid")) {
            fluidStack = FluidStack.loadFluidStackFromNBT(tagCompound.getCompound("fluidStack"));
        } else {
            fluidStack = null;
        }
        typeRovers = EnumTypeRovers.values()[tagCompound.getByte("rovers")];
    }


    @Override
    public ItemStack getItemStack() {
        return this.stack;
    }

    @Override
    public FluidStack getFluidStack() {
        return fluidStack;
    }

    @Override
    public int getPercentResearchBody() {
        return percentplanet;
    }

    @Override
    public int getChance() {
        return this.min;
    }

    @Override
    public int getMaxChance() {
        return this.max;
    }

    @Override
    public IBody getBody() {
        return this.body;
    }

    @Override
    public int getPercentPanel() {
        return this.percentplanet;
    }

    @Override
    public CompoundTag writeNBTTag(final CompoundTag tagCompound) {
        tagCompound.putByte("percentplanet", (byte) percentplanet);
        tagCompound.putByte("min", (byte) min);
        tagCompound.putByte("max", (byte) max);
        tagCompound.putByte("rovers", (byte) typeRovers.ordinal());
        tagCompound.putBoolean("hasItem", stack != null);
        if (stack != null) {
            tagCompound.put("stack", stack.save(new CompoundTag()));
        }
        tagCompound.putBoolean("hasFluid", fluidStack != null);
        if (fluidStack != null) {
            tagCompound.put("fluidStack", fluidStack.writeToNBT(new CompoundTag()));
        }
        return tagCompound;
    }

    @Override
    public EnumTypeRovers getTypeRovers() {
        return typeRovers;
    }

}
