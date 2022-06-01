package com.denfop.items.bags;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.container.ContainerLeadBox;
import ic2.core.IC2;
import ic2.core.IC2Potion;
import ic2.core.IHasGui;
import ic2.core.init.BlocksItems;
import ic2.core.item.IHandHeldInventory;
import ic2.core.item.type.IRadioactiveItemType;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
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

public class ItemLeadBox extends Item implements IHandHeldInventory, IModelRegister {

    private final int slots;

    private final String internalName;

    public ItemLeadBox(String internalName) {

        this.setCreativeTab(IUCore.EnergyTab);
        this.setMaxStackSize(1);
        this.internalName = internalName;
        this.slots = 27;
        IUCore.proxy.addIModelRegister(this);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(internalName)).setUnlocalizedName(internalName);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "bags" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    @Override
    public void onUpdate(
            @Nonnull final ItemStack stack,
            @Nonnull final World worldIn,
            @Nonnull final Entity entityIn,
            final int itemSlot,
            final boolean isSelected
    ) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if (!(entityIn instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) entityIn;
        if (worldIn.provider.getWorldTime() % 40 == 0) {
            for (int i = 0; i < 36; i++) {
                final ItemStack stack1 = player.inventory.getStackInSlot(i);
                if (stack1.getItem() instanceof IRadioactiveItemType) {
                    HandHeldLeadBox box = (HandHeldLeadBox) getInventory(player, stack);
                    if (player.openContainer instanceof ContainerLeadBox) {
                        player.closeScreen();
                    }
                    if (box.canAdd(stack1)) {
                        box.add(stack1);
                        box.save();
                        player.removePotionEffect(IC2Potion.radiation);
                        player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                    }

                }
            }


        }
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

        ItemStack stack = StackUtil.get(player, hand);
        if (IC2.platform.isSimulating()) {
            IC2.platform.launchGui(player, this.getInventory(player, stack));
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));

        }

        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, EntityPlayer player) {
        if (!player.getEntityWorld().isRemote && !StackUtil.isEmpty(stack) && player.openContainer instanceof ContainerLeadBox) {
            HandHeldLeadBox toolbox = ((ContainerLeadBox) player.openContainer).base;
            if (toolbox.isThisContainer(stack)) {
                toolbox.saveAndThrow(stack);
                player.closeScreen();
            }
        }

        return true;
    }


    public IHasGui getInventory(EntityPlayer player, ItemStack stack) {
        return new HandHeldLeadBox(player, stack, this.slots);
    }


}
