package com.denfop.items.reactors;

import com.denfop.IUCore;
import com.denfop.Localization;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemsPumps extends ItemDamage {

    private final int power;
    private final int energy;
    private final int level;

    public ItemsPumps(final int maxDamage, int level, int power, int energy) {
        super(new Item.Properties().tab(IUCore.ReactorsTab).stacksTo(1), maxDamage);
        this.power = power;
        this.level = level;
        this.energy = energy;
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
            this.nameItem = "iu."+pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }
    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(Localization.translate("reactor.component_level") + (this.level + 1)));
        p_41423_.add(Component.literal(Localization.translate("reactor.component_level1")));


    }

    public int getLevel() {
        return level;
    }

    public int getEnergy() {
        return energy;
    }

    public int getPower() {
        return power;
    }
}
