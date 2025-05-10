package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.componets.HeatComponent;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemsHeatSensor extends Item {
    private String nameItem;

    public ItemsHeatSensor() {
        super(new Item.Properties().tab(IUCore.EnergyTab).stacksTo(1).setNoRepair());
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", Registry.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "iu.heat_sensor";
        }

        return this.nameItem;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack,
                                            UseOnContext p_41427_) {
        Player player = p_41427_.getPlayer();
        Level world = p_41427_.getLevel();
        BlockPos pos = p_41427_.getClickedPos();
        InteractionHand hand = p_41427_.getHand();
        if (!world.isClientSide) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof TileEntityInventory) {
                TileEntityInventory tileEntityInventory = (TileEntityInventory) tileEntity;
                HeatComponent component = tileEntityInventory.getComp(HeatComponent.class);
                if (component == null) {
                    return InteractionResult.PASS;
                }
                IUCore.proxy.messagePlayer(
                        player,
                        Localization.translate("iu.temperature") + String.format(
                                "%.2f",
                                component.getEnergy()
                        ) + "°C" + "/" + component.getCapacity() + "°C"
                );
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

}
