package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerPalletGenerator;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiPalletGenerator;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

public class TileEntityPalletGenerator extends TileElectricMachine {

    public static Map<ItemStack, Double> integerMap = new HashMap<>();
    public final InvSlot slot;
    public final ComponentBaseEnergy rad;
    public boolean update = true;
    public double generate = 0;


    public TileEntityPalletGenerator(BlockPos pos, BlockState state) {
        super(0, 14, 0,BlockBaseMachine3.pallet_generator,pos,state);
        this.energy = this.addComponent(Energy.asBasicSource(this, 5000000, tier));
        this.rad = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.RADIATION, this, 50000D));
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 6) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {

                for (Map.Entry<ItemStack, Double> entry : TileEntityPalletGenerator.integerMap.entrySet()) {
                    if (entry.getKey().is(stack.getItem())) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                TileEntityPalletGenerator tile = (TileEntityPalletGenerator) this.base;
                tile.update = true;
                return content;
            }

        };
        this.slot.setStackSizeLimit(1);
    }


    public static void init() {
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.nuclear_res.getStack(8)), 1.1);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(8)), 1.2);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.nuclear_res.getStack(9)), 1.3);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(4)), 1.6);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(0)), 1.75);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(1)), 1.9);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(9)), 2.05);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(2)), 2.2);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(3)), 2.45);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(6)), 2.7);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(7)), 2.85);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(10)), 3.162);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(5)), 3.32);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(11)), 3.7);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(12)), 4d);
    }

    @Override
    public ContainerPalletGenerator getGuiContainer(final Player var1) {
        return new ContainerPalletGenerator(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiPalletGenerator((ContainerPalletGenerator) menu);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.update) {
            this.update = false;
            double num = 0;
            this.generate = 0;
            for (ItemStack stack : this.slot) {
                for (Map.Entry<ItemStack, Double> entry : TileEntityPalletGenerator.integerMap.entrySet()) {
                    if (entry.getKey().is(stack.getItem())) {
                        if (num == 0) {
                            num = entry.getValue();
                        } else {
                            num *= entry.getValue();
                        }
                    }
                }
            }
            if (num != 0) {
                this.generate = 20000 / Math.ceil(num);
            }
        }
        if (this.generate != 0) {
            double num = this.energy.getFreeEnergy() / 50;
            if (num >= 1) {
                double num1 = this.rad.getEnergy() / this.generate;
                if (num1 >= 1) {
                    double num2 = Math.min(Math.min(num, num1), 10);
                    this.rad.useEnergy(num2 * this.generate);
                    this.energy.addEnergy(num2 * 50);
                    this.setActive(true);
                } else {
                    this.setActive(false);
                }

            } else {
                this.setActive(false);
            }

        } else {
            this.setActive(false);
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.pallet_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
