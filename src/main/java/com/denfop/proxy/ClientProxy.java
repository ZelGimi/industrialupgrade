package com.denfop.proxy;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.item.MultiBlockItem;
import com.denfop.blockentity.base.*;
import com.denfop.blockentity.creative.BlockEntityCreativeSteamStorage;
import com.denfop.blockentity.mechanism.*;
import com.denfop.blockentity.mechanism.steam.BlockEntitySteamStorage;
import com.denfop.blockentity.mechanism.steamboiler.BlockEntitySteamTankBoiler;
import com.denfop.blockentity.mechanism.steamturbine.tank.BlockEntityBaseSteamTurbineTank;
import com.denfop.blockentity.mechanism.water.BlockEntityBaseWaterGenerator;
import com.denfop.blockentity.mechanism.wind.BlockEntityWindGenerator;
import com.denfop.blockentity.mechanism.worlcollector.BlockEntityCrystallize;
import com.denfop.blockentity.panels.entity.BlockEntityMiniPanels;
import com.denfop.blockentity.panels.entity.BlockEntitySolarPanel;
import com.denfop.blockentity.transport.tiles.BlockEntityMultiCable;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.blocks.blockitem.ItemBlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.blocks.mechanism.BlockCreativeBlocksEntity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.effects.BreakingItemParticle;
import com.denfop.effects.EffectsRegister;
import com.denfop.effects.SteamAshParticle;
import com.denfop.events.ElectricItemTooltipHandler;
import com.denfop.events.TickHandler;
import com.denfop.events.client.EventAutoQuests;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.items.IProperties;
import com.denfop.items.upgradekit.ItemUpgradeMachinesKit;
import com.denfop.mixin.access.RenderChunkRegionAccessor;
import com.denfop.register.Register;
import com.denfop.render.TileEntityRenderGasChamber;
import com.denfop.render.advoilrefiner.TileEntityAdvOilRefinerRender;
import com.denfop.render.anvil.RenderItemAnvil;
import com.denfop.render.autocollector.TileEntityRenderAutoLatexCollector;
import com.denfop.render.base.DynamicFluidContainerModel;
import com.denfop.render.base.NuclearBombRenderer;
import com.denfop.render.base.RenderCoreProcess;
import com.denfop.render.base.SmallBeeRenderer;
import com.denfop.render.compressor.TileEntityRenderCompressor;
import com.denfop.render.dryer.TileEntityRenderDryer;
import com.denfop.render.fluidheater.TileEntityRenderFluidHeater;
import com.denfop.render.fluidintegrator.PrimalFluidIntegratorRenderer;
import com.denfop.render.imp_refiner.TileEntityImpOilRefinerRender;
import com.denfop.render.macerator.TileEntityRenderMacerator;
import com.denfop.render.oilquarry.QuarryOilRenderer;
import com.denfop.render.oilrefiner.TileEntityOilRefinerRender;
import com.denfop.render.panel.TileEntityMiniPanelRender;
import com.denfop.render.panel.TileEntitySolarPanelRender;
import com.denfop.render.primal_laser_polisher.PrimalLaserPolisherRender;
import com.denfop.render.primal_silicon_crystal_handler.PrimalSiliconCrystalHandlerRender;
import com.denfop.render.pump.TileEntityRenderPump;
import com.denfop.render.rolling.RenderItemRolling;
import com.denfop.render.sintezator.TileEntitySintezatorRenderer;
import com.denfop.render.squeezer.TileEntityRenderSqueezer;
import com.denfop.render.steam.SteamTankBoilerRenderer;
import com.denfop.render.steam.TileEntityRenderCreativeSteamStorage;
import com.denfop.render.steam.TileEntityRenderSteamStorage;
import com.denfop.render.steam_turbine_tank.TileEntityRenderSteamTurbineTank;
import com.denfop.render.streak.EventSpectralSuitEffect;
import com.denfop.render.stronganvil.RenderItemStrongAnvil;
import com.denfop.render.tank.TileEntityTankRender;
import com.denfop.render.transport.TileEntityCableRenderer;
import com.denfop.render.water.WaterGeneratorRenderer;
import com.denfop.render.windgenerator.KineticGeneratorRenderer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;

import static com.denfop.register.Register.containerBase;
import static com.denfop.register.Register.inventory_container;

public class ClientProxy extends CommonProxy {

    private final Minecraft mc = Minecraft.getInstance();
    List<IProperties> propertiesList = new ArrayList<>();

    public ClientProxy() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onRegisterAdditionalModels);
        modEventBus.addListener(this::registerParticleFactories);
        modEventBus.addListener(this::registerBlockColor);
        modEventBus.addListener(this::registerItemColor);
        modEventBus.addListener(this::registerRenderers);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::registerKeys);
        modEventBus.addListener(this::onRegisterGeometryLoaders);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventAutoQuests());
        new GlobalRenderManager();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {

        event.register("fluid_container", DynamicFluidContainerModel.Loader.INSTANCE);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(IUItem.entity_nuclear_bomb.get(), NuclearBombRenderer::new);
        event.registerEntityRenderer(IUItem.entity_bee.get(), SmallBeeRenderer::new);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onServerTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ++ItemUpgradeMachinesKit.tick;
            if (ItemUpgradeMachinesKit.tick % 40 == 0) {
                for (int i = 0; i < ItemUpgradeMachinesKit.inform.length; i++) {
                    final List<ItemStack> list1 = IUItem.map_upgrades.get(i);
                    if (list1 != null)
                        ItemUpgradeMachinesKit.inform[i] = ++ItemUpgradeMachinesKit.inform[i] % list1.size();
                }
            }
            ++ListInformationUtils.tick;
            if (ListInformationUtils.tick % 40 == 0) {
                if (ListInformationUtils.mechanism_info.size() > 0)
                    ListInformationUtils.index = (ListInformationUtils.index + 1) % ListInformationUtils.mechanism_info.size();
                if (ListInformationUtils.mechanism_info1.size() > 0)
                    ListInformationUtils.index1 = (ListInformationUtils.index1 + 1) % ListInformationUtils.mechanism_info1
                            .values()
                            .size();
                if (ListInformationUtils.mechanism_info2.size() > 0)
                    ListInformationUtils.index2 = (ListInformationUtils.index2 + 1) % ListInformationUtils.mechanism_info2.size();

            }
        }

    }

    @SubscribeEvent
    public void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.register(EffectsRegister.STEAM_ASH.get(), SteamAshParticle.Factory::new);
        event.register(EffectsRegister.ANVIL.get(), BreakingItemParticle.AnvilProvider::new);

    }

    @SubscribeEvent
    public void registerBlockColor(RegisterColorHandlersEvent.Block event) {
        event.register((state, world, pos, tintIndex) -> {

            Level level = null;
            if (world instanceof RenderChunkRegion)
                level = ((RenderChunkRegionAccessor) world).getLevel();
            if (world instanceof Level)
                level = (Level) world;
            if (level == null)
                return ModUtils.convertRGBcolorToInt(10
                        , 96, 8);
            Holder<Biome> biome = level.getBiome(pos);
            if (biome.is(Tags.Biomes.IS_SWAMP))
                return ModUtils.convertRGBcolorToInt(105
                        , 122, 93);
            if (biome.is(BiomeTags.IS_JUNGLE))
                return ModUtils.convertRGBcolorToInt(12
                        , 82, 32);
            return ModUtils.convertRGBcolorToInt(10
                    , 96, 8);
        }, IUItem.leaves.getBlock().get());

    }


    @SubscribeEvent
    public void registerItemColor(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> ModUtils.convertRGBcolorToInt(10
                , 96, 8), IUItem.leaves.getBlock().get());
        event.register(new DynamicFluidContainerModel.Colors(), IUItem.fluidCell.getItem());


    }

    public Level getWorld(ResourceKey<Level> dim) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server != null && dim != null ? server.getLevel(dim) : (Minecraft.getInstance() == null ? null : Minecraft.getInstance().level);
    }

    @OnlyIn(Dist.CLIENT)
    public void registerKeys(RegisterKeyMappingsEvent event) {
        IUCore.keyboard.register(event);
    }

    @Override
    public void preInit() {
        super.preInit();
        MenuScreens.register(
                containerBase.get(),
                (MenuScreens.ScreenConstructor<ContainerMenuBase<? extends CustomWorldContainer>, ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>>>) (menu, inventory, p_96217_) -> {
                    return ((CustomWorldContainer) menu.base).getGui(inventory.player, menu);
                }
        );
        MenuScreens.register(
                inventory_container.get(),
                (MenuScreens.ScreenConstructor<ContainerMenuBase<? extends CustomWorldContainer>, ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>>>) (menu, inventory, p_96217_) -> {
                    CustomWorldContainer stackInventory = ((CustomWorldContainer) menu.base);
                    return stackInventory.getGui(inventory.player, menu);
                }
        );
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityAnvil>) IUItem.anvil.getBlock(0).getValue().getBlockType(), RenderItemAnvil::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityPrimalFluidHeater>) IUItem.primalFluidHeater.getBlock(0).getValue().getBlockType(), TileEntityRenderFluidHeater::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityPrimalGasChamber>) IUItem.gasChamber.getBlock(0).getValue().getBlockType(), TileEntityRenderGasChamber::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntitySqueezer>) IUItem.squeezer.getBlock(0).getValue().getBlockType(), TileEntityRenderSqueezer::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityPrimalLaserPolisher>) IUItem.primalPolisher.getBlock(0).getValue().getBlockType(), PrimalLaserPolisherRender::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityLiquedTank>) IUItem.tank.getBlock(0).getValue().getBlockType(), TileEntityTankRender::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityLiquedTank>) IUItem.tank.getBlock(1).getValue().getBlockType(), TileEntityTankRender::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityLiquedTank>) IUItem.tank.getBlock(2).getValue().getBlockType(), TileEntityTankRender::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityLiquedTank>) IUItem.tank.getBlock(3).getValue().getBlockType(), TileEntityTankRender::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityLiquedTank>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.oak_tank).getValue().getBlockType(), TileEntityTankRender::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityLiquedTank>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.steel_tank).getValue().getBlockType(), TileEntityTankRender::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityDryer>) IUItem.dryer.getBlock().getValue().getBlockType(), TileEntityRenderDryer::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityPrimalSiliconCrystalHandler>) IUItem.primalSiliconCrystal.getBlock().getValue().getBlockType(), PrimalSiliconCrystalHandlerRender::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityMolecularTransformer>) IUItem.blockmolecular.getBlock().getValue().getBlockType(), RenderCoreProcess::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityCompressor>) IUItem.blockCompressor.getBlock().getValue().getBlockType(), TileEntityRenderCompressor::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityQuarryVein>) IUItem.oilquarry.getBlock().getValue().getBlockType(), QuarryOilRenderer::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityDoubleMolecular>) IUItem.blockdoublemolecular.getBlock().getValue().getBlockType(), RenderCoreProcess::new);

        BlockEntityMultiCable.list.forEach(cable -> BlockEntityRenderers.register(cable, TileEntityCableRenderer::new));
        BlockEntitySolarPanel.list.forEach(panel -> BlockEntityRenderers.register(panel, TileEntitySolarPanelRender::new));

        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityPrimalFluidIntegrator>) IUItem.fluidIntegrator.getBlock().getValue().getBlockType(), PrimalFluidIntegratorRenderer::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityOilRefiner>) IUItem.oilrefiner.getBlock().getValue().getBlockType(), TileEntityOilRefinerRender::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityAdvOilRefiner>) IUItem.oiladvrefiner.getBlock().getValue().getBlockType(), TileEntityAdvOilRefinerRender::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityImpOilRefiner>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.imp_refiner).getValue().getBlockType(), TileEntityImpOilRefinerRender::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityRollingMachine>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.rolling_machine).getValue().getBlockType(), RenderItemRolling::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityMacerator>) IUItem.blockMacerator.getBlock().getValue().getBlockType(), TileEntityRenderMacerator::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityPrimalPump>) IUItem.primal_pump.getBlock().getValue().getBlockType(), TileEntityRenderPump::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityStrongAnvil>) IUItem.strong_anvil.getBlock(0).getValue().getBlockType(), RenderItemStrongAnvil::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntitySintezator>) IUItem.blocksintezator.getBlock(0).getValue().getBlockType(), TileEntitySintezatorRenderer::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntitySteamTankBoiler>) IUItem.steam_boiler.getBlock(2).getValue().getBlockType(), SteamTankBoilerRenderer::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityMiniPanels>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.minipanel).getValue().getBlockType(), TileEntityMiniPanelRender::new);


        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityBaseSteamTurbineTank>) IUItem.steam_turbine.getBlock(13).getValue().getBlockType(), TileEntityRenderSteamTurbineTank::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityBaseSteamTurbineTank>) IUItem.steam_turbine.getBlock(14).getValue().getBlockType(), TileEntityRenderSteamTurbineTank::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityBaseSteamTurbineTank>) IUItem.steam_turbine.getBlock(15).getValue().getBlockType(), TileEntityRenderSteamTurbineTank::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityBaseSteamTurbineTank>) IUItem.steam_turbine.getBlock(16).getValue().getBlockType(), TileEntityRenderSteamTurbineTank::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntitySteamStorage>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.steam_storage).getValue().getBlockType(), TileEntityRenderSteamStorage::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityCrystallize>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.crystallize).getValue().getBlockType(), RenderCoreProcess::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityQuantumMolecular>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.quantum_transformer).getValue().getBlockType(), RenderCoreProcess::new);

        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityWindGenerator>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.simple_wind_generator).getValue().getBlockType(), KineticGeneratorRenderer::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityWindGenerator>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.adv_wind_generator).getValue().getBlockType(), KineticGeneratorRenderer::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityWindGenerator>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.imp_wind_generator).getValue().getBlockType(), KineticGeneratorRenderer::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityWindGenerator>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.per_wind_generator).getValue().getBlockType(), KineticGeneratorRenderer::new);

        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityBaseWaterGenerator>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.simple_water_generator).getValue().getBlockType(), WaterGeneratorRenderer::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityBaseWaterGenerator>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.adv_water_generator).getValue().getBlockType(), WaterGeneratorRenderer::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityBaseWaterGenerator>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.imp_water_generator).getValue().getBlockType(), WaterGeneratorRenderer::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityBaseWaterGenerator>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.per_water_generator).getValue().getBlockType(), WaterGeneratorRenderer::new);

        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityCreativeSteamStorage>) IUItem.creativeBlock.getBlock(BlockCreativeBlocksEntity.creative_steam_storage).getValue().getBlockType(), TileEntityRenderCreativeSteamStorage::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityLiquedTank>) IUItem.creativeBlock.getBlock(BlockCreativeBlocksEntity.creative_tank_storage).getValue().getBlockType(), TileEntityTankRender::new);
        BlockEntityRenderers.register((BlockEntityType<? extends BlockEntityAutoLatexCollector>) IUItem.basemachine2.getBlock(BlockBaseMachine3Entity.auto_latex_collector).getValue().getBlockType(), TileEntityRenderAutoLatexCollector::new);


        MinecraftForge.EVENT_BUS.register(new EventSpectralSuitEffect());
        EnumMultiMachine.write();
        new TickHandler();
    }


    @OnlyIn(Dist.CLIENT)
    private void onRegisterAdditionalModels(ModelEvent.BakingCompleted event) {
        for (RegistryObject<Item> registryObject : Register.ITEMS.getEntries()) {
            Item item = registryObject.get();
            if (item instanceof ItemBlockCore<?>) {
                ItemBlockCore<?> blockCore = (ItemBlockCore<?>) item;
                ModelResourceLocation model = new ModelResourceLocation(blockCore.getRegistryName(), "inventory");
                ModelResourceLocation modelBlock = BlockModelShaper.stateToModelLocation(blockCore.getBlock().defaultBlockState());
                if (blockCore instanceof ItemBlockTileEntity) {
                    ItemBlockTileEntity blockTileEntity = (ItemBlockTileEntity) blockCore;
                    if (blockTileEntity.getElement() instanceof MultiBlockItem) {
                        MultiBlockItem blockItem = (MultiBlockItem) blockTileEntity.getElement();
                        if (!blockItem.hasUniqueRender(null)) {
                            event.getModels().replace(model, event.getModels().get(modelBlock));
                        }
                    } else {
                        event.getModels().replace(model, event.getModels().get(modelBlock));
                    }
                } else
                    event.getModels().replace(model, event.getModels().get(modelBlock));
            }
        }
    }


    public void messagePlayer(Player player, String message) {
        if (player != null) {
            player.displayClientMessage(Component.translatable(message), false);
        }
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() ->
        {
            IUCore.proxy.getPropertiesList().forEach(properties -> {
                        for (String property : properties.properties())
                            ItemProperties.register((Item) properties,
                                    new ResourceLocation(property), (p_174676_, p_174677_, p_174678_, p_174679_) -> properties.getItemProperty(p_174676_, p_174677_, p_174678_, p_174679_, property));
                    }
            );

        });
        new ElectricItemTooltipHandler();
    }

    public List<IProperties> getPropertiesList() {
        return propertiesList;
    }

    public void addProperties(IProperties properties) {
        if (!propertiesList.contains(properties))
            propertiesList.add(properties);
    }

    public Player getPlayerInstance() {
        return this.mc.player;
    }
}
