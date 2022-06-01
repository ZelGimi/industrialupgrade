package com.denfop.items.bags;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.container.ContainerBags;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemEnergyBags extends Item implements IHandHeldInventory, IElectricItem, IModelRegister {

    private final int slots;
    private final int maxStorage;
    private final int getTransferLimit;

    private final String internalName;

    public ItemEnergyBags(String internalName, int slots, int maxStorage, int getTransferLimit) {

        this.setCreativeTab(IUCore.EnergyTab);
        this.setMaxStackSize(1);
        setMaxDamage(27);
        this.internalName = internalName;
        this.slots = slots;

        this.getTransferLimit = getTransferLimit;
        this.maxStorage = maxStorage;
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
            ElectricItem.manager.charge(var4, 2.147483647E9, Integer.MAX_VALUE, true, false);
            var3.add(var4);
            var3.add(new ItemStack(this, 1, 27));
        }
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand) {
        if (ElectricItem.manager.canUse(player.getHeldItem(hand), 350)) {
            ElectricItem.manager.use(player.getHeldItem(hand), 350, player);
            ItemStack stack = StackUtil.get(player, hand);
            if (IC2.platform.isSimulating()) {
                IC2.platform.launchGui(player, this.getInventory(player, stack));
                return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));

            }
        }
        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, EntityPlayer player) {
        if (!player.getEntityWorld().isRemote && !StackUtil.isEmpty(stack) && player.openContainer instanceof ContainerBags) {
            HandHeldBags toolbox = ((ContainerBags) player.openContainer).base;
            if (toolbox.isThisContainer(stack)) {
                toolbox.saveAndThrow(stack);
                player.closeScreen();
            }
        }

        return true;
    }


    public IHasGui getInventory(EntityPlayer player, ItemStack stack) {
        return new HandHeldBags(player, stack, slots);
    }

    @Override
    public boolean canProvideEnergy(final ItemStack itemStack) {
        return true;
    }

    @Override
    public double getMaxCharge(final ItemStack itemStack) {
        return this.maxStorage;
    }

    @Override
    public int getTier(final ItemStack itemStack) {
        return 2;
    }

    @Override
    public double getTransferLimit(final ItemStack itemStack) {
        return getTransferLimit;
    }

}
