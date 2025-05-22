package com.denfop.proxy;


import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.IFluidModelProvider;
import com.denfop.api.IModelRegister;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.FluidName;
import com.denfop.entity.EntityNuclearBombPrimed;
import com.denfop.events.ElectricItemTooltipHandler;
import com.denfop.events.TickHandler;
import com.denfop.gui.GuiColorPicker;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.ItemStackInventory;
import com.denfop.items.book.core.CoreBook;
import com.denfop.render.advoilrefiner.TileEntityAdvOilRefinerRender;
import com.denfop.render.anvil.RenderItemAnvil;
import com.denfop.render.anvil.RenderItemStrongAnvil;
import com.denfop.render.base.IUModelLoader;
import com.denfop.render.base.RenderCoreProcess;
import com.denfop.render.compressor.TileEntityRenderCompressor;
import com.denfop.render.crop.CropRender;
import com.denfop.render.crop.TileEntityDoubleCropRender;
import com.denfop.render.dryer.TileEntityRenderDryer;
import com.denfop.render.entity.RenderNuclearBombPrimed;
import com.denfop.render.fluidcell.FluidCellModel;
import com.denfop.render.fluidheater.TileEntityRenderFluidHeater;
import com.denfop.render.fluidintegrator.TileEntityRenderFluidIntegrator;
import com.denfop.render.gaschamber.TileEntityRenderGasChamber;
import com.denfop.render.impoilrefiner.TileEntityImpOilRefinerRender;
import com.denfop.render.macerator.TileEntityRenderMacerator;
import com.denfop.render.multiblock.TileEntityMultiBlockRender;
import com.denfop.render.oilquarry.TileEntityQuarryOilRender;
import com.denfop.render.oilrefiner.TileEntityOilRefinerRender;
import com.denfop.render.panel.TileEntityMiniPanelRender;
import com.denfop.render.panel.TileEntitySolarPanelRender;
import com.denfop.render.primal_laser_polisher.PrimalLaserPolisherRender;
import com.denfop.render.primal_silicon_crystal_handler.PrimalSiliconCrystalHandlerRender;
import com.denfop.render.pump.TileEntityRenderPump;
import com.denfop.render.rolling.RenderItemRolling;
import com.denfop.render.sintezator.TileEntitySintezatorRender;
import com.denfop.render.squeezer.TileEntityRenderSqueezer;
import com.denfop.render.steam.TileEntityRenderSteamStorage;
import com.denfop.render.steam.TileEntityRenderSteamTankBoiler;
import com.denfop.render.steam_turbine_tank.TileEntityRenderSteamTurbineTank;
import com.denfop.render.streak.EventSpectralSuitEffect;
import com.denfop.render.tank.TileEntityTankRender;
import com.denfop.render.transport.ModelCable;
import com.denfop.render.transport.TileEntityCableRender;
import com.denfop.render.water.WaterGeneratorRenderer;
import com.denfop.render.windgenerator.WindGeneratorRenderer;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileDoubleMolecular;
import com.denfop.tiles.base.TileEntityAnvil;
import com.denfop.tiles.base.TileEntityLiquedTank;
import com.denfop.tiles.base.TileEntityStrongAnvil;
import com.denfop.tiles.base.TileMolecularTransformer;
import com.denfop.tiles.base.TileQuantumMolecular;
import com.denfop.tiles.base.TileQuarryVein;
import com.denfop.tiles.base.TileSintezator;
import com.denfop.tiles.crop.TileEntityCrop;
import com.denfop.tiles.mechanism.TileAdvOilRefiner;
import com.denfop.tiles.mechanism.TileEntityCompressor;
import com.denfop.tiles.mechanism.TileEntityDryer;
import com.denfop.tiles.mechanism.TileEntityMacerator;
import com.denfop.tiles.mechanism.TileEntityPrimalFluidHeater;
import com.denfop.tiles.mechanism.TileEntityPrimalFluidIntegrator;
import com.denfop.tiles.mechanism.TileEntityPrimalGasChamber;
import com.denfop.tiles.mechanism.TileEntityPrimalLaserPolisher;
import com.denfop.tiles.mechanism.TileEntityPrimalPump;
import com.denfop.tiles.mechanism.TileEntityPrimalSiliconCrystalHandler;
import com.denfop.tiles.mechanism.TileEntityRollingMachine;
import com.denfop.tiles.mechanism.TileEntitySqueezer;
import com.denfop.tiles.mechanism.TileImpOilRefiner;
import com.denfop.tiles.mechanism.TileOilRefiner;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.tiles.mechanism.steam.TileSteamStorage;
import com.denfop.tiles.mechanism.steamboiler.TileEntitySteamTankBoiler;
import com.denfop.tiles.mechanism.steamturbine.tank.TileEntityBaseSteamTurbineTank;
import com.denfop.tiles.mechanism.water.TileBaseWaterGenerator;
import com.denfop.tiles.mechanism.wind.TileWindGenerator;
import com.denfop.tiles.mechanism.worlcollector.TileCrystallize;
import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import com.denfop.tiles.transport.types.AmpereType;
import com.denfop.tiles.transport.types.BioType;
import com.denfop.tiles.transport.types.CableType;
import com.denfop.tiles.transport.types.CoolType;
import com.denfop.tiles.transport.types.ExpType;
import com.denfop.tiles.transport.types.HeatColdType;
import com.denfop.tiles.transport.types.HeatType;
import com.denfop.tiles.transport.types.ICableItem;
import com.denfop.tiles.transport.types.ItemType;
import com.denfop.tiles.transport.types.NightType;
import com.denfop.tiles.transport.types.QEType;
import com.denfop.tiles.transport.types.RadTypes;
import com.denfop.tiles.transport.types.SEType;
import com.denfop.tiles.transport.types.SteamType;
import com.denfop.tiles.transport.types.UniversalType;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class ClientProxy extends CommonProxy {

    public static final List<IModelRegister> modelList = new LinkedList<>();

    public static final List<ICableItem> cableItemTextureAspire = new LinkedList<>();
    public static CropRender cropRender;
    private final Minecraft mc = Minecraft.getMinecraft();
    public GuiScreen gui;
    public ItemStackInventory invent;

    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        for (IModelRegister register : modelList) {
            register.registerModels();
        }
        new TickHandler();

        OBJLoader.INSTANCE.addDomain(Constants.MOD_ID);
        FluidName[] var8 = FluidName.values;
        final int var2 = var8.length;

        int var3;
        for (var3 = 0; var3 < var2; ++var3) {
            FluidName name = var8[var3];
            if (name.hasInstance()) {
                Fluid provider = name.getInstance();
                if (provider instanceof IFluidModelProvider) {
                    ((IFluidModelProvider) provider).registerModels(name);
                }
            }
        }
        ClientRegistry.bindTileEntitySpecialRenderer(
                TileCrystallize.class,
                new RenderCoreProcess<>()
        );
        ClientRegistry.bindTileEntitySpecialRenderer(
                TileMolecularTransformer.class,
                new RenderCoreProcess<>()
        );
        ClientRegistry.bindTileEntitySpecialRenderer(
                TileMultiBlockBase.class,
                new TileEntityMultiBlockRender<>()
        );
        ClientRegistry.bindTileEntitySpecialRenderer(
                TileDoubleMolecular.class,
                new RenderCoreProcess<>()
        );
        ClientRegistry.bindTileEntitySpecialRenderer(
                TileQuantumMolecular.class,
                new RenderCoreProcess<>()
        );
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMiniPanels.class, new TileEntityMiniPanelRender<>());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAnvil.class, new RenderItemAnvil());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStrongAnvil.class, new RenderItemStrongAnvil());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSintezator.class, new TileEntitySintezatorRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileOilRefiner.class, new TileEntityOilRefinerRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAdvOilRefiner.class, new TileEntityAdvOilRefinerRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileImpOilRefiner.class, new TileEntityImpOilRefinerRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiquedTank.class, new TileEntityTankRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileWindGenerator.class, new WindGeneratorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBaseWaterGenerator.class, new WaterGeneratorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSolarPanel.class, new TileEntitySolarPanelRender<>());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMultiCable.class, new TileEntityCableRender<>());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRollingMachine.class, new RenderItemRolling());
        ClientRegistry.bindTileEntitySpecialRenderer(TileQuarryVein.class, new TileEntityQuarryOilRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySqueezer.class, new TileEntityRenderSqueezer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDryer.class, new TileEntityRenderDryer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMacerator.class, new TileEntityRenderMacerator());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCompressor.class, new TileEntityRenderCompressor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrimalFluidHeater.class, new TileEntityRenderFluidHeater());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrimalGasChamber.class, new TileEntityRenderGasChamber());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSteamStorage.class, new TileEntityRenderSteamStorage());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamTankBoiler.class, new TileEntityRenderSteamTankBoiler());
        ClientRegistry.bindTileEntitySpecialRenderer(
                TileEntityBaseSteamTurbineTank.class,
                new TileEntityRenderSteamTurbineTank()
        );
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrop.class, new TileEntityDoubleCropRender());


        ClientRegistry.bindTileEntitySpecialRenderer(
                TileEntityPrimalFluidIntegrator.class,
                new TileEntityRenderFluidIntegrator()
        );
        ClientRegistry.bindTileEntitySpecialRenderer(
                TileEntityPrimalSiliconCrystalHandler.class,
                new PrimalSiliconCrystalHandlerRender()
        );
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrimalLaserPolisher.class, new PrimalLaserPolisherRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrimalPump.class, new TileEntityRenderPump());


        EnumMultiMachine.write();
        RenderingRegistry.registerEntityRenderingHandler(
                EntityNuclearBombPrimed.class,
                RenderNuclearBombPrimed::new
        );
        IUModelLoader loader = new IUModelLoader();
        loader.register("models/block/wiring/cable_iu", new ModelCable(CableType.values()));
        loader.register("models/block/wiring/cool/cool_pipes", new ModelCable(CoolType.values()));
        loader.register("models/block/wiring/universal_cable", new ModelCable(UniversalType.values()));
        loader.register("models/block/wiring/scable/scable_scable", new ModelCable(SEType.values()));
        loader.register("models/block/wiring/npipe/npipe_npipe", new ModelCable(NightType.values()));
        loader.register("models/block/wiring/qcable/qcable_qcable", new ModelCable(QEType.values()));
        loader.register("models/block/wiring/pipes/pipes_pipes", new ModelCable(HeatType.values()));
        loader.register("models/block/wiring/item_pipes/itemcable_pipes", new ModelCable(ItemType.values()));
        loader.register("models/block/wiring/heatcold/heatcool_pipes", new ModelCable(HeatColdType.values()));
        loader.register("models/block/wiring/radcable/rad_cable_radcable", new ModelCable(RadTypes.values()));
        loader.register("models/block/wiring/expcable/expcable_expcable", new ModelCable(ExpType.values()));
        loader.register("models/block/wiring/spipe/spipe_spipe", new ModelCable(SteamType.values()));
        loader.register("models/block/wiring/bpipe/bpipe_bpipe", new ModelCable(BioType.values()));
        loader.register("models/block/wiring/acable/acable_acable", new ModelCable(AmpereType.values()));

        cropRender = new CropRender();
        loader.register("models/block/crop/crop", cropRender);
        loader.register("models/item/itemcell/itemcell", new FluidCellModel());
        ModelLoaderRegistry.registerLoader(loader);
    }

    public void requestTick(boolean simulating, Runnable runnable) {
        if (simulating) {
            super.requestTick(simulating, runnable);
        } else {
            this.mc.addScheduledTask(runnable);
        }

    }

    public EntityPlayer getPlayerInstance() {
        return this.mc.player;
    }

    public World getWorld(int dimId) {
        if (this.isSimulating()) {
            return super.getWorld(dimId);
        } else {

            return getPlayerWorld();
        }
    }

    public World getPlayerWorld() {
        return this.mc.world;
    }

    public void messagePlayer(EntityPlayer player, String message, Object... args) {
        if (args.length > 0) {
            this.mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(
                    message,
                    (Object[]) this.getMessageComponents(args)
            ));
        } else {
            this.mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(message));
        }

    }

    public void playSoundSp(String sound, float f, float g) {
        this.getPlayerInstance().playSound(EnumSound.getSondFromString(sound), f, g);
    }

    public boolean isRendering() {
        return !this.isSimulating();
    }

    public GuiScreen getGui() {
        return gui;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(
            final int ID,
            final EntityPlayer player,
            final World world,
            final int x,
            final int y,
            final int z
    ) {
        if (ID == 1) {
            final ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
            if (stack.getItem() instanceof IItemStackInventory) {
                IItemStackInventory inventory = (IItemStackInventory) stack.getItem();
                invent = (ItemStackInventory) inventory.getInventory(player, stack);
                this.gui = invent.getGui(player, false);
                return this.gui;
            }
        }
        if (ID == 2) {
            final ItemStack stack = player.inventory.armorInventory.get(1);
            if (stack.getItem() instanceof IItemStackInventory) {
                IItemStackInventory inventory = (IItemStackInventory) stack.getItem();
                invent = (ItemStackInventory) inventory.getInventory(player, stack);
                this.gui = invent.getGui(player, false);
                return this.gui;
            }
        }
        if (ID == 4) {
            if (!player.inventory.armorInventory.get(2).isEmpty() && player.inventory.armorInventory
                    .get(2)
                    .getItem() == IUItem.spectral_chestplate) {
                return new GuiColorPicker(player);
            }
        }
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if (tile != null) {
            if (tile instanceof IAdvInventory) {
                return ((IAdvInventory<?>) tile).getGui(player, false);
            }
        }
        return null;
    }

    public void registerRecipe() {
        super.registerRecipe();

    }

    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new EventSpectralSuitEffect());
    }

    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        new ElectricItemTooltipHandler();
        CoreBook.init();
        final Block leaves = IUItem.leaves;
        this.mc.getBlockColors().registerBlockColorHandler((state, worldIn, pos, tintIndex) -> ModUtils.convertRGBcolorToInt(10
                , 96, 8), leaves);
        this.mc.getItemColors().registerItemColorHandler((stack, tintIndex) -> ModUtils.convertRGBcolorToInt(10
                , 96, 8), leaves);

    }

    public boolean addIModelRegister(IModelRegister modelRegister) {
        return modelList.add(modelRegister);
    }

    public boolean addTextureAspire(ICableItem modelRegister) {
        return cableItemTextureAspire.add(modelRegister);
    }

    public List<ICableItem> getTextureAtlas() {
        return cableItemTextureAspire;
    }

}
