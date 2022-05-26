package com.denfop.items.resource;

import ic2.core.block.state.EnumProperty;
import ic2.core.block.state.IIdProvider;
import ic2.core.item.ItemIC2;
import ic2.core.ref.IMultiItem;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ItemMulti<T extends Enum<T> & IIdProvider> extends ItemIC2 implements IMultiItem<T> {

    protected final EnumProperty<T> typeProperty;
    private final Map<T, ItemMulti.IItemRightClickHandler> rightClickHandlers;
    private final Map<T, ItemMulti.IItemUseHandler> useHandlers;
    private final Map<T, ItemMulti.IItemUpdateHandler> updateHandlers;
    private final Map<T, EnumRarity> rarityFilter;

    private ItemMulti(ItemName name, EnumProperty<T> typeProperty) {
        super(name);
        this.rightClickHandlers = new IdentityHashMap();
        this.useHandlers = new IdentityHashMap();
        this.updateHandlers = new IdentityHashMap();
        this.rarityFilter = new IdentityHashMap();
        this.typeProperty = typeProperty;
        this.setHasSubtypes(true);
    }

    protected ItemMulti(ItemName name, Class<T> typeClass) {
        this(name, new EnumProperty("type", typeClass));
    }

    public static <T extends Enum<T> & IIdProvider> ItemMulti<T> create(ItemName name, Class<T> typeClass) {
        EnumProperty<T> typeProperty = new EnumProperty("type", typeClass);
        if (typeProperty.getAllowedValues().size() > 32767) {
            throw new IllegalArgumentException("Too many values to fit in a short for " + typeClass);
        } else {
            return new ItemMulti(name, typeProperty);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(ItemName name) {
        Iterator var2 = this.typeProperty.getAllowedValues().iterator();

        while (var2.hasNext()) {
            T type = (T) var2.next();
            this.registerModel(type.getId(), name, type.getModelName());
        }

    }

    @SideOnly(Side.CLIENT)
    public final int getItemColor(ItemStack stack) {
        T type = this.getType(stack);
        return type == null ? super.getItemColor(stack) : type.getColor();
    }

    public String getUnlocalizedName(ItemStack stack) {
        T type = this.getType(stack);
        return type == null
                ? super.getUnlocalizedName(stack)
                : super.getUnlocalizedName(stack) + "." + type.getName();
    }

    public ItemStack getItemStack(T type) {
        if (!this.typeProperty.getAllowedValues().contains(type)) {
            throw new IllegalArgumentException("invalid property value " + type + " for property " + this.typeProperty);
        } else {
            return this.getItemStackUnchecked(type);
        }
    }

    protected ItemStack getItemStackUnchecked(T type) {
        return new ItemStack(this, 1, type.getId());
    }

    public ItemStack getItemStack(String variant) {
        T type = this.typeProperty.getValue(variant);
        if (type == null) {
            throw new IllegalArgumentException("invalid variant " + variant + " for " + this);
        } else {
            return this.getItemStackUnchecked(type);
        }
    }

    public String getVariant(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null stack");
        } else if (stack.getItem() != this) {
            throw new IllegalArgumentException("The stack " + stack + " doesn't match " + this);
        } else {
            T type = this.getType(stack);
            if (type == null) {
                throw new IllegalArgumentException("The stack " + stack + " doesn't reference any valid subtype");
            } else {
                return type.getName();
            }
        }
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            Iterator var3 = this.typeProperty.getShownValues().iterator();

            while (var3.hasNext()) {
                T type = (T) var3.next();
                subItems.add(this.getItemStackUnchecked(type));
            }

        }
    }

    public Set<T> getAllTypes() {
        return EnumSet.allOf(this.typeProperty.getValueClass());
    }

    public final T getType(ItemStack stack) {
        return this.typeProperty.getValue(stack.getMetadata());
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = StackUtil.get(player, hand);
        T type = this.getType(stack);
        if (type == null) {
            return new ActionResult(EnumActionResult.PASS, stack);
        } else {
            ItemMulti.IItemRightClickHandler handler = this.rightClickHandlers.get(type);
            return handler == null ? new ActionResult(EnumActionResult.PASS, stack) : handler.onRightClick(stack, player, hand);
        }
    }

    public EnumActionResult onItemUse(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumHand hand,
            EnumFacing side,
            float hitX,
            float hitY,
            float hitZ
    ) {
        ItemStack stack = StackUtil.get(player, hand);
        T type = this.getType(stack);
        if (type == null) {
            return EnumActionResult.PASS;
        } else {
            ItemMulti.IItemUseHandler handler = this.useHandlers.get(type);
            return handler == null ? EnumActionResult.PASS : handler.onUse(stack, player, pos, hand, side);
        }
    }

    public void onUpdate(ItemStack stack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {
        T type = this.getType(stack);
        if (type != null) {
            ItemMulti.IItemUpdateHandler handler = this.updateHandlers.get(type);
            if (handler != null) {
                handler.onUpdate(stack, world, entity, slotIndex, isCurrentItem);
            }
        }
    }

    public EnumRarity getRarity(ItemStack stack) {
        EnumRarity rarity = this.rarityFilter.get(this.getType(stack));
        return rarity != null ? rarity : super.getRarity(stack);
    }

    public void setRightClickHandler(T type, ItemMulti.IItemRightClickHandler handler) {
        if (type == null) {
            Iterator var3 = this.typeProperty.getAllowedValues().iterator();

            while (var3.hasNext()) {
                T cType = (T) var3.next();
                this.setRightClickHandler(cType, handler);
            }
        } else {
            this.rightClickHandlers.put(type, handler);
        }

    }

    public void setUseHandler(T type, ItemMulti.IItemUseHandler handler) {
        if (type == null) {
            Iterator var3 = this.typeProperty.getAllowedValues().iterator();

            while (var3.hasNext()) {
                T cType = (T) var3.next();
                this.setUseHandler(cType, handler);
            }
        } else {
            this.useHandlers.put(type, handler);
        }

    }

    public void setUpdateHandler(T type, ItemMulti.IItemUpdateHandler handler) {
        if (type == null) {
            Iterator var3 = this.typeProperty.getAllowedValues().iterator();

            while (var3.hasNext()) {
                T cType = (T) var3.next();
                this.setUpdateHandler(cType, handler);
            }
        } else {
            this.updateHandlers.put(type, handler);
        }

    }

    public void setRarity(T type, EnumRarity rarity) {
        if (type == null) {
            this.setRarity(rarity);
        } else {
            this.rarityFilter.put(type, rarity);
        }

    }

    public interface IItemUpdateHandler {

        void onUpdate(ItemStack var1, World var2, Entity var3, int var4, boolean var5);

    }

    public interface IItemUseHandler {

        EnumActionResult onUse(ItemStack var1, EntityPlayer var2, BlockPos var3, EnumHand var4, EnumFacing var5);

    }

    public interface IItemRightClickHandler {

        ActionResult<ItemStack> onRightClick(ItemStack var1, EntityPlayer var2, EnumHand var3);

    }

}
