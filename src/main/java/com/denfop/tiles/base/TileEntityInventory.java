//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.tiles.base;

import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.ITeBlock;
import ic2.core.block.TeBlockRegistry;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlot.InvSide;
import ic2.core.block.invslot.InvSlotUpgrade;
import ic2.core.util.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class TileEntityInventory extends TileEntityBlock implements ISidedInventory,
        IInventorySlotHolder<TileEntityInventory> {

    protected final List<InvSlot> invSlots = new ArrayList<>();
    protected final List<InfoInvSlots> infoInvSlotsList = new ArrayList<>();
    private final IItemHandler[] itemHandler;
    protected int size_inventory;

    public TileEntityInventory() {
        this.itemHandler = new IItemHandler[EnumFacing.VALUES.length + 1];
    }

    public static int getIndex(int loc) {
        return loc >>> 16;
    }

    protected static int calcRedstoneFromInvSlots(InvSlot... slots) {
        return calcRedstoneFromInvSlots(Arrays.asList(slots));
    }

    protected static int calcRedstoneFromInvSlots(Iterable<InvSlot> invSlots) {
        int space = 0;
        int used = 0;
        Iterator<InvSlot> var3 = invSlots.iterator();

        while (true) {
            InvSlot slot;
            do {
                if (!var3.hasNext()) {
                    if (used != 0 && space != 0) {
                        return 1 + used * 14 / space;
                    }

                    return 0;
                }

                slot = var3.next();
            } while (slot instanceof InvSlotUpgrade);

            int size = slot.size();
            int limit = slot.getStackSizeLimit();
            space += size * limit;

            for (int i = 0; i < size; ++i) {
                ItemStack stack = slot.get(i);
                if (!StackUtil.isEmpty(stack)) {
                    used += Math.min(limit, stack.getCount() * limit / stack.getMaxStackSize());
                }
            }
        }
    }

    protected boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        final boolean action = ForgeHooks
                .onRightClickBlock(player, hand, this.pos, side, new Vec3d(hitX, hitY, hitZ))
                .isCanceled();
        if (!action) {
            ItemStack stack = player.getHeldItem(hand);
            if (stack.getItem() instanceof IUpgradeItem) {
                if (this instanceof IUpgradableBlock) {
                    IUpgradeItem iUpgradeItem = (IUpgradeItem) stack.getItem();
                    IUpgradableBlock upgradableBlock = (IUpgradableBlock) this;
                    if (iUpgradeItem.isSuitableFor(stack, upgradableBlock.getUpgradableProperties())) {
                        for (final InvSlot invslot : this.invSlots) {
                            if (invslot instanceof com.denfop.invslot.InvSlotUpgrade) {
                                com.denfop.invslot.InvSlotUpgrade upgrade = (com.denfop.invslot.InvSlotUpgrade) invslot;
                                if (upgrade.add(stack)) {
                                    stack.setCount(0);
                                    player.openContainer.detectAndSendChanges();
                                    return true;
                                }
                                break;
                            }
                        }
                    }
                }
            }

            if (this instanceof IHasGui) {
                return this.getWorld().isRemote || IC2.platform.launchGui(player, (IHasGui) this);
            } else {
                return false;
            }
        }
        return false;
    }

    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        NBTTagCompound invSlotsTag = nbtTagCompound.getCompoundTag("InvSlots");

        for (final InvSlot invSlot : this.invSlots) {
            invSlot.readFromNbt(invSlotsTag.getCompoundTag(invSlot.name));
        }

    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        infoInvSlotsList.clear();
        for (InvSlot slot : this.invSlots) {
            for (int k = 0; k < slot.size(); k++) {
                infoInvSlotsList.add(new InfoInvSlots(slot, k));
            }

        }
        this.size_inventory = 0;

        InvSlot invSlot;
        for (Iterator<InvSlot> var2 = this.invSlots.iterator(); var2.hasNext(); size_inventory += invSlot.size()) {
            invSlot = var2.next();
        }

    }

    public List<InvSlot> getInvSlots() {
        return invSlots;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagCompound invSlotsTag = new NBTTagCompound();

        for (final InvSlot invSlot : this.invSlots) {
            NBTTagCompound invSlotTag = new NBTTagCompound();
            invSlot.writeToNbt(invSlotTag);
            invSlotsTag.setTag(invSlot.name, invSlotTag);
        }

        nbt.setTag("InvSlots", invSlotsTag);
        return nbt;
    }

    public int getSizeInventory() {
        if (size_inventory == 0 && this.invSlots.size() != 0) {
            InvSlot invSlot;
            for (Iterator<InvSlot> var2 = this.invSlots.iterator(); var2.hasNext(); size_inventory += invSlot.size()) {
                invSlot = var2.next();
            }
        }

        return size_inventory;
    }

    public boolean isEmpty() {

        return this.invSlots.isEmpty();

    }

    @Nonnull
    public ItemStack getStackInSlot(int index) {
        final InfoInvSlots loc = this.locateInfoInvSlot(index);
        return loc == null ? StackUtil.emptyStack : loc.getSlot().get(loc.getIndex());
    }

    @Nonnull
    public ItemStack decrStackSize(int index, int amount) {
        final InfoInvSlots loc = this.locateInfoInvSlot(index);
        if (loc == null) {
            return StackUtil.emptyStack;
        } else {
            ItemStack stack = loc.getSlot().get(loc.getIndex());
            if (StackUtil.isEmpty(stack)) {
                return StackUtil.emptyStack;
            } else if (amount >= StackUtil.getSize(stack)) {
                this.putStackAt(loc, StackUtil.emptyStack);
                return stack;
            } else {
                if (amount != 0) {
                    if (amount < 0) {
                        int space = Math.min(
                                loc.getSlot().getStackSizeLimit(),
                                stack.getMaxStackSize()
                        ) - StackUtil.getSize(stack);
                        amount = Math.max(amount, -space);
                    }

                    this.putStackAt(loc, StackUtil.decSize(stack, amount));
                }

                ItemStack ret = stack.copy();
                ret = StackUtil.setSize(ret, amount);
                return ret;
            }
        }
    }

    @Nonnull
    public ItemStack removeStackFromSlot(int index) {
        final InfoInvSlots loc = this.locateInfoInvSlot(index);
        if (loc == null) {
            return StackUtil.emptyStack;
        } else {
            ItemStack ret = loc.getSlot().get(loc.getIndex());
            if (!StackUtil.isEmpty(ret)) {
                this.putStackAt(loc, StackUtil.emptyStack);
            }

            return ret;
        }
    }

    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        final InfoInvSlots loc = this.locateInfoInvSlot(index);
        if (loc == null) {
            assert false;

        } else {
            if (StackUtil.isEmpty(stack)) {
                stack = StackUtil.emptyStack;
            }

            this.putStackAt(loc, stack);
        }
    }

    public void markDirty() {
        super.markDirty();

        for (final InvSlot invSlot : this.invSlots) {
            invSlot.onChanged();
        }

    }

    @Nonnull
    public String getName() {
        ITeBlock teBlock = TeBlockRegistry.get(this.getClass());
        String name = teBlock == null ? "invalid" : teBlock.getName();
        return this.getBlockType().getUnlocalizedName() + "." + name;
    }

    public boolean hasCustomName() {
        return false;
    }

    @Nonnull
    public ITextComponent getDisplayName() {
        return new TextComponentString(this.getName());
    }

    public int getInventoryStackLimit() {


        return 64;
    }

    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return !this.isInvalid() && player.getDistanceSq(this.pos) <= 64.0D;
    }

    public void openInventory(@Nonnull EntityPlayer player) {
    }

    public void closeInventory(@Nonnull EntityPlayer player) {
    }

    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        } else {
            InvSlot invSlot = this.getInventorySlot(index);
            return invSlot != null && invSlot.canInput() && invSlot.accepts(stack);
        }
    }

    @Nonnull
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        int[] ret = new int[this.getSizeInventory()];

        for (int i = 0; i < this.getSizeInventory(); i++) {
            ret[i] = i;
        }

        return ret;
    }

    public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing side) {
        if (StackUtil.isEmpty(stack)) {
            return false;
        } else {
            InvSlot targetSlot = this.getInventorySlot(index);
            if (targetSlot == null) {
                return false;
            } else if (targetSlot.canInput() && targetSlot.accepts(stack)) {
                if (targetSlot.preferredSide != InvSide.ANY && targetSlot.preferredSide.matches(side)) {
                    return true;
                } else {
                    return targetSlot.preferredSide == InvSide.ANY;
                }
            } else {
                return false;
            }
        }
    }

    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing side) {
        InvSlot targetSlot = this.getInventorySlot(index);
        return targetSlot != null && targetSlot.canOutput();
    }

    public int getField(int id) {
        return 0;
    }

    public void setField(int id, int value) {
    }

    public int getFieldCount() {
        return 0;
    }

    public void clear() {

        for (final InvSlot invSlot : this.invSlots) {
            invSlot.clear();
        }

    }

    public int getBaseIndex(InvSlot invSlot) {
        int ret = 0;

        InvSlot slot;
        for (Iterator<InvSlot> var3 = this.invSlots.iterator(); var3.hasNext(); ret += slot.size()) {
            slot = var3.next();
            if (slot == invSlot) {
                return ret;
            }
        }

        return -1;
    }

    public TileEntityInventory getParent() {
        return this;
    }

    public InvSlot getInventorySlot(String name) {
        Iterator<InvSlot> var2 = this.invSlots.iterator();

        InvSlot invSlot;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            invSlot = var2.next();
        } while (!invSlot.name.equals(name));

        return invSlot;
    }

    public void addInventorySlot(InvSlot inventorySlot) {
        assert this.invSlots.stream().noneMatch((slot) -> slot.name.equals(inventorySlot.name));

        this.invSlots.add(inventorySlot);
    }

    public InfoInvSlots locateInfoInvSlot(int extIndex) {
        try {
            return this.infoInvSlotsList.get(extIndex);
        } catch (Exception e) {
            return null;
        }
    }

    public InvSlot getAt(int loc) {
        assert loc != -1;

        return this.invSlots.get(getIndex(loc));
    }

    private void putStackAt(InfoInvSlots loc, ItemStack stack) {
        loc.getSlot().put(loc.getIndex(), stack);
        super.markDirty();
    }

    protected InvSlot getInventorySlot(int extIndex) {
        final InfoInvSlots loc = this.locateInfoInvSlot(extIndex);
        return loc == null ? null : loc.getSlot();
    }

    public List<ItemStack> getAuxDrops(int fortune) {
        List<ItemStack> ret = new ArrayList<>(super.getAuxDrops(fortune));

        for (final InvSlot slot : this.invSlots) {
            for (final ItemStack stack : slot) {
                if (!StackUtil.isEmpty(stack)) {
                    ret.add(stack);
                }
            }
        }

        return ret;
    }

    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == null) {
                if (this.itemHandler[this.itemHandler.length - 1] == null) {
                    this.itemHandler[this.itemHandler.length - 1] = new InvWrapper(this);
                }

                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.itemHandler[this.itemHandler.length - 1]);
            } else {
                if (this.itemHandler[facing.ordinal()] == null) {
                    this.itemHandler[facing.ordinal()] = new SidedInvWrapper(this, facing);
                }

                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.itemHandler[facing.ordinal()]);
            }
        } else {
            return super.getCapability(capability, facing);
        }
    }

    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    protected int calcRedstoneFromInvSlots() {
        return calcRedstoneFromInvSlots(this.invSlots);
    }

}
