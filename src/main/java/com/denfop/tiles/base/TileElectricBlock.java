package com.denfop.tiles.base;

import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IStorage;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IEnergySource;
import com.denfop.api.item.IEnergyItem;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.componets.Energy;
import com.denfop.componets.Redstone;
import com.denfop.componets.WirelessComponent;
import com.denfop.container.ContainerElectricBlock;
import com.denfop.gui.GuiElectricBlock;
import com.denfop.invslot.InventoryCharge;
import com.denfop.invslot.InventoryDischarge;
import com.denfop.invslot.InventoryElectricBlock;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.proxy.CommonProxy;
import com.denfop.tiles.wiring.EnumElectricBlock;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TileElectricBlock extends TileEntityInventory implements
        IUpdatableTileEvent,
        IStorage {

    public final WirelessComponent wirelessComponent;
    public final double tier;
    public final boolean chargepad;
    public final String name;
    public final Energy energy;
    public final double maxStorage2;
    public final double l;
    public final InventoryCharge inputslotA;
    public final InventoryDischarge inputslotB;
    public final InventoryElectricBlock inputslotC;
    private final Redstone redstone;
    public EnumElectricBlock electricblock;
    public double output;
    public boolean rfeu = false;
    public boolean needsInvUpdate = false;
    public boolean movementcharge = false;
    public double output_plus;
    public short temp;
    public boolean load = false;
    public boolean movementchargeitem = false;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    public boolean addedToEnergyNet = false;
    private byte redstoneMode = 0;
    private EntityPlayer player;


    public TileElectricBlock(double tier1, double output1, double maxStorage1, boolean chargepad, String name) {

        this.tier = tier1;
        this.output = EnergyNetGlobal.instance.getPowerFromTier((int) tier);
        this.maxStorage2 = maxStorage1 * 4;
        this.chargepad = chargepad;
        this.name = name;
        this.inputslotA = new InventoryCharge(this, (int) tier);

        this.inputslotB = new InventoryDischarge(this, (int) tier);
        this.inputslotC = new InventoryElectricBlock(this, 3, 2);
        this.output_plus = 0;
        this.temp = 0;
        this.l = output1;
        this.player = null;
        this.energy = this.addComponent((new Energy(
                this,
                maxStorage1,
                Arrays.stream(EnumFacing.VALUES).filter(f -> f != this.getFacing()).collect(Collectors.toList()),
                Collections.singletonList(this.getFacing()),
                EnergyNetGlobal.instance.getTierFromPower(this.output),
                EnergyNetGlobal.instance.getTierFromPower(this.output),
                false
        )));
        this.energy.addManagedSlot(this.inputslotA);
        this.energy.addManagedSlot(this.inputslotB);
        this.redstone = this.addComponent(new Redstone(this));
        this.wirelessComponent = this.addComponent(new WirelessComponent(this));

    }

    public TileElectricBlock(EnumElectricBlock electricBlock) {
        this(electricBlock.tier, electricBlock.producing, electricBlock.maxstorage, electricBlock.chargepad, electricBlock.name1);
        electricblock = electricBlock;
    }

    public EnumElectricBlock getElectricBlock() {

        return electricblock;
    }

    @Override
    public boolean onSneakingActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (this.world.isRemote) {
            return false;
        }
        module_charge(player);
        return true;
    }

    public ItemStack getItem(EntityPlayer player, RayTraceResult target) {


        return super.getPickBlock(player, target);
    }

    @Override
    public ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        double retainedRatio = 0.8;
        double totalEnergy = this.energy.getEnergy();
        final ItemStack stack = super.getPickBlock(player, target);
        if (totalEnergy > 0.0D) {
            NBTTagCompound nbt = ModUtils.nbt(stack);
            nbt.setDouble("energy", Math.round(totalEnergy * retainedRatio));

        }
        return super.getPickBlock(player, target);
    }

    public EnumTypeAudio getType() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    public void initiate(int soundEvent) {
        if (this.getType() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }

        setType(valuesAudio[soundEvent % valuesAudio.length]);

        if (getSound() == null) {
            return;
        }
        if (soundEvent == 0) {
            this.getWorld().playSound(null, this.pos, getSound(), SoundCategory.BLOCKS, 1F, 1);
        } else if (soundEvent == 1) {
            new PacketStopSound(getWorld(), this.pos);
            this.getWorld().playSound(null, this.pos, EnumSound.InterruptOne.getSoundEvent(), SoundCategory.BLOCKS, 1F, 1);
        } else {
            new PacketStopSound(getWorld(), this.pos);
        }
    }

    public SoundEvent getSound() {
        return EnumSound.pen.getSoundEvent();
    }

    public List<ItemStack> getDrop() {
        return getAuxDrops(0);
    }

    @Override
    public void addInformation(final ItemStack itemStack, final List<String> info) {


        info.add(Localization.translate("iu.item.tooltip.Output") + " " + ModUtils.getString(EnergyNetGlobal.instance.getPowerFromTier(
                this.energy.getSourceTier())) + " EF/t ");
        info.add(Localization.translate("iu.item.tooltip.Capacity") + " " + ModUtils.getString(this.energy.getCapacity()) + " EF ");
        NBTTagCompound nbttagcompound = ModUtils.nbt(itemStack);
        if (this.energy == null || this.energy.getEnergy() == 0) {
            info.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(nbttagcompound.getDouble("energy"))
                    + " EF ");
        } else {
            info.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(this.energy.getEnergy())
                    + " EF ");
        }
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


        return super.onActivated(player, hand, side, hitX, hitY, hitZ);

    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!addedToEnergyNet) {
            this.addedToEnergyNet = true;
            if (!this.chargepad) {
                this.energy.setDirections(

                        Arrays.stream(EnumFacing.VALUES)
                                .filter(facing -> facing != this.getFacing())
                                .collect(Collectors.toList()), Collections.singletonList(this.getFacing()));

            } else {
                this.energy.setDirections(

                        Arrays.stream(EnumFacing.VALUES)
                                .filter(facing1 -> facing1 != EnumFacing.UP && facing1 != getFacing())
                                .collect(Collectors.toList()), Collections.singletonList(this.getFacing()));


            }
        }
        wirelessComponent.setEnergySource((IEnergySource) this.energy.getDelegate());
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
            IUCore.proxy.messagePlayer(player, Localization.translate("iu.error"));
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
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.getActive()) {
            World world = this.getWorld();
            Random rnd = world.rand;
            final int n = 4;
            final int green = 0;
            int blue = 1;
            for (int i = 0; i < n; ++i) {
                world.spawnParticle(
                        EnumParticleTypes.REDSTONE,
                        (float) pos.getX() + rnd.nextFloat(),
                        (float) (pos.getY() + 1) + rnd.nextFloat(),
                        (float) pos.getZ() + rnd.nextFloat(),
                        -1,
                        green,
                        blue
                );
                world.spawnParticle(
                        EnumParticleTypes.REDSTONE,
                        (float) pos.getX() + rnd.nextFloat(),
                        (float) (pos.getY() + 2) + rnd.nextFloat(),
                        (float) pos.getZ() + rnd.nextFloat(),
                        -1,
                        green,
                        blue
                );
            }
        }

    }

    protected void chargeitems(ItemStack itemstack, double chargefactor) {
        if (!(itemstack.getItem() instanceof IEnergyItem)) {
            return;
        }
        double freeamount = ElectricItem.manager.charge(itemstack, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true, true);
        double charge;
        if (freeamount > 0.0D) {
            charge = Math.min(freeamount, chargefactor);
            charge = Math.min(charge, ((IEnergyItem) itemstack.getItem()).getTransferEnergy(itemstack));
            if (this.energy.getEnergy() < charge) {
                charge = this.energy.getEnergy();
            }
            this.energy.useEnergy(ElectricItem.manager.charge(itemstack, charge, (int) this.tier, true, false));
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
                    if (armorcharged.getItem() instanceof IEnergyItem && this.energy.getEnergy() > 0) {
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
                                            + ModUtils.getString(sent) + " EF"
                            );
                            entityPlayer.inventoryContainer.detectAndSendChanges();
                        }


                    }

                }

            }

        }

        if (this.movementchargeitem) {
            for (ItemStack charged : entityPlayer.inventory.mainInventory) {
                if (charged != null) {
                    if (charged.getItem() instanceof IEnergyItem && this.energy.getEnergy() > 0) {
                        double sent = ElectricItem.manager.charge(charged, this.energy.getEnergy(), 2147483647, true, false);

                        this.energy.useEnergy(sent);
                        this.needsInvUpdate = (sent > 0.0D);
                        if (sent > 0) {
                            CommonProxy.sendPlayerMessage(
                                    entityPlayer,
                                    Localization.translate("successfully.charged")
                                            + charged.getDisplayName()
                                            + Localization.translate("iu.sendenergy")
                                            + ModUtils.getString(sent) + " EF"
                            );
                            entityPlayer.inventoryContainer.detectAndSendChanges();
                        }


                    }

                }

            }

        }
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        if (chargepad) {
            return Collections.singletonList(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D));
        } else {
            return super.getAabbs(forCollision);
        }
    }

    protected void updatePlayer(EntityPlayer entity) {
        this.player = entity;

    }

    public void onEntityCollision(Entity entity) {
        super.onEntityCollision(entity);
        if (!this.getWorld().isRemote && entity instanceof EntityPlayer) {
            if (this.chargepad && this.canEntityDestroy(entity)) {
                this.updatePlayer((EntityPlayer) entity);
            }

        }

    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (!load) {

            this.inputslotC.wirelessmodule();
            this.load = true;
        }
        this.needsInvUpdate = false;
        this.energy.setSendingEnabled(this.shouldEmitEnergy());
        this.energy.receivingDisabled = false;

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
            NBTTagCompound nbt = ModUtils.nbt(stack);
            this.energy.addEnergy(nbt.getDouble("energy"));
        }
    }

    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = new ArrayList<>();
        ret.addAll(this.getSelfDrops(fortune, true));
        ret.addAll(this.getAuxDrops(fortune));
        return ret;
    }

    public List<ItemStack> getSelfDrops(int fortune, boolean wrench) {
        ItemStack drop = this.getPickBlock(null, null);
        drop = this.adjustDrop(drop, wrench, fortune);
        return drop == null ? Collections.emptyList() : Collections.singletonList(drop);
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench, int fortune) {
        drop = super.adjustDrop(drop, wrench, fortune);
        if (drop.isItemEqual(this.getPickBlock(
                null,
                null
        )) && (wrench || this.teBlock.getDefaultDrop() == MultiTileBlock.DefaultDrop.Self)) {
            double retainedRatio = 0.8;
            if (fortune == 100) {
                retainedRatio = 1;
            }
            double totalEnergy = this.energy.getEnergy();
            if (totalEnergy > 0.0D) {
                NBTTagCompound nbt = ModUtils.nbt(drop);
                nbt.setDouble("energy", Math.round(totalEnergy * retainedRatio));

            }
        }
        return drop;
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (wrench || this.teBlock.getDefaultDrop() == MultiTileBlock.DefaultDrop.Self) {
            double retainedRatio = 0.8;
            double totalEnergy = this.energy.getEnergy();
            if (totalEnergy > 0.0D) {
                NBTTagCompound nbt = ModUtils.nbt(drop);
                nbt.setDouble("energy", Math.round(totalEnergy * retainedRatio));

            }
        }
        return drop;
    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.rfeu = nbttagcompound.getBoolean("rfeu");
        this.redstoneMode = nbttagcompound.getByte("redstoneMode");
    }

    public void setFacing(EnumFacing facing) {
        super.setFacing(facing);
        if (!this.chargepad) {
            this.energy.setDirections(

                    Arrays
                            .asList(EnumFacing.VALUES)
                            .stream()
                            .filter(facing1 -> facing1 != this.getFacing())
                            .collect(Collectors.toList()), Collections.singletonList(this.getFacing()));
        } else {
            this.energy.setDirections(

                    Arrays
                            .asList(EnumFacing.VALUES)
                            .stream()
                            .filter(facing1 -> facing1 != EnumFacing.UP && facing1 != getFacing())
                            .collect(Collectors.toList()), Collections.singletonList(this.getFacing()));


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

    public void updateTileServer(EntityPlayer player, double event) {

        ++this.redstoneMode;
        if (this.redstoneMode >= 7) {
            this.redstoneMode = 0;
        }

        IUCore.proxy.messagePlayer(player, this.getStringRedstoneMode());


    }

    public byte getRedstoneMode() {
        return redstoneMode;
    }

    public String getStringRedstoneMode() {
        return this.redstoneMode < 7 && this.redstoneMode >= 0 ?
                Localization.translate("iu.EUStorage.gui.mod.redstone" + this.redstoneMode) : "";
    }


    public String getInventoryName() {
        return Localization.translate(this.name);
    }


}
