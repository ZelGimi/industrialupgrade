package com.denfop.tiles.base;

import cofh.redstoneflux.api.IEnergyHandler;
import cofh.redstoneflux.api.IEnergyReceiver;
import com.denfop.Config;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.audio.AudioSource;
import com.denfop.audio.PositionSpec;
import com.denfop.container.ContainerMultiMachine;
import com.denfop.gui.GUIMultiMachine;
import com.denfop.gui.GUIMultiMachine1;
import com.denfop.gui.GUIMultiMachine2;
import com.denfop.gui.GUIMultiMachine3;
import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.items.modules.AdditionModule;
import com.denfop.items.modules.ItemModuleTypePanel;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileEntitySolarPanel;
import com.denfop.utils.ExperienceUtils;
import ic2.api.energy.EnergyNet;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.RecipeOutput;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.comp.Energy;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotDischarge;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotUpgrade;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class TileEntityMultiMachine extends TileEntityInventory implements IHasGui, IEnergyHandler, IEnergyReceiver,
        INetworkTileEntityEventListener, IUpgradableBlock {

    public final int min;
    public final int max;
    public final boolean random;
    public final int type;
    public final int sizeWorkingSlot;
    public final short[] progress;
    public final double maxEnergy2;
    public final int defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final int defaultEnergyStorage;
    public final InvSlotOutput outputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final Energy energy;
    public final InvSlotDischarge dischargeSlot;
    protected final double[] guiProgress;
    public EnumSolarPanels solartype;
    public boolean rf;
    public int expstorage;
    public IMachineRecipeManager recipe;
    public int module;
    public boolean quickly;
    public int[] col = new int[4];
    public boolean modulesize = false;
    public double energy2;
    public int energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public InvSlotProcessableMultiGeneric inputSlots;
    public AudioSource audioSource;
    public boolean modulestorage;


    public TileEntityMultiMachine(int energyconsume, int OperationsPerTick, IMachineRecipeManager recipe, int type) {
        this(1, energyconsume, OperationsPerTick, recipe, 0, 0, false, type);
    }

    public TileEntityMultiMachine(
            int energyconsume,
            int OperationsPerTick,
            IMachineRecipeManager<ic2.api.recipe.IRecipeInput, java.util.Collection<ItemStack>, ItemStack> recipe,
            int min,
            int max,
            boolean random,
            int type
    ) {
        this(1, energyconsume, OperationsPerTick, recipe, min, max, random, type);
    }

    public TileEntityMultiMachine(
            int aDefaultTier,
            int energyconsume,
            int OperationsPerTick,
            IMachineRecipeManager recipe,
            int min,
            int max,
            boolean random,
            int type
    ) {
        this.sizeWorkingSlot = getMachine().sizeWorkingSlot;
        this.progress = new short[sizeWorkingSlot];
        this.guiProgress = new double[sizeWorkingSlot];
        double coefenergy = getcoef();
        double speed = getspeed();
        this.defaultEnergyConsume = this.energyConsume = (int) (energyconsume * coefenergy);
        this.defaultOperationLength = this.operationLength = (int) (OperationsPerTick * 1D / speed);
        this.defaultTier = aDefaultTier;
        this.defaultEnergyStorage = energyconsume * OperationsPerTick;
        this.outputSlot = new InvSlotOutput(this, "output", sizeWorkingSlot);
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.dischargeSlot = new InvSlotDischarge(this, InvSlot.Access.NONE, aDefaultTier, false, InvSlot.InvSide.ANY);
        this.energy = this.addComponent(Energy
                .asBasicSink(this, (double) energyconsume * OperationsPerTick, aDefaultTier)
                .addManagedSlot(this.dischargeSlot));
        this.maxEnergy2 = energyconsume * OperationsPerTick * 4;
        this.rf = false;
        this.quickly = false;
        this.module = 0;
        this.recipe = recipe;
        this.min = min;
        this.max = max;
        this.random = random;
        this.type = type;
        this.expstorage = 0;
        this.solartype = null;
    }

    public List<ItemStack> getDrop() {

        return getAuxDrops(0);

    }

    protected List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        ItemStack stack_rf = ItemStack.EMPTY;
        ItemStack stack_quickly = ItemStack.EMPTY;
        ItemStack stack_modulesize = ItemStack.EMPTY;
        ItemStack panel = ItemStack.EMPTY;
        if (rf) {
            stack_rf = new ItemStack(IUItem.module7, 1, 4);
        }
        if (quickly) {
            stack_quickly = new ItemStack(IUItem.module_quickly);
        }
        if (modulesize) {
            stack_modulesize = new ItemStack(IUItem.module_stack);
        }
        if (solartype != null) {
            panel = new ItemStack(IUItem.module6, 1, solartype.meta);
        }
        if (!stack_rf.isEmpty() || !stack_quickly.isEmpty() || !stack_modulesize.isEmpty() || !panel.isEmpty()) {
            if (!stack_rf.isEmpty()) {
                ret.add(stack_rf);
                module--;
                rf = false;
            }
            if (!stack_quickly.isEmpty()) {
                ret.add(stack_quickly);
                module--;
                quickly = false;
            }
            if (!stack_modulesize.isEmpty()) {
                ret.add(stack_modulesize);
                modulesize = false;
                module--;
            }
            if (solartype != null) {
                ret.add(panel);
                solartype = null;
            }
        }
        return ret;
    }

    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {

        if (this.rf) {
            return receiveEnergy(maxReceive, simulate);
        } else {
            return 0;
        }

    }

    public int receiveEnergy(int paramInt, boolean paramBoolean) {
        int i = (int) Math.min(
                this.maxEnergy2 - this.energy2,
                Math.min(EnergyNet.instance.getPowerFromTier(this.energy.getSinkTier()) * 4, paramInt)
        );
        if (!paramBoolean) {
            this.energy2 += i;
        }
        return i;
    }

    public void updateVisibility(TileEntitySolarPanel type) {
        type.wetBiome = this.world.getBiome(this.pos).getRainfall() > 0.0F;
        type.noSunWorld = this.world.provider.isNether();

        type.rain = type.wetBiome && (this.world.isRaining() || this.world.isThundering());
        type.sunIsUp = this.world.isDaytime();
        type.skyIsVisible = this.world.canBlockSeeSky(this.pos.up()) &&
                (this.world.getBlockState(this.pos.up()).getMaterial().getMaterialMapColor() ==
                        MapColor.AIR) && !type.noSunWorld;
        if (!type.skyIsVisible) {
            type.active = TileEntitySolarPanel.GenerationState.NONE;
        }
        if (type.sunIsUp && type.skyIsVisible) {
            if (!(type.rain)) {
                type.active = TileEntitySolarPanel.GenerationState.DAY;
            } else {
                type.active = TileEntitySolarPanel.GenerationState.RAINDAY;
            }

        }
        if (!type.sunIsUp && type.skyIsVisible) {
            if (!(type.rain)) {
                type.active = TileEntitySolarPanel.GenerationState.NIGHT;
            } else {
                type.active = TileEntitySolarPanel.GenerationState.RAINNIGHT;
            }
        }
        if (type.getWorld().provider.getDimension() == 1) {
            type.active = TileEntitySolarPanel.GenerationState.END;
        }
        if (type.getWorld().provider.getDimension() == -1) {
            type.active = TileEntitySolarPanel.GenerationState.NETHER;
        }

    }

    @Override
    protected boolean onActivated(
            final EntityPlayer entityPlayer,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (this.expstorage > 0) {
            ExperienceUtils.addPlayerXP(entityPlayer, this.expstorage);
            this.expstorage = 0;
        }
        if (!entityPlayer.getHeldItem(hand).isEmpty()) {
            if (entityPlayer.getHeldItem(hand).getItem() instanceof ItemModuleTypePanel) {
                if (this.solartype != null) {
                    EnumSolarPanels type = this.solartype;
                    int meta = type.meta;
                    ItemStack stack = new ItemStack(IUItem.module6, 1, meta);
                    if (!entityPlayer.inventory.addItemStackToInventory(stack)) {
                        EntityItem item = new EntityItem(
                                entityPlayer.getEntityWorld(),
                                (int) (entityPlayer.posX),
                                (int) entityPlayer.posY - 1,
                                (int) (entityPlayer.posZ)
                        );
                        item.setItem(stack);
                        item.setPosition(entityPlayer.posX, entityPlayer.posY - 1, entityPlayer.posZ);
                        item.setDefaultPickupDelay();
                        world.spawnEntity(item);
                    }

                }
                this.solartype = ItemModuleTypePanel.getSolarType(entityPlayer.getHeldItem(hand).getItemDamage());
                entityPlayer.getHeldItem(hand).setCount(entityPlayer.getHeldItem(hand).getCount() - 1);
                return true;
            }
            if (entityPlayer.getHeldItem(hand).getItem() instanceof AdditionModule && entityPlayer
                    .getHeldItem(hand)
                    .getItemDamage() == 4) {
                if (!this.rf && this.module < 2) {
                    this.rf = true;
                    this.module++;
                    entityPlayer.getHeldItem(hand).setCount(entityPlayer.getHeldItem(hand).getCount() - 1);
                    return true;
                }
            }
            if (entityPlayer.getHeldItem(hand).getItem().equals(IUItem.module_quickly)) {
                if (!this.quickly && this.module < 2) {
                    this.quickly = true;
                    this.module++;
                    entityPlayer.getHeldItem(hand).setCount(entityPlayer.getHeldItem(hand).getCount() - 1);
                    return true;
                }
            }
            if (entityPlayer.getHeldItem(hand).getItem().equals(IUItem.module_stack)) {
                if (!this.modulesize && this.module < 2) {
                    this.modulesize = true;
                    this.module++;
                    entityPlayer.getHeldItem(hand).setCount(entityPlayer.getHeldItem(hand).getCount() - 1);
                    return true;
                }
            }
            if (entityPlayer.getHeldItem(hand).getItem().equals(IUItem.module_storage)) {
                if (!this.modulestorage && this.module < 2) {
                    this.modulestorage = true;
                    this.module++;
                    entityPlayer.getHeldItem(hand).setCount(entityPlayer.getHeldItem(hand).getCount() - 1);
                    return true;
                }
            }
        }
        return super.onActivated(entityPlayer, hand, side, hitX, hitY, hitZ);
    }

    public abstract String getInventoryName();

    public boolean canConnectEnergy(EnumFacing arg0) {
        return true;
    }

    public int getEnergyStored(EnumFacing from) {
        return (int) this.energy2;
    }

    public int getMaxEnergyStored(EnumFacing from) {
        return (int) this.maxEnergy2;
    }

    private double getspeed() {
        switch (this.sizeWorkingSlot) {
            case 2:
                return 2.2;
            case 3:
                return 3.4;
            case 4:
                return 4.5;
            default:
                return 1;
        }
    }

    private double getcoef() {
        switch (this.sizeWorkingSlot) {
            case 3:
                return 0.9;
            case 4:
                return 0.8;
            default:
                return 1;
        }
    }

    public abstract EnumMultiMachine getMachine();

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        for (int i = 0; i < sizeWorkingSlot; i++) {
            this.progress[i] = nbttagcompound.getShort("progress" + i);
        }
        if (nbttagcompound.getInteger("expstorage") > 0) {
            this.expstorage = nbttagcompound.getInteger("expstorage");
        }
        this.energy2 = nbttagcompound.getDouble("energy2");
        this.rf = nbttagcompound.getBoolean("rf");
        this.quickly = nbttagcompound.getBoolean("quickly");
        this.modulesize = nbttagcompound.getBoolean("modulesize");
        this.modulestorage = nbttagcompound.getBoolean("modulestorage");
        int id = nbttagcompound.getInteger("panelid");
        if (id != -1) {
            this.solartype = IUItem.map1.get(id);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        for (int i = 0; i < sizeWorkingSlot; i++) {
            nbttagcompound.setShort("progress" + i, progress[i]);
        }
        if (this.expstorage > 0) {
            nbttagcompound.setInteger("expstorage", this.expstorage);
        }
        nbttagcompound.setDouble("energy2", this.energy2);
        nbttagcompound.setBoolean("rf", this.rf);
        nbttagcompound.setBoolean("quickly", this.quickly);
        nbttagcompound.setBoolean("modulesize", this.modulesize);
        nbttagcompound.setBoolean("modulestorage", this.modulestorage);


        if (this.solartype != null) {
            nbttagcompound.setInteger("panelid", this.solartype.meta);
        } else {
            nbttagcompound.setInteger("panelid", -1);
        }
        return nbttagcompound;
    }

    protected void initiate(int soundEvent) {
        IC2.network.get(true).initiateTileEntityEvent(this, soundEvent, true);
    }

    protected void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
        }

    }

    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }

    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
        }

    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        boolean needsInvUpdate = false;
        if (solartype != null) {
            if (this.energy.getEnergy() < this.energy.getCapacity() || (this.energy2 < this.maxEnergy2 && this.rf)) {
                TileEntitySolarPanel panel = new TileEntitySolarPanel(solartype);
                if (panel.getWorld() != this.getWorld()) {
                    panel.setWorld(this.getWorld());
                }
                panel.skyIsVisible = this.world.canBlockSeeSky(this.pos.up()) &&
                        (this.world.getBlockState(this.pos.up()).getMaterial().getMaterialMapColor() ==
                                MapColor.AIR) && !panel.noSunWorld;
                panel.wetBiome = panel.getWorld().getBiome(this.pos).getRainfall() > 0.0F;
                panel.rain = panel.wetBiome && (this.world.isRaining() || this.world.isThundering());
                panel.sunIsUp = this.getWorld().isDaytime();

                if (panel.active == null || this.getWorld().provider.getWorldTime() % 40 == 0) {
                    updateVisibility(panel);
                }
                panel.gainFuel();
                if (this.energy.getEnergy() < this.energy.getCapacity()) {
                    this.energy.addEnergy(Math.min(panel.generating, energy.getFreeEnergy()));
                } else if (this.energy2 < this.maxEnergy2 && this.rf) {
                    this.energy2 += Math.min(panel.generating, (this.maxEnergy2 - this.energy2) / Config.coefficientrf);
                }
            }
        }
        int quickly = 1;

        boolean isActive = false;
        if (this.world.provider.getWorldTime() % 10 == 0) {
            if (this.modulestorage && !this.inputSlots.isEmpty()) {
                final ItemStack stack = this.inputSlots.getItem();
                int size = 0;
                int col = 0;
                for (int i = 0; i < sizeWorkingSlot; i++) {
                    ItemStack stack1 = this.inputSlots.get1(i);

                    if (stack1.isItemEqual(stack)) {
                        size += stack1.getCount();
                    }

                    if (stack1.isItemEqual(stack) || stack1.isEmpty()) {
                        col++;
                    }
                }
                int count = size / col;
                int count1 = size - (count * col);
                for (int i = 0; i < sizeWorkingSlot; i++) {
                    ItemStack stack1 = this.inputSlots.get1(i);
                    if ((stack1.isItemEqual(stack)) || stack1.isEmpty()) {
                        ItemStack stack2 = stack.copy();
                        int dop = 0;
                        int prom = 64 - count;
                        if (prom > 0) {
                            if (count1 > prom) {
                                dop += prom;
                                count1 -= prom;
                            } else {
                                dop += count1;
                                count1 = 0;
                            }

                        }

                        stack2.setCount(count + dop);
                        this.inputSlots.put1(i, stack2);

                    }

                }


            }
        }

        for (int i = 0; i < sizeWorkingSlot; i++) {
            RecipeOutput output = getOutput(i);
            if (this.quickly) {
                quickly = 100;
            }
            int size = 1;
            if (!this.inputSlots.get1(i).isEmpty()) {
                if (this.modulesize) {
                    for (int j = 0; ; j++) {
                        ItemStack stack = new ItemStack(
                                this.inputSlots.get1(i).getItem(),
                                j,
                                this.inputSlots.get1(i).getItemDamage()
                        );
                        if (recipe != null) {
                            if (recipe.apply(stack, false) != null) {
                                size = j;
                                break;
                            }
                        }
                    }
                    size = (int) Math.floor((float) this.inputSlots.get1(i).getCount() / size);
                    int size1 = 0;

                    for (int ii = 0; ii < sizeWorkingSlot; ii++) {
                        if (this.outputSlot.get(ii) != null) {
                            size1 += (64 - this.outputSlot.get(ii).getCount());
                        } else {
                            size1 += 64;
                        }
                    }
                    if (output != null) {
                        size1 = size1 / output.items.get(0).getCount();
                    }
                    size = Math.min(size1, size);
                }
            }
            if (output != null && (this.energy.canUseEnergy(this.energyConsume * quickly * size) || this.energy2 >= Math.abs(this.energyConsume * 4 * quickly * size))) {
                setActive(true);
                if (this.progress[i] == 0) {
                    initiate(0);
                    col[i] = this.inputSlots.get1(i).getCount();
                }
                if (this.inputSlots.get1(i).getCount() != col[i] && this.modulesize) {
                    this.progress[i] = (short) (col[i] * this.progress[i] / this.inputSlots.get1(i).getCount());
                    col[i] = this.inputSlots.get1(i).getCount();
                }
                if (this.energy.getEnergy() >= this.energyConsume * quickly * size) {
                    this.energy.useEnergy(this.energyConsume * quickly * size);
                } else if (this.energy2 >= Math.abs(this.energyConsume * 4 * quickly * size)) {
                    this.energy2 -= Math.abs(this.energyConsume * 4 * quickly * size);
                } else {
                    return;
                }
                isActive = true;
                this.progress[i]++;

                this.guiProgress[i] = (double) this.progress[i] / this.operationLength;

                if (this.progress[i] >= this.operationLength || this.quickly) {
                    this.guiProgress[i] = 0;
                    this.progress[i] = 0;
                    if (this.expstorage < 5000) {
                        Random rand = new Random();

                        int exp = rand.nextInt(3) + 1;
                        this.expstorage = this.expstorage + exp;
                        if (this.expstorage >= 5000) {
                            expstorage = 5000;
                        }
                    }

                    operate(i, output, size);
                    needsInvUpdate = true;
                    initiate(2);
                }
            } else {
                if (this.progress[i] != 0 && getActive()) {
                    initiate(1);
                }
                if (output == null) {
                    this.progress[i] = 0;
                }

            }

        }

        if (getActive() != isActive) {
            setActive(isActive);
        }


        needsInvUpdate |= this.upgradeSlot.tickNoMark();
        if (needsInvUpdate) {
            super.markDirty();
        }

    }

    public void setOverclockRates() {
        this.upgradeSlot.onChanged();
        this.operationsPerTick = this.upgradeSlot.getOperationsPerTick(this.defaultOperationLength);
        this.operationLength = this.upgradeSlot.getOperationLength(this.defaultOperationLength);
        this.energyConsume = this.upgradeSlot.getEnergyDemand(this.defaultEnergyConsume);
        int tier = this.upgradeSlot.getTier(this.defaultTier);
        this.energy.setSinkTier(tier);
        this.dischargeSlot.setTier(tier);
        this.energy.setCapacity(this.upgradeSlot.getEnergyStorage(
                this.defaultEnergyStorage,
                this.defaultOperationLength,
                this.defaultEnergyConsume
        ));

    }

    public void operate(int slotId, RecipeOutput output, int size) {
        for (int i = 0; i < this.operationsPerTick; i++) {

            operateOnce(slotId, output.items, size, output);
            output = getOutput(slotId);
            if (output == null) {
                break;
            }
        }
    }


    public void operateOnce(int slotId, List<ItemStack> processResult, int size, RecipeOutput output) {

        for (int i = 0; i < size; i++) {
            if (!random) {
                this.inputSlots.consume(slotId);
                this.outputSlot.add(processResult);
            } else {
                Random rand = new Random();
                if (rand.nextInt(max + 1) <= min) {
                    this.inputSlots.consume(slotId);
                    this.outputSlot.add(processResult);
                }
            }
        }
    }

    public RecipeOutput getOutput(int slotId) {
        if (this.inputSlots.isEmpty(slotId)) {
            return null;
        }
        RecipeOutput output = this.inputSlots.process(slotId);
        if (output == null) {
            return null;
        }
        if (this.outputSlot.canAdd(output.items)) {
            return output;
        }

        return null;
    }


    public ContainerBase<? extends TileEntityMultiMachine> getGuiContainer(EntityPlayer player) {
        return new ContainerMultiMachine(player, this, this.sizeWorkingSlot);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        if (type == 0) {
            return new GUIMultiMachine(new ContainerMultiMachine(player, this, sizeWorkingSlot));
        }
        if (type == 1) {
            return new GUIMultiMachine1(new ContainerMultiMachine(player, this, sizeWorkingSlot));
        }
        if (type == 2) {
            return new GUIMultiMachine2(new ContainerMultiMachine(player, this, sizeWorkingSlot));
        }
        if (type == 3) {
            return new GUIMultiMachine3(new ContainerMultiMachine(player, this, sizeWorkingSlot));
        }

        return null;
    }

    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }

    public void onNetworkEvent(int event) {
        if (this.audioSource == null && this.getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, this.getStartSoundFile());
        }

        switch (event) {
            case 0:
                if (this.audioSource != null && !this.audioSource.playing()) {
                    this.audioSource.play();
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    boolean stop = true;
                    for (int i = 0; i < sizeWorkingSlot; i++) {
                        if (this.getOutput(i) != null) {
                            stop = false;
                            break;
                        }
                    }
                    if (stop) {
                        if (this.getInterruptSoundFile() != null) {
                            IUCore.audioManager.playOnce(
                                    this,
                                    PositionSpec.Center,
                                    this.getInterruptSoundFile(),
                                    false,
                                    IUCore.audioManager.getDefaultVolume()
                            );
                        }
                    } else {
                        if (this.audioSource != null && !this.audioSource.playing()) {
                            this.audioSource.play();
                        }
                    }
                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
            case 3:
        }

    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        return this.energy.useEnergy(amount);
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing
        );
    }

    public void onGuiClosed(EntityPlayer player) {
    }


    public final float getChargeLevel1() {
        return Math.min((float) this.energy2 / (float) this.maxEnergy2, 1);
    }

    public final float getChargeLevel2() {
        return Math.min((float) this.energy.getEnergy() / (float) this.energy.getCapacity(), 1);
    }

    public double getProgress(int slotId) {
        return this.guiProgress[slotId];
    }

    public int getMode() {
        return 0;
    }

    public void onUpgraded() {
        this.rerender();
    }

}
