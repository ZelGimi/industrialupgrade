package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuItemManipulator;
import com.denfop.inventory.Inventory;
import com.denfop.items.bags.ItemEnergyBags;
import com.denfop.items.bags.ItemStackBags;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenItemManipulator;
import com.denfop.sound.EnumSound;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntityItemManipulator extends BlockEntityElectricMachine
        implements IUpdatableTileEvent {


    public final Inventory inputslot;
    public final Inventory inputslot1;
    public final Inventory inputslot2;
    public final Inventory outputSlot1;
    int type = 0;

    public BlockEntityItemManipulator(BlockPos pos, BlockState state) {
        super(0, 0, 0, BlockBaseMachine3Entity.itemmanipulator, pos, state);


        this.inputslot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
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
        this.inputslot2 = new Inventory(this, Inventory.TypeItemSlot.INPUT, 27) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return !(stack.getItem() instanceof ItemEnergyBags);
            }
        };
        this.outputSlot1 = new Inventory(this, Inventory.TypeItemSlot.OUTPUT, 27) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return false;
            }
        };
        this.inputslot1 = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
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

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.itemmanipulator;
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
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenItemManipulator((ContainerMenuItemManipulator) menu);
    }

    public ContainerMenuItemManipulator getGuiContainer(Player entityPlayer) {
        return new ContainerMenuItemManipulator(entityPlayer, this);
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
