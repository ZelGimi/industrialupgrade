package com.powerutils;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.energy.event.TileLoadEvent;
import com.denfop.api.energy.event.TileUnLoadEvent;
import com.denfop.api.energy.event.TilesUpdateEvent;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.integration.ae.AEIntegration;
import com.denfop.tiles.mechanism.TileGenerationMicrochip;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"ALL", "UnnecessaryFullyQualifiedName"})
@Mod.EventBusSubscriber
@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, dependencies = Constants.MOD_DEPS, version = Constants.MOD_VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", certificateFingerprint = Constants.MOD_CERTIFICATE)
public final class PowerUtils {


    public static final List<IModelRender> modelList = new ArrayList<>();
    public static BlockTileEntity itemPowerConverter;


    public static ResourceLocation getIdentifier(final String name) {
        return new ResourceLocation(Constants.MOD_ID, name);
    }

    public static void addIModelRegister(IModelRender puItemBase) {
        modelList.add(puItemBase);
    }


    @Mod.EventHandler
    public void load(final FMLPreInitializationEvent event) {
        PowerItem.init();

        itemPowerConverter = TileBlockCreator.instance.create(BlockPowerConverter.class);
        if (event.getSide() == Side.CLIENT) {
            for (IModelRender register : modelList) {
                register.registerModels();
            }
            itemPowerConverter.registerModels();
        }
        MinecraftForge.EVENT_BUS.register(this);
        PowerConfig.loadConfig(event.getSuggestedConfigurationFile(), event.getSide().isClient());

    }


    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Recipes.recipe.addRecipe(new ItemStack(PowerItem.module_ic), new Object[]{
                "ABA", "CDC", "ABA",

                Character.valueOf('A'), IUItem.copperCableItem,

                Character.valueOf('B'), new ItemStack(IUItem.tranformer, 1, 8),

                Character.valueOf('C'), TileGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),

                Character.valueOf('D'),
                new ItemStack(IUItem.electricblock, 1, 3)});
        Recipes.recipe.addRecipe(new ItemStack(PowerItem.module_rf), new Object[]{"ABA", "BDB", "ABA", Character.valueOf('A'),
                "ingotElectrum", Character.valueOf('B'), new ItemStack(IUItem.plate, 1, 5),
                Character.valueOf('D'), new ItemStack(IUItem.module7, 1, 4)});
        Recipes.recipe.addRecipe(new ItemStack(PowerItem.module_fe), new Object[]{
                "ABA", "CDC", "ABA",

                Character.valueOf('A'), new ItemStack(IUItem.basecircuit),

                Character.valueOf('B'), new ItemStack(IUItem.tranformer, 1, 9),

                Character.valueOf('C'), new ItemStack(IUItem.basecircuit, 1, 4),

                Character.valueOf('D'),
                new ItemStack(IUItem.core, 1, 1)});
        Recipes.recipe.addRecipe(new ItemStack(PowerItem.module_te), new Object[]{
                "ABA", "CDC", "ABA",

                Character.valueOf('A'), new ItemStack(IUItem.sunnarium, 1, 3),

                Character.valueOf('B'), new ItemStack(IUItem.photoniy),

                Character.valueOf('C'), new ItemStack(IUItem.preciousgem, 1, 2),

                Character.valueOf('D'),
                new ItemStack(IUItem.core, 1, 1)});
        Recipes.recipe.addRecipe(new ItemStack(this.itemPowerConverter), new Object[]{
                "ABA", "CDE", "ABA",

                Character.valueOf('A'), IUItem.advancedAlloy,

                Character.valueOf('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),

                Character.valueOf('C'), new ItemStack(PowerItem.module_ic),

                Character.valueOf('D'),
                IUItem.machine,

                Character.valueOf('E'), new ItemStack(PowerItem.module_rf)});
        Recipes.recipe.addRecipe(new ItemStack(this.itemPowerConverter, 1, 1), new Object[]{
                "ABA", "CDE", "ABA",

                Character.valueOf('A'), IUItem.advancedAlloy,

                Character.valueOf('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),

                Character.valueOf('C'), new ItemStack(PowerItem.module_ic),

                Character.valueOf('D'),
                IUItem.machine,

                Character.valueOf('E'), new ItemStack(PowerItem.module_fe)});
        Recipes.recipe.addRecipe(new ItemStack(this.itemPowerConverter, 1, 2), new Object[]{
                "ABA", "CDE", "ABA",

                Character.valueOf('A'), IUItem.advancedAlloy,

                Character.valueOf('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),

                Character.valueOf('C'), new ItemStack(PowerItem.module_ic),

                Character.valueOf('D'),
                IUItem.machine,

                Character.valueOf('E'), new ItemStack(PowerItem.module_te)});
        Recipes.recipe.addRecipe(new ItemStack(this.itemPowerConverter, 1, 3), new Object[]{
                "ABA", "CDE", "ABA",

                Character.valueOf('A'), IUItem.advancedAlloy,

                Character.valueOf('B'), TileGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),

                Character.valueOf('C'), new ItemStack(PowerItem.module_ic),

                Character.valueOf('D'),
                IUItem.machine,

                Character.valueOf('E'), new ItemStack(PowerItem.module_qe)});
        Recipes.recipe.addRecipe(new ItemStack(PowerItem.module_qe), new Object[]{
                "ABA", "CDC", "ABA",

                Character.valueOf('A'), new ItemStack(IUItem.sunnarium, 1, 3),

                Character.valueOf('B'), new ItemStack(IUItem.quantumtool),

                Character.valueOf('C'), new ItemStack(IUItem.radiationresources, 1, 2),

                Character.valueOf('D'),
                new ItemStack(IUItem.core, 1, 3)});
    }


    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void load(TileLoadEvent event) {
        if (PowerConfig.multi) {
            if (event.tileentity.hasCapability(CapabilityEnergy.ENERGY, null)) {
                final IEnergyStorage energy = event.tileentity.getCapability(CapabilityEnergy.ENERGY, null);
                if (energy.canExtract()) {
                    MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(event.getWorld(), new SourceEnergy(
                            event.tileentity,
                            energy
                    )));
                } else if (energy.canReceive()) {
                    MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(event.getWorld(), new SinkEnergy(
                            event.tileentity,
                            energy
                    )));
                }

            }
        }

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void unLoad(TileUnLoadEvent event) {
        if (PowerConfig.multi) {
            if (event.tileentity.hasCapability(CapabilityEnergy.ENERGY, null)) {
                final IEnergyStorage energy = event.tileentity.getCapability(CapabilityEnergy.ENERGY, null);
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(
                        event.getWorld(),
                        EnergyNetGlobal.instance.getTile(event.getWorld(), event.tileentity.getPos())
                ));

            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void update(TilesUpdateEvent event) {
        if (PowerConfig.multi) {
            for (TileEntity entity : event.tiles) {
                    if (entity.hasCapability(CapabilityEnergy.ENERGY, null)) {
                        final IEnergyStorage energy = entity.getCapability(CapabilityEnergy.ENERGY, null);
                        if (energy.canExtract()) {
                            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(event.getWorld(), new SourceEnergy(
                                    entity,
                                    energy
                            )));
                        } else if (energy.canReceive()) {
                            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(event.getWorld(), new SinkEnergy(
                                    entity,
                                    energy
                            )));
                        }


                }

            }
        }
    }

}
