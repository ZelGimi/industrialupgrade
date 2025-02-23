package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import com.denfop.tiles.bee.FrameAttributeLevel;
import com.denfop.tiles.bee.IFrameItem;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class ItemFrame extends ItemSubTypes<ItemFrame.Types> implements IModelRegister, IFrameItem {

    protected static final String NAME = "frame";

    public ItemFrame() {
        super(Types.class);
        this.setCreativeTab(IUCore.ItemTab);
        setMaxStackSize(1);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(
            @Nonnull final ItemStack stack,
            @Nullable final World worldIn,
            @Nonnull final List<String> tooltip,
            @Nonnull final ITooltipFlag flagIn
    ) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(Localization.translate("iu.reactoritem.durability") + " " + (this.getMaxCustomDamage(stack) - this.getCustomDamage(
                stack)) + "/" + this.getMaxCustomDamage(stack));
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    @Override
    public FrameAttributeLevel getAttribute(final int meta) {
        return FrameAttributeLevel.values()[meta];
    }

    @Override
    public int getCustomDamage(final ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return 0;
        } else {
            assert stack.getTagCompound() != null;
            return stack.getTagCompound().getInteger("damage");
        }
    }

    @Override
    public int getMaxCustomDamage(final ItemStack var1) {
        return 72000;
    }

    @Override
    public void setCustomDamage(final ItemStack stack, int damage) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        if (damage > getMaxCustomDamage(stack)) {
            damage = getMaxCustomDamage(stack);
        }
        nbt.setInteger("damage", damage);
    }

    public boolean applyCustomDamage(ItemStack stack, int damage, EntityLivingBase src) {
        int damage1 = this.getCustomDamage(stack) - damage;
        if (damage1 <= 0) {
            damage1 = 0;
        }
        this.setCustomDamage(stack, damage1);
        return getMaxCustomDamage(stack) - damage1 == 0;
    }

    public boolean showDurabilityBar(@Nonnull ItemStack stack) {
        return true;
    }

    public double getDurabilityForDisplay(@Nonnull ItemStack stack) {
        return (double) this.getCustomDamage(stack) / (double) this.getMaxCustomDamage(stack);
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
        chance_healing_3;

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

        public int getId() {
            return this.ID;
        }
    }

}
