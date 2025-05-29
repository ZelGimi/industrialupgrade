package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerTunerRecipe;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiRecipeTuner;
import com.denfop.invslot.InvSlot;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityRecipeTuner extends TileEntityInventory implements IUpdatableTileEvent {

    public final InvSlot slot;
    public final InvSlot input_slot;

    public TileEntityRecipeTuner(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.recipe_tuner,pos,state);
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.recipe_schedule.getItem();
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 9);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiRecipeTuner((ContainerTunerRecipe) menu);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.recipe_tuner;
    }

    @Override
    public ContainerTunerRecipe getGuiContainer(final Player var1) {
        return new ContainerTunerRecipe(this, var1);
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == 0) {
            if (!this.input_slot.isEmpty()) {
                final CompoundTag nbt = ModUtils.nbt(this.input_slot.get(0));
                final boolean mode = nbt.getBoolean("mode");
                nbt.putBoolean("mode", !mode);
            }
        } else if (var2 == 1) {
            if (!this.input_slot.isEmpty()) {
                final CompoundTag nbt = ModUtils.nbt(this.input_slot.get(0));
                for (int i = 0; i < 9; i++) {
                    if (this.slot.get(i).isEmpty()) {
                        nbt.put("recipe_" + i, new CompoundTag());
                    } else {
                        nbt.put("recipe_" + i, this.slot.get(i).serializeNBT());
                    }
                }
            }
        }
    }

}
