package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.api.inv.IHasGui;
import com.denfop.componets.*;
import com.denfop.componets.AbstractComponent;
import com.denfop.componets.client.ComponentClientEffectRender;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.INetworkDataProvider;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.block.ITeBlock;
import ic2.core.block.TeBlockRegistry;
import ic2.core.block.TileEntityBlock;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TileEntityInventory extends TileEntityBlock implements ISidedInventory,
        IInventorySlotHolder<TileEntityInventory>, INetworkDataProvider, ICapabilityProvider {

    protected final List<InvSlot> invSlots = new ArrayList<>();
    protected final List<InfoInvSlots> infoInvSlotsList = new ArrayList<>();
    protected final IItemHandler[] itemHandler;
    protected boolean isLoaded = false;
    protected int size_inventory;
    protected ComponentClientEffectRender componentClientEffectRender;
    protected Map<Capability<?>, AbstractComponent> capabilityComponents;
    protected List<AbstractComponent> componentList = new ArrayList<>();
    protected List<AbstractComponent> updatableComponents = new ArrayList<>();
    protected List<AbstractComponent> updateServerList = new ArrayList<>();
    protected List<AbstractComponent> updateClientList = new ArrayList<>();
    protected Map<String, AbstractComponent> advComponentMap = new HashMap<>();

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
        for (InvSlot slot : invSlots) {
            if (slot instanceof InvSlotUpgrade) {
                continue;
            }
            int size = slot.size();
            int limit = slot.getStackSizeLimit();
            space += size * limit;

            for (ItemStack stack : slot.gets()) {
                if (!stack.isEmpty()) {
                    used += Math.min(limit, stack.getCount() * limit / stack.getMaxStackSize());
                }
            }
        }
        if (used != 0 && space != 0) {
            return 1 + used * 14 / space;
        }

        return 0;
    }

    @SideOnly(Side.CLIENT)
    protected void updateEntityClient() {
        super.updateEntityClient();
        if (componentClientEffectRender != null) {
            componentClientEffectRender.render();
        }
        this.updateClientList.forEach(AbstractComponent::updateEntityClient);

    }

    @Override
    protected void onUnloaded() {
        this.componentList.forEach(AbstractComponent::onUnloaded);
        super.onUnloaded();
    }

    public NBTTagCompound getUpdateTag() {
        (IUCore.network.get(true)).sendInitialData(this);
        return new NBTTagCompound();
    }

    protected int getWeakPower(EnumFacing side) {
        BasicRedstoneComponent component = this.getComp(RedstoneEmitter.class);
        return component == null ? 0 : component.getLevel();
    }

    protected boolean canConnectRedstone(EnumFacing side) {
        return this.hasComp(RedstoneEmitter.class) || this.hasComp(Redstone.class);
    }

    protected int getComparatorInputOverride() {
        BasicRedstoneComponent component = this.getComp(ComparatorEmitter.class);
        return component == null ? 0 : component.getLevel();
    }

    public SPacketUpdateTileEntity getUpdatePacket() {
        (IUCore.network.get(true)).sendInitialData(this);
        return null;
    }

    public @NotNull BlockPos getBlockPos() {
        return this.pos;
    }

    public boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {


        final boolean action = ForgeHooks
                .onRightClickBlock(player, hand, this.pos, side, new Vec3d(hitX, hitY, hitZ))
                .isCanceled();
        if (!action) {
            this.componentList.forEach(AbstractComponent::onBlockActivated);
            ItemStack stack = player.getHeldItem(hand);
            if (!stack.isEmpty()) {
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
                                        this.markDirty();
                                        return true;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (this instanceof IHasGui) {

                return !this.getWorld().isRemote && IUCore.proxy.launchGui(player, (IHasGui) this);
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
        if (!this.componentList.isEmpty() && nbtTagCompound.hasKey("components", 10)) {
            NBTTagCompound componentsNbt = nbtTagCompound.getCompoundTag("components");
            for (int i = 0; i < this.componentList.size(); i++) {
                final AbstractComponent component = this.componentList.get(i);
                NBTTagCompound componentNbt = componentsNbt.getCompoundTag("component_" + i);
                component.readFromNbt(componentNbt);
            }
        }
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        this.componentList.forEach(AbstractComponent::onLoaded);
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
        if (!this.getWorld().isRemote) {
            IUCore.network.get(true).sendInitialData(this);
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
        if (!this.componentList.isEmpty()) {
            NBTTagCompound componentsNbt = new NBTTagCompound();

            for (int i = 0; i < this.componentList.size(); i++) {
                final AbstractComponent component = this.componentList.get(i);
                NBTTagCompound nbt1 = component.writeToNbt();
                if (nbt1 == null) {
                    nbt1 = new NBTTagCompound();
                }
                componentsNbt.setTag("component_" + i, nbt1);
            }
            nbt.setTag("components", componentsNbt);
        }
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
            return invSlot != null && invSlot.canInput() && invSlot.accepts(stack, this.locateInfoInvSlot(index).getIndex());
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
            } else if (targetSlot.canInput() && targetSlot.accepts(stack, this.locateInfoInvSlot(index).getIndex())) {
                if (targetSlot.preferredSide != InvSlot.InvSide.ANY && targetSlot.preferredSide.matches(side)) {
                    return true;
                } else {
                    return targetSlot.preferredSide == InvSlot.InvSide.ANY;
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
        for (AbstractComponent component : this.getComponentList()) {
            if(!component.getDrops().isEmpty())
                ret.addAll(component.getDrops());
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
            if (this.capabilityComponents == null) {
                return super.getCapability(capability, facing);
            } else {

                AbstractComponent comp = this.capabilityComponents.get(capability);
                return comp == null ? super.getCapability(capability, facing) : comp.getCapability(capability, facing);
            }
        }
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        for (AbstractComponent component : this.updateServerList) {
            component.updateEntityServer();
        }
        if (!isLoaded) {
            this.loadBeforeFirstUpdate();
        }
    }

    public void loadBeforeFirstUpdate() {
        isLoaded = true;
    }

    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        if (super.hasCapability(capability, facing)) {
            return true;
        } else if (this.capabilityComponents == null) {
            return false;
        } else {
            AbstractComponent comp = this.capabilityComponents.get(capability);
            return comp != null && comp.getProvidedCapabilities(facing).contains(capability);
        }

    }

    protected int calcRedstoneFromInvSlots() {
        return calcRedstoneFromInvSlots(this.invSlots);
    }

    protected final <T extends AbstractComponent> T addComponent(T component) {
        if (component == null) {
            throw new NullPointerException("null component");
        } else {

            componentList.add(component);
            advComponentMap.put(component.toString(), component);
            if (component.isClient()) {
                this.updateClientList.add(component);
            }
            if (component.isServer()) {
                this.updateServerList.add(component);
            }

            for (final Capability<?> capability : component.getProvidedCapabilities(null)) {
                this.addComponentCapability(capability, component);
            }

            return component;

        }
    }

    private void addComponentCapability(Capability<?> cap, AbstractComponent component) {
        if (this.capabilityComponents == null) {
            this.capabilityComponents = new IdentityHashMap<>();
        }

        AbstractComponent prev = this.capabilityComponents.put(cap, component);

        assert prev == null;

    }

    protected void onNeighborChange(Block neighbor, BlockPos neighborPos) {
        if (this.componentList != null) {

            for (final AbstractComponent component : componentList) {
                component.onNeighborChange(neighbor, neighborPos);
            }
        }

    }

    public boolean hasComp(Class<? extends AbstractComponent> cls) {
        for (final AbstractComponent component : componentList) {
            if (component.getClass() == cls) {
                return true;
            }
        }
        return false;
    }

    public List<AbstractComponent> getComponentList() {
        return componentList;
    }

    public ComponentClientEffectRender getComponentClientEffectRender() {
        return componentClientEffectRender;
    }

    public List<AbstractComponent> getUpdatableComponents() {
        return updatableComponents;
    }

    public <T extends AbstractComponent> T getComp(String cls) {
        for (AbstractComponent component : this.componentList) {
            if (component.toString().trim().equals(cls)) {
                return (T) component;
            }
        }
        return null;
    }

    public Map<Capability<?>, AbstractComponent> getCapabilityComponents() {
        return capabilityComponents;
    }

    public <T extends AbstractComponent> T getComp(Class<T> cls) {
        for (final AbstractComponent component : componentList) {
            if (component.getClass() == cls) {
                return (T) component;
            }
        }
        return null;
    }

    public final Iterable<? extends AbstractComponent> getComps() {
        return componentList;
    }

    @Override
    public List<String> getNetworkFields() {
        List<String> ret = new ArrayList<>(3);
        ret.add("teBlk=" + ("") + this.teBlock.getName());
        ret.add("active");
        ret.add("facing");
        return ret;
    }

}
