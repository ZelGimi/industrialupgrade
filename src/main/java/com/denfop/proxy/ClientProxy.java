package com.denfop.proxy;


import com.denfop.Config;
import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.IFluidModelProvider;
import com.denfop.api.IModelRegister;
import com.denfop.api.inv.IHasGui;
import com.denfop.blocks.FluidName;
import com.denfop.events.TickHandler;
import com.denfop.gui.GuiColorPicker;
import com.denfop.render.advoilrefiner.TileEntityAdvOilRefinerRender;
import com.denfop.render.base.IUModelLoader;
import com.denfop.render.base.RenderCoreProcess;
import com.denfop.render.multiblock.TileEntityMultiBlockRender;
import com.denfop.render.oilquarry.TileEntityQuarryOilRender;
import com.denfop.render.oilrefiner.TileEntityOilRefinerRender;
import com.denfop.render.sintezator.TileEntitySintezatorRender;
import com.denfop.render.streak.EntityRendererStreak;
import com.denfop.render.streak.EntityStreak;
import com.denfop.render.streak.EventSpectralSuitEffect;
import com.denfop.render.streak.EventStreakEffect;
import com.denfop.render.tank.TileEntityTankRender;
import com.denfop.render.tile.TileEntityAdminPanelRender;
import com.denfop.render.transport.ModelCable;
import com.denfop.render.transport.ModelCoolHeatPipes;
import com.denfop.render.transport.ModelCoolPipes;
import com.denfop.render.transport.ModelExpCable;
import com.denfop.render.transport.ModelPipes;
import com.denfop.render.transport.ModelQCable;
import com.denfop.render.transport.ModelSCable;
import com.denfop.render.transport.ModelTransportPipes;
import com.denfop.render.transport.ModelUniversalCable;
import com.denfop.render.water.WaterGeneratorRenderer;
import com.denfop.render.windgenerator.KineticGeneratorRenderer;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityAdminSolarPanel;
import com.denfop.tiles.base.TileEntityDoubleMolecular;
import com.denfop.tiles.base.TileEntityLiquedTank;
import com.denfop.tiles.base.TileEntityMolecularTransformer;
import com.denfop.tiles.base.TileEntityQuarryVein;
import com.denfop.tiles.base.TileEntitySintezator;
import com.denfop.tiles.mechanism.TileEntityAdvOilRefiner;
import com.denfop.tiles.mechanism.TileEntityOilRefiner;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockBase;
import com.denfop.tiles.mechanism.water.TileEntityBaseWaterGenerator;
import com.denfop.tiles.mechanism.wind.TileEntityWindGenerator;
import com.denfop.tiles.mechanism.worlcollector.TileEntityCrystallize;
import ic2.core.IC2;
import ic2.core.profile.ProfileManager;
import ic2.core.util.LogCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
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

import java.util.ArrayList;

public class ClientProxy extends CommonProxy {

    public static final ArrayList<IModelRegister> modelList = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();

    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        for (IModelRegister register : modelList) {
            register.registerModels();
        }
        if (Config.experiment) {
            new TickHandler();
        }

        OBJLoader.INSTANCE.addDomain(Constants.MOD_ID);
        FluidName[] var8 = FluidName.values;
        final int var2 = var8.length;

        int var3;
        for (var3 = 0; var3 < var2; ++var3) {
            FluidName name = var8[var3];
            if (!name.hasInstance()) {
                IC2.log.warn(LogCategory.Block, "The fluid " + name + " is not initialized.");
            } else {
                Fluid provider = name.getInstance();
                if (provider instanceof IFluidModelProvider) {
                    ((IFluidModelProvider) provider).registerModels(name);
                }
            }
        }
        ClientRegistry.bindTileEntitySpecialRenderer(
                TileEntityCrystallize.class,
                new RenderCoreProcess<>()
        );
        ClientRegistry.bindTileEntitySpecialRenderer(
                TileEntityMolecularTransformer.class,
                new RenderCoreProcess<>()
        );
        ClientRegistry.bindTileEntitySpecialRenderer(
                TileEntityMultiBlockBase.class,
                new TileEntityMultiBlockRender<>()
        );
        ClientRegistry.bindTileEntitySpecialRenderer(
                TileEntityDoubleMolecular.class,
                new RenderCoreProcess<>()
        );
        RenderingRegistry.registerEntityRenderingHandler(
                EntityStreak.class,
                renderManager -> new EntityRendererStreak(renderManager) {


                }
        );


        IUModelLoader loader = new IUModelLoader();
        loader.register(new ResourceLocation(Constants.MOD_ID, "models/block/wiring/cable_iu"), new ModelCable());
        loader.register(new ResourceLocation(Constants.MOD_ID, "models/block/wiring/pipes_iu"), new ModelPipes());
        loader.register(new ResourceLocation(Constants.MOD_ID, "models/block/wiring/qcable"), new ModelQCable());
        loader.register(new ResourceLocation(Constants.MOD_ID, "models/block/wiring/scable"), new ModelSCable());
        loader.register(new ResourceLocation(Constants.MOD_ID, "models/block/wiring/cool_pipes_iu"), new ModelCoolPipes());
        loader.register(new ResourceLocation(Constants.MOD_ID, "models/block/wiring/expcable"), new ModelExpCable());
        loader.register(new ResourceLocation(Constants.MOD_ID, "models/block/wiring/item_pipes"), new ModelTransportPipes());
        loader.register(new ResourceLocation(Constants.MOD_ID, "models/block/wiring/heatcold"), new ModelCoolHeatPipes());
        loader.register(new ResourceLocation(Constants.MOD_ID, "models/block/wiring/universal_cable"), new ModelUniversalCable());


        ModelLoaderRegistry.registerLoader(loader);
        ProfileManager.doTextureChanges();
        EnumMultiMachine.write();

    }

    public boolean launchGuiClient(EntityPlayer player, IHasGui inventory, boolean isAdmin) {
        this.mc.displayGuiScreen(inventory.getGui(player, isAdmin));
        return true;
    }


    @Override
    public void profilerEndStartSection(final String section) {
        if (this.isRendering()) {
            Minecraft.getMinecraft().mcProfiler.endStartSection(section);
        } else {
            super.profilerEndStartSection(section);
        }
    }

    @Override
    public void profilerEndSection() {
        if (this.isRendering()) {
            Minecraft.getMinecraft().mcProfiler.endSection();
        } else {
            super.profilerEndSection();
        }
    }

    public boolean isRendering() {
        return !this.isSimulating();
    }

    @Override
    public void profilerStartSection(final String section) {
        if (this.isRendering()) {
            Minecraft.getMinecraft().mcProfiler.startSection(section);
        } else {
            super.profilerStartSection(section);
        }
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
        if (ID == 4) {
            if (!player.inventory.armorInventory.get(2).isEmpty() && player.inventory.armorInventory
                    .get(2)
                    .getItem() == IUItem.spectral_chestplate) {
                return new GuiColorPicker(player);
            }
        }
        return null;
    }

    public void registerRecipe() {
        super.registerRecipe();

    }

    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new EventStreakEffect());

        MinecraftForge.EVENT_BUS.register(new EventSpectralSuitEffect());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySintezator.class, new TileEntitySintezatorRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityQuarryVein.class, new TileEntityQuarryOilRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAdminSolarPanel.class, new TileEntityAdminPanelRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOilRefiner.class, new TileEntityOilRefinerRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAdvOilRefiner.class, new TileEntityAdvOilRefinerRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiquedTank.class, new TileEntityTankRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindGenerator.class, new KineticGeneratorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBaseWaterGenerator.class, new WaterGeneratorRenderer());


    }

    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    public boolean addIModelRegister(IModelRegister modelRegister) {
        return modelList.add(modelRegister);
    }

}
