package com.denfop.items.space.teleport;

import com.denfop.Constants;
import com.denfop.api.space.EnumLevels;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.dimension.SpaceDimensionKeys;
import com.denfop.api.space.fakebody.Data;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.items.space.ItemPlanetaryTranslocator;
import com.denfop.network.packet.PacketSpaceTeleportFx;
import com.denfop.network.packet.PacketSpaceTeleportStateSync;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.Localization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = Constants.MOD_ID)
public final class SpaceTeleportController {


    public static final double TELEPORT_START_COST = 60_000D;
    public static final double CRITICAL_CHARGE = 150_000D;

    public static final int OUTBOUND_PREP_TICKS = 42;
    public static final int RETURN_PREP_TICKS = 36;
    public static final int EMERGENCY_PREP_TICKS = 52;
    public static final int SAFE_SEARCH_RADIUS = 32;

    private SpaceTeleportController() {
    }

    public static void ensureItemUuid(final net.minecraft.world.item.ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        if (!stack.has(DataComponentsInit.UUID_STRING)) {
            stack.set(DataComponentsInit.UUID_STRING, UUID.randomUUID().toString());
        }
    }

    public static SpaceTeleportScreenData buildScreenData(
            final ServerPlayer player,
            final net.minecraft.world.item.ItemStack openedStack
    ) {
        Map<IBody, Data> researchMap = SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(player.getUUID());

        java.util.List<SpaceTeleportScreenData.Entry> entries = new java.util.ArrayList<>();
        java.util.List<com.denfop.api.space.ISystem> systems = new java.util.ArrayList<>(SpaceNet.instance.getSystem());
        systems.sort(java.util.Comparator.comparing(com.denfop.api.space.ISystem::getName));

        for (com.denfop.api.space.ISystem system : systems) {
            if (system.getStarList() == null || system.getStarList().isEmpty()) {
                continue;
            }

            String systemTitle = prettifySystemName(system.getName());
            entries.add(new SpaceTeleportScreenData.Entry(
                    "",
                    systemTitle,
                    0D,
                    false,
                    false,
                    false,
                    0,
                    true
            ));

            java.util.List<com.denfop.api.space.IPlanet> planets = new java.util.ArrayList<>();
            java.util.List<com.denfop.api.space.IAsteroid> asteroids = new java.util.ArrayList<>();

            system.getStarList().forEach(star -> {
                if (star.getPlanetList() != null) {
                    planets.addAll(star.getPlanetList());
                }
                if (star.getAsteroidList() != null) {
                    asteroids.addAll(star.getAsteroidList());
                }
            });

            planets.sort(java.util.Comparator.comparingDouble(IBody::getDistance));
            asteroids.sort(java.util.Comparator.comparingDouble(IBody::getDistance));

            for (com.denfop.api.space.IPlanet planet : planets) {
                entries.add(buildBodyEntry(player, researchMap, planet, 0));

                java.util.List<com.denfop.api.space.ISatellite> satellites = new java.util.ArrayList<>(planet.getSatelliteList());
                satellites.sort(java.util.Comparator.comparingDouble(com.denfop.api.space.ISatellite::getDistanceFromPlanet));
                for (com.denfop.api.space.ISatellite satellite : satellites) {
                    entries.add(buildBodyEntry(player, researchMap, satellite, 1));
                }
            }

            for (com.denfop.api.space.IAsteroid asteroid : asteroids) {
                entries.add(buildBodyEntry(player, researchMap, asteroid, 0));
            }
        }

        net.minecraft.world.item.ItemStack tracked = openedStack;
        if (tracked == null || tracked.isEmpty() || !(tracked.getItem() instanceof ItemPlanetaryTranslocator)) {
            tracked = findAnyTranslocator(player);
        }

        double charge = tracked == null || tracked.isEmpty() ? 0D : ElectricItem.manager.getCharge(tracked);
        double maxCharge = tracked == null || tracked.isEmpty() ? 0D : ElectricItem.manager.getMaxCharge(tracked);

        SpaceTeleportSession session = loadSession(player);

        long countdown = session.isActive() ? session.getRemainingTicks(player.level().getGameTime()) : 0L;
        long critical = getTicksUntilCritical(player, session);
        boolean itemPresent = session.isActive()
                ? !findTrackedItem(player, session.trackedItemUuid).isEmpty()
                : tracked != null && !tracked.isEmpty();

        String selectedBody = "";
        if (session.isActive() && !session.targetBodyName.isEmpty()) {
            selectedBody = session.targetBodyName;
        } else {
            for (SpaceTeleportScreenData.Entry entry : entries) {
                if (entry.isBodyEntry() && entry.canTeleport) {
                    selectedBody = entry.bodyName;
                    break;
                }
            }
            if (selectedBody.isEmpty()) {
                for (SpaceTeleportScreenData.Entry entry : entries) {
                    if (entry.isBodyEntry()) {
                        selectedBody = entry.bodyName;
                        break;
                    }
                }
            }
        }

        return new SpaceTeleportScreenData(
                entries,
                selectedBody,
                charge,
                maxCharge,
                session.isActive(),
                session.phase,
                session.targetBodyName,
                critical,
                session.phase == SpaceTeleportPhase.RETURN_PREP ? countdown : 0L,
                session.reason,
                itemPresent
        );
    }

    private static SpaceTeleportScreenData.Entry buildBodyEntry(
            final ServerPlayer player,
            final Map<IBody, Data> researchMap,
            final IBody body,
            final int indent
    ) {
        Data data = researchMap == null ? null : researchMap.get(body);
        double percent = data == null ? 0D : data.getPercent();
        boolean dimensionExists = player.server.getLevel(SpaceDimensionKeys.levelKey(body.getName())) != null;
        EnumLevels level = resolveLevel(body);

        boolean canTeleport = level != EnumLevels.NONE && percent >= 100D && dimensionExists;

        return new SpaceTeleportScreenData.Entry(
                body.getName(),
                "",
                percent,
                dimensionExists,
                canTeleport,
                true,
                indent,
                false
        );
    }

    private static EnumLevels resolveLevel(final IBody body) {
        if (body instanceof com.denfop.api.space.IPlanet planet) {
            return planet.getLevels();
        }
        if (body instanceof com.denfop.api.space.ISatellite satellite) {
            return satellite.getLevels();
        }
        if (body instanceof com.denfop.api.space.IAsteroid asteroid) {
            return asteroid.getLevels();
        }
        return EnumLevels.NONE;
    }

    private static String prettifySystemName(final String name) {
        if (name == null || name.isEmpty()) {
            return "System";
        }
        String value = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        return value.replace("system", "").trim();
    }

    public static void requestTeleport(final ServerPlayer player, final String bodyName) {
        if (player == null || bodyName == null || bodyName.isEmpty()) {
            return;
        }

        SpaceTeleportSession current = loadSession(player);
        if (current.isActive()) {
            message(player, "iu.space.tp.already_active");
            sendSync(player);
            return;
        }

        IBody body = SpaceNet.instance.getBodyFromName(bodyName);
        if (body == null) {
            message(player, "iu.space.tp.invalid_target");
            return;
        }

        if (resolveLevel(body) == EnumLevels.NONE) {
            message(player, "iu.space.tp.invalid_target");
            return;
        }

        Map<IBody, Data> researchMap = SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(player.getUUID());
        Data data = researchMap == null ? null : researchMap.get(body);
        if (data == null || data.getPercent() < 100D) {
            message(player, "iu.space.tp.need_full_research");
            return;
        }

        ServerLevel targetLevel = player.server.getLevel(SpaceDimensionKeys.levelKey(body.getName()));
        if (targetLevel == null) {
            message(player, "iu.space.tp.dimension_missing");
            return;
        }

        net.minecraft.world.item.ItemStack stack = findAnyTranslocator(player);
        if (stack.isEmpty()) {
            message(player, "iu.space.tp.item_missing");
            return;
        }

        ensureItemUuid(stack);
        double cost = SpaceTeleportEnergyLogic.getTeleportCost(body, TELEPORT_START_COST);

        if (!ElectricItem.manager.canUse(stack, cost)) {
            message(player, "iu.space.tp.not_enough_energy");
            return;
        }

        ElectricItem.manager.discharge(stack, cost, 14, true, false, false);

        SpaceTeleportSession session = new SpaceTeleportSession();
        session.trackedItemUuid = stack.getOrDefault(DataComponentsInit.UUID_STRING, "");
        session.targetBodyName = body.getName();
        session.originDimension = player.level().dimension().location().toString();
        session.originX = player.getX();
        session.originY = player.getY();
        session.originZ = player.getZ();
        session.originYaw = player.getYRot();
        session.originPitch = player.getXRot();
        session.phase = SpaceTeleportPhase.OUTBOUND_PREP;
        session.reason = SpaceTeleportReason.NONE;
        session.phaseEndGameTime = player.level().getGameTime() + OUTBOUND_PREP_TICKS;

        saveSession(player, session);
        new PacketSpaceTeleportFx(player, SpaceTeleportFxType.OUTBOUND_CHARGE, OUTBOUND_PREP_TICKS, body.getName(), false);
        sendSync(player);
        message(player, "iu.space.tp.preparing");
    }

    public static void requestReturn(final ServerPlayer player) {
        if (player == null) {
            return;
        }

        SpaceTeleportSession session = loadSession(player);
        if (!session.isActive() || session.phase != SpaceTeleportPhase.ACTIVE) {
            sendSync(player);
            return;
        }

        session.phase = SpaceTeleportPhase.RETURN_PREP;
        session.reason = SpaceTeleportReason.MANUAL;
        session.phaseEndGameTime = player.level().getGameTime() + RETURN_PREP_TICKS;
        saveSession(player, session);

        new PacketSpaceTeleportFx(player, SpaceTeleportFxType.RETURN_CHARGE, RETURN_PREP_TICKS, session.targetBodyName, true);
        sendSync(player);
        message(player, "iu.space.tp.return_started");
    }

    private static void beginEmergencyReturn(
            final ServerPlayer player,
            final SpaceTeleportSession session,
            final SpaceTeleportReason reason
    ) {
        if (session.phase == SpaceTeleportPhase.RETURN_PREP) {
            return;
        }
        session.phase = SpaceTeleportPhase.RETURN_PREP;
        session.reason = reason;
        session.phaseEndGameTime = player.level().getGameTime() + EMERGENCY_PREP_TICKS;
        saveSession(player, session);
        new PacketSpaceTeleportFx(player, SpaceTeleportFxType.RETURN_CHARGE, EMERGENCY_PREP_TICKS, session.targetBodyName, true);
        sendSync(player);
    }

    private static void finishOutbound(final ServerPlayer player, final SpaceTeleportSession session) {
        net.minecraft.world.item.ItemStack tracked = findTrackedItem(player, session.trackedItemUuid);
        if (tracked.isEmpty()) {
            clearSession(player);
            sendSync(player);
            message(player, "iu.space.tp.item_missing");
            return;
        }

        IBody body = SpaceNet.instance.getBodyFromName(session.targetBodyName);
        if (body == null || resolveLevel(body) == EnumLevels.NONE) {
            clearSession(player);
            sendSync(player);
            message(player, "iu.space.tp.invalid_target");
            return;
        }

        ServerLevel targetLevel = player.server.getLevel(SpaceDimensionKeys.levelKey(body.getName()));
        if (targetLevel == null) {
            clearSession(player);
            sendSync(player);
            message(player, "iu.space.tp.dimension_missing");
            return;
        }

        BlockPosLike desired = new BlockPosLike(
                targetLevel.getSharedSpawnPos().getX(),
                targetLevel.getSharedSpawnPos().getY(),
                targetLevel.getSharedSpawnPos().getZ()
        );

        net.minecraft.core.BlockPos safe = SpaceTeleportSafePositionFinder.findNearestSafe(
                targetLevel,
                new net.minecraft.core.BlockPos(desired.x, desired.y, desired.z),
                SAFE_SEARCH_RADIUS
        );

        new PacketSpaceTeleportFx(player, SpaceTeleportFxType.OUTBOUND_TUNNEL, 14, body.getName(), false);
        player.teleportTo(targetLevel, safe.getX() + 0.5D, safe.getY(), safe.getZ() + 0.5D, session.originYaw, session.originPitch);
        player.fallDistance = 0F;

        session.phase = SpaceTeleportPhase.ACTIVE;
        session.reason = SpaceTeleportReason.NONE;
        session.phaseEndGameTime = 0L;
        saveSession(player, session);

        new PacketSpaceTeleportFx(player, SpaceTeleportFxType.ARRIVAL, 34, body.getName(), false);
        sendSync(player);
        message(player, "iu.space.tp.arrived");
    }

    private static void finishReturn(final ServerPlayer player, final SpaceTeleportSession session) {
        ResourceKey<Level> originKey = ResourceKey.create(
                net.minecraft.core.registries.Registries.DIMENSION,
                ResourceLocation.parse(session.originDimension)
        );

        ServerLevel originLevel = player.server.getLevel(originKey);
        if (originLevel == null) {
            originLevel = player.server.getLevel(Level.OVERWORLD);
        }
        if (originLevel == null) {
            clearSession(player);
            sendSync(player);
            return;
        }

        net.minecraft.core.BlockPos originPos = new net.minecraft.core.BlockPos(
                (int) Math.floor(session.originX),
                (int) Math.floor(session.originY),
                (int) Math.floor(session.originZ)
        );

        net.minecraft.core.BlockPos safe = SpaceTeleportSafePositionFinder.findNearestSafe(
                originLevel,
                originPos,
                SAFE_SEARCH_RADIUS
        );

        new PacketSpaceTeleportFx(player, SpaceTeleportFxType.RETURN_TUNNEL, 14, session.targetBodyName, true);
        player.teleportTo(originLevel, safe.getX() + 0.5D, safe.getY(), safe.getZ() + 0.5D, session.originYaw, session.originPitch);
        player.fallDistance = 0F;

        clearSession(player);
        new PacketSpaceTeleportFx(player, SpaceTeleportFxType.RETURN_ARRIVAL, 34, session.targetBodyName, true);
        sendSync(player);
        message(player, "iu.space.tp.returned_home");
    }

    private static long getTicksUntilCritical(final ServerPlayer player, final SpaceTeleportSession session) {
        if (!session.isActive() || session.phase != SpaceTeleportPhase.ACTIVE) {
            return 0L;
        }

        net.minecraft.world.item.ItemStack tracked = findTrackedItem(player, session.trackedItemUuid);
        if (tracked.isEmpty()) {
            return 0L;
        }

        IBody body = SpaceNet.instance.getBodyFromName(session.targetBodyName);
        double charge = ElectricItem.manager.getCharge(tracked);
        double maxCharge = ElectricItem.manager.getMaxCharge(tracked);

        return SpaceTeleportEnergyLogic.estimateRemainingTicks(
                charge,
                body,
                maxCharge,
                CRITICAL_CHARGE
        );
    }

    private static net.minecraft.world.item.ItemStack findAnyTranslocator(final Player player) {
        Inventory inv = player.getInventory();

        for (net.minecraft.world.item.ItemStack stack : inv.items) {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemPlanetaryTranslocator) {
                return stack;
            }
        }

        for (net.minecraft.world.item.ItemStack stack : inv.offhand) {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemPlanetaryTranslocator) {
                return stack;
            }
        }

        return net.minecraft.world.item.ItemStack.EMPTY;
    }

    private static net.minecraft.world.item.ItemStack findTrackedItem(final Player player, final String itemUuid) {
        if (itemUuid == null || itemUuid.isEmpty()) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }

        Inventory inv = player.getInventory();

        for (net.minecraft.world.item.ItemStack stack : inv.items) {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemPlanetaryTranslocator) {
                String tag = stack.getOrDefault(DataComponentsInit.UUID_STRING, "");
                if (!tag.isEmpty() && itemUuid.equals(tag)) {
                    return stack;
                }
            }
        }

        for (net.minecraft.world.item.ItemStack stack : inv.offhand) {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemPlanetaryTranslocator) {
                String tag = stack.getOrDefault(DataComponentsInit.UUID_STRING, "");
                if (!tag.isEmpty() && itemUuid.equals(tag)) {
                    return stack;
                }
            }
        }

        return net.minecraft.world.item.ItemStack.EMPTY;
    }

    public static SpaceTeleportSession loadSession(final ServerPlayer player) {
        CompoundTag root = player.getPersistentData();
        if (!root.contains(SpaceTeleportSession.TAG_NAME)) {
            return new SpaceTeleportSession();
        }

        CompoundTag tag = root.getCompound(SpaceTeleportSession.TAG_NAME);
        return new SpaceTeleportSession(tag);
    }

    public static void saveSession(final ServerPlayer player, final SpaceTeleportSession session) {
        player.getPersistentData().put(SpaceTeleportSession.TAG_NAME, session.write());
    }

    public static void clearSession(final ServerPlayer player) {
        player.getPersistentData().remove(SpaceTeleportSession.TAG_NAME);
    }

    private static void sendSync(final ServerPlayer player) {
        SpaceTeleportSession session = loadSession(player);
        net.minecraft.world.item.ItemStack tracked = session.isActive()
                ? findTrackedItem(player, session.trackedItemUuid)
                : findAnyTranslocator(player);

        double charge = tracked.isEmpty() ? 0D : ElectricItem.manager.getCharge(tracked);
        double maxCharge = tracked.isEmpty() ? 0D : ElectricItem.manager.getMaxCharge(tracked);
        long ticksUntilCritical = getTicksUntilCritical(player, session);
        long countdownTicks = session.isActive() ? session.getRemainingTicks(player.level().getGameTime()) : 0L;
        boolean itemPresent = !tracked.isEmpty();

        new PacketSpaceTeleportStateSync(
                player,
                session.isActive(),
                session.phase,
                session.targetBodyName,
                ticksUntilCritical,
                session.phase == SpaceTeleportPhase.RETURN_PREP ? countdownTicks : 0L,
                charge,
                maxCharge,
                session.reason,
                itemPresent
        );
    }

    private static void message(final ServerPlayer player, final String key) {
        player.displayClientMessage(net.minecraft.network.chat.Component.literal(Localization.translate(key)), true);
    }

    @SubscribeEvent
    public static void onPlayerTick(final PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        SpaceTeleportSession session = loadSession(player);
        if (!session.isActive()) {
            return;
        }

        long gameTime = player.level().getGameTime();

        if (session.phase == SpaceTeleportPhase.OUTBOUND_PREP) {
            if (gameTime >= session.phaseEndGameTime) {
                finishOutbound(player, session);
            } else if (player.tickCount % 10 == 0) {
                sendSync(player);
            }
            return;
        }

        if (session.phase == SpaceTeleportPhase.ACTIVE) {
            if (player.tickCount % 20 == 0) {
                net.minecraft.world.item.ItemStack tracked = findTrackedItem(player, session.trackedItemUuid);
                if (tracked.isEmpty()) {
                    beginEmergencyReturn(player, session, SpaceTeleportReason.ITEM_LOST);
                    return;
                }

                IBody body = SpaceNet.instance.getBodyFromName(session.targetBodyName);
                double maxCharge = ElectricItem.manager.getMaxCharge(tracked);
                double drainPerSecond = SpaceTeleportEnergyLogic.getActiveDrainPerSecond(
                        body,
                        maxCharge,
                        CRITICAL_CHARGE
                );

                if (!ElectricItem.manager.canUse(tracked, drainPerSecond)) {
                    beginEmergencyReturn(player, session, SpaceTeleportReason.LOW_CHARGE);
                    return;
                }

                ElectricItem.manager.discharge(tracked, drainPerSecond, Integer.MAX_VALUE, true, false, false);

                double charge = ElectricItem.manager.getCharge(tracked);
                if (charge <= CRITICAL_CHARGE) {
                    beginEmergencyReturn(player, session, SpaceTeleportReason.LOW_CHARGE);
                    return;
                }
            }

            if (player.tickCount % 10 == 0) {
                sendSync(player);
            }
            return;
        }

        if (session.phase == SpaceTeleportPhase.RETURN_PREP) {
            if (gameTime >= session.phaseEndGameTime) {
                finishReturn(player, session);
            } else if (player.tickCount % 5 == 0) {
                sendSync(player);
            }
        }
    }

    @SubscribeEvent
    public static void onClone(final PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer newPlayer && event.getOriginal() instanceof ServerPlayer oldPlayer) {
            CompoundTag oldData = oldPlayer.getPersistentData().getCompound(SpaceTeleportSession.TAG_NAME);
            if (!oldData.isEmpty()) {
                newPlayer.getPersistentData().put(SpaceTeleportSession.TAG_NAME, oldData.copy());
            }
        }
    }

    @SubscribeEvent
    public static void onRespawn(final PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SpaceTeleportSession session = loadSession(player);
            if (session.isActive()) {
                session.reason = SpaceTeleportReason.LOGIN_RECOVERY;
                finishReturn(player, session);
            } else {
                sendSync(player);
            }
        }
    }

    @SubscribeEvent
    public static void onLogin(final PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            sendSync(player);
        }
    }

    private record BlockPosLike(int x, int y, int z) {
    }
}