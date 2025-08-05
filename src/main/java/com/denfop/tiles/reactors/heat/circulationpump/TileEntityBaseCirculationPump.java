package com.denfop.tiles.reactors.heat.circulationpump;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerHeatCirculationPump;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiHeatCirculationPump;
import com.denfop.invslot.InvSlot;
import com.denfop.items.reactors.ItemsPumps;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.heat.ICirculationPump;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TileEntityBaseCirculationPump extends TileEntityMultiBlockElement implements ICirculationPump {

    private final int levelBlock;
    private final InvSlot slot;
    private int power;
    private int energy;

    public TileEntityBaseCirculationPump(int levelBlock, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.levelBlock = levelBlock;
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemsPumps && ((ItemsPumps) stack.getItem()).getLevel() <= ((TileEntityBaseCirculationPump) this.base).getBlockLevel();
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (content.isEmpty()) {
                    ((TileEntityBaseCirculationPump) this.base).setEnergy(0);
                    ((TileEntityBaseCirculationPump) this.base).setPower(0);
                } else {
                    ((TileEntityBaseCirculationPump) this.base).setEnergy(((ItemsPumps) content.getItem()).getEnergy());
                    ((TileEntityBaseCirculationPump) this.base).setPower(((ItemsPumps) content.getItem()).getPower());
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
    public ContainerHeatCirculationPump getGuiContainer(final Player var1) {
        return new ContainerHeatCirculationPump(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiHeatCirculationPump((ContainerHeatCirculationPump) menu);
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

    public InvSlot getSlot() {
        return slot;
    }

}
