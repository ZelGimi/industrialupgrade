package com.denfop.tiles.base;


import cofh.redstoneflux.api.IEnergyHandler;
import cofh.redstoneflux.api.IEnergyReceiver;
import com.denfop.Config;
import com.denfop.componets.EXPComponent;
import com.denfop.container.ContainerAutoSpawner;
import com.denfop.gui.GuiAutoSpawner;
import com.denfop.invslot.InvSlotModules;
import com.denfop.invslot.InvSlotUpgradeModule;
import com.denfop.utils.ModUtils;
import ic2.api.energy.EnergyNet;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityAutoSpawner extends TileEntityElectricMachine
        implements IEnergyReceiver, IEnergyHandler, IUpgradableBlock {

    public final InvSlotModules module_slot;
    public final double[] maxprogress;
    public final InvSlotUpgradeModule module_upgrade;
    public final int tempcostenergy;
    public final int[] progress;
    public final double maxEnergy2;
    public final int defaultconsume;
    public final EXPComponent exp;
    private final double defaultmaxprogress;
    public int costenergy;
    public int tempprogress;
    public FakePlayerSpawner player;
    public double energy2;
    public int speed;
    public int chance;
    public int spawn;
    public int experience;
    public EntityLiving[] mobUtils = new EntityLiving[4];
    public String[] description_mobs = new String[4];
    public LootTable[] loot_Tables = new LootTable[4];
    public LootContext.Builder[] lootContext = new LootContext.Builder[4];
    public int fireAspect;

    public TileEntityAutoSpawner() {
        super(150000, 14, 27);
        this.module_slot = new InvSlotModules(this);
        this.module_upgrade = new InvSlotUpgradeModule(this);
        this.progress = new int[module_slot.size()];
        this.maxEnergy2 = 50000 * Config.coefficientrf;
        this.defaultmaxprogress = 100;
        this.maxprogress = new double[4];
        this.tempprogress = 100;
        this.tempcostenergy = 1500;
        this.costenergy = 1500;
        this.speed = 0;
        this.chance = 0;
        this.spawn = 1;
        this.experience = 0;
        this.defaultconsume = this.costenergy;
        this.exp = this.addComponent(EXPComponent.asBasicSource(this, 15000, 14));

    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {

        return receiveEnergy(maxReceive, simulate);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.defaultconsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip, advanced);

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
        this.module_slot.update();
        this.module_upgrade.update();

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


        if (this.world.provider.getWorldTime() % 20 == 0 && !this.outputSlot.isEmpty()) {
            ModUtils.tick(this.outputSlot, this);
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
                this.tempprogress = (int) (this.maxprogress[i] - this.speed);

                if (this.progress[i] >= this.tempprogress) {
                    this.progress[i] = 0;
                    if (this.mobUtils[i] == null) {
                        continue;
                    }

                    final EntityLiving entity = this.mobUtils[i];
                    entity.setWorld(this.getWorld());
                    for (int j = 0; j < this.spawn; j++) {


                        dropItemFromEntity(entity, DamageSource.causePlayerDamage(player), this.loot_Tables[i], i);
                        int exp = Math.max(entity.getExperiencePoints(this.player), 1);
                        this.exp.addEnergy((exp + this.experience * exp / 100D));


                    }


                }
            }
        }
    }

    private void dropItemFromEntity(EntityLiving entity, DamageSource source, LootTable table, int index) {
        if (entity.isNonBoss()) {
            if (!this.world.isRemote) {
                int i = this.chance;
                LootContext.Builder lootcontext$builder = this.lootContext[index];
                if (table == null) {
                    return;
                }
                if (lootcontext$builder == null) {
                    lootcontext$builder = this.lootContext[index] = (new LootContext.Builder((WorldServer) this.world))
                            .withLootedEntity(entity)
                            .withDamageSource(source).withLuck(i);
                }
                List<ItemStack> list = table.generateLootForPools(
                        this.getWorld().rand,
                        lootcontext$builder.build()
                );

                if (entity instanceof EntityBlaze) {
                    list.add(new ItemStack(Items.BLAZE_ROD, this.getWorld().rand.nextInt(i + 1) + 1));
                } else if (entity instanceof EntitySlime) {
                    if (((EntitySlime) entity).isSmallSlime()) {
                        list.add(new ItemStack(Items.SLIME_BALL, this.getWorld().rand.nextInt(i + 1) + 1));
                    }
                } else if (entity instanceof EntityWitherSkeleton) {
                    if (this.world.rand.nextInt(101) >= 100 - (chance + 2)) {
                        list.add(new ItemStack(Items.SKULL, 1, 1));
                    }
                }
                for (ItemStack item : list) {


                    ItemStack smelt = ItemStack.EMPTY;
                    if (this.fireAspect > 0) {

                        smelt = FurnaceRecipes.instance().getSmeltingResult(item);
                        if (!smelt.isEmpty()) {
                            smelt.setCount(item.getCount());
                        }
                    }
                    if (smelt.isEmpty()) {
                        this.outputSlot.add(item);


                    } else {
                        this.outputSlot.add(smelt);

                    }

                }

            }
        }
    }


    @Override
    public void onNetworkEvent(int i) {

    }


    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {

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
