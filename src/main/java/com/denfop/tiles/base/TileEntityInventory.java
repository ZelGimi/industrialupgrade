package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.IUpgradeItem;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.componets.AbstractComponent;
import com.denfop.componets.ComponentPrivate;
import com.denfop.componets.Redstone;
import com.denfop.componets.client.ComponentClientEffectRender;
import com.denfop.container.ContainerBase;
import com.denfop.invslot.InvSlot;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class TileEntityInventory extends TileEntityBlock implements ISidedInventory,
        IAdvInventory<TileEntityInventory>, ICapabilityProvider {

    protected final List<InvSlot> invSlots = new ArrayList<>();
    protected final List<InfoInvSlots> infoInvSlotsList = new ArrayList<>();
    protected final IItemHandler[] itemHandler;
    private final ComponentPrivate componentPrivate;
    protected boolean isLoaded = false;
    protected int size_inventory;
    protected ComponentClientEffectRender componentClientEffectRender;
    private int[] slotsFace;

    public TileEntityInventory() {
        this.itemHandler = new IItemHandler[EnumFacing.VALUES.length + 1];
        componentPrivate = this.addComponent(new ComponentPrivate(this));
    }

    public static int getIndex(int loc) {
        return loc >>> 16;
    }


    public void onNetworkEvent(final int var1) {

    }

    @SideOnly(Side.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (componentClientEffectRender != null) {
            componentClientEffectRender.render();
        }
        this.updateClientList.forEach(AbstractComponent::updateEntityClient);

    }

    public boolean canUpgradeBlock() {
        for (AbstractComponent abstractComponent : this.componentList) {
            if (abstractComponent.canUpgradeBlock()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
    }


    public int getWeakPower(EnumFacing side) {
        return 0;
    }

    public boolean canConnectRedstone(EnumFacing side) {
        return this.hasComp(Redstone.class);
    }

    public int getComparatorInputOverride() {
        return 0;
    }


    public @NotNull BlockPos getBlockPos() {
        return this.pos;
    }

    public boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

        if (this.getWorld().isRemote) {
            return true;
        }

        final boolean action = ForgeHooks
                .onRightClickBlock(player, hand, this.pos, side, new Vec3d(hitX, hitY, hitZ))
                .isCanceled();
        if (!action) {
            final AtomicBoolean end = new AtomicBoolean(false);
            this.componentList.forEach(abstractComponent -> {
                end.set(end.get() || abstractComponent.onBlockActivated(player, hand));
            });
            if (end.get()) {
                return true;
            }
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

            player.openGui(IUCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }


    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        NBTTagCompound invSlotsTag = nbtTagCompound.getCompoundTag("InvSlots");

        for (final InvSlot invSlot : this.invSlots) {
            invSlot.readFromNbt(invSlotsTag.getCompoundTag(String.valueOf(this.invSlots.indexOf(invSlot))));
        }
    }

    @Override
    public void onLoaded() {
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
            invSlotsTag.setTag(String.valueOf(this.invSlots.indexOf(invSlot)), invSlotTag);
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
        return loc == null ? ModUtils.emptyStack : loc.getSlot().get(loc.getIndex());
    }

    @Nonnull
    public ItemStack decrStackSize(int index, int amount) {
        final InfoInvSlots loc = this.locateInfoInvSlot(index);
        if (loc == null) {
            return ModUtils.emptyStack;
        } else {
            ItemStack stack = loc.getSlot().get(loc.getIndex());
            if (ModUtils.isEmpty(stack)) {
                return ModUtils.emptyStack;
            } else if (amount >= ModUtils.getSize(stack)) {
                this.putStackAt(loc, ModUtils.emptyStack);
                return stack;
            } else {
                if (amount != 0) {
                    if (amount < 0) {
                        int space = Math.min(
                                loc.getSlot().getStackSizeLimit(),
                                stack.getMaxStackSize()
                        ) - ModUtils.getSize(stack);
                        amount = Math.max(amount, -space);
                    }

                    this.putStackAt(loc, ModUtils.decSize(stack, amount));
                }

                ItemStack ret = stack.copy();
                ret = ModUtils.setSize(ret, amount);
                return ret;
            }
        }
    }

    @Nonnull
    public ItemStack removeStackFromSlot(int index) {
        final InfoInvSlots loc = this.locateInfoInvSlot(index);
        if (loc == null) {
            return ModUtils.emptyStack;
        } else {
            ItemStack ret = loc.getSlot().get(loc.getIndex());
            if (!ModUtils.isEmpty(ret)) {
                this.putStackAt(loc, ModUtils.emptyStack);
            }

            return ret;
        }
    }

    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        final InfoInvSlots loc = this.locateInfoInvSlot(index);
        if (loc == null) {
            assert false;

        } else {
            if (ModUtils.isEmpty(stack)) {
                stack = ModUtils.emptyStack;
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
        String name = teBlock == null ? "invalid" : this.teBlock.getName();
        return this.getBlockType().getUnlocalizedName() + "." + name;
    }

    public boolean hasCustomName() {
        return false;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return null;
    }

    @Override
    public BlockTileEntity getBlock() {
        return null;
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
        if (this.slotsFace == null) {
            this.slotsFace = new int[this.getSizeInventory()];

            for (int i = 0; i < this.getSizeInventory(); i++) {
                this.slotsFace[i] = i;
            }

        }
        return this.slotsFace;
    }

    public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing side) {
        if (ModUtils.isEmpty(stack)) {
            return false;
        } else {
            InvSlot targetSlot = this.getInventorySlot(index);
            if (targetSlot == null) {
                return false;
            } else {
                return targetSlot.canInput() && targetSlot.accepts(stack, this.locateInfoInvSlot(index).getIndex());
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

    @Override
    public ContainerBase<?> getGuiContainer(final EntityPlayer var1) {
        return null;
    }

    @Override
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return null;
    }

    public TileEntityInventory getParent() {
        return this;
    }


    public void addInventorySlot(InvSlot inventorySlot) {
        assert this.invSlots.stream().noneMatch((slot) -> slot.equals(inventorySlot));

        this.invSlots.add(inventorySlot);
    }

    public InfoInvSlots locateInfoInvSlot(int extIndex) {
        try {
            return this.infoInvSlotsList.get(extIndex);
        } catch (Exception e) {
            return null;
        }
    }

    private void putStackAt(InfoInvSlots loc, ItemStack stack) {
        loc.getSlot().put(loc.getIndex(), stack);
        super.markDirty();
    }

    protected InvSlot getInventorySlot(int extIndex) {
        final InfoInvSlots loc = this.locateInfoInvSlot(extIndex);
        return loc == null ? null : loc.getSlot();
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);

    }


    public List<ItemStack> getSelfDrops(int fortune, boolean wrench) {
        final List<ItemStack> list = super.getSelfDrops(fortune, wrench);
        return list;
    }

    public List<ItemStack> getAuxDrops(int fortune) {
        List<ItemStack> ret = new ArrayList<>(super.getAuxDrops(fortune));
        for (final InvSlot slot : this.invSlots) {
            for (final ItemStack stack : slot.gets()) {
                if (!ModUtils.isEmpty(stack)) {
                    ret.add(stack);
                }
            }
        }
        for (AbstractComponent component : this.getComponentList()) {
            if (!component.getDrops().isEmpty()) {
                ret.addAll(component.getDrops());
            }
        }
        return ret;
    }

    public <T> T getCapability(@NotNull Capability<T> capability, EnumFacing facing) {
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


    public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing) {

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


    public final <T extends AbstractComponent> T addComponent(T component) {
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

    public final <T extends AbstractComponent> void removeComponent(T component) {
        if (component == null) {
            throw new NullPointerException("null component");
        } else {

            componentList.remove(component);
            advComponentMap.remove(component.toString(), component);
            if (component.isClient()) {
                this.updateClientList.remove(component);
            }
            if (component.isServer()) {
                this.updateServerList.remove(component);
            }

            for (final Capability<?> capability : component.getProvidedCapabilities(null)) {
                this.removeComponentCapability(capability, component);
            }

        }
    }

    public void addComponentCapability(Capability<?> cap, AbstractComponent component) {
        if (this.capabilityComponents == null) {
            this.capabilityComponents = new IdentityHashMap<>();
        }

        AbstractComponent prev = this.capabilityComponents.put(cap, component);

        assert prev == null;

    }

    public void removeComponentCapability(Capability<?> cap, AbstractComponent component) {
        if (this.capabilityComponents == null) {
            this.capabilityComponents = new IdentityHashMap<>();
        }

        this.capabilityComponents.remove(cap, component);


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


}
