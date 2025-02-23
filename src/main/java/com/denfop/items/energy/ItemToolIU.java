package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.register.Register;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public abstract class ItemToolIU extends ItemTool implements IModelRegister {

    protected final String name;
    protected EnumRarity rarity;

    protected ItemToolIU(
            String name,
            float damage,
            float speed,
            Set<Block> mineableBlocks
    ) {
        super(damage, speed, ToolMaterial.IRON, mineableBlocks);
        this.rarity = EnumRarity.COMMON;
        this.setMaxStackSize(1);
        this.setCreativeTab(IUCore.EnergyTab);
        this.name = name;


        if (name != null) {
            this.setUnlocalizedName(name);
            Register.registerItem(this, IUCore.getIdentifier(name));
        }
        IUCore.proxy.addIModelRegister(this);

    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {

        final String loc = Constants.MOD_ID +
                ':' +
                "energy" + "/" + name;
        return new ModelResourceLocation(loc, null);
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

    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(5);
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        return this.getUnlocalizedName();
    }

    public String getUnlocalizedNameInefficiently(ItemStack itemStack) {
        return this.getUnlocalizedName(itemStack);
    }

    public String getItemStackDisplayName(ItemStack itemStack) {
        return Localization.translate(this.getUnlocalizedName(itemStack));
    }

    protected boolean isInCreativeTab(CreativeTabs tab) {
        return super.isInCreativeTab(tab);
    }


    public boolean canHarvestBlock(IBlockState state, ItemStack itemStack) {


        return true;
    }

    public float getDestroySpeed(ItemStack itemStack, IBlockState state) {
        return this.canHarvestBlock(state, itemStack) ? this.efficiency : super.getDestroySpeed(itemStack, state);
    }

    public EnumRarity getRarity(ItemStack stack) {
        return stack.isItemEnchanted() && this.rarity != EnumRarity.EPIC ? EnumRarity.RARE : this.rarity;
    }


    public boolean canBeStoredInToolbox(ItemStack itemStack) {
        return true;
    }


}
