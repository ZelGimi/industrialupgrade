package com.denfop.villager;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class MerchantOffer {

    private final ItemStack baseCostA;

    private final ItemStack costB;

    private final ItemStack result;
    private final int maxUses;
    private int uses;
    private boolean rewardExp = true;
    private int specialPriceDiff;
    private int demand;
    private float priceMultiplier;
    private int xp = 1;

    public MerchantOffer(ItemStack pBaseCostA, ItemStack pResult, int pMaxUses, int pXp, float pPriceMultiplier) {
        this(pBaseCostA, ItemStack.EMPTY, pResult, pMaxUses, pXp, pPriceMultiplier);
    }

    public MerchantOffer(ItemStack pBaseCostA, ItemStack pCostB, ItemStack pResult, int pMaxUses, int pXp, float pPriceMultiplier) {
        this(pBaseCostA, pCostB, pResult, 0, pMaxUses, pXp, pPriceMultiplier);
    }

    public MerchantOffer(ItemStack pBaseCostA, ItemStack pCostB, ItemStack pResult, int pUses, int pMaxUses, int pXp, float pPriceMultiplier) {
        this(pBaseCostA, pCostB, pResult, pUses, pMaxUses, pXp, pPriceMultiplier, 0);
    }

    public MerchantOffer(ItemStack pBaseCostA, ItemStack pCostB, ItemStack pResult, int pUses, int pMaxUses, int pXp, float pPriceMultiplier, int pDemand) {
        this.baseCostA = pBaseCostA;
        this.costB = pCostB;
        this.result = pResult;
        this.uses = pUses;
        this.maxUses = pMaxUses;
        this.xp = pXp;
        this.priceMultiplier = pPriceMultiplier;
        this.demand = pDemand;
    }

    public ItemStack getBaseCostA() {
        return this.baseCostA;
    }

    public ItemStack getCostA() {
        if (this.baseCostA.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            int i = this.baseCostA.getCount();
            int j = Math.max(0, Mth.floor((float) (i * this.demand) * this.priceMultiplier));
            return this.baseCostA.copyWithCount(Mth.clamp(i + j + this.specialPriceDiff, 1, this.baseCostA.getMaxStackSize()));
        }
    }

    public ItemStack getCostB() {
        return this.costB;
    }

    public ItemStack getResult() {
        return this.result;
    }


    public void updateDemand() {
        this.demand = this.demand + this.uses - (this.maxUses - this.uses);
    }

    public ItemStack assemble() {
        return this.result.copy();
    }

    public int getUses() {
        return this.uses;
    }

    public void resetUses() {
        this.uses = 0;
    }

    public int getMaxUses() {
        return this.maxUses;
    }

    public void increaseUses() {
        ++this.uses;
    }

    public int getDemand() {
        return this.demand;
    }

    public void addToSpecialPriceDiff(int pAdd) {
        this.specialPriceDiff += pAdd;
    }

    public void resetSpecialPriceDiff() {
        this.specialPriceDiff = 0;
    }

    public int getSpecialPriceDiff() {
        return this.specialPriceDiff;
    }

    public void setSpecialPriceDiff(int pPrice) {
        this.specialPriceDiff = pPrice;
    }

    public float getPriceMultiplier() {
        return this.priceMultiplier;
    }

    public int getXp() {
        return this.xp;
    }

    public boolean isOutOfStock() {
        return this.uses >= this.maxUses;
    }

    public void setToOutOfStock() {
        this.uses = this.maxUses;
    }

    public boolean needsRestock() {
        return this.uses > 0;
    }

    public boolean shouldRewardExp() {
        return this.rewardExp;
    }
}
