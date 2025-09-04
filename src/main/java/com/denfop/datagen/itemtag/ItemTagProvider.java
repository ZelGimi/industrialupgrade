package com.denfop.datagen.itemtag;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemTagProvider extends ItemTagsProvider {
    public static List<IItemTag> list = new ArrayList<>();
    public static Map<ResourceLocation, List<ItemStack>> mapItems = new HashMap<>();

    public ItemTagProvider(DataGenerator gen, BlockTagsProvider blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(gen, blockTags, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for (IItemTag iItemTag : list) {
            Item item = iItemTag.getItem();
            String[] stringTags = iItemTag.getTags();
            for (String stringTag : stringTags) {
                TagKey<Item> tagKey = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(stringTag));
                this.tag(tagKey).add(item);
            }
        }
        addIngot("Lithium", IUItem.crafting_elements.getStack(447));
        addGem("Bor", IUItem.crafting_elements.getStack(448));
        addGem("Beryllium", IUItem.crafting_elements.getStack(449));
        addIngot("Uranium", IUItem.itemiu.getStack(2));
        addPlate("Lithium", IUItem.crafting_elements.getStack(450));
        addPlate("Bor", IUItem.crafting_elements.getStack(451));
        addPlate("Beryllium", IUItem.crafting_elements.getStack(452));
        addPlateDense("Beryllium", IUItem.crafting_elements.getStack(452));
        addCustom("machineBlock", IUItem.machine.getItem());
        addCustom("machineBlockCasing", IUItem.machine.getItem());
        addCustom("machineBlockAdvanced", IUItem.advancedMachine.getItem());
        addCustom("machineBlockAdvancedCasing", IUItem.advancedMachine.getItem());
        addPlate("AdvancedAlloy", IUItem.advancedAlloy.getItem());
        addPlate("Carbon", IUItem.carbonPlate.getItem());
        addCustom("circuitBasic", IUItem.electronicCircuit.getItem());
        addCustom("circuitAdvanced", IUItem.advancedCircuit.getItem());
        addCustom("dusts/redstone", Items.REDSTONE);
        addPlateDense("Steel", IUItem.denseplateadviron.getItem());
        addPlateDense("Iron", IUItem.denseplateiron.getItem());
        addPlateDense("Gold", IUItem.denseplategold.getItem());
        addPlateDense("Copper", IUItem.denseplatecopper.getItem());
        addPlateDense("Tin", IUItem.denseplatetin.getItem());
        addPlateDense("Lead", IUItem.denseplatelead.getItem());
        addPlateDense("Lapis", IUItem.denseplatelapi.getItem());
        addPlateDense("Obsidian", IUItem.denseplateobsidian.getItem());
        addPlateDense("Bronze", IUItem.denseplatebronze.getItem());
        addGem("Americium", IUItem.radiationresources.getStack(0));
        addGem("Neptunium", IUItem.radiationresources.getStack(1));
        addGem("Curium", IUItem.radiationresources.getStack(2));
        addGem("Thorium", IUItem.toriy.getItem());
        addIngot("Uranium", IUItem.itemiu.getStack(2));
        this.tag(TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge:crystal/proton"))).add(IUItem.proton.getItem());
        this.tag(TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge:crystal/photon"))).add(IUItem.photoniy.getItem());
        this.tag(TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge:crystalingot/photon"))).add(IUItem.photoniy_ingot.getItem());
        this.tag(TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge:nuggets/neutron"))).add(IUItem.neutronium.getItem());
    }

    public void addCustom(String tag, Item item) {
        String[] stringTags = new String[]{"forge:" + tag};
        for (String stringTag : stringTags) {
            TagKey<Item> tagKey = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(stringTag.toLowerCase()));
            this.tag(tagKey).add(item);
        }
    }

    public void addPlate(String tag, Item item) {
        String[] stringTags = new String[]{"forge:plates/" + tag, "forge:plates"};
        for (String stringTag : stringTags) {
            TagKey<Item> tagKey = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(stringTag.toLowerCase()));
            this.tag(tagKey).add(item);
        }
    }

    public void addPlateDense(String tag, Item item) {
        String[] stringTags = new String[]{"forge:platedense/" + tag, "forge:platedense"};
        for (String stringTag : stringTags) {
            TagKey<Item> tagKey = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(stringTag.toLowerCase()));
            this.tag(tagKey).add(item);
        }
    }

    public void addIngot(String tag, Item item) {
        String[] stringTags = new String[]{"forge:ingots/" + tag, "forge:ingots"};
        for (String stringTag : stringTags) {
            TagKey<Item> tagKey = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(stringTag.toLowerCase()));
            this.tag(tagKey).add(item);
        }
    }

    public void addDust(String tag, Item item) {
        String[] stringTags = new String[]{"forge:dusts/" + tag, "forge:dusts"};
        for (String stringTag : stringTags) {
            TagKey<Item> tagKey = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(stringTag.toLowerCase()));
            this.tag(tagKey).add(item);
        }
    }

    public void addCasing(String tag, Item item) {
        String[] stringTags = new String[]{"forge:casings/" + tag, "forge:casings"};
        for (String stringTag : stringTags) {
            TagKey<Item> tagKey = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(stringTag.toLowerCase()));
            this.tag(tagKey).add(item);
        }
    }

    public void addGem(String tag, Item item) {
        String[] stringTags = new String[]{"forge:gems/" + tag, "forge:gems"};
        for (String stringTag : stringTags) {
            TagKey<Item> tagKey = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(stringTag.toLowerCase()));
            this.tag(tagKey).add(item);
        }
    }
}
