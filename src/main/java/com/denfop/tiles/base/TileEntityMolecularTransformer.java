package com.denfop.tiles.base;

import cofh.redstoneflux.api.IEnergyReceiver;
import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerBaseMolecular;
import com.denfop.gui.GuiMolecularTransformer;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.utils.ModUtils;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.audio.AudioSource;
import ic2.core.item.type.MiscResourceType;
import ic2.core.item.type.NuclearResourceType;
import ic2.core.ref.ItemName;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class TileEntityMolecularTransformer extends TileEntityElectricMachine implements INetworkTileEntityEventListener,
        IEnergyReceiver, INetworkClientTileEntityEventListener, IUpdateTick {

    public boolean queue;
    public byte redstoneMode;
    public int operationLength;
    public boolean rf = false;
    public int operationsPerTick;
    public AudioSource audioSource;
    public InvSlotRecipes inputSlot;
    public MachineRecipe output;
    public double perenergy;
    public double differenceenergy;
    public double size;
    protected double progress;
    protected double guiProgress;

    public TileEntityMolecularTransformer() {
        super(0, 14, 1);
        this.progress = 0;
        this.queue = false;
        this.redstoneMode = 0;
        this.energy = this.addComponent(AdvEnergy.asBasicSink(this, 0, 14).addManagedSlot(this.dischargeSlot));
        this.inputSlot = new InvSlotRecipes(this, "molecular", this);
        this.output = null;
    }

    public static void init() {


        addrecipe(new ItemStack(Items.SKULL, 1, 0), new ItemStack(Items.SKULL, 1, 1), Config.molecular);

        addrecipe(new ItemStack(Items.SKULL, 1, 1), new ItemStack(Items.NETHER_STAR, 1), Config.molecular1);

        addrecipe(new ItemStack(Items.IRON_INGOT, 1, 0), ItemName.misc_resource.getItemStack(MiscResourceType.iridium_ore),
                Config.molecular2
        );

        addrecipe(
                ItemName.nuclear.getItemStack(NuclearResourceType.plutonium),
                new ItemStack(IUItem.proton, 1),
                Config.molecular3
        );

        addrecipe("ingotSpinel", OreDictionary.getOres("ingotIridium").get(0), Config.molecular4);

        addrecipe(new ItemStack(IUItem.photoniy), new ItemStack(IUItem.photoniy_ingot), Config.molecular5);

        addrecipe(new ItemStack(Blocks.NETHERRACK), new ItemStack(Items.GUNPOWDER, 2), Config.molecular6);

        addrecipe(new ItemStack(Blocks.SAND), new ItemStack(Blocks.GRAVEL, 1), Config.molecular7);

        addrecipe("dustCoal", new ItemStack(Items.DIAMOND), Config.molecular8);

        addrecipe("ingotCopper", OreDictionary.getOres("ingotNickel").get(0), Config.molecular9);

        addrecipe("ingotLead", OreDictionary.getOres("ingotGold").get(0), Config.molecular10);

        if (OreDictionary.getOres("ingotSilver").size() >= 1) {
            addrecipe("ingotTin", OreDictionary.getOres("ingotSilver").get(0), Config.molecular11);
        }

        if (OreDictionary.getOres("ingotSilver").size() >= 1) {
            addrecipe("ingotSilver",
                    OreDictionary.getOres("ingotTungsten").get(0), Config.molecular12
            );
        }

        addrecipe("ingotTungsten",
                OreDictionary.getOres("ingotSpinel").get(0), Config.molecular13
        );

        addrecipe("ingotChromium",
                OreDictionary.getOres("ingotMikhail").get(0), Config.molecular14
        );

        addrecipe("ingotPlatinum",
                OreDictionary.getOres("ingotChromium").get(0), Config.molecular15
        );

        addrecipe("ingotGold", OreDictionary.getOres("ingotPlatinum").get(0), Config.molecular16);

        addrecipe("ingotIridium", new ItemStack(IUItem.core, 1, 0), Config.molecular17);

        addrecipe(new ItemStack(IUItem.core, 4, 0), new ItemStack(IUItem.core, 1, 1), Config.molecular18);

        addrecipe(new ItemStack(IUItem.core, 4, 1), new ItemStack(IUItem.core, 1, 2), Config.molecular19);

        addrecipe(new ItemStack(IUItem.core, 4, 2), new ItemStack(IUItem.core, 1, 3), Config.molecular20);

        addrecipe(new ItemStack(IUItem.core, 4, 3), new ItemStack(IUItem.core, 1, 4), Config.molecular21);

        addrecipe(new ItemStack(IUItem.core, 4, 4), new ItemStack(IUItem.core, 1, 5), Config.molecular22);

        addrecipe(new ItemStack(IUItem.core, 4, 5), new ItemStack(IUItem.core, 1, 6), Config.molecular23);

        addrecipe(new ItemStack(IUItem.core, 4, 6), new ItemStack(IUItem.core, 1, 7), Config.molecular24);

        addrecipe(new ItemStack(IUItem.core, 4, 7), new ItemStack(IUItem.core, 1, 8), Config.molecular25);

        addrecipe(new ItemStack(IUItem.core, 4, 8), new ItemStack(IUItem.core, 1, 9), Config.molecular26);
        addrecipe(new ItemStack(IUItem.core, 4, 9), new ItemStack(IUItem.core, 1, 10), Config.molecular38);

        addrecipe(new ItemStack(IUItem.core, 4, 10), new ItemStack(IUItem.core, 1, 11), Config.molecular39);
        addrecipe(new ItemStack(IUItem.core, 4, 11), new ItemStack(IUItem.core, 1, 12), Config.molecular40);
        addrecipe(new ItemStack(IUItem.core, 4, 12), new ItemStack(IUItem.core, 1, 13), Config.molecular41);

        //

        addrecipe(new ItemStack(IUItem.matter, 1, 1), new ItemStack(IUItem.lens, 1, 5), Config.molecular27);

        addrecipe(new ItemStack(IUItem.matter, 1, 2), new ItemStack(IUItem.lens, 1, 6), Config.molecular28);

        addrecipe(new ItemStack(IUItem.matter, 1, 3), new ItemStack(IUItem.lens, 1, 2), Config.molecular29);

        addrecipe(new ItemStack(IUItem.matter, 1, 4), new ItemStack(IUItem.lens, 1, 4), Config.molecular30);

        addrecipe(new ItemStack(IUItem.matter, 1, 5), new ItemStack(IUItem.lens, 1, 1), Config.molecular31);

        addrecipe(new ItemStack(IUItem.matter, 1, 6), new ItemStack(IUItem.lens, 1, 3), Config.molecular32);

        addrecipe(new ItemStack(IUItem.matter, 1, 7), new ItemStack(IUItem.lens, 1), Config.molecular33);

        addrecipe(
                ItemName.misc_resource.getItemStack(MiscResourceType.iridium_ore),
                new ItemStack(IUItem.photoniy),
                Config.molecular34
        );

        addrecipe("ingotMikhail",
                OreDictionary.getOres("ingotMagnesium").get(0), Config.molecular35
        );

        addrecipe("ingotMagnesium", OreDictionary.getOres("ingotCaravky").get(0), Config.molecular36);

        addrecipe("ingotManganese", OreDictionary.getOres("ingotCobalt").get(0), 350000);


        addrecipe("ingotCaravky", new ItemStack(IUItem.iuingot, 1, 18), 600000);
        addrecipe("ingotCobalt", new ItemStack(IUItem.iuingot, 1, 16), 350000);
        addrecipe(new ItemStack(IUItem.iuingot, 1, 16), new ItemStack(IUItem.iuingot, 1, 15), 300000);

    }

    public static void addrecipe(ItemStack stack, ItemStack stack1, double energy) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setDouble("energy", energy);
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "molecular",
                new BaseMachineRecipe(new Input(input.forStack(stack)), new RecipeOutput(nbt, stack1))
        );
    }

    public static void addrecipe(String stack, ItemStack stack1, double energy) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setDouble("energy", energy);
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "molecular",
                new BaseMachineRecipe(new Input(input.forOreDict(stack)), new RecipeOutput(nbt, stack1))
        );

    }

    public List<Double> getTime(final double energy) {
        final double dif = this.differenceenergy;
        return ModUtils.Time((energy - this.energy.getEnergy()) / (dif * 20));
    }

    public double getInput() {
        return EnergyNet.instance
                .getNodeStats(this.energy.getDelegate())
                .getEnergyIn();
    }

    @Override
    public ContainerBase<? extends TileEntityMolecularTransformer> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerBaseMolecular(entityPlayer, this);
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    protected boolean isNormalCube() {
        return false;
    }

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.blockmolecular);
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
        if (player.getHeldItem(hand).getItem() instanceof ItemAdditionModule && player.getHeldItem(hand).getItemDamage() == 4) {
            if (!this.rf) {
                this.rf = true;
                player.getHeldItem(hand).setCount(player.getHeldItem(hand).getCount() - 1);
                return true;
            }
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        ret.add("queue");
        ret.add("redstoneMode");
        ret.add("energy");

        return ret;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public String getInventoryName() {
        return "Molecular Transformer";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);


        this.queue = nbttagcompound.getBoolean("queue");
        this.redstoneMode = nbttagcompound.getByte("redstoneMode");

        this.progress = nbttagcompound.getDouble("progress");
        this.rf = nbttagcompound.getBoolean("rf");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setByte("redstoneMode", this.redstoneMode);
        nbttagcompound.setBoolean("queue", this.queue);
        nbttagcompound.setDouble("progress", this.progress);
        nbttagcompound.setBoolean("rf", this.rf);
        return nbttagcompound;

    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiMolecularTransformer(new ContainerBaseMolecular(entityPlayer, this));
    }


    public void onNetworkEvent(EntityPlayer player, int event) {

        if (event == 0) {
            this.redstoneMode = (byte) (this.redstoneMode + 1);
            if (this.redstoneMode >= 8) {
                this.redstoneMode = 0;
            }
            IC2.network.get(true).updateTileEntityField(this, "redstoneMode");
            this.getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 2);
        }
        if (event == 1) {
            this.queue = !this.queue;
            setOverclockRates();
        }
    }


    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
        }
    }

    public void operate(MachineRecipe output) {
        List<ItemStack> processResult = output.getRecipe().output.items;
        operateOnce(processResult);
    }

    public void operate(MachineRecipe output, int size) {
        List<ItemStack> processResult = output.getRecipe().output.items;
        operateOnce(processResult, size);
    }

    public void operateOnce(List<ItemStack> processResult) {

        this.inputSlot.consume();
        this.outputSlot.add(processResult);
        if (!this.inputSlot.continue_process(this.output)) {
            getOutput();
        }
    }

    public void operateOnce(List<ItemStack> processResult, int size) {
        for (int i = 0; i < size; i++) {

            this.inputSlot.consume();
            this.outputSlot.add(processResult);

        }
        if (!this.inputSlot.continue_process(this.output)) {
            getOutput();
        }
    }

    protected void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            inputSlot.load();
            this.setOverclockRates();

        }

    }

    public void setOverclockRates() {
        MachineRecipe output = getOutput();
        if (!this.queue) {
            if (inputSlot.isEmpty() || !this.inputSlot.continue_proccess(this.outputSlot)) {
                this.energy.setCapacity(0);
            } else if (output != null) {
                this.energy.setCapacity(output.getRecipe().output.metadata.getDouble("energy"));
            } else {
                this.energy.setCapacity(0);
            }
        } else {

            if (inputSlot.isEmpty() || !this.inputSlot.continue_proccess(this.outputSlot)) {
                this.energy.setCapacity(0);
            } else if (output != null) {
                int size;
                ItemStack output1 = this.output.getRecipe().output.items.get(0);
                size = this.output.getRecipe().input.getInputs().get(0).getInputs().get(0).getCount();
                size = (int) Math.floor((float) this.inputSlot.get().getCount() / size);
                int size1 = this.outputSlot.get() != null
                        ? (64 - this.outputSlot.get().getCount()) / output1.stackSize
                        : 64 / output1.stackSize;

                size = Math.min(size1, size);
                size = Math.min(size, output1.getMaxStackSize());
                this.energy.setCapacity(output.getRecipe().output.metadata.getDouble("energy") * size);
            } else {
                this.energy.setCapacity(0);
            }
        }


    }

    public void updateEntityServer() {
        super.updateEntityServer();

        MachineRecipe output = this.output;
        if (!queue) {
            if (output != null && this.outputSlot.canAdd(output.getRecipe().output.items)) {
                this.differenceenergy = this.energy.getEnergy() - this.perenergy;
                this.perenergy = this.energy.getEnergy();
                setActive(true);
                markDirty();
                if (energy.getCapacity() > 0) {
                    IC2.network.get(true).initiateTileEntityEvent(this, 0, true);
                }


                this.progress = this.energy.getEnergy();
                double k = this.progress;
                this.guiProgress = (k / output.getRecipe().output.metadata.getDouble("energy"));


                if (this.energy.getEnergy() >= output.getRecipe().output.metadata.getDouble("energy")) {
                    operate(output);

                    this.progress = 0;
                    this.energy.useEnergy(this.energy.getEnergy());

                    IC2.network.get(true).initiateTileEntityEvent(this, 2, true);
                }
            } else {
                if (this.energy.getEnergy() != 0 && getActive()) {
                    IC2.network.get(true).initiateTileEntityEvent(this, 1, true);
                }
                this.energy.useEnergy(this.energy.getEnergy());
                this.energy.setCapacity(0);
                setActive(false);
            }

        } else {
            if (output != null && this.inputSlot.continue_proccess(this.outputSlot)) {
                this.differenceenergy = this.energy.getEnergy() - this.perenergy;
                this.perenergy = this.energy.getEnergy();
                setActive(true);
                markDirty();
                if (energy.getEnergy() > 0) {
                    IC2.network.get(true).initiateTileEntityEvent(this, 0, true);
                }


                int size;
                ItemStack output1 = this.output.getRecipe().output.items.get(0);
                size = this.output.getRecipe().input.getInputs().get(0).getInputs().get(0).getCount();

                size = (int) Math.floor((float) this.inputSlot.get().getCount() / size);
                int size1 = this.outputSlot.get() != null
                        ? (64 - this.outputSlot.get().getCount()) / output1.stackSize
                        : 64 / output1.stackSize;

                size = Math.min(size1, size);
                size = Math.min(size, output1.getMaxStackSize());

                double p = (this.energy.getEnergy() / (output.getRecipe().output.metadata.getDouble("energy") * size));
                if (p <= 1) {
                    this.guiProgress = p;
                }
                if (p > 1) {
                    this.guiProgress = 1;
                }
                if (this.energy.getEnergy() >= (output.getRecipe().output.metadata.getDouble("energy") * size)) {
                    operate(output, size);

                    this.progress = 0;
                    this.energy.useEnergy(this.energy.getEnergy());

                    IC2.network.get(true).initiateTileEntityEvent(this, 2, true);
                }
            } else {
                if (this.energy.getEnergy() != 0 && getActive()) {
                    IC2.network.get(true).initiateTileEntityEvent(this, 1, true);
                }
                this.energy.useEnergy(this.energy.getEnergy());
                this.energy.setCapacity(0);
                setActive(false);
            }

        }
        if (getActive() && output == null) {
            setActive(false);
        }
    }

    public MachineRecipe getOutput() {

        this.output = this.inputSlot.process();
        return this.output;
    }

    public double getProgress() {
        return Math.min(this.guiProgress, 1);
    }


    @Override
    public int receiveEnergy(final EnumFacing enumFacing, final int i, final boolean b) {
        if (this.rf) {
            return receiveEnergy(i, b);
        } else {
            return 0;
        }
    }

    public int receiveEnergy(int paramInt, boolean paramBoolean) {
        int i = (int) Math.min(
                ((IEnergySink) this.energy.getDelegate()).getDemandedEnergy() * Config.coefficientrf,
                Math.min(EnergyNet.instance.getPowerFromTier(14) * Config.coefficientrf, paramInt)
        );
        if (!paramBoolean) {
            this.energy.addEnergy(i * 1F / Config.coefficientrf);
        }
        return i;
    }

    public String getStartSoundFile() {
        return "Machines/molecular.ogg";
    }

    @Override
    public int getEnergyStored(final EnumFacing enumFacing) {
        return (int) this.energy.getEnergy();
    }

    @Override
    public int getMaxEnergyStored(final EnumFacing enumFacing) {
        return (int) this.energy.getCapacity();
    }

    @Override
    public boolean canConnectEnergy(final EnumFacing enumFacing) {
        return true;
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

}
