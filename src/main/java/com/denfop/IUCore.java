package com.denfop;

import com.denfop.api.Recipes;
import com.denfop.api.cool.CoolNet;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.heat.HeatNet;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.RecipeInputStack;
import com.denfop.api.recipe.RecipesCore;
import com.denfop.api.sytem.EnergyBase;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.transport.TransportNetGlobal;
import com.denfop.api.upgrade.BaseUpgradeSystem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.vein.VeinSystem;
import com.denfop.api.windsystem.WindSystem;
import com.denfop.api.windsystem.upgrade.RotorUpgradeSystem;
import com.denfop.audio.Sounds;
import com.denfop.blocks.BlockIUFluid;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.cool.CoolNetGlobal;
import com.denfop.events.TickHandlerIU;
import com.denfop.heat.HeatNetGlobal;
import com.denfop.items.energy.EntityAdvArrow;
import com.denfop.items.energy.ItemNanoSaber;
import com.denfop.items.energy.ItemQuantumSaber;
import com.denfop.items.energy.ItemSpectralSaber;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.upgradekit.ItemUpgradeMachinesKit;
import com.denfop.network.NetworkManager;
import com.denfop.network.Sides;
import com.denfop.proxy.CommonProxy;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.render.streak.PlayerStreakInfo;
import com.denfop.tabs.TabCore;
import com.denfop.tiles.mechanism.quarry.QuarryItem;
import com.denfop.utils.KeyboardIU;
import com.denfop.utils.Keys;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

@SuppressWarnings({"ALL", "UnnecessaryFullyQualifiedName"})
@Mod.EventBusSubscriber
@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, dependencies = Constants.MOD_DEPS, version = Constants.MOD_VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]")
public final class IUCore {

    public static final CreativeTabs IUTab = new TabCore(0, "IUTab");
    public static final CreativeTabs ModuleTab = new TabCore(1, "ModuleTab");
    public static final CreativeTabs ItemTab = new TabCore(2, "ItemTab");
    public static final CreativeTabs OreTab = new TabCore(3, "OreTab");
    public static final CreativeTabs EnergyTab = new TabCore(4, "EnergyTab");
    public static final CreativeTabs RecourseTab = new TabCore(5, "ResourceTab");
    public static final CreativeTabs ReactorsTab = new TabCore(6, "ReactorsTab");
    public static final CreativeTabs UpgradeTab = new TabCore(7, "UpgradeTab");
    public static final CreativeTabs BlueprintTab = new TabCore(8, "BlueprintTab");
    public static final CreativeTabs ElementsTab = new TabCore(9, "CraftingElementsTab");

    public static final List<IMultiTileBlock> list_teBlocks = new ArrayList<>();
    public static final Map<Integer, EnumModule> modules = new HashMap<>();
    public static final List<ItemStack> list = new ArrayList<>();
    public static final List<ItemStack> get_ore = new ArrayList<>();
    public static final List<ItemStack> get_ingot = new ArrayList<>();
    public static final List<ItemStack> get_crushed = new ArrayList<>();
    public static final List<ItemStack> get_comb_crushed = new ArrayList<>();
    public static final List<RecipeInputStack> get_all_list = new ArrayList<>();
    public static final List<ItemStack> fish_rodding = new ArrayList<>();
    public static final List<ItemStack> list_adding = new ArrayList<>();
    public static final List<ItemStack> list_removing = new ArrayList<>();
    public static final List<ItemStack> list_furnace_adding = new ArrayList<>();
    public static final List<ItemStack> list_furnace_removing = new ArrayList<>();

    public static final List<ItemStack> list_crushed_adding = new ArrayList<>();
    public static final List<ItemStack> list_crushed_removing = new ArrayList<>();

    public static final List<ItemStack> list_comb_crushed_adding = new ArrayList<>();
    public static final List<ItemStack> list_comb_crushed_removing = new ArrayList<>();
    public static final List<QuarryItem> list_quarry = new ArrayList<>();

    public static final List<QuarryItem> get_ingot_quarry = new ArrayList<>();
    public static final List<QuarryItem> get_crushed_quarry = new ArrayList<>();
    public static final List<QuarryItem> get_comb_crushed_quarry = new ArrayList<>();
    public static final List<List<ItemStack>> removing_list = new ArrayList<>();
    public static final Map<String, PlayerStreakInfo> mapStreakInfo = new HashMap<>();

    public static boolean dynamicTrees = false;
    public static Logger log;
    @SidedProxy(clientSide = "com.denfop.proxy.ClientProxy", serverSide = "com.denfop.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance("industrialupgrade")
    public static IUCore instance;
    @SidedProxy(
            clientSide = "com.denfop.utils.KeyboardClient",
            serverSide = "com.denfop.utils.KeyboardIU"
    )
    public static KeyboardIU keyboard;
    public static Sides<NetworkManager> network;
    public static Map<String, LootTable> lootTables = new HashMap<>();
    public static Random random;

    static {
        FluidRegistry.enableUniversalBucket();
        random = new Random();
        IUCore.instance = new IUCore();
        Keys.instance = IUCore.keyboard;
        IUCore.network = new Sides<>("com.denfop.network.NetworkManager", "com.denfop.network.NetworkManagerClient");

    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> evt) {
        evt.getRegistry().register(EntityEntryBuilder.create().entity(EntityAdvArrow.class).id(new ResourceLocation(
                Constants.MOD_ID,
                "adv_arrow"
        ), 8).name("AdvArrow").tracker(256, 10, true).build());
    }

    public static boolean isSimulating() {
        return !FMLCommonHandler.instance().getEffectiveSide().isClient();
    }


    public static ResourceLocation getIdentifier(final String name) {
        return new ResourceLocation(Constants.MOD_ID, name);
    }

    public static void initENet() {


        HeatNet.instance = HeatNetGlobal.initialize();
        CoolNet.instance = CoolNetGlobal.initialize();
        EnergyBase.init();
        TransportNetGlobal.initialize();
        new VeinSystem();
        new RadiationSystem();
        new WindSystem();

    }

    public static boolean isHasVersion(String modid, String version) {
        final Map<String, ModContainer> map = Loader.instance().getIndexedModList();
        ModContainer container = map.get(modid);
        if (container != null) {
            String version_this = container.getVersion();
            final StringBuilder string_builder = new StringBuilder(version_this);
            version_this = version_this.replace("2.8.", "").replace("-ex112", "");
            if (Integer.parseInt(version_this) >= Integer.parseInt(version)) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        if (proxy.isRendering()) {
            Sounds.registerSounds(event.getRegistry());
        }

    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (IUCore.proxy.isSimulating()) {
            keyboard.removePlayerReferences(event.player);
        }

    }

    @Mod.EventHandler
    public void load(final FMLPreInitializationEvent event) {
        if (Loader.isModLoaded("ic2")) {
            throw new RuntimeException("Uninstall Industrial Craft 2. Industrial Upgrade can't run through the big problem in " +
                    "that mod. Industrial Upgrade is much better than the old version of Industrial Craft 2");
        }
        MinecraftForge.EVENT_BUS.register(this);
        ModUtils.log = event.getModLog();
        IUCore.log = event.getModLog();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        new TileBlockCreator();
        Config.loadNormalConfig(event.getSuggestedConfigurationFile());
        EnergyNetGlobal.initialize();
        UpgradeSystem.system = new BaseUpgradeSystem();
        new RotorUpgradeSystem();
        new com.denfop.api.water.upgrade.RotorUpgradeSystem();
        Recipes.recipes = new RecipesCore();
        MinecraftForge.EVENT_BUS.register(new TickHandlerIU());


        Keys.instance = IUCore.keyboard;
        IUPotion.radiation = new IUPotion("radiation", true, 5149489, new ItemStack[0]);
        proxy.preInit(event);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);


    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        proxy.init(event);
        proxy.registerRecipe();
        initENet();


    }

    @SubscribeEvent
    public void getore(OreDictionary.OreRegisterEvent event) {
        String oreClass = event.getName();
        if (oreClass.startsWith("ore")) {
            if (get_ore == null) {

                assert false;
                get_ore.addAll(OreDictionary.getOres(oreClass));
            } else if (!get_ore.contains(OreDictionary.getOres(oreClass).get(0))) {
                get_ore.addAll(OreDictionary.getOres(oreClass));

            }

        }
        if (oreClass.startsWith("ore")) {
            String temp = oreClass.substring(3);

            if (OreDictionary.getOres("gem" + temp) == null || OreDictionary.getOres("gem" + temp).size() < 1) {
                if (!list.contains(OreDictionary.getOres(oreClass).get(0))) {
                    list.add(OreDictionary.getOres(oreClass).get(0));
                }

            } else {
                if (!list.contains(OreDictionary.getOres("gem" + temp).get(0))) {
                    list.add(OreDictionary.getOres("gem" + temp).get(0));
                }
            }

        }

        if (oreClass.startsWith("gem")) {
            String temp = oreClass.substring(3);
            String orename = "ore" + temp;

            list.removeAll(OreDictionary.getOres(orename));
            if (!list.contains(OreDictionary.getOres(oreClass).get(0))) {
                list.add(OreDictionary.getOres(oreClass).get(0));
            }


        }
    }

    void addInList1(ItemStack stack) {
        boolean add = true;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isItemEqual(stack)) {
                add = false;
                break;
            }
        }
        if (add) {
            list.add(stack);
        }
    }

    @SubscribeEvent
    public void load_table(final LootTableLoadEvent event) {
        if (event.getName().toString().contains("entities")) {
            StringBuilder builder = new StringBuilder(event.getName().toString());
            int index = builder.indexOf("/");
            int index1 = builder.indexOf(":");
            builder.delete(index1 + 1, index + 1);
            if (builder.toString().contains("sheep")) {
                index = builder.indexOf("/");
                if (index >= 0) {
                    builder.replace(index, index + 1, "_");
                }
            }

            lootTables.put(builder.toString(), event.getTable());
            if (builder.toString().equals("minecraft:iron_golem")) {
                lootTables.put("minecraft:villager_golem", event.getTable());
            }
        }

    }

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        proxy.postInit(event);
        ((RecipesCore) Recipes.recipes).setCanAdd(false);
        addInList1(new ItemStack(Items.DIAMOND));
        addInList1(new ItemStack(Items.EMERALD));
        addInList1(new ItemStack(Items.REDSTONE));
        addInList1(new ItemStack(Items.DYE, 1, 4));
        addInList1(new ItemStack(Items.COAL));
        addInList1(new ItemStack(Items.GLOWSTONE_DUST));
        addInList1(new ItemStack(Items.QUARTZ));
        dynamicTrees = Loader.isModLoaded("dynamictrees");
        addOre("oreCoal");
        addOre("oreIron");
        addOre("oreGold");
        addOre("oreDiamond");
        addOre("oreRedstone");
        addOre("oreEmerald");
        addOre1("oreIron");
        addOre1("oreGold");
        addOre1("oreIridium");
        addOre1("crystalCertusQuartz");
        addOre("oreIridium");
        addOre("oreTin");
        addOre1("oreTin");
        addOre("oreCopper");
        addOre1("oreCopper");
        addOre("oreLead");
        addOre1("oreLead");
        get_ore.add(new ItemStack(Blocks.REDSTONE_ORE));
        get_ore.add(new ItemStack(Blocks.LIT_REDSTONE_ORE));
        removeOre("oreEndLapis");
        removeOre("oreEndEmerald");
        removeOre("oreEndDiamond");
        removeOre("oreEndGold");
        removeOre("oreEndIron");
        removeOre("oreNetherGold");
        removeOre("oreNetherIron");
        removeOre("oreNetherRedstone");
        removeOre("oreEndRedstone");
        removeOre("oreNetherCoal");
        removeOre("oreNetherLapis");
        removeOre("oreNetherEmerald");
        removeOre("oreNetherDiamond");
        removeOre("oreEndCoal");
        removeOre("gemFluix");
        removeOre("oreKasai");
        removeOre("oreDimensionalShard");
        removeOre("gemFluix");
        removeOre("oreCrystalAir");
        removeOre("oreCrystalEarth");
        removeOre("oreCrystalFire");
        removeOre("oreCrystalNether");
        removeOre("oreCrystalWater");
        removeOre("oreCrystalOrder");
        removeOre("oreCrystalEntropy");
        removeOre("oreCrystalTaint");
        removeOre("gemIridium");
        removeOre("gemAmericium");
        removeOre("gemNeptunium");
        removeOre("gemCurium");
        removeOre("gemThorium");
        for (String name : RegisterOreDictionary.list_heavyore) {
            removeOre("ore" + name);
        }
        removeOre("oreDraconium");
        removeOre("oreClathrateOilShale");
        removeOre("oreClathrateOilSand");
        removeOre("oreClathrateOilSand");
        for (ItemStack stack : IUCore.list) {
            BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput("macerator", false, stack);
            if (recipe != null) {
                this.get_crushed.add(recipe.getOutput().items.get(0));
            } else {
                this.get_crushed.add(stack);
            }
        }
        this.get_comb_crushed.clear();
        for (ItemStack stack : IUCore.list) {
            BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput("comb_macerator", false, stack);
            if (recipe != null) {
                this.get_comb_crushed.add(recipe.getOutput().items.get(0));
            } else {
                this.get_comb_crushed.add(stack);
            }
        }
        get_ingot.clear();
        for (ItemStack stack : IUCore.list) {
            BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput("furnace", false, stack);
            if (recipe != null) {
                this.get_ingot.add(recipe.getOutput().items.get(0));
            } else {
                this.get_ingot.add(stack);
            }
        }

        IUCore.list_adding.forEach(stack -> addOre1(stack));
        IUCore.list_removing.forEach(stack -> removeOre(stack));
        IUCore.list.forEach(stack -> {

            get_all_list.add(new RecipeInputStack(stack));
        });
        get_all_list.removeIf(stack -> IUCore.get_ingot.contains(stack));
        IUCore.list_furnace_adding.forEach(stack -> addOre2(stack));
        IUCore.list_furnace_removing.forEach(stack -> removeOre2(stack));
        IUCore.get_ingot.forEach(stack -> {

            get_all_list.add(new RecipeInputStack(stack));
        });

        get_all_list.removeIf(stack -> IUCore.get_comb_crushed.contains(stack));
        IUCore.list_comb_crushed_adding.forEach(stack -> addOre4(stack));
        IUCore.list_comb_crushed_removing.forEach(stack -> removeOre4(stack));
        IUCore.get_comb_crushed.forEach(stack -> {

            get_all_list.add(new RecipeInputStack(stack));
        });
        get_all_list.removeIf(stack -> IUCore.get_crushed.contains(stack));
        IUCore.list_crushed_adding.forEach(stack -> addOre3(stack));
        IUCore.list_crushed_removing.forEach(stack -> removeOre3(stack));
        IUCore.get_crushed.forEach(stack -> {

            get_all_list.add(new RecipeInputStack(stack));
        });

        IUCore.get_crushed.forEach(stack -> {
            if (!stack.isEmpty()) {
                this.
                        get_crushed_quarry.add(new QuarryItem(stack));
            }
        });
        IUCore.list.forEach(stack -> {
            if (!stack.isEmpty()) {
                this.
                        list_quarry.add(new QuarryItem(stack));
            }
        });
        IUCore.get_ingot.forEach(stack -> {
            if (!stack.isEmpty()) {
                this.
                        get_ingot_quarry.add(new QuarryItem(stack));
            }
        });
        IUCore.get_comb_crushed.forEach(stack -> {
            if (!stack.isEmpty()) {
                this.
                        get_comb_crushed_quarry.add(new QuarryItem(stack));
            }
        });
        fish_rodding.add(new ItemStack(Items.FISH));
        fish_rodding.add(new ItemStack(Items.FISH, 1, 1));
        fish_rodding.add(new ItemStack(Items.FISH, 1, 2));
        fish_rodding.add(new ItemStack(Items.FISH, 1, 3));
        fish_rodding.add(new ItemStack(Items.BONE));
        fish_rodding.add(new ItemStack(Items.ENCHANTED_BOOK));
        fish_rodding.add(new ItemStack(Items.POTIONITEM));
        fish_rodding.add(new ItemStack(Items.LEATHER_BOOTS));
        fish_rodding.add(new ItemStack(Items.BOW));
        fish_rodding.add(new ItemStack(Items.SADDLE));
        fish_rodding.add(new ItemStack(Items.FISHING_ROD));
        fish_rodding.add(new ItemStack(Blocks.WATERLILY));
        fish_rodding.add(new ItemStack(Blocks.TRIPWIRE_HOOK));
        fish_rodding.add(new ItemStack(Items.NAME_TAG));
        fish_rodding.add(new ItemStack(Items.STICK));
        fish_rodding.add(new ItemStack(Items.BOWL));
        fish_rodding.add(new ItemStack(Items.ROTTEN_FLESH));
        fish_rodding.add(new ItemStack(Items.STRING));
        fish_rodding.add(new ItemStack(Items.LEATHER));
        fish_rodding.add(new ItemStack(Items.DYE));
    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onViewRenderFogDensity(EntityViewRenderEvent.FogDensity event) {
        if (event.getState().getBlock() instanceof BlockIUFluid) {
            event.setCanceled(true);
            Fluid fluid = ((BlockIUFluid) event.getState().getBlock()).getFluid();

            event.setDensity((float) ((float) Math.abs(fluid.getDensity()) / 20000D * 2.0D));
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ++ItemQuantumSaber.ticker;
            ++ItemSpectralSaber.ticker;
            ++ItemNanoSaber.ticker;
        }

    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ++ItemUpgradeMachinesKit.tick;
            if (ItemUpgradeMachinesKit.tick % 40 == 0) {
                for (int i = 0; i < ItemUpgradeMachinesKit.inform.length; i++) {
                    final List<ItemStack> list1 = IUItem.map_upgrades.get(i);
                    ItemUpgradeMachinesKit.inform[i] = ++ItemUpgradeMachinesKit.inform[i] % list1.size();
                }
            }
            ++ListInformationUtils.tick;
            if (ListInformationUtils.tick % 40 == 0) {
                ListInformationUtils.index = (ListInformationUtils.index + 1) % ListInformationUtils.mechanism_info.size();
                ListInformationUtils.index1 = (ListInformationUtils.index1 + 1) % ListInformationUtils.mechanism_info1.size();
                ListInformationUtils.index2 = (ListInformationUtils.index2 + 1) % ListInformationUtils.mechanism_info2.size();

            }
        }

    }

    @Mod.EventHandler
    public void init(final FMLFingerprintViolationEvent event) {
        java.util.logging.Logger.getLogger(this.getClass().getName()).log(
                Level.SEVERE,
                "Invalid fingerprint detected! The file " + event.getSource().getName() +
                        " may have been tampered with. This version will NOT be supported by the author!"
        );
    }

    public void addOre(String name) {
        if (OreDictionary.getOres(name).size() >= 1) {
            if (!get_ore.contains(OreDictionary.getOres(name).get(0))) {
                get_ore.add(OreDictionary.getOres(name).get(0));

            }
        }
    }

    public void addOre1(String name) {
        if (OreDictionary.getOres(name).size() >= 1) {
            if (!list.contains(OreDictionary.getOres(name).get(0))) {
                list.add(OreDictionary.getOres(name).get(0));

            }
        }
    }

    public void addOre1(ItemStack name) {
        list.add(name);
    }

    public void removeOre(ItemStack name) {
        list.removeIf(stack -> stack.isItemEqual(name));
    }

    public void addOre2(ItemStack name) {
        get_ingot.add(name);
    }

    public void addOre3(ItemStack name) {
        get_crushed.add(name);
    }

    public void addOre4(ItemStack name) {
        get_comb_crushed.add(name);
    }

    public void removeOre2(ItemStack name) {
        get_ingot.removeIf(stack -> stack.isItemEqual(name));
    }

    public void removeOre3(ItemStack name) {
        get_crushed.removeIf(stack -> stack.isItemEqual(name));
    }

    public void removeOre4(ItemStack name) {
        get_comb_crushed.removeIf(stack -> stack.isItemEqual(name));
    }

    public void removeOre(String name) {
        if (OreDictionary.getOres(name).size() >= 1) {
            if (list.contains(OreDictionary.getOres(name).get(0))) {
                list.remove(OreDictionary.getOres(name).get(0));

            }
        }
    }

}
