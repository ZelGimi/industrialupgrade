package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.pollution.component.ChunkLevel;
import com.denfop.api.pollution.component.LevelPollution;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.Localization;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemPollutionDevice extends Item implements IItemTab {
    private String nameItem;

    public ItemPollutionDevice() {
        super(new Properties().stacksTo(1).setNoRepair());
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_339594_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(Localization.translate("iu.pollution_scanner.info")));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            ChunkPos playerChunk = new ChunkPos(player.blockPosition());

            ChunkLevel airChunkLevel = PollutionManager.pollutionManager.getChunkLevelAir(playerChunk);
            ChunkLevel soilChunkLevel = PollutionManager.pollutionManager.getChunkLevelSoil(playerChunk);

            if (airChunkLevel != null) {
                sendPollutionMessage(player, airChunkLevel.getLevelPollution(), "message.pollution.air");
            }

            if (soilChunkLevel != null) {
                sendPollutionMessage(player, soilChunkLevel.getLevelPollution(), "message.pollution.soil");
            }

            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
        }
        return super.use(world, player, hand);
    }

    private void sendPollutionMessage(Player player, LevelPollution level, String messageKey) {
        switch (level) {
            case LOW:
            case VERY_LOW:
            case MEDIUM:
            case HIGH:
            case VERY_HIGH:
                IUCore.proxy.messagePlayer(player, Localization.translate(messageKey + "." + level.name().toLowerCase()));
                break;
        }
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem;
    }

}
