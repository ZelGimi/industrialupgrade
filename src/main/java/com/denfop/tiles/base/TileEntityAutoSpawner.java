package com.denfop.tiles.base;


import cofh.redstoneflux.api.IEnergyHandler;
import cofh.redstoneflux.api.IEnergyReceiver;
import com.denfop.Config;
import com.denfop.Ic2Items;
import com.denfop.componets.EXPComponent;
import com.denfop.container.ContainerAutoSpawner;
import com.denfop.gui.GuiAutoSpawner;
import com.denfop.invslot.InvSlotBook;
import com.denfop.invslot.InvSlotModules;
import com.denfop.invslot.InvSlotUpgradeModule;
import ic2.api.energy.EnergyNet;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityAutoSpawner extends TileEntityElectricMachine
        implements IHasGui, INetworkTileEntityEventListener, IEnergyReceiver, IEnergyHandler, IUpgradableBlock {

    public final InvSlotModules module_slot;
    public final InvSlotBook book_slot;
    public final int maxprogress;
    public final InvSlotUpgradeModule module_upgrade;
    public final int tempcostenergy;
    public final int[] progress;
    public final double maxEnergy2;
    public final int defaultconsume;
    public final EXPComponent exp;
    public int costenergy;
    public int tempprogress;
    public FakePlayerSpawner player;
    public double energy2;
    public int speed;
    public int chance;
    public int spawn;
    public int experience;
    public EntityLiving[] mobUtils = new EntityLiving[4];

    public TileEntityAutoSpawner() {
        super(150000, 14, 27);
        this.module_slot = new InvSlotModules(this);
        this.book_slot = new InvSlotBook(this);
        this.module_upgrade = new InvSlotUpgradeModule(this);
        this.progress = new int[module_slot.size()];
        this.maxEnergy2 = 50000 * Config.coefficientrf;
        this.maxprogress = 100;
        this.tempprogress = 100;
        this.tempcostenergy = 900;
        this.costenergy = 900;
        this.speed = 0;
        this.chance = 0;
        this.spawn = 1;
        this.experience = 0;
        this.defaultconsume = this.costenergy;
        this.exp = this.addComponent(EXPComponent.asBasicSource(this, 15000, 14));

    }

    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {

        return receiveEnergy(maxReceive, simulate);

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

    @Override
    protected void onLoaded() {
        super.onLoaded();
        if (!this.world.isRemote) {
            this.player = new FakePlayerSpawner(getWorld());
        }
        this.module_upgrade.update();
        this.module_slot.update();
    }

    public boolean canConnectEnergy(EnumFacing arg0) {
        return true;
    }

    public int getEnergyStored(EnumFacing from) {
        return (int) this.energy2;
    }

    public int getMaxEnergyStored(EnumFacing from) {
        return (int) this.maxEnergy2;
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAutoSpawner(new ContainerAutoSpawner(entityPlayer, this));
    }

    public ContainerBase<? extends TileEntityAutoSpawner> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerAutoSpawner(entityPlayer, this);
    }


    public void updateEntityServer() {

        super.updateEntityServer();


        ItemStack stack3 = Ic2Items.ejectorUpgrade;
        if (this.world.provider.getWorldTime() % 6 == 0 && !this.outputSlot.isEmpty()) {
            ((IUpgradeItem) stack3.getItem()).onTick(stack3, this);
        }


        for (int i = 0; i < module_slot.size(); i++) {
            if (!this.module_slot.get(i).isEmpty()) {
                if (this.energy.getEnergy() >= this.costenergy || this.energy2 >= this.costenergy * Config.coefficientrf) {
                    this.progress[i]++;
                    if (this.energy.getEnergy() >= this.costenergy) {
                        this.energy.useEnergy(this.costenergy);
                    } else {
                        this.energy2 -= this.costenergy * Config.coefficientrf;
                    }
                }
                this.tempprogress = this.maxprogress - this.speed;

                if (this.progress[i] >= this.tempprogress) {
                    this.progress[i] = 0;
                    String name = this.module_slot.get(i).serializeNBT().getString("id");

                    if (Config.EntityList.contains(name)) {
                        return;
                    }

                    if (this.mobUtils[i] == null) {
                        continue;
                    }
                    final EntityLiving entity = this.mobUtils[i];

                    for (int j = 0; j < this.spawn; j++) {


                        dropItemFromEntity(entity, player, DamageSource.causePlayerDamage(player));
                        int exp = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(
                                entity,
                                this.player,
                                entity.experienceValue
                        );

                        this.exp.addEnergy((exp + this.experience * exp / 100D));


                    }


                }
            }
        }
    }

    private void dropItemFromEntity(EntityLiving entity, FakePlayerSpawner player, DamageSource source) {

        if (!this.world.isRemote) {
            int i = net.minecraftforge.common.ForgeHooks.getLootingLevel(entity, player, source);

            entity.captureDrops = true;
            entity.capturedDrops.clear();


            boolean flag = entity.recentlyHit > 0;
            entity.dropLoot(flag, i, source);


            entity.captureDrops = false;

            if (!net.minecraftforge.common.ForgeHooks.onLivingDrops(
                    entity,
                    source,
                    entity.capturedDrops,
                    i,
                    entity.recentlyHit > 0
            )) {
                for (EntityItem item : entity.capturedDrops) {

                    if (item.isDead) {
                        continue;
                    }
                    ItemStack drop = item.getItem();
                    ItemStack smelt = null;
                    if (this.player.fireAspect > 0) {

                        smelt = FurnaceRecipes.instance().getSmeltingResult(drop);
                        if (!smelt.isEmpty()) {
                            smelt.setCount(drop.getCount());
                        }
                    }
                    if (smelt == null || smelt.isEmpty()) {
                        if (this.outputSlot.canAdd(drop)) {
                            this.outputSlot.add(drop);
                        }
                    } else {
                        if (this.outputSlot.canAdd(smelt)) {
                            this.outputSlot.add(smelt);
                        }
                    }
                    item.setDead();

                }
            }
        }
    }


    @Override
    public void onNetworkEvent(int i) {

    }

    public int getEnchant(int enchantID) {
        for (int i = 0; i < this.book_slot.size(); i++) {
            if (this.book_slot.get(i) == null) {
                continue;
            }
            ItemStack stack = this.book_slot.get(i);
            if (stack.getItem() instanceof ItemEnchantedBook) {
                NBTTagList bookNBT = ItemEnchantedBook.getEnchantments(this.book_slot.get(i));
                if (bookNBT.tagCount() == 1) {
                    short id = bookNBT.getCompoundTagAt(0).getShort("id");
                    if (id == enchantID) {
                        return bookNBT.getCompoundTagAt(0).getShort("lvl");
                    }
                }
            }
        }
        return 0;
    }


    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {

    }

    @Override
    public String getInventoryName() {
        return null;
    }


    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("energy2", energy2);
        for (int i = 0; i < 4; i++) {
            nbttagcompound.setInteger("progress" + i, progress[i]);
        }
        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        for (int i = 0; i < 4; i++) {
            progress[i] = nbttagcompound.getInteger("progress" + i);
        }

    }


    @Override
    public double getEnergy() {
        return 0;
    }

    @Override
    public boolean useEnergy(final double v) {
        return false;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.ItemProducing
        );
    }

}
