package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileRadiationPurifier extends TileElectricMachine {


    private final int type;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();

    public TileRadiationPurifier() {
        super(50000, 14, 1);
        this.type = 1;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.radiation_purifier;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public void onUnloaded() {
        super.onUnloaded();

    }


    public void updateEntityServer() {

        super.updateEntityServer();

        if (this.energy.canUseEnergy(10 * type)) {
            this.energy.useEnergy(10 * type);
            if (!this.getActive()) {
                setActive(true);
            }
            initiate(0);
        } else {
            if (this.getActive()) {
                setActive(false);
            }
            initiate(2);
        }
        if (getWorld().provider.getWorldTime() % 300 == 0) {
            initiate(2);
        }
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }


    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        return false;
    }


    @Override
    public ContainerBase<?> getGuiContainer(final EntityPlayer entityPlayer) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return null;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.radiation.getSoundEvent();
    }

}
