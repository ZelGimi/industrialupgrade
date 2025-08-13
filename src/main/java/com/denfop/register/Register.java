package com.denfop.register;

import com.denfop.*;
import com.denfop.api.space.rovers.enums.EnumRoversLevel;
import com.denfop.api.space.rovers.enums.EnumRoversLevelFluid;
import com.denfop.api.space.rovers.enums.EnumTypeRovers;
import com.denfop.api.upgrades.UpgradeRegistry;
import com.denfop.audio.Sounds;
import com.denfop.blocks.*;
import com.denfop.blocks.blockitem.*;
import com.denfop.blocks.fluid.IUFluidType;
import com.denfop.blocks.mechanism.*;
import com.denfop.container.ContainerBase;
import com.denfop.effects.EffectsRegister;
import com.denfop.entity.SmallBee;
import com.denfop.entity.EntityNuclearBombPrimed;
import com.denfop.items.*;
import com.denfop.items.armour.*;
import com.denfop.items.armour.material.ArmorMaterials;
import com.denfop.items.armour.special.EnumSubTypeArmor;
import com.denfop.items.armour.special.EnumTypeArmor;
import com.denfop.items.armour.special.ItemSpecialArmor;
import com.denfop.items.bags.ItemEnergyBags;
import com.denfop.items.bags.ItemLeadBox;
import com.denfop.items.bee.ItemBeeAnalyzer;
import com.denfop.items.bee.ItemJarBees;
import com.denfop.items.book.ItemBook;
import com.denfop.items.creative.ItemCreativeBattery;
import com.denfop.items.creative.ItemCreativeTomeResearchSpace;
import com.denfop.items.crop.ItemAgriculturalAnalyzer;
import com.denfop.items.crop.ItemCrops;
import com.denfop.items.energy.*;
import com.denfop.items.energy.instruments.EnumTypeInstruments;
import com.denfop.items.energy.instruments.EnumVarietyInstruments;
import com.denfop.items.energy.instruments.ItemEnergyInstruments;
import com.denfop.items.genome.ItemBeeGenome;
import com.denfop.items.genome.ItemCropGenome;
import com.denfop.items.modules.ItemUpgradeModule;
import com.denfop.items.modules.*;
import com.denfop.items.panel.*;
import com.denfop.items.reactors.*;
import com.denfop.items.relocator.ItemRelocator;
import com.denfop.items.resource.*;
import com.denfop.items.resource.alloys.*;
import com.denfop.items.resource.preciousresources.ItemPreciousGem;
import com.denfop.items.space.*;
import com.denfop.items.upgradekit.ItemUpgradeKit;
import com.denfop.items.upgradekit.ItemUpgradeMachinesKit;
import com.denfop.items.upgradekit.ItemUpgradePanelKit;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IndustrialShapedRecipeSerializer;
import com.denfop.recipe.IndustrialShapelessRecipeSerializer;
import com.denfop.recipe.universalrecipe.*;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.villager.VillagerInit;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.io.IOException;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = IUCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Register {


    public static DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registries.PLACED_FEATURE, IUCore.MODID);

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, IUCore.MODID);
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, IUCore.MODID);
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, IUCore.MODID);

    public static DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, IUCore.MODID);
    public static DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, IUCore.MODID);
    public static DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, IUCore.MODID);
    public static DeferredRegister<MobEffect> MOB_EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, IUCore.MODID);
    public static DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, IUCore.MODID);
    public static DeferredRegister<MenuType<?>> MENU_TYPE = DeferredRegister.create(ForgeRegistries.MENU_TYPES, IUCore.MODID);

    public static DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registries.CONFIGURED_FEATURE, IUCore.MODID);

    public static DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, IUCore.MODID);
    public static DeferredRegister<RecipeType<?>> RECIPE_TYPE = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, IUCore.MODID);
    public static DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IUCore.MODID);

    public static RegistryObject<DamageType> currentObject;
    public static RegistryObject<DamageType> radiationObject;
    public static RegistryObject<DamageType> frostbiteObject;
    public static RegistryObject<DamageType> poison_gasObject;
    public static RegistryObject<DamageType> beeObject;
    public static DeferredRegister<DamageType> DAMAGE_TYPE = DeferredRegister.create(Registries.DAMAGE_TYPE, IUCore.MODID);

    public static ForgeFlowingFluid.Properties properties;
    private static ItemStackInventory invent;
    public static RegistryObject<MenuType<ContainerBase<?>>> containerBase;
    public static RegistryObject<MenuType<ContainerBase<?>>> inventory_container;

    public static DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, IUCore.MODID);
    public static RegistryObject<RecipeType<Recipe<?>>> UNIVERSAL_RECIPE_TYPE;
    public static RegistryObject<IURecipeSerializer> RECIPE_SERIALIZER_IU;

    public static RegistryObject<RecipeType<Recipe<?>>> UNIVERSAL_RECIPE_TYPE_DELETE;
    public static RegistryObject<IURecipeDeleteSerializer> RECIPE_SERIALIZER_IU_DELETE;

    public static RegistryObject<RecipeType<Recipe<?>>> QUANTUM_QUARRY;
    public static RegistryObject<QuantumQuarrySerializer> RECIPE_SERIALIZER_QUANTUM_QUARRY;
    public static RegistryObject<RecipeType<Recipe<?>>> BODY_RECIPE;
    public static RegistryObject<SpaceBodySerializer> RECIPE_SERIALIZER_BODY_RECIPE;
    public static RegistryObject<RecipeType<Recipe<?>>> SATELLITE_RECIPE;
    public static RegistryObject<SatelliteSerializer> RECIPE_SERIALIZER_SATELLITE_RECIPE;
    public static RegistryObject<RecipeType<Recipe<?>>> PLANET_RECIPE;
    public static RegistryObject<PlanetSerializer> RECIPE_SERIALIZER_PLANET_RECIPE;
    public static RegistryObject<RecipeType<Recipe<?>>> STAR_RECIPE;
    public static RegistryObject<StarSerializer> RECIPE_SERIALIZER_STAR_RECIPE;
    public static RegistryObject<RecipeType<Recipe<?>>> SYSTEM_RECIPE;
    public static RegistryObject<SystemSerializer> RECIPE_SERIALIZER_SYSTEM_RECIPE;
    public static RegistryObject<RecipeType<Recipe<?>>> ASTEROID_RECIPE;
    public static RegistryObject<AsteroidSerializer> RECIPE_SERIALIZER_ASTEROID_RECIPE;
    public static RegistryObject<RecipeType<Recipe<?>>> COLONY_RECIPE;
    public static RegistryObject<ColonySerializer> RECIPE_SERIALIZER_COLONY_RECIPE;
    public static DeferredRegister<ParticleType<?>> PARTICLE_TYPE = DeferredRegister.create(Registries.PARTICLE_TYPE, IUCore.MODID);
    public static DeferredRegister<PoiType> POI_TYPE = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, IUCore.MODID);
    public static DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(Registries.VILLAGER_PROFESSION, IUCore.MODID);
    public static RegistryObject<IndustrialShapelessRecipeSerializer> RECIPE_SERIALIZER_SHAPELESS_RECIPE;
    public static RegistryObject<IndustrialShapedRecipeSerializer> RECIPE_SERIALIZER_SHAPED_RECIPE;
    public static RegistryObject<SmelterSerializer> RECIPE_SERIALIZER_SMELTERY_RECIPE;
    public static RegistryObject<RecipeType<Recipe<?>>> SMELTERY_RECIPE;

    public static void register() {
        ITEMS.register(IUCore.context.getModEventBus());
        BLOCKS.register(IUCore.context.getModEventBus());
        SOUND_EVENTS.register(IUCore.context.getModEventBus());
        RECIPE_SERIALIZER.register(IUCore.context.getModEventBus());
        FLUIDS.register(IUCore.context.getModEventBus());
        FLUID_TYPES.register(IUCore.context.getModEventBus());
        MOB_EFFECT.register(IUCore.context.getModEventBus());
        FEATURES.register(IUCore.context.getModEventBus());
        CONFIGURED_FEATURES.register(IUCore.context.getModEventBus());
        PLACED_FEATURES.register(IUCore.context.getModEventBus());
        BLOCK_ENTITIES.register(IUCore.context.getModEventBus());
        MENU_TYPE.register(IUCore.context.getModEventBus());
        ENTITIES.register(IUCore.context.getModEventBus());
        DAMAGE_TYPE.register(IUCore.context.getModEventBus());
        RECIPE_TYPE.register(IUCore.context.getModEventBus());
        TABS.register(IUCore.context.getModEventBus());
        PARTICLE_TYPE.register(IUCore.context.getModEventBus());
        POI_TYPE.register(IUCore.context.getModEventBus());
        VILLAGER_PROFESSIONS.register(IUCore.context.getModEventBus());
        EffectsRegister.register(PARTICLE_TYPE);
        UNIVERSAL_RECIPE_TYPE = RECIPE_TYPE.register("universal_recipe", () -> new RecipeType<>() {});
        RECIPE_SERIALIZER_IU = RECIPE_SERIALIZER.register("universal_recipe", IURecipeSerializer::new);
        UNIVERSAL_RECIPE_TYPE_DELETE = RECIPE_TYPE.register("universal_recipe_delete", () -> new RecipeType<>() {});
        RECIPE_SERIALIZER_IU_DELETE = RECIPE_SERIALIZER.register("universal_recipe_delete", IURecipeDeleteSerializer::new);
        RECIPE_SERIALIZER_SHAPED_RECIPE= RECIPE_SERIALIZER.register("shaped_recipe", IndustrialShapedRecipeSerializer::new);
        RECIPE_SERIALIZER_SHAPELESS_RECIPE= RECIPE_SERIALIZER.register("shapeless_recipe", IndustrialShapelessRecipeSerializer::new);

        
        QUANTUM_QUARRY = RECIPE_TYPE.register("quantum_quarry", () -> new RecipeType<>() {});
        RECIPE_SERIALIZER_QUANTUM_QUARRY = RECIPE_SERIALIZER.register("quantum_quarry", QuantumQuarrySerializer::new);
        BODY_RECIPE = RECIPE_TYPE.register("body_resource", () -> new RecipeType<>() {});
        RECIPE_SERIALIZER_BODY_RECIPE = RECIPE_SERIALIZER.register("body_resource", SpaceBodySerializer::new);
        SATELLITE_RECIPE = RECIPE_TYPE.register("satellite_add", () -> new RecipeType<>() {});
        RECIPE_SERIALIZER_SATELLITE_RECIPE = RECIPE_SERIALIZER.register("satellite_add", SatelliteSerializer::new);
        PLANET_RECIPE = RECIPE_TYPE.register("planet_add", () -> new RecipeType<>() {});
        RECIPE_SERIALIZER_PLANET_RECIPE = RECIPE_SERIALIZER.register("planet_add", PlanetSerializer::new);
        STAR_RECIPE = RECIPE_TYPE.register("star_add", () -> new RecipeType<>() {});
        RECIPE_SERIALIZER_STAR_RECIPE = RECIPE_SERIALIZER.register("star_add", StarSerializer::new);
        SYSTEM_RECIPE = RECIPE_TYPE.register("system_add", () -> new RecipeType<>() {});
        RECIPE_SERIALIZER_SYSTEM_RECIPE = RECIPE_SERIALIZER.register("system_add", SystemSerializer::new);
        ASTEROID_RECIPE = RECIPE_TYPE.register("asteroid_add", () -> new RecipeType<>() {});
        RECIPE_SERIALIZER_ASTEROID_RECIPE = RECIPE_SERIALIZER.register("asteroid_add", AsteroidSerializer::new);
        COLONY_RECIPE = RECIPE_TYPE.register("colony_resource_add", () -> new RecipeType<>() {});
        RECIPE_SERIALIZER_COLONY_RECIPE = RECIPE_SERIALIZER.register("colony_resource_add", ColonySerializer::new);

        SMELTERY_RECIPE = RECIPE_TYPE.register("smeltery", () -> new RecipeType<>() {});
        RECIPE_SERIALIZER_SMELTERY_RECIPE = RECIPE_SERIALIZER.register("smeltery", () -> new SmelterSerializer());
        TABS.register("iutab",
                () -> IUCore.IUTab);
        TABS.register("moduletab",
                () -> IUCore.ModuleTab);
        TABS.register("itemtab",
                () -> IUCore.ItemTab);
        TABS.register("oretab",
                () -> IUCore.OreTab);
        TABS.register("energytab",
                () -> IUCore.EnergyTab);
        TABS.register("resourcetab",
                () -> IUCore.RecourseTab);
        TABS.register("reactorstab",
                () -> IUCore.ReactorsTab);
        TABS.register("upgradetab",
                () -> IUCore.UpgradeTab);
        TABS.register("elementstab",
                () -> IUCore.ElementsTab);
        TABS.register("reactorsblocktab",
                () -> IUCore.ReactorsBlockTab);
        TABS.register("croptab",
                () -> IUCore.CropsTab);
        TABS.register("beetab",
                () -> IUCore.BeesTab);
        TABS.register("genometab",
                () -> IUCore.GenomeTab);
        TABS.register("spacetab",
                () -> IUCore.SpaceTab);
        TABS.register("fluidtab",
                () -> IUCore.fluidCellTab);
        containerBase = MENU_TYPE.register("containerbase", () -> IForgeMenuType.create((windowId, inv, data) -> {
            CustomPacketBuffer packetBuffer = new CustomPacketBuffer(data);
            try {
                BlockEntity blockEntity = (BlockEntity) DecoderHandler.decode(packetBuffer);
                if (blockEntity instanceof TileEntityInventory)
                    return ((ContainerBase<?>) ((TileEntityInventory) blockEntity).createMenu(windowId, inv, inv.player));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }));
        inventory_container = MENU_TYPE.register("inventory_container", () -> IForgeMenuType.create((windowId, inv, data) -> {
            CustomPacketBuffer packetBuffer = new CustomPacketBuffer(data);
            try {
                byte id = packetBuffer.readByte();
                if (id == 1) {
                    final ItemStack stack = inv.player.getItemInHand(InteractionHand.MAIN_HAND);
                    if (stack.getItem() instanceof IItemStackInventory inventory) {
                        Player player = inv.player;
                        invent = (ItemStackInventory) inventory.getInventory(player, stack);
                        return ((ContainerBase<?>) invent.createMenu(windowId, inv, inv.player));
                    }
                } else if (id == 2) {
                    final ItemStack stack = inv.player.getItemBySlot(EquipmentSlot.LEGS);
                    if (stack.getItem() instanceof IItemStackInventory inventory) {
                        Player player = inv.player;
                        invent = (ItemStackInventory) inventory.getInventory(player, stack);
                        return ((ContainerBase<?>) invent.createMenu(windowId, inv, inv.player));
                    }
                } else if (id == 3) {
                    final ItemStack stack = inv.player.getItemBySlot(EquipmentSlot.CHEST);
                    if (stack.getItem() instanceof IItemStackInventory inventory) {
                        Player player = inv.player;
                        invent = (ItemStackInventory) inventory.getInventory(player, stack);
                        return ((ContainerBase<?>) invent.createMenu(windowId, inv, inv.player));
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return null;
        }));
        Sounds.registerSounds(SOUND_EVENTS);
        registerFluids();
        registerPotions();
        IUItem.entity_nuclear_bomb = ENTITIES.register("nuclear_bomb", () ->
                EntityType.Builder.<EntityNuclearBombPrimed>of(EntityNuclearBombPrimed::new, MobCategory.MISC)
                        .sized(0.98F, 0.98F)
                        .clientTrackingRange(10)
                        .updateInterval(10)
                        .build("nuclear_bomb"));
        IUItem.entity_bee = ENTITIES.register("bee", () -> EntityType.Builder.of(SmallBee::new, MobCategory.CREATURE).sized(0.7F*0.2f, 0.6F*0.2f).clientTrackingRange(8).build("bee"));
        IUItem.combinersolidmatter = new DataBlockEntity<>(BlockCombinerSolid.class);
        IUItem.oiladvrefiner = new DataBlockEntity<>(BlockAdvRefiner.class);
        IUItem.adv_se_generator = new DataBlockEntity<>(BlockAdvSolarEnergy.class);
        IUItem.machines = new DataBlockEntity<>(BlockBaseMachine.class);
        IUItem.basemachine = new DataBlockEntity<>(BlockBaseMachine1.class);
        IUItem.basemachine1 = new DataBlockEntity<>(BlockBaseMachine2.class);
        IUItem.basemachine2 = new DataBlockEntity<>(BlockBaseMachine3.class);

        IUItem.anvil = new DataBlockEntity<>(BlockAnvil.class);
        IUItem.blockmolecular = new DataBlockEntity<>(BlockMolecular.class);
        IUItem.chargepadelectricblock = new DataBlockEntity<>(BlockChargepadStorage.class);
        IUItem.electricblock = new DataBlockEntity<>(BlockEnergyStorage.class);
        IUItem.blockpanel = new DataBlockEntity<>(BlockSolarPanels.class);
        IUItem.machines_base = new DataBlockEntity<>(BlockMoreMachine.class);
        IUItem.simplemachine = new DataBlockEntity<>(BlockSimpleMachine.class);
        IUItem.machines_base1 = new DataBlockEntity<>(BlockMoreMachine1.class);
        IUItem.machines_base2 = new DataBlockEntity<>(BlockMoreMachine2.class);
        IUItem.machines_base3 = new DataBlockEntity<>(BlockMoreMachine3.class);
        IUItem.pho_machine = new DataBlockEntity<>(BlocksPhotonicMachine.class);
        IUItem.blastfurnace = new DataBlockEntity<>(BlockBlastFurnace.class);
        IUItem.mini_smeltery = new DataBlockEntity<>(BlockMiniSmeltery.class);
        IUItem.oilgetter = new DataBlockEntity<>(BlockPetrolQuarry.class);
        IUItem.primalFluidHeater = new DataBlockEntity<>(BlockPrimalFluidHeater.class);
        IUItem.electronics_assembler = new DataBlockEntity<>(BlockElectronicsAssembler.class);
        IUItem.gasChamber = new DataBlockEntity<>(BlockGasChamber.class);
        IUItem.imp_se_generator = new DataBlockEntity<>(BlockImpSolarEnergy.class);
        IUItem.blockSE = new DataBlockEntity<>(BlockSolarEnergy.class);
        IUItem.squeezer = new DataBlockEntity<>(BlockSqueezer.class);
        IUItem.solderingMechanism = new DataBlockEntity<>(BlockSolderingMechanism.class);
        IUItem.solidmatter = new DataBlockEntity<>(BlockSolidMatter.class);
        IUItem.programming_table = new DataBlockEntity<>(BlockPrimalProgrammingTable.class);
        IUItem.primalPolisher = new DataBlockEntity<>(BlockPrimalLaserPolisher.class);
        IUItem.tank = new DataBlockEntity<>(BlockTank.class);
        IUItem.dryer = new DataBlockEntity<>(BlockDryer.class);
        IUItem.primalSiliconCrystal = new DataBlockEntity<>(BlockPrimalSiliconCrystalHandler.class);
        IUItem.blockCompressor = new DataBlockEntity<>(BlockCompressor.class);
        IUItem.cable = new DataBlockEntity<>(BlockCable.class);
        IUItem.coolpipes = new DataBlockEntity<>(BlockCoolPipe.class);
        IUItem.pipes = new DataBlockEntity<>(BlockPipe.class);
        IUItem.heatcold_pipes = new DataBlockEntity<>(BlockHeatColdPipe.class);
        IUItem.scable = new DataBlockEntity<>(BlockSolariumCable.class);
        IUItem.qcable = new DataBlockEntity<>(BlockQuantumCable.class);
        IUItem.amperepipes = new DataBlockEntity<>(BlockAmpereCable.class);
        IUItem.steamPipe = new DataBlockEntity<>(BlockSteamPipe.class);
        IUItem.biopipes = new DataBlockEntity<>(BlockBioPipe.class);
        IUItem.nightpipes = new DataBlockEntity<>(BlockNightCable.class);
        IUItem.radcable_item = new DataBlockEntity<>(BlockRadPipe.class);
        IUItem.expcable = new DataBlockEntity<>(BlockExpCable.class);
        IUItem.universal_cable = new DataBlockEntity<>(BlockUniversalCable.class);
        IUItem.item_pipes = new DataBlockEntity<>(BlockTransportPipe.class);
        IUItem.barrel = new DataBlockEntity<>(BlockBarrel.class);
        IUItem.convertersolidmatter = new DataBlockEntity<>(BlockConverterMatter.class);
        IUItem.fluidIntegrator = new DataBlockEntity<>(BlockPrimalFluidIntegrator.class);
        IUItem.sunnariummaker = new DataBlockEntity<>(BlockSunnariumMaker.class);
        IUItem.sunnariumpanelmaker = new DataBlockEntity<>(BlockSunnariumPanelMaker.class);
        IUItem.primal_pump = new DataBlockEntity<>(BlockPrimalPump.class);
        IUItem.oilquarry = new DataBlockEntity<>(BlockQuarryVein.class);
        IUItem.refractoryFurnace = new DataBlockEntity<>(BlockRefractoryFurnace.class);
        IUItem.upgradeblock = new DataBlockEntity<>(BlockUpgradeBlock.class);
        IUItem.oilrefiner = new DataBlockEntity<>(BlockRefiner.class);
        IUItem.blockdoublemolecular = new DataBlockEntity<>(BlockDoubleMolecularTransfomer.class);
        IUItem.crop = new DataBlockEntity<>(BlockCrop.class);
        IUItem.apiary = new DataBlockEntity<>(BlockApiary.class);
        IUItem.hive = new DataBlockEntity<>(BlockHive.class);
        IUItem.tranformer = new DataBlockEntity<>(BlockTransformer.class);
        IUItem.blockMacerator = new DataBlockEntity<>(BlockMacerator.class);
        IUItem.blockadmin = new DataBlockEntity<>(BlockAdminPanel.class);
        IUItem.chemicalPlant = new DataBlockEntity<>(BlockChemicalPlant.class);
        IUItem.blockwireinsulator = new DataBlockEntity<>(BlockPrimalWireInsulator.class);
        IUItem.cokeoven = new DataBlockEntity<>(BlockCokeOven.class);
        IUItem.adv_cokeoven = new DataBlockEntity<>(BlockAdvCokeOven.class);
        IUItem.cyclotron = new DataBlockEntity<>(BlockCyclotron.class);
        IUItem.strong_anvil = new DataBlockEntity<>(BlockStrongAnvil.class);
        IUItem.blocksintezator = new DataBlockEntity<>(BlockSintezator.class);
        IUItem.earthQuarry = new DataBlockEntity<>(BlockEarthQuarry.class);
        IUItem.geothermalpump = new DataBlockEntity<>(BlockGeothermalPump.class);
        IUItem.gasTurbine = new DataBlockEntity<>(BlockGasTurbine.class);
        IUItem.gas_well = new DataBlockEntity<>(BlockGasWell.class);
        IUItem.smeltery = new DataBlockEntity<>(BlockSmeltery.class);
        IUItem.lightning_rod = new DataBlockEntity<>(BlockLightningRod.class);
        IUItem.steam_boiler = new DataBlockEntity<>(BlockSteamBoiler.class);
        IUItem.windTurbine = new DataBlockEntity<>(BlockWindTurbine.class);
        IUItem.hydroTurbine = new DataBlockEntity<>(BlockHydroTurbine.class);
        IUItem.steam_turbine = new DataBlockEntity<>(BlockSteamTurbine.class);
        IUItem.water_reactors_component = new DataBlockEntity<>(BlockWaterReactors.class);
        IUItem.gas_reactor = new DataBlockEntity<>(BlockGasReactor.class);
        IUItem.graphite_reactor = new DataBlockEntity<>(BlocksGraphiteReactors.class);
        IUItem.heat_reactor = new DataBlockEntity<>(BlockHeatReactor.class);
        IUItem.creativeBlock = new DataBlockEntity<>(BlockCreativeBlocks.class);



        IUItem.crafting_elements = new DataItem<>(ItemCraftingElements.Types.class, ItemCraftingElements.class);
        IUItem.basecircuit = new DataItem<>(ItemBaseCircuit.Types.class, ItemBaseCircuit.class);
        IUItem.casing = new DataItem<>(ItemCasing.Types.class, ItemCasing.class);
        IUItem.nuclear_res = new DataItem<>(ItemNuclearResource.Types.class, ItemNuclearResource.class);
        IUItem.crops = new DataItem<>(ItemCrops.Types.class, ItemCrops.class);
        IUItem.crushed = new DataItem<>(ItemCrushed.Types.class, ItemCrushed.class);
        IUItem.genome_bee = new DataItem<>(ItemBeeGenome.Types.class, ItemBeeGenome.class);
        IUItem.genome_crop = new DataItem<>(ItemCropGenome.Types.class, ItemCropGenome.class);

        IUItem.purifiedcrushed = new DataItem<>(ItemPurifiedCrushed.Types.class, ItemPurifiedCrushed.class);

        IUItem.iuingot = new DataItem<>(ItemIngots.ItemIngotsTypes.class, ItemIngots.class);
        IUItem.nugget = new DataItem<>(ItemNugget.Types.class, ItemNugget.class);
        IUItem.plate = new DataItem<>(ItemPlate.Types.class, ItemPlate.class);
        IUItem.smalldust = new DataItem<>(ItemSmallDust.Types.class, ItemSmallDust.class);
        IUItem.sunnarium = new DataItem<>(ItemSunnarium.Types.class, ItemSunnarium.class);
        IUItem.sunnariumpanel = new DataItem<>(ItemSunnariumPanel.Types.class, ItemSunnariumPanel.class);
        IUItem.paints = new DataItem<>(ItemPaints.Types.class, ItemPaints.class);
        IUItem.spawnermodules = new DataItem<>(ItemSpawnerModules.Types.class, ItemSpawnerModules.class);
        IUItem.module7 = new DataItem<>(ItemAdditionModule.Types.class, ItemAdditionModule.class);
        IUItem.module6 = new DataItem<>(ItemModuleTypePanel.Types.class, ItemModuleTypePanel.class);
        IUItem.module5 = new DataItem<>(ItemModuleType.Types.class, ItemModuleType.class);

        IUItem.basemodules = new DataItem<>(ItemBaseModules.Types.class, ItemBaseModules.class);
        IUItem.iudust = new DataItem<>(ItemDust.ItemDustTypes.class, ItemDust.class);
        IUItem.jarBees = new DataItem<>(ItemJarBees.Types.class, ItemJarBees.class);
        IUItem.doubleplate = new DataItem<>(ItemDoublePlate.ItemDoublePlateTypes.class, ItemDoublePlate.class);
        IUItem.gear = new DataItem<>(ItemGear.Types.class, ItemGear.class);
        IUItem.core = new DataItem<>(ItemCore.Types.class, ItemCore.class);
        IUItem.pellets = new DataItem<>(RadiationPellets.Types.class, RadiationPellets.class);
        IUItem.radiationresources = new DataItem<>(RadiationResources.Types.class, RadiationResources.class);
        IUItem.agricultural_analyzer = new DataSimpleItem<>(new ResourceLocation("tools", "agricultural_analyzer"), ItemAgriculturalAnalyzer::new);

        IUItem.bee_analyzer = new DataSimpleItem<>(new ResourceLocation("tools", "bee_analyzer"), ItemBeeAnalyzer::new);
        IUItem.rotorupgrade_schemes = new DataSimpleItem<>(new ResourceLocation("", "rotorupgrade_schemes"), IUItemBase::new);
        IUItem.spectral_box = new DataSimpleItem<>(new ResourceLocation("", "spectral_box"), IUItemBase::new);
        IUItem.adv_spectral_box = new DataSimpleItem<>(new ResourceLocation("", "adv_spectral_box"), IUItemBase::new);
        IUItem.leadbox = new DataSimpleItem<>(new ResourceLocation("bags", "lead_box"), ItemLeadBox::new);
        IUItem.antiairpollution = new DataSimpleItem<>(new ResourceLocation("", "antiairpollution"), () -> new IUItemBase(IUCore.ModuleTab));
        IUItem.antiairpollution1 = new DataSimpleItem<>(new ResourceLocation("", "antiairpollution1"), () -> (IUItemBase) new IUItemBase(IUCore.ModuleTab));
        IUItem.antisoilpollution = new DataSimpleItem<>(new ResourceLocation("", "antisoilpollution"), () -> (IUItemBase) new IUItemBase(IUCore.ModuleTab));
        IUItem.antisoilpollution1 = new DataSimpleItem<>(new ResourceLocation("", "antisoilpollution1"), () -> (IUItemBase) new IUItemBase(IUCore.ModuleTab));


        IUItem.coal_chunk1 = new DataSimpleItem<>(new ResourceLocation("", "coal_chunk"), IUItemBase::new);
        IUItem.compresscarbon = new DataSimpleItem<>(new ResourceLocation("", "compresscarbon"), IUItemBase::new);
        IUItem.compressAlloy = new DataSimpleItem<>(new ResourceLocation("", "compresscarbonultra"), IUItemBase::new);
        IUItem.photoniy = new DataSimpleItem<>(new ResourceLocation("", "photoniy"), IUItemBase::new);
        IUItem.photoniy_ingot = new DataSimpleItem<>(new ResourceLocation("", "photoniy_ingot"), IUItemBase::new);
        IUItem.compressIridiumplate = new DataSimpleItem<>(new ResourceLocation("", "quantumitems2"), IUItemBase::new);
        IUItem.advQuantumtool = new DataSimpleItem<>(new ResourceLocation("", "quantumitems3"), IUItemBase::new);
        IUItem.canister = new DataSimpleItem<>(new ResourceLocation("tools", "canister"), ItemCanister::new);
        IUItem.pipette = new DataSimpleItem<>(new ResourceLocation("", "pipette"), ItemPipette::new);

        IUItem.radioprotector = new DataSimpleItem<>(new ResourceLocation("tools", "radioprotector"), ItemRadioprotector::new);
        IUItem.expmodule = new DataSimpleItem<>(new ResourceLocation("", "expmodule"), () -> new IUItemBase(IUCore.ModuleTab));
        IUItem.doublecompressIridiumplate = new DataSimpleItem<>(new ResourceLocation("", "quantumitems4"), IUItemBase::new);
        IUItem.nanoBox = new DataSimpleItem<>(new ResourceLocation("", "nanobox"), IUItemBase::new);
        IUItem.module_schedule = new DataSimpleItem<>(new ResourceLocation("", "module_schedule"), IUItemBase::new);
        IUItem.quantumtool = new DataSimpleItem<>(new ResourceLocation("", "quantumitems6"), IUItemBase::new);
        IUItem.advnanobox = new DataSimpleItem<>(new ResourceLocation("", "quantumitems7"), IUItemBase::new);
        IUItem.neutronium = new DataSimpleItem<>(new ResourceLocation("", "neutronium"), IUItemBase::new);
        IUItem.plast = new DataSimpleItem<>(new ResourceLocation("", "plast"), IUItemBase::new);
        IUItem.module_stack = new DataSimpleItem<>(new ResourceLocation("", "module_stack"), () -> new IUItemBase(IUCore.ModuleTab));
        IUItem.module_quickly = new DataSimpleItem<>(new ResourceLocation("", "module_quickly"), () -> new IUItemBase(IUCore.ModuleTab));
        IUItem.module_storage = new DataSimpleItem<>(new ResourceLocation("", "module_storage"), () -> new IUItemBase(IUCore.ModuleTab));
        IUItem.module_infinity_water = new DataSimpleItem<>(new ResourceLocation("", "module_infinity_water"), () -> new IUItemBase(IUCore.ModuleTab));
        IUItem.module_separate = new DataSimpleItem<>(new ResourceLocation("", "module_separate"), () -> new IUItemBase(IUCore.ModuleTab));
        IUItem.plastic_plate = new DataSimpleItem<>(new ResourceLocation("", "plastic_plate"), IUItemBase::new);

        IUItem.doublescrapBox = new DataSimpleItem<>(new ResourceLocation("", "doublescrapbox"), IUItemBase::new);
        IUItem.quarrymodule = new DataSimpleItem<>(new ResourceLocation("", "quarrymodule"), () -> new IUItemBase(IUCore.ModuleTab));
        IUItem.analyzermodule = new DataSimpleItem<>(new ResourceLocation("", "analyzermodule"), () -> new IUItemBase(IUCore.ModuleTab));


        IUItem.componentVent = new DataSimpleItem<>(new ResourceLocation("reactors", "component_vent"), () -> new ItemComponentVent(1, 3));
        IUItem.adv_componentVent = new DataSimpleItem<>(new ResourceLocation("reactors", "adv_component_vent"), () -> new ItemComponentVent(2, 4));
        IUItem.imp_componentVent = new DataSimpleItem<>(new ResourceLocation("reactors", "imp_component_vent"), () -> new ItemComponentVent(3, 5));
        IUItem.per_componentVent = new DataSimpleItem<>(new ResourceLocation("reactors", "per_component_vent"), () -> new ItemComponentVent(4, 6));

        IUItem.proton_energy_coupler = new DataSimpleItem<>(new ResourceLocation("reactors", "proton_energy_coupler"), () -> new ItemEnergyCoupler((int) (3600 * 2.5), 1, 0.05));
        IUItem.adv_proton_energy_coupler = new DataSimpleItem<>(new ResourceLocation("reactors", "adv_proton_energy_coupler"), () -> new ItemEnergyCoupler((int) (7200 * 2.5), 2, 0.1));
        IUItem.imp_proton_energy_coupler = new DataSimpleItem<>(new ResourceLocation("reactors", "imp_proton_energy_coupler"), () -> new ItemEnergyCoupler(10800 * 3, 3, 0.15));
        IUItem.per_proton_energy_coupler = new DataSimpleItem<>(new ResourceLocation("reactors", "per_proton_energy_coupler"), () -> new ItemEnergyCoupler((int) (14400 * 3.5), 4, 0.2));

        IUItem.neutron_protector = new DataSimpleItem<>(new ResourceLocation("reactors", "neutron_protector"), () -> new ItemNeutronProtector(3600 * 4, 1));
        IUItem.adv_neutron_protector = new DataSimpleItem<>(new ResourceLocation("reactors", "adv_neutron_protector"), () -> new ItemNeutronProtector(7200 * 6, 2));
        IUItem.imp_neutron_protector = new DataSimpleItem<>(new ResourceLocation("reactors", "imp_neutron_protector"), () -> new ItemNeutronProtector((int) (10800 * 6), 3));
        IUItem.per_neutron_protector = new DataSimpleItem<>(new ResourceLocation("reactors", "per_neutron_protector"), () -> new ItemNeutronProtector((int) (14400 * 6), 4));

        IUItem.simple_capacitor_item = new DataSimpleItem<>(new ResourceLocation("capacitor", "simple_capacitor"), () -> new ItemCapacitor(0, 0.02, 10000));
        IUItem.adv_capacitor_item = new DataSimpleItem<>(new ResourceLocation("capacitor", "adv_capacitor"), () -> new ItemCapacitor(1, 0.04, 15000));
        IUItem.imp_capacitor_item = new DataSimpleItem<>(new ResourceLocation("capacitor", "imp_capacitor"), () -> new ItemCapacitor(2, 0.08, 20000));
        IUItem.per_capacitor_item = new DataSimpleItem<>(new ResourceLocation("capacitor", "per_capacitor"), () -> new ItemCapacitor(3, 0.12, 30000));

        IUItem.simple_exchanger_item = new DataSimpleItem<>(new ResourceLocation("exchanger", "simple_exchanger"), () -> new ItemExchanger(0, 0.05, 10000));
        IUItem.adv_exchanger_item = new DataSimpleItem<>(new ResourceLocation("exchanger", "adv_exchanger"), () -> new ItemExchanger(1, 0.1, 15000));
        IUItem.imp_exchanger_item = new DataSimpleItem<>(new ResourceLocation("exchanger", "imp_exchanger"), () -> new ItemExchanger(2, 0.15, 20000));
        IUItem.per_exchanger_item = new DataSimpleItem<>(new ResourceLocation("exchanger", "per_exchanger"), () -> new ItemExchanger(3, 0.2, 30000));

        IUItem.protonshard = new DataSimpleItem<>(new ResourceLocation("reactors", "protonshard"), ItemRadioactive::new);
        IUItem.proton = new DataSimpleItem<>(new ResourceLocation("reactors", "proton"), ItemRadioactive::new);
        IUItem.toriy = new DataSimpleItem<>(new ResourceLocation("reactors", "toriy"), ItemRadioactive::new);

        IUItem.capacitor = new DataSimpleItem<>(new ResourceLocation("reactors", "capacitor"), () -> new ItemReactorCapacitor(25000, 1, 4));
        IUItem.adv_capacitor = new DataSimpleItem<>(new ResourceLocation("reactors", "adv_capacitor"), () -> new ItemReactorCapacitor(50000, 2, 6));
        IUItem.imp_capacitor = new DataSimpleItem<>(new ResourceLocation("reactors", "imp_capacitor"), () -> new ItemReactorCapacitor(100000, 3, 8));
        IUItem.per_capacitor = new DataSimpleItem<>(new ResourceLocation("reactors", "per_capacitor"), () -> new ItemReactorCapacitor(200000, 4, 12));
        IUItem.coolant = new DataSimpleItem<>(new ResourceLocation("reactors", "coolant"), () -> new ItemReactorCoolant(1, 100000, 2));
        IUItem.adv_coolant = new DataSimpleItem<>(new ResourceLocation("reactors", "adv_coolant"), () -> new ItemReactorCoolant(2, 250000, 4));
        IUItem.imp_coolant = new DataSimpleItem<>(new ResourceLocation("reactors", "imp_coolant"), () -> new ItemReactorCoolant(3, 500000, 7));


        IUItem.heat_exchange = new DataSimpleItem<>(new ResourceLocation("reactors", "heat_exchange"), () -> new ItemReactorHeatExchanger(2500, 1, 10, 0.8));
        IUItem.adv_heat_exchange = new DataSimpleItem<>(new ResourceLocation("reactors", "adv_heat_exchange"), () -> new ItemReactorHeatExchanger(5000, 2, 12, 0.75));
        IUItem.imp_heat_exchange = new DataSimpleItem<>(new ResourceLocation("reactors", "imp_heat_exchange"), () -> new ItemReactorHeatExchanger(7500, 3, 15, 0.6));
        IUItem.per_heat_exchange = new DataSimpleItem<>(new ResourceLocation("reactors", "per_heat_exchange"), () -> new ItemReactorHeatExchanger(10000, 4, 20, 0.45));


        IUItem.reactor_plate = new DataSimpleItem<>(new ResourceLocation("reactors", "reactor_plate"), () -> new ItemReactorPlate(1, 2));
        IUItem.adv_reactor_plate = new DataSimpleItem<>(new ResourceLocation("reactors", "adv_reactor_plate"), () -> new ItemReactorPlate(2, 1.5));
        IUItem.imp_reactor_plate = new DataSimpleItem<>(new ResourceLocation("reactors", "imp_reactor_plate"), () -> new ItemReactorPlate(3, 1.25));
        IUItem.per_reactor_plate = new DataSimpleItem<>(new ResourceLocation("reactors", "per_reactor_plate"), () -> new ItemReactorPlate(4, 1));


        IUItem.vent = new DataSimpleItem<>(new ResourceLocation("reactors", "vent"), () -> new ItemReactorVent(2500, 1, 8, 0.9, 4));
        IUItem.adv_Vent = new DataSimpleItem<>(new ResourceLocation("reactors", "adv_vent"), () -> new ItemReactorVent(4000, 2, 10, 0.85, 7));
        IUItem.imp_Vent = new DataSimpleItem<>(new ResourceLocation("reactors", "imp_vent"), () -> new ItemReactorVent(5000, 3, 14, 0.8, 11));
        IUItem.per_Vent = new DataSimpleItem<>(new ResourceLocation("reactors", "per_vent"), () -> new ItemReactorVent(6000, 4, 20, 0.75, 15));


        IUItem.fan = new DataSimpleItem<>(new ResourceLocation("fan", "fan"), () -> new ItemsFan(2000, 0, 1, 2));
        IUItem.adv_fan = new DataSimpleItem<>(new ResourceLocation("fan", "adv_fan"), () -> new ItemsFan(5000, 1, 2, 4));
        IUItem.imp_fan = new DataSimpleItem<>(new ResourceLocation("fan", "imp_fan"), () -> new ItemsFan(7500, 2, 4, 9));
        IUItem.per_fan = new DataSimpleItem<>(new ResourceLocation("fan", "per_fan"), () -> new ItemsFan(10000, 3, 6, 15));


        IUItem.impBatChargeCrystal = new DataSimpleItem<>(new ResourceLocation("battery", "itemadvbatchargecrystal"), () -> new ItemBattery(100000000, 32368D, 5, true));
        IUItem.perBatChargeCrystal = new DataSimpleItem<>(new ResourceLocation("battery", "itembatchargecrystal"), () -> new ItemBattery(100000000 * 4, 129472D, 6, true));
        IUItem.AdvlapotronCrystal = new DataSimpleItem<>(new ResourceLocation("battery", "itembatlamacrystal"), () -> new ItemBattery(100000000, 8092.0D, 4, false));

        IUItem.reBattery = new DataSimpleItem<>(new ResourceLocation("battery", "re_battery"), () -> new ItemBattery(100000.0, 100.0, 1));
        IUItem.energy_crystal = new DataSimpleItem<>(new ResourceLocation("battery", "energy_crystal"), () -> new ItemBattery(1000000.0, 2048.0, 3));
        IUItem.lapotron_crystal = new DataSimpleItem<>(new ResourceLocation("battery", "lapotron_crystal"), () -> new ItemBattery(1.0E7, 8092.0, 4));
        IUItem.charging_re_battery = new DataSimpleItem<>(new ResourceLocation("battery", "charging_re_battery"), () -> new ItemBattery(40000.0, 128.0, 1, true));
        IUItem.advanced_charging_re_battery = new DataSimpleItem<>(new ResourceLocation("battery", "advanced_charging_re_battery"), () -> new ItemBattery(400000.0, 1024.0, 2, true));
        IUItem.charging_energy_crystal = new DataSimpleItem<>(new ResourceLocation("battery", "charging_energy_crystal"), () -> new ItemBattery(4000000.0, 8192.0, 3, true));
        IUItem.charging_lapotron_crystal = new DataSimpleItem<>(new ResourceLocation("battery", "charging_lapotron_crystal"), () -> new ItemBattery(4.0E7, 32768.0, 4, true));
        IUItem.advBattery = new DataSimpleItem<>(new ResourceLocation("battery", "advanced_re_battery"), () -> new ItemBattery(10000.0, 256.0, 2));


        IUItem.pump = new DataSimpleItem<>(new ResourceLocation("pumps", "pump"), () -> new ItemsPumps(2000, 0, 1, 2));
        IUItem.adv_pump = new DataSimpleItem<>(new ResourceLocation("pumps", "adv_pump"), () -> new ItemsPumps(5000, 1, 2, 4));
        IUItem.imp_pump = new DataSimpleItem<>(new ResourceLocation("pumps", "imp_pump"), () -> new ItemsPumps(7500, 2, 4, 9));
        IUItem.per_pump = new DataSimpleItem<>(new ResourceLocation("pumps", "per_pump"), () -> new ItemsPumps(10000, 3, 6, 15));

        IUItem.anode = new DataSimpleItem<>(new ResourceLocation("chemistry", "anode"), () -> new ItemChemistry(20000));
        IUItem.cathode = new DataSimpleItem<>(new ResourceLocation("chemistry", "cathode"), () -> new ItemChemistry(20000));
        IUItem.adv_anode = new DataSimpleItem<>(new ResourceLocation("chemistry", "adv_anode"), () -> new ItemChemistry(100000));
        IUItem.adv_cathode = new DataSimpleItem<>(new ResourceLocation("chemistry", "adv_cathode"), () -> new ItemChemistry(100000));

        IUItem.reactorprotonSimple = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorprotonsimple"), () -> new ItemBaseRod(1,
                95, 6, 3
        ));
        IUItem.reactorprotonDual = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorprotondual"), () -> new ItemBaseRod(2,
                190, 6, 3
        ));
        IUItem.reactorprotonQuad = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorprotonquad"), () -> new ItemBaseRod(4,
                380, 6, 3
        ));


        //
        IUItem.reactortoriySimple = new DataSimpleItem<>(new ResourceLocation("reactors", "reactortoriysimple"), () -> new ItemBaseRod(1,
                50, 3, 2
        ));
        IUItem.reactortoriyDual = new DataSimpleItem<>(new ResourceLocation("reactors", "reactortoriydual"), () -> new ItemBaseRod(2,
                100, 3, 2
        ));
        IUItem.reactortoriyQuad = new DataSimpleItem<>(new ResourceLocation("reactors", "reactortoriyquad"), () -> new ItemBaseRod(4,
                200, 3, 2
        ));

        //
//
        IUItem.reactoramericiumSimple = new DataSimpleItem<>(new ResourceLocation("reactors", "reactoramericiumsimple"), () -> new ItemBaseRod(1,
                80, (float) 4.5D, 2
        ));
        IUItem.reactoramericiumDual = new DataSimpleItem<>(new ResourceLocation("reactors", "reactoramericiumdual"), () -> new ItemBaseRod(2,
                160, (float) 4.5D, 2
        ));
        IUItem.reactoramericiumQuad = new DataSimpleItem<>(new ResourceLocation("reactors", "reactoramericiumquad"), () -> new ItemBaseRod(4,
                320, (float) 4.5D, 2
        ));

        //
        //
        IUItem.reactorneptuniumSimple = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorneptuniumsimple"), () -> new ItemBaseRod(1,
                65, (float) 3.5D, 2
        ));
        IUItem.reactorneptuniumDual = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorneptuniumdual"), () -> new ItemBaseRod(2,
                130, (float) 3.5D, 2
        ));
        IUItem.reactorneptuniumQuad = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorneptuniumquad"), () -> new ItemBaseRod(4,
                260, (float) 3.5D, 2
        ));

        //
        IUItem.reactorcuriumSimple = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorcuriumsimple"), () -> new ItemBaseRod(1,
                100, (float) 9.5D, 3
        ));
        IUItem.reactorcuriumDual = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorcuriumdual"), () -> new ItemBaseRod(2,
                200, (float) 9.5D, 3
        ));
        IUItem.reactorcuriumQuad = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorcuriumquad"), () -> new ItemBaseRod(4,
                400, (float) 9.5D, 3
        ));

        //
        //
        IUItem.reactorcaliforniaSimple = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorcaliforniasimple"), () -> new ItemBaseRod(1,
                120, (float) 18D, 3
        ));
        IUItem.reactorcaliforniaDual = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorcaliforniadual"), () -> new ItemBaseRod(2,
                240, (float) 18D, 3
        ));
        IUItem.reactorcaliforniaQuad = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorcaliforniaquad"), () -> new ItemBaseRod(4,
                480, (float) 18D, 3
        ));

        //
        //
        IUItem.reactorfermiumSimple = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorfermiumsimple"), () -> new ItemBaseRod(1,
                230, (float) 26D, 4
        ));
        IUItem.reactorfermiumDual = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorfermiumdual"), () -> new ItemBaseRod(2,
                460, (float) 26D, 4
        ));
        IUItem.reactorfermiumQuad = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorfermiumquad"), () -> new ItemBaseRod(4,
                920, (float) 26D, 4
        ));

        //
        //
        IUItem.reactormendeleviumSimple = new DataSimpleItem<>(new ResourceLocation("reactors", "reactormendeleviumsimple"), () -> new ItemBaseRod(1,
                260, (float) 36, 4
        ));
        IUItem.reactormendeleviumDual = new DataSimpleItem<>(new ResourceLocation("reactors", "reactormendeleviumdual"), () -> new ItemBaseRod(2,
                520, (float) 36, 4
        ));
        IUItem.reactormendeleviumQuad = new DataSimpleItem<>(new ResourceLocation("reactors", "reactormendeleviumquad"), () -> new ItemBaseRod(4,
                1050, (float) 36, 4
        ));
        //
        IUItem.reactornobeliumSimple = new DataSimpleItem<>(new ResourceLocation("reactors", "reactornobeliumsimple"), () -> new ItemBaseRod(1,
                285, (float) 49, 4
        ));
        IUItem.reactornobeliumDual = new DataSimpleItem<>(new ResourceLocation("reactors", "reactornobeliumdual"), () -> new ItemBaseRod(2,
                590, (float) 49, 4
        ));
        IUItem.reactornobeliumQuad = new DataSimpleItem<>(new ResourceLocation("reactors", "reactornobeliumquad"), () -> new ItemBaseRod(4,
                1200, (float) 49, 4
        ));

        //
        //
        IUItem.reactorlawrenciumSimple = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorlawrenciumsimple"), () -> new ItemBaseRod(1,
                300, (float) 60, 4
        ));
        IUItem.reactorlawrenciumDual = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorlawrenciumdual"), () -> new ItemBaseRod(2,
                620, (float) 60, 4
        ));
        IUItem.reactorlawrenciumQuad = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorlawrenciumquad"), () -> new ItemBaseRod(4,
                1300, (float) 60, 4
        ));
        //
        IUItem.reactorberkeliumSimple = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorberkeliumsimple"), () -> new ItemBaseRod(1,
                150, (float) 20D, 4
        ));
        IUItem.reactorberkeliumDual = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorberkeliumdual"), () -> new ItemBaseRod(2,
                300, (float) 20D, 4
        ));
        IUItem.reactorberkeliumQuad = new DataSimpleItem<>(new ResourceLocation("reactors", "reactorberkeliumquad"), () -> new ItemBaseRod(4,
                600, (float) 20D, 4
        ));

        //
        //
        IUItem.reactoreinsteiniumSimple = new DataSimpleItem<>(new ResourceLocation("reactors", "reactoreinsteiniumsimple"), () -> new ItemBaseRod(1,
                180, (float) 23D, 4
        ));
        IUItem.reactoreinsteiniumDual = new DataSimpleItem<>(new ResourceLocation("reactors", "reactoreinsteiniumdual"), () -> new ItemBaseRod(2,
                360, (float) 23D, 4
        ));
        IUItem.reactoreinsteiniumQuad = new DataSimpleItem<>(new ResourceLocation("reactors", "reactoreinsteiniumquad"), () -> new ItemBaseRod(4,
                720, (float) 23D, 4
        ));

        //
        //
        IUItem.reactoruran233Simple = new DataSimpleItem<>(new ResourceLocation("reactors", "reactoruran233simple"), () -> new ItemBaseRod(1,
                35, (float) 3D, 1
        ));
        IUItem.reactoruran233Dual = new DataSimpleItem<>(new ResourceLocation("reactors", "reactoruran233dual"), () -> new ItemBaseRod(2,
                70, (float) 3D, 1
        ));
        IUItem.reactoruran233Quad = new DataSimpleItem<>(new ResourceLocation("reactors", "reactoruran233quad"), () -> new ItemBaseRod(4,
                140, (float) 3D, 1
        ));

        IUItem.uranium_fuel_rod = new DataSimpleItem<>(new ResourceLocation("reactors", "uranium_fuel_rod"), () -> new ItemBaseRod(1,
                25, (float) 1.5, 1
        ));
        IUItem.dual_uranium_fuel_rod = new DataSimpleItem<>(new ResourceLocation("reactors", "dual_uranium_fuel_rod"), () -> new ItemBaseRod(2,
                50, (float) 1.5, 1
        ));
        IUItem.quad_uranium_fuel_rod = new DataSimpleItem<>(new ResourceLocation("reactors", "quad_uranium_fuel_rod"), () -> new ItemBaseRod(4,
                100, (float) 1.5, 1
        ));
        IUItem.mox_fuel_rod = new DataSimpleItem<>(new ResourceLocation("reactors", "mox_fuel_rod"), () -> new ItemBaseRod(1,
                40, (float) 3.5, 1
        ));
        IUItem.dual_mox_fuel_rod = new DataSimpleItem<>(new ResourceLocation("reactors", "dual_mox_fuel_rod"), () -> new ItemBaseRod(2,
                80, (float) 3.5, 1
        ));
        IUItem.quad_mox_fuel_rod = new DataSimpleItem<>(new ResourceLocation("reactors", "quad_mox_fuel_rod"), () -> new ItemBaseRod(4,
                160, (float) 3.5, 1
        ));

        IUItem.frequency_transmitter = new DataSimpleItem<>(new ResourceLocation("tools", "frequency_transmitter"), ItemFrequencyTransmitter::new);

        IUItem.neutroniumingot = new DataSimpleItem<>(new ResourceLocation("", "neutroniumingot"), IUItemBase::new);
        IUItem.connect_item = new DataSimpleItem<>(new ResourceLocation("", "connect_item"), IUItemBase::new);
        IUItem.upgrademodule = new DataItem<>(ItemUpgradeModule.Types.class, ItemUpgradeModule.class);
        IUItem.module = new DataItem<>(com.denfop.items.ItemUpgradeModule.Types.class, com.denfop.items.ItemUpgradeModule.class);
        IUItem.module9 = new DataItem<>(ItemQuarryModule.CraftingTypes.class, ItemQuarryModule.class);
        IUItem.fluidCell = new DataSimpleItem<>(new ResourceLocation("itemcell", "itemcell"), ItemFluidCell::new);
        IUItem.smallFluidCell = new DataSimpleItem<>(new ResourceLocation("itemcell", "smallfluidcell"), ItemSmallFluidCell::new);
        IUItem.reinforcedFluidCell = new DataSimpleItem<>(new ResourceLocation("itemcell", "reinforcedfluidcell"), ItemReinforcedFluidCell::new);
        IUItem.sprayer = new DataSimpleItem<>(new ResourceLocation("tools", "foam_sprayer"), ItemSprayer::new);
        IUItem.latexPipette = new DataSimpleItem<>(new ResourceLocation("tools", "latex_pipette"), ItemLatexPipette::new);


        IUItem.ore = new DataBlock<>(BlockOre.Type.class, BlockOre.class, ItemBlockOre.class);
        IUItem.ore2 = new DataBlock<>(BlockOres2.Type.class, BlockOres2.class, ItemBlockOre2.class);
        IUItem.ore3 = new DataBlock<>(BlockOres3.Type.class, BlockOres3.class, ItemBlockOre3.class);
        IUItem.classic_ore = new DataBlock<>(BlockClassicOre.Type.class, BlockClassicOre.class, ItemBlockClassicOre.class);
        IUItem.toriyore = new DataBlock<>(BlockThoriumOre.Type.class, BlockThoriumOre.class, ItemBlockThoriumOre.class);
        IUItem.mineral = new DataBlock<>(BlockMineral.Type.class, BlockMineral.class, ItemBlockMineral.class);
        IUItem.apatite = new DataBlock<>(BlockApatite.Type.class, BlockApatite.class, ItemBlockApatite.class);
        IUItem.humus = new DataBlock<>(BlockHumus.Type.class, BlockHumus.class, ItemBlockHumus.class);
        IUItem.heavyore = new DataBlock<>(BlockHeavyOre.Type.class, BlockHeavyOre.class, ItemBlockHeavyOre.class);
        IUItem.blockResource = new DataBlock<>(BlockResource.Type.class, BlockResource.class, ItemBlockResource.class);
        IUItem.blockRubberWoods = new DataBlock<>(BlockPlanksRubberWood.Type.class, BlockPlanksRubberWood.class, ItemBlockRubberPlanks.class);
        IUItem.rubWood = new DataMultiBlock<>(BlockRubWood.RubberWoodState.class, BlockRubWood.class, ItemBlockRubWood.class);
        IUItem.swampRubWood = new DataMultiBlock<>(BlockSwampRubWood.RubberWoodState.class, BlockSwampRubWood.class, ItemBlockSwampRubWood.class);
        IUItem.tropicalRubWood = new DataMultiBlock<>(BlockTropicalRubWood.RubberWoodState.class, BlockTropicalRubWood.class, ItemBlockTropicalRubWood.class);
        IUItem.rubberSapling = new DataSimpleBlock<>(RubberSapling.class, ItemBlockRubberSapling.class, "sapling", "rubber_sapling");
        IUItem.foam = new DataBlock<>(BlockFoam.FoamType.class, BlockFoam.class, ItemBlockFoam.class);
        IUItem.blockdeposits = new DataBlock<>(BlockDeposits.Type.class, BlockDeposits.class, ItemBlockDeposits.class);
        IUItem.blockdeposits1 = new DataBlock<>(BlockDeposits1.Type.class, BlockDeposits1.class, ItemBlockDeposits1.class);
        IUItem.oilblock = new DataBlock<>(BlockOil.Type.class, BlockOil.class, ItemBlockOil.class);
        IUItem.gasBlock = new DataBlock<>(BlockGas.Type.class, BlockGas.class, ItemBlockGas.class);
        IUItem.blockdeposits2 = new DataBlock<>(BlockDeposits2.Type.class, BlockDeposits2.class, ItemBlockDeposits2.class);
        IUItem.preciousore = new DataBlock<>(BlockPreciousOre.Type.class, BlockPreciousOre.class, ItemBlockPreciousOre.class);
        IUItem.alloysblock = new DataBlock<>(BlocksAlloy.Type.class, BlocksAlloy.class, ItemBlockAlloy.class);
        IUItem.basalts = new DataBlock<>(BlockBasalts.Type.class, BlockBasalts.class, ItemBlockBasalts.class);
        IUItem.preciousblock = new DataBlock<>(BlockPrecious.Type.class, BlockPrecious.class, ItemBlockPrecious.class);
        IUItem.space_ore = new DataBlock<>(BlockSpace.Type.class, BlockSpace.class, ItemBlockSpace.class);
        IUItem.space_ore1 = new DataBlock<>(BlockSpace1.Type.class, BlockSpace1.class, ItemBlockSpace1.class);
        IUItem.space_ore2 = new DataBlock<>(BlockSpace2.Type.class, BlockSpace2.class, ItemBlockSpace2.class);
        IUItem.space_ore3 = new DataBlock<>(BlockSpace3.Type.class, BlockSpace3.class, ItemBlockSpace3.class);
        IUItem.nuclear_bomb = new DataBlock<>(BlockNuclearBomb.Type.class, BlockNuclearBomb.class, ItemBlockNuclearBomb.class);
        IUItem.space_stone = new DataBlock<>(BlockSpaceStone.Type.class, BlockSpaceStone.class, ItemBlockSpaceStone.class);
        IUItem.space_stone1 = new DataBlock<>(BlockSpaceStone1.Type.class, BlockSpaceStone1.class, ItemBlockSpaceStone1.class);
        IUItem.space_cobblestone = new DataBlock<>(BlockSpaceCobbleStone.Type.class, BlockSpaceCobbleStone.class, ItemBlockSpaceCobbleStone.class);
        IUItem.space_cobblestone1 = new DataBlock<>(BlockSpaceCobbleStone1.Type.class, BlockSpaceCobbleStone1.class, ItemBlockSpaceCobbleStone1.class);

        IUItem.rawsBlock = new DataBlock<>(BlockRaws.Type.class, BlockRaws.class, ItemBlockRaws.class);


        IUItem.basaltheavyore = new DataBlock<>(BlockBasaltHeavyOre.Type.class, BlockBasaltHeavyOre.class, ItemBlockBasaltHeavyOre.class);
        IUItem.basaltheavyore1 = new DataBlock<>(BlockBasaltHeavyOre1.Type.class, BlockBasaltHeavyOre1.class, ItemBlockBasaltHeavyOre1.class);
        IUItem.alloysblock1 = new DataBlock<>(BlocksAlloy1.Type.class, BlocksAlloy1.class, ItemBlockAlloy1.class);
        IUItem.block = new DataBlock<>(BlocksIngot.Type.class, BlocksIngot.class, ItemBlockIngot.class);
        IUItem.block1 = new DataBlock<>(BlockIngots1.Type.class, BlockIngots1.class, ItemBlockIngot1.class);
        IUItem.block2 = new DataBlock<>(BlockIngots2.Type.class, BlockIngots2.class, ItemBlockIngot2.class);
        IUItem.leaves = new DataSimpleBlock<>(RubberLeaves.class, ItemBlockLeaves.class, "leaves", "leaves");
        IUItem.radiationore = new DataBlock<>(BlocksRadiationOre.Type.class, BlocksRadiationOre.class, ItemBlockRadiationOre.class);
        IUItem.glass = new DataBlock<>(BlockTexGlass.Type.class, BlockTexGlass.class, ItemBlockTexGlass.class);

        IUItem.coolupgrade = new DataItem<>(ItemCoolingUpgrade.Types.class, ItemCoolingUpgrade.class);
        IUItem.autoheater = new DataSimpleItem<>(new ResourceLocation("", "autoheater"), () -> new IUItemBase(IUCore.ModuleTab));
        IUItem.upgrade_speed_creation = new DataSimpleItem<>(new ResourceLocation("", "upgrade_speed_creation"), () -> new IUItemBase(IUCore.ModuleTab));

        IUItem.treetap = new DataSimpleItem<>(new ResourceLocation("tools", "treetap"), ItemTreetap::new);

        IUItem.recipe_schedule = new DataSimpleItem<>(new ResourceLocation("", "recipe_schedule"), ItemRecipeSchedule::new);
        IUItem.magnet = new DataSimpleItem<>(new ResourceLocation("energy", "magnet"), () -> new ItemMagnet(100000, 5000, 4, 7));
        IUItem.impmagnet = new DataSimpleItem<>(new ResourceLocation("energy", "impmagnet"), () -> new ItemMagnet(200000, 7500, 5, 11));
        IUItem.electric_treetap = new DataSimpleItem<>(new ResourceLocation("energy", "electric_treetap"), ItemTreetapEnergy::new);
        IUItem.electric_wrench = new DataSimpleItem<>(new ResourceLocation("energy", "electric_wrench"), ItemToolWrenchEnergy::new);
        IUItem.electric_hoe = new DataSimpleItem<>(new ResourceLocation("energy", "electric_hoe"), ItemEnergyToolHoe::new);
        IUItem.wrench = new DataSimpleItem<>(new ResourceLocation("energy", "wrench"), ItemToolWrench::new);
        IUItem.windmeter = new DataSimpleItem<>(new ResourceLocation("energy", "wind_meter"), ItemWindMeter::new);
        IUItem.itemiu = new DataItem<>(ItemIUCrafring.Types.class, ItemIUCrafring.class);
        IUItem.rawMetals = new DataItem<>(ItemRawMetals.Types.class, ItemRawMetals.class);
        IUItem.rawIngot = new DataItem<>(ItemRawIngot.Types.class, ItemRawIngot.class);
        IUItem.radiationModule = new DataItem<>(ItemReactorModules.Types.class, ItemReactorModules.class);
        IUItem.stik = new DataItem<>(ItemSticks.Types.class, ItemSticks.class);
        IUItem.verysmalldust = new DataItem<>(ItemVerySmallDust.Types.class, ItemVerySmallDust.class);
        IUItem.corewater = new DataItem<>(ItemCoreWater.Types.class, ItemCoreWater.class);
        IUItem.corewind = new DataItem<>(ItemCoreWind.Types.class, ItemCoreWind.class);
        IUItem.crystalMemory = new DataSimpleItem<>(new ResourceLocation("", "crystal_memory"), ItemCrystalMemory::new);
        IUItem.lens = new DataItem<>(ItemLens.Types.class, ItemLens.class);
        IUItem.spaceItem = new DataItem<>(ItemSpace.Types.class, ItemSpace.class);
        IUItem.rotors_upgrade = new DataItem<>(ItemRotorsUpgrade.Types.class, ItemRotorsUpgrade.class);
        IUItem.spaceupgrademodule_schedule = new DataSimpleItem<>(new ResourceLocation("", "spaceupgrademodule_schedule"), () -> new IUItemBase(IUCore.ModuleTab));
        IUItem.colonial_building = new DataItem<>(ItemColonialBuilding.Types.class, ItemColonialBuilding.class);
        IUItem.research_lens = new DataItem<>(ItemResearchLens.Types.class, ItemResearchLens.class);
        IUItem.spaceupgrademodule = new DataItem<>(ItemSpaceUpgradeModule.Types.class, ItemSpaceUpgradeModule.class);
        IUItem.water_rotors_upgrade = new DataItem<>(ItemWaterRotorsUpgrade.Types.class, ItemWaterRotorsUpgrade.class);
        IUItem.excitednucleus = new DataItem<>(ItemExcitedNucleus.Types.class, ItemExcitedNucleus.class);
        IUItem.water_rod = new DataItem<>(ItemWaterRod.Types.class, ItemWaterRod.class);
        IUItem.relocator = new DataSimpleItem<>(new ResourceLocation("energy", "relocator"), () -> new ItemRelocator());
        IUItem.rocket = new DataSimpleItem<>(new ResourceLocation("rover", "rocket"), () -> new ItemRover("rocket", 10000, EnumRoversLevel.ONE, EnumTypeRovers.ROCKET, 2, 500000, 2048,
                EnumRoversLevelFluid.ONE, 2));
        IUItem.adv_rocket = new DataSimpleItem<>(new ResourceLocation("rover", "adv_rocket"), () -> new ItemRover("adv_rocket", 20000, EnumRoversLevel.TWO, EnumTypeRovers.ROCKET, 3, 1000000, 4096,
                EnumRoversLevelFluid.TWO, 3.5));
        IUItem.imp_rocket = new DataSimpleItem<>(new ResourceLocation("rover", "imp_rocket"), () -> new ItemRover("imp_rocket", 30000, EnumRoversLevel.THREE, EnumTypeRovers.ROCKET, 4, 2000000, 8192,
                EnumRoversLevelFluid.THREE, 5));
        IUItem.per_rocket = new DataSimpleItem<>(new ResourceLocation("rover", "per_rocket"), () -> new ItemRover("per_rocket", 40000, EnumRoversLevel.FOUR, EnumTypeRovers.ROCKET, 5, 5000000, 16384,
                EnumRoversLevelFluid.FOUR, 7.5));
        IUItem.probe = new DataSimpleItem<>(new ResourceLocation("rover", "probe"), () -> new ItemRover("probe", 10000, EnumRoversLevel.ONE, EnumTypeRovers.PROBE, 2, 500000, 2048,
                EnumRoversLevelFluid.ONE, 1.35));
        IUItem.adv_probe = new DataSimpleItem<>(new ResourceLocation("rover", "adv_probe"), () -> new ItemRover("adv_probe", 20000, EnumRoversLevel.TWO, EnumTypeRovers.PROBE, 3, 1000000, 4096,
                EnumRoversLevelFluid.TWO, 2.25));
        IUItem.imp_probe = new DataSimpleItem<>(new ResourceLocation("rover", "imp_probe"), () -> new ItemRover("imp_probe", 30000, EnumRoversLevel.THREE, EnumTypeRovers.PROBE, 4, 2000000, 8192,
                EnumRoversLevelFluid.THREE, 3));
        IUItem.per_probe = new DataSimpleItem<>(new ResourceLocation("rover", "per_probe"), () -> new ItemRover("per_probe", 40000, EnumRoversLevel.FOUR, EnumTypeRovers.PROBE, 5, 5000000, 16384,
                EnumRoversLevelFluid.FOUR, 4));
        IUItem.book = new DataSimpleItem<>(new ResourceLocation("book", "guide_book"), () -> new ItemBook("guide_book"));

        IUItem.rover = new DataSimpleItem<>(new ResourceLocation("rover", "rover"), () -> new ItemRover("rover", 10000, EnumRoversLevel.ONE, EnumTypeRovers.ROVERS, 2, 500000, 2048,
                EnumRoversLevelFluid.ONE, 1));
        IUItem.adv_rover = new DataSimpleItem<>(new ResourceLocation("rover", "adv_rover"), () -> new ItemRover("adv_rover", 20000, EnumRoversLevel.TWO, EnumTypeRovers.ROVERS, 3, 1000000, 4096,
                EnumRoversLevelFluid.TWO, 1.5));
        IUItem.imp_rover = new DataSimpleItem<>(new ResourceLocation("rover", "imp_rover"), () -> new ItemRover("imp_rover", 30000, EnumRoversLevel.THREE, EnumTypeRovers.ROVERS, 4, 2000000, 8192,
                EnumRoversLevelFluid.THREE, 2));
        IUItem.per_rover = new DataSimpleItem<>(new ResourceLocation("rover", "per_rover"), () -> new ItemRover("per_rover", 40000, EnumRoversLevel.FOUR, EnumTypeRovers.ROVERS, 5, 5000000, 16384,
                EnumRoversLevelFluid.FOUR, 3));
        IUItem.satellite = new DataSimpleItem<>(new ResourceLocation("rover", "satellite"), () -> new ItemRover("satellite", 10000, EnumRoversLevel.ONE, EnumTypeRovers.SATELLITE, 2, 500000, 2048,
                EnumRoversLevelFluid.ONE, 1.65));
        IUItem.adv_satellite = new DataSimpleItem<>(new ResourceLocation("rover", "adv_satellite"), () -> new ItemRover("adv_satellite", 20000, EnumRoversLevel.TWO, EnumTypeRovers.SATELLITE, 3, 1000000, 4096,
                EnumRoversLevelFluid.TWO, 3.1));
        IUItem.imp_satellite = new DataSimpleItem<>(new ResourceLocation("rover", "imp_satellite"), () -> new ItemRover("imp_satellite", 30000, EnumRoversLevel.THREE, EnumTypeRovers.SATELLITE, 4, 2000000, 8192,
                EnumRoversLevelFluid.THREE, 4));
        IUItem.per_satellite = new DataSimpleItem<>(new ResourceLocation("rover", "per_satellite"), () -> new ItemRover("per_satellite", 40000, EnumRoversLevel.FOUR, EnumTypeRovers.SATELLITE, 5, 5000000, 16384,
                EnumRoversLevelFluid.FOUR, 6));
        IUItem.cutter = new DataSimpleItem<>(new ResourceLocation("", "cutter"), ItemToolCutter::new);
        IUItem.ForgeHammer = new DataSimpleItem<>(new ResourceLocation("", "forge_hammer"), () -> new ItemToolHammer(80));
        IUItem.ObsidianForgeHammer = new DataSimpleItem<>(new ResourceLocation("", "obsidian_hammer"), () -> new ItemToolHammer(240));
        IUItem.solderingIron = new DataSimpleItem<>(new ResourceLocation("", "solderingiron"), () -> new ItemToolCrafting(1500));
        IUItem.laser = new DataSimpleItem<>(new ResourceLocation("", "laser"), () -> new ItemToolCrafting(60));
        IUItem.matter = new DataItem<>(ItemSolidMatter.Types.class, ItemSolidMatter.class);
        IUItem.windrod = new DataItem<>(ItemWindRod.Types.class, ItemWindRod.class);
        IUItem.preciousgem = new DataItem<>(ItemPreciousGem.Types.class, ItemPreciousGem.class);
        IUItem.photonglass = new DataItem<>(ItemPhotoniumGlass.Types.class, ItemPhotoniumGlass.class);
        IUItem.solar_day_glass = new DataItem<>(ItemDaySolarPanelGlass.Types.class, ItemDaySolarPanelGlass.class);
        IUItem.solar_night_glass = new DataItem<>(ItemNightSolarPanelGlass.Types.class, ItemNightSolarPanelGlass.class);
        IUItem.solar_night_day_glass = new DataItem<>(ItemDayNightSolarPanelGlass.Types.class, ItemDayNightSolarPanelGlass.class);
        IUItem.solar_battery = new DataItem<>(ItemBatterySolarPanel.Types.class, ItemBatterySolarPanel.class);
        IUItem.solar_output = new DataItem<>(ItemOutputSolarPanel.Types.class, ItemOutputSolarPanel.class);
        IUItem.alloyscasing = new DataItem<>(ItemAlloysCasing.Types.class, ItemAlloysCasing.class);
        IUItem.alloysdoubleplate = new DataItem<>(ItemAlloysDoublePlate.Type.class, ItemAlloysDoublePlate.class);
        IUItem.alloysdust = new DataItem<>(ItemAlloysDust.Type.class, ItemAlloysDust.class);
        IUItem.alloysingot = new DataItem<>(ItemAlloysIngot.Type.class, ItemAlloysIngot.class);
        IUItem.alloygear = new DataItem<>(ItemAlloysGear.Types.class, ItemAlloysGear.class);
        IUItem.alloysnugget = new DataItem<>(ItemAlloysNugget.Types.class, ItemAlloysNugget.class);
        IUItem.alloysplate = new DataItem<>(ItemAlloysPlate.Types.class, ItemAlloysPlate.class);
        IUItem.frame = new DataItem<>(ItemFrame.Types.class, ItemFrame.class);
        IUItem.entitymodules = new DataItem<>(ItemEntityModule.Types.class, ItemEntityModule.class);
        IUItem.purifier = new DataSimpleItem<>(new ResourceLocation("energy", "purifier"), () -> new ItemPurifier(100000, 1000, 3));
        IUItem.gasSensor = new DataSimpleItem<>(new ResourceLocation("tools", "gas_sensor"), ItemGasSensor::new);
        IUItem.facadeItem = new DataSimpleItem<>(new ResourceLocation("", "facadeitem"), ItemFacadeItem::new);
        IUItem.efReader = new DataSimpleItem<>(new ResourceLocation("ef", "reader"), ItemEFReader::new);
        IUItem.rawLatex = new DataSimpleItem<>(new ResourceLocation("", "raw_latex"), IUItemBase::new);
        IUItem.net = new DataSimpleItem<>(new ResourceLocation("energy", "net"), ItemNet::new);
        IUItem.molot = new DataSimpleItem<>(new ResourceLocation("energy", "molot"), ItemHammer::new);
        IUItem.nanosaber = new DataSimpleItem<>(new ResourceLocation("energy_tools", "nano_saber"), () -> new ItemNanoSaber(160000, 500, 3, 19, 4));
        IUItem.quantumSaber = new DataSimpleItem<>(new ResourceLocation("energy_tools", "itemnanosaber1"), () -> new ItemQuantumSaber(200000, 15000, 4, 40, 8));
        IUItem.spectralSaber = new DataSimpleItem<>(new ResourceLocation("energy_tools", "itemnanosaber"), () -> new ItemSpectralSaber(600000, 40000, 5, 60, 12));
        IUItem.katana = new DataSimpleItem<>(new ResourceLocation("energy_tools", "katana"), () -> new ItemKatana());
        IUItem.bags = new DataSimpleItem<>(new ResourceLocation("bags", "iu_bags"), () -> new ItemEnergyBags(27, 50000, 500));
        IUItem.adv_bags = new DataSimpleItem<>(new ResourceLocation("bags", "adv_iu_bags"), () -> new ItemEnergyBags(45, 75000, 750));
        IUItem.imp_bags = new DataSimpleItem<>(new ResourceLocation("bags", "imp_iu_bags"), () -> new ItemEnergyBags(63, 100000, 1000));
        IUItem.booze_mug = new DataSimpleItem<>(new ResourceLocation("", "booze_mug"), ItemBooze::new);
        IUItem.suBattery = new DataSimpleItem<>(new ResourceLocation("", "single_use_battery"), () -> new ItemBatterySU(1200, 1));
        IUItem.UpgradeKit = new DataItem<>(ItemUpgradeKit.Types.class, ItemUpgradeKit.class);

        IUItem.veinsencor = new DataItem<>(ItemVeinSensor.Types.class, ItemVeinSensor.class);
        IUItem.upgradepanelkit = new DataItem<>(ItemUpgradePanelKit.Types.class, ItemUpgradePanelKit.class);
        IUItem.machinekit = new DataItem<>(ItemUpgradeMachinesKit.Types.class, ItemUpgradeMachinesKit.class);
        IUItem.steelHammer = new DataSimpleItem<>(new ResourceLocation("energy", "steel_hammer"), ItemSteelHammer::new);
        IUItem.ironHammer = new DataSimpleItem<>(new ResourceLocation("energy", "iron_hammer"), ItemIronHammer::new);
        IUItem.double_molecular = new DataSimpleItem<>(new ResourceLocation("", "double_molecular"), () -> new IUItemBase(IUCore.ModuleTab));
        IUItem.quad_molecular = new DataSimpleItem<>(new ResourceLocation("", "quad_molecular"), () -> new IUItemBase(IUCore.ModuleTab));
        IUItem.bee_frame_template = new DataSimpleItem<>(new ResourceLocation("", "bee_frame_template"), IUItemBase::new);
        IUItem.adv_bee_frame_template = new DataSimpleItem<>(new ResourceLocation("", "adv_bee_frame_template"), IUItemBase::new);
        IUItem.imp_bee_frame_template = new DataSimpleItem<>(new ResourceLocation("", "imp_bee_frame_template"), IUItemBase::new);
        IUItem.larva = new DataSimpleItem<>(new ResourceLocation("", "larva"), IUItemBase::new);
        IUItem.plant_mixture = new DataSimpleItem<>(new ResourceLocation("", "plant_mixture"), IUItemBase::new);
        IUItem.tomeResearch = new DataSimpleItem<>(new ResourceLocation("", "tome_research"), () -> new ItemTomeResearchSpace());
        IUItem.ironMesh = new DataSimpleItem<>(new ResourceLocation("mesh", "ironmesh"), () -> new ItemMesh("ironmesh", 1000, 1));
        IUItem.steelMesh = new DataSimpleItem<>(new ResourceLocation("mesh", "steelmesh"), () -> new ItemMesh("steelmesh", 2000, 2));
        IUItem.boridehafniumMesh = new DataSimpleItem<>(new ResourceLocation("mesh", "boridehafniummesh"), () -> new ItemMesh("boridehafniummesh", 3500, 3));
        IUItem.vanadiumaluminumMesh = new DataSimpleItem<>(new ResourceLocation("mesh", "vanadiumaluminummesh"), () -> new ItemMesh("vanadiumaluminummesh", 5000, 4));
        IUItem.steleticMesh = new DataSimpleItem<>(new ResourceLocation("mesh", "steleticmesh"), () -> new ItemMesh("steleticmesh", 8000, 5));
        IUItem.water_rotor_wood = new DataSimpleItem<>(new ResourceLocation("water_rotor", "water_rotor_wood"), () -> new ItemWaterRotor("water_rotor_wood", 10800 / 2, 0.25F, new ResourceLocation(
                Constants.MOD_ID,
                "textures/item/rotor" +
                        "/wood_rotor_model.png"
        ), 1, 0, new Color(94,71,39)));
        IUItem.water_rotor_bronze = new DataSimpleItem<>(new ResourceLocation("water_rotor", "water_rotor_bronze"), () -> new ItemWaterRotor("water_rotor_bronze", 86400 / 2, 0.5F, new ResourceLocation(
                Constants.MOD_ID,
                "textures/item/rotor/bronze_rotor_model_1.png"
        ), 2, 1, new Color(158,79,0)));
        IUItem.water_rotor_iron = new DataSimpleItem<>(new ResourceLocation("water_rotor", "water_rotor_iron"), () -> new ItemWaterRotor("water_rotor_iron", (int) (86400 / 1.5), 0.5F, new ResourceLocation(
                Constants.MOD_ID,
                "textures/item/rotor/iron_rotor_model_1.png"
        ), 3, 2, new Color(159,159,159)));
        IUItem.water_rotor_steel = new DataSimpleItem<>(new ResourceLocation("water_rotor", "water_rotor_steel"), () -> new ItemWaterRotor(
                "water_rotor_steel",
                (int) (172800 / 1.5),
                0.75F,
                new ResourceLocation(
                        Constants.MOD_ID,
                        "textures/item/rotor/steel_rotor_model_1.png"
                ),
                4,
                3, new Color(157,166,170)
        ));
        IUItem.water_rotor_carbon = new DataSimpleItem<>(new ResourceLocation("water_rotor", "water_rotor_carbon"), () -> new ItemWaterRotor(
                "water_rotor_carbon",
                (int) (604800 / 1.5),
                1.0F,
                new ResourceLocation(
                        Constants.MOD_ID, "textures/item/rotor/carbon_rotor_model_1.png"),
                5,
                4,new Color(41,41,41)
        ));
        IUItem.water_iridium = new DataSimpleItem<>(new ResourceLocation("water_rotor", "water_iridium"), () -> new ItemWaterRotor("water_iridium",
                (int) (172800 / 1.4D),2,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbo_rotor_model1.png"), 6, 5, new Color(214,212,216)
        ));
        IUItem.water_compressiridium = new DataSimpleItem<>(new ResourceLocation("water_rotor", "water_compressiridium"), () -> new ItemWaterRotor("water_compressiridium",
                (int) (172800 / 1.2D), 4.0F,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_2.png"), 7, 6, new Color(135,134,136)
        ));
        IUItem.water_spectral = new DataSimpleItem<>(new ResourceLocation("water_rotor", "water_spectral"), () -> new ItemWaterRotor("water_spectral",
                (int) (172800 / 1.1D), 8.0F,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_3.png"), 8, 7, new Color(195,151,32)
        ));
        IUItem.water_myphical = new DataSimpleItem<>(new ResourceLocation("water_rotor", "water_myphical"), () -> new ItemWaterRotor("water_myphical",
                3456000, 16,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_4.png"), 9, 8, new Color(98,22,182)
        ));

        IUItem.water_photon = new DataSimpleItem<>(new ResourceLocation("water_rotor", "water_photon"), () -> new ItemWaterRotor("water_photon", 691200,
                32,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_6.png"), 10, 9, new Color(36,175,17)
        ));
        IUItem.water_neutron = new DataSimpleItem<>(new ResourceLocation("water_rotor", "water_neutron"), () -> new ItemWaterRotor("water_neutron", 27648000,
                64,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_5.png"), 11, 10,new Color(41,86,208)
        ));

        IUItem.water_barionrotor = new DataSimpleItem<>(new ResourceLocation("water_rotor", "water_barionrotor"), () -> new ItemWaterRotor("water_barionrotor",
                27648000 * 4, 64 * 2,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_7.png"), 12, 11, new Color(155,17,175)
        ));

        IUItem.water_adronrotor = new DataSimpleItem<>(new ResourceLocation("water_rotor", "water_adronrotor"), () -> new ItemWaterRotor("water_adronrotor",
                27648000 * 16, 64 * 4,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_8.png"), 13, 12,new Color(0,179,192)
        ));
        IUItem.water_ultramarinerotor = new DataSimpleItem<>(new ResourceLocation("water_rotor", "water_ultramarinerotor"), () -> new ItemWaterRotor("water_ultramarinerotor",
                27648000 * 64, 64 * 8,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_9.png"), 14, 13, new Color(17,175,166)
        ));
        IUItem.wood_steam_blade = new DataSimpleItem<>(new ResourceLocation("steam_blade", "steam_blade_wood"), () -> new ItemSteamRod(0, 0.02, 10800 / 2, new ResourceLocation(
                Constants.MOD_ID,
                "textures/item/rotor" +
                        "/wood_rotor_model.png"
        )));
        IUItem.rotor_wood = new DataSimpleItem<>(new ResourceLocation("rotor", "rotor_wood"), () -> new ItemWindRotor("rotor_wood", 5, 10800 / 2, 0.25F, new ResourceLocation(
                Constants.MOD_ID,
                "textures/item/rotor" +
                        "/wood_rotor_model.png"
        ), 1, 0, new Color(158,79,0)));
        IUItem.rotor_bronze = new DataSimpleItem<>(new ResourceLocation("rotor", "rotor_bronze"), () -> new ItemWindRotor("rotor_bronze", 7, 86400 / 2, 0.5F, new ResourceLocation(
                Constants.MOD_ID,
                "textures/item/rotor/bronze_rotor_model_1.png"
        ), 2, 1, new Color(158,79,0)));
        IUItem.rotor_iron = new DataSimpleItem<>(new ResourceLocation("rotor", "rotor_iron"), () -> new ItemWindRotor("rotor_iron", 7, (int) (86400 / 1.5), 0.5F, new ResourceLocation(
                Constants.MOD_ID,
                "textures/item/rotor/iron_rotor_model_1.png"
        ), 3, 2, new Color(159,159,159)));
        IUItem.rotor_steel = new DataSimpleItem<>(new ResourceLocation("rotor", "rotor_steel"), () -> new ItemWindRotor("rotor_steel", 9, (int) (172800 / 1.5), 0.75F, new ResourceLocation(
                Constants.MOD_ID,
                "textures/item/rotor/steel_rotor_model_1.png"
        ), 4, 3, new Color(157,166,170)));
        IUItem.rotor_carbon = new DataSimpleItem<>(new ResourceLocation("rotor", "rotor_carbon"), () -> new ItemWindRotor("rotor_carbon", 11, (int) (604800 / 1.5), 1.0F, new ResourceLocation(
                Constants.MOD_ID, "textures/item/rotor/carbon_rotor_model_1.png"), 5, 4,new Color(41,41,41)));

        IUItem.iridium = new DataSimpleItem<>(new ResourceLocation("rotor", "iridium"), () -> new ItemWindRotor("iridium", 11, (int) (172800 / 1.4D),
                2,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_1.png"), 6, 5,  new Color(214,212,216)
        ));
        IUItem.compressiridium = new DataSimpleItem<>(new ResourceLocation("rotor", "compressiridium"), () -> new ItemWindRotor("compressiridium", 11,
                (int) (172800 / 1.2D), 4,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_2.png"), 7, 6,new Color(135,134,136)
        ));
        IUItem.spectral = new DataSimpleItem<>(new ResourceLocation("rotor", "spectral"), () -> new ItemWindRotor("spectral", 11,
                (int) (172800 / 1.1D), 8,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_3.png"), 8, 7, new Color(195,151,32)
        ));
        IUItem.myphical = new DataSimpleItem<>(new ResourceLocation("rotor", "myphical"), () -> new ItemWindRotor("myphical", 11,
                3456000, 16,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_4.png"), 9, 8, new Color(98,22,182)
        ));

        IUItem.photon = new DataSimpleItem<>(new ResourceLocation("rotor", "photon"), () -> new ItemWindRotor("photon", 11, 691200,
                32,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_6.png"), 10, 9, new Color(36,175,17)
        ));
        IUItem.neutron = new DataSimpleItem<>(new ResourceLocation("rotor", "neutron"), () -> new ItemWindRotor("neutron", 11, 27648000,
                64,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_5.png"), 11, 10,new Color(41,86,208)
        ));

        IUItem.barionrotor = new DataSimpleItem<>(new ResourceLocation("rotor", "barionrotor"), () -> new ItemWindRotor("barionrotor", 11,
                27648000 * 4, 64 * 2,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_7.png"), 12, 11,  new Color(155,17,175)
        ));

        IUItem.adronrotor = new DataSimpleItem<>(new ResourceLocation("rotor", "adronrotor"), () -> new ItemWindRotor("adronrotor", 11,
                27648000 * 16, 64 * 4,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_8.png"), 13, 12,new Color(0,179,192)
        ));
        IUItem.ultramarinerotor = new DataSimpleItem<>(new ResourceLocation("rotor", "ultramarinerotor"), () -> new ItemWindRotor("ultramarinerotor", 11,
                27648000 * 64, 64 * 8,
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_9.png"), 14, 13,new Color(17,175,166)
        ));
        IUItem.bronze_steam_blade = new DataSimpleItem<>(new ResourceLocation("steam_blade", "steam_blade_bronze"), () -> new ItemSteamRod(0, 0.04, 10800, new ResourceLocation(
                Constants.MOD_ID,
                "textures/item/rotor/bronze_rotor_model_1.png"
        )));
        IUItem.iron_steam_blade = new DataSimpleItem<>(new ResourceLocation("steam_blade", "steam_blade_iron"), () -> new ItemSteamRod(0, 0.08, (int) (10800 * 1.5), new ResourceLocation(
                Constants.MOD_ID,
                "textures/item/rotor/iron_rotor_model_1.png"
        )));
        IUItem.steel_steam_blade = new DataSimpleItem<>(new ResourceLocation("steam_blade", "steam_blade_steel"), () -> new ItemSteamRod(0, 0.1, (int) (20800 * 1.5), new ResourceLocation(
                Constants.MOD_ID,
                "textures/item/rotor/steel_rotor_model_1.png"
        )));
        IUItem.carbon_steam_blade = new DataSimpleItem<>(new ResourceLocation("steam_blade", "steam_blade_carbon"), () -> new ItemSteamRod(1, 0.2, (int) (60800 * 1.5), new ResourceLocation(
                Constants.MOD_ID, "textures/item/rotor/carbon_rotor_model_1.png")));


        IUItem.iridium_steam_blade = new DataSimpleItem<>(new ResourceLocation("steam_blade", "steam_blade_iridium"), () -> new ItemSteamRod(1, 0.4, (int) (60800 * 2),
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbo_rotor_model1.png")
        ));
        IUItem.compressiridium_steam_blade = new DataSimpleItem<>(new ResourceLocation("steam_blade", "steam_blade_compressiridium"), () -> new ItemSteamRod(1, 0.6, (int) (60800 * 3),
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_2.png")
        ));
        IUItem.spectral_steam_blade = new DataSimpleItem<>(new ResourceLocation("steam_blade", "steam_blade_spectral"), () -> new ItemSteamRod(1, 0.95, (int) (60800 * 4),
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_3.png")
        ));
        IUItem.myphical_steam_blade = new DataSimpleItem<>(new ResourceLocation("steam_blade", "steam_blade_myphical"), () -> new ItemSteamRod(2, 1.15, (int) (60800 * 6),
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_4.png")
        ));

        IUItem.photon_steam_blade = new DataSimpleItem<>(new ResourceLocation("steam_blade", "steam_blade_photon"), () -> new ItemSteamRod(2, 1.5, (int) (60800 * 10),
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_6.png")
        ));
        IUItem.neutron_steam_blade = new DataSimpleItem<>(new ResourceLocation("steam_blade", "steam_blade_neutron"), () -> new ItemSteamRod(2, 2.0, (int) (60800 * 15),
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_5.png")
        ));

        IUItem.barion_steam_blade = new DataSimpleItem<>(new ResourceLocation("steam_blade", "steam_blade_barion"), () -> new ItemSteamRod(3, 2.5, (int) (60800 * 20),
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_7.png")
        ));

        IUItem.hadron_steam_blade = new DataSimpleItem<>(new ResourceLocation("steam_blade", "steam_blade_hadron"), () -> new ItemSteamRod(3, 3, (int) (60800 * 30),
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_8.png")
        ));
        IUItem.ultramarine_steam_blade = new DataSimpleItem<>(new ResourceLocation("steam_blade", "steam_blade_ultramarine"), () -> new ItemSteamRod(3, 3.5, (int) (60800 * 50),
                new ResourceLocation(Constants.MOD_ID, "textures/item/carbon_rotor_model_9.png")
        ));
        IUItem.hops = new DataSimpleItem<>(new ResourceLocation("", "hops"), () -> (IUItemBase) new IUItemBase(IUCore.CropsTab));
        IUItem.tomato = new DataSimpleItem<>(new ResourceLocation("", "tomato"), () -> new ItemFoodIU("tomato", 3, 0.6F));
        IUItem.terra_wart = new DataSimpleItem<>(new ResourceLocation("", "terra_wart"), () -> new ItemFoodIU("terra_wart", 1, 0.4F, true));
        IUItem.corn = new DataSimpleItem<>(new ResourceLocation("", "corn"), () -> new ItemFoodIU("corn", 2, 0.4f));
        IUItem.raspberry = new DataSimpleItem<>(new ResourceLocation("", "raspberry"), () -> new ItemFoodIU("raspberry", 2, 0.4f));
        IUItem.fertilizer = new DataSimpleItem<>(new ResourceLocation("", "fertilizer"), IUItemBase::new);
        IUItem.raw_apatite = new DataSimpleItem<>(new ResourceLocation("", "raw_apatite"), IUItemBase::new);
        IUItem.apatite_cube = new DataSimpleItem<>(new ResourceLocation("", "apatite_cube"), IUItemBase::new);
        IUItem.white_phosphorus = new DataSimpleItem<>(new ResourceLocation("", "white_phosphorus"), IUItemBase::new);
        IUItem.red_phosphorus = new DataSimpleItem<>(new ResourceLocation("", "red_phosphorus"), IUItemBase::new);
        IUItem.phosphorus_oxide = new DataSimpleItem<>(new ResourceLocation("", "phosphorus_oxide"), IUItemBase::new);
        IUItem.honeycomb = new DataSimpleItem<>(new ResourceLocation("", "honeycomb"), IUItemBase::new);
        IUItem.honey_drop = new DataSimpleItem<>(new ResourceLocation("", "honey_drop"), IUItemBase::new);
        IUItem.bee_pollen = new DataSimpleItem<>(new ResourceLocation("", "bee_pollen"), IUItemBase::new);
        IUItem.planner = new DataSimpleItem<>(new ResourceLocation("tools", "multiblock_planner"), () -> new ItemPlaner());
        IUItem.deplanner = new DataSimpleItem<>(new ResourceLocation("tools", "multiblock_deplanner"), () -> new ItemDeplanner());
        IUItem.pollutionDevice = new DataSimpleItem<>(new ResourceLocation("", "pollution_device"), () -> new ItemPollutionDevice());
        IUItem.reactorData = new DataSimpleItem<>(new ResourceLocation("tools", "reactor_data_item"), () -> new ItemReactorData());
        IUItem.coolingsensor = new DataSimpleItem<>(new ResourceLocation("tools", "cooling_sensor"), () -> new ItemsCoolingSensor());
        IUItem.heatsensor = new DataSimpleItem<>(new ResourceLocation("tools", "heat_sensor"), () -> new ItemsHeatSensor());
        IUItem.synthetic_rubber = new DataSimpleItem<>(new ResourceLocation("", "synthetic_rubber"), IUItemBase::new);
        IUItem.synthetic_plate = new DataSimpleItem<>(new ResourceLocation("", "synthetic_plate"), IUItemBase::new);
        IUItem.charged_redstone = new DataSimpleItem<>(new ResourceLocation("", "charged_redstone"), IUItemBase::new);
        IUItem.charged_quartz = new DataSimpleItem<>(new ResourceLocation("", "charged_quartz"), IUItemBase::new);
        IUItem.peat_balls = new DataSimpleItem<>(new ResourceLocation("", "peat_balls"), IUItemBase::new);
        IUItem.cultivated_peat_balls = new DataSimpleItem<>(new ResourceLocation("", "cultivated_peat"), IUItemBase::new);
        IUItem.nether_star_ingot = new DataSimpleItem<>(new ResourceLocation("", "nether_star_ingot"), IUItemBase::new);
        IUItem.wolframite = new DataSimpleItem<>(new ResourceLocation("", "wolframite"), IUItemBase::new);
        IUItem.wax_stick = new DataSimpleItem<>(new ResourceLocation("", "wax_stick"), IUItemBase::new);
        IUItem.royal_jelly = new DataSimpleItem<>(new ResourceLocation("", "royal_jelly"), IUItemBase::new);
        IUItem.beeswax = new DataSimpleItem<>(new ResourceLocation("", "beeswax"), IUItemBase::new);
        IUItem.cooling_mixture = new DataSimpleItem<>(new ResourceLocation("", "cooling_mixture"), IUItemBase::new);
        IUItem.helium_cooling_mixture = new DataSimpleItem<>(new ResourceLocation("", "helium_cooling_mixture"), IUItemBase::new);
        IUItem.cryogenic_cooling_mixture = new DataSimpleItem<>(new ResourceLocation("", "cryogenic_cooling_mixture"), IUItemBase::new);
        IUItem.medium_current_converter_to_low = new DataSimpleItem<>(new ResourceLocation("", "medium_current_converter_to_low"), IUItemBase::new);
        IUItem.high_current_converter_to_low = new DataSimpleItem<>(new ResourceLocation("", "high_current_converter_to_low"), IUItemBase::new);
        IUItem.extreme_current_converter_to_low = new DataSimpleItem<>(new ResourceLocation("", "extreme_current_converter_to_low"), IUItemBase::new);
        IUItem.upgrade_casing = new DataSimpleItem<>(new ResourceLocation("", "upgrade_casing"), IUItemBase::new);
        IUItem.voltage_sensor_for_mechanism = new DataSimpleItem<>(new ResourceLocation("", "voltage_sensor_for_mechanism"), IUItemBase::new);
        IUItem.graphene_wire = new DataSimpleItem<>(new ResourceLocation("", "graphene_wire"), IUItemBase::new);
        IUItem.graphene = new DataSimpleItem<>(new ResourceLocation("", "graphene"), IUItemBase::new);
        IUItem.graphene_plate = new DataSimpleItem<>(new ResourceLocation("", "graphene_plate"), IUItemBase::new);
        IUItem.motors_with_improved_bearings_ = new DataSimpleItem<>(new ResourceLocation("", "motors_with_improved_bearings_"), IUItemBase::new);
        IUItem.adv_motors_with_improved_bearings_ = new DataSimpleItem<>(new ResourceLocation("", "adv_motors_with_improved_bearings_"), IUItemBase::new);
        IUItem.imp_motors_with_improved_bearings_ = new DataSimpleItem<>(new ResourceLocation("", "imp_motors_with_improved_bearings_"), IUItemBase::new);
        IUItem.compressed_redstone = new DataSimpleItem<>(new ResourceLocation("", "compressed_redstone"), IUItemBase::new);
        IUItem.electronic_stabilizers = new DataSimpleItem<>(new ResourceLocation("", "electronic_stabilizers"), IUItemBase::new);
        IUItem.polonium_palladium_composite = new DataSimpleItem<>(new ResourceLocation("", "polonium_palladium_composite"), IUItemBase::new);
        IUItem.polished_stick = new DataSimpleItem<>(new ResourceLocation("", "polished_stick"), IUItemBase::new);
        IUItem.ruby_helmet = new DataSimpleItem<>(new ResourceLocation("armour", "ruby_helmet"), () -> new BaseArmor(ArmorMaterials.RUBY, ArmorItem.Type.HELMET, "ruby"));
        IUItem.ruby_chestplate = new DataSimpleItem<>(new ResourceLocation("armour", "ruby_chestplate"), () -> new BaseArmor(ArmorMaterials.RUBY,  ArmorItem.Type.CHESTPLATE, "ruby"));
        IUItem.ruby_leggings = new DataSimpleItem<>(new ResourceLocation("armour", "ruby_leggings"), () -> new BaseArmor(ArmorMaterials.RUBY,  ArmorItem.Type.LEGGINGS, "ruby"));
        IUItem.ruby_boots = new DataSimpleItem<>(new ResourceLocation("armour", "ruby_boots"), () -> new BaseArmor(ArmorMaterials.RUBY,  ArmorItem.Type.BOOTS, "ruby"));
        IUItem.topaz_helmet = new DataSimpleItem<>(new ResourceLocation("armour", "topaz_helmet"), () -> new BaseArmor(ArmorMaterials.TOPAZ,  ArmorItem.Type.HELMET, "topaz"));
        IUItem.topaz_chestplate = new DataSimpleItem<>(new ResourceLocation("armour", "topaz_chestplate"), () -> new BaseArmor(ArmorMaterials.TOPAZ,  ArmorItem.Type.CHESTPLATE, "topaz"));
        IUItem.topaz_leggings = new DataSimpleItem<>(new ResourceLocation("armour", "topaz_leggings"), () -> new BaseArmor(ArmorMaterials.TOPAZ,  ArmorItem.Type.LEGGINGS, "topaz"));
        IUItem.topaz_boots = new DataSimpleItem<>(new ResourceLocation("armour", "topaz_boots"), () -> new BaseArmor(ArmorMaterials.TOPAZ,  ArmorItem.Type.BOOTS, "topaz"));
        IUItem.sapphire_helmet = new DataSimpleItem<>(new ResourceLocation("armour", "sapphire_helmet"), () -> new BaseArmor(ArmorMaterials.SAPPHIRE,  ArmorItem.Type.HELMET, "sapphire"));
        IUItem.sapphire_chestplate = new DataSimpleItem<>(new ResourceLocation("armour", "sapphire_chestplate"), () -> new BaseArmor(
                ArmorMaterials.SAPPHIRE,
                ArmorItem.Type.CHESTPLATE,
                "sapphire"
        ));
        IUItem.sapphire_leggings = new DataSimpleItem<>(new ResourceLocation("armour", "sapphire_leggings"), () -> new BaseArmor(ArmorMaterials.SAPPHIRE,  ArmorItem.Type.LEGGINGS, "sapphire"));
        IUItem.sapphire_boots = new DataSimpleItem<>(new ResourceLocation("armour", "sapphire_boots"), () -> new BaseArmor(ArmorMaterials.SAPPHIRE,  ArmorItem.Type.BOOTS, "sapphire"));

        IUItem.batpack = new DataSimpleItem<>(new ResourceLocation("armour", "batpack"), () -> new ItemLappack("batpack", 60000.0, 1, 100.0));
        IUItem.advanced_batpack = new DataSimpleItem<>(new ResourceLocation("armour", "advanced_batpack"), () -> new ItemLappack("advanced_batpack", 600000.0, 2, 1000.0));
        IUItem.lappack = new DataSimpleItem<>(new ResourceLocation("armour", "lappack"), () -> new ItemLappack("lappack", 2.0E6, 3, 2500.0));

        IUItem.bronze_helmet = new DataSimpleItem<>(new ResourceLocation("armour", "bronze_helmet"), () -> new BaseArmor(ArmorMaterials.BRONZE,  ArmorItem.Type.HELMET, "bronze"));
        IUItem.bronze_chestplate = new DataSimpleItem<>(new ResourceLocation("armour", "bronze_chestplate"), () -> new BaseArmor(ArmorMaterials.BRONZE,  ArmorItem.Type.CHESTPLATE, "bronze"));
        IUItem.bronze_leggings = new DataSimpleItem<>(new ResourceLocation("armour", "bronze_leggings"), () -> new BaseArmor(ArmorMaterials.BRONZE,  ArmorItem.Type.LEGGINGS, "bronze"));
        IUItem.bronze_boots = new DataSimpleItem<>(new ResourceLocation("armour", "bronze_boots"), () -> new BaseArmor(ArmorMaterials.BRONZE,  ArmorItem.Type.BOOTS, "bronze"));


        IUItem.adv_lappack = new DataSimpleItem<>(new ResourceLocation("armour", "adv_lappack"), () -> new ItemLappack(
                "adv_lappack",
                25000000,
                3,
                50000
        ));
        IUItem.imp_lappack = new DataSimpleItem<>(new ResourceLocation("armour", "imp_lappack"), () -> new ItemLappack(
                "imp_lappack",
                50000000,
                4,
                100000
        ));
        IUItem.per_lappack = new DataSimpleItem<>(new ResourceLocation("armour", "per_lappack"), () -> new ItemLappack(
                "per_lappack",
                100000000,
                5,
                500000
        ));
        IUItem.electricJetpack = new DataSimpleItem<>(new ResourceLocation("armour", "jetpack"), () -> new ItemAdvJetpack("jetpack", 30000.0, 60.0, 1));

        IUItem.advjetpack = new DataSimpleItem<>(new ResourceLocation("armour", "advjetpack"), () -> new ItemAdvJetpack(
                "advjetpack",
                60000,
                120,
                2
        ));
        IUItem.impjetpack = new DataSimpleItem<>(new ResourceLocation("armour", "impjetpack"), () -> new ItemAdvJetpack(
                "impjetpack",
                120000,
                500,
                3
        ));
        IUItem.perjetpack = new DataSimpleItem<>(new ResourceLocation("armour", "perjetpack"), () -> new ItemAdvJetpack(
                "perjetpack",
                250000,
                1000,
                4
        ));
        IUItem.nightvision = new DataSimpleItem<>(new ResourceLocation("armour", "nightvision_goggles"), ItemArmorNightvisionGoggles::new);
        IUItem.hazmat_chestplate = new DataSimpleItem<>(new ResourceLocation("armour", "hazmat_chestplate"), () -> new ItemArmorHazmat("hazmat_chestplate", ArmorItem.Type.CHESTPLATE));
        IUItem.hazmat_helmet = new DataSimpleItem<>(new ResourceLocation("armour", "hazmat_helmet"), () -> new ItemArmorHazmat("hazmat_helmet", ArmorItem.Type.HELMET));
        IUItem.hazmat_leggings = new DataSimpleItem<>(new ResourceLocation("armour", "hazmat_leggings"), () -> new ItemArmorHazmat("hazmat_leggings", ArmorItem.Type.LEGGINGS));
        IUItem.rubber_boots = new DataSimpleItem<>(new ResourceLocation("armour", "rubber_boots"), () -> new ItemArmorHazmat("rubber_boots", ArmorItem.Type.BOOTS));

        IUItem.volcano_hazmat_chestplate = new DataSimpleItem<>(new ResourceLocation("armour", "volcano_hazmat_chestplate"), () -> new ItemArmorVolcanoHazmat("volcano_hazmat_chestplate", ArmorItem.Type.CHESTPLATE));
        IUItem.volcano_hazmat_helmet = new DataSimpleItem<>(new ResourceLocation("armour", "volcano_hazmat_helmet"), () -> new ItemArmorVolcanoHazmat("volcano_hazmat_helmet", ArmorItem.Type.HELMET));
        IUItem.volcano_hazmat_leggings = new DataSimpleItem<>(new ResourceLocation("armour", "volcano_hazmat_leggings"), () -> new ItemArmorVolcanoHazmat("volcano_hazmat_leggings", ArmorItem.Type.LEGGINGS));
        IUItem.volcano_rubber_boots = new DataSimpleItem<>(new ResourceLocation("armour", "volcano_rubber_boots"), () -> new ItemArmorVolcanoHazmat("volcano_rubber_boots", ArmorItem.Type.BOOTS));

        IUItem.spectralSolarHelmet = new DataSimpleItem<>(new ResourceLocation("armour", "spectral_solar_helmet"), () -> new ItemSolarPanelHelmet(

                4,
                "spectral_solar_helmet"
        ));
        IUItem.singularSolarHelmet = new DataSimpleItem<>(new ResourceLocation("armour", "singular_solar_helmet"), () -> new ItemSolarPanelHelmet(

                5,
                "singular_solar_helmet"
        ));
        IUItem.advancedSolarHelmet = new DataSimpleItem<>(new ResourceLocation("armour", "advanced_solar_helmet"), () -> new ItemSolarPanelHelmet(

                1,
                "advanced_solar_helmet"
        ));
        IUItem.hybridSolarHelmet = new DataSimpleItem<>(new ResourceLocation("armour", "hybrid_solar_helmet"), () -> new ItemSolarPanelHelmet(

                2,
                "hybrid_solar_helmet"
        ));
        IUItem.ultimateSolarHelmet = new DataSimpleItem<>(new ResourceLocation("armour", "ultimate_solar_helmet"), () -> new ItemSolarPanelHelmet(

                3,
                "ultimate_solar_helmet"
        ));

        IUItem.hazmathelmet = new DataSimpleItem<>(new ResourceLocation("armour", "hazmathelmet"), () -> new ItemAdvArmorHazmat("hazmathelmet", ArmorItem.Type.HELMET));
        IUItem.hazmatchest = new DataSimpleItem<>(new ResourceLocation("armour", "hazmatchest"), () -> new ItemAdvArmorHazmat("hazmatchest", ArmorItem.Type.CHESTPLATE));
        IUItem.hazmatleggins = new DataSimpleItem<>(new ResourceLocation("armour", "hazmatleggins"), () -> new ItemAdvArmorHazmat("hazmatleggins", ArmorItem.Type.LEGGINGS));
        IUItem.hazmatboosts = new DataSimpleItem<>(new ResourceLocation("armour", "hazmatboosts"), () -> new ItemAdvArmorHazmat("hazmatboosts", ArmorItem.Type.BOOTS));

        IUItem.spectral_helmet = new DataSimpleItem<>(new ResourceLocation("armour", "spectral_helmet"), () -> new ItemSpecialArmor(
                EnumSubTypeArmor.HELMET, EnumTypeArmor.SPECTRAL
        ));
        IUItem.spectral_chestplate = new DataSimpleItem<>(new ResourceLocation("armour", "spectral_chestplate"), () -> new ItemSpecialArmor(
                EnumSubTypeArmor.CHESTPLATE, EnumTypeArmor.SPECTRAL
        ));
        IUItem.spectral_leggings = new DataSimpleItem<>(new ResourceLocation("armour", "spectral_leggings"), () -> new ItemSpecialArmor(
                EnumSubTypeArmor.LEGGINGS, EnumTypeArmor.SPECTRAL
        ));
        IUItem.spectral_boots = new DataSimpleItem<>(new ResourceLocation("armour", "spectral_boots"), () -> new ItemSpecialArmor(
                EnumSubTypeArmor.BOOTS, EnumTypeArmor.SPECTRAL
        ));
        IUItem.adv_nano_helmet = new DataSimpleItem<>(new ResourceLocation("armour", "adv_nano_helmet"), () -> new ItemSpecialArmor(EnumSubTypeArmor.HELMET, EnumTypeArmor.ADV_NANO));
        IUItem.adv_nano_chestplate = new DataSimpleItem<>(new ResourceLocation("armour", "adv_nano_chestplate"), () -> new ItemSpecialArmor(EnumSubTypeArmor.CHESTPLATE, EnumTypeArmor.ADV_NANO));
        IUItem.adv_nano_leggings = new DataSimpleItem<>(new ResourceLocation("armour", "adv_nano_leggings"), () -> new ItemSpecialArmor(EnumSubTypeArmor.LEGGINGS, EnumTypeArmor.ADV_NANO));
        IUItem.adv_nano_boots = new DataSimpleItem<>(new ResourceLocation("armour", "adv_nano_boots"), () -> new ItemSpecialArmor(EnumSubTypeArmor.BOOTS, EnumTypeArmor.ADV_NANO));

        IUItem.nano_boots = new DataSimpleItem<>(new ResourceLocation("armour", "nano_boots"), () -> new ItemSpecialArmor(EnumSubTypeArmor.BOOTS, EnumTypeArmor.NANO));
        IUItem.nano_chestplate = new DataSimpleItem<>(new ResourceLocation("armour", "nano_chestplate"), () -> new ItemSpecialArmor(EnumSubTypeArmor.CHESTPLATE, EnumTypeArmor.NANO));
        IUItem.nano_helmet = new DataSimpleItem<>(new ResourceLocation("armour", "nano_helmet"), () -> new ItemSpecialArmor(EnumSubTypeArmor.HELMET, EnumTypeArmor.NANO));
        IUItem.nano_leggings = new DataSimpleItem<>(new ResourceLocation("armour", "nano_leggings"), () -> new ItemSpecialArmor(EnumSubTypeArmor.LEGGINGS, EnumTypeArmor.NANO));
        IUItem.quantum_boots = new DataSimpleItem<>(new ResourceLocation("armour", "quantum_boots"), () -> new ItemSpecialArmor(EnumSubTypeArmor.BOOTS, EnumTypeArmor.QUANTUM));
        IUItem.quantum_chestplate = new DataSimpleItem<>(new ResourceLocation("armour", "quantum_chestplate"), () -> new ItemSpecialArmor(EnumSubTypeArmor.CHESTPLATE, EnumTypeArmor.QUANTUM));
        IUItem.quantum_helmet = new DataSimpleItem<>(new ResourceLocation("armour", "quantum_helmet"), () -> new ItemSpecialArmor(EnumSubTypeArmor.HELMET, EnumTypeArmor.QUANTUM));
        IUItem.quantum_leggings = new DataSimpleItem<>(new ResourceLocation("armour", "quantum_leggings"), () -> new ItemSpecialArmor(EnumSubTypeArmor.LEGGINGS, EnumTypeArmor.QUANTUM));
        IUItem.chainsaw = new DataSimpleItem<>(new ResourceLocation("energy_tools", "chainsaw"), () -> new ItemEnergyInstruments(EnumTypeInstruments.CHAINSAW, EnumVarietyInstruments.CHAINSAW, "chainsaw"));
        IUItem.vajra = new DataSimpleItem<>(new ResourceLocation("energy_tools", "vajra"), () -> new ItemEnergyInstruments(EnumTypeInstruments.VAJRA, EnumVarietyInstruments.VAJRA, "vajra"));
        IUItem.ult_vajra = new DataSimpleItem<>(new ResourceLocation("energy_tools", "ult_vajra"), () -> new ItemEnergyInstruments(EnumTypeInstruments.ULT_VAJRA, EnumVarietyInstruments.VAJRA, "ult_vajra"));

        IUItem.GraviTool = new DataSimpleItem<>(new ResourceLocation("gravitool", "gravitool".toLowerCase()), () -> new ItemGraviTool());
        IUItem.drill = new DataSimpleItem<>(new ResourceLocation("energy_tools", "drill"), () -> new ItemEnergyInstruments(EnumTypeInstruments.SIMPLE_DRILL, EnumVarietyInstruments.SIMPLE, "drill"));
        IUItem.diamond_drill = new DataSimpleItem<>(new ResourceLocation("energy_tools", "diamond_drill"), () -> new ItemEnergyInstruments(EnumTypeInstruments.DIAMOND_DRILL, EnumVarietyInstruments.DIAMOND,
                "diamond_drill"
        ));
        IUItem.nanodrill = new DataSimpleItem<>(new ResourceLocation("energy_tools", "nanodrill"), () -> new ItemEnergyInstruments(EnumTypeInstruments.DRILL, EnumVarietyInstruments.NANO, "nanodrill"));
        IUItem.quantumdrill = new DataSimpleItem<>(new ResourceLocation("energy_tools", "quantumdrill"), () -> new ItemEnergyInstruments(
                EnumTypeInstruments.DRILL,
                EnumVarietyInstruments.QUANTUM,
                "quantumdrill"
        ));
        IUItem.spectraldrill = new DataSimpleItem<>(new ResourceLocation("energy_tools", "spectraldrill"), () -> new ItemEnergyInstruments(
                EnumTypeInstruments.DRILL,
                EnumVarietyInstruments.SPECTRAL,
                "spectraldrill"
        ));
        IUItem.nanopickaxe = new DataSimpleItem<>(new ResourceLocation("energy_tools", "nanopickaxe"), () -> new ItemEnergyInstruments(EnumTypeInstruments.PICKAXE, EnumVarietyInstruments.NANO, "nanopickaxe"));
        IUItem.nanoshovel = new DataSimpleItem<>(new ResourceLocation("energy_tools", "nanoshovel"), () -> new ItemEnergyInstruments(EnumTypeInstruments.SHOVEL, EnumVarietyInstruments.NANO, "nanoshovel"));
        IUItem.nanoaxe = new DataSimpleItem<>(new ResourceLocation("energy_tools", "nanoaxe"), () -> new ItemEnergyInstruments(EnumTypeInstruments.AXE, EnumVarietyInstruments.NANO, "nanoaxe"));
        IUItem.quantumpickaxe = new DataSimpleItem<>(new ResourceLocation("energy_tools", "quantumpickaxe"), () -> new ItemEnergyInstruments(
                EnumTypeInstruments.PICKAXE,
                EnumVarietyInstruments.QUANTUM,
                "quantumpickaxe"
        ));
        IUItem.quantumshovel = new DataSimpleItem<>(new ResourceLocation("energy_tools", "quantumshovel"), () -> new ItemEnergyInstruments(
                EnumTypeInstruments.SHOVEL,
                EnumVarietyInstruments.QUANTUM,
                "quantumshovel"
        ));
        IUItem.quantumaxe = new DataSimpleItem<>(new ResourceLocation("energy_tools", "quantumaxe"), () -> new ItemEnergyInstruments(EnumTypeInstruments.AXE, EnumVarietyInstruments.QUANTUM, "quantumaxe"));
        IUItem.spectralpickaxe = new DataSimpleItem<>(new ResourceLocation("energy_tools", "spectralpickaxe"), () -> new ItemEnergyInstruments(
                EnumTypeInstruments.PICKAXE,
                EnumVarietyInstruments.SPECTRAL,
                "spectralpickaxe"
        ));
        IUItem.perfect_drill = new DataSimpleItem<>(new ResourceLocation("energy_tools", "ultDDrill".toLowerCase()), () -> new ItemEnergyInstruments(
                EnumTypeInstruments.PERFECT_DRILL,
                EnumVarietyInstruments.PERFECT_DRILL,
                "ultDDrill"
        ));
        IUItem.spectralshovel = new DataSimpleItem<>(new ResourceLocation("energy_tools", "spectralshovel"), () -> new ItemEnergyInstruments(
                EnumTypeInstruments.SHOVEL,
                EnumVarietyInstruments.SPECTRAL,
                "spectralshovel"
        ));
        IUItem.spectralaxe = new DataSimpleItem<>(new ResourceLocation("energy_tools", "spectralaxe"), () -> new ItemEnergyInstruments(EnumTypeInstruments.AXE, EnumVarietyInstruments.SPECTRAL, "spectralaxe"));
        IUItem.nano_bow = new DataSimpleItem<>(new ResourceLocation("energy_tools", "nano_bow"), () -> new ItemEnergyBow(
                "nano_bow",
                0,
                2,
                5000,
                50000,
                1f
        ));
        IUItem.quantum_bow = new DataSimpleItem<>(new ResourceLocation("energy_tools", "quantum_bow"), () -> new ItemEnergyBow(
                "quantum_bow",
                0,
                3,
                25000,
                80000,
                2f
        ));
        IUItem.spectral_bow = new DataSimpleItem<>(new ResourceLocation("energy_tools", "spectral_bow"), () -> new ItemEnergyBow(
                "spectral_bow",
                0,
                4,
                50000,
                150000,
                4f
        ));
        IUItem.ruby_pickaxe = new DataSimpleItem<>(new ResourceLocation("", "ruby_pickaxe"), () -> new ItemPickaxe("ruby_pickaxe"));
        IUItem.topaz_pickaxe = new DataSimpleItem<>(new ResourceLocation("", "topaz_pickaxe"), () -> new ItemPickaxe("topaz_pickaxe"));
        IUItem.sapphire_pickaxe = new DataSimpleItem<>(new ResourceLocation("", "sapphire_pickaxe"), () -> new ItemPickaxe("sapphire_pickaxe"));
        IUItem.ruby_axe = new DataSimpleItem<>(new ResourceLocation("", "ruby_axe"), () -> new ItemAxe("ruby_axe"));
        IUItem.topaz_axe = new DataSimpleItem<>(new ResourceLocation("", "topaz_axe"), () -> new ItemAxe("topaz_axe"));
        IUItem.sapphire_axe = new DataSimpleItem<>(new ResourceLocation("", "sapphire_axe"), () -> new ItemAxe("sapphire_axe"));
        IUItem.ruby_shovel = new DataSimpleItem<>(new ResourceLocation("", "ruby_shovel"), () -> new ItemShovel("ruby_shovel"));
        IUItem.topaz_shovel = new DataSimpleItem<>(new ResourceLocation("", "topaz_shovel"), () -> new ItemShovel("topaz_shovel"));
        IUItem.sapphire_shovel = new DataSimpleItem<>(new ResourceLocation("", "sapphire_shovel"), () -> new ItemShovel("sapphire_shovel"));
        VillagerInit.init();

        IUItem.creativeBattery =new DataSimpleItem<>(new ResourceLocation("battery", "creative_battery"), () -> new ItemCreativeBattery(false));
        IUItem.creativeBatteryWireless =new DataSimpleItem<>(new ResourceLocation("battery", "creative_battery_wireless"), () -> new ItemCreativeBattery(true));
        IUItem.creativeTomeResearch = new DataSimpleItem<>(new ResourceLocation("", "creative_tome_research"), () -> new ItemCreativeTomeResearchSpace());

    }

    public static void registerFluids() {
        registerfluid(FluidName.fluidiron, 1000, 3000, false);
        registerfluid(FluidName.fluidquartz, 1000, 3000, false);
        registerfluid(FluidName.fluidchromium, 1000, 3000, false);
        registerfluid(FluidName.fluidnichrome, 1000, 3000, false);
        registerfluid(FluidName.fluidmagnesium, 1000, 3000, false);
        registerfluid(FluidName.fluidobsidian, 1000, 3000, false);
        registerfluid(FluidName.fluidduralumin, 1000, 3000, false);
        registerfluid(FluidName.fluidnickel, 1000, 3000, false);
        registerfluid(FluidName.fluidcarbon, 1000, 3000, false);
        registerfluid(FluidName.fluidsteel, 1000, 3000, false);
        registerfluid(FluidName.fluidtitaniumsteel, 1000, 3000, false);
        registerfluid(FluidName.fluidsodiumhydroxide, 1000, 3000, false);
        registerfluid(FluidName.fluidsodium_hypochlorite, 1000, 3000, false);
        registerfluid(FluidName.fluidgold, 1000, 3000, false);
        registerfluid(FluidName.fluidsilver, 1000, 3000, false);
        registerfluid(FluidName.fluidelectrum, 1000, 3000, false);
        registerfluid(FluidName.fluidinvar, 1000, 3000, false);
        registerfluid(FluidName.fluidcopper, 1000, 3000, false);
        registerfluid(FluidName.fluidtin, 1000, 3000, false);
        registerfluid(FluidName.fluidtungsten, 1000, 3000, false);
        registerfluid(FluidName.fluidwolframite, 1000, 3000, false);
        registerfluid(FluidName.fluidtemperedglass, 1000, 3000, false);
        registerfluid(FluidName.fluidbronze, 1000, 3000, false);
        registerfluid(FluidName.fluidgallium, 1000, 3000, false);
        registerfluid(FluidName.fluidarsenicum, 1000, 3000, false);
        registerfluid(FluidName.fluidaluminium, 1000, 3000, false);
        registerfluid(FluidName.fluidmanganese, 1000, 3000, false);
        registerfluid(FluidName.fluidferromanganese, 1000, 3000, false);
        registerfluid(FluidName.fluidaluminiumbronze, 1000, 3000, false);
        registerfluid(FluidName.fluidirontitanium, 1000, 3000, false);
        registerfluid(FluidName.fluidtitanium, 1000, 3000, false);
        registerfluid(FluidName.fluidarsenicum_gallium, 1000, 3000, false);


        registerfluid(FluidName.fluidNeutron, 3000, 300, false);
        registerfluid(FluidName.fluidHelium, -1000, 300, true);
        registerfluid(FluidName.fluidcryogen, -1000, 300, true);
        registerfluid(FluidName.fluidazurebrilliant, 1000, 300, false);
        registerfluid(FluidName.fluidglowstone, 1000, 300, false);
        registerfluid(FluidName.fluidrawlatex, 1000, 300, false);
        registerfluid(FluidName.fluidbenz, 3000, 500, false);
        registerfluid(FluidName.fluidpetrol90, 3000, 500, false);
        registerfluid(FluidName.fluidpetrol95, 3000, 500, false);
        registerfluid(FluidName.fluidpetrol100, 3000, 500, false);
        registerfluid(FluidName.fluidpetrol105, 3000, 500, false);
        registerfluid(FluidName.fluiddizel, 3000, 500, false);
        registerfluid(FluidName.fluida_diesel, 3000, 500, false);
        registerfluid(FluidName.fluidaa_diesel, 3000, 500, false);
        registerfluid(FluidName.fluidaaa_diesel, 3000, 500, false);
        registerfluid(FluidName.fluidaaaa_diesel, 3000, 500, false);
        registerfluid(FluidName.fluidneft, 3000, 500, false);


        registerfluid(FluidName.fluidsweet_medium_oil, 3000, 500, false);
        registerfluid(FluidName.fluidsweet_heavy_oil, 3000, 500, false);
        registerfluid(FluidName.fluidsour_light_oil, 3000, 500, false);
        registerfluid(FluidName.fluidsour_medium_oil, 3000, 500, false);
        registerfluid(FluidName.fluidsour_heavy_oil, 3000, 500, false);


        registerfluid(FluidName.fluidmotoroil, 3000, 500, false);
        registerfluid(FluidName.fluidsteam_oil, 3000, 500, false);

        registerfluid(FluidName.fluidblackoil, 3000, 500, false);
        registerfluid(FluidName.fluidcreosote, 3000, 500, false);
        registerfluid(FluidName.fluidindustrialoil, 3000, 500, false);
        registerfluid(FluidName.fluidpolyeth, -3000, 2000, true);
        registerfluid(FluidName.fluidpolyprop, -3000, 2000, true);
        registerfluid(FluidName.fluidacetylene, -3000, 2000, true);
        registerfluid(FluidName.fluidoxy, -3000, 500, true);
        registerfluid(FluidName.fluidnitricoxide, -3000, 500, true);

        registerfluid(FluidName.fluidnitrogenoxy, -3000, 500, true);
        registerfluid(FluidName.fluidmethane, -3000, 500, true);
        registerfluid(FluidName.fluiddibromopropane, -3000, 500, true);
        registerfluid(FluidName.fluidpropane, -3000, 500, true);
        registerfluid(FluidName.fluidethylene, -3000, 500, true);
        registerfluid(FluidName.fluidpropylene, -3000, 500, true);
        registerfluid(FluidName.fluidethane, -3000, 500, true);
        registerfluid(FluidName.fluidbutadiene, -3000, 500, true);
        registerfluid(FluidName.fluidpolybutadiene, -3000, 500, true);
        registerfluid(FluidName.fluidhyd, -3000, 500, true);
        registerfluid(FluidName.fluidnitrogenhydride, -3000, 500, true);
        registerfluid(FluidName.fluidnitrogendioxide, -3000, 500, true);
        registerfluid(FluidName.fluidfluorhyd, -3000, 500, true);
        registerfluid(FluidName.fluidazot, -3000, 500, true);
        registerfluid(FluidName.fluidco2, -3000, 500, true);
        registerfluid(FluidName.fluidgas, -3000, 500, true);
        registerfluid(FluidName.fluidchlorum, -3000, 500, true);
        registerfluid(FluidName.fluidfluor, -3000, 500, true);
        registerfluid(FluidName.fluidbromine, -3000, 500, true);
        registerfluid(FluidName.fluidiodine, 3000, 500, false);
        registerfluid(FluidName.fluidsulfuricacid, 3000, 500, false);
        registerfluid(FluidName.fluidnitricacid, 3000, 500, false);
        registerfluid(FluidName.fluidorthophosphoricacid, 3000, 500, false);

        registerfluid(FluidName.fluidbenzene, 3000, 500, false);
        registerfluid(FluidName.fluidethanol, 3000, 500, false);
        registerfluid(FluidName.fluidacrylonitrile, 3000, 500, false);
        registerfluid(FluidName.fluidpolyacrylonitrile, 3000, 500, false);
        registerfluid(FluidName.fluidbutadiene_nitrile, 3000, 500, false);
        registerfluid(FluidName.fluidtoluene, 3000, 500, false);
        registerfluid(FluidName.fluidmethylbromide, 3000, 500, false);
        registerfluid(FluidName.fluidmethylchloride, 3000, 500, false);
        registerfluid(FluidName.fluidhydrogenbromide, 3000, 500, false);
        registerfluid(FluidName.fluidtrinitrotoluene, 3000, 500, false);


        registerfluid(FluidName.fluidhydrogenchloride, 3000, 500, false);
        registerfluid(FluidName.fluidchloroethane, -3000, 500, true);
        registerfluid(FluidName.fluidtetraethyllead, 3000, 500, false);
        registerfluid(FluidName.fluidcarbonmonoxide, -3000, 500, true);
        registerfluid(FluidName.fluidmethanol, 3000, 500, false);
        registerfluid(FluidName.fluidbutene, -3000, 500, true);
        registerfluid(FluidName.fluidmethylpentane, -3000, 500, true);
        registerfluid(FluidName.fluidcyclohexane, -3000, 500, true);
        registerfluid(FluidName.fluidmethylcyclohexane, -3000, 500, true);
        registerfluid(FluidName.fluidmethylpentanal, -3000, 500, true);
        registerfluid(FluidName.fluidethylhexanol, 3000, 500, false);
        registerfluid(FluidName.fluidethylhexylnitrate, 3000, 500, false);
        registerfluid(FluidName.fluidmethylcyclohexylnitrate, 3000, 500, false);
        registerfluid(FluidName.fluidtertbutylsulfuricacid, 3000, 500, false);
        registerfluid(FluidName.fluidtertbutylalcohol, 3000, 500, false);
        registerfluid(FluidName.fluidisobutylene, -3000, 500, true);
        registerfluid(FluidName.fluidtertbutylmethylether, 3000, 500, false);
        registerfluid(FluidName.fluidmonochlorobenzene, 3000, 500, false);
        registerfluid(FluidName.fluidaniline, 3000, 500, false);
        registerfluid(FluidName.fluidmethyltrichloroaniline, 3000, 500, false);
        registerfluid(FluidName.fluidtrichloroaniline, 3000, 500, false);
        registerfluid(FluidName.fluidmethylsulfate, 3000, 500, false);
        registerfluid(FluidName.fluidpropionic_acid, 3000, 500, false);
        registerfluid(FluidName.fluidacetic_acid, 3000, 500, false);
        registerfluid(FluidName.fluidglucose, 3000, 500, false);


        registerfluid(FluidName.fluidwastesulfuricacid, 3000, 500, false);
        registerfluid(FluidName.fluidsulfuroxide, -3000, 500, false);
        registerfluid(FluidName.fluidsulfurtrioxide, -3000, 500, false);
        registerfluid(FluidName.fluidhydrogensulfide, -3000, 500, false);
        registerfluid(FluidName.fluidcoppersulfate, -3000, 500, false);

        registerfluid(FluidName.fluiduu_matter, 3000, 3000, false);
        registerfluid(FluidName.fluidconstruction_foam, 10000, 50000, false);
        registerfluid(FluidName.fluidcoolant, 1000, 3000, false);

        registerfluid(FluidName.fluidapianroyaljelly, 1000, 100, false);
        registerfluid(FluidName.fluidprotein, 1000, 100, false);
        registerfluid(FluidName.fluidbeeswax, 1000, 100, false);
        registerfluid(FluidName.fluidseedoil, 1000, 100, false);
        registerfluid(FluidName.fluidbacteria, 1000, 100, false);
        registerfluid(FluidName.fluidplantmixture, 1000, 100, false);
        registerfluid(FluidName.fluidbeerna, 1000, 100, false);
        registerfluid(FluidName.fluidcroprna, 1000, 100, false);
        registerfluid(FluidName.fluidbeedna, 1000, 100, false);
        registerfluid(FluidName.fluidcropdna, 1000, 100, false);
        registerfluid(FluidName.fluidunstablemutagen, 1000, 100, false);
        registerfluid(FluidName.fluidmutagen, 1000, 100, false);
        registerfluid(FluidName.fluidbeegenetic, 1000, 100, false);
        registerfluid(FluidName.fluidcropgenetic, 1000, 100, false);


        registerfluid(FluidName.fluidhot_coolant, 1000, 3000, false);
        registerfluid(FluidName.fluidpahoehoe_lava, 50000, 250000, false);
        registerfluid(FluidName.fluidbiomass, 1000, 3000, false);
        registerfluid(FluidName.fluidbiogas, 1000, 3000, true);
        registerfluid(FluidName.fluiddistilled_water, 1000, 1000, false);
        registerfluid(FluidName.fluidweed_ex, 1000, 1000, false);
        registerfluid(FluidName.fluidsuperheated_steam, -3000, 100, true);
        registerfluid(FluidName.fluidsteam, -800, 300, true);
        registerfluid(FluidName.fluidhot_water, 1000, 1000, false);
        registerfluid(FluidName.fluidair, 0, 500, true);


        registerfluid(FluidName.fluidroyaljelly, 1000, 50, false);
        registerfluid(FluidName.fluidhoney, 1000, 50, false);

        registerfluid(FluidName.fluidmoltenmikhail, 1000, 1200, false);
        registerfluid(FluidName.fluidmoltenaluminium, 2200, 660, false);
        registerfluid(FluidName.fluidmoltenvanadium, 6000, 1910, false);
        registerfluid(FluidName.fluidmoltentungsten, 19250, 3422, false);
        registerfluid(FluidName.fluidmoltencobalt, 8000, 1495, false);
        registerfluid(FluidName.fluidmoltenmagnesium, 1700, 650, false);
        registerfluid(FluidName.fluidmoltennickel, 8900, 1455, false);
        registerfluid(FluidName.fluidmoltenplatinum, 21400, 1768, false);
        registerfluid(FluidName.fluidmoltentitanium, 4500, 1668, false);
        registerfluid(FluidName.fluidmoltenchromium, 7180, 1907, false);
        registerfluid(FluidName.fluidmoltenspinel, 5500, 2150, false);
        registerfluid(FluidName.fluidmoltensilver, 10500, 962, false);
        registerfluid(FluidName.fluidmoltenzinc, 7100, 419, false);
        registerfluid(FluidName.fluidmoltenmanganese, 7300, 1244, false);
        registerfluid(FluidName.fluidmolteniridium, 22500, 2446, false);
        registerfluid(FluidName.fluidmoltengermanium, 5320, 938, false);
        registerfluid(FluidName.fluidmoltencopper, 8900, 1085, false);
        registerfluid(FluidName.fluidmoltengold, 19300, 1064, false);
        registerfluid(FluidName.fluidmolteniron, 7850, 1538, false);
        registerfluid(FluidName.fluidmoltenlead, 11340, 327, false);
        registerfluid(FluidName.fluidmoltentin, 7300, 231, false);
        registerfluid(FluidName.fluidmoltenuranium, 18800, 1135, false);
        registerfluid(FluidName.fluidmoltenosmium, 22600, 3033, false);
        registerfluid(FluidName.fluidmoltentantalum, 16600, 3017, false);
        registerfluid(FluidName.fluidmoltencadmium, 8650, 321, false);
        registerfluid(FluidName.fluidmoltenarsenic, 5720, 613, false);
        registerfluid(FluidName.fluidmoltenbarium, 3590, 1000, false);
        registerfluid(FluidName.fluidmoltenbismuth, 9800, 271, false);
        registerfluid(FluidName.fluidmoltengadolinium, 7800, 1312, false);
        registerfluid(FluidName.fluidmoltengallium, 5900, 2204, false);
        registerfluid(FluidName.fluidmoltenhafnium, 13400, 2233, false);
        registerfluid(FluidName.fluidmoltenyttrium, 4450, 1526, false);
        registerfluid(FluidName.fluidmoltenmolybdenum, 10100, 2623, false);
        registerfluid(FluidName.fluidmoltenneodymium, 7200, 1021, false);
        registerfluid(FluidName.fluidmoltenniobium, 8600, 2477, false);
        registerfluid(FluidName.fluidmoltenpalladium, 12250, 1554, false);
        registerfluid(FluidName.fluidmoltenpolonium, 10000, 254, false);
        registerfluid(FluidName.fluidmoltenstrontium, 2600, 1373, false);
        registerfluid(FluidName.fluidmoltenthallium, 11800, 304, false);
        registerfluid(FluidName.fluidmoltenzirconium, 6500, 1855, false);


        registerfluid(FluidName.fluiddimethylhydrazine, 6500, 50, false);
        registerfluid(FluidName.fluidhydrazine, 6500, 50, false);
        registerfluid(FluidName.fluiddecane, 6500, 50, false);
        registerfluid(FluidName.fluidxenon, 6500, -50, true);
    }

    private static void registerPotions() {

        MOB_EFFECT.register("radiation", () -> {
            IUPotion.radiation = new IUPotion(MobEffectCategory.HARMFUL, 5149489);
            return IUPotion.radiation;
        });
        MOB_EFFECT.register("frostbite", () -> {
            IUPotion.frostbite = new IUPotion(MobEffectCategory.HARMFUL, 5149489);
            return IUPotion.frostbite;
        });
        MOB_EFFECT.register("poison_gas", () -> {
            IUPotion.poison_gas = new IUPotion(MobEffectCategory.HARMFUL, 5149489);
            return IUPotion.poison_gas;
        });
    }

    private static void registerfluid(
            FluidName name, int density,
            int temperature, boolean isGaseous
    ) {
        IUFluidType fluidType = new IUFluidType(name, FluidType.Properties.create().density(density).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).canDrown(!isGaseous).temperature(temperature).descriptionId("fluid." + name.getName()));
        FLUID_TYPES.register(name.name().toLowerCase() + "_types", () -> fluidType);

        properties = new ForgeFlowingFluid.Properties(() -> fluidType, () -> new ForgeFlowingFluid.Source(properties), () -> new ForgeFlowingFluid.Flowing(properties));
        FluidHandler handler = new FluidHandler(fluidType, name);
        name.setInstance(handler.source);
        name.setInstanceHandler(handler);
    }

    public static void writeItems() {
        IUItem.reinforcedStone = IUItem.blockResource.getStack(BlockResource.Type.reinforced_stone.getId());
        IUItem.reinforcedGlass = new ItemStack(IUItem.glass.getItem(0));
        IUItem.bronzeBlock = IUItem.blockResource.getStack(BlockResource.Type.bronze_block.getId());
        IUItem.copperBlock = new ItemStack(Blocks.COPPER_BLOCK);
        IUItem.tinBlock = IUItem.blockResource.getStack(BlockResource.Type.tin_block.getId());
        IUItem.uraniumBlock = IUItem.blockResource.getStack(BlockResource.Type.uranium_block.getId());
        IUItem.leadBlock = IUItem.blockResource.getStack(BlockResource.Type.lead_block.getId());
        IUItem.advironblock = IUItem.blockResource.getStack(BlockResource.Type.steel_block.getId());
        IUItem.machine = IUItem.blockResource.getStack(BlockResource.Type.machine.getId());
        IUItem.advancedMachine = IUItem.blockResource.getStack(BlockResource.Type.advanced_machine.getId());


        IUItem.UranFuel = IUItem.nuclear_res.getStack(ItemNuclearResource.Types.uranium);
        IUItem.Plutonium = IUItem.nuclear_res.getStack(ItemNuclearResource.Types.plutonium);
        IUItem.smallPlutonium = IUItem.nuclear_res.getStack(ItemNuclearResource.Types.small_plutonium);
        IUItem.Uran235 = IUItem.nuclear_res.getStack(ItemNuclearResource.Types.uranium_235);
        IUItem.smallUran235 = IUItem.nuclear_res.getStack(ItemNuclearResource.Types.small_uranium_235);
        IUItem.Uran238 = IUItem.nuclear_res.getStack(ItemNuclearResource.Types.uranium_238);
        IUItem.mox = IUItem.nuclear_res.getStack(ItemNuclearResource.Types.mox);
        IUItem.latex = new ItemStack(IUItem.crafting_elements.getItemFromMeta(290), 1);
        IUItem.rubber = new ItemStack(IUItem.crafting_elements.getItemFromMeta(271), 1);
        IUItem.elemotor = new ItemStack(IUItem.crafting_elements.getItemFromMeta(276), 1);
        IUItem.powerunit = new ItemStack(IUItem.crafting_elements.getItemFromMeta(279), 1);
        IUItem.powerunitsmall = new ItemStack(IUItem.crafting_elements.getItemFromMeta(278), 1);
        IUItem.heatconducto = new ItemStack(IUItem.crafting_elements.getItemFromMeta(277), 1);
        IUItem.cfPowder = new ItemStack(IUItem.crafting_elements.getItemFromMeta(289), 1);
        IUItem.electronicCircuit = new ItemStack(IUItem.crafting_elements.getItemFromMeta(272), 1);
        IUItem.advancedCircuit = new ItemStack(IUItem.crafting_elements.getItemFromMeta(273), 1);
        IUItem.circuitSpectral = new ItemStack(IUItem.basecircuit.getItemFromMeta(11));
        IUItem.circuitPhotonic = new ItemStack(IUItem.basecircuit.getItemFromMeta(21));
        IUItem.cirsuitQuantum = new ItemStack(IUItem.basecircuit.getItemFromMeta(10));
        IUItem.circuitNano = new ItemStack(IUItem.basecircuit.getItemFromMeta(9));
        IUItem.advancedAlloy = new ItemStack(IUItem.crafting_elements.getItemFromMeta(274), 1);
        IUItem.iridiumOre = new ItemStack(IUItem.crafting_elements.getItemFromMeta(275), 1);
        IUItem.iridiumShard = new ItemStack(IUItem.crafting_elements.getItemFromMeta(291), 1);
        IUItem.scrap = new ItemStack(IUItem.crafting_elements.getItemFromMeta(287), 1);
        IUItem.coalBall = new ItemStack(IUItem.crafting_elements.getItemFromMeta(283), 1);
        IUItem.carbonPlate = new ItemStack(IUItem.crafting_elements.getItemFromMeta(282), 1);
        IUItem.carbonMesh = new ItemStack(IUItem.crafting_elements.getItemFromMeta(281), 1);
        IUItem.carbonFiber = new ItemStack(IUItem.crafting_elements.getItemFromMeta(280), 1);
        IUItem.scrapBox = new ItemStack(IUItem.crafting_elements.getItemFromMeta(288), 1);
        IUItem.iridiumPlate = new ItemStack(IUItem.crafting_elements.getItemFromMeta(285), 1);
        IUItem.coal_chunk = new ItemStack(IUItem.crafting_elements.getItemFromMeta(286), 1);
        IUItem.rawcrystalmemory = new ItemStack(IUItem.crafting_elements.getItemFromMeta(292), 1);
        IUItem.biochaff = new ItemStack(IUItem.crafting_elements.getItemFromMeta(293), 1);
        IUItem.casingadviron = new ItemStack(IUItem.casing.getItemFromMeta(24), 1);
        IUItem.casingcopper = new ItemStack(IUItem.casing.getItemFromMeta(20), 1);
        IUItem.casingtin = new ItemStack(IUItem.casing.getItemFromMeta(25), 1);
        IUItem.casingbronze = new ItemStack(IUItem.casing.getItemFromMeta(19), 1);
        IUItem.casinggold = new ItemStack(IUItem.casing.getItemFromMeta(21), 1);
        IUItem.casingiron = new ItemStack(IUItem.casing.getItemFromMeta(22), 1);
        IUItem.casinglead = new ItemStack(IUItem.casing.getItemFromMeta(23), 1);
        IUItem.crushedIronOre = new ItemStack(IUItem.crushed.getItemFromMeta(21), 1);
        IUItem.crushedCopperOre = new ItemStack(IUItem.crushed.getItemFromMeta(19), 1);
        IUItem.crushedGoldOre = new ItemStack(IUItem.crushed.getItemFromMeta(20), 1);
        IUItem.crushedTinOre = new ItemStack(IUItem.crushed.getItemFromMeta(23), 1);
        IUItem.crushedUraniumOre = new ItemStack(IUItem.crushed.getItemFromMeta(24), 1);
        IUItem.crushedLeadOre = new ItemStack(IUItem.crushed.getItemFromMeta(22), 1);
        IUItem.purifiedCrushedIronOre = new ItemStack(IUItem.purifiedcrushed.getItemFromMeta(21), 1);
        IUItem.purifiedCrushedCopperOre = new ItemStack(IUItem.purifiedcrushed.getItemFromMeta(19), 1);
        IUItem.purifiedCrushedGoldOre = new ItemStack(IUItem.purifiedcrushed.getItemFromMeta(20), 1);
        IUItem.purifiedCrushedTinOre = new ItemStack(IUItem.purifiedcrushed.getItemFromMeta(23), 1);
        IUItem.purifiedCrushedUraniumOre = new ItemStack(IUItem.purifiedcrushed.getItemFromMeta(24), 1);
        IUItem.purifiedCrushedLeadOre = new ItemStack(IUItem.purifiedcrushed.getItemFromMeta(22), 1);
        IUItem.smallIronDust = new ItemStack(IUItem.smalldust.getItemFromMeta(22), 1);
        IUItem.smallCopperDust = new ItemStack(IUItem.smalldust.getItemFromMeta(20), 1);
        IUItem.smallGoldDust = new ItemStack(IUItem.smalldust.getItemFromMeta(21), 1);
        IUItem.smallTinDust = new ItemStack(IUItem.smalldust.getItemFromMeta(27), 1);
        IUItem.smallLeadDust = new ItemStack(IUItem.smalldust.getItemFromMeta(24), 1);
        IUItem.smallSulfurDust = new ItemStack(IUItem.smalldust.getItemFromMeta(26), 1);
        IUItem.smallObsidian = new ItemStack(IUItem.smalldust.getItemFromMeta(25), 1);
        IUItem.stoneDust = new ItemStack(IUItem.iudust.getItemFromMeta(30), 1);
        IUItem.energiumDust = new ItemStack(IUItem.iudust.getItemFromMeta(24), 1);
        IUItem.bronzeDust = new ItemStack(IUItem.iudust.getItemFromMeta(19), 1);
        IUItem.clayDust = new ItemStack(IUItem.iudust.getItemFromMeta(20), 1);
        IUItem.coalDust = new ItemStack(IUItem.iudust.getItemFromMeta(21), 1);
        IUItem.copperDust = new ItemStack(IUItem.iudust.getItemFromMeta(22), 1);
        IUItem.goldDust = new ItemStack(IUItem.iudust.getItemFromMeta(25), 1);
        IUItem.ironDust = new ItemStack(IUItem.iudust.getItemFromMeta(26), 1);
        IUItem.tinDust = new ItemStack(IUItem.iudust.getItemFromMeta(32), 1);
        IUItem.leadDust = new ItemStack(IUItem.iudust.getItemFromMeta(28), 1);
        IUItem.obsidianDust = new ItemStack(IUItem.iudust.getItemFromMeta(29), 1);
        IUItem.lapiDust = new ItemStack(IUItem.iudust.getItemFromMeta(27), 1);
        IUItem.sulfurDust = new ItemStack(IUItem.iudust.getItemFromMeta(31), 1);
        IUItem.silicondioxideDust = new ItemStack(IUItem.iudust.getItemFromMeta(33), 1);
        IUItem.diamondDust = new ItemStack(IUItem.iudust.getItemFromMeta(23), 1);
        IUItem.tinOre = IUItem.classic_ore.getItemStack(BlockClassicOre.Type.tin);
        IUItem.uraniumOre = IUItem.classic_ore.getItemStack(BlockClassicOre.Type.uranium);
        IUItem.leadOre = IUItem.classic_ore.getItemStack(BlockClassicOre.Type.lead);
        IUItem.denseplateadviron = new ItemStack(IUItem.doubleplate.getItemFromMeta(26), 1);
        IUItem.platecopper = new ItemStack(IUItem.plate.getItemFromMeta(20), 1);
        IUItem.platetin = new ItemStack(IUItem.plate.getItemFromMeta(27), 1);
        IUItem.platebronze = new ItemStack(IUItem.plate.getItemFromMeta(19), 1);
        IUItem.plategold = new ItemStack(IUItem.plate.getItemFromMeta(21), 1);
        IUItem.plateiron = new ItemStack(IUItem.plate.getItemFromMeta(22), 1);
        IUItem.platelead = new ItemStack(IUItem.plate.getItemFromMeta(24), 1);
        IUItem.platelapis = new ItemStack(IUItem.plate.getItemFromMeta(23), 1);
        IUItem.plateobsidian = new ItemStack(IUItem.plate.getItemFromMeta(25), 1);
        IUItem.plateadviron = new ItemStack(IUItem.plate.getItemFromMeta(26), 1);
        IUItem.denseplatecopper = new ItemStack(IUItem.doubleplate.getItemFromMeta(20), 1);
        IUItem.denseplatetin = new ItemStack(IUItem.doubleplate.getItemFromMeta(27), 1);
        IUItem.denseplatebronze = new ItemStack(IUItem.doubleplate.getItemFromMeta(19), 1);
        IUItem.denseplategold = new ItemStack(IUItem.doubleplate.getItemFromMeta(21), 1);
        IUItem.denseplateiron = new ItemStack(IUItem.doubleplate.getItemFromMeta(22), 1);
        IUItem.denseplatelead = new ItemStack(IUItem.doubleplate.getItemFromMeta(24), 1);
        IUItem.denseplatelapi = new ItemStack(IUItem.doubleplate.getItemFromMeta(23), 1);
        IUItem.denseplateobsidian = new ItemStack(IUItem.doubleplate.getItemFromMeta(25), 1);

        IUItem.mixedMetalIngot = new ItemStack(IUItem.iuingot.getItemFromMeta(19), 1);
        IUItem.copperIngot = new ItemStack(Items.COPPER_INGOT);
        IUItem.tinIngot = new ItemStack(IUItem.iuingot.getItemFromMeta(24), 1);
        IUItem.bronzeIngot = new ItemStack(IUItem.iuingot.getItemFromMeta(20), 1);
        IUItem.leadIngot = new ItemStack(IUItem.iuingot.getItemFromMeta(22), 1);
        IUItem.advIronIngot = new ItemStack(IUItem.iuingot.getItemFromMeta(23), 1);
        IUItem.coil = new ItemStack(IUItem.crafting_elements.getItemFromMeta(294), 1);
        IUItem.plantBall = new ItemStack(IUItem.crafting_elements.getItemFromMeta(295), 1);
        IUItem.tinCan = new ItemStack(IUItem.crafting_elements.getItemFromMeta(296), 1);
        IUItem.compressedCoalBall = new ItemStack(IUItem.crafting_elements.getItemFromMeta(297), 1);
        IUItem.copperboiler = new ItemStack(IUItem.crafting_elements.getItemFromMeta(284), 1);
        IUItem.reactorCoolantSimple = new ItemStack(IUItem.crafting_elements.getItemFromMeta(298), 1);
        IUItem.reactorCoolantTriple = new ItemStack(IUItem.crafting_elements.getItemFromMeta(299), 1);
        IUItem.reactorCoolantSix = new ItemStack(IUItem.crafting_elements.getItemFromMeta(300), 1);

        IUItem.overclockerUpgrade_1 = UpgradeRegistry.register(new ItemStack(IUItem.module.getStack(com.denfop.items.ItemUpgradeModule.Type.Overclocker1.ordinal())));
        IUItem.overclockerUpgrade1 = UpgradeRegistry.register(new ItemStack(IUItem.module.getStack(com.denfop.items.ItemUpgradeModule.Type.Overclocker2.ordinal())));
        IUItem.tranformerUpgrade = UpgradeRegistry.register(new ItemStack(IUItem.module.getStack(com.denfop.items.ItemUpgradeModule.Type.transformer.ordinal())));
        IUItem.tranformerUpgrade1 = UpgradeRegistry.register(new ItemStack(IUItem.module.getStack(com.denfop.items.ItemUpgradeModule.Type.transformer1.ordinal())));
        IUItem.lap_energystorage_upgrade = UpgradeRegistry.register(new ItemStack(IUItem.module.getStack(com.denfop.items.ItemUpgradeModule.Type.storage.ordinal())));
        IUItem.adv_lap_energystorage_upgrade = UpgradeRegistry.register(new ItemStack(IUItem.module.getStack(com.denfop.items.ItemUpgradeModule.Type.adv_storage.ordinal())));
        IUItem.imp_lap_energystorage_upgrade = UpgradeRegistry.register(new ItemStack(IUItem.module.getStack(com.denfop.items.ItemUpgradeModule.Type.imp_storage.ordinal())));
        IUItem.pullingUpgrade = UpgradeRegistry.register(new ItemStack(IUItem.module.getStack(com.denfop.items.ItemUpgradeModule.Type.pulling.ordinal())));
        IUItem.per_lap_energystorage_upgrade = UpgradeRegistry.register(new ItemStack(IUItem.module.getStack(com.denfop.items.ItemUpgradeModule.Type.per_storage.ordinal())));
        IUItem.fluidpullingUpgrade = UpgradeRegistry.register(new ItemStack(IUItem.module.getStack(com.denfop.items.ItemUpgradeModule.Type.fluid_pulling.ordinal())));
        IUItem.overclockerUpgrade = UpgradeRegistry.register(new ItemStack(IUItem.module.getStack(com.denfop.items.ItemUpgradeModule.Type.overclocker.ordinal())));
        IUItem.transformerUpgrade = UpgradeRegistry.register(new ItemStack(IUItem.module.getStack(com.denfop.items.ItemUpgradeModule.Type.transformer_simple.ordinal())));
        IUItem.energyStorageUpgrade = UpgradeRegistry.register(new ItemStack(IUItem.module.getStack(com.denfop.items.ItemUpgradeModule.Type.energy_storage.ordinal())));
        IUItem.ejectorUpgrade = UpgradeRegistry.register(new ItemStack(IUItem.module.getStack(com.denfop.items.ItemUpgradeModule.Type.ejector.ordinal())));
        IUItem.fluidEjectorUpgrade = UpgradeRegistry.register(new ItemStack(IUItem.module.getStack(com.denfop.items.ItemUpgradeModule.Type.fluid_ejector.ordinal())));
        IUItem.copperCableItem = new ItemStack(IUItem.cable.getItem(11), 1);
        IUItem.insulatedCopperCableItem = new ItemStack(IUItem.cable.getItem(12), 1);
        IUItem.glassFiberCableItem = new ItemStack(IUItem.cable.getItem(13), 1);
        IUItem.goldCableItem = new ItemStack(IUItem.cable.getItem(14), 1);
        IUItem.insulatedGoldCableItem = new ItemStack(IUItem.cable.getItem(15), 1);
        IUItem.ironCableItem = new ItemStack(IUItem.cable.getItem(16), 1);
        IUItem.insulatedIronCableItem = new ItemStack(IUItem.cable.getItem(17), 1);
        IUItem.tinCableItem = new ItemStack(IUItem.cable.getItem(18), 1);
        IUItem.insulatedTinCableItem = new ItemStack(IUItem.cable.getItem(19), 1);
        IUItem.module1 = new ItemStack(IUItem.basemodules.getStack(0));
        IUItem.module2 = new ItemStack(IUItem.basemodules.getStack(3));
        IUItem.module3 = new ItemStack(IUItem.basemodules.getStack(6));
        IUItem.module4 = new ItemStack(IUItem.basemodules.getStack(9));
        IUItem.genmodule = new ItemStack(IUItem.basemodules.getStack(1));
        IUItem.genmodule1 = new ItemStack(IUItem.basemodules.getStack(2));
        IUItem.gennightmodule = new ItemStack(IUItem.basemodules.getStack(4));
        IUItem.gennightmodule1 = new ItemStack(IUItem.basemodules.getStack(5));
        IUItem.storagemodule = new ItemStack(IUItem.basemodules.getStack(7));
        IUItem.storagemodule1 = new ItemStack(IUItem.basemodules.getStack(8));
        IUItem.outputmodule = new ItemStack(IUItem.basemodules.getStack(10));
        IUItem.outputmodule1 = new ItemStack(IUItem.basemodules.getStack(11));
        IUItem.phase_module = new ItemStack(IUItem.basemodules.getStack(12));
        IUItem.phase_module1 = new ItemStack(IUItem.basemodules.getStack(13));
        IUItem.phase_module2 = new ItemStack(IUItem.basemodules.getStack(14));
        IUItem.moonlinse_module = new ItemStack(IUItem.basemodules.getStack(15));
        IUItem.moonlinse_module1 = new ItemStack(IUItem.basemodules.getStack(16));
        IUItem.moonlinse_module2 = new ItemStack(IUItem.basemodules.getStack(17));
        IUItem.module8 = new ItemStack(IUItem.module7.getStack(10), 1);
        IUItem.FluidCell = new ItemStack(IUItem.fluidCell.getItem());
    }


}
