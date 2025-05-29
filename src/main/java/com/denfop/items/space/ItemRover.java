package com.denfop.items.space;

import com.denfop.ElectricItem;
import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.api.space.rovers.enums.EnumRoversLevel;
import com.denfop.api.space.rovers.enums.EnumRoversLevelFluid;
import com.denfop.api.space.rovers.enums.EnumTypeRovers;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.api.space.upgrades.BaseSpaceUpgradeSystem;
import com.denfop.api.space.upgrades.SpaceUpgradeSystem;
import com.denfop.api.space.upgrades.event.EventItemLoad;
import com.denfop.blocks.FluidName;
import com.denfop.items.ItemFluidContainer;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.Arrays;
import java.util.List;

public class ItemRover extends ItemFluidContainer implements IRoversItem, IEnergyItem, IItemTab {

    private final EnumRoversLevel enumRoversLevel;
    private final EnumTypeRovers typeRovers;
    private final String name;
    private final double maxEnergy;
    private final double transferEnergy;
    private final int tier;
    private final EnumRoversLevelFluid fluids;
    private final double progress;
    public List<EnumTypeUpgrade> upgrades = Arrays.asList(EnumTypeUpgrade.values());

    public ItemRover(
            String name, int capacity, EnumRoversLevel enumRoversLevel, EnumTypeRovers typeRovers, int tier,
            double maxEnergy, double transferEnergy, EnumRoversLevelFluid fluids, double progress
    ) {
        super(new Properties().setNoRepair().stacksTo(1), capacity);
        this.name = name;
        this.maxEnergy = maxEnergy;
        this.fluids = fluids;
        this.transferEnergy = transferEnergy;
        this.tier = tier;
        this.progress = progress;
        this.enumRoversLevel = enumRoversLevel;
        this.typeRovers = typeRovers;
        BaseSpaceUpgradeSystem.list.add(() -> SpaceUpgradeSystem.system.addRecipe(this, EnumTypeUpgrade.values()));

    }
    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.SpaceTab;
    }
    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> subItems) {
        if (this.allowedIn(tab)) {
            subItems.add(new ItemStack(this));

            ItemStack stack1 = new ItemStack(this);
            ElectricItem.manager.charge(stack1, Double.MAX_VALUE, Integer.MAX_VALUE, true, false);
            subItems.add(stack1);

            for (FluidName fluidName : fluids.getLevelsList()) {
                ItemStack stack = new ItemStack(this);
                ElectricItem.manager.charge(stack, Double.MAX_VALUE, Integer.MAX_VALUE, true, false);
                subItems.add(this.getItemStack(stack, fluidName.getInstance().get()));
            }
        }
    }
    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.rover.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem;
    }
    @Override
    public boolean canProvideEnergy(final ItemStack var1) {
        return false;
    }

    @Override
    public double getMaxEnergy(final ItemStack var1) {
        return maxEnergy;
    }



    @Override
    public short getTierItem(final ItemStack var1) {
        return (short) tier;
    }

    @Override
    public boolean canfill(final Fluid var1) {
        for (FluidName fluidName : fluids.getLevelsList()) {
            if (fluidName.getInstance().get() == var1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getTransferEnergy(final ItemStack var1) {
        return transferEnergy;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public EnumTypeRovers getType() {
        return typeRovers;
    }

    @Override
    public IFluidHandlerItem getFluidHandler(ItemStack stack) {
        return FluidHandlerFix.getFluidHandler(stack);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int slot, boolean selected) {
        CompoundTag nbt = ModUtils.nbt(itemStack);

        if (!SpaceUpgradeSystem.system.hasInMap(itemStack)) {
            nbt.putBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, this, itemStack));
        }
    }

    @Override
    public EnumRoversLevel getLevel() {
        return enumRoversLevel;
    }

    @Override
    public double getAddProgress() {
        return progress;
    }

    @Override
    public List<EnumTypeUpgrade> getUpgradeModules() {
        return upgrades;
    }

}
