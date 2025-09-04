package com.denfop.blockentity.base;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.menu.VirtualSlot;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.IUpgradeItem;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.componets.AbstractComponent;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentPrivate;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.componets.client.ComponentClientEffectRender;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.LivingEntity;
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
import java.util.*;

import static com.denfop.register.Register.containerBase;

public class BlockEntityInventory extends BlockEntityBase implements CustomWorldContainer, WorldlyContainer {
    protected final List<Inventory> inventories = new ArrayList<>();
    protected final Map<Integer, Tuple<Integer, Integer>> indexInventoryList = new HashMap<>();

    protected final List<Inventory> inputSlots = new LinkedList<>();
    protected final List<Inventory> outputSlots = new LinkedList<>();
    protected final IItemHandler[] itemHandler;
    private final ComponentPrivate componentPrivate;
    protected int size_inventory;
    protected ComponentClientEffectRender componentClientEffectRender;
    protected SoilPollutionComponent pollutionSoil;
    protected AirPollutionComponent pollutionAir;
    private int[] slotsFace;
    private int windowId;

    public BlockEntityInventory(MultiBlockEntity tileBlock, BlockPos pos, BlockState state) {
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
            for (int i = 0; i < inventories.size(); i++) {
                Inventory invSlot = this.inventories.get(i);
                invSlot.readFromNbt(invSlotsTag.getCompound(String.valueOf(i)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        CompoundTag invSlotsTag = new CompoundTag();

        for (int i = 0; i < inventories.size(); i++) {
            CompoundTag invSlotTag = new CompoundTag();
            Inventory invSlot = this.inventories.get(i);
            invSlot.writeToNbt(invSlotTag);
            invSlotsTag.put(String.valueOf(i), invSlotTag);
        }
        try {
            EncoderHandler.encode(customPacketBuffer, invSlotsTag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public boolean onSneakingActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        for (AbstractComponent component : componentList) {
            if (component.onSneakingActivated(player, hand))
                return true;
        }
        return super.onSneakingActivated(player, hand, side, vec3);
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
                        for (final Inventory invslot : this.inventories) {
                            if (invslot instanceof InventoryUpgrade) {
                                InventoryUpgrade upgrade = (InventoryUpgrade) invslot;
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
        if (player.level().isClientSide)
            return;
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

    public Tuple<Integer, Integer> locateInfoInvSlot(int extIndex) {
        try {
            return this.indexInventoryList.get(extIndex);
        } catch (Exception e) {
            return null;
        }
    }

    private void putStackAt(Tuple<Integer, Integer> tuple, ItemStack stack) {
        inventories.get(tuple.getA()).set(tuple.getB(), stack);
        super.setChanged();
    }

    protected Inventory getInventorySlot(int extIndex) {
        Tuple<Integer, Integer> tuple = indexInventoryList.get(extIndex);
        return tuple == null ? null : this.inventories.get(tuple.getA());
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
    public void addInventorySlot(Inventory inventorySlot) {
        assert this.inventories.stream().noneMatch((slot) -> slot.equals(inventorySlot));
        this.inventories.add(inventorySlot);
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
    public ContainerMenuBase<?> getGuiContainer(Player var1) {
        return null;
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return null;
    }


    @Override
    public MultiBlockEntity getTeBlock() {
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
            Inventory targetSlot = this.getInventorySlot(index);
            if (targetSlot == null) {
                return false;
            } else {
                return targetSlot.canInput() && targetSlot.canPlaceItem(this.locateInfoInvSlot(index).getB(), stack);
            }
        }
    }

    public void removeInventorySlot(Inventory inventorySlot) {
        this.inventories.remove(inventorySlot);
    }

    public List<ItemStack> getSelfDrops(int fortune, boolean wrench) {
        return super.getSelfDrops(fortune, wrench);
    }

    public List<ItemStack> getAuxDrops(int fortune) {
        List<ItemStack> ret = new ArrayList<>(super.getAuxDrops(fortune));
        for (final Inventory slot : this.inventories) {
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
        for (int i = 0; i < inventories.size(); i++) {
            Inventory invSlot = inventories.get(i);
            invSlot.readFromNbt(invSlotsTag.getCompound(String.valueOf(i)));
        }
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        CompoundTag invSlotsTag = new CompoundTag();
        for (int i = 0; i < inventories.size(); i++) {
            Inventory invSlot = inventories.get(i);
            CompoundTag invSlotTag = new CompoundTag();
            invSlot.writeToNbt(invSlotTag);
            invSlotsTag.put(String.valueOf(i), invSlotTag);
        }

        nbt.put("InvSlots", invSlotsTag);

        return nbt;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack p_19240_, Direction p_19241_) {
        Inventory targetSlot = this.getInventorySlot(index);
        return targetSlot != null && targetSlot.canOutput();
    }


    @Override
    public int getContainerSize() {
        if (size_inventory == 0 && !this.inventories.isEmpty()) {
            Inventory invSlot;
            for (Iterator<Inventory> var2 = this.inventories.iterator(); var2.hasNext(); size_inventory += invSlot.size()) {
                invSlot = var2.next();
            }
        }

        return size_inventory;
    }


    @Override
    public boolean isEmpty() {
        return this.inventories.isEmpty();
    }

    public List<Inventory> getInputSlots() {
        return inputSlots;
    }

    public List<Inventory> getOutputSlots() {
        return outputSlots;
    }


    @Override
    public ItemStack getItem(int index) {
        final Tuple<Integer, Integer> tuple = this.locateInfoInvSlot(index);
        return tuple == null ? ModUtils.emptyStack : inventories.get(tuple.getA()).getItem(tuple.getB());
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
        final Tuple<Integer, Integer> tuple = this.locateInfoInvSlot(index);
        if (tuple == null) {
            return ModUtils.emptyStack;
        } else {
            ItemStack stack = inventories.get(tuple.getA()).getItem(tuple.getB());
            if (ModUtils.isEmpty(stack)) {
                return ModUtils.emptyStack;
            } else if (amount >= ModUtils.getSize(stack)) {
                this.putStackAt(tuple, ModUtils.emptyStack);
                return stack;
            } else {
                if (amount != 0) {
                    if (amount < 0) {
                        int space = Math.min(
                                inventories.get(tuple.getA()).getStackSizeLimit(),
                                stack.getMaxStackSize()
                        ) - ModUtils.getSize(stack);
                        amount = Math.max(amount, -space);
                    }

                    this.putStackAt(tuple, ModUtils.decSize(stack, amount));
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

    public List<Inventory> getInventories() {
        return inventories;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        inputSlots.clear();
        outputSlots.clear();
        int amount = 0;
        int index = 0;
        for (Inventory slot : this.inventories) {
            for (int k = 0; k < slot.size(); k++) {
                indexInventoryList.put(amount,new Tuple<>(index,k));
                amount++;
            }
            index++;
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

        Inventory invSlot;
        for (Iterator<Inventory> var2 = this.inventories.iterator(); var2.hasNext(); size_inventory += invSlot.size()) {
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
        Tuple<Integer, Integer> tuple = this.locateInfoInvSlot(index);
        if (tuple == null) {
            return ModUtils.emptyStack;
        } else {
            ItemStack ret = inventories.get(tuple.getA()).get(tuple.getB());
            if (!ModUtils.isEmpty(ret)) {
                this.putStackAt(tuple, ModUtils.emptyStack);
            }

            return ret;
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        for (final Inventory invSlot : this.inventories) {
            invSlot.setChanged();
        }
    }


    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        } else {
            Inventory inventory = this.getInventorySlot(index);
            return inventory != null && inventory.canInput() && inventory.canPlaceItem(this.locateInfoInvSlot(index).getB(), stack);
        }
    }

    public void setItem(int p_18944_, ItemStack stack) {
        Tuple<Integer, Integer> tuple = this.locateInfoInvSlot(p_18944_);
        if (tuple == null) {
            assert false;

        } else {
            if (ModUtils.isEmpty(stack)) {
                stack = ModUtils.emptyStack;
            }

            this.putStackAt(tuple, stack);
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
    public AbstractContainerMenu createMenu(int p_39954_, net.minecraft.world.entity.player.Inventory p_39955_, Player p_39956_) {
        this.windowId = p_39954_;
        return getGuiContainer(p_39956_);
    }
}
