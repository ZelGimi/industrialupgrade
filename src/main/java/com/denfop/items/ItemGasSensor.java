package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.blocks.FluidName;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.Localization;
import com.denfop.world.GenData;
import com.denfop.world.WorldGenGas;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemGasSensor extends Item implements IItemTab {
    private String nameItem;

    public ItemGasSensor() {
        super(new Item.Properties().stacksTo(1).setNoRepair());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.literal(Localization.translate("iu.gas_sensor.info")));
        pTooltipComponents.add(Component.literal(Localization.translate("iu.gas_sensor.info1")));
        pTooltipComponents.add(Component.literal(Localization.translate("iu.gas_sensor.info2")));
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
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

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
        }

        ChunkPos chunkPos = new ChunkPos((int) player.getX() >> 4, (int) player.getZ() >> 4);
        boolean empty = true;

        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                final ChunkPos chunkPos1 = new ChunkPos(chunkPos.x + i, chunkPos.z + j);
                GenData typeGas = WorldGenGas.gasMap.get(chunkPos1);

                if (typeGas != null) {
                    empty = false;
                    Component text = Component.literal("");

                    switch (typeGas.getTypeGas()) {
                        case GAS:
                            text = Component.translatable(FluidName.fluidgas.getInstance().get().getFluidType().getDescriptionId());
                            break;
                        case IODINE:
                            text = Component.translatable(FluidName.fluidiodine.getInstance().get().getFluidType().getDescriptionId());
                            break;
                        case BROMIDE:
                            text = Component.translatable(FluidName.fluidbromine.getInstance().get().getFluidType().getDescriptionId());
                            break;
                        case CHLORINE:
                            text = Component.translatable(FluidName.fluidchlorum.getInstance().get().getFluidType().getDescriptionId());
                            break;
                        case FLUORINE:
                            text = Component.translatable(FluidName.fluidfluor.getInstance().get().getFluidType().getDescriptionId());
                            break;
                    }

                    if (typeGas.getX() == 0 && typeGas.getZ() == 0) {
                        IUCore.proxy.messagePlayer(
                                player,
                                Component.literal(
                                        "X: " + (chunkPos1.getMinBlockX() + 16) +
                                                ", Y: " + typeGas.getY() +
                                                ", Z: " + (chunkPos1.getMinBlockZ() + 16) +
                                                " " + text.getString()
                                ).getString()
                        );
                    } else {
                        IUCore.proxy.messagePlayer(
                                player,
                                Component.literal(
                                        "X: " + typeGas.getX() +
                                                ", Y: " + typeGas.getY() +
                                                ", Z: " + typeGas.getZ() +
                                                " --> " + text.getString()
                                ).getString()
                        );
                    }
                }
            }
        }

        if (empty) {
            IUCore.proxy.messagePlayer(
                    player,
                    Component.translatable("iu.empty").getString()
            );
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

}
