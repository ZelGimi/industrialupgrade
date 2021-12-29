package com.denfop.integration.exnihilo;

import com.denfop.Constants;
import com.denfop.IUItem;
import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModBlocks;
import exnihilocreatio.blocks.BlockSieve;
import exnihilocreatio.modules.IExNihiloCreatioModule;
import exnihilocreatio.recipes.defaults.IRecipeDefaults;
import exnihilocreatio.registries.registries.HammerRegistry;
import exnihilocreatio.registries.registries.SieveRegistry;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class ExNihiloIntegration implements IExNihiloCreatioModule, IRecipeDefaults {
    public static Block gravel;
    public static Block dust;
    public static Block sand;
    public static Item gravel_crushed;
    public static Item dust_crushed;
    public static Item sand_crushed;

    public static List<String> itemNames() {
        List<String> list = new ArrayList<>();
        list.add("Mikhail");//0
        list.add("Aluminium");//1
        list.add("Vanady");//2
        list.add("Tungsten");//3
        list.add("Cobalt");//6
        list.add("Magnesium");//7
        list.add("Nickel");//8
        list.add("Platinum");//9
        list.add("Titanium");//10
        list.add("Chromium");//11
        list.add("Spinel");//12
        list.add("Silver");//14
        list.add("Zinc");//15
        list.add("Manganese");//16
        return list;
    }
    public static void registerOre(HammerRegistry registry,Block ore, int oreMeta, Item reward, int rewardMeta) {
        if (ore != null && reward != null) {
            registry.register(new BlockInfo(ore, oreMeta), new ItemStack(reward,1, rewardMeta),2, 1.0F, 0.0F);
            registry.register(new BlockInfo(ore, oreMeta), new ItemStack(reward,1, rewardMeta),2,  1.0F, 0.0F);
            registry.register(new BlockInfo(ore, oreMeta), new ItemStack(reward,1, rewardMeta),2,  1.0F, 0.0F);
            registry.register(new BlockInfo(ore, oreMeta), new ItemStack(reward,1, rewardMeta),2,  1.0F, 0.0F);
            registry.register(new BlockInfo(ore, oreMeta), new ItemStack(reward,1, rewardMeta),2,  0.5F, 0.1F);
            registry.register(new BlockInfo(ore, oreMeta),new ItemStack(reward,1, rewardMeta),2,  0.05F, 0.1F);
            registry.register(new BlockInfo(ore, oreMeta), new ItemStack(reward,1, rewardMeta),2,  0.0F, 0.05F);
        }

    }
    public void registerHammer(HammerRegistry registry) {
        for (int i = 0; i < IUItem.name_mineral1.size(); i++) {
            if (i != 6 && i != 7 && i != 11) {
                registerOre(registry,ExNihiloIntegration.gravel, i, ExNihiloIntegration.sand_crushed, i);
                registerOre(registry,ExNihiloIntegration.sand, i, ExNihiloIntegration.dust_crushed, i);
            }
        }
    }
        public void registerSieve(SieveRegistry registry) {
        for (int i = 0; i < IUItem.name_mineral1.size(); i++) {
            if (i != 6 && i != 7 && i != 11) {
                registry.register(new BlockInfo(Blocks.GRAVEL), new ItemInfo(ExNihiloIntegration.gravel_crushed, i), 0.1F,
                        BlockSieve.MeshType.IRON.getID());
                registry.register(new BlockInfo(Blocks.SAND),   new ItemInfo(ExNihiloIntegration.sand_crushed, i), 0.1F,
                        BlockSieve.MeshType.IRON.getID());
                registry.register(new BlockInfo(ModBlocks.dust),   new ItemInfo(ExNihiloIntegration.dust_crushed, i), 0.1F,
                        BlockSieve.MeshType.IRON.getID());
            }
        }
        registry.register(new BlockInfo(Blocks.SAND),   new ItemInfo(IUItem.toriy, 0), 0.15F, BlockSieve.MeshType.IRON.getID());

        registry.register(new BlockInfo(Blocks.SAND),   new ItemInfo(IUItem.radiationresources, 0), 0.05F,
                BlockSieve.MeshType.IRON.getID());
        registry.register(new BlockInfo(Blocks.SAND),   new ItemInfo(IUItem.radiationresources, 1), 0.02F,
                BlockSieve.MeshType.IRON.getID());
        registry.register(new BlockInfo(Blocks.SAND),   new ItemInfo(IUItem.radiationresources, 2), 0.01F,
                BlockSieve.MeshType.IRON.getID());
        registry.register(new BlockInfo(Blocks.SAND),   new ItemInfo(IUItem.preciousgem, 0), 0.1F,
                BlockSieve.MeshType.IRON.getID());
        registry.register(new BlockInfo(Blocks.SAND),   new ItemInfo(IUItem.preciousgem, 1), 0.1F,
                BlockSieve.MeshType.IRON.getID());
        registry.register(new BlockInfo(Blocks.SAND),  new ItemInfo(IUItem.preciousgem, 2), 0.1F,
                BlockSieve.MeshType.IRON.getID());

    }
    public static void init() {

        ExNihiloCreatio.loadedModules.add(new ExNihiloIntegration());



    }

    public static void oredictionary() {
        List<String> list = itemNames();
        for (int i = 0; i < list.size(); i++) {
            OreDictionary.registerOre("ore" + list.get(i), new ItemStack(Item.getItemFromBlock((gravel)), 1, i));
            OreDictionary.registerOre("ore" + list.get(i), new ItemStack(Item.getItemFromBlock((dust)), 1, i));
            OreDictionary.registerOre("ore" + list.get(i), new ItemStack(Item.getItemFromBlock((sand)), 1, i));

        }
    }

    @Override
    public String getMODID() {
        this.getClass();
        return Constants.MOD_ID;
    }

}
