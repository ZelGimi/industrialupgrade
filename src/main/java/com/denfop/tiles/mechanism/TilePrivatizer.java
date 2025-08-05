package com.denfop.tiles.mechanism;


import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerPrivatizer;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiPrivatizer;
import com.denfop.invslot.InvSlotPrivatizer;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class TilePrivatizer extends TileElectricMachine
        implements IUpdatableTileEvent {


    public final InvSlotPrivatizer inputslot;
    public final InvSlotPrivatizer inputslotA;
    public List<String> listItems = new ArrayList<>();

    public TilePrivatizer(BlockPos pos, BlockState state) {
        super(0, 10, 1,BlockBaseMachine3.privatizer,pos,state);


        this.inputslot = new InvSlotPrivatizer(this, 0, 9);
        this.inputslotA = new InvSlotPrivatizer(this, 1, 1);
    }
    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.privatizer_mechanism.info"));
    }
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.privatizer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }





    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiPrivatizer((ContainerPrivatizer) menu);
    }

    public ContainerPrivatizer getGuiContainer(Player entityPlayer) {
        return new ContainerPrivatizer(entityPlayer, this);
    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        this.inputslot.update();
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.pen.getSoundEvent();
    }


    @Override
    public void updateTileServer(Player player, double event) {
        if (!this.inputslotA.isEmpty()) {
            initiate(1);
            CompoundTag nbt = ModUtils.nbt(this.inputslotA.get(0));
            for (int i = 0; i < this.listItems.size(); i++) {
                nbt.putString("player_" + i, this.listItems.get(i));

            }
            nbt.putInt("size", this.listItems.size());
        }


    }

}
