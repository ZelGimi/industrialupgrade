package com.denfop.tiles.base;

import cofh.redstoneflux.api.IEnergyContainerItem;
import cofh.redstoneflux.api.IEnergyHandler;
import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import com.denfop.Config;
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
import com.denfop.invslot.InvSlotElectricBlock;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.proxy.CommonProxy;
import com.denfop.tiles.panels.entity.TransferRFEnergy;
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
import ic2.core.util.Util;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class TileEntityElectricBlock extends TileEntityInventory implements IHasGui,
        INetworkClientTileEntityEventListener, IEnergyHandler, IEnergyReceiver,
        IEnergyProvider, IStorage, INetworkTileEntityEventListener {

    public static EnumElectricBlock electricblock;
    public final double tier;
    public final boolean chargepad;
    public final String name;
    public final AdvEnergy energy;
    public final double maxStorage2;
    public final double l;
    public final InvSlotElectricBlock inputslotA;
    public final InvSlotElectricBlock inputslotB;
    public final InvSlotElectricBlock inputslotC;
    public final List<String> list_player;
    private final RedstoneEmitter rsEmitter;
    private final Redstone redstone;
    private final ComparatorEmitter comparator;
    public boolean wireless;
    public EntityPlayer player;
    public double output;
    public String UUID = "";
    public double energy2;
    public boolean rf;
    public boolean rfeu = false;
    public boolean needsInvUpdate = false;
    public boolean movementcharge = false;
    public boolean movementchargerf = false;
    public boolean movementchargeitemrf = false;
    public double output_plus;
    public short temp;
    public boolean movementchargeitem = false;
    public boolean personality = false;
    public List<WirelessTransfer> wirelessTransferList = new ArrayList<>();
    List<TransferRFEnergy> transferRFEnergyList = new ArrayList<>();
    private AudioSource audioSource;
    private byte redstoneMode = 0;

    public TileEntityElectricBlock(double tier1, double output1, double maxStorage1, boolean chargepad, String name) {

        this.energy2 = 0.0D;
        this.tier = tier1;
        this.output = EnergyNetGlobal.instance.getPowerFromTier((int) tier);
        this.player = null;
        this.maxStorage2 = maxStorage1 * 4;
        this.chargepad = chargepad;
        this.rf = false;
        this.name = name;
        this.inputslotA = new InvSlotElectricBlock(this, 1, "input", 1);
        this.inputslotB = new InvSlotElectricBlock(this, 2, "input1", 1);
        this.inputslotC = new InvSlotElectricBlock(this, 3, "input2", 2);
        this.output_plus = 0;
        this.temp = 0;
        this.wireless = false;
        this.l = output1;
        this.energy = this.addComponent((new AdvEnergy(this, maxStorage1,
                EnumSet.complementOf(EnumSet.of(this.getFacing())), EnumSet.of(this.getFacing()),
                EnergyNetGlobal.instance.getTierFromPower(this.output),
                EnergyNetGlobal.instance.getTierFromPower(this.output), false
        )));
        this.rsEmitter = this.addComponent(new RedstoneEmitter(this));
        this.redstone = this.addComponent(new Redstone(this));
        this.comparator = this.addComponent(new ComparatorEmitter(this));
        this.comparator.setUpdate(this.energy::getComparatorValue);
        this.list_player = new ArrayList<>();
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

    protected boolean canEntityDestroy(Entity entity) {
        return !this.personality || (entity instanceof EntityPlayer && this.list_player.contains(entity.getName()));
    }

    @Override
    protected boolean wrenchCanRemove(final EntityPlayer player) {
        return !this.personality || (this.list_player.contains(player.getName()));

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

        if (personality) {
            if (!(this.list_player.contains(player.getName()) || player.capabilities.isCreativeMode)) {
                CommonProxy.sendPlayerMessage(player, Localization.translate("iu.error"));
                return false;
            }
        }
        module_charge(player);
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);

    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        this.list_player.add(UUID);
        for (int h = 0; h < 2; h++) {
            if (!inputslotC.get(h).isEmpty() && inputslotC.get(h).getItem() instanceof ItemAdditionModule
                    && inputslotC.get(h).getItemDamage() == 0) {
                NBTTagCompound nbt = ModUtils.nbt(inputslotC.get(h));
                int size = nbt.getInteger("size");
                for (int m = 0; m < size; m++) {
                    this.list_player.add(nbt.getString("player_" + m));

                }
                break;
            }

        }
        this.wirelessTransferList.clear();
        this.inputslotC.wirelessmodule();

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
    public double getRFStored() {
        return this.energy2;
    }

    @Override
    public double getOutput() {
        return this.output;
    }

    protected void getItems(EntityPlayer player) {
        List<String> list = new ArrayList<>();
        list.add(UUID);
        for (int h = 0; h < 2; h++) {
            if (inputslotC.get(h) != null && inputslotC.get(h).getItem() instanceof ItemAdditionModule
                    && inputslotC.get(h).getItemDamage() == 0) {
                for (int m = 0; m < 9; m++) {
                    NBTTagCompound nbt = ModUtils.nbt(inputslotC.get(h));
                    String name = "player_" + m;
                    if (!nbt.getString(name).isEmpty()) {
                        list.add(nbt.getString(name));
                    }
                }
                break;
            }

        }


        if (player != null) {
            if (personality) {
                if (!(list.contains(player.getDisplayName().getFormattedText()) || player.capabilities.isCreativeMode)) {
                    IC2.platform.messagePlayer(player, Localization.translate("iu.error"));
                    return;
                }
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
        if (this.energy2 > 0 && itemstack.getItem() instanceof IEnergyContainerItem) {
            double sent = 0;

            IEnergyContainerItem item = (IEnergyContainerItem) itemstack.getItem();
            double energy_temp = this.energy2;
            if (item.getEnergyStored(itemstack) < item.getMaxEnergyStored(itemstack)
                    && this.energy2 > 0) {
                sent = (sent + this.extractEnergy1(
                        item.receiveEnergy(itemstack, (int) this.energy2, false), false));

            }
            energy_temp -= (sent * 2);
            this.energy2 = energy_temp;


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

    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {

        return receiveEnergy(maxReceive, simulate);

    }

    public int receiveEnergy(int paramInt, boolean paramBoolean) {
        int i = (int) Math.min(this.maxStorage2 - this.energy2, Math.min(this.output * 4, paramInt));
        if (!paramBoolean) {
            this.energy2 += i;
        }
        return i;
    }

    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        return extractEnergy((int) Math.min(this.output * 4, maxExtract), simulate);
    }

    public int extractEnergy(int paramInt, boolean paramBoolean) {
        int i = (int) Math.min(this.energy2, Math.min(this.output * 4, paramInt));
        if (!paramBoolean) {
            this.energy2 -= i;
        }
        return i;
    }

    public float getChargeLevel() {

        float ret = (float) ((float) this.energy.getEnergy() / (this.energy.getCapacity()));

        if (ret > 1.0F) {
            ret = 1.0F;
        }
        return ret;
    }

    public float getChargeLevel1() {

        float ret = (float) ((float) this.energy2 / (this.maxStorage2));

        if (ret > 1.0F) {
            ret = 1.0F;
        }
        return ret;
    }

    public boolean canConnectEnergy(EnumFacing arg0) {
        return true;
    }

    public int getEnergyStored(EnumFacing from) {
        return (int) this.energy2;
    }

    public int getMaxEnergyStored(EnumFacing from) {
        return (int) this.maxStorage2;
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
        if (this.movementchargerf) {

            for (ItemStack charged : entityPlayer.inventory.armorInventory) {
                if (charged != null) {

                    if (charged.getItem() instanceof IEnergyContainerItem && this.energy2 > 0) {
                        double sent = 0;

                        IEnergyContainerItem item = (IEnergyContainerItem) charged.getItem();
                        double energy_temp = this.energy2;
                        while (item.getEnergyStored(charged) < item.getMaxEnergyStored(charged)
                                && this.energy2 > 0) {
                            sent = (sent + this.extractEnergy1(
                                    item.receiveEnergy(charged, (int) this.energy2, false), false));

                        }
                        energy_temp -= (sent);
                        this.energy2 = energy_temp;
                        if (sent > 0) {
                            CommonProxy.sendPlayerMessage(
                                    entityPlayer,
                                    Localization.translate("successfully.charged")
                                            + charged.getDisplayName()
                                            + Localization.translate("iu.sendenergy")
                                            + ModUtils.getString(sent) + " RF"
                            );
                        }
                        entityPlayer.inventoryContainer.detectAndSendChanges();

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
        if (this.movementchargeitemrf) {
            for (ItemStack charged : entityPlayer.inventory.mainInventory) {
                if (charged != null) {

                    if (charged.getItem() instanceof IEnergyContainerItem && this.energy2 > 0) {
                        double sent = 0;

                        IEnergyContainerItem item = (IEnergyContainerItem) charged.getItem();
                        double energy_temp = this.energy2;
                        while (item.getEnergyStored(charged) < item.getMaxEnergyStored(charged)
                                && this.energy2 > 0) {
                            sent = (sent + this.extractEnergy1(
                                    item.receiveEnergy(charged, (int) this.energy2, false), false));

                        }
                        energy_temp -= (sent);
                        this.energy2 = energy_temp;
                        if (sent > 0) {
                            CommonProxy.sendPlayerMessage(
                                    entityPlayer,
                                    Localization.translate("successfully.charged")
                                            + charged.getDisplayName()
                                            + Localization.translate("iu.sendenergy")
                                            + ModUtils.getString(sent) + " RF"
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

    protected void onEntityCollision(Entity entity) {
        super.onEntityCollision(entity);
        if (!this.getWorld().isRemote && entity instanceof EntityPlayer) {
            if (this.chargepad) {
                this.playerstandsat((EntityPlayer) entity);
            }
            if (player != null) {
                module_charge(player);
            }
        }

    }

    public void playerstandsat(EntityPlayer entity) {
        if (this.player == null) {
            this.player = entity;
        } else if (this.player.getUniqueID() != entity.getUniqueID()) {
            this.player = entity;
        }
    }


    protected void updateEntityServer() {
        super.updateEntityServer();
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
                this.player = null;
                needsInvUpdate = true;
            } else if (getActive()) {
                setActive(false);
                needsInvUpdate = true;
            }
        }

        if (this.rf) {
            if (!this.rfeu) {
                if (energy.getEnergy() > 0 && energy2 < maxStorage2) {

                    energy2 += energy.getEnergy() * Config.coefficientrf;
                    energy.useEnergy(energy.getEnergy());

                }
                if (energy2 > maxStorage2) {
                    double rf = (energy2 - maxStorage2);
                    energy.addEnergy(rf / Config.coefficientrf);
                    energy2 = maxStorage2;
                }
            } else {

                if (energy2 > 0 && energy.getEnergy() < energy.getCapacity()) {
                    energy2 -= energy.addEnergy(energy2 / Config.coefficientrf) * Config.coefficientrf;

                }

            }
        }
        IEnergyContainerItem item;
        if (this.energy2 >= 1.0D && this.inputslotA.get(0) != null
                && this.inputslotA.get(0).getItem() instanceof IEnergyContainerItem) {
            item = (IEnergyContainerItem) this.inputslotA.get(0).getItem();
            if (item.getEnergyStored(this.inputslotA.get(0)) < item.getMaxEnergyStored(this.inputslotA.get(0))) {
                extractEnergy1(
                        item.receiveEnergy(this.inputslotA.get(0), (int) this.energy2, false),
                        false
                );
            }
        }
        if (this.energy2 < 0) {
            this.energy2 = 0;
        }
        if (this.energy2 >= this.maxStorage2) {
            this.energy2 = this.maxStorage2;
        }
        if (!this.inputslotA.isEmpty()) {
            boolean ignore = this.inputslotC.checkignore();
            if (this.inputslotA.charge(
                    this.energy.getEnergy() > 1D ? this.energy.getEnergy() : 0,
                    this.inputslotA.get(0),
                    true, ignore
            ) != 0) {
                this.energy.useEnergy(this.inputslotA.charge(this.energy.getEnergy() > 1D ? this.energy.getEnergy() : 0,
                        this.inputslotA.get(0), false, ignore
                ));
                needsInvUpdate = ((this.energy.getEnergy() > 1D ? this.energy.getEnergy() : 0) > 0.0D);
            }
        }
        if (!this.inputslotB.get(0).isEmpty()) {
            if (this.inputslotB.discharge(
                    this.energy.getEnergy() < this.energy.getCapacity() ? this.energy.getEnergy() : 0,
                    this.inputslotB.get(0),
                    true
            ) != 0) {

                this.energy.addEnergy(this.inputslotB.discharge(this.energy.getEnergy() < this.energy.getCapacity() ?
                        this.energy.getEnergy() : 0, this.inputslotB.get(0), false));
                needsInvUpdate = ((this.energy.getEnergy() > 1D ? this.energy.getEnergy() : 0) > 0.0D);
            }
        }
        if (this.energy2 > 0) {
            if (this.getWorld().getWorldTime() % 60 == 0) {
                transferRFEnergyList.clear();
                for (EnumFacing facing : EnumFacing.VALUES) {
                    BlockPos pos = new BlockPos(
                            this.pos.getX() + facing.getFrontOffsetX(),
                            this.pos.getY() + facing.getFrontOffsetY(),
                            this.pos.getZ() + facing.getFrontOffsetZ()
                    );
                    TileEntity tile = this.getWorld().getTileEntity(pos);
                    if (tile == null) {
                        continue;
                    }
                    if (tile instanceof IEnergyReceiver) {
                        transferRFEnergyList.add(new TransferRFEnergy(tile, ((IEnergyReceiver) tile), facing));
                    }
                }
            }
        }
        boolean refresh = false;
        for (TransferRFEnergy rfEnergy : this.transferRFEnergyList) {
            if (rfEnergy.getTile().isInvalid()) {
                refresh = true;
                continue;
            }
            extractEnergy(rfEnergy.getFacing(), rfEnergy.getSink().receiveEnergy(rfEnergy.getFacing().getOpposite(),
                    extractEnergy(rfEnergy.getFacing(), (int) this.energy2, true), false
            ), false);
        }
        if (refresh) {
            transferRFEnergyList.clear();
            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos pos = new BlockPos(
                        this.pos.getX() + facing.getFrontOffsetX(),
                        this.pos.getY() + facing.getFrontOffsetY(),
                        this.pos.getZ() + facing.getFrontOffsetZ()
                );
                TileEntity tile = this.getWorld().getTileEntity(pos);
                if (tile == null) {
                    continue;
                }
                if (tile instanceof IEnergyReceiver) {
                    transferRFEnergyList.add(new TransferRFEnergy(tile, ((IEnergyReceiver) tile), facing));
                }
            }
        }


    }

    public int getCapacity() {
        return (int) this.energy.getCapacity();
    }

    @Override
    public double getEUCapacity() {
        return this.energy.getCapacity();
    }

    @Override
    public double getRFCapacity() {
        return this.maxStorage2;
    }


    public int addEnergy(int amount) {
        this.energy.addEnergy(amount);
        return amount;
    }

    @Override
    public int getTier() {
        return this.energy.getSinkTier();
    }

    public double extractEnergy1(double maxExtract, boolean simulate) {
        double temp;

        temp = this.energy2;

        if (temp > 0) {
            double energyExtracted = Math.min(temp, maxExtract);
            if (!simulate &&
                    this.energy2 - temp >= 0.0D) {
                this.energy2 -= temp;
                if (energyExtracted > 0) {
                    temp -= energyExtracted;
                    this.energy2 += temp;
                }
                return energyExtracted;
            }
        }
        return 0;
    }

    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (!(getWorld()).isRemote) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            this.energy.addEnergy(nbt.getDouble("energy"));
            this.energy2 = nbt.getDouble("energy2");
            this.UUID = placer.getName();
        }
    }


    public void onPlaced(double eustored, double eustored1, EntityPlayer player, EnumFacing side) {
        super.onPlaced(null, player, side);
        if (!(getWorld()).isRemote) {
            this.energy.addEnergy(eustored);
            this.energy2 = eustored1;

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
                nbt.setDouble("energy2", Math.round(this.energy2 * retainedRatio));

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
                nbt.setDouble("energy2", Math.round(this.energy2 * retainedRatio));

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
        this.UUID = nbttagcompound.getString("UUID");
        this.energy2 = Util.limit(nbttagcompound.getDouble("energy2"), 0.0D,
                this.maxStorage2
        );
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
        nbttagcompound.setDouble("energy2", this.energy2);
        nbttagcompound.setString("UUID", this.UUID);
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
        if (event == 10) {
            if (this.rf) {
                this.rfeu = !this.rfeu;
                initiate(0);
            }
        } else {
            ++this.redstoneMode;
            if (this.redstoneMode >= 7) {
                this.redstoneMode = 0;
            }

            IC2.platform.messagePlayer(player, this.getStringRedstoneMode());

        }
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
