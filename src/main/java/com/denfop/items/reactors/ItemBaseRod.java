package com.denfop.items.reactors;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUPotion;
import com.denfop.Localization;
import com.denfop.api.item.IHazmatLike;
import com.denfop.api.reactors.EnumTypeComponent;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.api.reactors.IReactorItem;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;


public class ItemBaseRod extends ItemDamage implements IRadioactiveItemType, IReactorItem {

    public final int numberOfCells;
    private final int heat;
    private final float power;
    private final String name;
    private final int level;
    private final double radiation;
    double[] p = new double[]{5.0D, 20D, 60D, 200D};

    public ItemBaseRod(String internalName, int cells, int heat, float power, int level) {
        super(internalName, 1);
        this.heat = heat;
        this.power = power;
        this.setCreativeTab(IUCore.ReactorsTab);
        this.setMaxStackSize(1);
        this.numberOfCells = cells;
        this.setNoRepair();
        this.setCreativeTab(IUCore.ReactorsTab);
        this.name = internalName;
        this.level = level;
        this.radiation = power * level * cells;
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {

        final String loc = Constants.MOD_ID +
                ':' +
                "reactors" + "/" + name;
        return new ModelResourceLocation(loc, null);
    }

    @Override
    public boolean showDurabilityBar(@NotNull final ItemStack stack) {
        return false;
    }

    public void onUpdate(ItemStack stack, World world, Entity rawEntity, int slotIndex, boolean isCurrentItem) {
        if (rawEntity instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) rawEntity;
            if (!IHazmatLike.hasCompleteHazmat(entity)) {
                IUPotion.radiation.applyTo(
                        entity,
                        this.getRadiationDuration(),
                        this.getRadiationAmplifier()
                );
            }
        }
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item", "iu").replace(".name", ""));
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @Override
    public void addInformation(
            @Nonnull final ItemStack stack,
            final World world,
            @Nonnull final List<String> tooltip,
            @Nonnull final ITooltipFlag advanced
    ) {
        super.addInformation(stack, world, tooltip, advanced);
        double temp = Math.log10(this.numberOfCells);
        double temp1 = Math.log10(2);
        double m = temp / temp1;
        tooltip.add(Localization.translate("reactor.info") + ModUtils.getString(p[(int) m] * this.power * this.level) + " EF");
        tooltip.add(Localization.translate("reactor.rod.radiation") + (int) this.radiation);
        tooltip.add(Localization.translate("reactor.rod.heat") + this.heat);
        tooltip.add(Localization.translate("reactor.rod_level") + this.level);
        tooltip.add(Localization.translate("reactor.rod_level1"));

    }


    @Override
    public int getRadiationDuration() {
        return 200;
    }

    @Override
    public int getRadiationAmplifier() {
        return 100;
    }


    @Override
    public EnumTypeComponent getType() {
        return EnumTypeComponent.ROD;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    public double getRadiation() {
        return radiation;
    }

    @Override
    public int getAutoRepair(final IAdvReactor reactor) {
        return 0;
    }

    @Override
    public int getRepairOther(final IAdvReactor reactor) {
        return 0;
    }

    @Override
    public int getDamageCFromHeat(final int heat, final IAdvReactor reactor) {
        return 1;
    }

    @Override
    public int getHeat(final IAdvReactor reactor) {
        return this.heat;
    }

    @Override
    public double getHeatRemovePercent(final IAdvReactor reactor) {
        return 0;
    }

    @Override
    public void damageItem(ItemStack stack, final int damage) {

    }

    @Override
    public boolean updatableItem() {
        return true;
    }

    @Override
    public boolean caneExtractHeat() {
        return true;
    }

    @Override
    public double getEnergyProduction(final IAdvReactor reactor) {
        double temp = Math.log10(this.numberOfCells);
        double temp1 = Math.log10(2);
        double m = temp / temp1;
        return p[(int) m] * this.power * this.level;
    }

    @Override
    public boolean needClear(ItemStack stack) {
        return this.getMaxCustomDamage(stack) - this.getCustomDamage(
                stack) == 0;
    }

}
