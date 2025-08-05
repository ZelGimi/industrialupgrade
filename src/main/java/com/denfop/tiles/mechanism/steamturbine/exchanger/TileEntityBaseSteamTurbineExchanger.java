package com.denfop.tiles.mechanism.steamturbine.exchanger;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerBaseSteamTurbineExchanger;
import com.denfop.gui.GuiBaseSteamTurbineExchanger;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.steamturbine.IController;
import com.denfop.tiles.mechanism.steamturbine.IExchanger;
import com.denfop.tiles.reactors.graphite.IExchangerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TileEntityBaseSteamTurbineExchanger extends TileEntityMultiBlockElement implements IExchanger {

    private final int levelBlock;
    private final InvSlot slot;
    public double percent = 1;
    private IExchangerItem item;

    public TileEntityBaseSteamTurbineExchanger(int levelBlock, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.levelBlock = levelBlock;
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.EXCHANGE;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof IExchangerItem && ((IExchangerItem) stack.getItem()).getLevelExchanger() <= ((IController) getMain()).getBlockLevel();
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (!level.isClientSide) {
                    if (content.isEmpty()) {
                        ((TileEntityBaseSteamTurbineExchanger) this.base).percent = 0;
                    } else {
                        ((TileEntityBaseSteamTurbineExchanger) this.base).percent = ((IExchangerItem) content.getItem()).getPercent();
                        item = (IExchangerItem) content.getItem();
                    }
                }
                return content;
            }
        };
        slot.setStackSizeLimit(1);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            if (this.getSlot().get(0).isEmpty()) {
                this.percent = 0;
                this.item = null;
            } else {
                this.percent = ((IExchangerItem) this.getSlot().get(0).getItem()).getPercent();
                this.item = ((IExchangerItem) this.getSlot().get(0).getItem());
            }
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeDouble(this.percent);
        return customPacketBuffer;
    }

    @Override
    public ContainerBaseSteamTurbineExchanger getGuiContainer(final Player var1) {
        return new ContainerBaseSteamTurbineExchanger(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiBaseSteamTurbineExchanger((ContainerBaseSteamTurbineExchanger) menu);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.percent = customPacketBuffer.readDouble();
    }

    @Override
    public boolean hasOwnInventory() {
        return this.getMain() != null;
    }

    @Override
    public int getBlockLevel() {
        return -1;
    }

    @Override
    public InvSlot getSlot() {
        return slot;
    }

    @Override
    public double getPower() {
        if (this.getMain() == null || this.getSlot().isEmpty()) {
            return 1;
        }

        return this.percent * 2;
    }

    @Override
    public IExchangerItem getExchanger() {
        return item;
    }


}
