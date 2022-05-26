package com.denfop.items.book;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.init.BlocksItems;
import ic2.core.item.IHandHeldInventory;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBook extends Item implements IHandHeldInventory, IModelRegister {


    private final String internalName;

    public ItemBook(String internalName) {

        this.setCreativeTab(IUCore.ItemTab);
        this.setMaxStackSize(64);
        this.internalName = internalName;
        IUCore.proxy.addIModelRegister(this);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(internalName)).setUnlocalizedName(internalName);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name) {
        StringBuilder loc = new StringBuilder();
        loc.append(Constants.MOD_ID);
        loc.append(':');
        loc.append("book").append("/").append(name);

        return new ModelResourceLocation(loc.toString(), null);
    }

    public String getUnlocalizedName() {
        return "item." + this.internalName + ".name";
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> getModelLocation1(name));
        ModelBakery.registerItemVariants(this, getModelLocation1(name));
        ModelBakery.registerItemVariants(this, getModelLocation1(name));
    }

    @Override
    public void registerModels() {
        registerModels(this.internalName);
    }

    @Override
    public void getSubItems(final CreativeTabs p_150895_1_, final NonNullList<ItemStack> var3) {
        if (this.isInCreativeTab(p_150895_1_)) {
            final ItemStack var4 = new ItemStack(this, 1);
            var3.add(var4);
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

        ItemStack stack = StackUtil.get(player, hand);
        if (IC2.platform.isSimulating()) {
            IC2.platform.launchGui(player, this.getInventory(player, stack));
            return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));

        }

        return new ActionResult(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {

        if (!player.getEntityWorld().isRemote && !StackUtil.isEmpty(stack) && player.openContainer instanceof ContainerBook) {
            HandHeldBook toolbox = ((ContainerBook) player.openContainer).base;
            if (toolbox.isThisContainer(stack)) {
                toolbox.saveAndThrow(stack);
                player.closeScreen();
            }
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    public IHasGui getInventory(EntityPlayer player, ItemStack stack) {
        return new HandHeldBook(player, stack, this);
    }


}
