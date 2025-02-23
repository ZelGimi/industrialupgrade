package com.denfop.items.crop;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.agriculture.ICrop;
import com.denfop.api.agriculture.ICropItem;
import com.denfop.api.agriculture.genetics.Genome;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class ItemCrops extends ItemSubTypes<ItemCrops.Types> implements IModelRegister, ICropItem {

    protected static final String NAME = "crops";

    public ItemCrops() {
        super(Types.class);
        this.setCreativeTab(IUCore.CropsTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public void onUpdate(
            @Nonnull ItemStack itemStack,
            @Nonnull World p_77663_2_,
            @Nonnull Entity p_77663_3_,
            int p_77663_4_,
            boolean p_77663_5_
    ) {
        if (!(p_77663_3_ instanceof EntityPlayer)) {
            return;
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack p_77624_1_,
            @Nullable final World p_77624_2_,
            final List<String> p_77624_3_,
            final ITooltipFlag p_77624_4_
    ) {
        p_77624_3_.add(Localization.translate("iu.use_agriculture_analyzer") + Localization.translate(IUItem.agricultural_analyzer.getUnlocalizedName()));
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
    }



    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, final int meta, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(
                        Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(),
                        null
                )
        );
    }

    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final ICrop crop = CropNetwork.instance.getCrop(nbt.getInteger("crop_id"));
        return super.getItemStackDisplayName(stack) + ": " + Localization.translate("crop."+crop.getName());
    }

    @Override
    public ICrop getCrop(final int meta, final ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        return CropNetwork.instance.getCrop(nbt.getInteger("crop_id"));
    }
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            CropNetwork.instance.getCropMap().forEach((id,crop) -> {
                ItemStack stack = new ItemStack(this);
                final NBTTagCompound nbt = ModUtils.nbt(stack);
                nbt.setInteger("crop_id",id);
                new Genome(stack);
                subItems.add(stack);
            });
        }
    }
    public enum Types implements ISubEnum {
        seeds,
        ;

        private final String name;
        private final int ID;

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
