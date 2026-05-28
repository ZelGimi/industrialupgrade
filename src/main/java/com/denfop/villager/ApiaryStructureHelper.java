package com.denfop.villager;

import com.denfop.IUItem;
import com.denfop.api.bee.Bee;
import com.denfop.api.bee.BeeNetwork;
import com.denfop.blockentity.bee.BlockEntityApiary;
import com.denfop.blockentity.bee.EnumTypeBee;
import com.denfop.blockentity.bee.EnumTypeLife;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class ApiaryStructureHelper {

    private ApiaryStructureHelper() {
    }

    public static void populateStarterApiary(final BlockEntityApiary apiary, final RandomSource random) {
        if (apiary == null || apiary.getWorld() == null || apiary.getQueen() != null) {
            return;
        }

        final Map.Entry<Integer, Bee> beeEntry = BeeNetwork.instance.getBeeMap().entrySet().stream()
                .min(Comparator.comparingInt(Map.Entry::getKey))
                .orElse(null);

        if (beeEntry == null) {
            return;
        }

        populateApiaryWithBee(apiary, beeEntry.getKey(), beeEntry.getValue(), random);
    }

    public static void populateForSelectedBees(
            final BlockEntityApiary apiary,
            final List<Bee> selectedBees,
            final RandomSource random
    ) {
        if (apiary == null || apiary.getWorld() == null || apiary.getQueen() != null) {
            return;
        }

        if (selectedBees == null || selectedBees.isEmpty()) {
            populateStarterApiary(apiary, random);
            return;
        }


        final Bee preferredBee = selectedBees.get(0);
        final Map.Entry<Integer, Bee> resolved = resolveBeeEntry(preferredBee);

        if (resolved != null) {
            populateApiaryWithBee(apiary, resolved.getKey(), resolved.getValue(), random);
            return;
        }


        for (Bee bee : selectedBees) {
            final Map.Entry<Integer, Bee> entry = resolveBeeEntry(bee);
            if (entry != null) {
                populateApiaryWithBee(apiary, entry.getKey(), entry.getValue(), random);
                return;
            }
        }


        populateStarterApiary(apiary, random);
    }

    private static Map.Entry<Integer, Bee> resolveBeeEntry(final Bee bee) {
        if (bee == null || BeeNetwork.instance == null || BeeNetwork.instance.getBeeMap() == null) {
            return null;
        }


        final Bee directBee = BeeNetwork.instance.getBeeMap().get(bee.getId());
        if (directBee != null) {
            return Map.entry(bee.getId(), directBee);
        }


        for (Map.Entry<Integer, Bee> entry : BeeNetwork.instance.getBeeMap().entrySet()) {
            final Bee value = entry.getValue();
            if (value != null && value.getId() == bee.getId()) {
                return entry;
            }
        }

        final String name = bee.getName();
        if (name != null) {
            for (Map.Entry<Integer, Bee> entry : BeeNetwork.instance.getBeeMap().entrySet()) {
                final Bee value = entry.getValue();
                if (value != null && name.equals(value.getName())) {
                    return entry;
                }
            }
        }

        return null;
    }

    private static void populateApiaryWithBee(
            final BlockEntityApiary apiary,
            final int beeId,
            final Bee sourceBee,
            final RandomSource random
    ) {
        if (apiary == null || sourceBee == null) {
            return;
        }

        final Bee queen = sourceBee.copy();
        final ItemStack jar = IUItem.jarBees.getItemStack(0).copy();
        final CompoundTag jarNbt = ModUtils.nbt(jar);

        final int swarm = 18 + random.nextInt(7);

        jarNbt.putInt("bee_id", beeId);
        jarNbt.putInt("swarm", swarm);

        final CompoundTag tileNbt = new CompoundTag();
        tileNbt.putByte("bee_id", (byte) beeId);
        tileNbt.putLong("id_queen", random.nextLong());
        tileNbt.put("stack", jar.serializeNBT());

        final int attackBeeCount = (int) (swarm * (BlockEntityApiary.percentAttackBee / 100.0));
        final int doctorBeeCount = (int) (swarm * (BlockEntityApiary.percentDoctorBee / 100.0));
        final int workersBeeCount = (int) (swarm * (BlockEntityApiary.percentWorkersBee / 100.0));
        final int buildersBeeCount = Math.max(0, swarm - (attackBeeCount + doctorBeeCount + workersBeeCount));

        final ListTag bees = new ListTag();

        for (int i = 0; i < attackBeeCount; i++) {
            bees.add(createBeeTag(queen, EnumTypeBee.ATTACK, random));
        }
        for (int i = 0; i < doctorBeeCount; i++) {
            bees.add(createBeeTag(queen, EnumTypeBee.DOCTOR, random));
        }
        for (int i = 0; i < workersBeeCount; i++) {
            bees.add(createBeeTag(queen, EnumTypeBee.WORKER, random));
        }
        for (int i = 0; i < buildersBeeCount; i++) {
            bees.add(createBeeTag(queen, EnumTypeBee.BUILDER, random));
        }

        tileNbt.put("bees", bees);
        apiary.readFromNBT(tileNbt);
        apiary.setChanged();
    }

    private static CompoundTag createBeeTag(
            final Bee queen,
            final EnumTypeBee type,
            final RandomSource random
    ) {
        final int minLife = queen.getTickBirthRate();
        final int maxDelta = Math.max(1, queen.getTickLifecycles() - queen.getTickBirthRate());
        final int life = random.nextInt(maxDelta) + minLife;

        return new com.denfop.blockentity.bee.Bee(
                type,
                queen,
                EnumTypeLife.BEE,
                25,
                life
        ).writeToNBT();
    }
}