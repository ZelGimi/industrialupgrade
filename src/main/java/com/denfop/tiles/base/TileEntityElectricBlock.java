package com.denfop.tiles.base;

import cofh.redstoneflux.api.IEnergyContainerItem;
import com.denfop.IUCore;
import com.denfop.api.IStorage;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IAdvEnergySource;
import com.denfop.api.inv.IHasGui;
import com.denfop.audio.AudioSource;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.ComparatorEmitter;
import com.denfop.componets.Redstone;
import com.denfop.componets.RedstoneEmitter;
import com.denfop.container.ContainerElectricBlock;
import com.denfop.gui.GuiElectricBlock;
import com.denfop.invslot.InvSlotCharge;
import com.denfop.invslot.InvSlotDischarge;
import com.denfop.invslot.InvSlotElectricBlock;
import com.denfop.proxy.CommonProxy;
import com.denfop.tiles.panels.entity.WirelessTransfer;
import com.denfop.tiles.wiring.EnumElectricBlock;
import com.denfop.utils.ModUtils;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.IC2;
import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.ref.TeBlock;
import ic2.core.util.ConfigUtil;
import ic2.core.util.EntityIC2FX;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class TileEntityElectricBlock extends TileEntityInventory implements IHasGui,
        INetworkClientTileEntityEventListener,
        IStorage, INetworkTileEntityEventListener {

    public static EnumElectricBlock electricblock;
    public final double tier;
    public final boolean chargepad;
    public final String name;
    public final AdvEnergy energy;
    public final double maxStorage2;
    public final double l;
    public final InvSlotCharge inputslotA;
    public final InvSlotDischarge inputslotB;
    public final InvSlotElectricBlock inputslotC;
    private final RedstoneEmitter rsEmitter;
    private final Redstone redstone;
    private final ComparatorEmitter comparator;
    public boolean wireless;
    public double output;
    public boolean rfeu = false;
    public boolean needsInvUpdate = false;
    public boolean movementcharge = false;
    public double output_plus;
    public short temp;
    public boolean load = false;
    public boolean movementchargeitem = false;
    public List<WirelessTransfer> wirelessTransferList = new ArrayList<>();
    private AudioSource audioSource;
    private byte redstoneMode = 0;
    private EntityPlayer player;

    public TileEntityElectricBlock(double tier1, double output1, double maxStorage1, boolean chargepad, String name) {

        this.tier = tier1;
        this.output = EnergyNetGlobal.instance.getPowerFromTier((int) tier);
        this.maxStorage2 = maxStorage1 * 4;
        this.chargepad = chargepad;
        this.name = name;
        this.inputslotA = new InvSlotCharge(this, (int) tier);

        this.inputslotB = new InvSlotDischarge(this, (int) tier);
        this.inputslotB.setAllowRedstoneDust(false);
        this.inputslotC = new InvSlotElectricBlock(this, 3, "input2", 2);
        this.output_plus = 0;
        this.temp = 0;
        this.wireless = false;
        this.l = output1;
        this.player = null;
        this.energy = this.addComponent((new AdvEnergy(this, maxStorage1,
                EnumSet.complementOf(EnumSet.of(this.getFacing())), EnumSet.of(this.getFacing()),
                EnergyNetGlobal.instance.getTierFromPower(this.output),
                EnergyNetGlobal.instance.getTierFromPower(this.output), false
        )));
        this.energy.addManagedSlot(this.inputslotA);
        this.energy.addManagedSlot(this.inputslotB);
        this.rsEmitter = this.addComponent(new RedstoneEmitter(this));
        this.redstone = this.addComponent(new Redstone(this));
        this.comparator = this.addComponent(new ComparatorEmitter(this));
        this.comparator.setUpdate(this.energy::getComparatorValue);
    }

    public TileEntityElectricBlock(EnumElectricBlock electricBlock) {
        this(electricBlock.tier, electricBlock.producing, electricBlock.maxstorage, electricBlock.chargepad, electricBlock.name1);
        electricblock = electricBlock;
    }

    public static EnumElectricBlock getElectricBlock() {

        return electricblock;
    }

    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }

    }


    public void initiate(int soundEvent) {

        IUCore.network.get(true).initiateTileEntityEvent(this, soundEvent, true);

    }

    public String getStartSoundFile() {
        return "Machines/pen.ogg";
    }


    public void onNetworkEvent(int event) {
        if (this.audioSource == null && this.getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, this.getStartSoundFile());
        }

        if (event == 0) {
            if (this.audioSource != null) {
                this.audioSource.stop();
                this.audioSource.play();
            }
        }

    }

    public List<ItemStack> getDrop() {
        return getAuxDrops(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, final List<String> info, final ITooltipFlag advanced) {


        info.add(Localization.translate("ic2.item.tooltip.Output") + " " + ModUtils.getString(EnergyNetGlobal.instance.getPowerFromTier(
                this.energy.getSourceTier())) + " EU/t ");
        info.add(Localization.translate("ic2.item.tooltip.Capacity") + " " + ModUtils.getString(this.energy.getCapacity()) + " EU ");
        info.add(Localization.translate("ic2.item.tooltip.Capacity") + " " + ModUtils.getString(this.maxStorage2) + " RF ");
        NBTTagCompound nbttagcompound = ModUtils.nbt(itemStack);
        info.add(Localization.translate("ic2.item.tooltip.Store") + " " + ModUtils.getString(nbttagcompound.getDouble("energy"))
                + " EU ");
        info.add(Localization.translate("ic2.item.tooltip.Store") + " " + ModUtils.getString(nbttagcompound.getDouble("energy2"))
                + " RF ");
        info.add(Localization.translate("iu.tier") + ModUtils.getString(this.tier));


    }

    public ContainerElectricBlock getGuiContainer(EntityPlayer player) {
        return new ContainerElectricBlock(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiElectricBlock(new ContainerElectricBlock(entityPlayer, this));
    }

    public boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {


        module_charge(player);
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);

    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
    }

    public boolean shouldEmitEnergy() {
        boolean redstone = this.redstone.hasRedstoneInput();
        if (this.redstoneMode == 5) {
            return !redstone;
        } else if (this.redstoneMode != 6) {
            return true;
        } else {
            return !redstone || this.energy.getEnergy() > this.energy.getCapacity() - this.output * 20.0;
        }
    }

    @Override
    public double getEUStored() {
        return this.energy.getEnergy();
    }


    @Override
    public double getOutput() {
        return this.output;
    }

    protected void getItems(EntityPlayer player) {
        if (!this.canEntityDestroy(player)) {
            IC2.platform.messagePlayer(player, Localization.translate("iu.error"));
            return;
        }


        for (ItemStack current : player.inventory.armorInventory) {
            if (current != null) {
                chargeitems(current, this.output);
            }
        }
        for (ItemStack current : player.inventory.mainInventory) {
            if (current != null) {
                chargeitems(current, this.output);
            }
        }
        player.inventoryContainer.detectAndSendChanges();

    }

    @SideOnly(Side.CLIENT)
    protected void updateEntityClient() {
        super.updateEntityClient();
        World world = this.getWorld();
        Random rnd = world.rand;
        if (rnd.nextInt(8) == 0) {
            if (this.getActive()) {
                ParticleManager effect = FMLClientHandler.instance().getClient().effectRenderer;

                for (int particles = 20; particles > 0; --particles) {
                    double x = (float) this.pos.getX() + 0.0F + rnd.nextFloat();
                    double y = (float) this.pos.getY() + 0.9F + rnd.nextFloat();
                    double z = (float) this.pos.getZ() + 0.0F + rnd.nextFloat();
                    effect.addEffect(new EntityIC2FX(
                            world,
                            x,
                            y,
                            z,
                            60,
                            new double[]{0.0D, 0.1D, 0.0D},
                            new float[]{0.2F, 0.2F, 1.0F}
                    ));
                }
            }

        }
    }

    protected void chargeitems(ItemStack itemstack, double chargefactor) {
        if (!(itemstack.getItem() instanceof ic2.api.item.IElectricItem || itemstack.getItem() instanceof IEnergyContainerItem)) {
            return;
        }
        double freeamount = ElectricItem.manager.charge(itemstack, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true, true);
        double charge;
        if (freeamount > 0.0D) {
            charge = Math.min(freeamount, chargefactor);
            if (this.energy.getEnergy() < charge) {
                charge = this.energy.getEnergy();
            }
            this.energy.useEnergy(ElectricItem.manager.charge(itemstack, charge, Integer.MAX_VALUE, true, false));
        }

    }

    public float getChargeLevel() {

        float ret = (float) ((float) this.energy.getEnergy() / (this.energy.getCapacity()));

        if (ret > 1.0F) {
            ret = 1.0F;
        }
        return ret;
    }


    public void module_charge(EntityPlayer entityPlayer) {

        if (this.movementcharge) {

            for (ItemStack armorcharged : entityPlayer.inventory.armorInventory) {
                if (armorcharged != null) {
                    if (armorcharged.getItem() instanceof IElectricItem && this.energy.getEnergy() > 0) {
                        double sent = ElectricItem.manager.charge(armorcharged, this.energy.getEnergy(), 2147483647, true,
                                false
                        );
                        entityPlayer.inventoryContainer.detectAndSendChanges();
                        this.energy.useEnergy(sent);

                        this.needsInvUpdate = (sent > 0.0D);
                        if (sent > 0) {
                            CommonProxy.sendPlayerMessage(
                                    entityPlayer,
                                    Localization.translate("successfully.charged")
                                            + armorcharged.getDisplayName()
                                            + Localization.translate("iu.sendenergy")
                                            + ModUtils.getString(sent) + " EU"
                            );
                        }

                    }

                }

            }

        }

        if (this.movementchargeitem) {
            for (ItemStack charged : entityPlayer.inventory.mainInventory) {
                if (charged != null) {
                    if (charged.getItem() instanceof IElectricItem && this.energy.getEnergy() > 0) {
                        double sent = ElectricItem.manager.charge(charged, this.energy.getEnergy(), 2147483647, true, false);

                        this.energy.useEnergy(sent);
                        this.needsInvUpdate = (sent > 0.0D);
                        if (sent > 0) {
                            CommonProxy.sendPlayerMessage(
                                    entityPlayer,
                                    Localization.translate("successfully.charged")
                                            + charged.getDisplayName()
                                            + Localization.translate("iu.sendenergy")
                                            + ModUtils.getString(sent) + " EU"
                            );
                        }
                        entityPlayer.inventoryContainer.detectAndSendChanges();

                    }

                }

            }

        }
    }

    protected List<AxisAlignedBB> getAabbs(boolean forCollision) {
        if (chargepad) {
            return Collections.singletonList(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D));
        } else {
            return super.getAabbs(forCollision);
        }
    }

    protected void updatePlayer(EntityPlayer entity) {
        this.player = entity;
    }

    protected void onEntityCollision(Entity entity) {
        super.onEntityCollision(entity);
        if (!this.getWorld().isRemote && entity instanceof EntityPlayer) {
            if (this.chargepad && this.canEntityDestroy(entity)) {
                this.updatePlayer((EntityPlayer) entity);
            }

        }

    }


    protected void updateEntityServer() {
        super.updateEntityServer();
        if (!load) {
            this.wirelessTransferList.clear();
            this.inputslotC.wirelessmodule();
            this.wireless = !this.wirelessTransferList.isEmpty();
            this.load = true;
        }
        this.needsInvUpdate = false;
        this.energy.setSendingEnabled(this.shouldEmitEnergy());
        this.rsEmitter.setLevel(this.shouldEmitRedstone() ? 15 : 0);
        if (this.wireless) {
            boolean refresh = false;
            try {
                for (WirelessTransfer transfer : this.wirelessTransferList) {
                    if (transfer.getTile().isInvalid()) {
                        refresh = true;
                        continue;
                    }
                    double energy = Math.min(
                            ((IAdvEnergySource) this.energy.getDelegate()).getOfferedEnergy(),
                            transfer.getSink().getDemandedEnergy()
                    );
                    transfer.work(energy);
                    this.energy.useEnergy(energy);
                }
            } catch (Exception ignored) {
            }
            if (refresh) {
                this.wirelessTransferList.clear();
                this.inputslotC.wirelessmodule();
            }
        }
        if (chargepad) {
            if (this.player != null && this.energy.getEnergy() >= 1.0D) {
                if (!getActive()) {
                    setActive(true);
                }
                getItems(this.player);
                module_charge(this.player);
                this.player = null;
                needsInvUpdate = true;
            } else if (getActive()) {
                setActive(false);
                needsInvUpdate = true;
            }
        }
        boolean ignore = this.inputslotC.checkignore();
        this.inputslotA.setIgnore(ignore);
    }

    public int getCapacity() {
        return (int) this.energy.getCapacity();
    }

    @Override
    public double getEUCapacity() {
        return this.energy.getCapacity();
    }


    public int addEnergy(int amount) {
        this.energy.addEnergy(amount);
        return amount;
    }

    @Override
    public int getTier() {
        return this.energy.getSinkTier();
    }


    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (!(getWorld()).isRemote) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            this.energy.addEnergy(nbt.getDouble("energy"));
        }
    }

    protected List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = new ArrayList<>();
        ret.addAll(this.getSelfDrops(fortune, true));
        ret.addAll(this.getAuxDrops(fortune));
        return ret;
    }

    protected List<ItemStack> getSelfDrops(int fortune, boolean wrench) {
        ItemStack drop = this.getPickBlock(null, null);
        drop = this.adjustDrop(drop, wrench, fortune);
        return drop == null ? Collections.emptyList() : Collections.singletonList(drop);
    }

    private ItemStack adjustDrop(ItemStack drop, boolean wrench, int fortune) {
        drop = super.adjustDrop(drop, wrench);
        if (wrench || this.teBlock.getDefaultDrop() == TeBlock.DefaultDrop.Self) {
            double retainedRatio = ConfigUtil.getDouble(MainConfig.get(), "balance/energyRetainedInStorageBlockDrops");
            if (fortune == 100) {
                retainedRatio = 1;
            }
            double totalEnergy = this.energy.getEnergy();
            if (retainedRatio > 0.0D && totalEnergy > 0.0D) {
                NBTTagCompound nbt = StackUtil.getOrCreateNbtData(drop);
                nbt.setDouble("energy", Math.round(totalEnergy * retainedRatio));

            }
        }
        return drop;
    }

    protected ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (wrench || this.teBlock.getDefaultDrop() == TeBlock.DefaultDrop.Self) {
            double retainedRatio = ConfigUtil.getDouble(MainConfig.get(), "balance/energyRetainedInStorageBlockDrops");
            double totalEnergy = this.energy.getEnergy();
            if (retainedRatio > 0.0D && totalEnergy > 0.0D) {
                NBTTagCompound nbt = StackUtil.getOrCreateNbtData(drop);
                nbt.setDouble("energy", Math.round(totalEnergy * retainedRatio));

            }
        }
        return drop;
    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        if (!this.chargepad) {
            this.energy.setDirections(EnumSet.complementOf(EnumSet.of(this.getFacing())), (EnumSet.of(this.getFacing())));
        } else {
            this.energy.setDirections(
                    EnumSet.complementOf(EnumSet.of(this.getFacing(), EnumFacing.UP)),
                    EnumSet.of(this.getFacing())
            );
        }
        this.rfeu = nbttagcompound.getBoolean("rfeu");
        this.redstoneMode = nbttagcompound.getByte("redstoneMode");
    }

    public void setFacing(EnumFacing facing) {
        super.setFacing(facing);
        if (!this.chargepad) {
            this.energy.setDirections(EnumSet.complementOf(EnumSet.of(this.getFacing())), (EnumSet.of(this.getFacing())));
        } else {
            this.energy.setDirections(EnumSet.complementOf(EnumSet.of(facing, EnumFacing.UP)), EnumSet.of(facing));

        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("rfeu", this.rfeu);
        nbttagcompound.setByte("redstoneMode", this.redstoneMode);
        return nbttagcompound;
    }


    public void onGuiClosed(EntityPlayer player) {
    }

    public boolean shouldEmitRedstone() {
        switch (this.redstoneMode) {
            case 1:
                return this.energy.getEnergy() >= this.energy.getCapacity() - this.output * 20.0;
            case 2:
                return this.energy.getEnergy() > this.output && this.energy.getEnergy() < this.energy.getCapacity() - this.output;
            case 3:
                return this.energy.getEnergy() < this.energy.getCapacity() - this.output;
            case 4:
                return this.energy.getEnergy() < this.output;
            default:
                return false;
        }
    }

    public void onNetworkEvent(EntityPlayer player, int event) {

        ++this.redstoneMode;
        if (this.redstoneMode >= 7) {
            this.redstoneMode = 0;
        }

        IC2.platform.messagePlayer(player, this.getStringRedstoneMode());


    }

    public byte getRedstoneMode() {
        return redstoneMode;
    }

    public String getStringRedstoneMode() {
        return this.redstoneMode < 7 && this.redstoneMode >= 0 ?
                Localization.translate("ic2.EUStorage.gui.mod.redstone" + this.redstoneMode) : "";
    }


    public String getInventoryName() {
        return Localization.translate(this.name);
    }


}
