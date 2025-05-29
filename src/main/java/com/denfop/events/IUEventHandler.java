package com.denfop.events;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.pollution.ChunkLevel;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.api.space.upgrades.SpaceUpgradeSystem;
import com.denfop.api.space.upgrades.info.SpaceUpgradeItemInform;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeItemInform;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.container.ContainerBags;
import com.denfop.container.ContainerLeadBox;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.ItemBaseCircuit;
import com.denfop.items.ItemCraftingElements;
import com.denfop.items.armour.ItemAdvJetpack;
import com.denfop.items.armour.special.EnumCapability;
import com.denfop.items.armour.special.ItemSpecialArmor;
import com.denfop.items.bags.ItemEnergyBags;
import com.denfop.items.bags.ItemLeadBox;
import com.denfop.items.modules.*;
import com.denfop.items.reactors.IRadioactiveItemType;
import com.denfop.items.resource.ItemNuclearResource;
import com.denfop.mixin.invoker.LevelInvoker;
import com.denfop.network.WorldData;
import com.denfop.network.packet.PacketColorPickerAllLoggIn;
import com.denfop.network.packet.PacketRadiationUpdateValue;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.lightning_rod.IController;
import com.denfop.tiles.mechanism.TileEntityPalletGenerator;
import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import com.denfop.utils.CapturedMobUtils;
import com.denfop.utils.Keyboard;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static com.denfop.tiles.lightning_rod.TileEntityLightningRodController.controllerMap;

public class IUEventHandler {

    public static List<ItemEntity> entityItemList = new LinkedList<>();
    final ChatFormatting[] name = {ChatFormatting.DARK_PURPLE, ChatFormatting.YELLOW, ChatFormatting.BLUE,
            ChatFormatting.RED, ChatFormatting.GRAY, ChatFormatting.GREEN, ChatFormatting.DARK_AQUA, ChatFormatting.AQUA};
    final String[] mattertype = {"matter.name", "sun_matter.name", "aqua_matter.name", "nether_matter.name", "night_matter.name", "earth_matter.name", "end_matter.name", "aer_matter.name"};
    Tuple<ItemStack, BaseMachineRecipe> tupleRecipe;
    Tuple<ItemStack, BaseMachineRecipe> tupleReplicatorRecipe;

    public static boolean getUpgradeItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof IUpgradeItem;

    }

    @SubscribeEvent
    public void onWorldTick1(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Level level = event.level;

            if (!level.isThundering() || level.isClientSide || level.getGameTime() % 20 != 0) {
                return;
            }

            if (WorldBaseGen.random.nextInt(100) < 2) {
                for (Map.Entry<BlockPos, IController> entry : controllerMap.entrySet()) {
                    IController controller = entry.getValue();

                    if (controller.isFull() && !controller.getTimer().isCanWork()) {
                        BlockPos antennaPos = controller.getBlockAntennaPos();

                        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                        if (lightning != null) {
                            lightning.moveTo(antennaPos.getX(), antennaPos.getY(), antennaPos.getZ());
                            level.addFreshEntity(lightning);
                            controller.getTimer().setCanWork(true);
                            controller.getTimer().resetTime();
                            controller.getEnergy().addEnergy(500000);
                        }
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void workCutters(PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (world.isClientSide) {
            return;
        }

        // Check if the item used is the cutter
        if (stack.getItem() == IUItem.cutter.getItem()) {

            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof TileEntityMultiCable) {
                TileEntityMultiCable cable = (TileEntityMultiCable) tile;

                ItemStack drop = cable.getPickBlock(player, null);
                if (!drop.isEmpty()) {
                    ModUtils.dropAsEntity(world, pos, drop);
                }

                cable.removeConductor();
            }
        } else if (!stack.isEmpty()) {
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof TileEntityBlock && player.isShiftKeyDown()) {
                ((TileEntityBlock) tile).onSneakingActivated(player, event.getHand(), event.getFace(),
                        event.getHitVec().getLocation());
            }
        }
    }

    @SubscribeEvent
    public void bagPickup(EntityItemPickupEvent event) {
        Player player = event.getEntity();
        try {
            boolean isContainerBags = !(player.containerMenu instanceof ContainerBags);
            boolean isContainerBox = !(player.containerMenu instanceof ContainerLeadBox);

            if (isContainerBags) {
                for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (stack.getItem() instanceof ItemEnergyBags) {
                        if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.BAGS, stack)) {
                            ItemEnergyBags bags = (ItemEnergyBags) stack.getItem();
                            ItemStack stack2 = event.getItem().getItem();
                            if (!event.getItem().isRemoved() && !(stack2.getItem() instanceof ItemEnergyBags)) {
                                if (bags.canInsert(player, stack, stack2)) {
                                    ItemStack stack1 = stack2.copy();
                                    stack1.setCount(Math.min(stack1.getCount(), stack1.getMaxStackSize()));
                                    bags.insert(player, stack, stack1);
                                    event.getItem().setItem(stack1);
                                    event.getItem().remove(Entity.RemovalReason.KILLED);
                                    event.setResult(Event.Result.DENY);
                                    event.setCanceled(true);
                                    return;
                                }
                            }
                        }
                    }
                }
            }

            if (isContainerBox) {
                for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (stack.getItem() instanceof ItemLeadBox) {
                        ItemLeadBox bags = (ItemLeadBox) stack.getItem();
                        ItemStack stack2 = event.getItem().getItem();
                        if (!event.getItem().isRemoved() && !(stack2.getItem() instanceof ItemLeadBox)) {
                            if (bags.canInsert(player, stack, stack2)) {
                                ItemStack stack1 = stack2.copy();
                                stack1.setCount(Math.min(stack1.getCount(), stack1.getMaxStackSize()));
                                bags.insert(player, stack, stack1);
                                event.getItem().setItem(stack1);
                                event.getItem().remove(Entity.RemovalReason.KILLED);
                                event.setCanceled(true);
                                return;
                            }
                        }
                    }
                }
            }
        } catch (NoClassDefFoundError ignored) {
        }
    }

    @SubscribeEvent
    public void onCropGrowPre(BlockEvent.CropGrowEvent.Pre event) {
        BlockPos pos = event.getPos();
        Level world = (Level) event.getLevel();
        ChunkPos chunkPos = new ChunkPos(pos);


        final ChunkLevel pollution = PollutionManager.pollutionManager.getChunkLevelSoil(chunkPos);

        if (pollution != null) {
            int pollutionLevel = pollution.getLevelPollution().ordinal();

            if (pollutionLevel >= 4) {
                event.setResult(Event.Result.DENY);
            } else if (pollutionLevel >= 1) {
                int chance = 0;
                if (pollutionLevel == 1) {
                    chance = 25;
                } else if (pollutionLevel == 2) {
                    chance = 50;
                } else {
                    chance = 75;
                }

                if (world.random.nextInt(100) < chance) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public void onCropGrowPre1(BlockEvent.CropGrowEvent.Pre event) {
        BlockPos pos = event.getPos().below();
        Level world = (Level) event.getLevel();
        BlockState state = world.getBlockState(pos);


        if (state.getBlock() == IUItem.humus.getBlock(0)) {
            if (world.random.nextFloat() < 0.5f) {
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public void loginPlayer(PlayerEvent.PlayerLoggedInEvent event) {
        RadiationSystem.rad_system.update(event.getEntity());
        if (event.getEntity().getLevel().isClientSide) {
            return;
        }
        new PacketColorPickerAllLoggIn();
        new PacketRadiationUpdateValue(event.getEntity(), event.getEntity().getPersistentData().getDouble("radiation"));
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            CompoundTag nbt = player.getPersistentData();
            nbt.putDouble("radiation", 0.0);
        }
    }

    public void setFly(Player player, boolean fly, ItemStack stack) {
        player.getAbilities().flying = fly;
        player.getAbilities().mayfly = fly;
        player.getPersistentData().putBoolean("isFlyActive", fly);
        if (player.getLevel().isClientSide && !fly) {
            player.getAbilities().setFlyingSpeed((float) 0.05);
            player.fallDistance = 0;
        } else {
            boolean edit = player.getPersistentData().getBoolean("edit_fly");
            if (!edit) {
                int flyspeed = (UpgradeSystem.system.hasModules(
                        EnumInfoUpgradeModules.FLYSPEED,
                        stack
                ) ?
                        UpgradeSystem.system.getModules(
                                EnumInfoUpgradeModules.FLYSPEED,
                                stack
                        ).number : 0);

                if (player.getLevel().isClientSide) {
                    player.getAbilities().setFlyingSpeed((float) ((float) 0.1 + 0.05 * flyspeed));
                }
            } else {
                if (player.getLevel().isClientSide) {
                    player.getAbilities().setFlyingSpeed(player.getPersistentData().getFloat("fly_speed"));
                }
            }
        }
    }

    public boolean canFly(ItemStack stack) {
        return stack
                .getItem() == IUItem.spectral_chestplate.getItem() ||
                (stack.getItem() instanceof ItemSpecialArmor && (((ItemSpecialArmor) stack.getItem())
                        .getListCapability()
                        .contains(EnumCapability.FLY) || ((ItemSpecialArmor) stack.getItem()).getListCapability().contains(
                        EnumCapability.JETPACK_FLY))) || (stack
                .getItem() instanceof ItemAdvJetpack && UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.FLY, stack));
    }

    @SubscribeEvent
    public void FlyUpdate(LivingEvent.LivingTickEvent event) {

        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        CompoundTag nbtData = player.getPersistentData();
        if (!player.isCreative()) {

            if (!player.getInventory().armor.get(2).isEmpty()) {
                if (canFly(player.getInventory().armor.get(2))) {
                    CompoundTag nbtData1 = ModUtils.nbt(player.getInventory().armor.get(2));
                    boolean jetpack = nbtData1.getBoolean("jetpack");
                    if (!jetpack) {
                        if (nbtData.getBoolean("isFlyActive")) {
                            nbtData.putBoolean("hasFly", true);
                            setFly(player, false, player.getInventory().armor
                                    .get(2));
                        }
                    } else {
                        if (!player.isOnGround()) {
                            if (nbtData1.getBoolean("canFly")) {
                                setFly(player, true, player.getInventory().armor
                                        .get(2));
                                nbtData1.putBoolean("canFly", false);
                                nbtData.putBoolean("canjump", false);
                            }
                        } else {
                            if (nbtData.getBoolean("isFlyActive")) {
                                setFly(player, false, player.getInventory().armor
                                        .get(2));
                            }
                        }
                    }
                } else if (!player.getInventory().armor.get(2).isEmpty()) {
                    if (nbtData.getBoolean("isFlyActive")) {
                        setFly(player, false, player.getInventory().armor
                                .get(2));
                    }
                }
            } else {
                if (nbtData.getBoolean("isFlyActive")) {
                    setFly(player, false, player.getInventory().armor
                            .get(2));
                }
            }
        } else {
            if (nbtData.getBoolean("isFlyActive")) {
                setFly(player, false, player.getInventory().armor
                        .get(2));
            }
        }


    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Level world = event.level;
            if (world.getGameTime() % 20 != 0)
                return;
            Iterable<Entity> iterable = ((LevelInvoker) world).getGetEntities().getAll();
            List<Entity> list = StreamSupport.stream(iterable.spliterator(), false).filter(entity -> entity instanceof ItemEntity)
                    .toList();

            for (Entity entity : list) {
                if (entity instanceof ItemEntity) {
                    ItemEntity itemEntity = (ItemEntity) entity;
                    if (itemEntity.isRemoved()) {
                        continue;
                    }
                   if (   itemEntity.isInWater()) {
                        ItemEntity entityItem = (checkAndTransform(world, itemEntity));
                        if (entityItem != null) {
                            world.addFreshEntity(entityItem);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        WorldData.onWorldUnload((Level) event.getLevel());
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void addInformItem(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (event.getItemStack().isEmpty())
            return;
        Item item = stack.getItem();
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();


        if (item == IUItem.charged_redstone.getItem()) {
            event.getToolTip().add(Component.literal(Localization.translate("charged_redstone.info")));
        }

        if (stack.getItem() instanceof BlockItem) {
            BlockItem blockItem = (BlockItem) stack.getItem();
            BlockState state = blockItem.getBlock().getStateForPlacement(new BlockPlaceContext(player.level, player, player.getUsedItemHand(), stack, new BlockHitResult(new Vec3(0, 0, 0), Direction.UP, BlockPos.ZERO, false)));
            List<Component> list = ModUtils.getInformationFromOre(state);
            if (!list.isEmpty()) {
                event.getToolTip().add(Component.literal(Localization.translate("veins_ores.info1")));
                event.getToolTip().add(Component.literal(Localization.translate("veins_ores.info")));
                event.getToolTip().addAll(list);
            }
        }

        if (item instanceof IRadioactiveItemType || item instanceof ItemNuclearResource) {
            event.getToolTip().add(Component.literal(Localization.translate("iu.radiation.warning")));
            event.getToolTip().add(Component.literal(Localization.translate("iu.radioprotector.info")));
        }

        if (IUItem.tank.contains(stack)) {
            switch (IUItem.tank.getMetaFromItemStack(stack)) {
                case 1:
                    event.getToolTip().add(Component.literal(Localization.translate("iu.storage_fluid") + 160 + " B"));
                    break;
                case 2:
                    event.getToolTip().add(Component.literal(Localization.translate("iu.storage_fluid") + 480 + " B"));
                    break;
                case 3:
                    event.getToolTip().add(Component.literal(Localization.translate("iu.storage_fluid") + 2560 + " B"));
                    break;
                case 0:
                    event.getToolTip().add(Component.literal(Localization.translate("iu.storage_fluid") + 40 + " B"));
                    break;
            }
        }

        for (Map.Entry<ItemStack, Double> entry : TileEntityPalletGenerator.integerMap.entrySet()) {
            if (entry.getKey().is(stack.getItem())) {
                event.getToolTip().add(Component.literal(Localization.translate("iu.pellets.info") + entry.getValue()));
                event.getToolTip().add(Component.literal(Localization.translate("iu.pellets.info1")));
            }
        }

        if (item.equals(IUItem.module_quickly.getItem())) {
            event.getToolTip().add(Component.literal(Localization.translate("iu.info.module.speed")));
        }
        if (item.equals(IUItem.module_stack.getItem())) {
            event.getToolTip().add(Component.literal(Localization.translate("iu.info.module.stack")));
        }
        if (item.equals(IUItem.module_storage.getItem())) {
            event.getToolTip().add(Component.literal(Localization.translate("iu.info.module.sorting")));
        }

        if (item instanceof ItemCraftingElements<?> && ((ItemCraftingElements<?>) item).getElement().getId() >= 206 && ((ItemCraftingElements<?>) item).getElement().getId() <= 216) {

            int meta = ((ItemCraftingElements<?>) item).getElement().getId() - 205;
            event.getToolTip().add(Component.literal(Localization.translate("iu.limiter.info9") + EnergyNetGlobal.instance.getPowerFromTier(meta) + " EF"));
        }


        if ((item instanceof ItemBaseCircuit<?> && (((ItemBaseCircuit<?>) item).getElement().getId() == 9 || ((ItemBaseCircuit<?>) item).getElement().getId() == 10 || ((ItemBaseCircuit<?>) item).getElement().getId() == 11 || ((ItemBaseCircuit<?>) item).getElement().getId() == 21)) ||
                (item instanceof ItemCraftingElements<?> && (((ItemCraftingElements<?>) item).getElement().getId() == 272 || ((ItemCraftingElements<?>) item).getElement().getId() == 273))) {

            final CompoundTag nbt = ModUtils.nbt(stack);
            final int level = nbt.getInt("level");
            if (level != 0) {
                event.getToolTip().add(Component.literal(Localization.translate("circuit.level") + " " + level));
            }
        }

        if (item instanceof ItemAdditionModule<?> && ((ItemAdditionModule<?>) item).getElement().getId() == 9) {
            event.getToolTip().add(Component.literal(Localization.translate("module.wireless")));
        }

        if (item.equals(IUItem.upgrade_speed_creation.getItem()) || item.equals(IUItem.autoheater.getItem()) ||
                (item instanceof ItemCoolingUpgrade<?>) || item.equals(IUItem.module_quickly.getItem()) ||
                item.equals(IUItem.module_stack.getItem()) || item.equals(IUItem.module_storage.getItem()) ||
                (item instanceof ItemAdditionModule<?> && (((ItemAdditionModule<?>) item).getElement().getId() == 4 || ((ItemAdditionModule<?>) item).getElement().getId() == 10))) {
            event.getToolTip().add(Component.literal(Localization.translate("module.wireless")));
        }

        if (item instanceof ItemEntityModule<?>) {
            CapturedMobUtils capturedMobUtils = CapturedMobUtils.create(stack);
            if (capturedMobUtils != null) {
                Entity entity = capturedMobUtils.getEntity(player.level, true);
                event.getToolTip().add(entity.getName());
            }
        }

        if (ListInformationUtils.mechanism_info != null && !ListInformationUtils.mechanism_info.isEmpty())
            if (item.equals(IUItem.module_quickly.getItem()) || item.equals(IUItem.module_stack.getItem()) || item.equals(IUItem.module_storage.getItem())) {
                event.getToolTip().add(Component.literal(Localization.translate("using_kit")));
                if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    event.getToolTip().add(Component.literal(ListInformationUtils.mechanism_info.get(ListInformationUtils.index)));
                } else {
                    for (String name : ListInformationUtils.mechanism_info) {
                        event.getToolTip().add(Component.literal(name));
                    }
                }
            }

        if (item instanceof ItemCoolingUpgrade<?>) {
            event.getToolTip().add(Component.literal(Localization.translate("using_kit")));
            final List<String> list = ListInformationUtils.integerListMap.get(((ItemCoolingUpgrade<?>) item).getElement().getId());
            if (list != null)
                if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    event.getToolTip().add(Component.literal(list.get(ListInformationUtils.index % list.size())));
                } else {
                    for (String name : list) {
                        event.getToolTip().add(Component.literal(name));
                    }
                }
        }

        if (item.equals(IUItem.autoheater.getItem()) && ListInformationUtils.mechanism_info2.size() > 0) {
            event.getToolTip().add(Component.literal(Localization.translate("using_kit")));
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.getToolTip().add(Component.literal(ListInformationUtils.mechanism_info2.get(ListInformationUtils.index2)));
            } else {
                for (String name : ListInformationUtils.mechanism_info2) {
                    event.getToolTip().add(Component.literal(name));
                }
            }
        }

        if (item.equals(IUItem.upgrade_speed_creation.getItem()) && ListInformationUtils.mechanism_info1.size() > 0) {
            event.getToolTip().add(Component.literal(Localization.translate("using_kit")));
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.getToolTip().add(new ArrayList<>(ListInformationUtils.mechanism_info1.values()).get(ListInformationUtils.index1));
            } else {
                for (Component name : ListInformationUtils.mechanism_info1.values()) {
                    event.getToolTip().add(name);
                }
            }
        }

        if (stack.getItem().equals(IUItem.analyzermodule.getItem())) {
            event.getToolTip().add(Component.literal(Localization.translate("iu.analyzermodule")));
        }

        if (stack.getItem().equals(IUItem.quarrymodule.getItem())) {
            event.getToolTip().add(Component.literal(Localization.translate("iu.quarrymodule")));
        }

        if (stack.getItem().equals(IUItem.expmodule.getItem())) {
            event.getToolTip().add(Component.literal(Localization.translate("iu.expierence_module.info")));
        }

        if (stack.getItem() instanceof ItemEntityModule) {
            int meta = ((ItemEntityModule<?>) stack.getItem()).getElement().getId();
            if (meta == 0) {
                event.getToolTip().add(Component.literal(Localization.translate("iu.entitymodule")));
            }
            if (meta == 1) {
                event.getToolTip().add(Component.literal(Localization.translate("iu.entitymodule1")));
            }
        }

        if (stack.getItem() instanceof ItemBaseModules<?>) {
            EnumModule module = EnumModule.getFromID(((ItemBaseModules<?>) stack.getItem()).getElement().getId());
            if (module.type.equals(EnumBaseType.PHASE)) {
                event.getToolTip().add(Component.literal(Localization.translate("iu.phasemodule")));
            }
            if (module.type.equals(EnumBaseType.MOON_LINSE)) {
                event.getToolTip().add(Component.literal(Localization.translate("iu.moonlinse")));
            }
        }

        if (getUpgradeItem(stack) && UpgradeSystem.system.hasInMap(stack)) {
            final List<UpgradeItemInform> lst = UpgradeSystem.system.getInformation(stack);
            final int col = UpgradeSystem.system.getRemaining(stack);
            if (!lst.isEmpty()) {
                for (UpgradeItemInform upgrade : lst) {
                    event.getToolTip().add(Component.literal(upgrade.getName()));
                }
            }
            if (col != 0) {
                event.getToolTip().add(Component.literal(Localization.translate("free_slot") + col + Localization.translate("free_slot1")));
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    event.getToolTip().add(Component.literal(Localization.translate("iu.can_upgrade_item")));
                    IUpgradeItem iUpgradeItem = (IUpgradeItem) stack.getItem();
                    final List<String> list = UpgradeSystem.system.getAvailableUpgrade(iUpgradeItem, stack);
                    list.forEach(s -> event.getToolTip().add(Component.literal(s)));
                }
            } else {
                event.getToolTip().add(Component.literal(Localization.translate("not_free_slot")));
            }
        }

        if (stack.getItem() instanceof IRoversItem && SpaceUpgradeSystem.system.hasInMap(stack)) {
            final List<SpaceUpgradeItemInform> lst = SpaceUpgradeSystem.system.getInformation(stack);
            final int col = SpaceUpgradeSystem.system.getRemaining(stack);
            if (!lst.isEmpty()) {
                for (SpaceUpgradeItemInform upgrade : lst) {
                    event.getToolTip().add(Component.literal(upgrade.getName()));
                }
            }
            if (col != 0) {
                event.getToolTip().add(Component.literal(Localization.translate("free_slot") + col + Localization.translate("free_slot1")));
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    event.getToolTip().add(Component.literal(Localization.translate("iu.can_upgrade_item")));
                    IRoversItem iUpgradeItem = (IRoversItem) stack.getItem();
                    final List<String> list = SpaceUpgradeSystem.system.getAvailableUpgrade(iUpgradeItem, stack);
                    list.forEach(s -> event.getToolTip().add(Component.literal(s)));
                }
            } else {
                event.getToolTip().add(Component.literal(Localization.translate("not_free_slot")));
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void addInfo(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if ( IUItem.machineRecipe == null)
            IUItem.machineRecipe = Recipes.recipes.getRecipeStack("converter");
        if ( IUItem.fluidMatterRecipe == null)
            IUItem.fluidMatterRecipe = Recipes.recipes.getRecipeStack("replicator");
        if (tupleRecipe == null) {
            for (Map.Entry<ItemStack, BaseMachineRecipe> entry : IUItem.machineRecipe) {
                if (entry.getKey().is(stack.getItem())) {
                    tupleRecipe = new Tuple<>(stack, entry.getValue());
                    if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        event.getToolTip().add(Component.nullToEmpty(Localization.translate("press.lshift")));
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        event.getToolTip().add(Component.nullToEmpty(Localization.translate("clonning")));

                        final RecipeOutput output1 = entry.getValue().output;
                        for (int i = 0; i < this.name.length; i++) {
                            if (output1.metadata.getDouble(("quantitysolid_" + i)) != 0) {
                                event
                                        .getToolTip()
                                        .add(Component.nullToEmpty(name[i] + Localization.translate(mattertype[i]) + ": " + output1.metadata.getDouble(
                                                ("quantitysolid_" + i)) + Localization.translate("matternumber")));
                            }
                        }
                    }
                    break;
                }
            }
            if (tupleRecipe == null) {
                tupleRecipe = new Tuple<>(stack, null);
            }
        } else {
            if (tupleRecipe.getA().is(stack.getItem())) {
                if (tupleRecipe.getB() != null) {
                    if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        event.getToolTip().add(Component.nullToEmpty(Localization.translate("press.lshift")));
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        event.getToolTip().add(Component.nullToEmpty(Localization.translate("clonning")));

                        final RecipeOutput output1 = tupleRecipe.getB().output;
                        for (int i = 0; i < this.name.length; i++) {
                            if (output1.metadata.getDouble(("quantitysolid_" + i)) != 0) {
                                event
                                        .getToolTip()
                                        .add(Component.nullToEmpty(name[i] + Localization.translate(mattertype[i]) + ": " + output1.metadata.getDouble(
                                                ("quantitysolid_" + i)) + Localization.translate("matternumber")));
                            }
                        }
                    }
                }
            } else {
                boolean find = false;
                for (Map.Entry<ItemStack, BaseMachineRecipe> entry : IUItem.machineRecipe) {
                    if (entry.getKey().is(stack.getItem())) {
                        tupleRecipe = new Tuple<>(stack, entry.getValue());
                        find = true;
                        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                            event.getToolTip().add(Component.nullToEmpty(Localization.translate("press.lshift")));
                        }
                        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                            event.getToolTip().add(Component.nullToEmpty(Localization.translate("clonning")));

                            final RecipeOutput output1 = entry.getValue().output;
                            for (int i = 0; i < this.name.length; i++) {
                                if (output1.metadata.getDouble(("quantitysolid_" + i)) != 0) {
                                    event
                                            .getToolTip()
                                            .add(Component.nullToEmpty(name[i] + Localization.translate(mattertype[i]) + ": " + output1.metadata.getDouble(
                                                    ("quantitysolid_" + i)) + Localization.translate("matternumber")));
                                }
                            }
                        }
                        break;
                    }
                }
                if (!find) {
                    tupleRecipe = new Tuple<>(stack, null);
                }
            }
        }
        if (tupleReplicatorRecipe == null) {
            for (Map.Entry<ItemStack, BaseMachineRecipe> entry : IUItem.fluidMatterRecipe) {
                if (entry.getKey().is(stack.getItem())) {
                    if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        if (!event.getToolTip().contains(Component.nullToEmpty(Localization.translate("press.lshift")))) {
                            event.getToolTip().add(Component.nullToEmpty(Localization.translate("press.lshift")));
                        }
                    }
                    tupleReplicatorRecipe = new Tuple<>(stack, entry.getValue());
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {

                        final RecipeOutput output1 = entry.getValue().output;
                        final double matter = output1.metadata.getDouble("matter");
                        String usingMatter = ModUtils.getStringBukket(matter) + Localization.translate(Constants.ABBREVIATION +
                                ".generic.text.bucketUnit");
                        event
                                .getToolTip()
                                .add(Component.nullToEmpty(Localization.translate(Constants.ABBREVIATION + ".replicator_using_matter") + ChatFormatting.DARK_PURPLE + usingMatter));

                    }
                    break;
                }
            }
            if (tupleReplicatorRecipe == null) {
                tupleReplicatorRecipe = new Tuple<>(stack, null);
            }
        } else {
            if (tupleReplicatorRecipe.getA().is(stack.getItem())) {
                if (tupleReplicatorRecipe.getB() != null) {
                    if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        if (!event.getToolTip().contains(Component.nullToEmpty(Localization.translate("press.lshift")))) {
                            event.getToolTip().add(Component.nullToEmpty(Localization.translate("press.lshift")));
                        }
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {

                        final RecipeOutput output1 = tupleReplicatorRecipe.getB().output;
                        final double matter = output1.metadata.getDouble("matter");
                        String usingMatter = ModUtils.getStringBukket(matter) + Localization.translate(Constants.ABBREVIATION +
                                ".generic.text.bucketUnit");
                        event
                                .getToolTip()
                                .add(Component.nullToEmpty(Localization.translate(Constants.ABBREVIATION + ".replicator_using_matter") + ChatFormatting.DARK_PURPLE + usingMatter));

                    }
                }
            } else {
                tupleReplicatorRecipe = null;
                for (Map.Entry<ItemStack, BaseMachineRecipe> entry : IUItem.fluidMatterRecipe) {
                    if (entry.getKey().is(stack.getItem())) {
                        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                            if (!event.getToolTip().contains(Component.nullToEmpty(Localization.translate("press.lshift")))) {
                                event.getToolTip().add(Component.nullToEmpty(Localization.translate("press.lshift")));
                            }
                        }
                        tupleReplicatorRecipe = new Tuple<>(stack, entry.getValue());
                        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {

                            final RecipeOutput output1 = entry.getValue().output;
                            final double matter = output1.metadata.getDouble("matter");
                            String usingMatter = ModUtils.getStringBukket(matter) + Localization.translate(Constants.ABBREVIATION +
                                    ".generic.text.bucketUnit");
                            event
                                    .getToolTip()
                                    .add(Component.nullToEmpty(Localization.translate(Constants.ABBREVIATION + ".replicator_using_matter") + ChatFormatting.DARK_PURPLE + usingMatter));

                        }
                        break;
                    }
                }
                if (tupleReplicatorRecipe == null) {
                    tupleReplicatorRecipe = new Tuple<>(stack, null);
                }
            }
        }

    }

    private ItemEntity checkAndTransform(Level world, ItemEntity entityItem) {
        LevelEntityGetter<Entity> list1 = ((LevelInvoker) world).getGetEntities();
        List<ItemEntity> nearbyItems = Lists.newArrayList();
        list1.get(new AABB(entityItem.getX() - 1, entityItem.getY() - 1, entityItem.getZ() - 1,
                entityItem.getX() + 1, entityItem.getY() + 1, entityItem.getZ() + 1
        ), (p_151522_) -> {
            if (p_151522_ instanceof ItemEntity) {
                nearbyItems.add((ItemEntity) p_151522_);
            }
        });
        int redstoneNeeded = 4;
        int poloniumNeeded = 1;
        int electrumNeeded = 1;

        List<ItemEntity> redstoneItems = new LinkedList<>();
        List<ItemEntity> poloniumItems = new LinkedList<>();
        List<ItemEntity> electrumItems = new LinkedList<>();


        for (ItemEntity item : nearbyItems) {
            if (item.isRemoved()) {
                continue;
            }
            ItemStack stack = item.getItem();

            if (stack.getItem() == Items.REDSTONE && redstoneNeeded > 0) {
                redstoneItems.add(item);
                redstoneNeeded -= stack.getCount();
            } else if (stack.is(IUItem.iudust.getStack(55)) && poloniumNeeded > 0) {
                poloniumItems.add(item);
                poloniumNeeded -= stack.getCount();
            } else if (stack.is(IUItem.iuingot.getStack(13)) && electrumNeeded > 0) {
                electrumItems.add(item);
                electrumNeeded -= stack.getCount();
            }
        }


        if (redstoneNeeded <= 0 && poloniumNeeded <= 0 && electrumNeeded <= 0) {
            int remainingRedstone = 4;
            for (ItemEntity item : redstoneItems) {
                ItemStack stack = item.getItem();
                if (stack.getCount() <= remainingRedstone) {
                    remainingRedstone -= stack.getCount();
                    item.setRemoved(Entity.RemovalReason.KILLED);
                } else {
                    stack.shrink(remainingRedstone);
                    break;
                }
            }


            int remainingPolonium = 1;
            for (ItemEntity item : poloniumItems) {
                ItemStack stack = item.getItem();
                if (stack.getCount() <= remainingPolonium) {
                    remainingPolonium -= stack.getCount();
                    item.setRemoved(Entity.RemovalReason.KILLED);
                } else {
                    stack.shrink(remainingPolonium);
                    break;
                }
            }


            int remainingElectrum = 1;
            for (ItemEntity item : electrumItems) {
                ItemStack stack = item.getItem();
                if (stack.getCount() <= remainingElectrum) {
                    remainingElectrum -= stack.getCount();
                    item.setRemoved(Entity.RemovalReason.KILLED);
                } else {
                    stack.shrink(remainingElectrum);
                    break;
                }
            }


            ItemStack chargedRedstone = new ItemStack(IUItem.charged_redstone.getItem());
            return new ItemEntity(world, entityItem.getX(), entityItem.getY(), entityItem.getZ(), chargedRedstone);
        }

        return null;
    }
}
