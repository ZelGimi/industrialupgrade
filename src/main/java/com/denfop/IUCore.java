package com.denfop;

import com.denfop.api.Recipes;
import com.denfop.api.bee.BeeAI;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.crop.Crop;
import com.denfop.api.crop.CropInit;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.guidebook.GuideBookCore;
import com.denfop.api.item.upgrade.BaseUpgradeSystem;
import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.api.multiblock.MultiBlockSystem;
import com.denfop.api.otherenergies.common.EnergyBase;
import com.denfop.api.otherenergies.cool.CoolNet;
import com.denfop.api.otherenergies.cool.CoolNetGlobal;
import com.denfop.api.otherenergies.heat.HeatNet;
import com.denfop.api.otherenergies.heat.HeatNetGlobal;
import com.denfop.api.otherenergies.pressure.PressureNet;
import com.denfop.api.otherenergies.pressure.PressureNetGlobal;
import com.denfop.api.otherenergies.transport.TransportNetGlobal;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.pollution.radiation.RadiationSystem;
import com.denfop.api.recipe.*;
import com.denfop.api.solar.SolarEnergySystem;
import com.denfop.api.space.BaseSpaceSystem;
import com.denfop.api.space.SpaceInit;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.EventHandlerPlanet;
import com.denfop.api.space.upgrades.BaseSpaceUpgradeSystem;
import com.denfop.api.space.upgrades.SpaceUpgradeSystem;
import com.denfop.api.tesseract.TesseractSystem;
import com.denfop.api.vein.common.VeinSystem;
import com.denfop.api.vein.gas.GasVeinSystem;
import com.denfop.api.windsystem.WindSystem;
import com.denfop.api.windsystem.upgrade.RotorUpgradeSystem;
import com.denfop.blockentity.base.IManufacturerBlock;
import com.denfop.blockentity.bee.BlockEntityApiary;
import com.denfop.blockentity.mechanism.BlockEntityPalletGenerator;
import com.denfop.blockentity.mechanism.BlockEntitySolidCooling;
import com.denfop.blockentity.mechanism.EnumTypeMachines;
import com.denfop.blockentity.mechanism.quarry.QuarryItem;
import com.denfop.blockentity.panels.entity.EnumSolarPanels;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.config.ModConfig;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.datagen.itemtag.ItemTagProvider;
import com.denfop.entity.SmallBee;
import com.denfop.events.*;
import com.denfop.integration.one_probe.RegisterProvider;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.energy.EntityAdvArrow;
import com.denfop.items.energy.ItemNanoSaber;
import com.denfop.items.energy.ItemQuantumSaber;
import com.denfop.items.energy.ItemSpectralSaber;
import com.denfop.items.relocator.RelocatorNetwork;
import com.denfop.items.upgradekit.ItemUpgradePanelKit;
import com.denfop.network.NetworkManager;
import com.denfop.network.Sides;
import com.denfop.network.packet.PacketUpdateInformationAboutQuestsPlayer;
import com.denfop.network.packet.PacketUpdateRelocator;
import com.denfop.proxy.ClientProxy;
import com.denfop.proxy.CommonProxy;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipes.*;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.register.Register;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.render.streak.PlayerStreakInfo;
import com.denfop.tabs.TabCore;
import com.denfop.utils.*;
import com.denfop.villager.TradingSystem;
import com.denfop.world.WorldBaseGen;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

import java.util.*;

import static com.denfop.api.Recipes.inputFactory;
import static com.denfop.api.space.BaseSpaceSystem.fluidToLevel;
import static com.denfop.utils.ListInformationUtils.mechanism_info1;


@Mod(IUCore.MODID)
public class IUCore {
    public static final CreativeModeTab IUTab = new TabCore(0, "IUTab");
    public static final CreativeModeTab ModuleTab = new TabCore(1, "ModuleTab");
    public static final CreativeModeTab ItemTab = new TabCore(2, "ItemTab");
    public static final CreativeModeTab OreTab = new TabCore(3, "OreTab");
    public static final CreativeModeTab EnergyTab = new TabCore(4, "EnergyTab");
    public static final CreativeModeTab RecourseTab = new TabCore(5, "ResourceTab");
    public static final CreativeModeTab ReactorsTab = new TabCore(6, "ReactorsTab");
    public static final CreativeModeTab UpgradeTab = new TabCore(7, "UpgradeTab");
    public static final CreativeModeTab ElementsTab = new TabCore(9, "CraftingElementsTab");
    public static final CreativeModeTab ReactorsBlockTab = new TabCore(10, "ReactorsBlockTab");
    public static final CreativeModeTab CropsTab = new TabCore(11, "CropsTab");
    public static final CreativeModeTab BeesTab = new TabCore(12, "BeesTab");
    public static final CreativeModeTab GenomeTab = new TabCore(13, "GenomeTab");
    public static final CreativeModeTab SpaceTab = new TabCore(14, "SpaceTab");
    public static final CreativeModeTab fluidCellTab = new TabCore(15, "fluidCellTab");

    public static final String MODID = "industrialupgrade";
    public static final CommonProxy proxy;
    public static final Logger LOGGER = LogUtils.getLogger();
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
    public static final List<QuarryItem> get_polisher_quarry = new ArrayList<>();
    public static final List<QuarryItem> get_separator_quarry = new ArrayList<>();
    public static final List<QuarryItem> get_comb_crushed_quarry = new ArrayList<>();
    public static final List<List<ItemStack>> removing_list = new ArrayList<>();

    public static final List<ItemStack> get_polisher = new ArrayList<>();
    public static final Map<String, PlayerStreakInfo> mapStreakInfo = new HashMap<>();
    public static final Map<String, LootTable> lootTables = new HashMap<>();
    public static Sides<NetworkManager> network;
    public static Random random = new Random();
    public static RandomSource randomSource = new LegacyRandomSource(random.nextLong());
    public static KeyboardIU keyboard;
    public static FMLJavaModLoadingContext context;
    public static List<Runnable> runnableListAfterRegisterItem = new ArrayList<>();
    public static boolean update = false;
    public static IUCore instance;
    public static Map<Item, Crop> cropMap = new HashMap<>();
    static List<String> stringList = new ArrayList<>();
    static boolean change = false;
    static boolean register = false;
    static boolean register1 = false;

    static {
        proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        IUCore.network = new Sides<>("com.denfop.network.NetworkManager", "com.denfop.network.NetworkManagerClient");
        keyboard = DistExecutor.unsafeRunForDist(() -> KeyboardClient::new, () -> KeyboardIU::new);
        Keys.instance = IUCore.keyboard;
    }

    private boolean register2 = false;

    public IUCore() {
        this.context = FMLJavaModLoadingContext.get();
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_SPEC);

        IUCore.instance = this;
        new TileBlockCreator();
        ElectricItem.manager = new ElectricItemManager();
        EnergyNetGlobal.instance = new EnergyNetGlobal();
        Recipes.registerRecipes();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new TickHandlerIU());
        MinecraftForge.EVENT_BUS.register(new EventSaveData());
        MinecraftForge.EVENT_BUS.register(new Register());
        MinecraftForge.EVENT_BUS.register(new IUEventHandler());
        MinecraftForge.EVENT_BUS.register(new EventUpdate());

        Register.register();
        new WorldBaseGen();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::registerContent);


        modEventBus.addListener(this::preInit);
        modEventBus.addListener(this::init);
        modEventBus.addListener(this::postInit);
        modEventBus.addListener(this::onAttributeCreate);

        TradingSystem.init();
        MinecraftForge.EVENT_BUS.register(this);

    }

    public static void addrecipe(
            ItemStack stack,
            double matter,
            double sunmatter,
            double aquamatter,
            double nethermatter,
            double nightmatter,
            double earthmatter,
            double endmatter,
            double aermatter
    ) {
        CompoundTag nbt = new CompoundTag();
        double[] quantitysolid = {Precision.round(matter, 2),
                Precision.round(sunmatter, 2), Precision.round(aquamatter, 2),
                Precision.round(nethermatter, 2), Precision.round(nightmatter, 2),
                Precision.round(earthmatter, 2),
                Precision.round(endmatter, 2), Precision.round(aermatter, 2)};
        for (int i = 0; i < quantitysolid.length; i++) {
            ModUtils.SetDoubleWithoutItem(nbt, ("quantitysolid_" + i), quantitysolid[i]);
        }
        final IInputHandler input = inputFactory;
        if (stack.isEmpty()) {
            return;
        }
        List<TagKey<Item>> list1 = stack.getItem().builtInRegistryHolder().tags().toList();
        if (list1.isEmpty()) {
            Recipes.recipes.addRecipe("converter", new BaseMachineRecipe(new Input(input.getInput(stack)), new RecipeOutput(
                    nbt,
                    stack
            )));
        } else {
            Recipes.recipes.addRecipe(
                    "converter",
                    new BaseMachineRecipe(new Input(input.getInput(list1)), new RecipeOutput(
                            nbt,
                            stack
                    ))
            );
        }

    }

    @SubscribeEvent
    public void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(IUItem.entity_bee.get(), SmallBee.createAttributes().build());
    }

    public void registerData(Level level) {
        if (!register1) {
            register1 = true;
            ListInformationUtils.init();
            List<MultiBlockEntity> tiles = TileBlockCreator.instance.getAllTiles();
            for (MultiBlockEntity tileBlock : tiles)

                if (tileBlock.getDummyTe() instanceof IManufacturerBlock) {
                    if (!mechanism_info1.containsKey(tileBlock)) {
                        mechanism_info1.put(tileBlock, tileBlock.getDummyTe().getPickBlock(null, null).getDisplayName());
                    }
                }

            CropInit.initBiomes();
            RecipeManager recipeManager = level.getRecipeManager();


            Collection<SmeltingRecipe> furnaceRecipes = recipeManager.getAllRecipesFor(RecipeType.SMELTING);

            for (SmeltingRecipe recipe : furnaceRecipes) {
                ItemStack input = recipe.getIngredients().get(0).getItems()[0];
                ItemStack output = recipe.getResultItem();
                if (input.isEmpty()) {
                    continue;
                }
                CompoundTag nbt = new CompoundTag();
                try {
                    nbt.putFloat("experience", recipe.getExperience());
                } catch (Exception e) {
                    nbt.putFloat("experience", 0.1F);

                }
                Recipes.recipes.addRecipe(
                        "furnace",
                        new BaseMachineRecipe(
                                new Input(
                                        inputFactory.getInput(input)
                                ),
                                new RecipeOutput(nbt, output)
                        )
                );
            }

            if (!change) {
                change = true;
                removeOre("forge:gems/Iridium");
                removeOre("forge:gems/Americium");
                removeOre("forge:gems/Neptunium");
                removeOre("forge:gems/Curium");
                removeOre("forge:gems/Thorium");
                removeOre("forge:gems/Bor");
                removeOre("forge:gems/CrystalFlux");
                removeOre("forge:gems/Beryllium");
                addOre1(new ItemStack(Items.REDSTONE));
                addOre1(new ItemStack(Items.COAL));
                for (String name : RegisterOreDictionary.list_heavyore) {
                    removeOre("forge:ores/" + name);
                }
                for (String name : RegisterOreDictionary.list_mineral) {
                    removeOre("forge:ores/" + name);
                }
                for (String name : RegisterOreDictionary.spaceElementList) {
                    removeOre("forge:ores/" + name);
                }
                removeOre("forge:ores/Sulfur");
                removeOre("forge:ores/Boron");
                removeOre("forge:ores/Beryllium");
                removeOre("forge:ores/Lithium");
                removeOre("forge:ores/uranium");
                removeOre("forge:ores/Thorium");
                removeOre("forge:ores/Calcium");
                removeOre("forge:ores/Coal");
                removeOre("forge:ores/apatite");
                removeOre("forge:raw_materials/uranium");
                removeOre("forge:ores/Thorium");
                removeOre("forge:ores/Redstone");
                removeOre("forge:ores/sheelite");
                removeOre("forge:ores/Draconium");
                removeOre("forge:ores/netherite_scrap");
                removeOre("forge:ores/saltpeter");
                IUCore.list_furnace_adding.forEach(this::addOre2);
                IUCore.list_furnace_removing.forEach(this::removeOre2);
                IUCore.list_adding.forEach(this::addOre1);
                IUCore.list_removing.forEach(this::removeOre);
                IUCore.list_comb_crushed_adding.forEach(this::addOre4);
                IUCore.list_comb_crushed_removing.forEach(this::removeOre4);
                IUCore.list_crushed_adding.forEach(this::addOre3);
                IUCore.list_crushed_removing.forEach(this::removeOre3);
                IUCore.list.forEach(stack -> {

                    get_all_list.add(new RecipeInputStack(stack));
                });
                get_all_list.removeIf(stack -> IUCore.get_ingot.contains(stack.getItemStack().get(0)));
                IUCore.get_ingot.forEach(stack -> {

                    get_all_list.add(new RecipeInputStack(stack));
                });

                get_all_list.removeIf(stack -> IUCore.get_comb_crushed.contains(stack.getItemStack().get(0)));
                IUCore.get_comb_crushed.forEach(stack -> {

                    get_all_list.add(new RecipeInputStack(stack));
                });
                get_all_list.removeIf(stack -> IUCore.get_crushed.contains(stack.getItemStack().get(0)));
                IUCore.get_crushed.forEach(stack -> {

                    get_all_list.add(new RecipeInputStack(stack));
                });

                IUCore.get_crushed.forEach(stack -> {
                    if (!stack.isEmpty()) {
                        get_crushed_quarry.add(new QuarryItem(stack));
                    }
                });
                IUCore.get_polisher.forEach(stack -> {
                    if (!stack.isEmpty()) {
                        this.
                                get_polisher_quarry.add(new QuarryItem(stack));
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
            }
            try {

                PotionRecipes.init();
                List<PotionRecipes.Mix<Potion>> potionMixes = PotionRecipes.potionMixes;
                for (PotionRecipes.Mix<Potion> mix : potionMixes) {
                    Holder<Potion> from = mix.from();
                    Ingredient ingredient = mix.ingredient();
                    Holder<Potion> to = mix.to();

                    ItemStack inputPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), from.value());
                    ItemStack outputPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), to.value());


                    Recipes.recipes.addRecipe(
                            "brewing",
                            new BaseMachineRecipe(
                                    new Input(
                                            inputFactory.getInput(inputPotion),
                                            inputFactory.getInput(ingredient.getItems()[0])
                                    ),
                                    new RecipeOutput(null, outputPotion)
                            )
                    );
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            ;
        }
    }

    @SubscribeEvent
    public void blindness(LivingHurtEvent event) {
        Level world = event.getEntity().level;
        if (world.isClientSide()) {
            return;
        }

        Entity sourceEntity = event.getSource().getDirectEntity();
        if (!(sourceEntity instanceof EntityAdvArrow tippedArrow)) {
            return;
        }

        try {
            ItemStack stack = tippedArrow.getStack();
            boolean blindness = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.BLINDNESS, stack);
            if (!blindness) {
                return;
            }

            event.getEntity().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60));
            tippedArrow.discard();
        } catch (Exception e) {
            tippedArrow.discard();
        }
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation name = event.getName();


        if (name.getPath().startsWith("entities/")) {
            String id = name.toString();

            int colonIndex = id.indexOf(':');
            int slashIndex = id.indexOf('/');
            if (colonIndex >= 0 && slashIndex > colonIndex) {
                StringBuilder builder = new StringBuilder(id);
                builder.delete(colonIndex + 1, slashIndex + 1);


                if (builder.toString().contains("sheep")) {
                    int index = builder.indexOf("/");
                    if (index >= 0) {
                        builder.replace(index, index + 1, "_");
                    }
                }

                String key = builder.toString();
                lootTables.put(key, event.getTable());

                if (key.equals("minecraft:iron_golem")) {
                    lootTables.put("minecraft:villager_golem", event.getTable());
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.getEntity().getLevel().isClientSide()) {
            IUCore.keyboard.removePlayerReferences(event.getEntity());
        }

    }

    private void registerContent(RegisterEvent event) {
        if (Objects.equals(event.getForgeRegistry(), ForgeRegistries.ITEMS)) {
            Register.writeItems();
            CropInit.init();
        }

    }

    public void init(InterModEnqueueEvent setup) {
        proxy.init();
        if (ModList.get().isLoaded("theoneprobe")) {
            RegisterProvider registerProvider = new RegisterProvider();
            registerProvider.registerIMC(setup);
        }

        EnumTypeMachines.writeSound();
    }

    public void postInit(FMLLoadCompleteEvent setup) {
        ((RecipesCore) Recipes.recipes).setCanAdd(false);

        BlockEntityApiary.beeAI =  BeeAI.beeAI;
        cropMap.put(Items.WHEAT_SEEDS, CropInit.wheat_seed);
        cropMap.put(Items.SUGAR_CANE, CropInit.reed_seed);
        cropMap.put(Items.POTATO, CropInit.potato);
        cropMap.put(Items.CARROT, CropInit.carrot);
        cropMap.put(Items.MELON_SEEDS, CropInit.melon);
        cropMap.put(Items.PUMPKIN_SEEDS, CropInit.pumpkin);
        cropMap.put(Items.MELON, CropInit.melon);
        cropMap.put(Items.PUMPKIN, CropInit.pumpkin);
        cropMap.put(Items.BEETROOT, CropInit.beet);
        cropMap.put(Items.NETHER_WART, CropInit.nether_wart);
        proxy.postInit();
        new GuideBookCore();
        GuideBookCore.init();
        for (IItemTag iItemTag : ItemTagProvider.list) {
            Item item = iItemTag.getItem();
            String[] stringTags = iItemTag.getTags();
            for (String stringTag : stringTags) {
                ItemTagProvider.mapItems.computeIfAbsent(new ResourceLocation(stringTag), k -> new ArrayList<>()).add(new ItemStack(item));
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
        if (IUItem.machine == null)
            Register.writeItems();
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


        addIngot("gold", Items.GOLD_INGOT);
        addIngot("iron", Items.IRON_INGOT);
        addIngot("copper", Items.COPPER_INGOT);
        addGem("diamond", Items.DIAMOND);
        addGem("emerald", Items.EMERALD);
        addGem("coal", Items.COAL);
        addGem("quarts", Items.QUARTZ);
        addCustomMinecraft("wool", Blocks.WHITE_WOOL.asItem());
        addCustom("string", Items.STRING);
        addCustomMinecraft("saplings", Blocks.OAK_SAPLING.asItem());
        addRawMaterials("gold", Items.GOLD_INGOT);
        addRawMaterials("iron", Items.IRON_INGOT);
        addRawMaterials("copper", Items.COPPER_INGOT);
        addStorageBlocks("gold", Blocks.GOLD_BLOCK.asItem());
        addStorageBlocks("iron", Blocks.IRON_BLOCK.asItem());
        addStorageBlocks("copper", Blocks.COPPER_BLOCK.asItem());
        addStorageBlocks("coal", Blocks.COAL_BLOCK.asItem());
        addCustomMinecraft("leaves", Blocks.OAK_LEAVES.asItem());
        addStorageBlocks("lapis", Blocks.LAPIS_BLOCK.asItem());
        fluidToLevel.put(FluidName.fluidhydrazine.getInstance().get(), 1);
        fluidToLevel.put(FluidName.fluiddimethylhydrazine.getInstance().get(), 2);
        fluidToLevel.put(FluidName.fluiddecane.getInstance().get(), 3);
        fluidToLevel.put(FluidName.fluidxenon.getInstance().get(), 4);
    }

    public void addRawMaterials(String tag, Item item) {
        String[] stringTags = new String[]{"forge:raw_materials/" + tag};
        for (String stringTag : stringTags) {
            ItemTagProvider.mapItems.computeIfAbsent(new ResourceLocation(stringTag.toLowerCase()), k -> new ArrayList<>()).add(new ItemStack(item));

        }
    }

    public void addStorageBlocks(String tag, Item item) {
        String[] stringTags = new String[]{"forge:storage_blocks/" + tag};
        for (String stringTag : stringTags) {
            ItemTagProvider.mapItems.computeIfAbsent(new ResourceLocation(stringTag.toLowerCase()), k -> new ArrayList<>()).add(new ItemStack(item));

        }
    }

    public void addCustomMinecraft(String tag, Item item) {
        String[] stringTags = new String[]{"minecraft:" + tag};
        for (String stringTag : stringTags) {
            ItemTagProvider.mapItems.computeIfAbsent(new ResourceLocation(stringTag.toLowerCase()), k -> new ArrayList<>()).add(new ItemStack(item));

        }
    }

    public void addCustom(String tag, Item item) {
        String[] stringTags = new String[]{"forge:" + tag};
        for (String stringTag : stringTags) {
            ItemTagProvider.mapItems.computeIfAbsent(new ResourceLocation(stringTag.toLowerCase()), k -> new ArrayList<>()).add(new ItemStack(item));

        }
    }

    public void addPlate(String tag, Item item) {
        String[] stringTags = new String[]{"forge:plates/" + tag, "forge:plates"};
        for (String stringTag : stringTags) {
            ItemTagProvider.mapItems.computeIfAbsent(new ResourceLocation(stringTag.toLowerCase()), k -> new ArrayList<>()).add(new ItemStack(item));

        }
    }

    public void addPlateDense(String tag, Item item) {
        String[] stringTags = new String[]{"forge:platedense/" + tag, "forge:platedense"};
        for (String stringTag : stringTags) {
            ItemTagProvider.mapItems.computeIfAbsent(new ResourceLocation(stringTag.toLowerCase()), k -> new ArrayList<>()).add(new ItemStack(item));

        }
    }

    public void addIngot(String tag, Item item) {
        String[] stringTags = new String[]{"forge:ingots/" + tag, "forge:ingots"};
        for (String stringTag : stringTags) {
            ItemTagProvider.mapItems.computeIfAbsent(new ResourceLocation(stringTag.toLowerCase()), k -> new ArrayList<>()).add(new ItemStack(item));

        }
    }

    public void addDust(String tag, Item item) {
        String[] stringTags = new String[]{"forge:dusts/" + tag, "forge:dusts"};
        for (String stringTag : stringTags) {
            ItemTagProvider.mapItems.computeIfAbsent(new ResourceLocation(stringTag.toLowerCase()), k -> new ArrayList<>()).add(new ItemStack(item));

        }
    }

    public void addCasing(String tag, Item item) {
        String[] stringTags = new String[]{"forge:casings/" + tag, "forge:casings"};
        for (String stringTag : stringTags) {
            ItemTagProvider.mapItems.computeIfAbsent(new ResourceLocation(stringTag.toLowerCase()), k -> new ArrayList<>()).add(new ItemStack(item));

        }
    }

    public void addGem(String tag, Item item) {
        String[] stringTags = new String[]{"forge:gems/" + tag, "forge:gems"};
        for (String stringTag : stringTags) {
            ItemTagProvider.mapItems.computeIfAbsent(new ResourceLocation(stringTag.toLowerCase()), k -> new ArrayList<>()).add(new ItemStack(item));

        }
    }


    @SubscribeEvent
    public void updateRecipe(PlayerEvent.PlayerLoggedInEvent event) {
        if (!update) {
            update = true;

            Map<List<List<ItemStack>>, MatterRecipe> itemStackMap1 = new HashMap<>();
            long startTime = System.nanoTime();
            List<CraftingRecipe> listRecipes = event.getEntity().getLevel().getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
            List<MatterRecipe> matterRecipeList = new ArrayList<>();
            IUCore.LOGGER.debug("Checking recipes {} ", listRecipes.size());


            for (CraftingRecipe r : listRecipes) {
                List<List<ItemStack>> itemStackList = new ArrayList<>();
                for (Ingredient ingredient : r.getIngredients()) {
                    List<ItemStack> itemStackList1;
                    itemStackList1 = Arrays.asList(ingredient.getItems());
                    if (!itemStackList1.isEmpty()) {
                        itemStackList.add(itemStackList1);
                    }
                }
                ItemStack output = ItemStack.EMPTY;
                output = r.getResultItem();
                if (output == ItemStack.EMPTY)
                    continue;
                final CompoundTag nbt = ModUtils.nbtOrNull(output);

                if ((nbt != null && nbt.contains("RSControl"))) {
                    continue;
                }
                if (Recipes.recipes.getRecipeOutput("converter", false, output) != null) {
                    continue;
                }
                MatterRecipe matterRecipe = new MatterRecipe(r.getResultItem());
                matterRecipeList.add(matterRecipe);
                itemStackMap1.put(itemStackList, matterRecipe);
            }
            IUCore.LOGGER.debug(
                    "Finished checking recipes for converter matter after {} ms.",
                    (System.nanoTime() - startTime) / 1000000L
            );
            startTime = System.nanoTime();
            Map<List<List<ItemStack>>, MatterRecipe> itemStackMap3 = new HashMap<>();
            final long startTime1 = System.nanoTime();
            for (int i = 0; i < 1; i++) {
                for (Map.Entry<List<List<ItemStack>>, MatterRecipe> entry : itemStackMap1.entrySet()) {
                    List<List<ItemStack>> list = entry.getKey();
                    final ItemStack output = entry.getValue().getStack();
                    MatterRecipe matterRecipe = entry.getValue();
                    if (matterRecipe.can()) {
                        continue;
                    }
                    List<List<ItemStack>> list2 = new ArrayList<>();
                    for (List<ItemStack> list1 : list) {
                        boolean need_continue = false;
                        for (ItemStack stack : list1) {
                            BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput("converter", false, stack);
                            if (recipe == null) {
                                continue;
                            }
                            need_continue = true;
                            matterRecipe.addMatter(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_0") / output.getCount());
                            matterRecipe.addSun(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_1") / output.getCount());
                            matterRecipe.addAqua(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_2") / output.getCount());
                            matterRecipe.addNether(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_3") / output.getCount());
                            matterRecipe.addNight(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_4") / output.getCount());
                            matterRecipe.addEarth(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_5") / output.getCount());
                            matterRecipe.addEnd(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_6") / output.getCount());
                            matterRecipe.addAer(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_7") / output.getCount());
                            list2.add(list1);
                            break;
                        }
                        if (!need_continue) {
                            break;
                        }

                    }
                    list.removeIf(list2::contains);
                    if (list.isEmpty()) {
                        matterRecipe.setCan(true);
                        itemStackMap3.put(list, matterRecipe);
                    }

                }
                List<MatterRecipe> matterRecipeList1 = new ArrayList<>();
                matterRecipeList.forEach(matterRecipe1 -> {
                    if (matterRecipe1.can()) {
                        try {
                            addrecipe(matterRecipe1.getStack(),
                                    Precision.round(matterRecipe1.getMatter(), 2),
                                    Precision.round(matterRecipe1.getSun(), 2)
                                    ,
                                    Precision.round(matterRecipe1.getAqua(), 2),
                                    Precision.round(matterRecipe1.getNether(), 2),
                                    Precision.round(matterRecipe1.getNight(), 2),
                                    Precision.round(matterRecipe1.getEarth(), 2),
                                    Precision.round(matterRecipe1.getEnd(), 2),
                                    Precision.round(matterRecipe1.getAer(), 2)
                            );
                            matterRecipeList1.add(matterRecipe1);
                        } catch (Exception e) {
                        }
                        ;

                    }

                });
                matterRecipeList.removeAll(matterRecipeList1);
                itemStackMap3.forEach((key, value) -> itemStackMap1.remove(key));
                IUCore.LOGGER.debug("Finished  %d stage recipes for converter matter after {} ms. ", i,
                        (System.nanoTime() - startTime) / 1000000L
                );
                startTime = System.nanoTime();
            }

            IUCore.LOGGER.debug("Finished adding recipes for converter matter after {} ms.", (System.nanoTime() - startTime1) / 1000000L
            );
            matterRecipeList.clear();
            itemStackMap1.clear();
            IUItem.machineRecipe = Recipes.recipes.getRecipeStack("converter");
            IUItem.fluidMatterRecipe = Recipes.recipes.getRecipeStack("replicator");
        }
    }

    @SubscribeEvent
    public void updateRecipe(ServerStartingEvent event) {
        if (!update) {
            update = true;


            Map<List<List<ItemStack>>, MatterRecipe> itemStackMap1 = new HashMap<>();
            long startTime = System.nanoTime();
            List<CraftingRecipe> listRecipes = event.getServer().getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
            List<MatterRecipe> matterRecipeList = new ArrayList<>();
            IUCore.LOGGER.debug("Checking recipes {} ", listRecipes.size());


            for (CraftingRecipe r : listRecipes) {
                List<List<ItemStack>> itemStackList = new ArrayList<>();
                for (Ingredient ingredient : r.getIngredients()) {
                    List<ItemStack> itemStackList1;
                    itemStackList1 = Arrays.asList(ingredient.getItems());
                    if (!itemStackList1.isEmpty()) {
                        itemStackList.add(itemStackList1);
                    }
                }
                ItemStack output = ItemStack.EMPTY;
                output = r.getResultItem();
                if (output == ItemStack.EMPTY)
                    continue;
                final CompoundTag nbt = ModUtils.nbtOrNull(output);

                if ((nbt != null && nbt.contains("RSControl"))) {
                    continue;
                }
                if (Recipes.recipes.getRecipeOutput("converter", false, output) != null) {
                    continue;
                }
                MatterRecipe matterRecipe = new MatterRecipe(r.getResultItem());
                matterRecipeList.add(matterRecipe);
                itemStackMap1.put(itemStackList, matterRecipe);
            }
            IUCore.LOGGER.debug(
                    "Finished checking recipes for converter matter after {} ms.",
                    (System.nanoTime() - startTime) / 1000000L
            );
            startTime = System.nanoTime();
            Map<List<List<ItemStack>>, MatterRecipe> itemStackMap3 = new HashMap<>();
            final long startTime1 = System.nanoTime();
            for (int i = 0; i < 1; i++) {
                for (Map.Entry<List<List<ItemStack>>, MatterRecipe> entry : itemStackMap1.entrySet()) {
                    List<List<ItemStack>> list = entry.getKey();
                    final ItemStack output = entry.getValue().getStack();
                    MatterRecipe matterRecipe = entry.getValue();
                    if (matterRecipe.can()) {
                        continue;
                    }
                    List<List<ItemStack>> list2 = new ArrayList<>();
                    for (List<ItemStack> list1 : list) {
                        boolean need_continue = false;
                        for (ItemStack stack : list1) {
                            BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput("converter", false, stack);
                            if (recipe == null) {
                                continue;
                            }
                            need_continue = true;
                            matterRecipe.addMatter(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_0") / output.getCount());
                            matterRecipe.addSun(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_1") / output.getCount());
                            matterRecipe.addAqua(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_2") / output.getCount());
                            matterRecipe.addNether(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_3") / output.getCount());
                            matterRecipe.addNight(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_4") / output.getCount());
                            matterRecipe.addEarth(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_5") / output.getCount());
                            matterRecipe.addEnd(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_6") / output.getCount());
                            matterRecipe.addAer(stack.getCount() * recipe.output.metadata.getDouble("quantitysolid_7") / output.getCount());
                            list2.add(list1);
                            break;
                        }
                        if (!need_continue) {
                            break;
                        }

                    }
                    list.removeIf(list2::contains);
                    if (list.isEmpty()) {
                        matterRecipe.setCan(true);
                        itemStackMap3.put(list, matterRecipe);
                    }

                }
                List<MatterRecipe> matterRecipeList1 = new ArrayList<>();
                matterRecipeList.forEach(matterRecipe1 -> {
                    if (matterRecipe1.can()) {
                        try {
                            addrecipe(matterRecipe1.getStack(),
                                    Precision.round(matterRecipe1.getMatter(), 2),
                                    Precision.round(matterRecipe1.getSun(), 2)
                                    ,
                                    Precision.round(matterRecipe1.getAqua(), 2),
                                    Precision.round(matterRecipe1.getNether(), 2),
                                    Precision.round(matterRecipe1.getNight(), 2),
                                    Precision.round(matterRecipe1.getEarth(), 2),
                                    Precision.round(matterRecipe1.getEnd(), 2),
                                    Precision.round(matterRecipe1.getAer(), 2)
                            );
                            matterRecipeList1.add(matterRecipe1);
                        } catch (Exception e) {
                        }
                        ;

                    }

                });
                matterRecipeList.removeAll(matterRecipeList1);
                itemStackMap3.forEach((key, value) -> itemStackMap1.remove(key));
                IUCore.LOGGER.debug("Finished  %d stage recipes for converter matter after {} ms. ", i,
                        (System.nanoTime() - startTime) / 1000000L
                );
                startTime = System.nanoTime();
            }

            IUCore.LOGGER.debug("Finished adding recipes for converter matter after {} ms.", (System.nanoTime() - startTime1) / 1000000L
            );
            matterRecipeList.clear();
            itemStackMap1.clear();
            IUItem.machineRecipe = Recipes.recipes.getRecipeStack("converter");
            IUItem.fluidMatterRecipe = Recipes.recipes.getRecipeStack("replicator");
        }
    }

    public void preInit(FMLCommonSetupEvent setup) {
        new VeinSystem();
        new GasVeinSystem();
        HeatNet.instance = HeatNetGlobal.initialize();
        CoolNet.instance = CoolNetGlobal.initialize();
        PressureNet.instance = PressureNetGlobal.initialize();

        SolarEnergySystem.system = new SolarEnergySystem();
        new MultiBlockSystem();
        new RadiationSystem();
        new WindSystem();
        EnergyBase.init();
        TransportNetGlobal.initialize();
        TesseractSystem.init();
        PollutionManager.pollutionManager = new PollutionManager();
        SpaceNet.instance = new BaseSpaceSystem();
        SpaceUpgradeSystem.system = new BaseSpaceUpgradeSystem();
        RelocatorNetwork.init();
        UpgradeSystem.system = new BaseUpgradeSystem();
        new RotorUpgradeSystem();
        new com.denfop.api.water.upgrade.RotorUpgradeSystem();
        EnergyNetGlobal.initialize();
        proxy.preInit();
        InitMultiBlockSystem.init();
        IUItem.register_mineral();
        EnumSolarPanels.registerTile();
        ItemUpgradePanelKit.EnumSolarPanelsKit.registerkit();


    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        ServerLevel world = event.getServer().overworld();

        if (!world.isClientSide) {
            EventHandlerPlanet.data = world.getDataStorage().computeIfAbsent(WorldSavedDataIU::new, WorldSavedDataIU::new, Constants.MOD_ID);

            EventHandlerPlanet.data.setWorld((Level) world);
            EventHandlerPlanet.data.setDirty(true);

        }
    }

    @SubscribeEvent
    public void loginPlayer(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().getLevel().isClientSide) {
            return;
        }
        if (!GuideBookCore.uuidGuideMap.containsKey(event.getEntity().getUUID())) {
            GuideBookCore.instance.load(event.getEntity().getUUID(), event.getEntity());
            event.getEntity().addItem(new ItemStack(IUItem.book.getItem()));
            event.getEntity().addItem(new ItemStack(IUItem.veinsencor.getStack(0)));
        } else {
            GuideBookCore.instance.loadOrThrow(event.getEntity().getUUID());
            new PacketUpdateInformationAboutQuestsPlayer(GuideBookCore.uuidGuideMap.get(event.getEntity().getUUID()), event.getEntity());
        }

        new PacketUpdateRelocator(event.getEntity());

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartedEvent event) {
        registerData(event.getServer().overworld());
    }


    @SubscribeEvent
    public void getore(TagsUpdatedEvent event) {
        if (!register) {
            register = true;
            Iterable<Holder<Item>> tagOres = Registry.ITEM.getTagOrEmpty(ItemTags.create(new ResourceLocation("forge", "ores")));
            MaceratorRecipe.recipe();
            CompressorRecipe.recipe();
            ExtractorRecipe.init();
            OreWashingRecipe.init();
            ReplicatorRecipe.init();
            CentrifugeRecipe.init();
            MetalFormerRecipe.init();
            BlockEntityPalletGenerator.init();
            BlockEntitySolidCooling.init();
            SpaceInit.jsonInit();
            BaseSpaceUpgradeSystem.list.forEach(Runnable::run);
            IUCore.runnableListAfterRegisterItem.forEach(Runnable::run);
            new ScrapboxRecipeManager();
            Recipes.recipes.initializationRecipes();
            Recipes.recipes.removeAllRecipesFromList();
            Recipes.recipes.addAllRecipesFromList();
            final IInputHandler input = com.denfop.api.Recipes.inputFactory;
            for (int i = 0; i < 8; i++) {
                Recipes.recipes.addRecipe(
                        "matter",
                        new BaseMachineRecipe(
                                new Input(
                                        input.getInput(new ItemStack(IUItem.matter.getStack(i), 1))
                                ),
                                new RecipeOutput(null, new ItemStack(IUItem.matter.getStack(i), 1))
                        )
                );
            }
            for (Holder<Item> holder : tagOres) {
                get_ore.add(new ItemStack(holder));
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
                        if (stringList.contains(name))
                            continue;
                        TagKey<Item> tag = ItemTags.create(new ResourceLocation("forge", "gems/" + name));
                        List<Holder<Item>> gemList = new ArrayList<>();
                        Registry.ITEM.getTagOrEmpty(tag).forEach(gemList::add);
                        TagKey<Item> tag1 = ItemTags.create(new ResourceLocation("forge", "raw_materials/" + name));
                        List<Holder<Item>> rawList = new ArrayList<>();
                        Registry.ITEM.getTagOrEmpty(tag1).forEach(rawList::add);
                        if (!gemList.isEmpty()) {
                            if (!stringList.contains(name)) {
                                list.add(new ItemStack(gemList.get(0).get()));
                                stringList.add(name);
                                break;
                            }

                        } else {
                            if (!rawList.isEmpty()) {
                                if (!stringList.contains(name)) {
                                    list.add(new ItemStack(rawList.get(0).get()));
                                    stringList.add(name);
                                    break;
                                }
                            } else if (!stringList.contains(name)) {
                                list.add(new ItemStack(item));
                                stringList.add(name);
                                break;
                            }
                        }
                    }
                }
            }


        }
    }

    public void removeOre(String name) {
        List<ItemStack> input = inputFactory.getInput(name).getInputs();
        if (!input.isEmpty()) {
            list.removeIf(stack -> stack.is(input.get(0).getItem()));
        }
    }

    public void addOre1(ItemStack name) {
        list.add(name);
    }

    public void removeOre(ItemStack name) {
        list.removeIf(stack -> stack.is(name.getItem()));
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
        get_ingot.removeIf(stack -> stack.is(name.getItem()));
    }

    public void removeOre3(ItemStack name) {
        get_crushed.removeIf(stack -> stack.is(name.getItem()));
    }

    public void removeOre4(ItemStack name) {
        get_comb_crushed.removeIf(stack -> stack.is(name.getItem()));
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ++ItemQuantumSaber.ticker;
            ++ItemSpectralSaber.ticker;
            ++ItemNanoSaber.ticker;
        }

    }


}
