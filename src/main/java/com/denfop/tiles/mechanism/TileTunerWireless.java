package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerTunerWireless;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiTunerWireless;
import com.denfop.invslot.InvSlotTuner;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileTunerWireless extends TileElectricMachine
        implements IUpdatableTileEvent {


    public final InvSlotTuner inputslot;


    public TileTunerWireless(BlockPos pos, BlockState state) {
        super(0, 10, 1,BlockBaseMachine3.tuner,pos,state);


        this.inputslot = new InvSlotTuner(this);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.tuner;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public void updateEntityServer() {

        super.updateEntityServer();
        if (getWorld().getGameTime() % 40 == 0) {
            if (getActive()) {
                setActive(false);
            }
        }
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiTunerWireless((ContainerTunerWireless) menu);
    }

    public ContainerTunerWireless getGuiContainer(Player entityPlayer) {
        return new ContainerTunerWireless(entityPlayer, this);
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.pen.getSoundEvent();
    }

    @Override
    public void updateTileServer(Player player, double event) {
        if (!this.inputslot.isEmpty()) {
            initiate(1);
            CompoundTag nbt = ModUtils.nbt(this.inputslot.get(0));
            boolean change = nbt.getBoolean("change");
            change = !change;
            nbt.putBoolean("change", change);
            setActive(true);
        }


    }

}
