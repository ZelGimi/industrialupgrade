package com.denfop.tiles.base;

import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.inv.ITileInventory;
import com.denfop.api.inv.VirtualSlot;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.IUpgradeItem;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.componets.AbstractComponent;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentPrivate;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.componets.client.ComponentClientEffectRender;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.denfop.register.Register.containerBase;

public class TileEntityInventory extends TileEntityBlock implements IAdvInventory<TileEntityInventory>, WorldlyContainer, ITileInventory {
    protected final List<InvSlot> invSlots = new ArrayList<>();
    protected final List<InfoInvSlots> infoInvSlotsList = new ArrayList<>();

    protected final List<InvSlot> inputSlots = new LinkedList<>();
    protected final List<InvSlot> outputSlots = new LinkedList<>();
    protected final IItemHandler[] itemHandler;
    private final ComponentPrivate componentPrivate;
    protected boolean isLoaded = false;
    protected int size_inventory;
    protected ComponentClientEffectRender componentClientEffectRender;
    protected SoilPollutionComponent pollutionSoil;
    protected AirPollutionComponent pollutionAir;
    private int[] slotsFace;
    private int windowId;

    public TileEntityInventory(IMultiTileBlock tileBlock, BlockPos pos, BlockState state) {
        super(tileBlock, pos, state);
        this.itemHandler = new IItemHandler[Direction.values().length + 1];
        componentPrivate = this.addComponent(new ComponentPrivate(this));
    }

    public static int getIndex(int loc) {
        return loc >>> 16;
    }


    public void onNetworkEvent(final int var1) {

    }

    @Override
    public void readContainerPacket(CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            CompoundTag invSlotsTag = (CompoundTag) DecoderHandler.decode(customPacketBuffer);
            for (final InvSlot invSlot : this.invSlots) {
                invSlot.readFromNbt(invSlotsTag.getCompound(String.valueOf(findSlot(invSlot))));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer =  super.writeContainerPacket();
        CompoundTag invSlotsTag = new CompoundTag();

        for (final InvSlot invSlot : this.invSlots) {
            CompoundTag invSlotTag = new CompoundTag();
            invSlot.writeToNbt(invSlotTag);
            invSlotsTag.put(String.valueOf(findSlot(invSlot)), invSlotTag);
        }
        try {
            EncoderHandler.encode(customPacketBuffer,invSlotsTag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (level.isClientSide) {
            return true;
        }
        for (AbstractComponent component : componentList) {
            if (component.onBlockActivated(player, hand))
                return true;
        }

        ItemStack stack = player.getItemInHand(hand);
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
                                    if (player instanceof ServerPlayer serverPlayer) {
                                        serverPlayer.containerMenu.broadcastChanges();
                                    }
                                    this.setChanged();
                                    return true;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        openContainer(player);
        return true;
    }

    private void openContainer(Player player) {
        if (getGuiContainer(player) != null) {
            CustomPacketBuffer growingBuffer = new CustomPacketBuffer();

            try {
                EncoderHandler.encode(growingBuffer, this);
            } catch (IOException var5) {
                throw new RuntimeException(var5);
            }

            growingBuffer.flip();

            player.closeContainer();
            NetworkHooks.openScreen((ServerPlayer) player, this, buf -> buf.writeBytes(growingBuffer));
        }
    }

    public InfoInvSlots locateInfoInvSlot(int extIndex) {
        try {
            return this.infoInvSlotsList.get(extIndex);
        } catch (Exception e) {
            return null;
        }
    }

    private void putStackAt(InfoInvSlots loc, ItemStack stack) {
        loc.getSlot().set(loc.getIndex(), stack);
        super.setChanged();
    }

    protected InvSlot getInventorySlot(int extIndex) {
        final InfoInvSlots loc = this.locateInfoInvSlot(extIndex);
        return loc == null ? null : loc.getSlot();
    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction facing) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (facing == null) {
                if (this.itemHandler[this.itemHandler.length - 1] == null) {
                    this.itemHandler[this.itemHandler.length - 1] = new InvWrapper(this);
                }

                return LazyOptional.of(() -> this.itemHandler[this.itemHandler.length - 1]).cast();
            } else {
                if (this.itemHandler[facing.ordinal()] == null) {
                    this.itemHandler[facing.ordinal()] = new SidedInvWrapper(this, facing);
                }

                return LazyOptional.of(() -> this.itemHandler[facing.ordinal()]).cast();
            }
        } else {
            if (this.capabilityComponents == null) {
                return super.getCapability(cap, facing);
            } else {

                AbstractComponent comp = this.capabilityComponents.get(cap);
                return comp == null ? super.getCapability(cap, facing) : LazyOptional.of(() -> comp.getCapability(cap, facing)).cast();
            }
        }
    }

    @Override
    public TileEntityInventory getParent() {
        return this;
    }


    @Override
    public void addInventorySlot(InvSlot inventorySlot) {
        assert this.invSlots.stream().noneMatch((slot) -> slot.equals(inventorySlot));
        this.invSlots.add(inventorySlot);
    }


    @Override
    public int getBaseIndex(InvSlot var1) {
        int ret = 0;

        InvSlot slot;
        for (Iterator<InvSlot> var3 = this.invSlots.iterator(); var3.hasNext(); ret += slot.size()) {
            slot = var3.next();
            if (slot == var1) {
                return ret;
            }
        }

        return -1;
    }


    @Override
    public MenuType<?> getMenuType() {
        return containerBase.get();
    }

    @Override
    public int getContainerId() {
        return windowId;
    }


    @Override
    public ContainerBase<?> getGuiContainer(Player var1) {
        return null;
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return null;
    }


    @Override
    public IMultiTileBlock getTeBlock() {
        return null;
    }


    @Override
    public BlockTileEntity getBlock() {
        return null;
    }


    @Override
    public int[] getSlotsForFace(Direction p_19238_) {
        if (this.slotsFace == null) {
            this.slotsFace = new int[this.getContainerSize()];

            for (int i = 0; i < this.getContainerSize(); i++) {
                this.slotsFace[i] = i;
            }

        }
        return this.slotsFace;
    }


    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction p_19237_) {
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

    public void removeInventorySlot(InvSlot inventorySlot) {
        this.invSlots.remove(inventorySlot);
    }

    public List<ItemStack> getSelfDrops(int fortune, boolean wrench) {
        return super.getSelfDrops(fortune, wrench);
    }

    public List<ItemStack> getAuxDrops(int fortune) {
        List<ItemStack> ret = new ArrayList<>(super.getAuxDrops(fortune));
        for (final InvSlot slot : this.invSlots) {
            if (!(slot instanceof VirtualSlot)) {
                for (final ItemStack stack : slot) {
                    if (!ModUtils.isEmpty(stack)) {
                        ret.add(stack);
                    }
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

    @Override
    public void onUnloaded() {
        super.onUnloaded();
    }



    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            this.componentPrivate.onNetworkUpdate((CustomPacketBuffer) DecoderHandler.decode(customPacketBuffer));
            for (AbstractComponent component : this.componentList)
                component.onNetworkUpdate(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer packetBuffer = super.writePacket();
        try {
            EncoderHandler.encode(packetBuffer, this.componentPrivate);
            for (AbstractComponent component : this.componentList)
                packetBuffer.writeBytes(component.updateComponent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packetBuffer;
    }

    public void readFromNBT(CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        CompoundTag invSlotsTag = nbtTagCompound.getCompound("InvSlots");
       for (int i = 0; i < invSlots.size(); i++){
            InvSlot invSlot = invSlots.get(i);
            invSlot.readFromNbt(invSlotsTag.getCompound(String.valueOf(i)));
        }
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        CompoundTag invSlotsTag = new CompoundTag();
        for (int i = 0; i < invSlots.size(); i++){
            InvSlot invSlot = invSlots.get(i);
            CompoundTag invSlotTag = new CompoundTag();
            invSlot.writeToNbt(invSlotTag);
            invSlotsTag.put(String.valueOf(i), invSlotTag);
        }

        nbt.put("InvSlots", invSlotsTag);

        return nbt;
    }
    public int findSlot(InvSlot invSlot){
        for (int i = 0; i < invSlots.size(); i++){
            InvSlot invSlot1 = invSlots.get(i);
            if (invSlot.equals(invSlot1)){
                return i;
            }
        }
        return -1;
    }
    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack p_19240_, Direction p_19241_) {
        InvSlot targetSlot = this.getInventorySlot(index);
        return targetSlot != null && targetSlot.canOutput();
    }


    @Override
    public int getContainerSize() {
        if (size_inventory == 0 && !this.invSlots.isEmpty()) {
            InvSlot invSlot;
            for (Iterator<InvSlot> var2 = this.invSlots.iterator(); var2.hasNext(); size_inventory += invSlot.size()) {
                invSlot = var2.next();
            }
        }

        return size_inventory;
    }


    @Override
    public boolean isEmpty() {
        return this.invSlots.isEmpty();
    }

    public List<InvSlot> getInputSlots() {
        return inputSlots;
    }

    public List<InvSlot> getOutputSlots() {
        return outputSlots;
    }


    @Override
    public ItemStack getItem(int index) {
        final InfoInvSlots loc = this.locateInfoInvSlot(index);
        return loc == null ? ModUtils.emptyStack : loc.getSlot().get(loc.getIndex());
    }

    @Override
    public int getMaxStackSize() {
        return getInventoryStackLimit();
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public ItemStack removeItem(int index, int amount) {
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

    public ComponentPrivate getComponentPrivate() {
        return componentPrivate;
    }

    public List<InvSlot> getInvSlots() {
        return invSlots;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        infoInvSlotsList.clear();
        inputSlots.clear();
        outputSlots.clear();
        for (InvSlot slot : this.invSlots) {
            for (int k = 0; k < slot.size(); k++) {
                infoInvSlotsList.add(new InfoInvSlots(slot, k));
            }
            if (slot.getTypeItemSlot() != null) {
                if (slot.getTypeItemSlot().isInput()) {
                    this.inputSlots.add(slot);
                }
                if (slot.getTypeItemSlot().isOutput()) {
                    this.outputSlots.add(slot);
                }
            }
        }
        this.size_inventory = 0;

        InvSlot invSlot;
        for (Iterator<InvSlot> var2 = this.invSlots.iterator(); var2.hasNext(); size_inventory += invSlot.size()) {
            invSlot = var2.next();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (componentClientEffectRender != null) {
            componentClientEffectRender.render();
        }

    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
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

    @Override
    public void setChanged() {
        super.setChanged();
        for (final InvSlot invSlot : this.invSlots) {
            invSlot.onChanged();
        }
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        } else {
            InvSlot invSlot = this.getInventorySlot(index);
            return invSlot != null && invSlot.canInput() && invSlot.accepts(stack, this.locateInfoInvSlot(index).getIndex());
        }
    }

    @Override
    public void setItem(int p_18944_, ItemStack stack) {
        final InfoInvSlots loc = this.locateInfoInvSlot(p_18944_);
        if (loc == null) {
            assert false;

        } else {
            if (ModUtils.isEmpty(stack)) {
                stack = ModUtils.emptyStack;
            }

            this.putStackAt(loc, stack);
        }
    }


    @Override
    public boolean stillValid(Player p_18946_) {
        return !this.isRemoved();
    }

    @Override
    public void clearContent() {
    }

    @Nonnull
    public String getName() {
        return this.getBlock().item.getDescriptionId();
    }

    @Override
    public Component getDisplayName() {
        return Component.literal(Localization.translate(this.getName()));
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        this.windowId = p_39954_;
        return getGuiContainer(p_39956_);
    }
}
