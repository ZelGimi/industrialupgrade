package com.denfop.proxy;


import com.denfop.Config;
import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.IFluidModelProvider;
import com.denfop.api.IModelRegister;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.FluidName;
import com.denfop.events.ElectricItemTooltipHandler;
import com.denfop.events.TickHandler;
import com.denfop.gui.GuiColorPicker;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.book.core.CoreBook;
import com.denfop.render.advoilrefiner.TileEntityAdvOilRefinerRender;
import com.denfop.render.base.RenderCoreProcess;
import com.denfop.render.multiblock.TileEntityMultiBlockRender;
import com.denfop.render.oilquarry.TileEntityQuarryOilRender;
import com.denfop.render.oilrefiner.TileEntityOilRefinerRender;
import com.denfop.render.panel.TileEntityMiniPanelRender;
import com.denfop.render.panel.TileEntitySolarPanelRender;
import com.denfop.render.sintezator.TileEntitySintezatorRender;
import com.denfop.render.streak.EventSpectralSuitEffect;
import com.denfop.render.tank.TileEntityTankRender;
import com.denfop.render.tile.TileEntityAdminPanelRender;
import com.denfop.render.transport.TileEntityCableRender;
import com.denfop.render.water.WaterGeneratorRenderer;
import com.denfop.render.windgenerator.KineticGeneratorRenderer;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileAdminSolarPanel;
import com.denfop.tiles.base.TileDoubleMolecular;
import com.denfop.tiles.base.TileEntityLiquedTank;
import com.denfop.tiles.base.TileMolecularTransformer;
import com.denfop.tiles.base.TileQuarryVein;
import com.denfop.tiles.base.TileSintezator;
import com.denfop.tiles.mechanism.TileAdvOilRefiner;
import com.denfop.tiles.mechanism.TileOilRefiner;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.tiles.mechanism.water.TileBaseWaterGenerator;
import com.denfop.tiles.mechanism.wind.TileWindGenerator;
import com.denfop.tiles.mechanism.worlcollector.TileCrystallize;
import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
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
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ClientProxy extends CommonProxy {

    public static final ArrayList<IModelRegister> modelList = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();
    private GuiScreen gui;

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
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMiniPanels.class, new TileEntityMiniPanelRender<>());

        ClientRegistry.bindTileEntitySpecialRenderer(TileSintezator.class, new TileEntitySintezatorRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileQuarryVein.class, new TileEntityQuarryOilRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAdminSolarPanel.class, new TileEntityAdminPanelRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileOilRefiner.class, new TileEntityOilRefinerRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAdvOilRefiner.class, new TileEntityAdvOilRefinerRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiquedTank.class, new TileEntityTankRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileWindGenerator.class, new KineticGeneratorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBaseWaterGenerator.class, new WaterGeneratorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMultiCable.class, new TileEntityCableRender<>());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSolarPanel.class, new TileEntitySolarPanelRender<>());


        EnumMultiMachine.write();

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
            World world = this.mc.world;
            return world.provider.getDimension() == dimId ? world : null;
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
                this.gui = inventory.getInventory(player, stack).getGui(player, false);
                return this.gui;
            }
        }
        if (ID == 2) {
            final ItemStack stack = player.inventory.armorInventory.get(1);
            if (stack.getItem() instanceof IItemStackInventory) {
                IItemStackInventory inventory = (IItemStackInventory) stack.getItem();
                this.gui = inventory.getInventory(player, stack).getGui(player, false);
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
    }

    public boolean addIModelRegister(IModelRegister modelRegister) {
        return modelList.add(modelRegister);
    }

}
