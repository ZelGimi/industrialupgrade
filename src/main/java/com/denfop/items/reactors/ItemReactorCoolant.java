package com.denfop.items.reactors;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.reactors.EnumTypeComponent;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.api.reactors.IReactorItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemReactorCoolant extends ItemDamage implements IReactorItem {

    private final int level;
    private final int heat;

    public ItemReactorCoolant(final String name, int level, int damage, int heat) {
        super(name, damage);
        this.level = level;
        this.heat = heat;
        setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {

        final String loc = Constants.MOD_ID +
                ':' +
                "reactors" + "/" + name;
        return new ModelResourceLocation(loc, null);
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item", "iu").replace(".name", ""));
    }

    @Override
    public boolean needClear(ItemStack stack) {
        return this.getMaxCustomDamage(stack) - this.getCustomDamage(
                stack) == 0;
    }

    public boolean showDurabilityBar(@Nonnull ItemStack stack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    @Override
    public EnumTypeComponent getType() {
        return EnumTypeComponent.COOLANT_ROD;
    }

    @Override
    public int getLevel() {
        return this.level;
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
        return this.heat;
    }

    @Override
    public int getHeat(final IAdvReactor reactor) {
        return 0;
    }

    @Override
    public double getHeatRemovePercent(final IAdvReactor reactor) {
        return 0.5;
    }

    @Override
    public void damageItem(final ItemStack stack, final int damage) {
        applyCustomDamage(stack, damage, null);
    }

    @Override
    public void addInformation(
            @Nonnull final ItemStack stack,
            final World world,
            @Nonnull final List<String> tooltip,
            @Nonnull final ITooltipFlag advanced
    ) {
        super.addInformation(stack, world, tooltip, advanced);
        tooltip.add(Localization.translate("iu.reactoritem.durability") + " " + (this.getMaxCustomDamage(stack) - this.getCustomDamage(
                stack)) + "/" + this.getMaxCustomDamage(stack));

        tooltip.add(Localization.translate("reactor.component_level") + this.level);
        tooltip.add(Localization.translate("reactor.component_level1"));
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
        return 0;
    }

    public int needFill(ItemStack stack) {

        final int max = this.getMaxCustomDamage(stack);
        final int custom = this.getCustomDamage(
                stack);
        final int min = max / 1000;
        if (max - custom == max) {
            return 0;
        }
        return (max - custom) / min;
    }

    public void fill(ItemStack stack) {
        applyCustomDamage(stack, this.getMaxCustomDamage(stack) / 1000, null);
    }

}
