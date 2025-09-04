package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.items.reactors.ItemDamage;
import com.denfop.tabs.IItemTab;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemMesh extends ItemDamage implements IItemTab {
    private final int level;

    public ItemMesh(final String name, final int maxDamage, int level) {
        super(new Properties().stacksTo(1), maxDamage);
        this.level = level;
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

    public int getLevel() {
        return level;
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
    }

}
