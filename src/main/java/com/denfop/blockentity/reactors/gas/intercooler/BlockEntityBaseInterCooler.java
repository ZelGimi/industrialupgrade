package com.denfop.blockentity.reactors.gas.intercooler;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.gas.IInterCooler;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuInterCooler;
import com.denfop.inventory.Inventory;
import com.denfop.items.reactors.ItemsFan;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenInterCooler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BlockEntityBaseInterCooler extends BlockEntityMultiBlockElement implements IInterCooler {

    private final int levelBlock;
    private final Inventory slot;
    private int power;
    private int energy;

    public BlockEntityBaseInterCooler(int levelBlock, MultiBlockEntity multiTileBlock, BlockPos pos, BlockState state) {
        super(multiTileBlock, pos, state);
        this.levelBlock = levelBlock;
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ItemsFan && ((ItemsFan) stack.getItem()).getLevel() <= ((BlockEntityBaseInterCooler) this.base).getBlockLevel();
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (!level.isClientSide) {
                    if (content.isEmpty()) {
                        ((BlockEntityBaseInterCooler) this.base).setEnergy(0);
                        ((BlockEntityBaseInterCooler) this.base).setPower(0);
                    } else {
                        ((BlockEntityBaseInterCooler) this.base).setEnergy(((ItemsFan) content.getItem()).getEnergy());
                        ((BlockEntityBaseInterCooler) this.base).setPower(((ItemsFan) content.getItem()).getPower());
                    }
                }
                return content;
            }
        };
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 20 == 0) {
            if (!this.getSlot().get(0).isEmpty() && ((ItemsFan) this.getSlot().get(0).getItem()).getBarWidth(this
                    .getSlot()
                    .get(0)) == 0) {
                this.getSlot().set(0, ItemStack.EMPTY);
            }

        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            if (this.getSlot().get(0).isEmpty()) {
                this.setEnergy(0);
                this.setPower(0);
            } else {

                this.setEnergy(((ItemsFan) this.getSlot().get(0).getItem()).getEnergy());
                this.setPower(((ItemsFan) this.getSlot().get(0).getItem()).getPower());

            }
        }
    }

    @Override
    public int getBlockLevel() {
        return levelBlock;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(final int energy) {
        this.energy = energy;
    }

    public int getPower() {
        return power;
    }

    public void setPower(final int power) {
        this.power = power;
    }

    public Inventory getSlot() {
        return slot;
    }

    @Override
    public ContainerMenuInterCooler getGuiContainer(final Player var1) {
        return new ContainerMenuInterCooler(this, var1);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenInterCooler((ContainerMenuInterCooler) menu);
    }

}
