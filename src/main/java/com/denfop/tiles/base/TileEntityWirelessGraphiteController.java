package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerWirelessControllerGraphiteReactors;
import com.denfop.gui.GuiWirelessControllerGraphiteReactors;
import com.denfop.invslot.Inventory;
import com.denfop.items.ItemReactorData;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.tiles.reactors.graphite.IGraphiteController;
import com.denfop.tiles.reactors.graphite.graphite_controller.TileEntityGraphiteController;
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

public class TileEntityWirelessGraphiteController extends TileEntityInventory implements IUpdatableTileEvent {


    public final Inventory invslot;
    public List<ItemStack> itemStacks = new LinkedList<>();
    public List<TileEntityGraphiteController> graphiteControllers = new LinkedList<>();
    public TileMultiBlockBase tileMultiBlockBase = null;

    public TileEntityWirelessGraphiteController() {
        this.invslot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
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
    public ContainerWirelessControllerGraphiteReactors getGuiContainer(final EntityPlayer var1) {
        return new ContainerWirelessControllerGraphiteReactors(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiWirelessControllerGraphiteReactors(getGuiContainer(var1));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.wireless_controller_graphite_reactors;
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

        graphiteControllers.clear();
        itemStacks.clear();
        tileMultiBlockBase = null;
        final NBTTagCompound nbt = ModUtils.nbt(this.invslot.get(0));
        BlockPos pos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
        TileEntity tileEntity = this.getWorld().getTileEntity(pos);
        if (tileEntity instanceof TileMultiBlockBase && tileEntity instanceof IGraphiteReactor) {
            this.tileMultiBlockBase = (TileMultiBlockBase) tileEntity;
            if (this.tileMultiBlockBase.isFull()) {
                final List<BlockPos> pos1 = this.tileMultiBlockBase
                        .getMultiBlockStucture()
                        .getPosFromClass(tileMultiBlockBase.getFacing(), tileMultiBlockBase.getBlockPos(),
                                IGraphiteController.class
                        );
                for (BlockPos pos2 : pos1) {
                    TileEntity tileEntity1 = this.getWorld().getTileEntity(pos2);
                    if (tileEntity1 != null) {
                        graphiteControllers.add((TileEntityGraphiteController) tileEntity1);
                        this.itemStacks.add(((TileEntityGraphiteController) tileEntity1).getPickBlock(null, null));
                    }
                }
            }

        } else {
            this.itemStacks.add(ItemStack.EMPTY);
        }
    }


    @Override
    public void onLoaded() {
        super.onLoaded();

    }

    @Override
    public void loadBeforeFirstUpdate() {
        super.loadBeforeFirstUpdate();
        if (!this.getWorld().isRemote) {
            updateList();
        }
    }

    public void updateList() {
        itemStacks.clear();
        graphiteControllers.clear();
        itemStacks.clear();
        tileMultiBlockBase = null;
        final NBTTagCompound nbt = ModUtils.nbt(this.invslot.get(0));
        BlockPos pos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
        TileEntity tileEntity = this.getWorld().getTileEntity(pos);
        if (tileEntity instanceof TileMultiBlockBase && tileEntity instanceof IGraphiteReactor) {
            this.tileMultiBlockBase = (TileMultiBlockBase) tileEntity;
            if (this.tileMultiBlockBase.isFull()) {
                final List<BlockPos> pos1 = this.tileMultiBlockBase
                        .getMultiBlockStucture()
                        .getPosFromClass(tileMultiBlockBase.getFacing(), tileMultiBlockBase.getBlockPos(),
                                IGraphiteController.class
                        );
                for (BlockPos pos2 : pos1) {
                    TileEntity tileEntity1 = this.getWorld().getTileEntity(pos2);
                    if (tileEntity1 != null) {
                        graphiteControllers.add((TileEntityGraphiteController) tileEntity1);
                        this.itemStacks.add(((TileEntityGraphiteController) tileEntity1).getPickBlock(null, null));
                    }
                }
            }

        } else {
            this.itemStacks.add(ItemStack.EMPTY);
        }
    }


    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (!this.invslot.get(0).isEmpty() && this.tileMultiBlockBase != null) {
            if (graphiteControllers.size() < var2) {
                graphiteControllers.clear();
                if (this.tileMultiBlockBase.isFull()) {
                    final List<BlockPos> pos1 = this.tileMultiBlockBase
                            .getMultiBlockStucture()
                            .getPosFromClass(tileMultiBlockBase.getFacing(), tileMultiBlockBase.getBlockPos(),
                                    IGraphiteController.class
                            );
                    for (BlockPos pos2 : pos1) {
                        TileEntity tileEntity1 = this.getWorld().getTileEntity(pos2);
                        if (tileEntity1 != null) {
                            graphiteControllers.add((TileEntityGraphiteController) tileEntity1);

                        }
                    }
                }
            }
            TileEntityGraphiteController controller = this.graphiteControllers.get((int) var2);

            if (controller != null && controller.getMain() != null && controller.getMain().isFull() && !controller.isInvalid()) {
                controller.onActivated(var1, var1.getActiveHand(), EnumFacing.NORTH, 0, 0, 0);
            }
        }
    }

}
