package com.denfop.items.bags;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUPotion;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.container.ContainerLeadBox;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.reactors.IRadioactiveItemType;
import com.denfop.items.reactors.ItemBaseRod;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemLeadBox extends Item implements IItemStackInventory, IModelRegister {

    private final int slots;

    private final String internalName;

    public ItemLeadBox(String internalName) {

        this.setCreativeTab(IUCore.EnergyTab);
        this.setMaxStackSize(1);
        this.internalName = internalName;
        this.slots = 27;
        IUCore.proxy.addIModelRegister(this);
        Register.registerItem((Item) this, IUCore.getIdentifier(internalName)).setUnlocalizedName(internalName);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "bags" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    public boolean canInsert(EntityPlayer player, ItemStack stack, ItemStack stack1) {
        ItemStackLeadBox box = (ItemStackLeadBox) getInventory(player, stack);
        NBTTagCompound nbt = ModUtils.nbt(stack);
        boolean rod = nbt.getBoolean("rod");
        if (stack1.getItem() instanceof IRadioactiveItemType) {
            if (!rod) {
                if (stack1.getItem() instanceof ItemBaseRod) {
                    return false;
                }
            } else {
                return box.canAdd(stack1);
            }
        }
        return false;
    }

    public void insert(EntityPlayer player, ItemStack stack, ItemStack stack1) {
        ItemStackLeadBox box = (ItemStackLeadBox) getInventory(player, stack);
        box.add(stack1);
        box.markDirty();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            final ITooltipFlag flagIn
    ) {
        tooltip.add(Localization.translate("iu.radiationbox"));
        NBTTagCompound nbt = ModUtils.nbt(stack);
        boolean rod = nbt.getBoolean("rod");
        tooltip.add(Localization.translate("message.text.mode_no_instrument") + ": "
                + (rod ? Localization.translate("message.leadbox.enable") : Localization.translate("message.leadbox.disable")));
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.changemode_key") + Localization.translate(
                    "iu.changemode_rcm1") + " + SHIFT");
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
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
            if (slot_id != itemSlot && !player.getEntityWorld().isRemote && !ModUtils.isEmpty(stack) && player.openContainer instanceof ContainerLeadBox) {
                ItemStackLeadBox toolbox = ((ContainerLeadBox) player.openContainer).base;
                if (toolbox.isThisContainer(stack)) {
                    toolbox.saveAsThrown(stack);
                    player.closeScreen();
                    nbt.setBoolean("open", false);
                }
            }
        }

        if (worldIn.provider.getWorldTime() % 40 == 0) {
            if (!(player.openContainer instanceof ContainerLeadBox)) {
                boolean rod = nbt.getBoolean("rod");
                for (int i = 0; i < 36; i++) {
                    final ItemStack stack1 = player.inventory.getStackInSlot(i);
                    if (stack1.getItem() instanceof IRadioactiveItemType) {
                        if (!rod) {
                            if (stack1.getItem() instanceof ItemBaseRod) {
                                continue;
                            }
                        }
                        ItemStackLeadBox box = (ItemStackLeadBox) getInventory(player, stack);
                        if (box.canAdd(stack1)) {
                            box.add(stack1);
                            player.removePotionEffect(IUPotion.radiation);
                            player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                            player.inventoryContainer.detectAndSendChanges();
                            box.markDirty();
                        }

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

        ItemStack stack = ModUtils.get(player, hand);
        if (IUCore.proxy.isSimulating() && !player.isSneaking()) {
            save(stack, player);
            player.openGui(IUCore.instance, 1, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));

        } else if (IUCore.proxy.isSimulating() && player.isSneaking()) {
            NBTTagCompound nbt = ModUtils.nbt(stack);
            boolean rod = !nbt.getBoolean("rod");
            nbt.setBoolean("rod", rod);
            if (rod) {
                IUCore.proxy.messagePlayer(
                        player,
                        TextFormatting.GREEN + Localization.translate("message.text.mode_no_instrument") + ": "
                                + Localization.translate("message.leadbox.enable")
                );
            } else {
                IUCore.proxy.messagePlayer(
                        player,
                        TextFormatting.RED + Localization.translate("message.text.mode_no_instrument") + ": "
                                + Localization.translate("message.leadbox.disable")
                );
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    public void save(ItemStack stack, EntityPlayer player) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setBoolean("open", true);
        nbt.setInteger("slot_inventory", player.inventory.currentItem);
    }

    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, EntityPlayer player) {
        if (!player.getEntityWorld().isRemote && !ModUtils.isEmpty(stack) && player.openContainer instanceof ContainerLeadBox) {
            ItemStackLeadBox toolbox = ((ContainerLeadBox) player.openContainer).base;
            if (toolbox.isThisContainer(stack)) {
                toolbox.saveAndThrow(stack);
                player.closeScreen();
            }
        }

        return true;
    }


    public IAdvInventory getInventory(EntityPlayer player, ItemStack stack) {
        return new ItemStackLeadBox(player, stack, this.slots);
    }


}
