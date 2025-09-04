package com.denfop.blockentity.reactors.heat.circulationpump;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.heat.ICirculationPump;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuHeatCirculationPump;
import com.denfop.inventory.Inventory;
import com.denfop.items.reactors.ItemsPumps;
import com.denfop.screen.ScreenHeatCirculationPump;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BlockEntityBaseCirculationPump extends BlockEntityMultiBlockElement implements ICirculationPump {

    private final int levelBlock;
    private final Inventory slot;
    private int power;
    private int energy;

    public BlockEntityBaseCirculationPump(int levelBlock, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.levelBlock = levelBlock;
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ItemsPumps && ((ItemsPumps) stack.getItem()).getLevel() <= ((BlockEntityBaseCirculationPump) this.base).getBlockLevel();
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (content.isEmpty()) {
                    ((BlockEntityBaseCirculationPump) this.base).setEnergy(0);
                    ((BlockEntityBaseCirculationPump) this.base).setPower(0);
                } else {
                    ((BlockEntityBaseCirculationPump) this.base).setEnergy(((ItemsPumps) content.getItem()).getEnergy());
                    ((BlockEntityBaseCirculationPump) this.base).setPower(((ItemsPumps) content.getItem()).getPower());
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
    public ContainerMenuHeatCirculationPump getGuiContainer(final Player var1) {
        return new ContainerMenuHeatCirculationPump(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenHeatCirculationPump((ContainerMenuHeatCirculationPump) menu);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
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

}
