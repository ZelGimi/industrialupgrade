package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerWirelessControllerReactors;
import com.denfop.gui.GuiWirelessControllerReactors;
import com.denfop.invslot.Inventory;
import com.denfop.items.ItemReactorData;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.LinkedList;
import java.util.List;

public class TileEntityWirelessControllerReactors extends TileEntityInventory implements IUpdatableTileEvent {


    public final Inventory invslot;
    public List<ItemStack> itemStacks = new LinkedList<>();

    public List<TileMultiBlockBase> tileMultiBlockBaseList = new LinkedList<>();

    public TileEntityWirelessControllerReactors() {
        this.invslot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 12) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                updateList();
            }

            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                if (!(stack.getItem() instanceof ItemReactorData)) {
                    return false;
                }
                final NBTTagCompound nbt = ModUtils.nbt(stack);
                return !nbt.getString("name").isEmpty();
            }
        };
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public ContainerWirelessControllerReactors getGuiContainer(final EntityPlayer var1) {
        return new ContainerWirelessControllerReactors(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiWirelessControllerReactors(getGuiContainer(var1));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.wireless_controller_reactors;
    }


    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {

        }

        super.addInformation(stack, tooltip);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);

        tileMultiBlockBaseList.clear();
        itemStacks.clear();
        for (int i = 0; i < 12; i++) {
            final NBTTagCompound nbt = ModUtils.nbt(this.invslot.get(i));
            BlockPos pos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
            TileEntity tileEntity = this.getWorld().getTileEntity(pos);
            if (tileEntity instanceof TileMultiBlockBase && tileEntity instanceof IAdvReactor) {
                this.tileMultiBlockBaseList.add((TileMultiBlockBase) tileEntity);
                this.itemStacks.add(((TileMultiBlockBase) tileEntity).getPickBlock(null, null));
            } else {
                this.tileMultiBlockBaseList.add(null);
                this.itemStacks.add(ItemStack.EMPTY);
            }
        }
    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            updateList();
        }
    }

    public void updateList() {
        itemStacks.clear();
        tileMultiBlockBaseList.clear();
        for (int i = 0; i < 12; i++) {
            final NBTTagCompound nbt = ModUtils.nbt(this.invslot.get(i));
            BlockPos pos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
            TileEntity tileEntity = this.getWorld().getTileEntity(pos);
            if (tileEntity instanceof TileMultiBlockBase && tileEntity instanceof IAdvReactor) {
                this.tileMultiBlockBaseList.add((TileMultiBlockBase) tileEntity);
                this.itemStacks.add(((TileMultiBlockBase) tileEntity).getPickBlock(null, null));
            } else {
                this.tileMultiBlockBaseList.add(null);
                this.itemStacks.add(ItemStack.EMPTY);
            }
        }
    }


    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (!this.itemStacks.get((int) var2).isEmpty()) {
            TileMultiBlockBase tileMultiBlockBase = this.tileMultiBlockBaseList.get((int) var2);
            if (tileMultiBlockBase != null && tileMultiBlockBase.isFull() && !tileMultiBlockBase.isInvalid()) {
                tileMultiBlockBase.onActivated(var1, var1.getActiveHand(), EnumFacing.NORTH, 0, 0, 0);

            }
        }
    }

}
