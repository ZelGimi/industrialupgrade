package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerPalletGenerator;
import com.denfop.gui.GuiPalletGenerator;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEntityPalletGenerator extends TileElectricMachine {

    public static Map<ItemStack, Double> integerMap = new HashMap<>();
    public final InvSlot slot;
    public final ComponentBaseEnergy rad;
    public  boolean update = true;
    public double generate = 0;


    public TileEntityPalletGenerator() {
        super(0, 14, 0);
        this.energy = this.addComponent(AdvEnergy.asBasicSource(this, 5000000, tier));
        this.rad = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.RADIATION, this, 50000D));
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 6) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {

                for (Map.Entry<ItemStack, Double> entry : TileEntityPalletGenerator.integerMap.entrySet()) {
                    if (entry.getKey().isItemEqual(stack)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                TileEntityPalletGenerator tile = (TileEntityPalletGenerator) this.base;
                tile.update = true;
            }

        };
        this.slot.setStackSizeLimit(1);
    }



    public static void init(){
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.nuclear_res,1,8),1.1);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets,1,8),1.2);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.nuclear_res,1,9),1.3);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.nuclear_res,1,10),1.45);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets,1,4),1.6);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets,1,0),1.75);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets,1,1),1.9);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets,1,9),2.05);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets,1,2),2.2);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets,1,3),2.45);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets,1,6),2.7);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets,1,7),2.85);
        TileEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets,1,5),3.162);
    }

    @Override
    public ContainerPalletGenerator getGuiContainer(final EntityPlayer var1) {
        return new ContainerPalletGenerator(this,var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiPalletGenerator(this.getGuiContainer(var1));
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.update) {
            this.update = false;
            double num = 0;
            this.generate = 0;
            for (ItemStack stack : this.slot.getContents()) {
                for (Map.Entry<ItemStack, Double> entry : TileEntityPalletGenerator.integerMap.entrySet()) {
                    if (entry.getKey().isItemEqual(stack)) {
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
        if (this.generate != 0 ) {
            double num = this.energy.getFreeEnergy() / 50;
            if (num >= 1) {
                double num1 = this.rad.getEnergy() / this.generate;
                if (num1 >= 1) {
                    double num2 = Math.min(Math.min(num, num1), 10);
                    this.rad.useEnergy(num2 * this.generate);
                    this.energy.addEnergy(num2 * 50);
                    this.setActive(true);
                }else{
                    this.setActive(false);
                }

            }else{
                this.setActive(false);
            }

        }else{
            this.setActive(false);
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.pallet_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
