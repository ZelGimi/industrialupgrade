package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.api.steam.ISteamBlade;
import com.denfop.items.reactors.ItemDamage;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemSteamRod extends ItemDamage implements ISteamBlade, IItemTab {
    private final double percent;
    private final int level;
    private final ResourceLocation resourceLocation;

    public ItemSteamRod(int level, double percent, int damage, ResourceLocation resourceLocation) {
        super(new Properties().stacksTo(1), damage);
        this.percent = percent;
        this.level = level;
        this.resourceLocation = resourceLocation;
    }
    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }
    public int getLevel() {
        return level;
    }

    @Override
    public double getCoef() {
        return percent;
    }

    @Override
    public boolean damageBlade(final ItemStack stack) {
        return applyCustomDamage(stack, -1, null);
    }

    @Override
    public ResourceLocation getTexture() {
        return resourceLocation;
    }

    @Override
    public void appendHoverText(
            @Nonnull final ItemStack stack,
            @Nullable final Level world,
            @Nonnull final List<Component> tooltip,
            @Nonnull final TooltipFlag flag
    ) {
        super.appendHoverText(stack, world, tooltip, flag);

        tooltip.add(Component.translatable("iu.reactoritem.durability")
                .append(" " + (this.getMaxCustomDamage(stack) - this.getCustomDamage(stack)) + "/" + this.getMaxCustomDamage(stack)));

        tooltip.add(Component.translatable("reactor.component_level")
                .append(String.valueOf(this.level + 1)));
    }

}
