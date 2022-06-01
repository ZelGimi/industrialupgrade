package com.denfop;

import aroma1997.uncomplication.enet.EnergyNetGlobal;
import com.denfop.api.IElectricBlock;
import com.denfop.api.IStorage;
import com.denfop.api.Recipes;
import com.denfop.api.cooling.CoolNet;
import com.denfop.api.exp.EXPNet;
import com.denfop.api.heat.HeatNet;
import com.denfop.api.qe.QENet;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.ListRecipes;
import com.denfop.api.se.SENet;
import com.denfop.api.upgrade.BaseUpgradeSystem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.vein.VeinSystem;
import com.denfop.audio.AudioManager;
import com.denfop.blocks.BlockIUFluid;
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
import com.denfop.blocks.mechanism.BlockCoolPipes;
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
import com.denfop.blocks.mechanism.BlockPipes;
import com.denfop.blocks.mechanism.BlockQCable;
import com.denfop.blocks.mechanism.BlockQuarryVein;
import com.denfop.blocks.mechanism.BlockRefiner;
import com.denfop.blocks.mechanism.BlockSCable;
import com.denfop.blocks.mechanism.BlockSimpleMachine;
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
import com.denfop.cool.CoolNetGlobal;
import com.denfop.events.TickHandler;
import com.denfop.events.TickHandlerIU;
import com.denfop.exp.EXPNetGlobal;
import com.denfop.heat.HeatNetGlobal;
import com.denfop.integration.avaritia.BlockAvaritiaSolarPanel;
import com.denfop.integration.botania.BlockBotSolarPanel;
import com.denfop.integration.de.BlockDESolarPanel;
import com.denfop.integration.thaumcraft.BlockThaumSolarPanel;
import com.denfop.items.energy.ItemQuantumSaber;
import com.denfop.items.energy.ItemSpectralSaber;
import com.denfop.items.modules.EnumModule;
import com.denfop.network.NetworkManager;
import com.denfop.proxy.CommonProxy;
import com.denfop.qe.QENetGlobal;
import com.denfop.se.SENetGlobal;
import com.denfop.tabs.TabCore;
import com.denfop.utils.KeyboardIU;
import com.denfop.utils.Keys;
import com.denfop.utils.ModUtils;
import ic2.api.energy.EnergyNet;
import ic2.api.event.TeBlockFinalCallEvent;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.core.IC2;
import ic2.core.block.ITeBlock;
import ic2.core.block.TeBlockRegistry;
import ic2.core.util.SideGateway;
import ic2.core.util.Util;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@SuppressWarnings({"ALL", "UnnecessaryFullyQualifiedName"})
@Mod.EventBusSubscriber
@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, dependencies = Constants.MOD_DEPS, version = Constants.MOD_VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", certificateFingerprint = Constants.MOD_CERTIFICATE)
public final class IUCore {

    public static final CreativeTabs SSPTab = new TabCore(0, "IUTab");
    public static final CreativeTabs tabssp1 = new TabCore(1, "ModuleTab");
    public static final CreativeTabs ItemTab = new TabCore(2, "ItemTab");
    public static final CreativeTabs OreTab = new TabCore(3, "OreTab");
    public static final CreativeTabs EnergyTab = new TabCore(4, "EnergyTab");
    public static final CreativeTabs RecourseTab = new TabCore(5, "ResourceTab");
    public static final CreativeTabs ReactorsTab = new TabCore(6, "ReactorsTab");
    public static final CreativeTabs UpgradeTab = new TabCore(7, "UpgradeTab");
    public static final CreativeTabs BlueprintTab = new TabCore(8, "BlueprintTab");

    public static final Map<Integer, EnumModule> modules = new HashMap<>();
    public static final List<ItemStack> list = new ArrayList<>();
    public static final List<ItemStack> get_ore = new ArrayList<>();
    public static final List<ItemStack> get_ingot = new ArrayList<>();
    public static final List<ItemStack> get_crushed = new ArrayList<>();
    public static final List<ItemStack> get_comb_crushed = new ArrayList<>();
    public static Logger log;
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
        IUCore.network = new SideGateway<>("com.denfop.network.NetworkManager", "com.denfop.network.NetworkManagerClient");

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
        register(BlockSimpleMachine.class, BlockSimpleMachine.IDENTITY);
        register(BlockAdvChamber.class, BlockAdvChamber.IDENTITY);
        register(BlockImpChamber.class, BlockImpChamber.IDENTITY);
        register(BlockPerChamber.class, BlockPerChamber.IDENTITY);
        register(BlockTransformer.class, BlockTransformer.IDENTITY);
        register(BlockDoubleMolecularTransfomer.class, BlockDoubleMolecularTransfomer.IDENTITY);
        register(BlockAdminPanel.class, BlockAdminPanel.IDENTITY);
        register(BlockCable.class, BlockCable.IDENTITY);
        register(BlockPipes.class, BlockPipes.IDENTITY);
        register(BlockQuarryVein.class, BlockQuarryVein.IDENTITY);
        register(BlockQCable.class, BlockQCable.IDENTITY);
        register(BlockSCable.class, BlockSCable.IDENTITY);
        register(BlockCoolPipes.class, BlockCoolPipes.IDENTITY);
        register(BlockTank.class, BlockTank.IDENTITY);
        register(BlockConverterMatter.class, BlockConverterMatter.IDENTITY);
        Config.Thaumcraft = Loader.isModLoaded("thaumcraft");
        Config.DraconicLoaded = Loader.isModLoaded("draconicevolution");
        Config.AvaritiaLoaded = Loader.isModLoaded("avaritia");
        Config.BotaniaLoaded = Loader.isModLoaded("botania");
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
            register(BlockThaumSolarPanel.class, BlockThaumSolarPanel.IDENTITY);
        }


    }

    public static ResourceLocation getIdentifier(final String name) {
        return new ResourceLocation(Constants.MOD_ID, name);
    }

    public static void initENet() {
        if (Config.newsystem) {
            EnergyNet.instance = EnergyNetGlobal.initialize();
        } else {
            EnergyNet.instance = aroma1997.uncomplication.enet.old.EnergyNetGlobal.initialize();
        }
        HeatNet.instance = HeatNetGlobal.initialize();
        CoolNet.instance = CoolNetGlobal.initialize();
        QENet.instance = QENetGlobal.initialize();
        SENet.instance = SENetGlobal.initialize();
        EXPNet.instance = EXPNetGlobal.initialize();
        new VeinSystem();
        new RadiationSystem();
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (IC2.platform.isSimulating()) {
            keyboard.removePlayerReferences(event.player);
        }

    }

    @Mod.EventHandler
    public void load(final FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ModUtils.log = event.getModLog();
        IUCore.log = event.getModLog();
        Config.loadNormalConfig(event.getSuggestedConfigurationFile());
        UpgradeSystem.system = new BaseUpgradeSystem();
        Recipes.recipes = new ListRecipes();
        proxy.regrecipemanager();
        MinecraftForge.EVENT_BUS.register(new TickHandlerIU());
        if (Config.experiment) {
            new TickHandler();
        }

        IUCore.audioManager.initialize();
        Keys.instance = IUCore.keyboard;
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

    void addInList(ItemStack stack) {
        boolean add = true;
        for (int i = 0; i < get_ingot.size(); i++) {
            if (get_ingot.get(i).isItemEqual(stack)) {
                add = false;
                break;
            }
        }
        if (add) {
            get_ingot.add(stack);
        }
    }

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        proxy.postInit(event);


        addInList1(new ItemStack(Items.DIAMOND));
        addInList1(new ItemStack(Items.EMERALD));
        addInList1(new ItemStack(Items.REDSTONE));
        addInList1(new ItemStack(Items.DYE, 1, 4));
        addInList1(new ItemStack(Items.COAL));
        addInList1(new ItemStack(Items.GLOWSTONE_DUST));


        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isItemEqual(Ic2Items.iridiumOre)) {
                list.remove(i);
            }
        }

        list.add(new ItemStack(IUItem.ore, 1, 14));


        addOre("oreCoal");
        addOre("oreIron");
        addOre("oreGold");
        addOre("oreDiamond");
        addOre("oreRedstone");
        addOre("oreEmerald");
        addOre1("oreIron");
        addOre1("oreGold");
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
        for (ItemStack stack : IUCore.list) {
            BaseMachineRecipe recipe = Recipes.recipes.getRecipeOutput("furnace", false, stack);
            if (recipe != null) {
                this.get_ingot.add(recipe.getOutput().items.get(0));
            } else {
                this.get_ingot.add(stack);
            }
        }
        final Iterable<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipe = ic2.api.recipe.Recipes.compressor.getRecipes();
        final Iterator<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> iter = recipe.iterator();
        List<MachineRecipe<IRecipeInput, Collection<ItemStack>>> lst = new ArrayList<>();
        while (iter.hasNext()) {
            MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe1 = iter.next();
            List<ItemStack> list = (List<ItemStack>) recipe1.getOutput();
            if (list.get(0).isItemEqual(Ic2Items.iridiumOre)) {
                iter.remove();
                break;
            }
        }

    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onViewRenderFogDensity(EntityViewRenderEvent.FogDensity event) {
        if (event.getState().getBlock() instanceof BlockIUFluid) {
            event.setCanceled(true);
            Fluid fluid = ((BlockIUFluid) event.getState().getBlock()).getFluid();
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            event.setDensity((float) Util.map(Math.abs(fluid.getDensity()), 20000.0D, 2.0D));
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ++ItemQuantumSaber.ticker;
            ++ItemSpectralSaber.ticker;
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

    public void removeOre(String name) {
        if (OreDictionary.getOres(name).size() >= 1) {
            if (list.contains(OreDictionary.getOres(name).get(0))) {
                list.remove(OreDictionary.getOres(name).get(0));

            }
        }
    }

}
