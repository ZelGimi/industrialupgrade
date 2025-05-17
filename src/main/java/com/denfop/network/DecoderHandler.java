package com.denfop.network;

import com.denfop.IUCore;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.recipe.RecipeInfo;
import com.denfop.api.vein.Vein;
import com.denfop.invslot.InvSlot;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.EncodedType;
import com.denfop.render.streak.PlayerStreakInfo;
import com.denfop.utils.ModUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.math.Vector3d;
import io.netty.buffer.ByteBufInputStream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class DecoderHandler {

    private static final Map<Class<?>, EncodedType> classToTypeCache =
            Collections.synchronizedMap(new IdentityHashMap<>());

    public static Object decode(CustomPacketBuffer is) throws IOException {
        try {
            return decode(is, typeFromId(is.readUnsignedByte()));
        } catch (IllegalArgumentException var3) {
            return null;
        }
    }

    public static <T> T decode(CustomPacketBuffer is, Class<T> clazz) throws IOException {
        EncodedType type = typeFromClass(clazz);
        if (type.threadSafe) {
            return (T) decode(is, type);
        } else {
            throw new IllegalArgumentException("requesting decode for non thread safe type");
        }
    }

    public static Object decodeDeferred(CustomPacketBuffer is, Class<?> clazz) throws IOException {
        EncodedType type = typeFromClass(clazz);
        return decode(is, type);
    }


    private static EncodedType typeFromId(int id) {
        if (id >= 0 && id < EncodedType.types.length) {
            return EncodedType.types[id];
        } else {
            throw new IllegalArgumentException("invalid type id: " + id);
        }
    }


    private static Class<?> box(Class<?> clazz) {
        if (clazz == Byte.TYPE) {
            return Byte.class;
        } else if (clazz == Short.TYPE) {
            return Short.class;
        } else if (clazz == Integer.TYPE) {
            return Integer.class;
        } else if (clazz == Long.TYPE) {
            return Long.class;
        } else if (clazz == Float.TYPE) {
            return Float.class;
        } else if (clazz == Double.TYPE) {
            return Double.class;
        } else if (clazz == Boolean.TYPE) {
            return Boolean.class;
        } else {
            return clazz == Character.TYPE ? Character.class : clazz;
        }
    }

    private static Class<?> unbox(Class<?> clazz) {
        if (clazz == Byte.class) {
            return Byte.TYPE;
        } else if (clazz == Short.class) {
            return Short.TYPE;
        } else if (clazz == Integer.class) {
            return Integer.TYPE;
        } else if (clazz == Long.class) {
            return Long.TYPE;
        } else if (clazz == Float.class) {
            return Float.TYPE;
        } else if (clazz == Double.class) {
            return Double.TYPE;
        } else if (clazz == Boolean.class) {
            return Boolean.TYPE;
        } else {
            return clazz == Character.class ? Character.TYPE : clazz;
        }
    }

    private static EncodedType typeFromClass(Class<?> cls) {
        if (cls == null) {
            return EncodedType.Null;
        } else if (cls.isArray()) {
            return EncodedType.Array;
        } else {
            if (cls.isPrimitive()) {
                cls = box(cls);
            }

            EncodedType ret = EncodedType.classToTypeMap.get(cls);
            if (ret != null) {
                return ret;
            } else {
                ret = classToTypeCache.get(cls);
                if (ret != null) {
                    return ret;
                } else {

                    EncodedType[] var3 = EncodedType.types;

                    for (EncodedType type : var3) {
                        if (type.cls != null && type.cls.isAssignableFrom(cls)) {
                            classToTypeCache.put(cls, type);
                            return type;
                        }
                    }

                    throw new IllegalStateException("unmatched " + cls);

                }
            }
        }
    }

    private static Class<?> getClass(String type) {
        try {
            return Class.forName(type);
        } catch (ClassNotFoundException var2) {
            throw new RuntimeException("Missing type from the class path expected by network: " + type, var2);
        }
    }


    public static Block getBlock(ResourceLocation loc) {
        Block ret = Registry.BLOCK.get(loc);
        if (ret != Blocks.AIR) {
            return ret;
        } else {
            return loc.getNamespace().equals("minecraft") && loc.getPath().equals("air") ? ret : null;
        }
    }

    public static Object decode(final CustomPacketBuffer is, EncodedType type) throws IOException {
        final int inputAmount;
        int i;
        switch (type) {
            case Array:
                EncodedType componentType = typeFromId(is.readUnsignedByte());
                boolean primitive = is.readBoolean();
                boolean isEnum = componentType == EncodedType.Enum;
                Class<?> component = primitive ? unbox(componentType.cls) : componentType.cls;
                if (component == null || isEnum) {
                    assert isEnum;

                    component = getClass(is.readString());
                }

                final Class<?> componentClass = component;
                final int len = is.readVarInt();
                boolean anyTypeMismatch = is.readBoolean();
                boolean needsResolving = !componentType.threadSafe;
                Object array = new Object[len];
                if (!needsResolving) {
                    array = Array.newInstance(component, len);
                } else {
                    array = new Object[len];
                }


                if (!anyTypeMismatch) {
                    if (isEnum) {
                        Object[] constants = component.getEnumConstants();

                        assert constants != null;

                        for (int k = 0; k < len; ++k) {
                            Array.set(array, k, constants[(Integer) decode(is, componentType)]);
                        }
                    } else {
                        for (i = 0; i < len; ++i) {
                            Array.set(array, i, decode(is, componentType));
                        }
                    }
                } else {
                    for (i = 0; i < len; ++i) {
                        EncodedType cType = typeFromId(is.readUnsignedByte());
                        if (!cType.threadSafe && !needsResolving) {
                            needsResolving = true;
                            if (componentClass != Object.class) {
                                Object newArray = new Object[len];
                                System.arraycopy(array, 0, newArray, 0, i);
                                array = newArray;
                            }
                        }

                        Array.set(array, i, decode(is, cType));
                    }
                }

                if (!needsResolving) {
                    return array;
                }

                final Object finalArray = array;
                Object ret_array = Array.newInstance(componentClass, len);

                for (int i1 = 0; i1 < len; ++i1) {
                    Array.set(ret_array, i1, getValue(Array.get(finalArray, i1)));
                }

                return ret_array;
            case Block:
                return getBlock((ResourceLocation) decode(is, EncodedType.ResourceLocation));
            case network_object:
                return is;
            case BlockPos:
                return new BlockPos(is.readInt(), is.readInt(), is.readInt());
            case Boolean:
                return is.readBoolean();
            case Byte:
                return is.readByte();
            case Character:
                return is.readChar();
            case ChunkPos:
                return new ChunkPos(is.readInt(), is.readInt());
            case Collection:
                final Object ret = decode(is, EncodedType.Array);
                return Arrays.asList((Object[]) ret);
            case Component:
                return is;
            case Double:
                return is.readDouble();
            case Enchantment:
                return Registry.ENCHANTMENT.get((ResourceLocation) decode(is, EncodedType.ResourceLocation));
            case Enum:
                return is.readVarInt();
            case Float:
                return is.readFloat();
            case Fluid:
                return Registry.FLUID.get((ResourceLocation) decode(is, EncodedType.ResourceLocation));
            case FluidStack:
                FluidStack ret2 = new FluidStack((Fluid) decode(is, EncodedType.Fluid), is.readInt());
                if (!ret2.isEmpty())
                ret2.setTag((CompoundTag) decode(is));
                return ret2;
            case FluidTank:
                FluidStack fluidStack = (FluidStack) decode(is);
                FluidTank fluidTank = new FluidTank(is.readInt());
                fluidTank.setFluid(fluidStack);
                return fluidTank;
            case GameProfile:
                return new GameProfile((UUID) decode(is), is.readString());
            case Integer:
                return is.readInt();
            case InvSlot:
                ItemStack[] contents = (ItemStack[]) decode(is, EncodedType.Array);
                InvSlot ret3 = new InvSlot(contents.length);

                for (i = 0; i < contents.length; ++i) {
                    ret3.set(i, contents[i]);
                }

                return ret3;
            case Item:
                return Registry.ITEM.get((ResourceLocation) decode(is, EncodedType.ResourceLocation));
            case ItemStack:
                int size = is.readByte();
                if (size == 0) {
                    return ModUtils.emptyStack;
                }
                Item item = decode(is, Item.class);
                CompoundTag nbt = (CompoundTag) decode(is);
                ItemStack ret1 = new ItemStack(item, size);
                ret1.setTag(nbt);
                return ret1;
            case Long:
                return is.readLong();
            case NBTTagCompound:
                return NbtIo.read(new ByteBufInputStream(is), NbtAccounter.UNLIMITED);
            case Null:
                return null;
            case Object:
                return new Object();
            case Potion:
                return Registry.POTION.get((ResourceLocation) decode(is, EncodedType.ResourceLocation));
            case ResourceLocation:
                return new ResourceLocation(is.readString(), is.readString());
            case Short:
                return is.readShort();
            case String:
                return is.readString();
            case TileEntity:
                final Level deferredWorld = (Level) decode(
                        is,
                        EncodedType.World
                );
                final BlockPos pos = (BlockPos) decode(is, EncodedType.BlockPos);
                return deferredWorld.getChunkAt(pos).getBlockEntity(pos);
            case UUID:
                return new UUID(is.readLong(), is.readLong());
            case Vec3:
                return new Vector3d(is.readDouble(), is.readDouble(), is.readDouble());
            case DataOre:
                return new com.denfop.tiles.base.DataOre(is);
            case Vein:
                return new Vein(is);
            case RecipeInfo:
                return new RecipeInfo(is);
            case Radiation:
                return new Radiation(is);

            case World:

                return IUCore.proxy.getWorld(is.readResourceKey(Registry.DIMENSION_REGISTRY));

            default:
                throw new IllegalArgumentException("unhandled type: " + type);
        }
    }

    public static <T> T getValue(Object decoded) {
        return (T) decoded;
    }

}
