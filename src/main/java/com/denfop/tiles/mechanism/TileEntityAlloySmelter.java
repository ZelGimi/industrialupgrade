package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.ITemperature;
import com.denfop.api.Recipes;
import com.denfop.api.heat.IHeatEmitter;
import com.denfop.api.heat.IHeatSink;
import com.denfop.api.heat.event.HeatTileLoadEvent;
import com.denfop.api.heat.event.HeatTileUnloadEvent;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiAlloySmelter;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileEntityDoubleElectricMachine;
import com.denfop.tiles.base.TileEntityElectricMachine;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class TileEntityAlloySmelter extends TileEntityDoubleElectricMachine implements ITemperature, IHeatSink {

    public short maxtemperature;
    public short temperature;
    private ITemperature source;
    private boolean auto;

    public TileEntityAlloySmelter() {
        super(1, 300, 1, Localization.translate("iu.Alloymachine.name"), EnumDoubleElectricMachine.ALLOY_SMELTER);
        this.temperature = 0;
        this.maxtemperature = 5000;
        this.source = null;
        this.auto = false;
    }

    public static void init() {

        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        addAlloysmelter(
                input.forStack(new ItemStack(Items.IRON_INGOT), 1),
                input.forStack(new ItemStack(Items.COAL), 2),
                new ItemStack(Ic2Items.advIronIngot.getItem(), 1, 5), 4000
        );
        addAlloysmelter(
                input.forStack(new ItemStack(Items.GOLD_INGOT), 1),
                input.forOreDict("ingotSilver", 1),
                new ItemStack(
                        OreDictionary.getOres("ingotElectrum").get(0).getItem(),
                        2,
                        OreDictionary.getOres("ingotElectrum").get(0).getItemDamage()
                ), 3500
        );
        addAlloysmelter(
                input.forOreDict("ingotNickel", 1),
                input.forStack(new ItemStack(Items.IRON_INGOT), 2),
                new ItemStack(
                        OreDictionary.getOres("ingotInvar").get(0).getItem(),
                        3,
                        OreDictionary.getOres("ingotInvar").get(0).getItemDamage()
                ), 5000
        );

        addAlloysmelter(
                input.forOreDict("ingotCopper", 1),
                input.forOreDict("ingotZinc", 1),
                new ItemStack(IUItem.alloysingot, 1, 2), 3000
        );
        addAlloysmelter(
                input.forOreDict("ingotNickel", 1),
                input.forOreDict("ingotChromium", 1),
                new ItemStack(IUItem.alloysingot, 1, 4), 4000
        );
        addAlloysmelter(
                input.forOreDict("ingotAluminium", 1),
                input.forOreDict("ingotMagnesium", 1),
                new ItemStack(IUItem.alloysingot, 1, 8), 2000
        );
        addAlloysmelter(
                input.forOreDict("ingotAluminum", 1),
                input.forOreDict("ingotMagnesium", 1),
                new ItemStack(IUItem.alloysingot, 1, 8), 2000
        );
        addAlloysmelter(
                input.forOreDict("ingotAluminium", 1),
                input.forOreDict("ingotTitanium", 1),
                new ItemStack(IUItem.alloysingot, 1, 1), 5000
        );
        addAlloysmelter(
                input.forOreDict("ingotAluminum", 1),
                input.forOreDict("ingotTitanium", 1),
                new ItemStack(IUItem.alloysingot, 1, 1), 5000
        );
        addAlloysmelter(
                input.forStack(new ItemStack(Items.IRON_INGOT), 1),
                input.forOreDict("ingotManganese", 1),
                new ItemStack(IUItem.alloysingot, 1, 9), 4500
        );


    }

    public static void addAlloysmelter(IRecipeInput container, IRecipeInput fill, ItemStack output, int temperature) {
        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("alloysmelter", new BaseMachineRecipe(
                new Input(container, fill),
                new RecipeOutput(nbt, output)
        ));
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.temperature = nbttagcompound.getShort("temperature");
        this.auto = nbttagcompound.getBoolean("auto");
        this.source = null;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("temperature", this.temperature);
        nbttagcompound.setBoolean("auto", this.auto);
        return nbttagcompound;
    }

    @Override
    protected boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        final ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem().equals(IUItem.autoheater) && !this.auto) {
            this.auto = true;
            stack.shrink(1);
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public ITemperature getSource() {
        return this.source;
    }

    @Override
    public void setSource(final ITemperature source) {
        this.source = source;
    }

    public void onLoaded() {
        super.onLoaded();
        MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this));
    }

    public void onUnloaded() {
        MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this));
        super.onUnloaded();

    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAlloySmelter(new ContainerDoubleElectricMachine(entityPlayer, this, this.type));
    }

    @Override
    public void operateOnce(MachineRecipe output, List<ItemStack> processResult) {
        this.inputSlotA.consume();
        this.outputSlot.add(processResult);
    }

    public String getStartSoundFile() {
        return "Machines/alloysmelter.ogg";
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.temperature > 0) {
            this.temperature--;
        }
        if (this.auto) {
            if (this.temperature + 2 <= this.maxtemperature) {
                this.temperature += 2;
            }
        }
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
    public boolean receiver() {
        return true;
    }

    public ITemperature getITemperature() {
        return this;
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
        return Math.max(0.0D, this.maxtemperature);
    }

    public void setHeatStored(double amount) {
        if (this.temperature < amount) {
            this.temperature = (short) amount;
        }
    }

    @Override
    public double injectHeat(final EnumFacing var1, final double var2, final double var4) {
        this.setHeatStored(var2);
        return 0.0D;
    }

}
