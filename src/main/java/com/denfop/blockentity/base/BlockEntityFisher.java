package com.denfop.blockentity.base;


import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.item.energy.EnergyItem;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine2Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuFisher;
import com.denfop.inventory.InventoryFisher;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenFisher;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.sound.EnumSound;
import com.denfop.utils.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityFisher extends BlockEntityElectricMachine
        implements IUpgradableBlock, IManufacturerBlock {


    public final int energyconsume;
    public final InventoryFisher inputslot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public int progress;
    private boolean checkwater;
    private int level = 1;
    private FakePlayerSpawner player;
    private FishingHook energyFishHook;
    private LootParams.Builder lootcontext$builder;
    private LootTable table;
    private LootPool listPool;

    public BlockEntityFisher(BlockPos pos, BlockState state) {
        super(1E4, 14, 9, BlockBaseMachine2Entity.fisher, pos, state);
        this.progress = 0;
        this.energyconsume = 100;
        this.checkwater = false;
        this.inputslot = new InventoryFisher(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            progress = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, progress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine2Entity.fisher;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1.getBlock(this.getTeBlock().getId());
    }

    @Override
    public int getLevelMechanism() {
        return this.level;
    }

    @Override
    public void setLevelMech(final int levelMech) {
        this.level = levelMech;
    }

    @Override
    public void removeLevel(final int level) {
        this.level -= level;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.energyconsume + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip);

    }

    private boolean checkwater() {
        int x1 = this.pos.getX();
        int y1 = this.pos.getY() - 2;
        int z1 = this.pos.getZ();
        for (int i = x1 - 1; i <= x1 + 1; i++) {
            for (int j = z1 - 1; j <= z1 + 1; j++) {
                for (int k = y1 - 1; k <= y1 + 1; k++) {
                    if (this.getWorld().getBlockState(new BlockPos(i, k, j)).getBlock() != Blocks.WATER) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.getWorld().isClientSide) {
            return;
        }
        checkwater = checkwater();
        this.player = new FakePlayerSpawner(getWorld());
        player.setPos(Vec3.atLowerCornerOf(this.getPos().below()));
        this.energyFishHook = new FishingHook(player, getWorld(), 0, 0);
        this.lootcontext$builder = new LootParams.Builder((ServerLevel) this.getWorld());
        this.table = this.getWorld().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.FISHING);
        this.listPool = this.table.getPool("main");
    }

    public void updateEntityServer() {

        super.updateEntityServer();
        if (this.getActive() && this.getLevel().getGameTime() % 5 == 0) {
            ParticleUtils.spawnFishingMachineParticles(this.getLevel(), pos, this.getLevel().random);
        }

        if (this.getWorld().getGameTime() % 100 == 0) {
            checkwater = checkwater();
        }
        if (checkwater && !this.inputslot.isEmpty()) {
            if ((this.inputslot.get(0).getItem() instanceof EnergyItem)) {
                boolean need = ElectricItem.manager.canUse(this.inputslot.get(0), 100);
                if (!need) {
                    return;
                }
            }
            if (progress < 100) {
                if (this.energy.getEnergy() >= this.energyconsume) {
                    if (!this.getActive()) {
                        this.setActive(true);
                    }
                    if (this.getActive() && this.progress == 0) {
                        initiate(0);
                    }
                    progress += this.level;


                } else {
                    if (this.getActive()) {
                        initiate(2);
                        this.setActive(false);
                    }
                }
            }
        } else {
            if (this.getActive()) {
                initiate(2);
                this.setActive(false);
            }
        }

        if (checkwater && progress >= 100) {
            if (this.getActive()) {
                initiate(2);
            }
            if (!this.inputslot.isEmpty()) {
                ItemStack stack = this.inputslot.get(0);
                int j = (int) (EnchantmentHelper.getFishingTimeReduction((ServerLevel) getWorld(), stack, player) * 20.0F);
                int k = EnchantmentHelper.getFishingLuckBonus((ServerLevel) getWorld(), stack, player);
                BlockPos pos1 = pos.below();
                lootcontext$builder
                        .withLuck((float) k)
                        .withParameter(LootContextParams.THIS_ENTITY, energyFishHook)
                        .withParameter(LootContextParams.ATTACKING_ENTITY, this.player).withParameter(LootContextParams.TOOL, stack).withParameter(LootContextParams.ORIGIN, new Vec3(pos1.getX(), pos1.getY(), pos1.getZ()));

                List<ItemStack> list = new ArrayList<>();
                this.table.getRandomItems(lootcontext$builder.create(LootContextParamSets.FISHING), list::add);
                for (ItemStack var1 : list) {
                    if (this.outputSlot.add(var1)) {
                        this.energy.useEnergy(this.energyconsume);
                    }
                    progress = 0;
                }
                int damage = stack.getMaxDamage() - stack.getDamageValue();
                int m = EnchantmentHelper.getItemEnchantmentLevel(this.getWorld().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.UNBREAKING), stack);
                RandomSource rand;
                if (!((stack.getItem() instanceof EnergyItem))) {
                    if (this.inputslot.get(0).getDamageValue() > -1) {

                        rand = this.getWorld().random;
                        if (rand.nextInt(1 + m) == 0 && damage > -1) {
                            this.inputslot.get(0).setDamageValue(this.inputslot.get(0).getDamageValue() + 1);
                        }


                    }

                    if (this.inputslot.get(0).getDamageValue() >= this.inputslot.get(0).getMaxDamage() && damage > -1) {
                        this.inputslot.consume(1);
                    }
                } else {
                    rand = this.getWorld().random;
                    if ((stack.getItem() instanceof EnergyItem)) {
                        if (rand.nextInt(1 + m) == 0) {
                            ElectricItem.manager.use(stack, 100, null);
                        }
                    }
                }
            }
        }
        if (getActive()) {
            if (this.getWorld().getGameTime() % 20 == 0 && !this.outputSlot.isEmpty()) {
                ModUtils.tick(this.outputSlot, this);
            }
        }
    }

    public List<ItemStack> getWrenchDrops(Player player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.level != 1) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation.getItem(), this.level));
            this.level = 1;
        }
        return ret;
    }

    @Override
    public boolean onActivated(
            final Player player,
            final InteractionHand hand,
            final Direction side,
            final Vec3 hit
    ) {
        if (level < 10) {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation.getItem())) {
                return super.onActivated(player, hand, side, hit);
            } else {
                stack.shrink(1);
                this.level++;
                return true;
            }
        } else {
            return super.onActivated(player, hand, side, hit);
        }
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getInt("progress");

        this.level = nbttagcompound.getInt("level");
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);

        nbttagcompound.putInt("progress", this.progress);
        nbttagcompound.putInt("level", this.level);

        return nbttagcompound;
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenFisher((ContainerMenuFisher) isAdmin);
    }

    public ContainerMenuFisher getGuiContainer(Player entityPlayer) {
        return new ContainerMenuFisher(entityPlayer, this);
    }


    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.ItemInput
        );
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.fisher.getSoundEvent();
    }

}
