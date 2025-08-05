package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.recipes.ScrapboxRecipeManager;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IUItemBase extends Item implements IItemTab {
    private CreativeModeTab tabCore;
    private String nameItem;

    public IUItemBase() {
        super(new Properties());
    }

    public IUItemBase(CreativeModeTab tabCore) {
        super(new Properties());
        this.tabCore = tabCore;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand hand) {
        if (!player.getItemInHand(hand).is(IUItem.doublescrapBox.getItem())) {
            return super.use(pLevel, player, hand);
        } else {
            int i = 0;
            ItemStack stack = player.getItemInHand(hand);
            while (i < 9) {
                if (!pLevel.isClientSide) {
                    ItemStack drop = ScrapboxRecipeManager.instance.getDrop(IUItem.scrapBox);
                    player.drop(drop, false);
                }
                i++;
            }
            stack.shrink(1);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));

        }

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

        return this.nameItem + ".name";
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return this.tabCore == null ? IUCore.ItemTab : this.tabCore;
    }
}
