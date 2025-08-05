package com.denfop.tiles.base;

import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IStorage;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IEnergySource;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.componets.Energy;
import com.denfop.componets.Redstone;
import com.denfop.componets.WirelessComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerElectricBlock;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiElectricBlock;
import com.denfop.invslot.InvSlotCharge;
import com.denfop.invslot.InvSlotDischarge;
import com.denfop.invslot.InvSlotElectricBlock;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.proxy.CommonProxy;
import com.denfop.tiles.wiring.EnumElectricBlock;
import com.denfop.utils.ModUtils;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    public final InvSlotCharge inputslotA;
    public final InvSlotDischarge inputslotB;
    public final InvSlotElectricBlock inputslotC;
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
    private Player player;


    public TileElectricBlock(double tier1, double output1, double maxStorage1, boolean chargepad, String name, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.tier = tier1;
        this.output = EnergyNetGlobal.instance.getPowerFromTier((int) tier);
        this.maxStorage2 = maxStorage1 * 4;
        this.chargepad = chargepad;
        this.name = name;
        this.inputslotA = new InvSlotCharge(this, (int) tier);

        this.inputslotB = new InvSlotDischarge(this, (int) tier);
        this.inputslotC = new InvSlotElectricBlock(this, 3, 2);
        this.output_plus = 0;
        this.temp = 0;
        this.l = output1;
        this.player = null;
        this.energy = this.addComponent((new Energy(
                this,
                maxStorage1,
                Arrays.stream(Direction.values()).filter(f -> f != this.getFacing()).collect(Collectors.toList()),
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

    public TileElectricBlock(EnumElectricBlock electricBlock, IMultiTileBlock block, BlockPos pos, BlockState state) {
        this(electricBlock.tier, electricBlock.producing, electricBlock.maxstorage, electricBlock.chargepad, electricBlock.name1, block, pos, state);
        electricblock = electricBlock;
    }

    public EnumElectricBlock getElectricBlock() {

        return electricblock;
    }

    @Override
    public boolean onSneakingActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (this.level.isClientSide) {
            return false;
        }
        module_charge(player);
        return true;
    }

    public ItemStack getItem(Player player, HitResult target) {


        return super.getPickBlock(player, target);
    }

    @Override
    public ItemStack getPickBlock(final Player player, final HitResult target) {
        double retainedRatio = 0.8;
        double totalEnergy = this.energy.getEnergy();
        final ItemStack stack = super.getPickBlock(player, target);
        if (totalEnergy > 0.0D) {
            CompoundTag nbt = ModUtils.nbt(stack);
            nbt.putDouble("energy", Math.round(totalEnergy * retainedRatio));

        }
        return super.getPickBlock(player, target);
    }

    public EnumTypeAudio getTypeAudio() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    public void initiate(int soundEvent) {
        if (this.getTypeAudio() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }

        setType(valuesAudio[soundEvent % valuesAudio.length]);

        if (getSound() == null) {
            return;
        }
        if (soundEvent == 0) {
            this.getWorld().playSound(null, this.pos, getSound(), SoundSource.BLOCKS, 1F, 1);
        } else if (soundEvent == 1) {
            new PacketStopSound(getWorld(), this.pos);
            this.getWorld().playSound(null, this.pos, EnumSound.InterruptOne.getSoundEvent(), SoundSource.BLOCKS, 1F, 1);
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
        CompoundTag nbttagcompound = ModUtils.nbt(itemStack);
        if (this.energy == null || this.energy.getEnergy() == 0) {
            info.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(nbttagcompound.getDouble("energy"))
                    + " EF ");
        } else {
            info.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(this.energy.getEnergy())
                    + " EF ");
        }
        info.add(Localization.translate("iu.tier") + ModUtils.getString(this.tier));


    }

    public ContainerElectricBlock getGuiContainer(Player player) {
        return new ContainerElectricBlock(player, this);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiElectricBlock((ContainerElectricBlock) isAdmin);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!addedToEnergyNet) {
            this.addedToEnergyNet = true;
            if (!this.chargepad) {
                this.energy.setDirections(

                        Arrays.stream(Direction.values())
                                .filter(facing -> facing != this.getFacing())
                                .collect(Collectors.toList()), Collections.singletonList(this.getFacing()));

            } else {
                this.energy.setDirections(

                        Arrays.stream(Direction.values())
                                .filter(facing1 -> facing1 != Direction.UP && facing1 != getFacing())
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

    protected void getItems(Player player) {
        if (!this.canEntityDestroy(player)) {
            IUCore.proxy.messagePlayer(player, Localization.translate("iu.error"));
            return;
        }


        for (ItemStack current : player.getInventory().armor) {
            if (current != null) {
                chargeitems(current, this.output);
            }
        }
        for (ItemStack current : player.getInventory().items) {
            if (current != null) {
                chargeitems(current, this.output);
            }
        }
        player.containerMenu.broadcastChanges();

    }

    @OnlyIn(Dist.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.getActive()) {
            Level world = this.getLevel();
            RandomSource rnd = world.getRandom();
            final int n = 4;
            final float green = 0.0f;
            final float blue = 1.0f;
            final float red = 0;
            final float scale = 1.0f;

            for (int i = 0; i < n; ++i) {
                world.addParticle(
                        new DustParticleOptions(new Vector3f(red, green, blue), scale),
                        pos.getX() + rnd.nextFloat(),
                        pos.getY() + 1 + rnd.nextFloat(),
                        pos.getZ() + rnd.nextFloat(),
                        0.0, 0.0, 0.0
                );

                world.addParticle(
                        new DustParticleOptions(new Vector3f(red, green, blue), scale),
                        pos.getX() + rnd.nextFloat(),
                        pos.getY() + 2 + rnd.nextFloat(),
                        pos.getZ() + rnd.nextFloat(),
                        0.0, 0.0, 0.0
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


    public void module_charge(Player entityPlayer) {

        if (this.movementcharge) {

            for (ItemStack armorcharged : entityPlayer.getInventory().armor) {
                if (armorcharged != null) {
                    if (armorcharged.getItem() instanceof IEnergyItem && this.energy.getEnergy() > 0) {
                        double sent = ElectricItem.manager.charge(armorcharged, this.energy.getEnergy(), 2147483647, true,
                                false
                        );
                        entityPlayer.containerMenu.broadcastChanges();
                        this.energy.useEnergy(sent);

                        this.needsInvUpdate = (sent > 0.0D);
                        if (sent > 0) {
                            CommonProxy.sendPlayerMessage(
                                    entityPlayer,
                                    Localization.translate("successfully.charged")
                                            + armorcharged.getDisplayName().getString()
                                            + Localization.translate("iu.sendenergy")
                                            + ModUtils.getString(sent) + " EF"
                            );
                            entityPlayer.containerMenu.broadcastChanges();
                        }


                    }

                }

            }

        }

        if (this.movementchargeitem) {
            for (ItemStack charged : entityPlayer.getInventory().items) {
                if (charged != null) {
                    if (charged.getItem() instanceof IEnergyItem && this.energy.getEnergy() > 0) {
                        double sent = ElectricItem.manager.charge(charged, this.energy.getEnergy(), 2147483647, true, false);

                        this.energy.useEnergy(sent);
                        this.needsInvUpdate = (sent > 0.0D);
                        if (sent > 0) {
                            CommonProxy.sendPlayerMessage(
                                    entityPlayer,
                                    Localization.translate("successfully.charged")
                                            + charged.getDisplayName().getString()
                                            + Localization.translate("iu.sendenergy")
                                            + ModUtils.getString(sent) + " EF"
                            );
                            entityPlayer.containerMenu.broadcastChanges();
                        }


                    }

                }

            }

        }
    }

    public List<AABB> getAabbs(boolean forCollision) {
        if (chargepad) {
            return Collections.singletonList(new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D));
        } else {
            return super.getAabbs(forCollision);
        }
    }

    protected void updatePlayer(Player entity) {
        this.player = entity;

    }

    public void onEntityCollision(Entity entity) {
        super.onEntityCollision(entity);
        if (!this.getWorld().isClientSide && entity instanceof Player) {
            if (this.chargepad && this.canEntityDestroy(entity)) {
                this.updatePlayer((Player) entity);
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
        this.energy.setReceivingEnabled(true);

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


    public void onPlaced(ItemStack stack, LivingEntity placer, Direction facing) {
        super.onPlaced(stack, placer, facing);
        if (!(getWorld()).isClientSide) {
            CompoundTag nbt = ModUtils.nbt(stack);
            this.energy.addEnergy(nbt.getDouble("energy"));
        }
    }

    public List<ItemStack> getWrenchDrops(Player player, int fortune) {
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
        if (drop.is(this.getPickBlock(
                null,
                null
        ).getItem()) && (wrench || this.teBlock.getDefaultDrop() == DefaultDrop.Self)) {
            double retainedRatio = 0.8;
            if (fortune == 100) {
                retainedRatio = 1;
            }
            double totalEnergy = this.energy.getEnergy();
            if (totalEnergy > 0.0D) {
                CompoundTag nbt = ModUtils.nbt(drop);
                nbt.putDouble("energy", Math.round(totalEnergy * retainedRatio));

            }
        }
        return drop;
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (wrench || this.teBlock.getDefaultDrop() == DefaultDrop.Self) {
            double retainedRatio = 0.8;
            double totalEnergy = this.energy.getEnergy();
            if (totalEnergy > 0.0D) {
                CompoundTag nbt = ModUtils.nbt(drop);
                nbt.putDouble("energy", Math.round(totalEnergy * retainedRatio));

            }
        }
        return drop;
    }


    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.rfeu = nbttagcompound.getBoolean("rfeu");
        this.redstoneMode = nbttagcompound.getByte("redstoneMode");
    }

    public void setFacing(Direction facing) {
        super.setFacing(facing);
        if (!this.chargepad) {
            this.energy.setDirections(

                    Arrays
                            .asList(Direction.values())
                            .stream()
                            .filter(facing1 -> facing1 != this.getFacing())
                            .collect(Collectors.toList()), Collections.singletonList(this.getFacing()));
        } else {
            this.energy.setDirections(

                    Arrays
                            .asList(Direction.values())
                            .stream()
                            .filter(facing1 -> facing1 != Direction.UP && facing1 != getFacing())
                            .collect(Collectors.toList()), Collections.singletonList(this.getFacing()));


        }
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putBoolean("rfeu", this.rfeu);
        nbttagcompound.putByte("redstoneMode", this.redstoneMode);
        return nbttagcompound;
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

    public void updateTileServer(Player player, double event) {

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
