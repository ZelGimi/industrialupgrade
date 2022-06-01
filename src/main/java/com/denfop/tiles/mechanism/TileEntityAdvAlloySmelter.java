package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
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
import com.denfop.container.ContainerTripleElectricMachine;
import com.denfop.gui.GuiAdvAlloySmelter;
import com.denfop.tiles.base.EnumTripleElectricMachine;
import com.denfop.tiles.base.TileEntityElectricMachine;
import com.denfop.tiles.base.TileEntityTripleElectricMachine;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityAdvAlloySmelter extends TileEntityTripleElectricMachine implements ITemperature, IHeatSink {

    public short maxtemperature;
    public short temperature;
    private ITemperature source;
    private boolean auto;

    public TileEntityAdvAlloySmelter() {
        super(1, 300, 1, Localization.translate("iu.AdvAlloymachine.name"), EnumTripleElectricMachine.ADV_ALLOY_SMELTER);
        this.temperature = 0;
        this.maxtemperature = 5000;
        this.auto = false;
    }

    public static void init() {
        addAlloysmelter("ingotCopper", "ingotZinc", "ingotLead", new ItemStack(IUItem.alloysingot, 1, 3), 4500);
        addAlloysmelter("ingotAluminium", "ingotMagnesium", "ingotManganese", new ItemStack(IUItem.alloysingot, 1, 5), 4000);
        addAlloysmelter("ingotAluminum", "ingotMagnesium", "ingotManganese", new ItemStack(IUItem.alloysingot, 1, 5), 4000);

        addAlloysmelter("ingotAluminium",
                "ingotCopper", "ingotTin",
                new ItemStack(IUItem.alloysingot, 1, 0), 3000
        );
        addAlloysmelter("ingotAluminum",
                "ingotCopper", "ingotTin",
                new ItemStack(IUItem.alloysingot, 1, 0), 3000
        );
        addAlloysmelter("ingotAluminium",
                "ingotVanady", "ingotCobalt",
                new ItemStack(IUItem.alloysingot, 1, 6), 4500
        );
        addAlloysmelter("ingotAluminum",
                "ingotVanady", "ingotCobalt",
                new ItemStack(IUItem.alloysingot, 1, 6), 4500
        );
        addAlloysmelter("ingotChromium",
                "ingotTungsten", "ingotNickel",
                new ItemStack(IUItem.alloysingot, 1, 7), 5000
        );
    }

    public static void addAlloysmelter(String container, String fill, String fill1, ItemStack output, int temperature) {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("advalloysmelter", new BaseMachineRecipe(
                new Input(input.forOreDict(container), input.forOreDict(fill), input.forOreDict(fill1)),
                new RecipeOutput(nbt, output)
        ));

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

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.temperature = nbttagcompound.getShort("temperature");
        this.auto = nbttagcompound.getBoolean("auto");
        this.source = null;
    }

    @Override
    public ITemperature getSource() {
        return this.source;
    }

    @Override
    public void setSource(final ITemperature source) {
        this.source = source;
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

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("temperature", this.temperature);
        nbttagcompound.setBoolean("auto", this.auto);
        return nbttagcompound;
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

    public String getInventoryName() {

        return Localization.translate("iu.AdvAlloymachine.name");
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAdvAlloySmelter(new ContainerTripleElectricMachine(entityPlayer, this, type));
    }

    @Override
    public void operateOnce(final MachineRecipe output, final List<ItemStack> processResult) {
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
