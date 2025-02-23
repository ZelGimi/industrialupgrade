package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.items.reactors.ItemDamage;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemMesh extends ItemDamage {

    private final int level;

    public ItemMesh(final String name, final int maxDamage, int level) {
        super(name, maxDamage);
        this.level = level;
        this.setCreativeTab(IUCore.ItemTab);
        setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {

        final String loc = Constants.MOD_ID +
                ':' +
                "mesh" + "/" + name;
        return new ModelResourceLocation(loc, null);
    }

    @Override
    public void addInformation(
            @Nonnull final ItemStack stack,
            final World world,
            @Nonnull final List<String> tooltip,
            @Nonnull final ITooltipFlag advanced
    ) {
        super.addInformation(stack, world, tooltip, advanced);
        tooltip.add(Localization.translate("iu.reactoritem.durability") + " " + (this.getMaxCustomDamage(stack) - this.getCustomDamage(
                stack)) + "/" + this.getMaxCustomDamage(stack));

    }

    public int getLevel() {
        return level;
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item", "iu").replace(".name", ""));
    }

}
