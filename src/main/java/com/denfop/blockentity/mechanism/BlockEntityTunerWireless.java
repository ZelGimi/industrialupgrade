package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuTunerWireless;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.WirelessConnection;
import com.denfop.inventory.InventoryTuner;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenTunerWireless;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public class BlockEntityTunerWireless extends BlockEntityElectricMachine
        implements IUpdatableTileEvent {


    public final InventoryTuner inputslot;


    public BlockEntityTunerWireless(BlockPos pos, BlockState state) {
        super(0, 10, 1, BlockBaseMachine3Entity.tuner, pos, state);


        this.inputslot = new InventoryTuner(this);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.wireless_mechanism.info"));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.tuner;
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
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenTunerWireless((ContainerMenuTunerWireless) menu);
    }

    public ContainerMenuTunerWireless getGuiContainer(Player entityPlayer) {
        return new ContainerMenuTunerWireless(entityPlayer, this);
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.pen.getSoundEvent();
    }

    @Override
    public void updateTileServer(Player player, double event) {
        if (!this.inputslot.isEmpty()) {
            initiate(1);
            WirelessConnection wirelessConnection = this.inputslot.get(0).getOrDefault(DataComponentsInit.WIRELESS, WirelessConnection.EMPTY);

            boolean change = wirelessConnection.change();
            change = !change;
            wirelessConnection.updateChange(this.inputslot.get(0), change);
            setActive(true);
        }


    }

}
