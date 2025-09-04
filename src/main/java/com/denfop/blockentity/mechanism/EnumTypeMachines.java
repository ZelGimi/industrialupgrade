package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.dataregistry.DataBlockEntity;
import com.denfop.sound.EnumSound;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.Arrays;

public enum EnumTypeMachines {
    MACERATOR(IUItem.machines_base, 2, "macerator", EnumSound.MaceratorOp.getNameSounds()),
    EXTRACTOR(IUItem.machines_base, 11, "extractor", EnumSound.extractor.getNameSounds()),
    COMPRESSOR(IUItem.machines_base, 5, "compressor", EnumSound.compressor.getNameSounds()),
    ELECTRICFURNACE(IUItem.machines_base, 8, "furnace", "furnace"),
    METALFOMER(IUItem.machines_base, 14, "extruding", EnumSound.extruder.getNameSounds()),

    RECYCLER(IUItem.machines_base1, 2, "recycler", EnumSound.RecyclerOp.getNameSounds()),
    COMBRECYCLER(IUItem.machines_base1, 5, "recycler", EnumSound.RecyclerOp.getNameSounds()),
    COMBMACERATOR(IUItem.machines_base1, 9, "comb_macerator", EnumSound.MaceratorOp.getNameSounds()),
    ROLLING(IUItem.machines_base2, 3, "rolling", EnumSound.metalformer.getNameSounds()),
    EXTRUDING(IUItem.machines_base2, 7, "extruding", EnumSound.extruder.getNameSounds()),
    CUTTING(IUItem.machines_base2, 11, "cutting", EnumSound.cutter.getNameSounds()),
    FARMER(IUItem.machines_base3, 3, "farmer", EnumSound.Fermer.getNameSounds()),
    ASSAMPLERSCRAP(IUItem.machines_base3, 7, "scrap", EnumSound.AssamplerScrap.getNameSounds()),
    OreWashing(IUItem.machines_base3, 11, "orewashing", EnumSound.ore_washing.getNameSounds()),
    Centrifuge(IUItem.machines_base3, 15, "centrifuge", EnumSound.centrifuge.getNameSounds()),
    Gearing(IUItem.machines_base3, 19, "gearing", "");
    public final int meta;
    public final DataBlockEntity block;
    private final String soundName;
    public String recipe;
    private SoundEvent sound;

    EnumTypeMachines(DataBlockEntity block, int meta, String recipe, String soundEvent) {
        this.block = block;
        this.meta = meta;
        this.recipe = recipe;
        this.soundName = soundEvent;
    }

    public static void writeSound() {
        Arrays.stream(EnumTypeMachines.values()).filter(types -> !types.soundName.isEmpty()).forEach(types -> {


            if (types.soundName.equals("furnace")) {
                types.setSound(SoundEvents.FURNACE_FIRE_CRACKLE);
            } else {
                types.setSound(EnumSound.getSondFromString(types.soundName));
            }
        });
    }

    public SoundEvent getSound() {
        return sound;
    }

    public void setSound(final SoundEvent sound) {
        this.sound = sound;
    }


}
