package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.recipe.ReplicatorRecipe;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemCrystalMemory extends Item {
    private String nameItem;

    public ItemCrystalMemory() {
        super(new Properties().tab(IUCore.ItemTab).stacksTo(1));
    }

    public ItemStack readItemStack(ItemStack stack) {
        CompoundTag nbt = ModUtils.nbt(stack);
        CompoundTag contentTag = nbt.getCompound("Pattern");
        return ItemStack.of(contentTag);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        ItemStack recorded = this.readItemStack(p_41421_);
        if (!ModUtils.isEmpty(recorded)) {
            p_41423_.add(Component.literal(Localization.translate("iu.item.CrystalMemory.tooltip.iu.item") + " " + recorded.getDisplayName().getString()));
            p_41423_.add(Component.literal(Localization.translate("iu.item.CrystalMemory.tooltip.UU-Matter") + " " + ModUtils.getString(
                    ReplicatorRecipe.getInBuckets(
                            recorded)) + "B"));
        } else {
            p_41423_.add(Component.literal(Localization.translate("iu.item.CrystalMemory.tooltip.Empty")));
        }
    }

    public void writecontentsTag(ItemStack stack, ItemStack recorded) {
        CompoundTag nbt = ModUtils.nbt(stack);
        CompoundTag contentTag = new CompoundTag();
        recorded.save(contentTag);
        nbt.put("Pattern", contentTag);
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", Registry.ITEM.getKey(this)));
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
