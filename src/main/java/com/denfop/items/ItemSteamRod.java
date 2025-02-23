package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.steam.ISteamBlade;
import com.denfop.items.reactors.ItemDamage;
import com.denfop.register.Register;
import com.denfop.tiles.reactors.graphite.ICapacitorItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemSteamRod extends ItemDamage implements IModelRegister, ISteamBlade {

    private final double percent;
    private final int level;
    private final String name;
    private final ResourceLocation resourceLocation;

    public ItemSteamRod(final String name, int level, double percent, int damage, ResourceLocation resourceLocation) {
        super(null, damage);
        this.percent = percent;
        this.level = level;
        this.setNoRepair();
        this.name = name;
        setMaxStackSize(1);
        this.resourceLocation=resourceLocation;
        this.setCreativeTab(IUCore.ItemTab);
        this.setUnlocalizedName("steam_blade_" + name);
        Register.registerItem(this, IUCore.getIdentifier("steam_blade_" + name));
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {

        final String loc = Constants.MOD_ID +
                ':' +
                "steam_blade" + "/" + name;
        return new ModelResourceLocation(loc, null);
    }


    public int getLevel() {
        return level;
    }

    @Override
    public double getCoef() {
        return percent;
    }

    @Override
    public boolean damageBlade(final ItemStack stack) {
     return applyCustomDamage(stack, -1, null);
    }

    @Override
    public ResourceLocation getTexture() {
        return resourceLocation;
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(String name) {
        this.registerModel(0, name, null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name) {
        registerModel(this, meta, name);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name, String extraName) {
        registerModel(this, meta, name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation("steam_blade_" + name));
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item", "iu").replace(".name", ""));
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
        tooltip.add(Localization.translate("reactor.component_level") + (this.level + 1));

    }

}
