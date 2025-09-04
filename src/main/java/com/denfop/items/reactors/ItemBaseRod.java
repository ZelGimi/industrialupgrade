package com.denfop.items.reactors;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.item.armor.HazmatLike;
import com.denfop.api.reactors.EnumTypeComponent;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.api.reactors.IReactorItem;
import com.denfop.potion.IUPotion;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;


public class ItemBaseRod extends ItemDamage implements IRadioactiveItemType, IReactorItem, IItemTab {

    public final int numberOfCells;
    private final int heat;
    private final float power;
    private final int level;
    private final double radiation;
    double[] p = new double[]{5.0D, 20D, 60D, 200D, 500D, 1300D};

    public ItemBaseRod(int cells, int heat, float power, int level) {
        super(new Item.Properties().stacksTo(1).setNoRepair(), 1);
        this.heat = heat;
        this.power = power;
        this.numberOfCells = cells;
        this.level = level;
        this.radiation = power * level * cells;
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ReactorsTab;
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            ResourceLocation res = BuiltInRegistries.ITEM.getKey(this);
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", res));
            String targetString = res.getNamespace()+".";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = res.getNamespace().startsWith(Constants.MOD_ID) ? "iu." + pathBuilder.toString().split("\\.")[2] : res.getNamespace() + "." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean isCurrentItem) {
        if (entity instanceof LivingEntity) {
            LivingEntity entityLiving = (LivingEntity) entity;
            if (!HazmatLike.hasCompleteHazmat(entityLiving)) {
                IUPotion.radiation.applyEffect(entityLiving, this.getRadiationDuration());
            }
        }
    }


    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable TooltipContext p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        double temp = Math.log10(this.numberOfCells);
        double temp1 = Math.log10(2);
        double m = temp / temp1;
        p_41423_.add(Component.literal(Localization.translate("reactor.info") + ModUtils.getString(p[(int) m] * this.power * this.level * 1.25) + " EF"));
        p_41423_.add(Component.literal(Localization.translate("reactor.rod.radiation") + (int) this.radiation));
        p_41423_.add(Component.literal(Localization.translate("reactor.rod.heat") + this.heat));
        p_41423_.add(Component.literal(Localization.translate("reactor.rod_level") + this.level));
        p_41423_.add(Component.literal(Localization.translate("reactor.rod_level1")));
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
        return p[(int) m] * this.power * this.level * 1.25;
    }

    @Override
    public boolean needClear(ItemStack stack) {
        return this.getMaxCustomDamage(stack) - this.getCustomDamage(
                stack) == 0;
    }

}
