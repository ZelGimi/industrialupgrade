package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerTunerWireless;
import com.denfop.gui.GuiTunerWireless;
import com.denfop.invslot.InvSlotTuner;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileTunerWireless extends TileElectricMachine
        implements IUpdatableTileEvent {


    public final InvSlotTuner inputslot;


    public TileTunerWireless() {
        super(0, 10, 1);


        this.inputslot = new InvSlotTuner(this);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.tuner;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public void updateEntityServer() {

        super.updateEntityServer();
        if (getWorld().provider.getWorldTime() % 40 == 0) {
            if (getActive()) {
                setActive(false);
            }
        }
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiTunerWireless(new ContainerTunerWireless(entityPlayer, this));
    }

    public ContainerTunerWireless getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerTunerWireless(entityPlayer, this);
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.pen.getSoundEvent();
    }

    @Override
    public void updateTileServer(EntityPlayer player, double event) {
        if (!this.inputslot.isEmpty()) {
            initiate(1);
            NBTTagCompound nbt = ModUtils.nbt(this.inputslot.get());
            boolean change = nbt.getBoolean("change");
            change = !change;
            nbt.setBoolean("change", change);
            setActive(true);
        }


    }

}
