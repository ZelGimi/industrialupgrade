package com.denfop.tiles.mechanism;

import com.denfop.tiles.base.TileEntityElectricMachine;
import ic2.core.ContainerBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

public class TileEntityMagnetGenerator extends TileEntityElectricMachine {

    public TileEntityMagnetGenerator() {
        super("", 500000, 1, 1);
    }

    @Override
    protected boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        return false;
    }

    public void updateEntityServer() {

        super.updateEntityServer();

        if (this.energy.getEnergy() + 2 < this.energy.getCapacity()) {
            this.energy.addEnergy(2);
            setActive(true);
            initiate(0);
        } else {
            initiate(2);
            setActive(false);
        }
        if (getWorld().provider.getWorldTime() % 60 == 0) {
            initiate(2);
        }
    }

    public String getStartSoundFile() {
        return "Machines/magnet_generator.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    @Override
    public ContainerBase<?> getGuiContainer(final EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return null;
    }

}
