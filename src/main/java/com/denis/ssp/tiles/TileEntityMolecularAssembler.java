package com.Denfop.ssp.tiles;


import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.network.IGrowingBuffer;
import ic2.api.network.INetworkCustomEncoder;
import ic2.api.recipe.MachineRecipeResult;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.init.Localization;
import ic2.core.network.DataEncoder;
import ic2.core.network.GuiSynced;
import ic2.core.util.StackUtil;
import ic2.core.util.Tuple;
import ic2.core.util.Util;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.Denfop.ssp.IMolecularTransformerRecipeManager;
import com.Denfop.ssp.TransparentDynamicGUI;

public class TileEntityMolecularAssembler extends TileEntityInventory implements IEnergySink, IHasGui, IGuiValueProvider
{
    protected static final List<AxisAlignedBB> AABBs;
    protected static final byte MAX_TIME_WAIT = 40;
    public final InvSlotProcessable<IMolecularTransformerRecipeManager.Input, ItemStack, ItemStack> inputSlot;
    public final InvSlotOutput outputSlot;
    @GuiSynced
    protected Tuple.T2<ItemStack, MolecularOutput> currentRecipe;
    private boolean addedToEnet;
    protected double energyIn;
    protected double energyGiven;
    @GuiSynced
    protected double lastEnergyGiven;
    @GuiSynced
    protected double energyUsed;
    protected byte wait;

    public TileEntityMolecularAssembler() {
        this.inputSlot = new InvSlotProcessable<IMolecularTransformerRecipeManager.Input, ItemStack, ItemStack>(this, "input", 1, IMolecularTransformerRecipeManager.RECIPES) {
            protected ItemStack getInput(final ItemStack stack) {
                return stack;
            }

            protected void setInput(final ItemStack input) {
                this.put(input);
            }
        };
        this.outputSlot = new InvSlotOutput((TileEntityInventory)this, "output", 1);
        this.comparator.setUpdate(() -> (this.currentRecipe == null) ? 0 : ((int) Util.lerp(0.0, 15.0, this.energyUsed / this.currentRecipe.b.totalEU)));
    }

    protected void onLoaded() {
        super.onLoaded();
        if (!this.world.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnet = true;
        }
    }

    protected void onUnloaded() {
        super.onUnloaded();
        if (this.addedToEnet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnet = false;
        }
    }

    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.energyUsed = nbt.getDouble("energyUsed");
        if (nbt.hasKey("recipe")) {
            final ItemStack input = new ItemStack(nbt.getCompoundTag("recipe"));
            final MachineRecipeResult<IMolecularTransformerRecipeManager.Input, ItemStack, ItemStack> output = IMolecularTransformerRecipeManager.RECIPES.apply(input, false);
            if (output != null) {
                this.currentRecipe = new Tuple.T2<>(input, new MolecularOutput(output));
            }
        }
    }

    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("energyUsed", this.energyUsed);
        if (this.currentRecipe != null) {
            nbt.setTag("recipe", this.currentRecipe.a.writeToNBT(new NBTTagCompound()));
        }
        return nbt;
    }

    public List<String> getNetworkedFields() {
        final List<String> out = super.getNetworkedFields();
        out.add("energyUsed");
        out.add("energyIn");
        return out;
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        this.getActive();
        boolean nextActive;
        boolean updateInv = false;
        if (this.currentRecipe == null) {
            if (!this.inputSlot.isEmpty()) {
                updateInv = (nextActive = this.canWork());
            }
            else {
                nextActive = false;
            }
        }
        else {
            nextActive = true;
        }
        this.lastEnergyGiven = this.energyGiven;
        this.energyGiven = 0.0;
        if (nextActive) {
            if (this.energyIn <= 0.0) {
                this.energyIn = 0.0;
                final byte wait = this.wait;
                this.wait = (byte)(wait + 1);
                if (wait >= 40) {
                    nextActive = false;
                }
            }
            else {
                this.wait = 0;
                final double energyLeft = this.getDemandedEnergy();
                if (energyLeft > this.energyIn) {
                    this.energyUsed += this.energyIn;
                    this.energyIn = 0.0;
                }
                else {
                    this.energyIn -= energyLeft;
                    this.outputSlot.add(this.currentRecipe.b.output);
                    this.currentRecipe = null;
                    this.energyUsed = 0.0;
                    updateInv = true;
                }
            }
        }
        if (this.getActive() != nextActive) {
            this.setActive(nextActive);
            this.getWorld().checkLightFor(EnumSkyBlock.BLOCK, this.pos);
        }
        if (updateInv) {
            this.markDirty();
        }
    }

    protected boolean canWork() {
        final MachineRecipeResult<IMolecularTransformerRecipeManager.Input, ItemStack, ItemStack> result = this.inputSlot.process();
        if (result != null && this.outputSlot.canAdd(StackUtil.copy(result.getOutput()))) {
            this.currentRecipe = new Tuple.T2<>(this.inputSlot.get().copy(), new MolecularOutput(result));
            this.inputSlot.consume(result);
            return true;
        }
        return false;
    }

    protected List<AxisAlignedBB> getAabbs(final boolean forCollision) {
        return TileEntityMolecularAssembler.AABBs;
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(final EnumFacing side, final BlockPos otherPos) {
        return false;
    }

    public boolean canRenderBreaking() {
        return true;
    }

    protected int getLightValue() {
        return this.getActive() ? 12 : 0;
    }

    public void onNetworkUpdate(final String field) {
        super.onNetworkUpdate(field);
        if (field.equals("active")) {
            this.getWorld().checkLightFor(EnumSkyBlock.BLOCK, this.pos);
        }
    }

    public boolean acceptsEnergyFrom(final IEnergyEmitter emitter, final EnumFacing side) {
        return true;
    }

    public int getSinkTier() {
        return 14;
    }

    public double getDemandedEnergy() {
        return (this.currentRecipe == null) ? (this.energyGiven = 0.0) : (this.currentRecipe.b.totalEU - this.energyUsed);
    }

    public double injectEnergy(final EnumFacing directionFrom, final double amount, final double voltage) {
        this.energyGiven += amount;
        final double wanted = this.getDemandedEnergy();
        if (wanted == 0.0) {
            return amount;
        }
        if (wanted >= amount) {
            this.energyIn += amount;
            return 0.0;
        }
        final double in = amount - wanted;
        this.energyIn += in;
        return amount - in;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", this.getSinkTier()));
    }

    public ContainerBase<TileEntityMolecularAssembler> getGuiContainer(final EntityPlayer player) {
        return DynamicContainer.create(this, player, GuiParser.parse(this.teBlock));
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer player, final boolean isAdmin) {
        return TransparentDynamicGUI.create(this, player, GuiParser.parse(this.teBlock));
    }

    public void onGuiClosed(final EntityPlayer player) {
    }

    public double getGuiValue(final String name) {
        if ("progress".equals(name)) {
            return (this.currentRecipe == null) ? 0.0 : (this.energyUsed / this.currentRecipe.b.totalEU);
        }
        throw new IllegalArgumentException("Unexpected GUI value requested: " + name);
    }

    @SideOnly(Side.CLIENT)
    public String getInput() {
        return (this.currentRecipe == null) ? "" : this.currentRecipe.a.getDisplayName();
    }

    @SideOnly(Side.CLIENT)
    public String getOutput() {
        return (this.currentRecipe == null) ? "" : this.currentRecipe.b.output.getDisplayName();
    }

    @SideOnly(Side.CLIENT)
    public String getEnergyNeeded() {
        return (this.currentRecipe == null) ? "" : String.format("%,d %s", this.currentRecipe.b.totalEU, Localization.translate("ic2.generic.text.EU"));
    }

    @SideOnly(Side.CLIENT)
    public String getEU() {
        return (this.currentRecipe == null) ? "" : String.format("%,.0f %s", this.lastEnergyGiven, Localization.translate("ic2.generic.text.EUt"));
    }

    @SideOnly(Side.CLIENT)
    public String getPercent() {
        return (this.currentRecipe == null) ? "" : String.format("%,.0f%%", this.energyUsed * 100.0 / this.currentRecipe.b.totalEU);
    }

    static {
        AABBs = Arrays.asList(new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 1.0, 0.75), new AxisAlignedBB(0.05, 0.0, 0.2, 0.6, 1.0, 0.8));
    }

    public static final class MolecularOutput implements INetworkCustomEncoder
    {
        public final ItemStack output;
        public final int totalEU;

        public MolecularOutput(final MachineRecipeResult<IMolecularTransformerRecipeManager.Input, ItemStack, ItemStack> result) {
            this(result.getOutput(), result.getRecipe().getInput().totalEU);
        }

        private MolecularOutput(final ItemStack output, final int totalEU) {
            this.output = output;
            this.totalEU = totalEU;
        }

        public static void registerNetwork() {
            DataEncoder.addNetworkEncoder(MolecularOutput.class, new MolecularOutput(null, 0));
        }

        public boolean isThreadSafe() {
            return true;
        }

        public void encode(final IGrowingBuffer buffer, final Object instance) throws IOException {
            final MolecularOutput mo = (MolecularOutput)instance;
            DataEncoder.encode(buffer, mo.output, false);
            DataEncoder.encode(buffer, mo.totalEU, false);
        }

        public Object decode(final IGrowingBuffer buffer) throws IOException {
            return new MolecularOutput((ItemStack)DataEncoder.decode(buffer, DataEncoder.EncodedType.ItemStack), (int)DataEncoder.decode(buffer, DataEncoder.EncodedType.Integer));
        }
    }
}
