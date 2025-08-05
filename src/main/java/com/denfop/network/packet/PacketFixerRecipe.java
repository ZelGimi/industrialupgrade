package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.*;
import com.denfop.api.space.SpaceInit;
import com.denfop.api.space.upgrades.BaseSpaceUpgradeSystem;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipes.*;
import com.denfop.tiles.mechanism.TileEntityPalletGenerator;
import com.denfop.tiles.mechanism.TileSolidCooling;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TagsUpdatedEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketFixerRecipe implements IPacket {
    public PacketFixerRecipe() {

    }

    public PacketFixerRecipe(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        IUCore.network.getServer().sendPacket(buffer, player);
    }

    @Override
    public byte getId() {
        return 56;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final Player entityPlayer) {
        if (! IUCore.register) {
            IUCore.register = true;
            Iterable<Holder<Item>> tagOres = BuiltInRegistries.ITEM.getTagOrEmpty(ItemTags.create(new ResourceLocation("forge", "ores")));
            SpaceInit.jsonInit();
            new ScrapboxRecipeManager();
            for (Holder<Item> holder : tagOres) {
                IUCore.get_ore.add(new ItemStack(holder));
                Item item = holder.value();

                List<TagKey<Item>> list1 = item.builtInRegistryHolder().tags().toList();
                for (TagKey<Item> tagKey : list1) {
                    ResourceLocation resourceLocation = tagKey.location();
                    if (resourceLocation.getNamespace().equals("forge") && resourceLocation.getPath().startsWith("ores/")) {
                        String name = resourceLocation.getPath();
                        StringBuilder pathBuilder = new StringBuilder(name);
                        String targetString = "ores/";
                        String replacement = "";
                        if (replacement != null) {
                            int index = pathBuilder.indexOf(targetString);
                            while (index != -1) {
                                pathBuilder.replace(index, index + targetString.length(), replacement);
                                index = pathBuilder.indexOf(targetString, index + replacement.length());
                            }
                        }
                        name = pathBuilder.toString();
                        if ( IUCore.stringList.contains(name))
                            continue;
                        TagKey<Item> tag = ItemTags.create(new ResourceLocation("forge", "gems/" + name));
                        List<Holder<Item>> gemList = new ArrayList<>();
                        BuiltInRegistries.ITEM.getTagOrEmpty(tag).forEach(gemList::add);
                        TagKey<Item> tag1 = ItemTags.create(new ResourceLocation("forge", "raw_materials/" + name));
                        List<Holder<Item>> rawList = new ArrayList<>();
                        BuiltInRegistries.ITEM.getTagOrEmpty(tag1).forEach(rawList::add);
                        if (!gemList.isEmpty()) {
                            if (! IUCore.stringList.contains(name)) {
                                IUCore.list.add(new ItemStack(gemList.get(0).get()));
                                IUCore.stringList.add(name);
                                break;
                            }

                        } else {
                            if (!rawList.isEmpty()) {
                                if (! IUCore.stringList.contains(name)) {
                                    IUCore.list.add(new ItemStack(rawList.get(0).get()));
                                    IUCore.stringList.add(name);
                                    break;
                                }
                            } else if (! IUCore.stringList.contains(name)) {
                                IUCore.list.add(new ItemStack(item));
                                IUCore.stringList.add(name);
                                break;
                            }
                        }
                    }
                }
            }


        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
