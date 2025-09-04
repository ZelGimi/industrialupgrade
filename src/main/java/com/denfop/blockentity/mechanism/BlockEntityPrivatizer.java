package com.denfop.blockentity.mechanism;


import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuPrivatizer;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.inventory.InventoryPrivatizer;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenPrivatizer;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityPrivatizer extends BlockEntityElectricMachine
        implements IUpdatableTileEvent {


    public final InventoryPrivatizer inputslot;
    public final InventoryPrivatizer inputslotA;
    public List<String> listItems = new ArrayList<>();

    public BlockEntityPrivatizer(BlockPos pos, BlockState state) {
        super(0, 10, 1, BlockBaseMachine3Entity.privatizer, pos, state);


        this.inputslot = new InventoryPrivatizer(this, 0, 9);
        this.inputslotA = new InventoryPrivatizer(this, 1, 1);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.privatizer_mechanism.info"));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.privatizer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenPrivatizer((ContainerMenuPrivatizer) menu);
    }

    public ContainerMenuPrivatizer getGuiContainer(Player entityPlayer) {
        return new ContainerMenuPrivatizer(entityPlayer, this);
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
            this.inputslotA.get(0).set(DataComponentsInit.LIST_STRING, new ArrayList<>(this.listItems));
        }


    }

}
