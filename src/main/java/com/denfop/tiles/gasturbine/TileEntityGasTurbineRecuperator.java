package com.denfop.tiles.gasturbine;

import com.denfop.IUItem;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasTurbine;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerGasTurbineRecuperator;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiGasTurbineRecuperator;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.graphite.IExchangerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TileEntityGasTurbineRecuperator extends TileEntityMultiBlockElement implements IRecuperator {

    private final InvSlot invSlot;
    double power;

    public TileEntityGasTurbineRecuperator(BlockPos pos, BlockState state) {
        super(BlockGasTurbine.gas_turbine_recuperator, pos, state);
        this.invSlot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
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
    public ContainerGasTurbineRecuperator getGuiContainer(final Player var1) {
        return new ContainerGasTurbineRecuperator(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiGasTurbineRecuperator((ContainerGasTurbineRecuperator) menu);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasTurbine.gas_turbine_recuperator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gasTurbine.getBlock(getTeBlock());
    }

    @Override
    public InvSlot getExchanger() {
        return invSlot;
    }

    @Override
    public double getPower() {
        return power;
    }

}
