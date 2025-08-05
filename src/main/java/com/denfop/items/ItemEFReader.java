package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.gui.GUIEFReader;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStackEvent;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ItemEFReader extends Item implements IItemStackInventory, IUpdatableItemStackEvent, IItemTab {
    private String nameItem;

    public ItemEFReader() {
        super(new Properties().stacksTo(1).setNoRepair());
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
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
    @OnlyIn(Dist.CLIENT)
    public void updateField(final String name, final CustomPacketBuffer buffer, final ItemStack stack) {
        if (Minecraft.getInstance().screen instanceof GUIEFReader) {
            GUIEFReader guiefReader = (GUIEFReader) Minecraft.getInstance().screen;
            guiefReader.readField(name, buffer);
        }
    }

    @Override
    public void updateEvent(final int event, final ItemStack stack) {

    }

    @Override
    public IAdvInventory getInventory(final Player var1, final ItemStack var2) {
        return new EFReaderInventory(var1, var2);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        InteractionHand hand = context.getHand();
        if (!world.isClientSide) {

            if (EnergyNetGlobal.instance.getTile(
                    world,
                    pos
            ) != EnergyNetGlobal.EMPTY) {
                player.getItemInHand(context.getHand()).set(DataComponentsInit.TELEPORT, pos);
                CustomPacketBuffer growingBuffer = new CustomPacketBuffer(player.registryAccess());

                growingBuffer.writeByte(1);

                growingBuffer.flip();
                player.openMenu(getInventory(player, player.getItemInHand(hand)), buf -> buf.writeBytes(growingBuffer));


                return InteractionResult.SUCCESS;
            } else {
                IUCore.proxy.messagePlayer(player, "This block isn`t energyTile");
                return InteractionResult.PASS;
            }
        }
        return InteractionResult.PASS;
    }
}
