package com.denfop.audio;

import net.minecraft.util.SoundEvent;

public enum EnumSound {
    air_collector(TypePath.Machines, "air_collector"),
    alloysmelter(TypePath.Machines, "alloysmelter"),
    analyzer(TypePath.Machines, "analyzer"),
    AssamplerScrap(TypePath.Machines, "AssamplerScrap"),
    blast_furnace(TypePath.Machines, "blast_furnace"),
    centrifuge(TypePath.Machines, "centrifuge"),
    compressor(TypePath.Machines, "CompressorOp"),
    cooling(TypePath.Machines, "cooling"),
    cutter(TypePath.Machines, "cutter"),
    diesel_generator(TypePath.Machines, "diesel_generator"),
    electrolyzer(TypePath.Machines, "electrolyzer"),
    enrichment(TypePath.Machines, "enrichment"),
    extractor(TypePath.Machines, "ExtractorOp"),
    extruder(TypePath.Machines, "extruder"),
    Fermer(TypePath.Machines, "Fermer"),
    fisher(TypePath.Machines, "fisher"),
    gas(TypePath.Machines, "gas"),
    gen_cobblectone(TypePath.Machines, "gen_cobblectone"),
    gen_lava(TypePath.Machines, "gen_lava"),
    gen_obsidiant(TypePath.Machines, "gen_obsidiant"),
    genmirc(TypePath.Machines, "genmirc"),
    handlerho(TypePath.Machines, "handlerho"),
    hydrogen_generator(TypePath.Machines, "hydrogen_generator"),
    InterruptOne(TypePath.Machines, "InterruptOne"),
    MaceratorOp(TypePath.Machines, "MaceratorOp"),
    magnet(TypePath.Machines, "magnet"),
    magnet_generator(TypePath.Machines, "magnet_generator"),
    metalformer(TypePath.Machines, "metalformer"),
    molecular(TypePath.Machines, "molecular"),
    oilgetter(TypePath.Machines, "oilgetter"),
    oilrefiner(TypePath.Machines, "oilrefiner"),
    ore_washing(TypePath.Machines, "ore_washing"),
    painting(TypePath.Machines, "painting"),
    pen(TypePath.Machines, "pen"),
    petrol_generator(TypePath.Machines, "petrol_generator"),
    plastic(TypePath.Machines, "plastic"),
    plastic_plate(TypePath.Machines, "plastic_plate"),
    plate(TypePath.Machines, "plate"),
    PumpOp(TypePath.Machines, "PumpOp"),
    quarry(TypePath.Machines, "quarry"),
    radiation(TypePath.Machines, "radiation"),
    RecyclerOp(TypePath.Machines, "RecyclerOp"),
    rig(TypePath.Machines, "rig"),
    synthesys(TypePath.Machines, "synthesys"),
    upgrade_block(TypePath.Machines, "upgrade_block"),
    welding(TypePath.Machines, "welding"),
    WitherDeath1(TypePath.Machines, "WitherDeath1"),
    WitherHurt3(TypePath.Machines, "WitherHurt3"),
    WitherIdle1(TypePath.Machines, "WitherIdle1"),
    WitherSpawn1(TypePath.Machines, "WitherSpawn1"),
    zab(TypePath.Machines, "zab"),
    zap(TypePath.Machines, "zap"),
    bow(TypePath.Tools, "bow"),
    JetpackLoop(TypePath.Tools, "JetpackLoop"),
    NanosabreIdle(TypePath.Tools, "NanosabreIdle"),
    NanosabrePowerup(TypePath.Tools, "NanosabrePowerup"),
    NanosabreSwing1(TypePath.Tools, "NanosabreSwing1"),
    NanosabreSwing2(TypePath.Tools, "NanosabreSwing2"),
    NanosabreSwing3(TypePath.Tools, "NanosabreSwing3"),
    purifier(TypePath.Tools, "purifier"),
    toolchange(TypePath.Tools, "toolchange"),
    Treetap(TypePath.Tools, "Treetap"),
    wrench(TypePath.Tools, "wrench"),
    teleporter(TypePath.Machines, "teleporter"),
    katana(TypePath.Tools, "katana");
    private final TypePath typePath;
    private final String nameSounds;
    private SoundEvent soundEvent;

    EnumSound(TypePath typePath, String nameSounds) {
        this.typePath = typePath;
        this.nameSounds = nameSounds;
    }

    public static SoundEvent getSondFromString(String sound) {
        for (EnumSound sound1 : EnumSound.values()) {
            if (sound1.nameSounds.toLowerCase().trim().equals(sound.trim().toLowerCase())) {
                return sound1.soundEvent;
            }
        }
        return null;
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }

    public void setSoundEvent(final SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
    }

    public String getNameSounds() {
        return nameSounds;
    }

    public String getSoundName() {
        return typePath.name() + "." + nameSounds;
    }
}
