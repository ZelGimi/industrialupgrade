package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.recipe.ReplicatorRecipe;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemCrystalMemory extends Item implements IItemTab {
    private String nameItem;

    public ItemCrystalMemory() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

    public ItemStack readItemStack(HolderLookup.Provider access, ItemStack stack) {
        return stack.getOrDefault(DataComponentsInit.PATTERN, ItemStack.EMPTY);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable TooltipContext p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);

        if (p_41421_.has(DataComponentsInit.PATTERN)) {
            @Nullable ItemStack recorded = p_41421_.get(DataComponentsInit.PATTERN);
            p_41423_.add(Component.literal(Localization.translate("iu.item.CrystalMemory.tooltip.iu.item") + " " + recorded.getDisplayName().getString()));
            p_41423_.add(Component.literal(Localization.translate("iu.item.CrystalMemory.tooltip.UU-Matter") + " " + ModUtils.getString(
                    ReplicatorRecipe.getInBuckets(
                            recorded)) + "B"));
        } else {
            p_41423_.add(Component.literal(Localization.translate("iu.item.CrystalMemory.tooltip.Empty")));
        }
    }

    public void writecontentsTag(RegistryAccess registryAccess, ItemStack stack, ItemStack recorded) {
        stack.set(DataComponentsInit.PATTERN, recorded);
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
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
}
