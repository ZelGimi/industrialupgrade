package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuTunerRecipe;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.inventory.Inventory;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenRecipeTuner;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityRecipeTuner extends BlockEntityInventory implements IUpdatableTileEvent {

    public final Inventory slot;
    public final Inventory input_slot;

    public BlockEntityRecipeTuner(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.recipe_tuner, pos, state);
        this.input_slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.recipe_schedule.getItem();
            }

            @Override
            public ItemStack set(int i, ItemStack empty) {
                return super.set(i, empty);
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 9);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.receptor_mechanism.info"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenRecipeTuner((ContainerMenuTunerRecipe) menu);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.recipe_tuner;
    }

    @Override
    public ContainerMenuTunerRecipe getGuiContainer(final Player var1) {
        return new ContainerMenuTunerRecipe(this, var1);
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == 0) {
            if (!this.input_slot.isEmpty()) {
                final boolean mode = this.input_slot.get(0).getOrDefault(DataComponentsInit.BLACK_LIST, false);
                this.input_slot.get(0).set(DataComponentsInit.BLACK_LIST, !mode);
            }
        } else if (var2 == 1) {
            if (!this.input_slot.isEmpty()) {
                List<ItemStack> itemStackList = new ArrayList<>();
                for (int i = 0; i < 9; i++) {
                    if (!this.slot.get(i).isEmpty()) {
                        itemStackList.add(this.slot.get(i));
                    }
                }
                this.input_slot.get(0).set(DataComponentsInit.LIST_STACK, itemStackList);
            }
        }
    }

}
