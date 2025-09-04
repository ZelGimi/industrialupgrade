package com.denfop.items.reactors;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.reactors.EnumTypeComponent;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.api.reactors.IReactorItem;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.Localization;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemComponentVent extends ItemDamage implements IReactorItem, IItemTab {

    private final int level;
    private final int autoRepair;

    public ItemComponentVent(int level, int autoRepair) {
        super(new Item.Properties().stacksTo(1), 1);
        this.level = level;
        this.autoRepair = autoRepair;
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

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(Localization.translate("reactor.component_vent1") + 150 * level));
        p_41423_.add(Component.literal(Localization.translate("reactor.component_vent") + this.autoRepair));
        p_41423_.add(Component.literal(Localization.translate("reactor.component_level") + this.level));
        p_41423_.add(Component.literal(Localization.translate("reactor.component_level1")));

    }


    @Override
    public boolean needClear(ItemStack stack) {
        return this.getMaxCustomDamage(stack) - this.getCustomDamage(
                stack) == 0;
    }

    @Override
    public EnumTypeComponent getType() {
        return EnumTypeComponent.HEAT_SINK;
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
        return (int) (autoRepair * reactor.getModuleComponentVent());
    }

    @Override
    public int getDamageCFromHeat(final int heat, final IAdvReactor reactor) {
        return 1;
    }

    @Override
    public int getHeat(final IAdvReactor reactor) {
        return 0;
    }

    @Override
    public double getHeatRemovePercent(final IAdvReactor reactor) {
        return 1.2;
    }

    @Override
    public void damageItem(final ItemStack stack, final int damage) {
        if (damage > 150 * level)
            this.applyCustomDamage(stack, damage, null);
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

}
