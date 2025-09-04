package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.Inventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStack;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static com.denfop.register.Register.inventory_container;

public abstract class ItemStackInventory implements CustomWorldContainer, IUpdatableItemStack {

    public final Player player;
    protected final ItemStack[] inventory;
    protected ItemStack containerStack;
    protected boolean cleared;
    private int containerId;

    public ItemStackInventory(Player player, ItemStack containerStack, int inventorySize) {
        this.containerStack = containerStack;
        this.inventory = new ItemStack[inventorySize];
        Arrays.fill(this.inventory, ItemStack.EMPTY);
        this.player = player;
        if (!player.level().isClientSide()) {
            CompoundTag nbt = ModUtils.nbt(containerStack);
            if (!nbt.contains("uid", 3)) {
                nbt.putInt("uid", IUCore.random.nextInt());
            }

            ListTag contentList = nbt.getList("Items", 10);

            for (int i = 0; i < contentList.size(); ++i) {
                CompoundTag slotNbt = contentList.getCompound(i);
                int slot = slotNbt.getByte("Slot");
                if (slot >= 0 && slot < this.inventory.length) {
                    this.inventory[slot] = ItemStack.of(slotNbt);
                }
            }
        }

    }

    @Override
    public Level getWorld() {
        return player.level();
    }

    @Override
    public CustomPacketBuffer writeContainer() {
        return new CustomPacketBuffer();
    }

    @Override
    public void readContainer(final CustomPacketBuffer buffer) {

    }

    public ItemStack[] getInventory() {
        return inventory;
    }


    @Override
    public int getContainerSize() {
        return this.inventory.length;
    }

    public boolean isEmpty() {

        for (ItemStack stack : this.inventory) {
            if (!ModUtils.isEmpty(stack)) {
                return false;
            }
        }

        return true;
    }


    @Override
    public ItemStack getItem(int slot) {
        return this.inventory[slot];
    }


    @Override
    public ItemStack removeItem(int index, int amount) {
        ItemStack stack;
        if (index >= 0 && index < this.inventory.length && !ModUtils.isEmpty(stack = this.inventory[index])) {
            ItemStack ret;
            if (amount >= ModUtils.getSize(stack)) {
                ret = stack;
                this.inventory[index] = ModUtils.emptyStack;
            } else {
                ret = ModUtils.setSize(stack, amount);
                this.inventory[index] = ModUtils.decSize(stack, amount);
            }

            this.save();
            return ret;
        } else {
            return ModUtils.emptyStack;
        }
    }


    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack ret = this.getItem(index);
        if (!ModUtils.isEmpty(ret)) {
            this.setItem(index, ItemStack.EMPTY);
        }

        return ret;
    }


    @Override
    public void setItem(int slot, ItemStack stack) {
        if (!ModUtils.isEmpty(stack) && ModUtils.getSize(stack) > this.getInventoryStackLimit()) {
            stack = ModUtils.setSize(stack, this.getInventoryStackLimit());
        }

        if (ModUtils.isEmpty(stack)) {
            this.inventory[slot] = ModUtils.emptyStack;
        } else {
            this.inventory[slot] = stack;
        }

        this.save();
    }

    @Override
    public boolean canPlaceItem(int p_18952_, ItemStack p_18953_) {
        return false;
    }

    @Override
    public void setChanged() {
        save();
    }


    @Override
    public boolean stillValid(Player player) {
        return player == this.player && this.getPlayerInventoryIndex() >= -1;
    }

    public ItemStack getContainerStack() {
        return containerStack;
    }


    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isItemValidForSlot(int slot, ItemStack stack1) {
        return false;
    }


    protected abstract String getName();

    public Component getDisplayName() {
        return Component.literal(this.getName());
    }

    public boolean isThisContainer(ItemStack stack) {
        if (!ModUtils.isEmpty(stack) && stack.getItem() == this.containerStack.getItem()) {
            CompoundTag nbt = stack.getTag();
            return nbt != null && nbt.getInt("uid") == this.getUid();
        } else {
            return false;
        }
    }

    protected int getUid() {
        CompoundTag nbt = ModUtils.nbt(this.containerStack);
        return nbt.getInt("uid");
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = ModUtils.nbt(this.containerStack);
        return nbt;
    }

    protected int getPlayerInventoryIndex() {
        for (int i = -1; i < this.player.getInventory().getContainerSize(); ++i) {
            ItemStack stack = i == -1 ? this.player.getInventory().getSelected() : this.player.getInventory().getItem(i);
            if (this.isThisContainer(stack)) {
                return i;
            }
        }

        return -2147483648;
    }

    protected void save() {
        if (!player.level().isClientSide()) {
            if (this.containerStack.isEmpty())
                this.containerStack = this.player.getInventory().getSelected();
            if (!this.cleared) {
                boolean dropItself = false;

                for (int i = 0; i < this.inventory.length; ++i) {
                    if (this.isThisContainer(this.inventory[i])) {
                        this.inventory[i] = null;
                        dropItself = true;
                    }
                }

                ListTag contentList = new ListTag();

                int idx;
                for (idx = 0; idx < this.inventory.length; ++idx) {
                    if (!ModUtils.isEmpty(this.inventory[idx])) {
                        CompoundTag nbt = new CompoundTag();
                        this.inventory[idx].save(nbt);
                        nbt.putByte("Slot", (byte) idx);
                        contentList.add(nbt);
                    }
                }

                ModUtils.nbt(this.containerStack).put("Items", contentList);

                this.containerStack = ModUtils.setSize(this.containerStack, 1);

                if (dropItself) {
                    ModUtils.dropAsEntity(this.player.level(), this.player.blockPosition(), this.containerStack);
                    this.clear();
                } else {
                    idx = this.getPlayerInventoryIndex();
                    if (idx < -1) {
                        this.clear();
                    } else if (idx == -1) {
                        this.player.getInventory().setPickedItem(this.containerStack);
                    } else {
                        this.player.getInventory().setItem(idx, this.containerStack);
                    }
                }

            }
        }
        player.containerMenu.broadcastChanges();
    }

    public void saveAsThrown(ItemStack stack) {
        assert !player.level().isClientSide();

        ListTag contentList = new ListTag();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (!ModUtils.isEmpty(this.inventory[i]) && !this.isThisContainer(this.inventory[i])) {
                CompoundTag nbt = new CompoundTag();
                nbt.putByte("Slot", (byte) i);
                inventory[i].save(nbt);
                contentList.add(nbt);
            }
        }

        ModUtils.nbt(stack).put("Items", contentList);

        assert ModUtils.nbt(stack).getInt("uid") == 0;

        this.clear();
    }

    public void clear() {
        Arrays.fill(this.inventory, ItemStack.EMPTY);

        this.cleared = true;
    }




    @Override
    public void addInventorySlot(Inventory var1) {

    }


    @Override
    public MenuType<?> getMenuType() {
        return inventory_container.get();
    }

    @Override
    public int getContainerId() {
        return this.containerId;
    }


    @Override
    public ContainerMenuBase<?> getGuiContainer(Player var1) {
        return null;
    }


    @Override
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> var2) {
        return null;
    }


    @Override
    public void clearContent() {

    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_39954_, net.minecraft.world.entity.player.Inventory p_39955_, Player p_39956_) {
        containerId = p_39954_;
        return getGuiContainer(p_39956_);
    }
}
