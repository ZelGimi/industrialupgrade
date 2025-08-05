package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.reactors.IHeatReactor;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerWirelessControllerHeatReactors;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.ReactorData;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiWirelessControllerHeatReactors;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemReactorData;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.tiles.reactors.heat.IGraphiteController;
import com.denfop.tiles.reactors.heat.graphite_controller.TileEntityGraphiteController;
import com.denfop.utils.Keyboard;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TileEntityWirelessHeatController extends TileEntityInventory implements IUpdatableTileEvent {


    public final InvSlot invslot;
    public List<ItemStack> itemStacks = new LinkedList<>();
    public List<TileEntityGraphiteController> graphiteControllers = new LinkedList<>();
    public TileMultiBlockBase tileMultiBlockBase = null;

    public TileEntityWirelessHeatController(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.wireless_controller_heat_reactors, pos, state);
        this.invslot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                updateList();
                return content;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                if (!(stack.getItem() instanceof ItemReactorData)) {
                    return false;
                }
                return stack.has(DataComponentsInit.REACTOR_DATA);
            }
        };
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public ContainerWirelessControllerHeatReactors getGuiContainer(final Player var1) {
        return new ContainerWirelessControllerHeatReactors(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiWirelessControllerHeatReactors((ContainerWirelessControllerHeatReactors) menu);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.wireless_controller_heat_reactors;
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
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer packetBuffer = super.writeContainerPacket();
        updateList();
        try {
            EncoderHandler.encode(packetBuffer, this.invslot.get(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packetBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);


        try {
            this.invslot.set(0, (ItemStack) DecoderHandler.decode(customPacketBuffer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void onLoaded() {
        super.onLoaded();
    }

    @Override
    public void loadBeforeFirstUpdate() {
        super.loadBeforeFirstUpdate();
        if (!this.getWorld().isClientSide) {
            updateList();
        }
    }

    public void updateList() {

        itemStacks.clear();
        graphiteControllers.clear();
        itemStacks.clear();
        tileMultiBlockBase = null;
        if (this.invslot.get(0).isEmpty())
            return;
        ReactorData reactorData = this.invslot.get(0).get(DataComponentsInit.REACTOR_DATA);
        BlockPos pos = reactorData.pos();
        BlockEntity tileEntity = this.getWorld().getBlockEntity(pos);
        if (tileEntity instanceof TileMultiBlockBase && tileEntity instanceof IHeatReactor) {
            this.tileMultiBlockBase = (TileMultiBlockBase) tileEntity;
            if (this.tileMultiBlockBase.isFull()) {
                final List<BlockPos> pos1 = this.tileMultiBlockBase
                        .getMultiBlockStucture()
                        .getPosFromClass(tileMultiBlockBase.getFacing(), tileMultiBlockBase.getBlockPos(),
                                IGraphiteController.class
                        );
                for (BlockPos pos2 : pos1) {
                    BlockEntity tileEntity1 = this.getWorld().getBlockEntity(pos2);
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
    public void updateTileServer(final Player var1, final double var2) {
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
                        BlockEntity tileEntity1 = this.getWorld().getBlockEntity(pos2);
                        if (tileEntity1 != null) {
                            graphiteControllers.add((TileEntityGraphiteController) tileEntity1);

                        }
                    }
                }
            }
            TileEntityGraphiteController controller = this.graphiteControllers.get((int) var2);
            if (controller != null && controller.getMain() != null && controller.getMain().isFull() && !controller.isRemoved()) {
                controller.onActivated(var1, var1.getUsedItemHand(), Direction.NORTH, new Vec3(0, 0, 0));
            }
        }
    }

}
