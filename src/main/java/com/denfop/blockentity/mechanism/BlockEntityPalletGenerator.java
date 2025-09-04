package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Energy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuPalletGenerator;
import com.denfop.inventory.Inventory;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenPalletGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

public class BlockEntityPalletGenerator extends BlockEntityElectricMachine {

    public static Map<ItemStack, Double> integerMap = new HashMap<>();
    public final Inventory slot;
    public final ComponentBaseEnergy rad;
    public boolean update = true;
    public double generate = 0;


    public BlockEntityPalletGenerator(BlockPos pos, BlockState state) {
        super(0, 14, 0, BlockBaseMachine3Entity.pallet_generator, pos, state);
        this.energy = this.addComponent(Energy.asBasicSource(this, 5000000, tier));
        this.rad = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.RADIATION, this, 1000000D));
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 6) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {

                for (Map.Entry<ItemStack, Double> entry : BlockEntityPalletGenerator.integerMap.entrySet()) {
                    if (entry.getKey().is(stack.getItem())) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                BlockEntityPalletGenerator tile = (BlockEntityPalletGenerator) this.base;
                tile.update = true;
                return content;
            }

        };
        this.slot.setStackSizeLimit(1);
    }


    public static void init() {
        BlockEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.nuclear_res.getStack(8)), 1.1);
        BlockEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(8)), 1.2);
        BlockEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.nuclear_res.getStack(9)), 1.3);
        BlockEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(4)), 1.6);
        BlockEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(0)), 1.75);
        BlockEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(1)), 1.9);
        BlockEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(9)), 2.05);
        BlockEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(2)), 2.2);
        BlockEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(3)), 2.45);
        BlockEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(6)), 2.7);
        BlockEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(7)), 2.85);
        BlockEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(10)), 3.162);
        BlockEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(5)), 3.32);
        BlockEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(11)), 3.7);
        BlockEntityPalletGenerator.integerMap.put(new ItemStack(IUItem.pellets.getStack(12)), 4d);
    }

    @Override
    public ContainerMenuPalletGenerator getGuiContainer(final Player var1) {
        return new ContainerMenuPalletGenerator(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenPalletGenerator((ContainerMenuPalletGenerator) menu);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.update) {
            this.update = false;
            double num = 0;
            this.generate = 0;
            for (ItemStack stack : this.slot) {
                for (Map.Entry<ItemStack, Double> entry : BlockEntityPalletGenerator.integerMap.entrySet()) {
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

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.pallet_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
