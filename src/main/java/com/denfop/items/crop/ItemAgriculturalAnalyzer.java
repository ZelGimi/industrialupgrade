package com.denfop.items.crop;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.container.ContainerAgriculturalAnalyzer;
import com.denfop.items.IItemStackInventory;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemAgriculturalAnalyzer extends Item implements IItemStackInventory, IModelRegister {


    private final String internalName;

    public ItemAgriculturalAnalyzer(String internalName) {

        this.setCreativeTab(IUCore.EnergyTab);
        this.setMaxStackSize(1);
        this.internalName = internalName;
        IUCore.proxy.addIModelRegister(this);
        Register.registerItem((Item) this, IUCore.getIdentifier(internalName)).setUnlocalizedName(internalName);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "tools" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    public void save(ItemStack stack, EntityPlayer player) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setBoolean("open", true);
        nbt.setInteger("slot_inventory", player.inventory.currentItem);
    }

    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, EntityPlayer player) {
        if (!player.getEntityWorld().isRemote && !ModUtils.isEmpty(stack) && player.openContainer instanceof ContainerAgriculturalAnalyzer) {
            ItemStackAgriculturalAnalyzer toolbox = ((ContainerAgriculturalAnalyzer) player.openContainer).base;
            if (toolbox.isThisContainer(stack)) {
                toolbox.saveAndThrow(stack);
                player.closeScreen();
            }
        }

        return true;
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
        NBTTagCompound nbt = ModUtils.nbt(stack);


        if (nbt.getBoolean("open")) {
            int slot_id = nbt.getInteger("slot_inventory");
            if (slot_id != itemSlot && !player.getEntityWorld().isRemote && !ModUtils.isEmpty(stack) && player.openContainer instanceof ContainerAgriculturalAnalyzer) {
                ItemStackAgriculturalAnalyzer toolbox = ((ContainerAgriculturalAnalyzer) player.openContainer).base;
                if (toolbox.isThisContainer(stack)) {
                    toolbox.saveAsThrown(stack);
                    player.closeScreen();
                    nbt.setBoolean("open", false);
                }
            }
        }
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {

        ItemStack stack = ModUtils.get(player, hand);
        if (IUCore.proxy.isSimulating() && !player.isSneaking()) {
            save(stack, player);
            player.openGui(IUCore.instance, 1, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));

        }

        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    public IAdvInventory getInventory(EntityPlayer player, ItemStack stack) {
        return new ItemStackAgriculturalAnalyzer(player, stack, 1);
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

}
