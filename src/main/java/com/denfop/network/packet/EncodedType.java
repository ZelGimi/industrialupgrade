package com.denfop.network.packet;

import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.space.fakebody.FakeAsteroid;
import com.denfop.api.space.fakebody.FakePlanet;
import com.denfop.api.space.fakebody.FakeSatellite;
import com.denfop.api.tesseract.Channel;
import com.denfop.componets.AbstractComponent;
import com.denfop.recipe.IInputItemStack;
import com.denfop.tiles.base.DataOre;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

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
    String(String.class),
    Enum(Enum.class),
    UUID(java.util.UUID.class),
    Block(net.minecraft.block.Block.class),
    Item(net.minecraft.item.Item.class),
    TileEntity(net.minecraft.tileentity.TileEntity.class, false),
    ItemStack(net.minecraft.item.ItemStack.class),
    World(net.minecraft.world.World.class, false),
    NBTTagCompound(net.minecraft.nbt.NBTTagCompound.class),
    ResourceLocation(net.minecraft.util.ResourceLocation.class),
    GameProfile(com.mojang.authlib.GameProfile.class),
    Potion(net.minecraft.potion.Potion.class),
    Enchantment(net.minecraft.enchantment.Enchantment.class),
    BlockPos(net.minecraft.util.math.BlockPos.class),
    ChunkPos(net.minecraft.util.math.ChunkPos.class),
    Vec3(Vec3d.class),
    Fluid(net.minecraftforge.fluids.Fluid.class),
    Vein(com.denfop.api.vein.Vein.class),
    MachineRecipe(BaseMachineRecipe.class),
    Input(IInputItemStack.class),
    FluidStack(net.minecraftforge.fluids.FluidStack.class),
    FluidTank(net.minecraftforge.fluids.FluidTank.class),
    InvSlot(com.denfop.invslot.InvSlot.class),
    FAKE_PLANET(FakePlanet.class),
    FAKE_SATELLITE(FakeSatellite.class),
    FAKE_ASTEROID(FakeAsteroid.class),
    Component(AbstractComponent.class, false),

    Radiation(com.denfop.api.radiationsystem.Radiation.class),
    Collection(java.util.Collection.class),
    BaseLevelSystem(com.denfop.api.research.main.BaseLevelSystem.class),
    BaseResearch(com.denfop.api.research.main.BaseResearch.class),
    RecipeInfo(com.denfop.api.recipe.RecipeInfo.class),
    PlayerStreakInfo(com.denfop.render.streak.PlayerStreakInfo.class),
    DataOre(DataOre.class),
    channel(Channel.class),
    network_object(INetworkObject.class),
    player(EntityPlayer.class),
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

