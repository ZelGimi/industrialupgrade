package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.ITemperature;
import com.denfop.api.Recipes;
import com.denfop.api.heat.IHeatEmitter;
import com.denfop.api.heat.IHeatSink;
import com.denfop.api.heat.event.HeatTileLoadEvent;
import com.denfop.api.heat.event.HeatTileUnloadEvent;
import com.denfop.container.ContainerTripleElectricMachine;
import com.denfop.gui.GUIAdvAlloySmelter;
import com.denfop.tiles.base.EnumTripleElectricMachine;
import com.denfop.tiles.base.TileEntityElectricMachine;
import com.denfop.tiles.base.TileEntityTripleElectricMachine;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.RecipeOutput;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityAdvAlloySmelter extends TileEntityTripleElectricMachine  implements ITemperature, IHeatSink {
    public  short maxtemperature;
    public  short temperature;
    public TileEntityAdvAlloySmelter() {
        super(1, 300, 1, Localization.translate("iu.AdvAlloymachine.name"), EnumTripleElectricMachine.ADV_ALLOY_SMELTER);
        this.temperature = 0;
        this.maxtemperature = 5000;
    }
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.temperature = nbttagcompound.getShort("temperature");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("temperature", this.temperature);
        return nbttagcompound;
    }
    public static void init() {
        addAlloysmelter("ingotCopper", "ingotZinc", "ingotLead", new ItemStack(IUItem.alloysingot, 1, 3),4500);
        addAlloysmelter("ingotAluminium", "ingotMagnesium", "ingotManganese", new ItemStack(IUItem.alloysingot, 1, 5),4000);

        addAlloysmelter("ingotAluminium",
                "ingotCopper", "ingotTin",
                new ItemStack(IUItem.alloysingot, 1, 0),3000
        );

        addAlloysmelter("ingotAluminium",
                "ingotVanady", "ingotCobalt",
                new ItemStack(IUItem.alloysingot, 1, 6),4500
        );

        addAlloysmelter("ingotChromium",
                "ingotTungsten", "ingotNickel",
                new ItemStack(IUItem.alloysingot, 1, 7),5000
        );
    }

    public static void addAlloysmelter(String container, String fill, String fill1, ItemStack output,int temperature) {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.Alloyadvsmelter.addRecipe(input.forOreDict(container), input.forOreDict(fill), input.forOreDict(fill1),
                output,nbt
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

    public String getInventoryName() {

        return Localization.translate("iu.AdvAlloymachine.name");
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIAdvAlloySmelter(new ContainerTripleElectricMachine(entityPlayer, this, type));
    }

    @Override
    public void operateOnce(final RecipeOutput output, final List<ItemStack> processResult) {
        this.inputSlotA.consume();
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


}
