package com.denfop.tiles.gasturbine;

import com.denfop.IUItem;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasTurbine;
import com.denfop.container.ContainerGasTurbineRecuperator;
import com.denfop.gui.GuiGasTurbineRecuperator;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.graphite.IExchangerItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityGasTurbineRecuperator extends TileEntityMultiBlockElement implements IRecuperator {

    private final InvSlot invSlot;
    double power;

    public TileEntityGasTurbineRecuperator() {
        this.invSlot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof IExchangerItem;
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (content.isEmpty()) {
                    power = 0;
                } else {
                    power = getPowerFromLevel((IExchangerItem) content.getItem());
                }
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.EXCHANGE;
            }

            private double getPowerFromLevel(IExchangerItem item) {
                switch (item.getLevel()) {
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
    public ContainerGasTurbineRecuperator getGuiContainer(final EntityPlayer var1) {
        return new ContainerGasTurbineRecuperator(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiGasTurbineRecuperator(getGuiContainer(var1));
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
        return IUItem.gasTurbine;
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
