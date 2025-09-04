package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.blockentity.Wrenchable;
import com.denfop.api.item.energy.EnergyItem;
import com.denfop.api.item.upgrade.UpgradeItem;
import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.api.item.upgrade.event.EventItemLoad;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.IManufacturerBlock;
import com.denfop.componets.AbstractComponent;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IProperties;
import com.denfop.proxy.CommonProxy;
import com.denfop.sound.EnumSound;
import com.denfop.utils.*;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.denfop.api.item.upgrade.EnumUpgrades.GRAVITOOL;

public class ItemGraviTool extends TieredItem implements EnergyItem, UpgradeItem, IProperties {
    protected static final double ROTATE = 50.0D;
    protected static final double HOE = 50.0D;
    protected static final double TAP = 50.0D;
    private String nameItem;

    public ItemGraviTool() {
        super(Tiers.IRON, new Properties().stacksTo(1).setNoRepair().tab(IUCore.EnergyTab));
        IUCore.proxy.addProperties(this);
        IUCore.runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, getUpgradeModules()));

    }

    public static GraviToolMode readToolMode(ItemStack stack) {
        return GraviToolMode.getFromID(ModUtils.nbt(stack).getInt("toolMode"));
    }

    public static GraviToolMode readNextToolMode(ItemStack stack) {
        return GraviToolMode.getFromID(ModUtils.nbt(stack).getInt("toolMode") + 1);
    }

    public static void saveToolMode(ItemStack stack, ItemGraviTool.GraviToolMode mode) {
        ModUtils.nbt(stack).putInt("toolMode", mode.ordinal());
    }

    public static boolean hasNecessaryPower(ItemStack stack, double usage, Player player) {
        double coef = 1D - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, stack).number * 0.25D : 0);

        return ElectricItem.manager.canUse(stack, usage * coef);
    }

    protected static boolean checkNecessaryPower(ItemStack stack, double usage, Player player) {
        return checkNecessaryPower(stack, usage, player, false);
    }

    protected static boolean checkNecessaryPower(ItemStack stack, double usage, Player player, boolean supressSound) {
        double coef = 1D - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, stack).number * 0.25D : 0);

        if (ElectricItem.manager.use(stack, usage * coef, player)) {
            if (!supressSound && player.level.isClientSide) {

            }

            return true;
        } else {
            CommonProxy.sendPlayerMessage(player, Localization.translate("message.text.noenergy"));
        }

        return false;
    }

    public static boolean hasToolMode(ItemStack stack) {
        if (!stack.hasTag()) {
            return false;
        }
        assert stack.getTag() != null;
        return stack.getTag().contains("toolMode", 4);
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", Registry.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "item.gravitool";
        }

        return this.nameItem;
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
    public String[] properties() {
        return new String[]{"mode"};
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getItemProperty(ItemStack itemStack, ClientLevel level, LivingEntity entity, int p174679, String property) {
        return readToolMode(itemStack).ordinal() * 0.25f;
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_HOE_ACTIONS.contains(toolAction) && readToolMode(stack) == GraviToolMode.HOE;
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if (IUCore.keyboard.isChangeKeyDown(player)) {
            ItemStack stack = ModUtils.get(player, hand);

            if (world.isClientSide) {
                player.playSound(EnumSound.toolchange.getSoundEvent(), 1F, 1F);
            } else {
                ItemGraviTool.GraviToolMode mode = readNextToolMode(stack);
                saveToolMode(stack, mode);
                CommonProxy.sendPlayerMessage(player, mode.colour + Localization.translate(mode.translationName));
            }

            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        }

        return super.use(world, player, hand);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(
            @Nonnull ItemStack stack,
            @Nullable Level world,
            @Nonnull List<Component> tooltip,
            @Nonnull TooltipFlag flag
    ) {
        ItemGraviTool.GraviToolMode mode = readToolMode(stack);

        tooltip.add(Component.translatable("message.text.mode")
                .append(": " + mode.colour)
                .append(Component.translatable(mode.translationName)));

        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("press.lshift"));
        } else {
            tooltip.add(Component.translatable("iu.changemode_key")
                    .append(" " + KeyboardClient.changemode.getKey().getDisplayName().getString())
                    .append(Component.translatable("iu.changemode_rcm")));
        }

        super.appendHoverText(stack, world, tooltip, flag);
    }

    @Nonnull
    @Override
    public InteractionResult onItemUseFirst(@Nonnull ItemStack stack, @Nonnull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        return switch (readToolMode(stack)) {
            case WRENCH ->
                    this.onWrenchUse(stack, player, level, pos, context.getClickedFace()) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
            case SCREWDRIVER ->
                    this.onScrewdriverUse(stack, player, level, pos) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
            default -> super.onItemUseFirst(stack, context);
        };
    }

    @Nonnull
    @Override
    public InteractionResult useOn(@Nonnull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hitResult = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), pos, context.isInside());

        if (player == null) {
            return InteractionResult.PASS;
        }

        switch (readToolMode(stack)) {
            case HOE:
                return this.onHoeUse(stack, player, level, pos, hitResult.getDirection()) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
            case TREETAP:
                return this.onTreeTapUse(stack, player, level, pos, hitResult.getDirection()) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
            case PURIFIER:
                BlockEntity tile = level.getBlockEntity(pos);
                if (!(tile instanceof BlockEntityInventory) && !(tile instanceof IManufacturerBlock)) {
                    return InteractionResult.PASS;
                }

                double coef = 1D - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, stack) ?
                        UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, stack).number * 0.25D
                        : 0);

                if (tile instanceof BlockEntityInventory) {
                    BlockEntityInventory base = (BlockEntityInventory) tile;
                    double energy = 10000;

                    if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PURIFIER, stack)) {
                        energy = 0;
                    }

                    if (!base.canEntityDestroy(player)) {
                        return InteractionResult.FAIL;
                    }

                    for (AbstractComponent component : base.getComponentList()) {
                        if (component.canUsePurifier(player) && ElectricItem.manager.canUse(stack, energy * coef)) {
                            component.workPurifier();
                            return InteractionResult.SUCCESS;
                        }
                    }
                }

                if (tile instanceof BlockEntityMultiMachine) {
                    if (!ElectricItem.manager.canUse(stack, 500 * coef)) {
                        return InteractionResult.PASS;
                    }

                    BlockEntityMultiMachine base = (BlockEntityMultiMachine) tile;
                    List<ItemStack> stackList = new ArrayList<>();

                    if (base.multi_process.quickly) {
                        stackList.add(new ItemStack(IUItem.module_quickly.getItem()));
                        base.multi_process.setQuickly(false);
                        base.multi_process.shrinkModule(1);
                    }
                    if (base.multi_process.modulesize) {
                        stackList.add(new ItemStack(IUItem.module_stack.getItem()));
                        base.multi_process.setModulesize(false);
                        base.multi_process.shrinkModule(1);
                    }
                    if (base.multi_process.module_separate) {
                        stackList.add(new ItemStack(IUItem.module_separate.getItem()));
                        base.multi_process.module_separate = false;
                        base.multi_process.shrinkModule(1);
                    }
                    if (base.solartype != null) {
                        stackList.add(new ItemStack(IUItem.module6.getStack(base.solartype.meta), 1));
                        base.solartype = null;
                    }
                    if (base.multi_process.modulestorage) {
                        stackList.add(new ItemStack(IUItem.module_storage.getItem()));
                        base.multi_process.setModulestorage(false);
                        base.multi_process.shrinkModule(1);
                    }
                    if (base.multi_process.module_infinity_water) {
                        stackList.add(new ItemStack(IUItem.module_infinity_water.getItem()));
                        base.multi_process.module_infinity_water = false;
                        base.multi_process.shrinkModule(1);
                    }

                    for (ItemStack dropStack : stackList) {
                        if (!level.isClientSide) {
                            ItemEntity itemEntity = new ItemEntity((ServerLevel) level, player.getX(), player.getY(), player.getZ(), dropStack);
                            itemEntity.setPickUpDelay(0);
                            level.addFreshEntity(itemEntity);
                            player.playNotifySound(EnumSound.purifier.getSoundEvent(), SoundSource.PLAYERS, 1F, 1);
                        }
                    }

                    ElectricItem.manager.use(stack, 500 * coef, player);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;

            default:
                return super.onItemUseFirst(stack, context);
        }
    }

    protected boolean onHoeUse(ItemStack stack, Player player, Level world, BlockPos pos, Direction side) {
        if (player.mayUseItemAt(pos.relative(side), side, stack) && hasNecessaryPower(stack, HOE, player)) {
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            BlockState state1 = world.getBlockState(pos.below());

            if (side != Direction.DOWN && state1.isAir()) {
                if (block == Blocks.GRASS_BLOCK || block == Blocks.DIRT_PATH) {
                    return this.setHoedBlock(stack, player, world, pos, Blocks.FARMLAND.defaultBlockState());
                }

                if (block == Blocks.DIRT) {
                    return this.setHoedBlock(stack, player, world, pos, Blocks.FARMLAND.defaultBlockState());
                } else if (block == Blocks.COARSE_DIRT) {
                    return this.setHoedBlock(stack, player, world, pos, Blocks.DIRT.defaultBlockState());
                }

            }
            return false;
        }
        return false;
    }

    protected boolean setHoedBlock(ItemStack stack, Player player, Level world, BlockPos pos, BlockState state) {
        if (checkNecessaryPower(stack, HOE, player, true)) {
            world.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!world.isClientSide) {
                world.setBlock(pos, state, 11);
            }
            return true;
        }
        return false;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide) {
            CompoundTag nbt = itemStack.getOrCreateTag();

            if (!UpgradeSystem.system.hasInMap(itemStack)) {
                nbt.putBoolean("hasID", false);
                MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, this, itemStack));
            }
        }
    }

    protected boolean onTreeTapUse(ItemStack stack, Player player, Level world, BlockPos pos, Direction side) {
        BlockState state = world.getBlockState(pos);
        return hasNecessaryPower(
                stack,
                TAP,
                player
        ) && (state.getBlock() == IUItem.rubWood.getBlock().get() && ItemTreetap.attemptExtract(
                player,
                world,
                pos,
                side,
                state,
                null
        ) || state.getBlock() == IUItem.swampRubWood.getBlock().get() && ItemTreetap.attemptSwampExtract(
                player,
                world,
                pos,
                side,
                state,
                null
        ) || state.getBlock() == IUItem.tropicalRubWood.getBlock().get() && ItemTreetap.attemptTropicalExtract(
                player,
                world,
                pos,
                side,
                state,
                null
        )) && checkNecessaryPower(stack, TAP, player);
    }

    protected boolean onWrenchUse(ItemStack stack, Player player, Level world, BlockPos pos, Direction side) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (state.isAir()) {
            return false;
        }

        if (block instanceof Wrenchable) {
            Wrenchable wrenchable = (Wrenchable) block;
            Direction current = wrenchable.getFacing(world, pos);
            Direction newFacing;

            if (!IUCore.keyboard.isChangeKeyDown(player)) {
                newFacing = player.isShiftKeyDown() ? side.getOpposite() : side;
            } else {
                Direction.Axis axis = side.getAxis();
                if ((player.isShiftKeyDown() || side.getAxisDirection() != Direction.AxisDirection.POSITIVE) &&
                        (!player.isShiftKeyDown() || side.getAxisDirection() != Direction.AxisDirection.NEGATIVE)) {
                    newFacing = current.getCounterClockWise(axis)
                            .getCounterClockWise(axis)
                            .getCounterClockWise(axis);
                } else {
                    newFacing = current.getClockWise(axis);
                }
            }

            if (current != newFacing) {
                if (!hasNecessaryPower(stack, ROTATE, player)) {
                    return false;
                }

                if (wrenchable.setFacing(world, pos, newFacing, player)) {
                    player.playSound(EnumSound.wrench.getSoundEvent(), 1F, 1);
                    return checkNecessaryPower(stack, ROTATE, player);
                }
            }

            if (wrenchable.wrenchCanRemove(world, pos, player)) {
                if (!hasNecessaryPower(stack, ROTATE, player)) {
                    return false;
                }

                player.playSound(EnumSound.wrench.getSoundEvent(), 1F, 1);
                if (!world.isClientSide) {
                    BlockEntity te = world.getBlockEntity(pos);
                    int experience;

                    if (player instanceof ServerPlayer) {
                        experience = ForgeHooks.onBlockBreakEvent(
                                world,
                                ((ServerPlayer) player).gameMode.getGameModeForPlayer(),
                                (ServerPlayer) player,
                                pos
                        );
                        if (experience < 0) {
                            return false;
                        }
                    } else {
                        experience = 0;
                    }

                    block.playerWillDestroy(world, pos, state, player);

                    if (!block.onDestroyedByPlayer(state, world, pos, player, true, world.getFluidState(pos))) {
                        return false;
                    }

                    int fortune = world.random.nextInt(100);
                    if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.WRENCH, stack)) {
                        fortune = 100;
                    }

                    for (ItemStack drop : wrenchable.getWrenchDrops(world, pos, state, te, player, fortune)) {
                        ModUtils.dropAsEntity(world, pos, drop);
                    }

                    wrenchable.wrenchBreak(world, pos);

                    if (!player.isCreative() && experience > 0) {
                        block.popExperience((ServerLevel) world, pos, experience);
                    }
                }
                return checkNecessaryPower(stack, ROTATE, player);
            }
        }
        return false;
    }

    protected boolean onScrewdriverUse(ItemStack stack, Player player, Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (!state.isAir() && block instanceof HorizontalDirectionalBlock && checkNecessaryPower(stack, 500.0D, player)) {
            Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);

            facing = player.isShiftKeyDown() ? facing.getCounterClockWise() : facing.getClockWise();

            world.setBlock(pos, state.setValue(HorizontalDirectionalBlock.FACING, facing), 3);
            return true;
        }
        return false;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            ElectricItemManager.addChargeVariants(this, p_41392_);
        }
    }

    @Override
    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    @Override
    public double getMaxEnergy(ItemStack stack) {
        return 300000.0D;
    }

    @Override
    public short getTierItem(ItemStack stack) {
        return 2;
    }

    @Override
    public double getTransferEnergy(ItemStack stack) {
        return 10000.0D;
    }

    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return GRAVITOOL.list;
    }

    public enum GraviToolMode {
        HOE(ChatFormatting.GOLD),
        TREETAP(ChatFormatting.LIGHT_PURPLE),
        WRENCH(ChatFormatting.AQUA),
        SCREWDRIVER(ChatFormatting.YELLOW),
        PURIFIER(ChatFormatting.DARK_AQUA);
        private static final ItemGraviTool.GraviToolMode[] VALUES = values();
        public final String translationName = "iu.graviTool.snap." + this.name().toLowerCase(Locale.ENGLISH);
        public final ChatFormatting colour;
        private final ModelResourceLocation model =
                new ModelResourceLocation(Constants.MOD_ID + ":" + "gravitool/gravitool".toLowerCase(Locale.ENGLISH) + this
                        .name()
                        .toLowerCase(Locale.ENGLISH), null);

        GraviToolMode(ChatFormatting colour) {
            this.colour = colour;
        }

        public static ItemGraviTool.GraviToolMode getFromID(int ID) {
            return VALUES[ID % VALUES.length];
        }
    }

}

