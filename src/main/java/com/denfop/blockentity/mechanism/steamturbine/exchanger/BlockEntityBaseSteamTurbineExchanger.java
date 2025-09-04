package com.denfop.blockentity.mechanism.steamturbine.exchanger;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.mechanism.steamturbine.IController;
import com.denfop.blockentity.mechanism.steamturbine.IExchanger;
import com.denfop.blockentity.reactors.graphite.IExchangerItem;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuBaseSteamTurbineExchanger;
import com.denfop.inventory.Inventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenBaseSteamTurbineExchanger;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntityBaseSteamTurbineExchanger extends BlockEntityMultiBlockElement implements IExchanger {

    private final int levelBlock;
    private final Inventory slot;
    public double percent = 1;
    private IExchangerItem item;

    public BlockEntityBaseSteamTurbineExchanger(int levelBlock, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.levelBlock = levelBlock;
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.EXCHANGE;
            }

            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof IExchangerItem && ((IExchangerItem) stack.getItem()).getLevelExchanger() <= ((IController) getMain()).getBlockLevel();
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (!level.isClientSide) {
                    if (content.isEmpty()) {
                        ((BlockEntityBaseSteamTurbineExchanger) this.base).percent = 0;
                    } else {
                        ((BlockEntityBaseSteamTurbineExchanger) this.base).percent = ((IExchangerItem) content.getItem()).getPercent();
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
    public ContainerMenuBaseSteamTurbineExchanger getGuiContainer(final Player var1) {
        return new ContainerMenuBaseSteamTurbineExchanger(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenBaseSteamTurbineExchanger((ContainerMenuBaseSteamTurbineExchanger) menu);
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
    public Inventory getSlot() {
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
