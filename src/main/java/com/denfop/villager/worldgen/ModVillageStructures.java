package com.denfop.villager.worldgen;

import com.denfop.IUCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModVillageStructures {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, IUCore.MODID);
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, IUCore.MODID);

    public static final RegistryObject<StructureType<ProfessionHamletStructure>> PROFESSION_HAMLET =
            STRUCTURE_TYPES.register("profession_hamlet", () -> () -> ProfessionHamletStructure.CODEC);
    private static boolean initialized;    public static final RegistryObject<StructurePieceType> PROFESSION_HAMLET_PIECE =
            STRUCTURE_PIECES.register(
                    "profession_hamlet_piece",
                    () -> (StructurePieceType.ContextlessType) ProfessionHamletPiece::new
            );

    private ModVillageStructures() {
    }

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        STRUCTURE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        STRUCTURE_PIECES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


}
