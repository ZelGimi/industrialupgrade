package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.blocks.ISubEnum;
import com.denfop.tiles.bee.FrameAttributeLevel;
import com.denfop.tiles.bee.IFrameItem;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class ItemFrame<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IFrameItem {
    public ItemFrame(T element) {
        super(new Item.Properties().tab(IUCore.ItemTab).stacksTo(1), element);
    }

    @Override
    public FrameAttributeLevel getAttribute(final int meta) {
        return FrameAttributeLevel.values()[this.getElement().getId()];
    }

    @Override
    public int getCustomDamage(final ItemStack stack) {
        if (!stack.hasTag()) {
            return 0;
        } else {
            assert stack.getTag() != null;
            return stack.getTag().getInt("damage");
        }
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(Localization.translate("iu.reactoritem.durability") + " " + (this.getMaxCustomDamage(p_41421_) - this.getCustomDamage(
                p_41421_)) + "/" + this.getMaxCustomDamage(p_41421_)));
    }

    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return true;
    }

    public int getBarWidth(@Nonnull ItemStack stack) {
        return Math.round(13.0F - (float) ((double) this.getCustomDamage(stack) * 13.0F / (double) this.getMaxCustomDamage(stack)));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb((float) (Math.max(0.0F, 1.0F - (this.getCustomDamage(stack) / (double) this.getMaxCustomDamage(stack))) / 3.0F), 1.0F, 1.0F);
    }

    @Override
    public int getMaxCustomDamage(final ItemStack var1) {
        return 72000;
    }

    @Override
    public void setCustomDamage(final ItemStack stack, int damage) {
        CompoundTag nbt = ModUtils.nbt(stack);
        if (damage > getMaxCustomDamage(stack)) {
            damage = getMaxCustomDamage(stack);
        }
        nbt.putInt("damage", damage);
    }

    public boolean applyCustomDamage(ItemStack stack, int damage, LivingEntity src) {
        int damage1 = this.getCustomDamage(stack) - damage;
        if (damage1 <= 0) {
            damage1 = 0;
        }
        this.setCustomDamage(stack, damage1);
        return getMaxCustomDamage(stack) - damage1 == 0;
    }

    public enum Types implements ISubEnum {
        slow_aging_1,
        slow_aging_2,
        slow_aging_3,

        producing_1,
        producing_2,
        producing_3,

        speed_crop_1,
        speed_crop_2,
        speed_crop_3,

        speed_birth_rate_1,
        speed_birth_rate_2,
        speed_birth_rate_3,

        chance_crossing_1,
        chance_crossing_2,
        chance_crossing_3,
        storage_jelly_1,
        storage_jelly_2,
        storage_jelly_3,
        storage_food_1,
        storage_food_2,
        storage_food_3,
        chance_healing_1,
        chance_healing_2,
        chance_healing_3;;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        Types() {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = this.ordinal();
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "frame";
        }

        public int getId() {
            return this.ID;
        }
    }
}
