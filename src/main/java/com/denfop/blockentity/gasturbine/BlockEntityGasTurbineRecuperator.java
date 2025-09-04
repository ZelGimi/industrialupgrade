package com.denfop.blockentity.gasturbine;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.graphite.IExchangerItem;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasTurbineEntity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuGasTurbineRecuperator;
import com.denfop.inventory.Inventory;
import com.denfop.screen.ScreenGasTurbineRecuperator;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntityGasTurbineRecuperator extends BlockEntityMultiBlockElement implements IRecuperator {

    private final Inventory inventory;
    double power;

    public BlockEntityGasTurbineRecuperator(BlockPos pos, BlockState state) {
        super(BlockGasTurbineEntity.gas_turbine_recuperator, pos, state);
        this.inventory = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof IExchangerItem;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (content.isEmpty()) {
                    power = 0;
                } else {
                    power = getPowerFromLevel((IExchangerItem) content.getItem());
                }
                return content;
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.EXCHANGE;
            }

            private double getPowerFromLevel(IExchangerItem item) {
                switch (item.getLevelExchanger()) {
                    case 2:
                        return 1.025;
                    case 3:
                        return 1.05;
                    case 4:
                        return 1.075;
                    default:
                        return 1;
                }
            }
        };
    }

    @Override
    public ContainerMenuGasTurbineRecuperator getGuiContainer(final Player var1) {
        return new ContainerMenuGasTurbineRecuperator(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenGasTurbineRecuperator((ContainerMenuGasTurbineRecuperator) menu);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasTurbineEntity.gas_turbine_recuperator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gasTurbine.getBlock(getTeBlock());
    }

    @Override
    public Inventory getExchanger() {
        return inventory;
    }

    @Override
    public double getPower() {
        return power;
    }

}
