package com.denfop.tiles.reactors.heat.circulationpump;

import com.denfop.container.ContainerHeatCirculationPump;
import com.denfop.gui.GuiHeatCirculationPump;
import com.denfop.invslot.InvSlot;
import com.denfop.items.reactors.ItemsPumps;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.heat.ICirculationPump;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBaseCirculationPump extends TileEntityMultiBlockElement implements ICirculationPump {

    private final int level;
    private final InvSlot slot;
    private int power;
    private int energy;

    public TileEntityBaseCirculationPump(int level) {
        this.level = level;
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemsPumps && ((ItemsPumps) stack.getItem()).getLevel() <= ((TileEntityBaseCirculationPump) this.base).getBlockLevel();
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (content.isEmpty()) {
                    ((TileEntityBaseCirculationPump) this.base).setEnergy(0);
                    ((TileEntityBaseCirculationPump) this.base).setPower(0);
                } else {
                    ((TileEntityBaseCirculationPump) this.base).setEnergy(((ItemsPumps) content.getItem()).getEnergy());
                    ((TileEntityBaseCirculationPump) this.base).setPower(((ItemsPumps) content.getItem()).getPower());
                }
            }
        };
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.getWorldTime() % 20 == 0) {
            if (!this.getSlot().get().isEmpty() && ((ItemsPumps) this.getSlot().get().getItem()).getDurabilityForDisplay(this
                    .getSlot()
                    .get()) == 1) {
                this.getSlot().put(0, ItemStack.EMPTY);
            }

        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            if (this.getSlot().get().isEmpty()) {
                this.setEnergy(0);
                this.setPower(0);

            } else {

                this.setEnergy(((ItemsPumps) this.getSlot().get().getItem()).getEnergy());
                this.setPower(((ItemsPumps) this.getSlot().get().getItem()).getPower());
            }
        }
    }

    @Override
    public ContainerHeatCirculationPump getGuiContainer(final EntityPlayer var1) {
        return new ContainerHeatCirculationPump(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiHeatCirculationPump(getGuiContainer(var1));
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public int getBlockLevel() {
        return level;
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
