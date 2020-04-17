// 
// Decompiled by Procyon v0.5.36
// 

package com.Denfop.ssp;

import net.minecraftforge.fml.relauncher.SideOnly;

import com.Denfop.ssp.items.ItemArmourSolarHelmet;
import com.Denfop.ssp.items.CraftingThings;
import com.Denfop.ssp.items.ItemArmorQuantumBoosts;
import com.Denfop.ssp.items.ItemArmorQuantumChestplate;
import com.Denfop.ssp.items.ItemArmorQuantumLeggins;
import com.chocohead.advsolar.items.ItemDoubleSlab;

import ic2.core.block.state.IIdProvider;
import ic2.core.ref.IItemModelProvider;
import ic2.core.ref.IMultiItem;
import ic2.core.ref.ItemName;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

public enum SSP_Items
{
    Spectral_SOLAR_HELMET, 
    Singular_SOLAR_HELMET, 
    Quantum_chestplate,
    Quantum_leggins,
    Quantum_boosts,
    CRAFTING;
	
    
    private Item instance;
    
    public <T extends net.minecraft.item.Item> T getInstance() {
        return (T)this.instance;
    }
    
    public <T extends java.lang.Enum> ItemStack getItemStack(final T variant) {
        if (this.instance == null) {
            return null;
        }
        if (this.instance instanceof IMultiItem) {
            final IMultiItem<IIdProvider> multiItem = (IMultiItem<IIdProvider>)this.instance;
            return multiItem.getItemStack((IIdProvider)variant);
        }
        if (variant == null) {
            return new ItemStack(this.instance);
        }
        throw new IllegalArgumentException("Not applicable");
    }
    
    public <T extends net.minecraft.item.Item> void setInstance(final T instance) {
        if (this.instance != null) {
            throw new IllegalStateException("Duplicate instances!");
        }
        this.instance = (Item)instance;
    }
   
    static void buildItems(final Side side) {
    	SSP_Items.Singular_SOLAR_HELMET.setInstance(new ItemArmourSolarHelmet(ItemArmourSolarHelmet.SolarHelmetTypes.Singular));
        
        SSP_Items.Spectral_SOLAR_HELMET.setInstance(new ItemArmourSolarHelmet(ItemArmourSolarHelmet.SolarHelmetTypes.Spectral));
        SSP_Items.CRAFTING.setInstance(new CraftingThings());
        SSP_Items.Quantum_chestplate.setInstance(new ItemArmorQuantumChestplate());
        SSP_Items.Quantum_leggins.setInstance(new ItemArmorQuantumLeggins());
        SSP_Items.Quantum_boosts.setInstance(new ItemArmorQuantumBoosts());
        if (side == Side.CLIENT) {
            doModelGuf();
        }
    }
    
    @SideOnly(Side.CLIENT)
    private static void doModelGuf() {
        for (final SSP_Items item : values()) {
            ((IItemModelProvider) item.getInstance()).registerModels((ItemName)null);
        }
    }
}
