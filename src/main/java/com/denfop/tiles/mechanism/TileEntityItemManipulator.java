package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerItemManipulator;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiItemManipulator;
import com.denfop.invslot.InvSlot;
import com.denfop.items.bags.ItemEnergyBags;
import com.denfop.items.bags.ItemStackBags;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityItemManipulator extends TileElectricMachine
        implements IUpdatableTileEvent {


    public final InvSlot inputslot;
    public final InvSlot inputslot1;
    public final InvSlot inputslot2;
    public final InvSlot outputSlot1;
    int type = 0;

    public TileEntityItemManipulator(BlockPos pos, BlockState state) {
        super(0, 0, 0,BlockBaseMachine3.itemmanipulator,pos,state);


        this.inputslot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemEnergyBags;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (!content.isEmpty()) {
                    type = 1;
                } else {
                    type = 0;
                }
                return content;
            }
        };
        this.inputslot2 = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 27) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return !(stack.getItem() instanceof ItemEnergyBags);
            }
        };
        this.outputSlot1 = new InvSlot(this, InvSlot.TypeItemSlot.OUTPUT, 27) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return false;
            }
        };
        this.inputslot1 = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemEnergyBags;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (!content.isEmpty()) {
                    type = 2;
                } else {
                    type = 0;
                }
                return content;
            }
        };
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.itemmanipulator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
    }

    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiItemManipulator((ContainerItemManipulator) menu);
    }

    public ContainerItemManipulator getGuiContainer(Player entityPlayer) {
        return new ContainerItemManipulator(entityPlayer, this);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (type == 1 && !this.inputslot.get(0).isEmpty()) {
            type = 0;
            ItemEnergyBags itemEnergyBags = (ItemEnergyBags) this.inputslot.get(0).getItem();
            ItemStackBags box =
                    (ItemStackBags) itemEnergyBags.getInventory(
                            this.getWorld().getPlayerByUUID(this.getComponentPrivate().getPlayersUUID().get(0)),
                            this.inputslot.get(0)
                    );
            for (int i = 0; i < box.inventorySize; i++) {
                ItemStack stack = box.getItem(i);
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
            box.saveAsThrown(this.inputslot.get(0));
        }
        if (type == 2 && !this.inputslot1.get(0).isEmpty()) {
            type = 0;
            ItemEnergyBags itemEnergyBags = (ItemEnergyBags) this.inputslot1.get(0).getItem();
            Player player = this.getWorld().getPlayerByUUID(this.getComponentPrivate().getPlayersUUID().get(0));
            ItemStackBags box =
                    (ItemStackBags) itemEnergyBags.getInventory(
                            player,
                            this.inputslot1.get(0)
                    );

            for (int i = 0; i < 27; i++) {
                ItemStack stack = inputslot2.get(i);
                if (stack.isEmpty()) {
                    continue;
                }
                if (box.canAdd(stack)) {
                    final ItemStack stack1 = stack.copy();
                    stack1.setCount(Math.min(stack1.getCount(), stack1.getMaxStackSize()));
                    box.addWithoutSave(stack1);
                    inputslot2.set(i, stack1);
                }

            }
            box.saveAsThrown(this.inputslot1.get(0));
        }

    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.pen.getSoundEvent();
    }


    @Override
    public void updateTileServer(Player player, double event) {


    }

}
