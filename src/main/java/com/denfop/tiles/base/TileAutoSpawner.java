package com.denfop.tiles.base;


import cofh.redstoneflux.api.IEnergyHandler;
import cofh.redstoneflux.api.IEnergyReceiver;
import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerAutoSpawner;
import com.denfop.gui.GuiAutoSpawner;
import com.denfop.invslot.InvSlotModules;
import com.denfop.invslot.InvSlotUpgradeModule;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.ModUtils;
import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileAutoSpawner extends TileElectricMachine
        implements IEnergyReceiver, IEnergyHandler, IUpgradableBlock {

    public final InvSlotModules module_slot;
    public final double[] maxprogress;
    public final InvSlotUpgradeModule module_upgrade;
    public final int tempcostenergy;
    public final double maxEnergy2;
    public final int defaultconsume;
    public final ComponentBaseEnergy exp;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public int[] progress;
    public int costenergy;
    public int[] tempprogress;
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

    public List<LootPool>[] lootPoolList = new List[4];
    public int fireAspect;

    public TileAutoSpawner() {
        super(150000, 14, 27);
        this.module_slot = new InvSlotModules(this);
        this.module_upgrade = new InvSlotUpgradeModule(this);
        this.progress = new int[module_slot.size()];
        this.maxEnergy2 = 50000 * Config.coefficientrf;
        this.maxprogress = new double[4];
        this.tempprogress = new int[]{100, 100, 100, 100};
        this.tempcostenergy = 1500;
        this.costenergy = 1500;
        this.speed = 0;
        this.chance = 0;
        this.spawn = 1;
        this.experience = 0;
        this.defaultconsume = this.costenergy;
        this.exp = this.addComponent(ComponentBaseEnergy.asBasicSource(EnergyType.EXPERIENCE, this, 15000, 14));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.spawner;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {

        return receiveEnergy(maxReceive, simulate);

    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.defaultconsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip);

    }

    public int receiveEnergy(int paramInt, boolean paramBoolean) {
        int i = (int) Math.min(
                this.maxEnergy2 - this.energy2,
                Math.min(EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSinkTier()) * 4, paramInt)
        );
        if (!paramBoolean) {
            this.energy2 += i;
        }
        return i;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            progress = (int[]) DecoderHandler.decode(customPacketBuffer);
            energy2 = (double) DecoderHandler.decode(customPacketBuffer);
            tempprogress = (int[]) DecoderHandler.decode(customPacketBuffer);
            description_mobs = (String[]) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, progress);
            EncoderHandler.encode(packet, energy2);
            EncoderHandler.encode(packet, tempprogress);
            EncoderHandler.encode(packet, description_mobs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void onLoaded() {
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
    public GuiAutoSpawner getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAutoSpawner(new ContainerAutoSpawner(entityPlayer, this));
    }

    public ContainerAutoSpawner getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerAutoSpawner(entityPlayer, this);
    }


    public void updateEntityServer() {

        super.updateEntityServer();


        if (this.world.provider.getWorldTime() % 20 == 0 && !this.outputSlot.isEmpty()) {
            ModUtils.tick(this.outputSlot, this);
        }

        boolean active = false;
        for (int i = 0; i < module_slot.size(); i++) {
            if (!this.module_slot.get(i).isEmpty()) {
                if (this.energy.getEnergy() >= this.costenergy || this.energy2 >= this.costenergy * Config.coefficientrf) {
                    this.progress[i]++;
                    active = true;
                    if (this.energy.getEnergy() >= this.costenergy) {
                        this.energy.useEnergy(this.costenergy);
                    } else {
                        this.energy2 -= this.costenergy * Config.coefficientrf;
                    }
                }
                this.tempprogress[i] = (int) (this.maxprogress[i] - this.speed);
                this.tempprogress[i] = Math.max(1, this.tempprogress[i]);
                if (this.progress[i] >= this.tempprogress[i]) {
                    this.progress[i] = 0;
                    if (this.mobUtils[i] == null) {
                        continue;
                    }

                    final EntityLiving entity = this.mobUtils[i];
                    entity.setWorld(this.getWorld());

                    dropItemFromEntity(entity, DamageSource.causePlayerDamage(player), this.loot_Tables[i], i);
                    int exp = Math.max(entity.getExperiencePoints(this.player), 1);
                    this.exp.addEnergy((exp + this.experience * exp / 100D));


                }
            }
        }
        if (this.getActive() && !active) {
            this.setActive(active);
        } else if (!this.getActive() && active) {
            this.setActive(active);
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
                            .withLootedEntity(entity).withPlayer(this.player)
                            .withDamageSource(source).withLuck(i);
                    List<LootPool> lootPools = new ArrayList<>();
                    final LootPool mainPool = table.getPool("main");
                    if (mainPool != null) {
                        lootPools.add(mainPool);
                        int in = 1;
                        LootPool pool = table.getPool("pool" + in);
                        while (pool != null) {
                            lootPools.add(pool);
                            in++;
                            pool = table.getPool("pool" + in);
                        }
                    }
                    this.lootPoolList[index] = lootPools;
                }
                final LootContext context = lootcontext$builder.build();

                List<ItemStack> list = Lists.newArrayList();
                for (int j = 0; j < this.spawn; j++) {
                    for (LootPool lootpool : this.lootPoolList[index]) {
                        lootpool.generateLoot(list, this.getWorld().rand, context);
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
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.ItemExtract
        );
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

}
