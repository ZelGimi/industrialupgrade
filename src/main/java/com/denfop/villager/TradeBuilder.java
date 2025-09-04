package com.denfop.villager;

import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

public class TradeBuilder {
    Profession profession;
    int level = 1;
    int xp = 2;
    int maxUse = 8;
    private ItemStack result;
    private ItemStack second = ItemStack.EMPTY;
    private ItemStack first = ItemStack.EMPTY;

    public TradeBuilder(ItemStack stack) {
        this.result = stack;
    }

    public TradeBuilder() {
    }

    public static TradeBuilder create() {
        return new TradeBuilder();
    }

    public TradeBuilder addSecondStack(ItemStack second) {
        this.second = second;
        return this;
    }

    public TradeBuilder addFirstStack(ItemStack first) {
        this.first = first;
        return this;
    }

    public TradeBuilder setEmeralds(int count) {
        this.first = new ItemStack(Items.EMERALD, count);
        return this;
    }

    public TradeBuilder setXp(int xp) {
        this.xp = xp;
        return this;
    }

    public TradeBuilder setResult(ItemStack result) {
        this.result = result;
        return this;
    }

    public TradeBuilder setLevel(int level) {
        this.level = level;
        return this;
    }

    public TradeBuilder setProfession(Profession profession) {
        this.profession = profession;
        return this;
    }

    public TradeBuilder setMaxUse(int maxUse) {
        this.maxUse = maxUse;
        return this;
    }

    public void build() {
        Tuple<Profession, Integer> professionLevel = new Tuple<>(this.profession, level);
        MerchantOffer offer = new MerchantOffer(first, second, result, maxUse, xp, 0.05f);
        TradingSystem.instance.register(professionLevel, offer);
    }
}
