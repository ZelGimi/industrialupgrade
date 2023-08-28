package com.denfop.proxy;

import cofh.api.fluid.IFluidContainerItem;
import com.denfop.Config;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.IULoots;
import com.denfop.IUPotion;
import com.denfop.api.IModelRegister;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.recipe.ReplicatorRecipe;
import com.denfop.api.recipe.generators.FluidGeneratorCore;
import com.denfop.api.research.BaseResearchSystem;
import com.denfop.api.research.ResearchSystem;
import com.denfop.api.solar.SolarEnergySystem;
import com.denfop.api.space.BaseSpaceSystem;
import com.denfop.api.space.SpaceInit;
import com.denfop.api.space.SpaceNet;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.events.EventUpdate;
import com.denfop.events.IUEventHandler;
import com.denfop.integration.ae.AEIntegration;
import com.denfop.integration.avaritia.AvaritiaIntegration;
import com.denfop.integration.botania.BotaniaIntegration;
import com.denfop.integration.de.DraconicIntegration;
import com.denfop.integration.exnihilo.ExNihiloIntegration;
import com.denfop.integration.forestry.FIntegration;
import com.denfop.integration.mets.METSIntegration;
import com.denfop.integration.oc.OCIntegration;
import com.denfop.integration.projecte.ProjectEIntegration;
import com.denfop.integration.thaumcraft.ThaumcraftIntegration;
import com.denfop.integration.thermal.ThermalExpansionIntegration;
import com.denfop.items.CellType;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.upgradekit.ItemUpgradePanelKit;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipe.IInputItemStack;
import com.denfop.recipes.BaseRecipes;
import com.denfop.recipes.CannerRecipe;
import com.denfop.recipes.CentrifugeRecipe;
import com.denfop.recipes.CompressorRecipe;
import com.denfop.recipes.ExtractorRecipe;
import com.denfop.recipes.FurnaceRecipes;
import com.denfop.recipes.MaceratorRecipe;
import com.denfop.recipes.MetalFormerRecipe;
import com.denfop.recipes.OreWashingRecipe;
import com.denfop.recipes.ScrapboxRecipeManager;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.register.Register;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.tiles.base.TileConverterSolidMatter;
import com.denfop.tiles.mechanism.EnumTypeMachines;
import com.denfop.tiles.mechanism.multimechanism.simple.TileCombMacerator;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.utils.ElectricItemManager;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.MatterRecipe;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Precision;
import com.denfop.world.WorldGenOres;
import com.google.common.collect.Lists;
import forestry.api.core.IToolPipette;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonProxy implements IGuiHandler {


    public static boolean ae2;
    public static boolean gc;

    public static void sendPlayerMessage(EntityPlayer player, String text) {
        if (IUCore.proxy.isSimulating()) {
            IUCore.proxy.messagePlayer(
                    player,
                    text
            );
        }
    }

    public Object getGui() {
        return null;
    }

    public void removePotion(EntityLivingBase entity, Potion potion) {
        entity.removePotionEffect(potion);
    }

    public boolean isRendering() {
        return false;
    }


    public void preInit(FMLPreInitializationEvent event) {
        ElectricItem.manager = new ElectricItemManager();
        Recipes.registerRecipes();
        Recipes.registerWithSorter();
        Blocks.OBSIDIAN.setResistance(60.0F);
        Blocks.ENCHANTING_TABLE.setResistance(60.0F);
        Blocks.ENDER_CHEST.setResistance(60.0F);
        Blocks.ANVIL.setResistance(60.0F);
        Blocks.WATER.setResistance(30.0F);
        Blocks.FLOWING_WATER.setResistance(30.0F);
        Blocks.LAVA.setResistance(30.0F);
        IUPotion.init();
        Register.init();
        ae2 = Loader.isModLoaded("appliedenergistics2");
        gc = Loader.isModLoaded("galacticraftcore");
        if (Loader.isModLoaded("exnihilocreatio")) {
            ExNihiloIntegration.init();
        }
        if (Config.DraconicLoaded && Config.Draconic) {
            DraconicIntegration.init();
        }
        if (Config.thaumcraft && Config.Thaumcraft) {
            ThaumcraftIntegration.init();
        }
        if (Config.AvaritiaLoaded && Config.Avaritia) {
            AvaritiaIntegration.init();
        }

        if (Config.BotaniaLoaded && Config.Botania) {
            BotaniaIntegration.init();
        }
        ListInformationUtils.init();
        WorldGenOres.init();
        RegisterOreDictionary.oredict();
        CellType.register();
        IULoots.init();
        EnumSolarPanels.registerTile();
        ItemUpgradePanelKit.EnumSolarPanelsKit.registerkit();
        IUItem.register_mineral();


    }

    public void messagePlayer(EntityPlayer player, String message, Object... args) {
        if (player instanceof EntityPlayerMP) {
            TextComponentTranslation msg;
            if (args.length > 0) {
                msg = new TextComponentTranslation(message, (Object[]) this.getMessageComponents(args));
            } else {
                msg = new TextComponentTranslation(message);
            }

            player.sendMessage(msg);
        }

    }

    protected ITextComponent[] getMessageComponents(Object... args) {
        ITextComponent[] encodedArgs = new ITextComponent[args.length];

        for (int i = 0; i < args.length; ++i) {
            if (args[i] instanceof String && ((String) args[i]).startsWith("iu.")) {
                encodedArgs[i] = new TextComponentTranslation((String) args[i]);
            } else {
                encodedArgs[i] = new TextComponentString(args[i].toString());
            }
        }

        return encodedArgs;
    }

    public EntityPlayer getPlayerInstance() {
        return null;
    }

    public World getWorld(int dimId) {
        return DimensionManager.getWorld(dimId);
    }

    public World getPlayerWorld() {
        return null;
    }

    public void requestTick(boolean simulating, Runnable runnable) {
        if (!simulating) {
            throw new IllegalStateException();
        } else {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(runnable);
        }
    }

    public void init(FMLInitializationEvent event) {
        BaseRecipes.init();
        EnumTypeMachines.writeSound();
        TileBlockCreator.instance.buildBlocks();
        Recipes.recipes.initializationRecipes();
        CompressorRecipe.recipe();
        CannerRecipe.recipe();
        FurnaceRecipes.recipe();
        CentrifugeRecipe.init();
        MaceratorRecipe.recipe();
        MetalFormerRecipe.init();
        OreWashingRecipe.init();
        ExtractorRecipe.init();
        ReplicatorRecipe.init();
        new FluidGeneratorCore();
        new ScrapboxRecipeManager();
        SolarEnergySystem.system = new SolarEnergySystem();
        if (!Config.disableUpdateCheck) {
            MinecraftForge.EVENT_BUS.register(new EventUpdate());
        }
        SpaceNet.instance = new BaseSpaceSystem();
        SpaceInit.init();

        ResearchSystem.instance = new BaseResearchSystem();
        if (Loader.isModLoaded("jaopca")) {
            Recipes.recipes.removeAll("comb_macerator");
            TileCombMacerator.ores.clear();
            Recipes.recipes.reloadRecipes("com.denfop.tiles.mechanism.multimechanism.simple.TileCombMacerator");
        }
        Recipes.recipes.removeAllRecipesFromList();
        Recipes.recipes.addAllRecipesFromList();
    }

    public void postInit(FMLPostInitializationEvent event) {
        InitMultiBlockSystem.init();
        IUEventHandler sspEventHandler = new IUEventHandler();
        MinecraftForge.EVENT_BUS.register(sspEventHandler);
        Map<List<List<ItemStack>>, MatterRecipe> itemStackMap1 = new HashMap<>();
        long startTime = System.nanoTime();
        List<MatterRecipe> matterRecipeList = new ArrayList<>();
        IUCore.log.debug("Checking recipes %d ", ForgeRegistries.RECIPES.getValuesCollection().size()
        );


        for (IRecipe r : Lists.newArrayList(ForgeRegistries.RECIPES.getValuesCollection())) {
            List<List<ItemStack>> itemStackList = new ArrayList<>();
            for (Ingredient ingredient : r.getIngredients()) {
                List<ItemStack> itemStackList1;
                if (ingredient instanceof IInputItemStack) {
                    itemStackList1 = ((IInputItemStack) ingredient).getInputs();
                } else {
                    itemStackList1 = Arrays.asList(ingredient.getMatchingStacks());
                }
                if (!itemStackList1.isEmpty()) {
                    itemStackList.add(itemStackList1);
                }
            }
            final ItemStack output = r.getRecipeOutput();
            final NBTTagCompound nbt = ModUtils.nbtOrNull(output);

            if ((nbt != null && nbt.hasKey("RSControl")) || output.getItem() instanceof IFluidContainerItem || output.getItem() instanceof IToolPipette) {
                continue;
            }
            if (Recipes.recipes.getRecipeOutput("converter", false, output) != null) {
                continue;
            }
            MatterRecipe matterRecipe = new MatterRecipe(r.getRecipeOutput());
            matterRecipeList.add(matterRecipe);
            itemStackMap1.put(itemStackList, matterRecipe);
        }
        IUCore.log.debug(
                "Finished checking recipes for converter matter after %d ms.",
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
                    TileConverterSolidMatter.addrecipe(matterRecipe1.getStack(),
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
                }

            });
            matterRecipeList.removeAll(matterRecipeList1);
            itemStackMap3.forEach((key, value) -> itemStackMap1.remove(key));
            IUCore.log.debug("Finished  %d stage recipes for converter matter after %d ms. ", i,
                    (System.nanoTime() - startTime) / 1000000L
            );
            startTime = System.nanoTime();
        }

        IUCore.log.debug("Finished adding recipes for converter matter after %d ms.", (System.nanoTime() - startTime1) / 1000000L
        );
        matterRecipeList.clear();
        itemStackMap1.clear();
        IUItem.machineRecipe = Recipes.recipes.getRecipeStack("converter");
        IUItem.fluidMatterRecipe = Recipes.recipes.getRecipeStack("replicator");

        writeRecipe();
        for (List<ItemStack> stack : IUCore.removing_list) {
            Recipes.recipes.removeAllRecipe("furnace", new RecipeOutput(null, stack));
        }

        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        for (int i = 0; i < 8; i++) {
            Recipes.recipes.addRecipe(
                    "matter",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput(new ItemStack(IUItem.matter, 1, i))
                            ),
                            new RecipeOutput(null, new ItemStack(IUItem.matter, 1, i))
                    )
            );
        }
        if (Loader.isModLoaded("forestry")) {
            FIntegration.init();
        }
        if (Loader.isModLoaded("projecte") && Config.ProjectE) {
            ProjectEIntegration.init();
        }


        if (Loader.isModLoaded("mets")) {
            METSIntegration.init();
        }
    }

    public void registerRecipe() {

        if (Config.BotaniaLoaded && Config.Botania) {
            BotaniaIntegration.recipe();
        }
        if (Config.DraconicLoaded && Config.Draconic) {
            DraconicIntegration.Recipes();
        }
        if (Config.AvaritiaLoaded && Config.Avaritia) {
            AvaritiaIntegration.recipe();
        }
        if (Loader.isModLoaded("appliedenergistics2")) {
            AEIntegration.init();
        }
        if (Loader.isModLoaded("thermalexpansion")) {
            ThermalExpansionIntegration.init();
        }
        if (Loader.isModLoaded("opencomputers")) {
            OCIntegration.init();
        }

    }

    private void writeRecipe() {
        net.minecraft.item.crafting.FurnaceRecipes recipes = net.minecraft.item.crafting.FurnaceRecipes.instance();
        final Map<ItemStack, ItemStack> map = recipes.getSmeltingList();
        ItemStack output;
        ItemStack input;
        final IInputHandler inputFactory = com.denfop.api.Recipes.inputFactory;

        for (Map.Entry<ItemStack, ItemStack> entry : map.entrySet()) {
            output = entry.getValue();
            input = entry.getKey();
            if (input.isEmpty()) {
                continue;
            }
            NBTTagCompound nbt = new NBTTagCompound();
            try {
                nbt.setFloat("experience", recipes.getSmeltingExperience(output));
            } catch (Exception e) {
                nbt.setFloat("experience", 0.1F);

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
    }

    public boolean addIModelRegister(IModelRegister modelRegister) {
        return false;
    }


    @Nullable
    @Override
    public Object getServerGuiElement(
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
                return inventory.getInventory(player, stack).getGuiContainer(player);
            }
        }
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if (tile != null) {
            if (tile instanceof IAdvInventory) {
                return ((IAdvInventory<?>) tile).getGuiContainer(player);
            }
        }
        return null;
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
        return null;
    }

    public boolean isSimulating() {
        return !FMLCommonHandler.instance().getEffectiveSide().isClient();
    }


    public void playSoundSp(String sound, float f, float g) {
    }


}
