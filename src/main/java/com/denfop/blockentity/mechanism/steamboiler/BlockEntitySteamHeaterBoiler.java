package com.denfop.blockentity.mechanism.steamboiler;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamBoilerEntity;
import com.denfop.componets.HeatComponent;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BlockEntitySteamHeaterBoiler extends BlockEntityMultiBlockElement implements IHeater {

    private final HeatComponent heat;

    public BlockEntitySteamHeaterBoiler(BlockPos pos, BlockState state) {
        super(BlockSteamBoilerEntity.steam_boiler_heater, pos, state);
        this.heat = this.addComponent(HeatComponent.asBasicSink(this, 1000));
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 40 == 0 && this.heat.getEnergy() > 0) {
            this.heat.useEnergy(1);
        }
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.heatmachine.info"));
    }

    @Override
    public boolean isWork() {
        return heat.getEnergy() > 500;
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSteamBoilerEntity.steam_boiler_heater;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_boiler.getBlock(getTeBlock());
    }

}
