package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerModuleMachine;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiModuleMachine;
import com.denfop.invslot.InvSlotModule;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class TileModuleMachine extends TileElectricMachine
        implements IUpdatableTileEvent {


    public final InvSlotModule inputslot;
    public final InvSlotModule inputslotA;
    public List<TagKey<Item>> listItems = new ArrayList<>();

    public TileModuleMachine(BlockPos pos, BlockState state) {
        super(0, 10, 0, BlockBaseMachine.modulator, pos, state);


        this.inputslot = new InvSlotModule(this, 0, 27);
        inputslot.setStackSizeLimit(1);
        this.inputslotA = new InvSlotModule(this, 1, 1);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.modulator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines.getBlock(getTeBlock());
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.inputslot.update();
    }


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiModuleMachine((ContainerModuleMachine) menu);
    }

    public ContainerModuleMachine getGuiContainer(Player entityPlayer) {
        return new ContainerModuleMachine(entityPlayer, this);
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.pen.getSoundEvent();
    }


    @Override
    public void updateTileServer(Player player, double event) {
        if (!this.inputslotA.isEmpty()) {
            initiate(2);
            initiate(0);
            List<String> listString = new ArrayList<>();
            for (TagKey<Item> tagKey : this.listItems) {
                listString.add(tagKey.location().toString());

            }

            this.inputslotA.get(0).set(DataComponentsInit.LIST_STRING, listString);
        }


    }

}
