package com.denfop.tiles.base;


import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.MachineRecipe;
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
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiAutoSpawner;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlotModules;
import com.denfop.invslot.InvSlotUpgradeModule;
import com.denfop.mixin.access.LootTableAccessor;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.Keyboard;
import com.denfop.utils.ModUtils;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileAutoSpawner extends TileElectricMachine
        implements IUpgradableBlock {

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
    public LivingEntity[] mobUtils = new LivingEntity[4];
    public String[] description_mobs = new String[4];
    public LootTable[] loot_Tables = new LootTable[4];
    public LootParams.Builder[] lootContext = new LootParams.Builder[4];

    public List<LootPool>[] lootPoolList = new List[4];
    public int fireAspect;

    public TileAutoSpawner(BlockPos pos, BlockState state) {
        super(150000, 14, 27, BlockBaseMachine3.spawner, pos, state);
        this.module_slot = new InvSlotModules(this);
        this.module_upgrade = new InvSlotUpgradeModule(this);
        this.progress = new int[module_slot.size()];
        this.maxEnergy2 = 50000 * 4;
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
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    @Override
    public int getContainerSize() {
        return 1;
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
        if (!this.level.isClientSide) {
            this.player = new FakePlayerSpawner(getWorld());
        }
        this.module_slot.update();
        this.module_upgrade.update();

    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiAutoSpawner((ContainerAutoSpawner) menu);
    }

    public ContainerAutoSpawner getGuiContainer(Player entityPlayer) {
        return new ContainerAutoSpawner(entityPlayer, this);
    }


    public void updateEntityServer() {

        super.updateEntityServer();


        if (this.level.getGameTime() % 20 == 0 && !this.outputSlot.isEmpty()) {
            ModUtils.tick(this.outputSlot, this);
        }

        boolean active = false;
        for (int i = 0; i < module_slot.size(); i++) {
            if (!this.module_slot.get(i).isEmpty()) {
                if (this.energy.getEnergy() >= this.costenergy || this.energy2 >= this.costenergy * 4) {
                    this.progress[i]++;
                    active = true;
                    if (this.energy.getEnergy() >= this.costenergy) {
                        this.energy.useEnergy(this.costenergy);
                    } else {
                        this.energy2 -= this.costenergy * 4;
                    }
                }
                this.tempprogress[i] = (int) (this.maxprogress[i] - (this.maxprogress[i] * this.speed / 100D));
                this.tempprogress[i] = Math.max(1, this.tempprogress[i]);
                if (this.progress[i] >= this.tempprogress[i]) {
                    this.progress[i] = 0;
                    if (this.mobUtils[i] == null) {
                        continue;
                    }

                    final LivingEntity entity = this.mobUtils[i];
                    dropItemFromEntity(entity, player.damageSources().playerAttack(player), this.loot_Tables[i], i);
                    int exp = Math.max(entity.getExperienceReward((ServerLevel) level, entity), 1);
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

    private void dropItemFromEntity(LivingEntity entity, DamageSource source, LootTable table, int index) {
        if (!(entity instanceof WitherBoss) && !(entity instanceof EnderDragon)) {
            if (!this.level.isClientSide) {
                int i = this.chance;
                LootParams.Builder lootcontext$builder = this.lootContext[index];
                if (table == null) {
                    return;
                }
                if (lootcontext$builder == null) {
                    lootcontext$builder = this.lootContext[index] = (new LootParams.Builder((ServerLevel) this.level))
                            .withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ATTACKING_ENTITY, this.player).withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                            .withParameter(LootContextParams.DAMAGE_SOURCE, source).withParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, entity).withLuck(i).withParameter(LootContextParams.ORIGIN, new Vec3(pos.getX(), pos.getY(), pos.getZ()));
                    List<LootPool> lootPools = ((LootTableAccessor) table).getPools();
                    this.lootPoolList[index] = lootPools;
                }
                final LootParams context = lootcontext$builder.create(LootContextParamSets.ENTITY);

                List<ItemStack> list = Lists.newArrayList();
                for (int j = 0; j < this.spawn; j++) {
                    table.getRandomItems(context, list::add);
                }
                for (ItemStack item : list) {


                    ItemStack smelt = ItemStack.EMPTY;
                    if (this.fireAspect > 0) {
                        MachineRecipe recipe = Recipes.recipes.getRecipeMachineMultiOutput(Recipes.recipes.getRecipe("furnace"), Recipes.recipes.getRecipeList("furnace"), false, Collections.singletonList(item));
                        if (recipe != null) {
                            smelt = recipe.getRecipe().output.items.get(0).copy();
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


    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putDouble("energy2", energy2);
        for (int i = 0; i < 4; i++) {
            nbttagcompound.putInt("progress" + i, progress[i]);
        }
        return nbttagcompound;
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        for (int i = 0; i < 4; i++) {
            progress[i] = nbttagcompound.getInt("progress" + i);
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
