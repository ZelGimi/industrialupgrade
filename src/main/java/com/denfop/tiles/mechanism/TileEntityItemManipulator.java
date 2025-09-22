package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerItemManipulator;
import com.denfop.gui.GuiItemManipulator;
import com.denfop.invslot.Inventory;
import com.denfop.items.bags.ItemEnergyBags;
import com.denfop.items.bags.ItemStackBags;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityItemManipulator extends TileElectricMachine
        implements IUpdatableTileEvent {


    public final Inventory inputslot;
    public final Inventory inputslot1;
    public final Inventory inputslot2;
    public final Inventory outputSlot1;
    int type = 0;

    public TileEntityItemManipulator() {
        super(0, 0, 0);


        this.inputslot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ItemEnergyBags;
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (!content.isEmpty()) {
                    type = 1;
                } else {
                    type = 0;
                }
            }
        };
        this.inputslot2 = new Inventory(this, Inventory.TypeItemSlot.INPUT, 27) {
            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                return !(stack.getItem() instanceof ItemEnergyBags);
            }
        };
        this.outputSlot1 = new Inventory(this, Inventory.TypeItemSlot.OUTPUT, 27) {
            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                return false;
            }
        };
        this.inputslot1 = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ItemEnergyBags;
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (!content.isEmpty()) {
                    type = 2;
                } else {
                    type = 0;
                }
            }
        };
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.itemmanipulator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
    }

    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiItemManipulator(new ContainerItemManipulator(entityPlayer, this));
    }

    public ContainerItemManipulator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerItemManipulator(entityPlayer, this);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (type == 1 && !this.inputslot.get().isEmpty()) {
            type = 0;
            ItemEnergyBags itemEnergyBags = (ItemEnergyBags) this.inputslot.get().getItem();
            ItemStackBags box =
                    (ItemStackBags) itemEnergyBags.getInventory(
                            this.getWorld().getPlayerEntityByName(this.getComponentPrivate().getPlayers().get(0)),
                            this.inputslot.get()
                    );
            for (int i = 0; i < box.getSizeInventory(); i++) {
                ItemStack stack = box.getStackInSlot(i);
                if (stack.isEmpty()) {
                    continue;
                }
                final int countFill = outputSlot1.addExperimental(stack);
                if (countFill == 0) {
                    box.putWithoutSave(i, ItemStack.EMPTY);
                } else {
                    if (stack.getCount() != countFill) {
                        stack.setCount(countFill);
                        box.putWithoutSave(i, stack);
                    }
                }
            }
            box.saveAsThrown(this.inputslot.get());
        }
        if (type == 2 && !this.inputslot1.get().isEmpty()) {
            type = 0;
            ItemEnergyBags itemEnergyBags = (ItemEnergyBags) this.inputslot1.get().getItem();
            EntityPlayer player = this.getWorld().getPlayerEntityByName(this.getComponentPrivate().getPlayers().get(0));
            ItemStackBags box =
                    (ItemStackBags) itemEnergyBags.getInventory(
                            player,
                            this.inputslot1.get()
                    );

            for (int i = 0; i < 27; i++) {
                ItemStack stack = inputslot2.get(i);
                if (stack.isEmpty()) {
                    continue;
                }
                if (box.canAdd(stack)) {
                    final ItemStack stack1 = stack.copy();
                    stack1.setCount(Math.min(stack1.getCount(), stack1.stackSize));
                    box.addWithoutSave(stack1);
                    inputslot2.put(i, stack1);
                }

            }
            box.saveAsThrown(this.inputslot1.get());
        }

    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.pen.getSoundEvent();
    }


    @Override
    public void updateTileServer(EntityPlayer player, double event) {


    }

}
