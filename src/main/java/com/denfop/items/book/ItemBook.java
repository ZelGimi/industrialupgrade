package com.denfop.items.book;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.items.IItemStackInventory;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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

import javax.annotation.Nonnull;

public class ItemBook extends Item implements IItemStackInventory, IModelRegister {


    private final String internalName;

    public ItemBook(String internalName) {

        this.setCreativeTab(IUCore.ItemTab);
        this.setMaxStackSize(1);
        this.internalName = internalName;
        IUCore.proxy.addIModelRegister(this);
        Register.registerItem((Item) this, IUCore.getIdentifier(internalName)).setUnlocalizedName(internalName);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "book" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    @Nonnull
    public String getUnlocalizedName() {
        return "item." + this.internalName + ".name";
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> getModelLocation1(name));
        ModelBakery.registerItemVariants(this, getModelLocation1(name));
    }

    @Override
    public void registerModels() {
        registerModels(this.internalName);
    }

    @Override
    public void getSubItems(@Nonnull final CreativeTabs p_150895_1_, @Nonnull final NonNullList<ItemStack> var3) {
        if (this.isInCreativeTab(p_150895_1_)) {
            final ItemStack var4 = new ItemStack(this, 1);
            var3.add(var4);
        }
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {

        if (IUCore.proxy.isSimulating()) {
            player.openGui(IUCore.instance, 1, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));

        }

        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, EntityPlayer player) {

        if (!player.getEntityWorld().isRemote && !ModUtils.isEmpty(stack) && player.openContainer instanceof ContainerBook) {
            ItemStackBook toolbox = ((ContainerBook) player.openContainer).base;
            if (toolbox.isThisContainer(stack)) {
                toolbox.saveAsThrown(stack);
                player.closeScreen();
            }
        }
        return true;
    }


    public IAdvInventory getInventory(EntityPlayer player, ItemStack stack) {
        return new ItemStackBook(player, stack);
    }


}
