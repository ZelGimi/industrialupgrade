package com.denfop.items.resource;

import com.google.common.base.Function;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.profile.Version;
import ic2.core.ref.IItemModelProvider;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.Map;

public class ItemIU extends Item implements IItemModelProvider {

    private EnumRarity rarity;
    private Map<Capability<?>, Function<ItemStack, ?>> caps;

    public ItemIU(ItemName name) {
        this.rarity = EnumRarity.COMMON;
        this.setCreativeTab(IC2.tabIC2);
        if (name != null) {
            this.setUnlocalizedName(name.name());
            BlocksItems.registerItem(this, IC2.getIdentifier(name.name()));
            name.setInstance(this);
        }

    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, ItemName name, String extraName) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name, extraName));
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(ItemName name, String extraName) {
        StringBuilder loc = new StringBuilder();
        loc.append("ic2");
        loc.append(':');
        loc.append(name.getPath(extraName));
        return new ModelResourceLocation(loc.toString(), null);
    }

    public static boolean shouldReequip(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (!StackUtil.checkItemEquality(newStack, oldStack)) {
            return true;
        } else if (oldStack == null) {
            return false;
        } else if (StackUtil.getSize(oldStack) != StackUtil.getSize(newStack)) {
            return true;
        } else {
            return slotChanged && StackUtil.checkItemEqualityStrict(oldStack, newStack);
        }
    }

    protected static int getRemainingUses(ItemStack stack) {
        return stack.getMaxDamage() - stack.getItemDamage() + 1;
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(ItemName name) {
        this.registerModel(0, name, null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, ItemName name) {
        registerModel(this, meta, name, null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, ItemName name, String extraName) {
        registerModel(this, meta, name, extraName);
    }

    @SideOnly(Side.CLIENT)
    public int getItemColor(ItemStack stack) {
        return 16777215;
    }

    public String getUnlocalizedName() {
        return "ic2." + super.getUnlocalizedName().substring(5);
    }

    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName();
    }

    public String getUnlocalizedNameInefficiently(ItemStack stack) {
        return this.getUnlocalizedName(stack);
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return Localization.translate(this.getUnlocalizedName(stack));
    }

    protected boolean isInCreativeTab(CreativeTabs tab) {
        return this.isEnabled() && super.isInCreativeTab(tab);
    }

    protected boolean isEnabled() {
        return Version.shouldEnable(this.getClass());
    }

    public ItemIU setRarity(EnumRarity rarity) {
        if (rarity == null) {
            throw new NullPointerException("null rarity");
        } else {
            this.rarity = rarity;
            return this;
        }
    }

    public EnumRarity getRarity(ItemStack stack) {
        return stack.isItemEnchanted() && this.rarity != EnumRarity.EPIC ? EnumRarity.RARE : this.rarity;
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return shouldReequip(oldStack, newStack, slotChanged);
    }

    public <T> void addCapability(Capability<T> cap, Function<ItemStack, T> lookup) {
        if (this.caps == null) {
            this.caps = new IdentityHashMap();
        }

        assert !this.caps.containsKey(cap);

        this.caps.put(cap, lookup);
    }


    public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ICapabilityProvider() {
            public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                return ItemIU.this.caps != null && ItemIU.this.caps.containsKey(capability);
            }

            public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
                return ItemIU.this.caps != null && ItemIU.this.caps.containsKey(capability)
                        ? (T) ((Function) ItemIU.this.caps.get(capability)).apply(stack)
                        : null;
            }
        };
    }

}
