package com.denfop.items.energy.instruments;

import com.denfop.ElectricItem;
import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.upgrade.ILevelInstruments;
import com.denfop.api.upgrade.IUpgradeWithBlackList;
import com.denfop.api.upgrade.UpgradeItemInform;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemBlackListLoad;
import com.denfop.audio.EnumSound;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.UpgradeItem;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.IProperties;
import com.denfop.items.energy.ItemStackUpgradeItem;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.proxy.CommonProxy;
import com.denfop.utils.*;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemEnergyInstruments extends Item implements IEnergyItem, IItemStackInventory, IUpgradeWithBlackList,
        ILevelInstruments, IProperties, IItemTab {

    private final String name;
    private final int transferLimit;
    private final int maxCharge;
    private final int tier;
    private final int normalPower;
    private final int bigHolePower;
    private final int energyPerOperation;
    private final int energyBigHolePowerOperation;
    private final int ultraLowPower1;
    private final int ultraLowPower;
    private final Set<BlockState> mineableBlocks;
    private final int energyPerbigHolePowerOperation;
    private final int energyPerultraLowPowerOperation;
    private final List<EnumOperations> operations;
    private final List<TagKey<Block>> item_tools;
    private final String name_type;
    private final float fuel_balance = 10.0F;
    private final EnumTypeInstruments type;
    Set<String> toolType;
    private int efficiency;
    private String nameItem;

    public ItemEnergyInstruments(EnumTypeInstruments type, EnumVarietyInstruments variety, String name) {
        super(new Properties().setNoRepair().stacksTo(1).component(DataComponentsInit.UPGRADE_ITEM, UpgradeItem.EMPTY).component(DataComponentsInit.ENERGY, 0D).component(DataComponentsInit.MODE, 0).attributes(DiggerItem.createAttributes(Tiers.IRON, Tiers.IRON.getAttackDamageBonus(), Tiers.IRON.getSpeed())));
        this.name = name;
        this.type = type;
        this.name_type = type.getType_name() == null ? type.name().toLowerCase() : type.getType_name();
        this.transferLimit = variety.getEnergy_transfer();
        this.maxCharge = variety.getCapacity();
        this.tier = variety.getTier();
        this.efficiency = this.normalPower = variety.getNormal_power();
        this.bigHolePower = variety.getBig_holes();
        this.energyPerOperation = variety.getEnergyPerOperation();
        this.energyBigHolePowerOperation = variety.getEnergyPerBigOperation();
        this.energyPerbigHolePowerOperation = variety.getEnergyPerbigHolePowerOperation();
        this.energyPerultraLowPowerOperation = variety.getEnergyPerultraLowPowerOperation();
        this.ultraLowPower = variety.getMega_holes();
        this.ultraLowPower1 = variety.getUltra_power();
        this.mineableBlocks = type.getMineableBlocks();
        this.toolType = type.getToolType();
        this.operations = type.getListOperations();
        this.item_tools = type.getListItems();
        IUCore.proxy.addProperties(this);

        IUCore.runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, type.getEnumInfoUpgradeModules()));

    }

    @Override
    public int getMaxLevel(final ItemStack stack) {
        int maxLevel = ILevelInstruments.super.getMaxLevel(stack);
        if (maxLevel == Integer.MAX_VALUE) {
            return maxLevel;
        }
        maxLevel *= Math.max(0.5, 0.5 * tier);
        return maxLevel;
    }


    public boolean onDestroyed(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
        Block block = state.getBlock();
        if (block.equals(Blocks.AIR)) {
            return false;
        } else {


            if (state.liquid() || (state.getBlock().defaultDestroyTime() == 0 && !((Player) entity).isCreative())) {
                return false;
            }

            if (!world.isClientSide) {
                if (CommonHooks.fireBlockBreak(world, ((ServerPlayer) entity).gameMode.getGameModeForPlayer(), (ServerPlayer) entity, pos, state).isCanceled()) {
                    return false;
                }


                if (block.onDestroyedByPlayer(state, world, pos, (ServerPlayer) entity, true, world.getFluidState(pos))) {
                    List<UpgradeItemInform> upgradeItemInforms = UpgradeSystem.system.getInformation(stack);
                    boolean smelter = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SMELTER, stack, upgradeItemInforms);
                    boolean comb = UpgradeSystem.system.hasModules(
                            EnumInfoUpgradeModules.COMB_MACERATOR,
                            stack,
                            upgradeItemInforms
                    );
                    boolean mac = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.MACERATOR, stack, upgradeItemInforms);
                    boolean generator = UpgradeSystem.system.hasModules(
                            EnumInfoUpgradeModules.GENERATOR,
                            stack,
                            upgradeItemInforms
                    );
                    int random = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.RANDOM, stack, upgradeItemInforms) ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.RANDOM, stack, upgradeItemInforms).number : 0;
                    boolean black_list = stack.getOrDefault(DataComponentsInit.BLACK_LIST, false);

                    block.destroy(world, pos, state);
                    block.playerDestroy(world, (ServerPlayer) entity, pos, state, null, stack);
                    List<ItemEntity> items = entity.level().getEntitiesOfClass(
                            ItemEntity.class,
                            new AABB(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1,
                                    pos.getY() + 1,
                                    pos.getZ() + 1
                            )
                    );
                    this.addExperience(stack, 1);
                    if (!black_list && check_list(block,
                            0
                            , UpgradeSystem.system.getBlackList(stack)
                    )) {
                        for (ItemEntity item : items) {
                            if (!entity.level().isClientSide) {
                                ItemStack stack1 = item.getItem();

                                if (comb) {
                                    RecipeOutput rec = Recipes.recipes.getRecipeOutput("comb_macerator", false, stack1).output;
                                    if (rec != null) {
                                        stack1 = rec.items.get(0).copy();
                                        stack1.setCount(3);
                                    }
                                } else if (mac) {
                                    RecipeOutput rec = Recipes.recipes.getRecipeOutput("macerator", false, stack1).output;
                                    if (rec != null) {
                                        stack1 = rec.items.get(0).copy();

                                    }
                                }
                                ItemStack smelt = new ItemStack(Items.AIR);
                                if (smelter) {

                                    smelt = ModUtils.getRecipeFromType(world, stack1, RecipeType.SMELTING);
                                    if (!smelt.isEmpty()) {
                                        smelt.setCount(stack1.getCount());
                                    }
                                }
                                if (generator) {
                                    final int fuel = ModUtils.getFuelValue(stack1, false);
                                    final boolean rec = fuel > 0;
                                    if (rec) {
                                        int amount = stack1.getCount();
                                        int value = fuel / 4;
                                        amount *= value;
                                        amount *= fuel_balance;
                                        double sentPacket = ElectricItem.manager.charge(
                                                stack,
                                                amount,
                                                2147483647,
                                                true,
                                                false
                                        );
                                        amount -= sentPacket;
                                        amount /= (value * fuel_balance);
                                        stack1.setCount(amount);
                                    }
                                }
                                if (!smelt.isEmpty()) {
                                    item.setItem(smelt);
                                } else {
                                    item.setItem(stack1);
                                }

                                item.moveTo(entity.getX(), entity.getY(), entity.getZ(), 0.0F, 0.0F);
                                ((ServerPlayer) entity).connection.send(new ClientboundTeleportEntityPacket(item));
                                item.setPickUpDelay(0);

                            }
                        }
                        if (random != 0) {
                            final int rand = world.random.nextInt(100001);
                            if (rand >= 100000 - random) {
                                ItemEntity item = new ItemEntity(EntityType.ITEM, world);
                                item.setItem(IUCore.get_ingot.get(world.random.nextInt(IUCore.get_ingot.size())));
                                item.moveTo(entity.getX(), entity.getY(), entity.getZ(), 0.0F, 0.0F);
                                ((ServerPlayer) entity).connection.send(new ClientboundTeleportEntityPacket(item));
                                item.setPickUpDelay(0);
                            }
                        }
                    } else {
                        if (stack.getOrDefault(DataComponentsInit.BLACK_LIST, false)) {
                            for (ItemEntity item : items) {
                                if (!entity.level().isClientSide) {
                                    item.remove(Entity.RemovalReason.KILLED);

                                }
                            }
                        }
                        if (random != 0) {
                            final int rand = world.random.nextInt(100001);
                            if (rand >= 100000 - random) {
                                ItemEntity item = new ItemEntity(EntityType.ITEM, world);
                                item.setItem(IUCore.get_ingot.get(world.random.nextInt(IUCore.get_ingot.size())));
                                item.moveTo(entity.getX(), entity.getY(), entity.getZ(), 0.0F, 0.0F);
                                ((ServerPlayer) entity).connection.send(new ClientboundTeleportEntityPacket(item));
                                item.setPickUpDelay(0);
                            }
                        }
                    }
                }

            } else {
                if (block.onDestroyedByPlayer(state, world, pos, (Player) entity, true, world.getFluidState(pos))) {
                    block.destroy(world, pos, state);
                }


            }
            if (entity.isAlive()) {
                List<UpgradeItemInform> upgradeItemInforms = UpgradeSystem.system.getInformation(stack);
                float energy = energy(stack, upgradeItemInforms);
                if (energy != 0.0F) {
                    ElectricItem.manager.use(stack, energy, entity);
                }
            }

            return true;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (IUCore.keyboard.isSaveModeKeyDown(player)) {
            boolean save = !itemStack.getOrDefault(DataComponentsInit.SAVE, false);
            if (!worldIn.isClientSide)
                CommonProxy.sendPlayerMessage(
                        player,
                        ChatFormatting.GREEN + Localization.translate("message.savemode") +
                                (save ? Localization.translate("message.allow") : Localization.translate("message.disallow"))
                );
            itemStack.set(DataComponentsInit.SAVE, save);
        }
        if (IUCore.keyboard.isBlackListModeKeyDown(player)) {
            boolean black = !itemStack.getOrDefault(DataComponentsInit.BLACK_LIST, false);
            if (!worldIn.isClientSide)
                CommonProxy.sendPlayerMessage(
                        player,
                        ChatFormatting.GREEN + Localization.translate("message.blacklist") +
                                (black ? Localization.translate("message.allow") : Localization.translate("message.disallow"))
                );
            itemStack.set(DataComponentsInit.BLACK_LIST, black);
        }
        if (IUCore.keyboard.isChangeKeyDown(player)) {
            int toolMode = readToolMode(itemStack) + 1;
            if (!worldIn.isClientSide) {
                worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), EnumSound.toolchange.getSoundEvent(),
                        SoundSource.MASTER, 1F, 1
                );
            }
            if (toolMode >= this.operations.size()) {
                toolMode = 0;
            }
            saveToolMode(itemStack, toolMode);
            EnumOperations operation = this.operations.get(toolMode);
            if (!worldIn.isClientSide) {
                IUCore.proxy.messagePlayer(
                        player,
                        ChatFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                                + operation.getTextFormatting() + Localization.translate(operation.getName_mode())
                );
            }
            switch (operation) {
                case ORE:
                case TREE:
                case SHEARS:
                case TUNNEL:
                case DEFAULT:
                    this.efficiency = this.normalPower;
                    break;
                case BIGHOLES:
                    this.efficiency = this.bigHolePower;
                    break;
                case MEGAHOLES:
                    this.efficiency = this.ultraLowPower;
                    break;
                case ULTRAHOLES:
                    this.efficiency = this.ultraLowPower1;
                    break;
            }
            return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));

    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "item." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    public int readToolMode(ItemStack itemstack) {
        int toolMode = itemstack.get(DataComponentsInit.MODE);

        if (toolMode < 0 || toolMode >= this.operations.size()) {
            toolMode = 0;
        }
        return toolMode;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int slot, boolean par5) {
        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            NeoForge.EVENT_BUS.post(new EventItemBlackListLoad(world, this, itemStack, ModUtils.nbt(itemStack)));
        }

        if (entity instanceof Player) {
            if (IUCore.keyboard.isBlackListModeViewKeyDown((Player) entity)) {
                if (!entity.level().isClientSide && !itemStack.isEmpty() && ((Player) entity)
                        .getItemInHand(InteractionHand.MAIN_HAND)
                        .is(itemStack.getItem())) {


                    CustomPacketBuffer growingBuffer = new CustomPacketBuffer(entity.registryAccess());

                    growingBuffer.writeByte(1);

                    growingBuffer.flip();
                    ((Player) entity).openMenu(getInventory((Player) entity, itemStack), buf -> buf.writeBytes(growingBuffer));
                }
            }

        }
    }


    @Override
    public boolean hurtEnemy(ItemStack p_41395_, LivingEntity p_41396_, LivingEntity p_41397_) {
        return true;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 0;
    }


    public boolean isBookEnchantable(@Nonnull ItemStack stack, @Nonnull ItemStack book) {
        return false;
    }

    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    public double getMaxEnergy(ItemStack itemStack) {
        return this.maxCharge;
    }

    public boolean isBarVisible(final ItemStack stack) {
        return true;
    }

    public int getBarColor(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
    }


    public int getBarWidth(ItemStack stack) {

        return 13 - (int) (13.0F * Math.min(
                Math.max(
                        1 - ElectricItem.manager.getCharge(stack) / ElectricItem.manager.getMaxCharge(stack),
                        0.0
                ),
                1.0
        ));
    }


    @Override
    public void appendHoverText(ItemStack par1ItemStack, @Nullable TooltipContext p_41422_, List<Component> par3List, TooltipFlag p_41424_) {
        super.appendHoverText(par1ItemStack, p_41422_, par3List, p_41424_);
        int toolMode = readToolMode(par1ItemStack);
        EnumOperations operations = this.operations.get(toolMode);
        par3List.add(Component.literal(ChatFormatting.GOLD + Localization.translate("message.text.mode") + ": "
                + operations.getTextFormatting() + Localization.translate(operations.getName_mode())));
        par3List.add(Component.translatable(operations.getDescription()));
        if (par1ItemStack.getOrDefault(DataComponentsInit.SAVE, false)) {
            par3List.add(Component.literal(ChatFormatting.GREEN + Localization.translate("iu.savemode_allow")));
        }
        if (par1ItemStack.getOrDefault(DataComponentsInit.BLACK_LIST, false)) {
            par3List.add(Component.literal(ChatFormatting.DARK_GRAY + Localization.translate("iu.blacklist_allow")));
        }
        List<UpgradeItemInform> upgradeItemInforms = UpgradeSystem.system.getInformation(par1ItemStack);

        if (operations != EnumOperations.DEFAULT && operations != EnumOperations.ORE && operations != EnumOperations.TREE) {
            int aoe = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, par1ItemStack,
                    upgradeItemInforms
            )
                    ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, par1ItemStack, upgradeItemInforms).number
                    : 0);
            int dig_depth = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.DIG_DEPTH, par1ItemStack, upgradeItemInforms)
                    ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.DIG_DEPTH, par1ItemStack, upgradeItemInforms).number
                    : 0);

            par3List.add(Component.literal(Localization.translate("iu.instruments.info") + (operations.getArea_x() + aoe) + "x" + (operations.getArea_y() + aoe) + "x" + (operations.getArea_z() + dig_depth)));
        }
        float energy = energy(par1ItemStack, upgradeItemInforms);

        par3List.add(Component.literal(Localization.translate("iu.instruments.info2") + (int) energy + " EF"));

        if (!KeyboardIU.isKeyDown(InputConstants.KEY_LSHIFT)) {
            par3List.add(Component.literal(Localization.translate("press.lshift")));
        }

        if (KeyboardIU.isKeyDown(InputConstants.KEY_LSHIFT)) {


            par3List.add(Component.literal(Localization.translate("iu.changemode_key") + KeyboardClient.changemode.getKey().getDisplayName().getString() + Localization.translate(
                    "iu.changemode_rcm")));

            par3List.add(Component.literal(Localization.translate("iu.blacklist_key") + KeyboardClient.blackmode.getKey().getDisplayName().getString()
                    + Localization.translate(
                    "iu.changemode_rcm")));

            par3List.add(Component.literal(Localization.translate("iu.savemode_key") + KeyboardClient.savemode.getKey().getDisplayName().getString() + Localization.translate(
                    "iu.changemode_rcm")));
            par3List.add(Component.literal(Localization.translate("iu.blacklist_gui") + KeyboardClient.blacklistviewmode.getKey().getDisplayName().getString()));

        }
        ModUtils.mode(par1ItemStack, par3List);
        final int level = this.getLevel(par1ItemStack);
        final int maxLevel = this.getMaxLevel(par1ItemStack);
        final int experience = this.getExperience(par1ItemStack);
        par3List.add(Component.literal(Localization.translate("circuit.level") + ": " + level));
        par3List.add(Component.literal(Localization.translate("iu.space_colony_experience") + experience + "/" + maxLevel));
    }


    public short getTierItem(ItemStack itemStack) {
        return (short) this.tier;
    }

    public double getTransferEnergy(ItemStack itemStack) {
        return this.transferLimit;
    }

    @Nonnull
    public Set<String> getToolClasses(@Nonnull ItemStack stack) {
        return toolType;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        if (mineableBlocks.contains(state)) {
            return true;
        }

        for (TagKey<Block> blockTagKey : this.item_tools)
            if (state.is(blockTagKey))
                return true;
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        List<UpgradeItemInform> upgradeItemInforms = UpgradeSystem.system.getInformation(stack);
        int speed = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.EFFICIENCY, stack, upgradeItemInforms) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.EFFICIENCY, stack, upgradeItemInforms).number : 0;
        return !ElectricItem.manager.canUse(stack, this.energy(stack, upgradeItemInforms))
                ? 0.0F
                : (isCorrectToolForDrops(stack, state) ? (this.efficiency + (int) (this.efficiency * 0.2 * speed)) : 1.0F);

    }


    void chopTree(
            BlockPos pos,
            Player player,
            Level world,
            ItemStack stack,
            final List<BlockPos> list, float energy,
            final boolean smelter,
            final boolean comb,
            final boolean mac,
            final boolean generator,
            final int random,
            final boolean black_list, List<String> blackList
    ) {
        int Y = pos.getY();
        int X = pos.getX();
        int Z = pos.getZ();
        for (int xPos = X - 1; xPos <= X + 1; xPos++) {
            for (int yPos = Y; yPos <= Y + 1; yPos++) {
                for (int zPos = Z - 1; zPos <= Z + 1; zPos++) {
                    BlockPos pos1 = new BlockPos(xPos, yPos, zPos);
                    if (list.contains(pos1)) {
                        continue;
                    }
                    BlockState state = world.getBlockState(pos1);
                    Block block = state.getBlock();
                    list.add(pos1);
                    if (state.is(BlockTags.LOGS)) {

                        if (!player.isCreative()) {
                            onBlockDestroyed(stack, world, state, pos1, player, energy, smelter, comb, mac, generator, random,
                                    black_list, blackList
                            );
                        }
                        chopTree(
                                pos1,
                                player,
                                world,
                                stack,
                                list,
                                energy,
                                smelter,
                                comb,
                                mac,
                                generator,
                                random,
                                black_list,
                                blackList
                        );
                    }
                }
            }
        }
    }

    private boolean isTree(Level world, BlockPos pos) {
        Block wood = world.getBlockState(pos).getBlock();
        if (wood.equals(Blocks.AIR) || !wood.builtInRegistryHolder().is(BlockTags.LOGS)) {
            return false;
        }
        int top = pos.getY();
        int Y = pos.getY();
        int X = pos.getX();
        int Z = pos.getZ();
        for (int y = pos.getY(); y <= pos.getY() + 50; y++) {
            BlockPos pos1 = new BlockPos(X, y, Z);
            final BlockState blockstate = world.getBlockState(pos1);
            if (!blockstate.is(BlockTags.LOGS)
                    && !blockstate.is(BlockTags.LEAVES)) {
                top += y;
                break;
            }
        }
        int leaves = 0;
        for (int xPos = X - 1; xPos <= X + 1; xPos++) {
            for (int yPos = Y; yPos <= top; yPos++) {
                for (int zPos = Z - 1; zPos <= Z + 1; zPos++) {
                    BlockPos pos1 = new BlockPos(xPos, yPos, zPos);
                    final BlockState blockstate = world.getBlockState(pos1);
                    if (blockstate.is(BlockTags.LEAVES)) {
                        leaves++;
                    }
                }
            }
        }
        return leaves >= 3;
    }

    //
    boolean break_block(
            Level world, Block block, BlockHitResult mop, byte mode_item, Player player, BlockPos pos,
            ItemStack stack,
            final List<UpgradeItemInform> upgradeItemInforms,
            final boolean smelter,
            final boolean comb,
            final boolean mac,
            final boolean generator,
            final int random,
            final boolean black_list, List<String> blackList
    ) {
        byte xRange = mode_item;
        byte yRange = mode_item;
        byte zRange = mode_item;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        byte dig_depth = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms).number : 0);

        switch (mop.getDirection().ordinal()) {
            case 0:
            case 1:
                yRange = dig_depth;
                break;
            case 2:
            case 3:
                zRange = dig_depth;
                break;
            case 4:
            case 5:
                xRange = dig_depth;
                break;
        }

        boolean lowPower = false;
        boolean silktouch = EnchantmentHelper.getItemEnchantmentLevel(player.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH), stack) > 0;
        int fortune = EnchantmentHelper.getItemEnchantmentLevel(player.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE), stack);

        int Yy;
        Yy = yRange > 0 ? yRange - 1 : 0;

        float energy = energy(stack, upgradeItemInforms);


        boolean save = stack.getOrDefault(DataComponentsInit.SAVE, false);
        if (!player.isCreative()) {
            for (int xPos = x - xRange; xPos <= x + xRange; xPos++) {
                for (int yPos = y - yRange + Yy; yPos <= y + yRange + Yy; yPos++) {
                    for (int zPos = z - zRange; zPos <= z + zRange; zPos++) {
                        if (ElectricItem.manager.canUse(stack, energy)) {

                            BlockPos pos_block = new BlockPos(xPos, yPos, zPos);
                            if (save) {
                                if (world.getBlockEntity(pos_block) != null) {
                                    continue;
                                }
                            }

                            BlockState state = world.getBlockState(pos_block);
                            Block localBlock = state.getBlock();
                            if (!localBlock.equals(Blocks.AIR) && isCorrectToolForDrops(stack, state)
                                    && state.getDestroySpeed(world, pos_block) >= 0.0F
                            ) {
                                if (state.getDestroySpeed(world, pos_block) >= 0.0F) {
                                    onBlockDestroyed(stack, world, state, pos_block,
                                            player, energy, smelter, comb, mac, generator, random, black_list, blackList
                                    );
                                }
                                if (!silktouch) {
                                    ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos_block, player, stack
                                            , localBlock));
                                }


                            }
                        } else {
                            lowPower = true;
                            break;
                        }
                    }
                }
            }
        } else {
            if (ElectricItem.manager.canUse(stack, energy)) {
                BlockState state = world.getBlockState(pos);
                Block localBlock = state.getBlock();
                if (!localBlock.equals(Blocks.AIR) && isCorrectToolForDrops(stack, state)
                        && state.getDestroySpeed(world, pos) >= 0.0F
                        || (
                        block == Blocks.SPAWNER)) {
                    if (state.getDestroySpeed(world, pos) >= 0.0F) {
                        onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, blackList
                        );
                    }
                    if (!silktouch) {
                        ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos, player, stack
                                , localBlock));
                    }


                } else {
                    if (state.getDestroySpeed(world, pos) >= 0.0F) {
                        return onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, blackList
                        );
                    }
                }
            }
        }
        if (lowPower) {
            if (ElectricItem.manager.canUse(stack, energy)) {
                BlockState state = world.getBlockState(pos);
                Block localBlock = state.getBlock();

                if (!localBlock.equals(Blocks.AIR) && isCorrectToolForDrops(stack, state)
                        && state.getDestroySpeed(world, pos) >= 0.0F
                        || (block == Blocks.SPAWNER)) {

                    if (state.getDestroySpeed(world, pos) >= 0.0F) {
                        onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, blackList
                        );
                    }
                    if (!silktouch) {
                        ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos, player, stack
                                , localBlock));
                    }


                } else {
                    if (state.getDestroySpeed(world, pos) >= 0.0F) {
                        return onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, blackList
                        );
                    }
                }
            }
        }
        return true;
    }


    public boolean mineBlock(ItemStack stack, Level p_41417_, BlockState state, BlockPos pos, LivingEntity p_41420_) {
        if (!(p_41420_ instanceof Player player)) {
            return false;
        }
        EnumOperations operations = this.operations.get(readToolMode(stack));
        Level world = player.level();
        Block block = state.getBlock();
        BlockHitResult mop = RetraceDiggingUtils.retrace(player);
        List<UpgradeItemInform> upgradeItemInforms = UpgradeSystem.system.getInformation(stack);
        boolean smelter = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SMELTER, stack, upgradeItemInforms);
        boolean comb = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.COMB_MACERATOR, stack, upgradeItemInforms);
        boolean mac = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.MACERATOR, stack, upgradeItemInforms);
        boolean generator = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.GENERATOR, stack, upgradeItemInforms);
        int random = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.RANDOM, stack, upgradeItemInforms) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.RANDOM, stack, upgradeItemInforms).number : 0;
        boolean black_list = stack.getOrDefault(DataComponentsInit.BLACK_LIST, false);
        final List<String> list = UpgradeSystem.system.getBlackList(stack);
        switch (operations) {
            case DEFAULT:

                if (block == Blocks.AIR) {
                    return super.mineBlock(stack, p_41417_, state, pos, player);
                }

                byte aoe = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms) ?
                        UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number : 0);

                return break_block(world, block, mop, aoe, player, pos, stack, upgradeItemInforms, smelter, comb, mac, generator,
                        random, black_list, list
                );
            case BIGHOLES:

                if (block.equals(Blocks.AIR)) {
                    return super.mineBlock(stack, p_41417_, state, pos, player);
                }


                aoe = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms) ?
                        UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number : 0);
                if (player.isShiftKeyDown()) {
                    if (!mop.getType().equals(BlockHitResult.Type.MISS)) {
                        return break_block(world, block, mop, aoe, player, pos, stack, upgradeItemInforms,
                                smelter,
                                comb,
                                mac,
                                generator,
                                random,
                                black_list, list
                        );
                    }
                }

                return break_block(world, block, mop, (byte) (1 + aoe), player, pos, stack, upgradeItemInforms,
                        smelter,
                        comb,
                        mac,
                        generator,
                        random,
                        black_list, list
                );
            case MEGAHOLES:
                if (block.equals(Blocks.AIR)) {
                    return super.mineBlock(stack, p_41417_, state, pos, player);
                }

                aoe = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms) ?
                        UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number : 0);

                if (player.isShiftKeyDown()) {
                    if (!mop.getType().equals(BlockHitResult.Type.MISS)) {
                        return break_block(world, block, mop, aoe, player, pos, stack, upgradeItemInforms,
                                smelter,
                                comb,
                                mac,
                                generator,
                                random,
                                black_list, list
                        );
                    }
                }
                return break_block(world, block, mop, (byte) (2 + aoe), player, pos, stack, upgradeItemInforms,
                        smelter,
                        comb,
                        mac,
                        generator,
                        random,
                        black_list, list
                );
            case ULTRAHOLES:

                if (block.equals(Blocks.AIR)) {
                    return super.mineBlock(stack, p_41417_, state, pos, player);
                }
                aoe = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms) ?
                        UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number : 0);
                if (player.isShiftKeyDown()) {
                    if (!mop.getType().equals(BlockHitResult.Type.MISS)) {
                        return break_block(world, block, mop, aoe, player, pos, stack, upgradeItemInforms,
                                smelter,
                                comb,
                                mac,
                                generator,
                                random,
                                black_list, list
                        );
                    }
                }
                return break_block(world, block, mop, (byte) (3 + aoe), player, pos, stack, upgradeItemInforms,
                        smelter,
                        comb,
                        mac,
                        generator,
                        random,
                        black_list, list
                );
            case ORE:
                if (block.equals(Blocks.AIR)) {
                    return super.mineBlock(stack, p_41417_, state, pos, player);
                }
                boolean silktouch = EnchantmentHelper.getItemEnchantmentLevel(player.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH), stack) > 0;
                int fortune = EnchantmentHelper.getItemEnchantmentLevel(player.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE), stack);
                CompoundTag nbt = ModUtils.nbt(stack);
                nbt.putInt("ore", 1);
                float energy = energy(stack, upgradeItemInforms);
                if (!mop.getType().equals(BlockHitResult.Type.MISS)) {
                    ore_break(world, pos, player, silktouch, fortune, false, stack, block, smelter,
                            comb,
                            mac,
                            generator,
                            random, black_list, energy, list
                    );
                    return break_block(world, block, mop, (byte) (0), player, pos, stack, upgradeItemInforms,
                            smelter,
                            comb,
                            mac,
                            generator,
                            random,
                            black_list, list
                    );
                }
                return break_block(world, block, mop, (byte) (0), player, pos, stack, upgradeItemInforms,
                        smelter,
                        comb,
                        mac,
                        generator,
                        random,
                        black_list, list
                );
            case TREE:
                if (isTree(player.level(), pos)) {

                    List<BlockPos> list1 = new ArrayList<>();
                    energy = energy(stack, upgradeItemInforms);
                    chopTree(pos, player, player.level(), stack, list1, energy, smelter,
                            comb,
                            mac,
                            generator,
                            random, black_list, list
                    );
                    return break_block(world, block, mop, (byte) (0), player, pos, stack, upgradeItemInforms,
                            smelter,
                            comb,
                            mac,
                            generator,
                            random,
                            black_list, list
                    );
                }
                return break_block(world, block, mop, (byte) (0), player, pos, stack, upgradeItemInforms,
                        smelter,
                        comb,
                        mac,
                        generator,
                        random,
                        black_list, list
                );
            case TUNNEL:

                if (block == Blocks.AIR) {
                    return super.mineBlock(stack, p_41417_, state, pos, player);
                }


                return break_block_tunel(world, block, mop, player, pos, stack, upgradeItemInforms, smelter, comb, mac,
                        generator,
                        random, black_list, list
                );
            case SHEARS:
                if (block == Blocks.AIR) {
                    return super.mineBlock(stack, p_41417_, state, pos, player);
                }
                if (block.builtInRegistryHolder().is(BlockTags.LEAVES)) {
                    return break_shears(world, block, mop, player, pos, stack, upgradeItemInforms, smelter, comb, mac,
                            generator,
                            random, black_list, list
                    );
                } else {
                    aoe = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms)
                            ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number
                            : 0);

                    return break_block(
                            world,
                            block,
                            mop,
                            aoe,
                            player,
                            pos,
                            stack,
                            upgradeItemInforms,
                            smelter,
                            comb,
                            mac,
                            generator,
                            random,
                            black_list,
                            list
                    );
                }
        }
        return false;
    }

    private boolean break_shears(
            Level world,
            Block block,
            BlockHitResult mop,
            Player player,
            BlockPos pos,
            ItemStack itemstack,
            List<UpgradeItemInform> upgradeItemInforms,
            boolean smelter,
            boolean comb,
            boolean mac,
            boolean generator,
            int random,
            boolean blackList,
            List<String> list
    ) {
        float energy = energy(itemstack, upgradeItemInforms);
        BlockState state = world.getBlockState(pos);
        if (state.is(BlockTags.LEAVES) && ElectricItem.manager.use(itemstack, energy, player)) {
            if (!block.equals(Blocks.AIR) && isCorrectToolForDrops(itemstack, state)
                    && state.getDestroySpeed(world, pos) >= 0.0F
            ) {
                if (state.getDestroySpeed(world, pos) >= 0.0F) {
                    onBlockDestroyed(itemstack, world, state, pos,
                            player, energy, smelter, comb, mac, generator, random, blackList, list, true
                    );
                    return true;
                }
            }
        }
        return false;
    }

    private boolean break_block_tunel(
            Level world, Block block, BlockHitResult mop, Player player, BlockPos pos,
            ItemStack stack,
            final List<UpgradeItemInform> upgradeItemInforms,
            final boolean smelter,
            final boolean comb,
            final boolean mac,
            final boolean generator,
            final int random,
            final boolean black_list, List<String> list
    ) {
        byte xRange = 6;
        byte yRange = 6;
        byte zRange = 6;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        Direction direction = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE).getDirection();
        switch (direction.ordinal()) {

            case 0:
            case 1:
                xRange = 0;
                zRange = 0;
                break;
            case 2:
            case 3:
                xRange = 0;
                yRange = 0;
                break;
            case 4:
            case 5:
                zRange = 0;
                yRange = 0;
                break;
        }

        boolean lowPower = false;
        boolean silktouch = EnchantmentHelper.getItemEnchantmentLevel(player.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH), stack) > 0;
        int fortune = EnchantmentHelper.getItemEnchantmentLevel(player.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE), stack);

        CompoundTag nbt = ModUtils.nbt(stack);
        float energy = energy(stack, upgradeItemInforms);
        byte dig_depth = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms).number : 0);
        if (dig_depth > 0) {
            if (zRange > 0) {
                xRange = dig_depth;

            } else if (xRange > 0) {
                zRange = dig_depth;
            } else {
                zRange = dig_depth;
            }
        }
        boolean save = nbt.getBoolean("save");
        if (!player.isCreative()) {
            for (int xPos = x - xRange; xPos <= x + xRange; xPos++) {
                for (int yPos = y - yRange; yPos <= y + yRange; yPos++) {
                    for (int zPos = z - zRange; zPos <= z + zRange; zPos++) {
                        if (ElectricItem.manager.canUse(stack, energy)) {

                            BlockPos pos_block = new BlockPos(xPos, yPos, zPos);
                            if (save) {
                                if (world.getBlockEntity(pos_block) != null) {
                                    continue;
                                }
                            }

                            BlockState state = world.getBlockState(pos_block);
                            Block localBlock = state.getBlock();

                            if (!localBlock.equals(Blocks.AIR) && isCorrectToolForDrops(stack, state)
                                    && state.getDestroySpeed(world, pos_block) >= 0.0F
                            ) {
                                if (state.getDestroySpeed(world, pos_block) >= 0.0F) {
                                    onBlockDestroyed(stack, world, state, pos_block,
                                            player, energy, smelter, comb, mac, generator, random, black_list
                                            , list
                                    );
                                }
                                if (!silktouch) {
                                    ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos_block, player, stack
                                            , localBlock));
                                }


                            }


                        } else {
                            lowPower = true;
                            break;
                        }
                    }
                }
            }
        } else {
            if (ElectricItem.manager.canUse(stack, energy)) {
                BlockState state = world.getBlockState(pos);
                Block localBlock = state.getBlock();


                if (!localBlock.equals(Blocks.AIR) && isCorrectToolForDrops(stack, state)
                        && state.getDestroySpeed(world, pos) >= 0.0F
                        || (
                        block == Blocks.SPAWNER)) {
                    if (state.getDestroySpeed(world, pos) >= 0.0F) {
                        onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, list
                        );
                    }
                    if (!silktouch) {
                        ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos, player, stack
                                , localBlock));
                    }


                } else {
                    if (state.getDestroySpeed(world, pos) >= 0.0F) {
                        return onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, list
                        );
                    }
                }
            }
        }
        if (lowPower) {
            if (ElectricItem.manager.canUse(stack, energy)) {
                BlockState state = world.getBlockState(pos);
                Block localBlock = state.getBlock();

                if (!localBlock.equals(Blocks.AIR) && isCorrectToolForDrops(stack, state)
                        && state.getDestroySpeed(world, pos) >= 0.0F
                        || (block == Blocks.SPAWNER)) {

                    if (state.getDestroySpeed(world, pos) >= 0.0F) {
                        onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, list
                        );
                    }
                    if (!silktouch) {
                        ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos, player, stack
                                , localBlock));
                    }


                } else {
                    if (state.getDestroySpeed(world, pos) >= 0.0F) {
                        return onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, list
                        );
                    }
                }
            }
        }
        return true;
    }

    //
    private void ore_break(
            Level world, BlockPos pos, Player player, boolean silktouch, int fortune, boolean lowPower,
            ItemStack stack, Block block1,
            final boolean smelter,
            final boolean comb,
            final boolean mac,
            final boolean generator,
            final int random,
            final boolean black_list, float energy, List<String> list
    ) {

        CompoundTag NBTTagCompound = ModUtils.nbt(stack);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        for (int Xx = x - 2; Xx <= x + 2; Xx++) {
            for (int Yy = y - 2; Yy <= y + 2; Yy++) {
                for (int Zz = z - 2; Zz <= z + 2; Zz++) {

                    int ore = NBTTagCompound.getInt("ore");
                    if (ore < 32) {
                        BlockPos pos_block = new BlockPos(Xx, Yy, Zz);
                        BlockState state = world.getBlockState(pos_block);
                        Block localBlock = state.getBlock();

                        if (ModUtils.getore(localBlock, block1)) {
                            if (ElectricItem.manager.canUse(
                                    stack, energy

                            )) {


                                if (!player.isCreative()) {

                                    if (state.getDestroySpeed(world, pos_block) >= 0.0F) {
                                        onBlockDestroyed(stack, world, state, pos_block,
                                                player, energy,
                                                smelter, comb, mac, generator, random,
                                                black_list, list
                                        );

                                    }
                                    if (!silktouch) {
                                        ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos_block, player, stack
                                                , localBlock));
                                    }


                                    ore = ore + 1;
                                    NBTTagCompound.putInt("ore", ore);
                                    ore_break(world, pos_block, player, silktouch, fortune, lowPower, stack, block1,
                                            smelter, comb, mac, generator, random,
                                            black_list, energy, list
                                    );
                                } else {
                                    break;
                                }


                            } else {
                                lowPower = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

    }

    private int getExperience(
            BlockState state,
            Level world,
            BlockPos pos_block,
            Entity entity,
            ItemStack stack,
            final Block localBlock
    ) {
        int col = localBlock.getExpDrop(state, world, pos_block, null, entity, stack);
        col *= UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.EXPERIENCE, stack) ?
                (UpgradeSystem.system.getModules(EnumInfoUpgradeModules.EXPERIENCE, stack).number * 0.5 + 1) : 1;
        return col;
    }

    public boolean onBlockDestroyed(
            @Nonnull ItemStack stack,
            @Nonnull Level world,
            BlockState state,
            @Nonnull BlockPos pos,
            @Nonnull LivingEntity entity, float energy,
            final boolean smelter,
            final boolean comb,
            final boolean mac,
            final boolean generator,
            final int random,
            final boolean black_list,
            List<String> blackList
    ) {
        return onBlockDestroyed(
                stack,
                world,
                state,
                pos,
                entity,
                energy,
                smelter,
                comb,
                mac,
                generator,
                random,
                black_list,
                blackList,
                false
        );
    }

    public boolean onBlockDestroyed(
            @Nonnull ItemStack stack,
            @Nonnull Level world,
            BlockState state,
            @Nonnull BlockPos pos,
            @Nonnull LivingEntity entity, float energy,
            final boolean smelter,
            final boolean comb,
            final boolean mac,
            final boolean generator,
            final int random,
            final boolean black_list,
            List<String> blackList, boolean shears
    ) {

        Block block = state.getBlock();
        if (block.equals(Blocks.AIR)) {
            return false;
        } else {


            if (state.liquid() || (state.getBlock().defaultDestroyTime() == 0 && !((Player) entity).isCreative())) {
                return false;
            }

            if (!world.isClientSide) {

                if (CommonHooks.fireBlockBreak(world, ((ServerPlayer) entity).gameMode.getGameModeForPlayer(), (ServerPlayer) entity, pos, state).isCanceled()) {
                    return false;
                }


                if (block.onDestroyedByPlayer(state, world, pos, (ServerPlayer) entity, true, world.getFluidState(pos))) {
                    block.destroy(world, pos, state);

                    block.playerDestroy(world, (ServerPlayer) entity, pos, state, null, stack);
                    CompoundTag nbt = ModUtils.nbt(stack);
                    List<ItemEntity> items = entity.level().getEntitiesOfClass(
                            ItemEntity.class,
                            new AABB(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1,
                                    pos.getY() + 1,
                                    pos.getZ() + 1
                            )
                    );
                    this.addExperience(stack, 1);
                    if (!black_list || (ModUtils.getore(block, block) && check_list(block,
                            0
                            , blackList
                    ))) {
                        for (ItemEntity item : items) {
                            if (!entity.level().isClientSide) {
                                ItemStack stack1 = item.getItem();

                                if (comb) {
                                    RecipeOutput rec = Recipes.recipes.getRecipeOutput("comb_macerator", false, stack1).output;
                                    if (rec != null) {
                                        stack1 = rec.items.get(0).copy();
                                        stack1.setCount(3);
                                    }
                                } else if (mac) {
                                    RecipeOutput rec = Recipes.recipes.getRecipeOutput("macerator", false, stack1).output;
                                    if (rec != null) {
                                        stack1 = rec.items.get(0).copy();

                                    }
                                }
                                ItemStack smelt = new ItemStack(Items.AIR);
                                if (smelter) {

                                    smelt = ModUtils.getRecipeFromType(world, stack1, RecipeType.SMELTING);
                                    if (!smelt.isEmpty()) {
                                        smelt.setCount(stack1.getCount());
                                    }
                                }
                                if (generator) {
                                    final int fuel = ModUtils.getFuelValue(stack1, false);
                                    final boolean rec = fuel > 0;
                                    if (rec) {
                                        int amount = stack1.getCount();
                                        int value = fuel / 4;
                                        amount *= value;
                                        amount *= fuel_balance;
                                        double sentPacket = ElectricItem.manager.charge(
                                                stack,
                                                amount,
                                                2147483647,
                                                true,
                                                false
                                        );
                                        amount -= sentPacket;
                                        amount /= (value * fuel_balance);
                                        stack1.setCount(amount);
                                    }
                                }
                                if (!smelt.isEmpty()) {
                                    item.setItem(smelt);
                                } else {
                                    item.setItem(stack1);
                                }

                                item.moveTo(entity.getX(), entity.getY(), entity.getZ(), 0.0F, 0.0F);
                                ((ServerPlayer) entity).connection.send(new ClientboundTeleportEntityPacket(item));
                                item.setPickUpDelay(0);

                            }
                        }
                    } else {
                        if (nbt.getBoolean("black")) {
                            for (ItemEntity item : items) {
                                if (!entity.level().isClientSide) {
                                    item.remove(Entity.RemovalReason.KILLED);

                                }
                            }
                        }
                    }
                }
                if (random != 0) {
                    final int rand = world.random.nextInt(100001);
                    if (rand >= 100000 - random) {
                        ItemEntity item = new ItemEntity(EntityType.ITEM, world);
                        item.setItem(IUCore.get_ingot.get(world.random.nextInt(IUCore.get_ingot.size())));
                        item.moveTo(entity.getX(), entity.getY(), entity.getZ(), 0.0F, 0.0F);
                        ((ServerPlayer) entity).connection.send(new ClientboundTeleportEntityPacket(item));
                        item.setPickUpDelay(0);
                    }
                }

            } else {
                if (block.onDestroyedByPlayer(state, world, pos, (Player) entity, true, world.getFluidState(pos))) {
                    block.destroy(world, pos, state);
                }


            }
            if (entity.isAlive()) {

                if (energy != 0.0F) {
                    ElectricItem.manager.use(stack, energy, entity);
                }
            }

            return true;
        }
    }

    @Override
    public IAdvInventory getInventory(final Player player, final ItemStack stack) {
        return new ItemStackUpgradeItem(player, stack);
    }


    public boolean check_list(Block block, int metaFromState, final List<String> blackList) {

        if (blackList.isEmpty()) {
            return true;
        }

        ItemStack stack1 = new ItemStack(block, 1);
        if (stack1.isEmpty()) {
            return true;
        }
        ;
        List<TagKey<Item>> list = stack1.getTags().toList();
        if (list.isEmpty()) {
            return true;
        }

        String name = list.get(0).location().toString();


        return !blackList.contains(name);
    }

    public void saveToolMode(ItemStack itemstack, int toolMode) {
        itemstack.set(DataComponentsInit.MODE, toolMode);
    }


    public float energy(ItemStack stack, final List<UpgradeItemInform> upgradeItemInforms) {
        int energy1 = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, stack, upgradeItemInforms) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, stack, upgradeItemInforms).number : 0;
        int toolMode = readToolMode(stack);
        float energy;
        EnumOperations operations = this.operations.get(toolMode);
        energy = switch (operations) {
            case BIGHOLES ->
                    (float) (this.energyBigHolePowerOperation - this.energyBigHolePowerOperation * 0.25 * energy1);
            case MEGAHOLES ->
                    (float) (this.energyPerbigHolePowerOperation - this.energyPerbigHolePowerOperation * 0.25 * energy1);
            case ULTRAHOLES ->
                    (float) (this.energyPerultraLowPowerOperation - this.energyPerultraLowPowerOperation * 0.25 * energy1);
            default -> (float) (this.energyPerOperation - this.energyPerOperation * 0.25 * energy1);
        };
        return energy;


    }

    @Override
    public InteractionResult useOn(UseOnContext p_41427_) {
        Player player = p_41427_.getPlayer();
        if (this.toolType.contains("pickaxe")) {
            for (int i = 0; i < player.getInventory().items.size(); i++) {
                ItemStack torchStack = player.getInventory().items.get(i);
                if (!torchStack.isEmpty() && torchStack.is(Items.TORCH)) {
                    Item item = torchStack.getItem();
                    if (item instanceof BlockItem) {
                        UseOnContext useOnContext = new UseOnContext(p_41427_.getLevel(), p_41427_.getPlayer(), p_41427_.getHand(), torchStack, new BlockHitResult(p_41427_.getClickLocation(), p_41427_.getClickedFace(), p_41427_.getClickedPos(), p_41427_.isInside()));
                        boolean result = torchStack.getItem().useOn(useOnContext) == InteractionResult.SUCCESS;
                        if (result) {
                            torchStack.setCount(torchStack.getCount() - 1);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }


    public List<EnumOperations> getOperations() {
        return operations;
    }


    @Override
    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return type.getEnumInfoUpgradeModules();
    }


    @Override
    public String[] properties() {
        return new String[]{"mode"};
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public float getItemProperty(ItemStack stack, ClientLevel level, LivingEntity entity, int p174679, String property) {
        String[] mode = {"", "Zelen", "Demon", "Dark", "Cold", "Ender", "Ukraine", "Fire", "Snow", "Taiga", "Desert", "Emerald"};
        for (int i = 0; i < mode.length; i++)
            if (stack.getOrDefault(DataComponentsInit.SKIN, "").equals(mode[i]))
                return i;
        return 0;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> items) {
        if (this.allowedIn(p_41391_)) {
            ItemStack stack = new ItemStack(this, 1);

            ElectricItem.manager.charge(stack, 2.147483647E9D, 2147483647, true, false);
            items.add(stack);
            ItemStack itemstack = new ItemStack(this, 1);
            items.add(itemstack);
        }
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }
}
