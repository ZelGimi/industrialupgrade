package com.denfop;

import com.denfop.api.IElectricBlock;
import com.denfop.api.IStorage;
import com.denfop.audio.AudioManager;
import com.denfop.blocks.BlockIUFluid;
import com.denfop.blocks.BlocksItems;
import com.denfop.blocks.mechanism.BlockAdminPanel;
import com.denfop.blocks.mechanism.BlockAdvChamber;
import com.denfop.blocks.mechanism.BlockAdvRefiner;
import com.denfop.blocks.mechanism.BlockAdvSolarEnergy;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockCable;
import com.denfop.blocks.mechanism.BlockCombinerSolid;
import com.denfop.blocks.mechanism.BlockConverterMatter;
import com.denfop.blocks.mechanism.BlockDoubleMolecularTransfomer;
import com.denfop.blocks.mechanism.BlockImpChamber;
import com.denfop.blocks.mechanism.BlockImpSolarEnergy;
import com.denfop.blocks.mechanism.BlockMolecular;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.blocks.mechanism.BlockPerChamber;
import com.denfop.blocks.mechanism.BlockPetrolQuarry;
import com.denfop.blocks.mechanism.BlockQuarryVein;
import com.denfop.blocks.mechanism.BlockRefiner;
import com.denfop.blocks.mechanism.BlockSintezator;
import com.denfop.blocks.mechanism.BlockSolarEnergy;
import com.denfop.blocks.mechanism.BlockSolidMatter;
import com.denfop.blocks.mechanism.BlockSunnariumMaker;
import com.denfop.blocks.mechanism.BlockSunnariumPanelMaker;
import com.denfop.blocks.mechanism.BlockTank;
import com.denfop.blocks.mechanism.BlockTransformer;
import com.denfop.blocks.mechanism.BlockUpgradeBlock;
import com.denfop.blocks.mechanism.IUChargepadStorage;
import com.denfop.blocks.mechanism.IUStorage;
import com.denfop.blocks.mechanism.SSPBlock;
import com.denfop.events.TickHandlerIU;
import com.denfop.integration.avaritia.BlockAvaritiaSolarPanel;
import com.denfop.integration.botania.BlockBotSolarPanel;
import com.denfop.integration.de.BlockDESolarPanel;
import com.denfop.integration.thaumcraft.blockThaumcraftSolarPanel;
import com.denfop.items.CellType;
import com.denfop.items.ItemUpgradePanelKit;
import com.denfop.items.modules.EnumModule;
import com.denfop.network.NetworkManager;
import com.denfop.proxy.CommonProxy;
import com.denfop.register.Register;
import com.denfop.register.RegisterOreDict;
import com.denfop.tabs.IUTab;
import com.denfop.tiles.base.EnumUpgradeMultiMachine;
import com.denfop.tiles.mechanism.EnumUpgradesMultiMachine;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.wiring.EnumElectricBlockState;
import com.denfop.utils.KeyboardIU;
import com.denfop.utils.Keys;
import com.denfop.utils.ListInformation;
import com.denfop.utils.ModUtils;
import com.denfop.world.GenOre;
import ic2.api.event.TeBlockFinalCallEvent;
import ic2.core.ExplosionIC2;
import ic2.core.IC2;
import ic2.core.block.ITeBlock;
import ic2.core.block.TeBlockRegistry;
import ic2.core.util.SideGateway;
import ic2.core.util.Util;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@SuppressWarnings({"ALL", "UnnecessaryFullyQualifiedName"})
@Mod.EventBusSubscriber
@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, dependencies = Constants.MOD_DEPS, version = Constants.MOD_VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", certificateFingerprint = Constants.MOD_CERTIFICATE)
public final class IUCore {

    public static final CreativeTabs SSPTab = new IUTab(0, "IUTab");
    public static final CreativeTabs tabssp1 = new IUTab(1, "ModuleTab");
    public static final CreativeTabs ItemTab = new IUTab(2, "ItemTab");
    public static final CreativeTabs OreTab = new IUTab(3, "OreTab");
    public static final CreativeTabs EnergyTab = new IUTab(4, "EnergyTab");

    public static Logger log;
    public static final Map<Integer, EnumModule> modules = new HashMap<>();

    @SidedProxy(clientSide = "com.denfop.proxy.ClientProxy", serverSide = "com.denfop.proxy.CommonProxy")
    public static CommonProxy proxy;
    @SidedProxy(clientSide = "com.denfop.audio.AudioManagerClient", serverSide = "com.denfop.audio.AudioManager")

    public static AudioManager audioManager;
    @Mod.Instance("industrialupgrade")
    public static IUCore instance;
    @SidedProxy(
            clientSide = "com.denfop.utils.KeyboardClient",
            serverSide = "com.denfop.utils.KeyboardIU"
    )
    public static KeyboardIU keyboard;

    public static SideGateway<NetworkManager> network;

    static {
        FluidRegistry.enableUniversalBucket();
        IUCore.instance = new IUCore();
        Keys.instance = IUCore.keyboard;
        IUCore.network = new SideGateway("com.denfop.network.NetworkManager", "com.denfop.network.NetworkManagerClient");

    }
    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (IC2.platform.isSimulating()) {
            keyboard.removePlayerReferences(event.player);
        }

    }
    public static boolean isSimulating() {
        return !FMLCommonHandler.instance().getEffectiveSide().isClient();
    }


    public static <E extends Enum<E> & ITeBlock> void register(Class<E> enumClass, ResourceLocation ref) {
        TeBlockRegistry.addAll(enumClass, ref);
        TeBlockRegistry.setDefaultMaterial(ref, Material.ROCK);
        TeBlockRegistry.addCreativeRegisterer((list, block, itemblock, tab) -> {
            if (tab == CreativeTabs.SEARCH || tab == SSPTab) {
                block.getAllTypes().forEach(type -> {
                    if (type.hasItem()) {
                        list.add(block.getItemStack(type));
                        if (ref.equals(IUStorage.IDENTITY) || ref.equals(IUChargepadStorage.IDENTITY)) {
                            ItemStack filled = block.getItemStack(type);
                            ModUtils.nbt(filled).setDouble(
                                    "energy",
                                    ((IStorage) (((IElectricBlock) type).getDummyElec())).getEUCapacity()
                            );
                            ModUtils.nbt(filled).setDouble(
                                    "energy2",
                                    ((IStorage) (((IElectricBlock) type).getDummyElec())).getRFCapacity()
                            );
                            list.add(filled);
                        }
                    }
                });
            }
        }, ref);
    }

    @SubscribeEvent
    public static void register(final TeBlockFinalCallEvent event) {

        register(SSPBlock.class, SSPBlock.IDENTITY);
        register(IUStorage.class, IUStorage.IDENTITY);
        register(IUChargepadStorage.class, IUChargepadStorage.IDENTITY);
        register(BlockMoreMachine.class, BlockMoreMachine.IDENTITY);
        register(BlockMoreMachine1.class, BlockMoreMachine1.IDENTITY);
        register(BlockMoreMachine2.class, BlockMoreMachine2.IDENTITY);
        register(BlockMoreMachine3.class, BlockMoreMachine3.IDENTITY);
        register(BlockBaseMachine.class, BlockBaseMachine.IDENTITY);
        register(BlockSolidMatter.class, BlockSolidMatter.IDENTITY);
        register(BlockCombinerSolid.class, BlockCombinerSolid.IDENTITY);
        register(BlockSolarEnergy.class, BlockSolarEnergy.IDENTITY);
        register(BlockAdvSolarEnergy.class, BlockAdvSolarEnergy.IDENTITY);
        register(BlockImpSolarEnergy.class, BlockImpSolarEnergy.IDENTITY);
        register(BlockMolecular.class, BlockMolecular.IDENTITY);
        register(BlockBaseMachine1.class, BlockBaseMachine1.IDENTITY);
        register(BlockBaseMachine2.class, BlockBaseMachine2.IDENTITY);
        register(BlockBaseMachine3.class, BlockBaseMachine3.IDENTITY);
        register(BlockSintezator.class, BlockSintezator.IDENTITY);
        register(BlockSunnariumMaker.class, BlockSunnariumMaker.IDENTITY);
        register(BlockSunnariumPanelMaker.class, BlockSunnariumPanelMaker.IDENTITY);
        register(BlockRefiner.class, BlockRefiner.IDENTITY);
        register(BlockAdvRefiner.class, BlockAdvRefiner.IDENTITY);
        register(BlockUpgradeBlock.class, BlockUpgradeBlock.IDENTITY);
        register(BlockPetrolQuarry.class, BlockPetrolQuarry.IDENTITY);

        register(BlockAdvChamber.class, BlockAdvChamber.IDENTITY);
        register(BlockImpChamber.class, BlockImpChamber.IDENTITY);
        register(BlockPerChamber.class, BlockPerChamber.IDENTITY);
        register(BlockTransformer.class, BlockTransformer.IDENTITY);
        register(BlockDoubleMolecularTransfomer.class, BlockDoubleMolecularTransfomer.IDENTITY);
        register(BlockAdminPanel.class, BlockAdminPanel.IDENTITY);
        register(BlockCable.class, BlockCable.IDENTITY);
        register(BlockQuarryVein.class, BlockQuarryVein.IDENTITY);

        register(BlockTank.class, BlockTank.IDENTITY);
        register(BlockConverterMatter.class, BlockConverterMatter.IDENTITY);
        Config.Thaumcraft = Loader.isModLoaded("thaumcraft");
        Config.DraconicLoaded = Loader.isModLoaded("draconicevolution");
        Config.AvaritiaLoaded = Loader.isModLoaded("avaritia");
        Config.BotaniaLoaded = Loader.isModLoaded("botania");
        Config.EnchantingPlus = Loader.isModLoaded("eplus");
        Config.MineFactory = Loader.isModLoaded("MineFactoryReloaded");
        if (Config.AvaritiaLoaded) {
            register(BlockAvaritiaSolarPanel.class, BlockAvaritiaSolarPanel.IDENTITY);
        }

        if (Config.BotaniaLoaded) {
            register(BlockBotSolarPanel.class, BlockBotSolarPanel.IDENTITY);
        }
        if (Config.DraconicLoaded) {
            register(BlockDESolarPanel.class, BlockDESolarPanel.IDENTITY);
        }
        if (Config.Thaumcraft) {
            register(blockThaumcraftSolarPanel.class, blockThaumcraftSolarPanel.IDENTITY);
        }


    }


    public static ResourceLocation getIdentifier(final String name) {
        return new ResourceLocation(Constants.MOD_ID, name);
    }

    @Mod.EventHandler
    public void load(final FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(this);
        IUCore.log = event.getModLog();
        Config.loadConfig(event.getSuggestedConfigurationFile(), event.getSide().isClient());
        //    MainConfig.get().get("misc/enableEnetExplosions").set(false);
        proxy.regrecipemanager();
        MinecraftForge.EVENT_BUS.register(new TickHandlerIU());
        FMLCommonHandler.instance().bus().register(new TickHandlerIU());
        IUCore.audioManager.initialize();
        Keys.instance = IUCore.keyboard;
        BlocksItems.init();
        Register.init();
        SSPBlock.buildDummies();
        IUStorage.buildDummies();
        BlockMoreMachine.buildDummies();
        BlockMoreMachine1.buildDummies();
        BlockMoreMachine2.buildDummies();
        BlockMoreMachine3.buildDummies();
        IUChargepadStorage.buildDummies();
        BlockBaseMachine.buildDummies();
        BlockSolidMatter.buildDummies();
        BlockCombinerSolid.buildDummies();

        BlockSolarEnergy.buildDummies();
        BlockAdvSolarEnergy.buildDummies();
        BlockImpSolarEnergy.buildDummies();
        BlockMolecular.buildDummies();
        BlockSintezator.buildDummies();
        BlockSunnariumMaker.buildDummies();
        BlockSunnariumPanelMaker.buildDummies();
        BlockRefiner.buildDummies();
        BlockAdvRefiner.buildDummies();
        BlockUpgradeBlock.buildDummies();
        BlockPetrolQuarry.buildDummies();
        BlockAdvChamber.buildDummies();
        BlockImpChamber.buildDummies();
        BlockPerChamber.buildDummies();
        BlockPerChamber.buildDummies();
        BlockBaseMachine1.buildDummies();
        BlockBaseMachine2.buildDummies();
        BlockBaseMachine3.buildDummies();
        BlockTransformer.buildDummies();
        BlockDoubleMolecularTransfomer.buildDummies();
        BlockAdminPanel.buildDummies();
        BlockCable.buildDummies();
        BlockTank.buildDummies();
        BlockConverterMatter.buildDummies();

        BlockQuarryVein.buildDummies();
        if (Config.AvaritiaLoaded) {
            BlockAvaritiaSolarPanel.buildDummies();
        }
        if (Config.BotaniaLoaded) {
            BlockBotSolarPanel.buildDummies();
        }
        if (Config.DraconicLoaded) {
            BlockDESolarPanel.buildDummies();
        }
        if (Config.Thaumcraft) {
            blockThaumcraftSolarPanel.buildDummies();
        }
        proxy.preInit(event);
        ListInformation.init();
        GenOre.init();
        RegisterOreDict.oredict();

        CellType.register();

        IULoot.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        EnumSolarPanels.registerTile();
        ItemUpgradePanelKit.EnumSolarPanelsKit.registerkit();
        if (event.getSide().isClient()) {
            ForgeHooksClient.registerTESRItemStack(
                    Item.getItemFromBlock(IUItem.blockmolecular), 0,
                    BlockMolecular.molecular.getTeClass()
            );
        }

    }


    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        proxy.init(event);

        SPPRecipes.addCraftingRecipes();
        proxy.registerRecipe();

    }

    @SubscribeEvent
    public void getore(OreDictionary.OreRegisterEvent event) {
        String oreClass = event.getName();
        if (oreClass.startsWith("ore")) {
            if (oreClass.equals("oreChargedCertusQuartz")) {
                return;
            }
            if (oreClass.equals("oreCertusQuartz")) {
                return;
            }
            if (get_ore == null) {

                assert false;
                get_ore.addAll(OreDictionary.getOres(oreClass));
            } else if (!get_ore.contains(OreDictionary.getOres(oreClass).get(0))) {
                get_ore.addAll(OreDictionary.getOres(oreClass));

            }

        }

        if (oreClass.startsWith("ingot")) {
            String temp = oreClass.substring(5);
            String tempore = "ore" + temp;
            if (get_ingot == null) {
                if (OreDictionary.getOres(tempore).size() >= 1) {
                    get_ingot.add(OreDictionary.getOres(oreClass).get(0));
                }

            } else {
                if (OreDictionary.getOres(tempore).size() >= 1) {
                    if (!get_ingot.contains(OreDictionary.getOres(oreClass).get(0))) {
                        get_ingot.add(OreDictionary.getOres(oreClass).get(0));

                    }
                }
            }
        }
        if (oreClass.startsWith("gem")) {
            String temp = oreClass.substring(3);
            String tempore = "ore" + temp;
            if (get_ingot == null) {
                if (OreDictionary.getOres(tempore).size() >= 1) {
                    get_ingot.add(OreDictionary.getOres(oreClass).get(0));
                }

            } else {
                if (OreDictionary.getOres(tempore).size() >= 1) {
                    if (!get_ingot.contains(OreDictionary.getOres(oreClass).get(0))) {
                        get_ingot.add(OreDictionary.getOres(oreClass).get(0));

                    }
                }
            }
        }

        if (oreClass.startsWith("shard")) {
            String temp = oreClass.substring(5);
            String tempore = "ore" + temp;
            if (get_ingot == null) {
                if (OreDictionary.getOres(tempore).size() >= 1) {
                    get_ingot.add(OreDictionary.getOres(oreClass).get(0));
                }

            } else {
                if (OreDictionary.getOres(tempore).size() >= 1) {
                    if (!get_ingot.contains(OreDictionary.getOres(oreClass).get(0))) {
                        get_ingot.add(OreDictionary.getOres(oreClass).get(0));

                    }
                }
            }
        }
        if (oreClass.startsWith("ore")) {
            if (oreClass.equals("oreChargedCertusQuartz")) {
                return;
            }
            if (oreClass.equals("oreCertusQuartz")) {
                return;
            }
            String temp = oreClass.substring(3);

            if (OreDictionary.getOres("gem" + temp) == null || OreDictionary.getOres("gem" + temp).size() < 1) {
                if (!list.contains(OreDictionary.getOres(oreClass).get(0))) {
                    list.add(OreDictionary.getOres(oreClass).get(0));
                }

            } else {
                if (!list.contains(OreDictionary.getOres("gem" + temp).get(0))) {
                    list.add(OreDictionary.getOres("gem" + temp).get(0));
                    get_ingot.add(OreDictionary.getOres("gem" + temp).get(0));
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
            if (!get_ingot.contains(OreDictionary.getOres(oreClass).get(0))) {
                get_ingot.add(OreDictionary.getOres(oreClass).get(0));
            }


        }
    }

    public static final List<ItemStack> list = new ArrayList<>();
    public static final List<ItemStack> get_ore = new ArrayList<>();
    public static final List<ItemStack> get_ingot = new ArrayList<>();

    private static ic2.core.util.Config config;

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        proxy.postInit(event);
        EnumUpgradesMultiMachine.register();


        list.add(new ItemStack(Items.DIAMOND));
        list.add(new ItemStack(Items.EMERALD));
        list.add(new ItemStack(Items.REDSTONE));
        list.add(new ItemStack(Items.DYE, 1, 4));
        list.add(new ItemStack(Items.COAL));
        list.add(new ItemStack(Items.GLOWSTONE_DUST));
        get_ingot.add(new ItemStack(Items.DIAMOND));
        get_ingot.add(new ItemStack(Items.EMERALD));
        get_ingot.add(new ItemStack(Items.REDSTONE));
        get_ingot.add(new ItemStack(Items.DYE, 1, 4));
        get_ingot.add(new ItemStack(Items.COAL));
        get_ingot.add(new ItemStack(Items.GLOWSTONE_DUST));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isItemEqual(Ic2Items.iridiumOre)) {
                list.remove(i);
            }
        }
        list.add(new ItemStack(IUItem.ore, 1, 14));
        for (int i = 0; i < get_ingot.size(); i++) {
            if (get_ingot.get(i).isItemEqual(Ic2Items.iridiumOre)) {
                get_ingot.remove(i);
            }
        }
        get_ingot.add(new ItemStack(IUItem.iuingot, 1, 14));

    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onViewRenderFogDensity(EntityViewRenderEvent.FogDensity event) {
        if (event.getState().getBlock() instanceof BlockIUFluid) {
            event.setCanceled(true);
            Fluid fluid = ((BlockIUFluid) event.getState().getBlock()).getFluid();
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            event.setDensity((float) Util.map((double) Math.abs(fluid.getDensity()), 20000.0D, 2.0D));
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

}
