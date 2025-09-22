package com.denfop.items.space;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.api.space.rovers.enums.EnumRoversLevel;
import com.denfop.api.space.rovers.enums.EnumRoversLevelFluid;
import com.denfop.api.space.rovers.enums.EnumTypeRovers;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.api.space.upgrades.SpaceUpgradeSystem;
import com.denfop.api.space.upgrades.event.EventItemLoad;
import com.denfop.blocks.FluidName;
import com.denfop.items.ItemFluidContainer;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class ItemRover extends ItemFluidContainer implements IModelRegister, IRoversItem, IEnergyItem {

    protected static final String NAME = "rover";
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
        super(name, capacity);
        this.name = name;
        this.maxEnergy = maxEnergy;
        this.fluids = fluids;
        this.transferEnergy = transferEnergy;
        this.tier = tier;
        setMaxStackSize(1);
        this.progress = progress;
        this.enumRoversLevel = enumRoversLevel;
        this.typeRovers = typeRovers;
        this.setCreativeTab(IUCore.SpaceTab);
        SpaceUpgradeSystem.system.addRecipe(this, EnumTypeUpgrade.values());

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            final World world,
            final List<String> tooltip,
            final ITooltipFlag advanced
    ) {
        super.addInformation(stack, world, tooltip, advanced);
        tooltip.add(Localization.translate("iu.rover.info_temperature"));
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "iu.").replace(".name", ""));
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            subItems.add(new ItemStack(this));
            final ItemStack stack1 = new ItemStack(this);
            ElectricItem.manager.charge(stack1, 2.147483647E9D, 2147483647, true, false);
            subItems.add(stack1);
            for (FluidName fluidName : fluids.getLevelsList()) {
                final ItemStack stack = new ItemStack(this);
                ElectricItem.manager.charge(stack, 2.147483647E9D, 2147483647, true, false);
                subItems.add(this.getItemStack(stack, fluidName.getInstance()));
            }
        }
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
        return FluidUtil.getFluidHandler(stack);
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

    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(
                this,
                0,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + name, null)
        );
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
    public double getTransferEnergy(final ItemStack var1) {
        return transferEnergy;
    }

    @Override
    public void onUpdate(@Nonnull ItemStack itemStack, @Nonnull World world, @Nonnull Entity entity, int slot, boolean par5) {
        NBTTagCompound nbt = ModUtils.nbt(itemStack);

        if (!SpaceUpgradeSystem.system.hasInMap(itemStack)) {
            nbt.setBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, this, itemStack));
        }


    }

    @Override
    public boolean canfill(final Fluid var1) {
        for (FluidName fluidName : fluids.getLevelsList()) {
            if (fluidName.getInstance() == var1) {
                return true;
            }
        }
        return false;
    }

}
