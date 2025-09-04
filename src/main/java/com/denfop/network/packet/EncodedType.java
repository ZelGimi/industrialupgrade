package com.denfop.network.packet;


import com.denfop.api.recipe.*;
import com.denfop.api.vein.common.VeinBase;
import com.denfop.componets.AbstractComponent;
import com.denfop.inventory.Inventory;
import com.mojang.math.Vector3d;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.IdentityHashMap;
import java.util.Map;

public enum EncodedType {
    Null(null),
    Array(null),
    Byte(Byte.class),
    Short(Short.class),
    Integer(Integer.class),
    Long(Long.class),
    Float(Float.class),
    Double(Double.class),
    Boolean(Boolean.class),
    Character(Character.class),
    network_object(INetworkObject.class),
    String(String.class),
    Enum(Enum.class),
    UUID(java.util.UUID.class),
    Block(net.minecraft.world.level.block.Block.class),
    Item(net.minecraft.world.item.Item.class),
    TileEntity(BlockEntity.class, false),
    ItemStack(net.minecraft.world.item.ItemStack.class),
    World(Level.class, false),
    NBTTagCompound(CompoundTag.class),
    ResourceLocation(net.minecraft.resources.ResourceLocation.class),
    GameProfile(com.mojang.authlib.GameProfile.class),
    Potion(net.minecraft.world.item.alchemy.Potion.class),
    Enchantment(net.minecraft.world.item.enchantment.Enchantment.class),
    BlockPos(net.minecraft.core.BlockPos.class),
    ChunkPos(net.minecraft.world.level.ChunkPos.class),
    Vec3(Vector3d.class),
    Fluid(net.minecraft.world.level.material.Fluid.class),
    Vein(VeinBase.class),
    MachineRecipe(BaseMachineRecipe.class),
    FluidStack(net.minecraftforge.fluids.FluidStack.class),
    FluidTank(net.minecraftforge.fluids.capability.templates.FluidTank.class),
    InvSlot(Inventory.class),
    //    FAKE_PLANET(FakePlanet.class),
    //   FAKE_SATELLITE(FakeSatellite.class),
    //   FAKE_ASTEROID(FakeAsteroid.class),
    Component(AbstractComponent.class, false),

    Radiation(com.denfop.api.pollution.radiation.Radiation.class),
    Collection(java.util.Collection.class),
    //   BaseLevelSystem(com.denfop.api.research.main.BaseLevelSystem.class),
    //   BaseResearch(com.denfop.api.research.main.BaseResearch.class),
    RecipeInfo(com.denfop.api.recipe.RecipeInfo.class),
    PlayerStreakInfo(com.denfop.render.streak.PlayerStreakInfo.class),
    DataOre(com.denfop.blockentity.base.DataOre.class),
    BaseRecipe(BaseMachineRecipe.class),
    BaseFluidRecipe(BaseFluidMachineRecipe.class),
    recipeOutput(RecipeOutput.class),
    inputStack(IInput.class),
    inputFluidStack(IInputFluid.class),
    tuple(Tuple.class),
    Object(Object.class);

    public static final EncodedType[] types = values();
    public static final Map<Class<?>, EncodedType> classToTypeMap = new IdentityHashMap(types.length - 2);

    static {

        for (EncodedType type : types) {
            if (type.cls != null) {
                classToTypeMap.put(type.cls, type);
            }
        }

        if (types.length > 255) {
            throw new RuntimeException("too many types");
        }
    }

    public final Class<?> cls;
    public final boolean threadSafe;

    EncodedType(Class<?> cls) {
        this(cls, true);
    }

    EncodedType(Class<?> cls, boolean threadSafe) {
        this.cls = cls;
        this.threadSafe = threadSafe;
    }
}

