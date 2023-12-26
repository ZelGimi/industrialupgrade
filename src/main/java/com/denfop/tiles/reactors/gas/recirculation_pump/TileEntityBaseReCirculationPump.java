package com.denfop.tiles.reactors.gas.recirculation_pump;

import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerReCirculationPump;
import com.denfop.gui.GuiReCirculationPump;
import com.denfop.invslot.InvSlot;
import com.denfop.items.reactors.ItemsPumps;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.IRecirculationPump;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBaseReCirculationPump extends TileEntityMultiBlockElement implements IRecirculationPump {
    private final int level;
    private final InvSlot slot;
    private  int power;
    private int energy;
    public TileEntityBaseReCirculationPump(int level){
        this.level = level;
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT,1){
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemsPumps && ((ItemsPumps) stack.getItem()).getLevel() <= ((TileEntityBaseReCirculationPump)this.base).getLevel();
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if(content.isEmpty()){
                    ((TileEntityBaseReCirculationPump)this.base).setEnergy(0);
                    ((TileEntityBaseReCirculationPump)this.base).setPower(0);
                }else{
                    ((TileEntityBaseReCirculationPump)this.base).setEnergy(((ItemsPumps) content.getItem()).getEnergy());
                    ((TileEntityBaseReCirculationPump)this.base).setPower(((ItemsPumps) content.getItem()).getPower());
                }
            }
        };
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if(!this.getWorld().isRemote){
            if(this.getSlot().get().isEmpty()){
                this.setEnergy(0);
                this.setPower(0);
            }else{
                this.setEnergy(((ItemsPumps) this.getSlot().get().getItem()).getEnergy());
                this.setPower(((ItemsPumps) this.getSlot().get().getItem()).getPower());
            }
        }
    }

    @Override
    public ContainerReCirculationPump getGuiContainer(final EntityPlayer var1) {
        return new ContainerReCirculationPump(this,var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiReCirculationPump(getGuiContainer(var1));
    }
    @Override
    public boolean hasOwnInventory() {
        return true;
    }
    public void setEnergy(final int energy) {
        this.energy = energy;
    }

    public void setPower(final int power) {
        this.power = power;
    }

    @Override
    public int getLevel() {
        return level;
    }

    public int getEnergy() {
        return energy;
    }

    public int getPower() {
        return power;
    }
    public InvSlot getSlot() {
        return slot;
    }
}
