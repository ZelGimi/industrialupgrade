package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.ITemperature;
import com.denfop.api.Recipes;
import com.denfop.api.heat.IHeatEmitter;
import com.denfop.api.heat.IHeatSink;
import com.denfop.api.heat.event.HeatTileLoadEvent;
import com.denfop.api.heat.event.HeatTileUnloadEvent;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GUIAlloySmelter;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileEntityDoubleElectricMachine;
import com.denfop.tiles.base.TileEntityElectricMachine;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.RecipeOutput;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class TileEntityAlloySmelter extends TileEntityDoubleElectricMachine implements ITemperature, IHeatSink {

    public  short maxtemperature;
    public  short temperature;

    public TileEntityAlloySmelter() {
        super(1, 300, 1, Localization.translate("iu.Alloymachine.name"), EnumDoubleElectricMachine.ALLOY_SMELTER);
        this.temperature = 0;
        this.maxtemperature = 5000;
    }

    public static void init() {

        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        addAlloysmelter(
                input.forStack(new ItemStack(Items.IRON_INGOT), 1),
                input.forStack(new ItemStack(Items.COAL), 2),
                new ItemStack(Ic2Items.advIronIngot.getItem(), 1, 5),4000
        );
        addAlloysmelter(
                input.forStack(new ItemStack(Items.GOLD_INGOT), 1),
                input.forOreDict("ingotSilver", 1),
                new ItemStack(
                        OreDictionary.getOres("ingotElectrum").get(0).getItem(),
                        2,
                        OreDictionary.getOres("ingotElectrum").get(0).getItemDamage()
                ),3500
        );
        addAlloysmelter(
                input.forOreDict("ingotNickel", 1),
                input.forStack(new ItemStack(Items.IRON_INGOT), 2),
                new ItemStack(
                        OreDictionary.getOres("ingotInvar").get(0).getItem(),
                        3,
                        OreDictionary.getOres("ingotInvar").get(0).getItemDamage()
                ),3500
        );

        addAlloysmelter(
                input.forOreDict("ingotCopper", 1),
                input.forOreDict("ingotZinc", 1),
                new ItemStack(IUItem.alloysingot, 1, 2),4000
        );
        addAlloysmelter(
                input.forOreDict("ingotNickel", 1),
                input.forOreDict("ingotChromium", 1),
                new ItemStack(IUItem.alloysingot, 1, 4),4000
        );
        addAlloysmelter(
                input.forOreDict("ingotAluminium", 1),
                input.forOreDict("ingotMagnesium", 1),
                new ItemStack(IUItem.alloysingot, 1, 8),5000
        );
        addAlloysmelter(
                input.forOreDict("ingotAluminum", 1),
                input.forOreDict("ingotMagnesium", 1),
                new ItemStack(IUItem.alloysingot, 1, 8),5000
        );
        addAlloysmelter(
                input.forOreDict("ingotAluminium", 1),
                input.forOreDict("ingotTitanium", 1),
                new ItemStack(IUItem.alloysingot, 1, 1),5000
        );
        addAlloysmelter(
                input.forOreDict("ingotAluminum", 1),
                input.forOreDict("ingotTitanium", 1),
                new ItemStack(IUItem.alloysingot, 1, 1),5000
        );
        addAlloysmelter(
                input.forStack(new ItemStack(Items.IRON_INGOT), 1),
                input.forOreDict("ingotManganese", 1),
                new ItemStack(IUItem.alloysingot, 1, 9),4500
        );


    }
    public void onLoaded() {
        super.onLoaded();
        MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this));
    }
    public void onUnloaded() {
        MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this));
        super.onUnloaded();

    }
        public static void addAlloysmelter(IRecipeInput container, IRecipeInput fill, ItemStack output,int temperature) {
        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.Alloysmelter.addRecipe(container, fill, nbt, output);
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIAlloySmelter(new ContainerDoubleElectricMachine(entityPlayer, this, this.type));
    }

    @Override
    public void operateOnce(RecipeOutput output, List<ItemStack> processResult) {
        this.inputSlotA.consume(0);
        this.outputSlot.add(processResult);
    }

    public String getStartSoundFile() {
        return "Machines/alloysmelter.ogg";
    }



    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    @Override
    public World getWorldTile() {
        return this.getWorld();
    }

    @Override
    public boolean reveiver() {
        return true;
    }

    public ITemperature getITemperature() {
        return this;
    }
    @Override
    public boolean requairedTemperature() {
        return true;
    }

    @Override
    public short getTemperature() {
        return this.temperature;
    }

    @Override
    public void setTemperature(short temperature) {
        this.temperature = temperature;
    }

    @Override
    public short getMaxTemperature() {
        return this.maxtemperature;
    }

    @Override
    public boolean isFluidTemperature() {
        return false;
    }

    @Override
    public FluidStack getFluid() {
        return null;
    }

    @Override
    public TileEntityElectricMachine getTile() {
        return null;
    }

    @Override
    public boolean acceptsHeatFrom(final IHeatEmitter var1, final EnumFacing var2) {
        return true;
    }

    @Override
    public double getDemandedHeat() {
        return Math.max(0.0D, this.maxtemperature - this.temperature);
    }

    public void setHeatStored(double amount) {
        this.temperature = (short) amount;
    }

    @Override
    public double injectHeat(final EnumFacing var1, final double var2, final double var4) {
        this.setHeatStored(this.getTemperature() + var2);
        return 0.0D;
    }

}
