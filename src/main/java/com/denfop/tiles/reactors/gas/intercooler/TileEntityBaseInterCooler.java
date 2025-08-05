package com.denfop.tiles.reactors.gas.intercooler;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerInterCooler;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiInterCooler;
import com.denfop.invslot.InvSlot;
import com.denfop.items.reactors.ItemsFan;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.IInterCooler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TileEntityBaseInterCooler extends TileEntityMultiBlockElement implements IInterCooler {

    private final int levelBlock;
    private final InvSlot slot;
    private int power;
    private int energy;

    public TileEntityBaseInterCooler(int levelBlock, IMultiTileBlock multiTileBlock, BlockPos pos, BlockState state) {
        super(multiTileBlock, pos, state);
        this.levelBlock = levelBlock;
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemsFan && ((ItemsFan) stack.getItem()).getLevel() <= ((TileEntityBaseInterCooler) this.base).getBlockLevel();
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (!level.isClientSide) {
                    if (content.isEmpty()) {
                        ((TileEntityBaseInterCooler) this.base).setEnergy(0);
                        ((TileEntityBaseInterCooler) this.base).setPower(0);
                    } else {
                        ((TileEntityBaseInterCooler) this.base).setEnergy(((ItemsFan) content.getItem()).getEnergy());
                        ((TileEntityBaseInterCooler) this.base).setPower(((ItemsFan) content.getItem()).getPower());
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

    public InvSlot getSlot() {
        return slot;
    }

    @Override
    public ContainerInterCooler getGuiContainer(final Player var1) {
        return new ContainerInterCooler(this, var1);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiInterCooler((ContainerInterCooler) menu);
    }

}
