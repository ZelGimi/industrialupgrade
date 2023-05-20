package com.denfop.items.bags;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.inv.IHasGui;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.container.ContainerBags;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IHandHeldInventory;
import com.denfop.utils.ModUtils;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemEnergyBags extends Item implements IHandHeldInventory, IUpgradeItem, IElectricItem, IModelRegister {

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
        UpgradeSystem.system.addRecipe(this, EnumUpgrades.BAGS.list);

    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "bags" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    public int getItemEnchantability() {
        return 0;
    }

    public boolean isBookEnchantable(@Nonnull ItemStack stack, @Nonnull ItemStack book) {
        return false;
    }

    @Override
    public void onUpdate(
            final ItemStack stack,
            final World worldIn,
            final Entity entityIn,
            final int itemSlot,
            final boolean isSelected
    ) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if (nbt.getBoolean("open")) {
                int slot_id = nbt.getInteger("slot_inventory");
                if (slot_id != itemSlot && !player.getEntityWorld().isRemote && !StackUtil.isEmpty(stack) && player.openContainer instanceof ContainerBags) {
                    HandHeldBags toolbox = ((ContainerBags) player.openContainer).base;
                    if (toolbox.isThisContainer(stack)) {
                        toolbox.saveAsThrown(stack);
                        player.closeScreen();
                        nbt.setBoolean("open", false);
                    }
                }
            }
        }
        if (!UpgradeSystem.system.hasInMap(stack)) {
            nbt.setBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(worldIn, this, stack));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            final ITooltipFlag flagIn
    ) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        } else {
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            if (!nbt.hasKey("bag")) {
                return;
            }
            List<BagsDescription> list = new ArrayList<>();
            final NBTTagCompound nbt1 = nbt.getCompoundTag("bag");
            int size = nbt1.getInteger("size");
            for (int i = 0; i < size; i++) {
                list.add(new BagsDescription(nbt1.getCompoundTag(String.valueOf(i))));
            }
            for (BagsDescription description : list) {
                tooltip.add(TextFormatting.GREEN + "" + description.getCount() + "x " + description.getStack().getDisplayName());
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
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
        double coef = 1D - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, player.getHeldItem(hand)) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, player.getHeldItem(hand)).number * 0.25D : 0);

        if (ElectricItem.manager.canUse(player.getHeldItem(hand), 350 * coef)) {
            ElectricItem.manager.use(player.getHeldItem(hand), 350 * coef, player);
            ItemStack stack = StackUtil.get(player, hand);
            if (IC2.platform.isSimulating()) {
                save(stack, player);
                IUCore.proxy.launchGui(player, this.getInventory(player, stack));
                return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));

            }
        }
        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    @Override
    public @NotNull EnumActionResult onItemUseFirst(
            final @NotNull EntityPlayer player,
            final @NotNull World world,
            final @NotNull BlockPos pos,
            final @NotNull EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ,
            final @NotNull EnumHand hand
    ) {
        final TileEntity tile = world.getTileEntity(pos);
        if (player.isSneaking()) {
            if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side)) {
                IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
                HandHeldBags box = (HandHeldBags) getInventory(player, player.getHeldItem(hand));
                ItemStack[] itemStackList = box.getAll();
                for (ItemStack stack : itemStackList) {
                    if (stack == null || stack.isEmpty()) {
                        continue;
                    }
                    ModUtils.tick(itemStackList, handler, box);
                }
                return EnumActionResult.SUCCESS;
            }
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    public void save(ItemStack stack, EntityPlayer player) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setBoolean("open", true);
        nbt.setInteger("slot_inventory", player.inventory.currentItem);
    }

    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, EntityPlayer player) {
        if (!player.getEntityWorld().isRemote && !StackUtil.isEmpty(stack) && player.openContainer instanceof ContainerBags) {
            HandHeldBags toolbox = ((ContainerBags) player.openContainer).base;
            if (toolbox.isThisContainer(stack)) {
                toolbox.saveAsThrown(stack);
                player.closeScreen();
            }
        }

        return true;
    }

    public boolean canInsert(EntityPlayer player, ItemStack stack, ItemStack stack1) {
        HandHeldBags box = (HandHeldBags) getInventory(player, stack);
        return box.canAdd(stack1);
    }

    public void insert(EntityPlayer player, ItemStack stack, ItemStack stack1) {
        HandHeldBags box = (HandHeldBags) getInventory(player, stack);
        box.add(stack1);
        box.markDirty();
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
