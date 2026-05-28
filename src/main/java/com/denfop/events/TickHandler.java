package com.denfop.events;

import com.denfop.IUItem;
import com.denfop.api.item.upgrade.UpgradeItemInform;
import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.api.pollution.radiation.Radiation;
import com.denfop.api.pollution.radiation.RadiationSystem;
import com.denfop.api.storage.StorageStack;
import com.denfop.api.storage.cell.ItemStackCell;
import com.denfop.api.storage.cell.TypeCell;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.IUpgradeItem;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.*;
import com.denfop.blocks.blockitem.*;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.energy.instruments.EnumOperations;
import com.denfop.items.energy.instruments.ItemEnergyInstruments;
import com.denfop.items.storage.ItemCell;
import com.denfop.items.storage.ItemGridTooltipComponent;
import com.denfop.mixin.invoker.ParticleInvoker;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.*;

import static com.denfop.items.ItemVeinSensor.dataColors;
import static com.denfop.render.base.RenderType.LEASH;

public class TickHandler {
    final int latitudeSegments = 4;
    final int longitudeSegments = 4;
    double[] sinLat = new double[latitudeSegments + 1];
    double[] cosLat = new double[latitudeSegments + 1];
    double[] sinLng = new double[longitudeSegments + 1];
    double[] cosLng = new double[longitudeSegments + 1];
    boolean write = false;
    double[][] x1 = new double[latitudeSegments][longitudeSegments];
    double[][] x2 = new double[latitudeSegments][longitudeSegments];
    double[][] x3 = new double[latitudeSegments][longitudeSegments];
    double[][] x4 = new double[latitudeSegments][longitudeSegments];
    double[][] y1 = new double[latitudeSegments][longitudeSegments];
    double[][] y2 = new double[latitudeSegments][longitudeSegments];
    double[][] y3 = new double[latitudeSegments][longitudeSegments];
    double[][] y4 = new double[latitudeSegments][longitudeSegments];
    double[][] z1 = new double[latitudeSegments][longitudeSegments];
    double[][] z2 = new double[latitudeSegments][longitudeSegments];
    double[][] z3 = new double[latitudeSegments][longitudeSegments];
    double[][] z4 = new double[latitudeSegments][longitudeSegments];
    Set<UpgradableProperty> set = EnumSet.of(UpgradableProperty.FluidExtract, UpgradableProperty.FluidInput,
            UpgradableProperty.ItemInput, UpgradableProperty.ItemExtract);

    public TickHandler() {
        NeoForge.EVENT_BUS.register(this);
    }

    protected static BlockHitResult getPlayerPOVHitResult(Level p_41436_, Player p_41437_, ClipContext.Fluid p_41438_) {
        Vec3 vec3 = p_41437_.getEyePosition();
        Vec3 vec31 = vec3.add(p_41437_.calculateViewVector(p_41437_.getXRot(), p_41437_.getYRot()).scale(p_41437_.blockInteractionRange()));
        return p_41436_.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, p_41438_, p_41437_));
    }

    private static int countTotalStacks(Map<String, List<StorageStack>> storageMap, ItemStack[] allStacks) {
        int count = 0;

        for (List<StorageStack> list : storageMap.values()) {
            for (StorageStack storageStack : list) {
                if (storageStack == null) {
                    continue;
                }
                if (storageStack.getCount() <= 0) {
                    continue;
                }
                int slot = storageStack.getSlot();
                if (slot < 0 || slot >= allStacks.length) {
                    continue;
                }
                ItemStack stored = allStacks[slot];
                if (stored == null || stored.isEmpty()) {
                    continue;
                }
                count++;
            }
        }

        return count;
    }

    private static TagKey<Block> oreTag(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "ores/" + name));
    }

    private static boolean hasOreTag(BlockState state, String name) {
        return state.is(oreTag(name));
    }

    public int getOreColor(BlockState state) {
        if (dataColors.containsKey(state)) {
            return dataColors.get(state);
        }
        if (state.is(BlockTags.IRON_ORES) || hasOreTag(state, "iron")) {
            return ModUtils.convertRGBcolorToInt(156, 156, 156);
        } else if (state.is(BlockTags.GOLD_ORES) || hasOreTag(state, "gold")) {
            return 0xFFFFD700;
        } else if (state.is(BlockTags.DIAMOND_ORES) || hasOreTag(state, "diamond")) {
            return 0xFF00FFFF;
        } else if (state.is(BlockTags.LAPIS_ORES) || hasOreTag(state, "lapis")) {
            return ModUtils.convertRGBcolorToInt(30, 50, 173);
        } else if (state.is(BlockTags.REDSTONE_ORES) || hasOreTag(state, "redstone")) {
            return ModUtils.convertRGBcolorToInt(173, 30, 30);
        } else if (state.is(BlockTags.COPPER_ORES) || hasOreTag(state, "copper")) {
            return ModUtils.convertRGBcolorToInt(255, 144, 0);
        } else if (state.is(BlockTags.COAL_ORES) || hasOreTag(state, "coal")) {
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (state.is(BlockTags.EMERALD_ORES) || hasOreTag(state, "emerald")) {
            return ModUtils.convertRGBcolorToInt(0, 232, 0);
        }
        if (hasOreTag(state, "thorium")) {
            return ModUtils.convertRGBcolorToInt(134, 134, 139);
        }
        if (hasOreTag(state, "tin")) {
            return ModUtils.convertRGBcolorToInt(223, 223, 223);
        }
        if (hasOreTag(state, "lead")) {
            return ModUtils.convertRGBcolorToInt(168, 176, 150);
        }
        if (hasOreTag(state, "uranium")) {
            return ModUtils.convertRGBcolorToInt(89, 158, 73);
        }
        if (hasOreTag(state, "americium")) {
            return ModUtils.convertRGBcolorToInt(120, 152, 183);
        }
        if (hasOreTag(state, "neptunium")) {
            return ModUtils.convertRGBcolorToInt(97, 109, 88);
        }
        if (hasOreTag(state, "curium")) {
            return ModUtils.convertRGBcolorToInt(150, 166, 148);
        }
        if (hasOreTag(state, "ruby")) {
            return ModUtils.convertRGBcolorToInt(251, 140, 119);
        }
        if (hasOreTag(state, "sapphire")) {
            return ModUtils.convertRGBcolorToInt(38, 60, 143);
        }
        if (hasOreTag(state, "topaz")) {
            return ModUtils.convertRGBcolorToInt(204, 180, 47);
        }
        if (hasOreTag(state, "quartz")) {
            return ModUtils.convertRGBcolorToInt(223, 223, 223);
        }
        if (hasOreTag(state, "mikhail")) {
            return ModUtils.convertRGBcolorToInt(119, 210, 202);
        }
        if (hasOreTag(state, "aluminium")) {
            return ModUtils.convertRGBcolorToInt(108, 74, 108);
        }
        if (hasOreTag(state, "vanadium")) {
            return ModUtils.convertRGBcolorToInt(142, 240, 216);
        }
        if (hasOreTag(state, "tungsten")) {
            return ModUtils.convertRGBcolorToInt(199, 199, 199);
        }
        if (hasOreTag(state, "cobalt")) {
            return ModUtils.convertRGBcolorToInt(0, 166, 226);
        }
        if (hasOreTag(state, "magnesium")) {
            return ModUtils.convertRGBcolorToInt(170, 145, 160);
        }
        if (hasOreTag(state, "nickel")) {
            return ModUtils.convertRGBcolorToInt(145, 143, 88);
        }
        if (hasOreTag(state, "platinum")) {
            return ModUtils.convertRGBcolorToInt(104, 152, 237);
        }
        if (hasOreTag(state, "titanium")) {
            return ModUtils.convertRGBcolorToInt(71, 71, 71);
        }
        if (hasOreTag(state, "chromium")) {
            return ModUtils.convertRGBcolorToInt(83, 174, 85);
        }
        if (hasOreTag(state, "spinel")) {
            return ModUtils.convertRGBcolorToInt(184, 87, 145);
        }
        if (hasOreTag(state, "silver")) {
            return ModUtils.convertRGBcolorToInt(211, 211, 211);
        }
        if (hasOreTag(state, "zinc")) {
            return ModUtils.convertRGBcolorToInt(186, 186, 186);
        }
        if (hasOreTag(state, "manganese")) {
            return ModUtils.convertRGBcolorToInt(235, 193, 207);
        }
        if (hasOreTag(state, "iridium")) {
            return ModUtils.convertRGBcolorToInt(234, 234, 234);
        }
        if (hasOreTag(state, "germanium")) {
            return ModUtils.convertRGBcolorToInt(138, 85, 34);
        }
        if (hasOreTag(state, "magnetite")) {
            return ModUtils.convertRGBcolorToInt(137, 131, 149);
        }
        if (hasOreTag(state, "calaverite")) {
            return ModUtils.convertRGBcolorToInt(249, 175, 44);
        }
        if (hasOreTag(state, "galena")) {
            return ModUtils.convertRGBcolorToInt(150, 215, 206);
        }
        if (hasOreTag(state, "nickelite")) {
            return ModUtils.convertRGBcolorToInt(211, 202, 110);
        }
        if (hasOreTag(state, "pyrite")) {
            return ModUtils.convertRGBcolorToInt(212, 175, 55);
        }
        if (hasOreTag(state, "quartzite")) {
            return ModUtils.convertRGBcolorToInt(250, 246, 241);
        }
        if (hasOreTag(state, "uranite")) {
            return ModUtils.convertRGBcolorToInt(70, 145, 15);
        }
        if (hasOreTag(state, "azurite")) {
            return ModUtils.convertRGBcolorToInt(230, 107, 0);
        }
        if (hasOreTag(state, "rhodonite")) {
            return ModUtils.convertRGBcolorToInt(139, 0, 0);
        }
        if (hasOreTag(state, "alfildit")) {
            return ModUtils.convertRGBcolorToInt(55, 135, 135);
        }
        if (hasOreTag(state, "euxenite")) {
            return ModUtils.convertRGBcolorToInt(170, 123, 44);
        }
        if (hasOreTag(state, "smithsonite")) {
            return ModUtils.convertRGBcolorToInt(109, 206, 167);
        }
        if (hasOreTag(state, "ilmenite")) {
            return ModUtils.convertRGBcolorToInt(110, 110, 110);
        }
        if (hasOreTag(state, "todorokite")) {
            return ModUtils.convertRGBcolorToInt(198, 147, 64);
        }
        if (hasOreTag(state, "ferroaugite")) {
            return ModUtils.convertRGBcolorToInt(100, 76, 136);
        }
        if (hasOreTag(state, "sheelite")) {
            return ModUtils.convertRGBcolorToInt(135, 84, 64);
        }
        if (hasOreTag(state, "arsenopyrite")) {
            return ModUtils.convertRGBcolorToInt(12, 166, 166);
        }
        if (hasOreTag(state, "braggite")) {
            return ModUtils.convertRGBcolorToInt(55, 117, 104);
        }
        if (hasOreTag(state, "wolframite")) {
            return ModUtils.convertRGBcolorToInt(113, 97, 81);
        }
        if (hasOreTag(state, "germanite")) {
            return ModUtils.convertRGBcolorToInt(99, 51, 4);
        }
        if (hasOreTag(state, "coltan")) {
            return ModUtils.convertRGBcolorToInt(117, 88, 86);
        }
        if (hasOreTag(state, "crocoite")) {
            return ModUtils.convertRGBcolorToInt(118, 28, 17);
        }
        if (hasOreTag(state, "xenotime")) {
            return ModUtils.convertRGBcolorToInt(123, 76, 10);
        }
        if (hasOreTag(state, "iridosmine")) {
            return ModUtils.convertRGBcolorToInt(126, 101, 36);
        }
        if (hasOreTag(state, "theophrastite")) {
            return ModUtils.convertRGBcolorToInt(30, 126, 56);
        }
        if (hasOreTag(state, "tetrahedrite")) {
            return ModUtils.convertRGBcolorToInt(112, 129, 30);
        }
        if (hasOreTag(state, "fergusonite")) {
            return ModUtils.convertRGBcolorToInt(43, 43, 43);
        }
        if (hasOreTag(state, "celestine")) {
            return ModUtils.convertRGBcolorToInt(39, 64, 63);
        }
        if (hasOreTag(state, "zircon")) {
            return ModUtils.convertRGBcolorToInt(110, 25, 24);
        }
        if (hasOreTag(state, "crystal")) {
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        }
        if (hasOreTag(state, "arsenic")) {
            return ModUtils.convertRGBcolorToInt(191, 212, 65);
        }
        if (hasOreTag(state, "barium")) {
            return ModUtils.convertRGBcolorToInt(253, 242, 80);
        }
        if (hasOreTag(state, "bismuth")) {
            return ModUtils.convertRGBcolorToInt(37, 145, 133);
        }
        if (hasOreTag(state, "gadolinium")) {
            return ModUtils.convertRGBcolorToInt(255, 180, 0);
        }
        if (hasOreTag(state, "gallium")) {
            return ModUtils.convertRGBcolorToInt(252, 187, 89);
        }
        if (hasOreTag(state, "hafnium")) {
            return ModUtils.convertRGBcolorToInt(212, 231, 255);
        }
        if (hasOreTag(state, "yttrium")) {
            return ModUtils.convertRGBcolorToInt(222, 101, 98);
        }
        if (hasOreTag(state, "molybdenum")) {
            return ModUtils.convertRGBcolorToInt(118, 84, 192);
        }
        if (hasOreTag(state, "neodymium")) {
            return ModUtils.convertRGBcolorToInt(125, 122, 160);
        }
        if (hasOreTag(state, "niobium")) {
            return ModUtils.convertRGBcolorToInt(61, 148, 224);
        }
        if (hasOreTag(state, "palladium")) {
            return ModUtils.convertRGBcolorToInt(230, 105, 17);
        }
        if (hasOreTag(state, "polonium")) {
            return ModUtils.convertRGBcolorToInt(84, 194, 246);
        }
        if (hasOreTag(state, "strontium")) {
            return ModUtils.convertRGBcolorToInt(168, 90, 41);
        }
        if (hasOreTag(state, "thallium")) {
            return ModUtils.convertRGBcolorToInt(121, 229, 71);
        }
        if (hasOreTag(state, "zirconium")) {
            return ModUtils.convertRGBcolorToInt(255, 225, 136);
        }
        if (hasOreTag(state, "sulfur")) {
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        }
        if (hasOreTag(state, "lithium")) {
            return ModUtils.convertRGBcolorToInt(190, 207, 214);
        }
        if (hasOreTag(state, "beryllium")) {
            return ModUtils.convertRGBcolorToInt(194, 194, 194);
        }
        if (hasOreTag(state, "bor")) {
            return ModUtils.convertRGBcolorToInt(62, 69, 71);
        }
        if (hasOreTag(state, "osmium")) {
            return ModUtils.convertRGBcolorToInt(165, 236, 244);
        }
        if (hasOreTag(state, "tantalum")) {
            return ModUtils.convertRGBcolorToInt(141, 174, 83);
        }
        if (hasOreTag(state, "cadmium")) {
            return ModUtils.convertRGBcolorToInt(177, 100, 197);
        }
        if (hasOreTag(state, "saltpeter")) {
            return ModUtils.convertRGBcolorToInt(43, 43, 43);
        }
        if (hasOreTag(state, "calcium")) {
            return ModUtils.convertRGBcolorToInt(212, 212, 212);
        }
        if (hasOreTag(state, "fluorapatite")) {
            return ModUtils.convertRGBcolorToInt(48, 86, 16);
        }
        if (hasOreTag(state, "nepheline")) {
            return ModUtils.convertRGBcolorToInt(134, 95, 11);
        }
        if (hasOreTag(state, "calciumphosphate")) {
            return ModUtils.convertRGBcolorToInt(202, 202, 202);
        }
        if (hasOreTag(state, "sodiumphosphate")) {
            return ModUtils.convertRGBcolorToInt(202, 202, 202);
        }
        if (hasOreTag(state, "potassiumphosphate")) {
            return ModUtils.convertRGBcolorToInt(202, 202, 202);
        }
        return 0xFFFFFFFF;
    }

    @SubscribeEvent
    public void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event) {
        ItemStack hoveredStack = event.getItemStack();
        if (hoveredStack.isEmpty()) {
            return;
        }

        if (!(hoveredStack.getItem() instanceof ItemCell cellItem)) {
            return;
        }

        if (!Screen.hasShiftDown()) {
            return;
        }
        ItemStackCell cell = null;
        try {
            cell = cellItem.getCell(hoveredStack);
        } catch (Exception e) {
        }
        ;
        if (cell == null) {
            return;
        }
        boolean isFluid = cell.getCellInfo().typeCell() == TypeCell.FLUID;
        Map<String, List<StorageStack>> storageMap = cell.getStorageStack();
        if (storageMap == null || storageMap.isEmpty()) {
            return;
        }

        ItemStack[] allStacks = cell.getStacks();
        FluidStack[] allFluids = cell.getFluids();


        List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();

        elements.add(Either.<FormattedText, TooltipComponent>left(Component.literal(" ")));
        elements.add(Either.<FormattedText, TooltipComponent>left(Component.translatable("tooltip.iu.cell_contents")));

        int maxLines = 81;
        int shown = 0;

        List<ItemGridTooltipComponent.Entry> entries = new ArrayList<>();

        for (List<StorageStack> list : storageMap.values()) {
            for (StorageStack storageStack : list) {
                if (storageStack == null) {
                    continue;
                }

                if (storageStack.getCount() <= 0) {
                    continue;
                }

                int slot = storageStack.getSlot();
                if (!isFluid) {
                    if (slot < 0 || slot >= allStacks.length) {
                        continue;
                    }

                    ItemStack stored = allStacks[slot];
                    if (stored == null || stored.isEmpty()) {
                        continue;
                    }

                    entries.add(new ItemGridTooltipComponent.EntryItem(stored.copy(), storageStack.getCount()));
                } else {
                    if (slot < 0 || slot >= allFluids.length) {
                        continue;
                    }

                    FluidStack stored = allFluids[slot];
                    if (stored == null || stored.isEmpty()) {
                        continue;
                    }

                    entries.add(new ItemGridTooltipComponent.EntryFluid(stored.copy(), storageStack.getCount()));

                }
                shown++;

                if (shown >= maxLines) {
                    break;
                }
            }

            if (shown >= maxLines) {
                break;
            }
        }

        if (!entries.isEmpty()) {
            elements.add(Either.right(new ItemGridTooltipComponent(entries)));
        }

        int total = countTotalStacks(storageMap, allStacks);
        int left = total - shown;
        if (left > 0) {
            elements.add(Either.<FormattedText, TooltipComponent>left(
                    Component.translatable("tooltip.iu.more_stacks", left)
            ));
        }
    }

    private int getRadiationTier(Level world, ChunkPos chunkPos) {
        final Radiation rad = RadiationSystem.rad_system.getMap().get(chunkPos);
        return rad == null ? 0 : rad.getLevel().ordinal();
    }

    public int clamp(int a, int min, int max) {
        return a < min ? min : (a > max ? max : a);
    }

    public void showRadiationEffects(LocalPlayer player, Level world, ChunkPos chunkPos, int tier) {
        ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;
        int particleCount = clamp(tier * 30, 30, 120);


        double minX = chunkPos.getMinBlockX();
        double maxX = chunkPos.getMaxBlockX() + 1;
        double minZ = chunkPos.getMinBlockZ();
        double maxZ = chunkPos.getMaxBlockZ() + 1;

        RandomSource rand = new XoroshiroRandomSource(world.getGameTime());

        for (int i = 0; i < particleCount; i++) {
            double x, y = player.getY() + rand.nextDouble() * 16.0, z;

            switch (rand.nextInt(4)) {
                case 0 -> {
                    x = minX;
                    z = minZ + rand.nextDouble() * 16.0;
                }
                case 1 -> {
                    x = maxX;
                    z = minZ + rand.nextDouble() * 16.0;
                }
                case 2 -> {
                    x = minX + rand.nextDouble() * 16.0;
                    z = minZ;
                }
                default -> {
                    x = minX + rand.nextDouble() * 16.0;
                    z = maxZ;
                }
            }

            Vec3 position = new Vec3(x, y, z);


            boolean isPortal = rand.nextBoolean();
            Particle particle = isPortal
                    ? particleEngine.createParticle(ParticleTypes.PORTAL, position.x, position.y, position.z, 0, 0, 0)
                    : particleEngine.createParticle(ParticleTypes.SMOKE, position.x, position.y, position.z, 0, 0, 0);

            if (particle != null) {
                if (isPortal) {
                    particle.setColor(0.0F, 1.0F - tier * 0.2F, 0.0F);
                    ((ParticleInvoker) particle).invokeSetAlpha(0.5F + rand.nextFloat() * 0.5F);
                } else {
                    particle.setColor(0.1F, 0.9F - tier * 0.15F, 0.1F);
                    ((ParticleInvoker) particle).invokeSetAlpha(0.7F + rand.nextFloat() * 0.3F);
                    particle.setLifetime(40);
                }
            }
        }
    }

    public Direction getDirection(ItemStack stack) {
        int rawDir = stack.getOrDefault(DataComponentsInit.DIRECTION, (byte) 0);
        return rawDir >= 1 && rawDir <= 6 ? Direction.values()[rawDir - 1] : null;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderTileSide(RenderLevelStageEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || mc.level == null) return;

        if (!(player.getMainHandItem().getItem() instanceof IUpgradeItem upgradeItem)) return;
        if (!upgradeItem.isSuitableFor(player.getMainHandItem(), set)) {
            return;
        }
        if (mc.level == null || event.getStage() != RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
            return;
        }
        BlockHitResult ray = getPlayerPOVHitResult(mc.level, mc.player, ClipContext.Fluid.NONE);
        if (ray == null || ray.getType() != HitResult.Type.BLOCK) {
            return;
        }
        Direction facing = getDirection(player.getMainHandItem());
        BlockEntity tile = player.level().getBlockEntity(ray.getBlockPos());
        if (!(tile instanceof IUpgradableBlock)) {
            return;
        }
        PoseStack poseStack = event.getPoseStack();
        Matrix4f matrix = poseStack.last().pose();
        if (facing != null) {
            BlockPos pos = ray.getBlockPos().offset(facing.getNormal());
            int xRange = 0;
            int yRange = 0;
            int zRange = 0;


            Vec3 camera = event.getCamera().getPosition();

            double x = camera.x;
            double y = camera.y;
            double z = camera.z;

            poseStack.pushPose();
            poseStack.translate(-x, -y, -z);
            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
            RenderSystem.enableDepthTest();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
            VertexConsumer p_109623_ = Minecraft.getInstance()
                    .renderBuffers()
                    .bufferSource()
                    .getBuffer(RenderType.lines());
            RenderSystem.lineWidth(10F);
            Vec3 lookVec = player.getLookAngle();
            for (int xPos = (int) (x - xRange); xPos <= x + xRange; xPos++) {
                for (int yPos = (int) (y - yRange); yPos <= y + yRange; yPos++) {
                    for (int zPos = (int) (z - zRange); zPos <= z + zRange; zPos++) {
                        BlockPos currentPos = new BlockPos(xPos, yPos, zPos);
                        Vec3 blockVec = new Vec3(
                                currentPos.getX() + 0.5 - player.getX(),
                                currentPos.getY() + 0.5 - player.getY(),
                                currentPos.getZ() + 0.5 - player.getZ()
                        );


                        float p_109630_ = 1.0f;
                        float p_109635_ = 1.0f;
                        float p_109636_ = 1.0f;
                        float p_109633_ = 1.0f;
                        float p_109634_ = 1.0f;
                        float p_109631_ = 1.0f;
                        float p_109632_ = 1.0f;
                        Matrix4f matrix4f = poseStack.last().pose();
                        Matrix3f matrix3f = poseStack.last().normal();
                        int f = currentPos.getX();
                        int f1 = currentPos.getY();
                        int f2 = currentPos.getZ();
                        int f3 = currentPos.getX() + 1;
                        int f4 = currentPos.getY() + 1;
                        int f5 = currentPos.getZ() + 1;
                        p_109623_.addVertex(matrix4f, f, f1, f2).setColor(p_109630_, p_109635_, p_109636_, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f1, f2).setColor(p_109630_, p_109635_, p_109636_, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f, f1, f2).setColor(p_109634_, p_109631_, p_109636_, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f, f4, f2).setColor(p_109634_, p_109631_, p_109636_, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f, f1, f2).setColor(p_109634_, p_109635_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
                        p_109623_.addVertex(matrix4f, f, f1, f5).setColor(p_109634_, p_109635_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
                        p_109623_.addVertex(matrix4f, f3, f1, f2).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f4, f2).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f4, f2).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), -1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f, f4, f2).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), -1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f, f4, f2).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
                        p_109623_.addVertex(matrix4f, f, f4, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
                        p_109623_.addVertex(matrix4f, f, f4, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, -1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f, f1, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, -1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f, f1, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f1, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f1, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, -1.0F);
                        p_109623_.addVertex(matrix4f, f3, f1, f2).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, -1.0F);
                        p_109623_.addVertex(matrix4f, f, f4, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f4, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f1, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f4, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f4, f2).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
                        p_109623_.addVertex(matrix4f, f3, f4, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);

                    }
                }
            }

            RenderSystem.disableDepthTest();

            poseStack.popPose();
        } else {
            for (Direction facing1 : Direction.values()) {
                BlockPos pos = ray.getBlockPos().offset(facing1.getNormal());
                int xRange = 0;
                int yRange = 0;
                int zRange = 0;


                Vec3 camera = event.getCamera().getPosition();

                double x = camera.x;
                double y = camera.y;
                double z = camera.z;

                poseStack.pushPose();
                poseStack.translate(-x, -y, -z);
                x = pos.getX();
                y = pos.getY();
                z = pos.getZ();
                RenderSystem.enableDepthTest();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
                VertexConsumer p_109623_ = Minecraft.getInstance()
                        .renderBuffers()
                        .bufferSource()
                        .getBuffer(RenderType.lines());
                RenderSystem.lineWidth(10F);
                Vec3 lookVec = player.getLookAngle();
                for (int xPos = (int) (x - xRange); xPos <= x + xRange; xPos++) {
                    for (int yPos = (int) (y - yRange); yPos <= y + yRange; yPos++) {
                        for (int zPos = (int) (z - zRange); zPos <= z + zRange; zPos++) {
                            BlockPos currentPos = new BlockPos(xPos, yPos, zPos);
                            Vec3 blockVec = new Vec3(
                                    currentPos.getX() + 0.5 - player.getX(),
                                    currentPos.getY() + 0.5 - player.getY(),
                                    currentPos.getZ() + 0.5 - player.getZ()
                            );


                            float p_109630_ = 1.0f;
                            float p_109635_ = 1.0f;
                            float p_109636_ = 1.0f;
                            float p_109633_ = 1.0f;
                            float p_109634_ = 1.0f;
                            float p_109631_ = 1.0f;
                            float p_109632_ = 1.0f;
                            Matrix4f matrix4f = poseStack.last().pose();
                            Matrix3f matrix3f = poseStack.last().normal();
                            int f = currentPos.getX();
                            int f1 = currentPos.getY();
                            int f2 = currentPos.getZ();
                            int f3 = currentPos.getX() + 1;
                            int f4 = currentPos.getY() + 1;
                            int f5 = currentPos.getZ() + 1;
                            p_109623_.addVertex(matrix4f, f, f1, f2).setColor(p_109630_, p_109635_, p_109636_, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f3, f1, f2).setColor(p_109630_, p_109635_, p_109636_, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f, f1, f2).setColor(p_109634_, p_109631_, p_109636_, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f, f4, f2).setColor(p_109634_, p_109631_, p_109636_, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f, f1, f2).setColor(p_109634_, p_109635_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
                            p_109623_.addVertex(matrix4f, f, f1, f5).setColor(p_109634_, p_109635_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
                            p_109623_.addVertex(matrix4f, f3, f1, f2).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f3, f4, f2).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f3, f4, f2).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), -1.0F, 0.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f, f4, f2).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), -1.0F, 0.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f, f4, f2).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
                            p_109623_.addVertex(matrix4f, f, f4, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
                            p_109623_.addVertex(matrix4f, f, f4, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, -1.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f, f1, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, -1.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f, f1, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f3, f1, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f3, f1, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, -1.0F);
                            p_109623_.addVertex(matrix4f, f3, f1, f2).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, -1.0F);
                            p_109623_.addVertex(matrix4f, f, f4, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f3, f4, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f3, f1, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f3, f4, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                            p_109623_.addVertex(matrix4f, f3, f4, f2).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
                            p_109623_.addVertex(matrix4f, f3, f4, f5).setColor(p_109630_, p_109631_, p_109632_, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);

                        }
                    }
                }

                RenderSystem.disableDepthTest();

                poseStack.popPose();
            }
        }
    }


    @SubscribeEvent
    public void onRenderWorld(RenderLevelStageEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null || event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }
        if (player != null && minecraft.hitResult instanceof BlockHitResult) {
            BlockHitResult ray = (BlockHitResult) minecraft.hitResult;
            if (!(player.getMainHandItem().getItem() instanceof ItemEnergyInstruments)) {
                return;
            }

            if (ray.getType() != HitResult.Type.BLOCK) {
                return;
            }
            BlockPos blockPos = ray.getBlockPos();

            PoseStack poseStack = event.getPoseStack();

            float size = 1.0f;
            ItemEnergyInstruments instruments = (ItemEnergyInstruments) player.getMainHandItem().getItem();
            final ItemStack stack = player.getMainHandItem();
            List<UpgradeItemInform> upgradeItemInforms = UpgradeSystem.system.getInformation(stack);

            int toolMode = instruments.readToolMode(stack);
            EnumOperations operations = instruments.getOperations().get(toolMode);
            int aoe = 0;
            int dig_depth = 0;
            List<Integer> list;
            switch (operations) {
                case BIGHOLES:
                    aoe = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms) ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number : 0;
                    aoe += 1;
                    dig_depth = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms) ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms).number : 0);

                    list = UpgradeSystem.system.getUpgradeFromList(stack);
                    if (list != null && list.size() >= 5) {
                        dig_depth += list.get(4);
                    }
                    break;
                case MEGAHOLES:
                    aoe = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms) ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number : 0;
                    aoe += 2;
                    dig_depth = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms) ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms).number : 0);
                    list = UpgradeSystem.system.getUpgradeFromList(stack);
                    if (list != null && list.size() >= 5) {
                        dig_depth += list.get(4);
                    }
                    break;
                case ULTRAHOLES:
                    aoe = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms) ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number : 0;
                    aoe += 3;
                    dig_depth = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms) ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms).number : 0);
                    list = UpgradeSystem.system.getUpgradeFromList(stack);
                    if (list != null && list.size() >= 5) {
                        dig_depth += list.get(4);
                    }
                    break;
                default:
                    break;
            }
            Minecraft mc = Minecraft.getInstance();
            BlockPos pos = ray.getBlockPos();
            int xRange = aoe;
            int yRange = aoe;
            int zRange = aoe;
            switch (ray.getDirection().ordinal()) {
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
            int Yy;
            Yy = yRange > 0 ? yRange - 1 : 0;


            poseStack.pushPose();
            double camX = mc.gameRenderer.getMainCamera().getPosition().x();
            double camY = mc.gameRenderer.getMainCamera().getPosition().y();
            double camZ = mc.gameRenderer.getMainCamera().getPosition().z();
            poseStack.translate(-camX, -camY, -camZ);
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(0.0F, 1.0F, 0.0F, 0.5F);

            Tesselator tessellator = Tesselator.getInstance();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
            VertexConsumer p_109623_ = Minecraft.getInstance()
                    .renderBuffers()
                    .bufferSource()
                    .getBuffer(RenderType.lines());
            RenderSystem.lineWidth(10F);
            Vec3 lookVec = player.getLookAngle();
            for (int xPos = (int) (x - xRange); xPos <= x + xRange; xPos++) {
                for (int yPos = (int) (y - yRange + Yy); yPos <= y + yRange + Yy; yPos++) {
                    for (int zPos = (int) (z - zRange); zPos <= z + zRange; zPos++) {
                        BlockPos currentPos = new BlockPos(xPos, yPos, zPos);
                        BlockState state = player.level().getBlockState(currentPos);
                        if (state.isAir()) {
                            continue;
                        }


                        float p_109630_ = 1.0f;
                        float p_109635_ = 1.0f;
                        float p_109636_ = 1.0f;
                        float p_109633_ = 1.0f;
                        float p_109634_ = 1.0f;
                        float p_109631_ = 1.0f;
                        float p_109632_ = 1.0f;
                        Matrix4f matrix4f = poseStack.last().pose();
                        Matrix3f matrix3f = poseStack.last().normal();
                        int f = currentPos.getX();
                        int f1 = currentPos.getY();
                        int f2 = currentPos.getZ();
                        int f3 = currentPos.getX() + 1;
                        int f4 = currentPos.getY() + 1;
                        int f5 = currentPos.getZ() + 1;
                        p_109623_.addVertex(matrix4f, f, f1, f2).setColor(0, p_109635_, 0, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f1, f2).setColor(0, p_109635_, 0, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f, f1, f2).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f, f4, f2).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f, f1, f2).setColor(0, p_109635_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
                        p_109623_.addVertex(matrix4f, f, f1, f5).setColor(0, p_109635_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
                        p_109623_.addVertex(matrix4f, f3, f1, f2).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f4, f2).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f4, f2).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), -1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f, f4, f2).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), -1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f, f4, f2).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
                        p_109623_.addVertex(matrix4f, f, f4, f5).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
                        p_109623_.addVertex(matrix4f, f, f4, f5).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, -1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f, f1, f5).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, -1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f, f1, f5).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f1, f5).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f1, f5).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, -1.0F);
                        p_109623_.addVertex(matrix4f, f3, f1, f2).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, -1.0F);
                        p_109623_.addVertex(matrix4f, f, f4, f5).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f4, f5).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 1.0F, 0.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f1, f5).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f4, f5).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                        p_109623_.addVertex(matrix4f, f3, f4, f2).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
                        p_109623_.addVertex(matrix4f, f3, f4, f5).setColor(0, p_109631_, 0, p_109633_).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);

                    }
                }
            }


            RenderSystem.disableDepthTest();


            poseStack.popPose();
        }

    }


    @SubscribeEvent
    public void onRenderOres(RenderLevelStageEvent event) {


        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        LocalPlayer player = mc.player;
        if (player == null || mc.level == null || event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }
        if (!(player.getMainHandItem().getItem() instanceof ItemEnergyInstruments)) {
            return;
        }
        if (!write) {
            writeData();
        }
        HitResult ray = mc.hitResult;
        if (!(ray instanceof BlockHitResult blockRay)) {
            return;
        }

        ItemStack stack = player.getMainHandItem();
        ItemEnergyInstruments instruments = (ItemEnergyInstruments) stack.getItem();
        int toolMode = instruments.readToolMode(stack);
        EnumOperations operations = instruments.getOperations().get(toolMode);
        if (operations != EnumOperations.ORE) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();


        BlockPos centerPos = blockRay.getBlockPos();
        double camX = mc.gameRenderer.getMainCamera().getPosition().x();
        double camY = mc.gameRenderer.getMainCamera().getPosition().y();
        double camZ = mc.gameRenderer.getMainCamera().getPosition().z();
        for (int x = centerPos.getX() - 5; x <= centerPos.getX() + 5; x++) {
            for (int y = centerPos.getY() - 5; y <= centerPos.getY() + 5; y++) {
                for (int z = centerPos.getZ() - 5; z <= centerPos.getZ() + 5; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = player.level().getBlockState(pos);
                    if (isOre(state)) {
                        int color = getOreColor(state);
                        float r = ((color >> 16) & 0xFF) / 255.0F;
                        float g = ((color >> 8) & 0xFF) / 255.0F;
                        float b = (color & 0xFF) / 255.0F;

                        poseStack.pushPose();
                        VertexConsumer bufferSource = Minecraft.getInstance()
                                .renderBuffers()
                                .bufferSource()
                                .getBuffer(LEASH);
                        poseStack.translate(-camX, -camY, -camZ);
                        RenderSystem.enableDepthTest();
                        drawCircle(poseStack, bufferSource, pos, r, g, b, 0.5F);
                        RenderSystem.disableDepthTest();

                        poseStack.popPose();
                    }
                }
            }
        }

    }

    private void drawCircle(PoseStack poseStack, VertexConsumer bufferSource, BlockPos pos, float r, float g, float b, float alpha) {

        float x = pos.getX() + 0.5F;
        float y = pos.getY() + 0.5F;
        float z = pos.getZ() + 0.5F;
        float radius = 0.35F;
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        for (int i = 0; i < latitudeSegments; i++) {
            for (int j = 0; j < longitudeSegments; j++) {


                double x1 = this.x1[i][j];
                double y1 = this.y1[i][j];
                double z1 = this.z1[i][j];


                double x2 = this.x2[i][j];
                double y2 = this.y2[i][j];
                double z2 = this.z2[i][j];


                double x3 = this.x3[i][j];
                double y3 = this.y3[i][j];
                double z3 = this.z3[i][j];


                double x4 = this.x4[i][j];
                double y4 = this.y4[i][j];
                double z4 = this.z4[i][j];

                RenderSystem.setShaderColor(1, 1, 1, 1);
                bufferSource.addVertex(matrix, (float) (x + x1 * radius), (float) (y + y1 * radius), (float) (z + z1 * radius)).setColor(r, g, b, 1f);
                bufferSource.addVertex(matrix, (float) (x + x2 * radius), (float) (y + y2 * radius), (float) (z + z2 * radius)).setColor(r, g, b, 1f);
                bufferSource.addVertex(matrix, (float) (x + x3 * radius), (float) (y + y3 * radius), (float) (z + z3 * radius)).setColor(r, g, b, 1f);

                bufferSource.addVertex(matrix, (float) (x + x3 * radius), (float) (y + y3 * radius), (float) (z + z3 * radius)).setColor(r, g, b, 1f);
                bufferSource.addVertex(matrix, (float) (x + x4 * radius), (float) (y + y4 * radius), (float) (z + z4 * radius)).setColor(r, g, b, 1f);
                bufferSource.addVertex(matrix, (float) (x + x1 * radius), (float) (y + y1 * radius), (float) (z + z1 * radius)).setColor(r, g, b, 1f);
            }

        }
    }

    private void writeData() {
        write = true;
        for (int i = 0; i <= latitudeSegments; i++) {
            double lat = Math.PI * (-0.5 + (double) i / latitudeSegments);
            sinLat[i] = Math.sin(lat);
            cosLat[i] = Math.cos(lat);
        }


        for (int j = 0; j <= longitudeSegments; j++) {
            double lng = 2 * Math.PI * j / longitudeSegments;
            sinLng[j] = Math.sin(lng);
            cosLng[j] = Math.cos(lng);
        }
        for (int i = 0; i < latitudeSegments; i++) {
            for (int j = 0; j < longitudeSegments; j++) {

                x1[i][j] = cosLng[j] * cosLat[i];
                y1[i][j] = sinLat[i];
                z1[i][j] = sinLng[j] * cosLat[i];


                x2[i][j] = cosLng[j] * cosLat[i + 1];
                y2[i][j] = sinLat[i + 1];
                z2[i][j] = sinLng[j] * cosLat[i + 1];


                x3[i][j] = cosLng[j + 1] * cosLat[i + 1];
                y3[i][j] = sinLat[i + 1];
                z3[i][j] = sinLng[j + 1] * cosLat[i + 1];


                x4[i][j] = cosLng[j + 1] * cosLat[i];
                y4[i][j] = sinLat[i];
                z4[i][j] = sinLng[j + 1] * cosLat[i];
            }
        }
    }

    private boolean isOre(BlockState state) {
        return dataColors.containsKey(state) || state.is(Tags.Blocks.ORES);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void renderTick(RenderGuiEvent.Post event) {
        ClientTickHandler.onTickRender1(event.getGuiGraphics());


    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onPlayerUpdate(PlayerTickEvent.Pre event) {
        if (event.getEntity() instanceof LocalPlayer) {
            LocalPlayer player = (LocalPlayer) event.getEntity();
            Level world = player.level();
            ChunkPos chunkPos = new ChunkPos(player.getOnPos());


            int tier = getRadiationTier(world, chunkPos);


            if (tier > 1) {
                showRadiationEffects(player, world, chunkPos, tier);
            }
        }
    }

}
