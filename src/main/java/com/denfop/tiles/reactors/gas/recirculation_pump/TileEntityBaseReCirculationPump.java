package com.denfop.tiles.reactors.gas.recirculation_pump;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerReCirculationPump;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiReCirculationPump;
import com.denfop.invslot.InvSlot;
import com.denfop.items.reactors.ItemsPumps;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.IRecirculationPump;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityBaseReCirculationPump extends TileEntityMultiBlockElement implements IRecirculationPump {

    private final int levels;
    private final InvSlot slot;
    private int power;
    private int energy;

    public TileEntityBaseReCirculationPump(int levels, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block,pos,state);
        this.levels = levels;
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemsPumps && ((ItemsPumps) stack.getItem()).getLevel() <= ((TileEntityBaseReCirculationPump) this.base).getBlockLevel();
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (!level.isClientSide) {
                    if (content.isEmpty()) {
                        ((TileEntityBaseReCirculationPump) this.base).setEnergy(0);
                        ((TileEntityBaseReCirculationPump) this.base).setPower(0);
                    } else {
                        ((TileEntityBaseReCirculationPump) this.base).setEnergy(((ItemsPumps) content.getItem()).getEnergy());
                        ((TileEntityBaseReCirculationPump) this.base).setPower(((ItemsPumps) content.getItem()).getPower());
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
    public ContainerReCirculationPump getGuiContainer(final Player var1) {
        return new ContainerReCirculationPump(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiReCirculationPump((ContainerReCirculationPump) menu);
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

    public InvSlot getSlot() {
        return slot;
    }

}
