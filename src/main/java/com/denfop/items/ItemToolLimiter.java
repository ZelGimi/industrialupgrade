package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IAdvEnergyNet;
import com.denfop.api.IModelRegister;
import com.denfop.api.energy.LimitInfo;
import com.denfop.container.ContainerLimiter;
import com.denfop.gui.HandHeldLimiter;
import ic2.api.energy.EnergyNet;
import ic2.api.item.IBoxable;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.init.BlocksItems;
import ic2.core.item.IHandHeldInventory;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class ItemToolLimiter extends Item implements IBoxable, IHandHeldInventory, IModelRegister {

    public ItemToolLimiter() {
        this.maxStackSize = 1;
        this.setMaxDamage(0);
        this.setCreativeTab(IUCore.EnergyTab);
        BlocksItems.registerItem(this, IUCore.getIdentifier("limiter")).setUnlocalizedName("limiter");
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1() {
        final String loc = Constants.MOD_ID +
                ':' +
                "tool/limiter";

        return new ModelResourceLocation(loc, null);
    }

    @Override
    public void getSubItems(@Nonnull final CreativeTabs p_150895_1_, @Nonnull final NonNullList<ItemStack> var3) {
        if (this.isInCreativeTab(p_150895_1_)) {
            final ItemStack var4 = new ItemStack(this, 1);
            var3.add(var4);
        }
    }

    @Nonnull
    public String getUnlocalizedName() {
        return "iu.limiter_item" + ".name";
    }

    public @NotNull EnumActionResult onItemUseFirst(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumFacing side,
            float hitX,
            float hitY,
            float hitZ,
            EnumHand hand
    ) {
        if (world.isRemote) {
            return EnumActionResult.PASS;
        } else {


            return EnumActionResult.SUCCESS;
        }
    }

    public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {
        if (!player.getEntityWorld().isRemote && !StackUtil.isEmpty(stack) && player.openContainer instanceof ContainerLimiter) {
            HandHeldLimiter euReader = ((ContainerLimiter) player.openContainer).base;
            if (euReader.isThisContainer(stack)) {
                euReader.saveAsThrown(stack);
                player.closeScreen();
            }
        }

        return true;
    }

    public boolean canBeStoredInToolbox(ItemStack itemstack) {
        return true;
    }

    public IHasGui getInventory(EntityPlayer player, ItemStack stack) {
        return new HandHeldLimiter(player, stack);
    }

    @Override
    public void registerModels() {
        ModelLoader.setCustomMeshDefinition(this, stack -> getModelLocation1());
        ModelBakery.registerItemVariants(this, getModelLocation1());

    }

}
