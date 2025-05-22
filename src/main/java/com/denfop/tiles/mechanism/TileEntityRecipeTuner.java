package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerTunerRecipe;
import com.denfop.gui.GuiRecipeTuner;
import com.denfop.invslot.InvSlot;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityRecipeTuner extends TileEntityInventory implements IUpdatableTileEvent {

    public final InvSlot slot;
    public final InvSlot input_slot;

    public TileEntityRecipeTuner() {
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.recipe_schedule;
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 9);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiRecipeTuner(getGuiContainer(var1));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.recipe_tuner;
    }

    @Override
    public ContainerTunerRecipe getGuiContainer(final EntityPlayer var1) {
        return new ContainerTunerRecipe(this, var1);
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (var2 == 0) {
            if (!this.input_slot.isEmpty()) {
                final NBTTagCompound nbt = ModUtils.nbt(this.input_slot.get());
                final boolean mode = nbt.getBoolean("mode");
                nbt.setBoolean("mode", !mode);
            }
        } else if (var2 == 1) {
            if (!this.input_slot.isEmpty()) {
                final NBTTagCompound nbt = ModUtils.nbt(this.input_slot.get());
                for (int i = 0; i < 9; i++) {
                    if (this.slot.get(i).isEmpty()) {
                        nbt.setTag("recipe_" + i, new NBTTagCompound());
                    } else {
                        nbt.setTag("recipe_" + i, this.slot.get(i).serializeNBT());
                    }
                }
            }
        }
    }

}
