package com.denfop.blockentity.reactors.gas.recirculation_pump;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.gas.IRecirculationPump;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuReCirculationPump;
import com.denfop.inventory.Inventory;
import com.denfop.items.reactors.ItemsPumps;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenReCirculationPump;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BlockEntityBaseReCirculationPump extends BlockEntityMultiBlockElement implements IRecirculationPump {

    private final int levels;
    private final Inventory slot;
    private int power;
    private int energy;

    public BlockEntityBaseReCirculationPump(int levels, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.levels = levels;
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ItemsPumps && ((ItemsPumps) stack.getItem()).getLevel() <= ((BlockEntityBaseReCirculationPump) this.base).getBlockLevel();
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (!level.isClientSide) {
                    if (content.isEmpty()) {
                        ((BlockEntityBaseReCirculationPump) this.base).setEnergy(0);
                        ((BlockEntityBaseReCirculationPump) this.base).setPower(0);
                    } else {
                        ((BlockEntityBaseReCirculationPump) this.base).setEnergy(((ItemsPumps) content.getItem()).getEnergy());
                        ((BlockEntityBaseReCirculationPump) this.base).setPower(((ItemsPumps) content.getItem()).getPower());
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
            if (!this.getSlot().get(0).isEmpty() && ((ItemsPumps) this.getSlot().get(0).getItem()).getBarWidth(this
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

                this.setEnergy(((ItemsPumps) this.getSlot().get(0).getItem()).getEnergy());
                this.setPower(((ItemsPumps) this.getSlot().get(0).getItem()).getPower());

            }
        }
    }

    @Override
    public ContainerMenuReCirculationPump getGuiContainer(final Player var1) {
        return new ContainerMenuReCirculationPump(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenReCirculationPump((ContainerMenuReCirculationPump) menu);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public int getBlockLevel() {
        return levels;
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

}
