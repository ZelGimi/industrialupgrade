package com.denfop.network;


import com.denfop.api.pollution.radiation.Radiation;
import com.denfop.api.recipe.RecipeInfo;
import com.denfop.api.vein.common.VeinBase;
import com.denfop.blockentity.base.DataOre;
import com.denfop.componets.AbstractComponent;
import com.denfop.componets.Fluids;
import com.denfop.inventory.Inventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.EncodedType;
import com.denfop.network.packet.INetworkObject;
import com.denfop.utils.ModUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.math.Vector3d;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class EncoderHandler {

    private static final Map<Class<?>, EncodedType> classToTypeCache =
            Collections.synchronizedMap(new IdentityHashMap<>());

    public static void encode(CustomPacketBuffer os, Object o) throws IOException {
        try {
            encode(os, o, true);
        } catch (IllegalArgumentException ignored) {

        }

    }

    private static int idFromType(EncodedType type) {
        return type.ordinal();
    }


    private static EncodedType typeFromObject(Object o) {
        return o == null ? EncodedType.Null : typeFromClass(o.getClass());
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

    public static void encode(CustomPacketBuffer os, Object o, boolean withType) throws IOException {
        EncodedType type = typeFromObject(o);
        if (withType) {
            os.writeByte(idFromType(type));
        }

        int i;
        switch (type) {
            case Array:
                Class<?> componentClass = o.getClass().getComponentType();
                int len = Array.getLength(o);
                Object value;
                if (componentClass == Object.class && len > 0) {
                    boolean isEnum = false;
                    Class<?> target = null;

                    label200:
                    for (i = 0; i < len; ++i) {
                        value = Array.get(o, i);
                        if (target == null) {
                            if (value instanceof Enum) {
                                target = ((Enum) value).getDeclaringClass();
                                isEnum = true;
                            } else if (value != null) {
                                target = value.getClass();

                                assert target != Object.class;
                            }
                        } else if (value != null) {
                            Class<?> valueClass = value.getClass();
                            if (valueClass != target && !target.isAssignableFrom(valueClass)) {
                                if (isEnum || value instanceof Enum) {
                                    throw new IllegalArgumentException("Array of mixed enum entries");
                                }

                                do {
                                    if ((target = target.getSuperclass()) == Object.class) {
                                        ++i;

                                        while (i < len) {
                                            if (Array.get(o, i) instanceof Enum) {
                                                throw new IllegalArgumentException("Array of mixed enum entries");
                                            }

                                            ++i;
                                        }

                                        break label200;
                                    }
                                } while (!target.isAssignableFrom(valueClass));
                            } else {
                                assert isEnum == (value instanceof Enum);
                            }
                        } else if (isEnum) {
                            throw new IllegalArgumentException("Enum array with null entry");
                        }
                    }

                    componentClass = target;
                }

                EncodedType componentType = typeFromClass(componentClass);
                os.writeByte(idFromType(componentType));
                os.writeBoolean(componentClass.isPrimitive());
                if (componentType == EncodedType.Enum) {
                    os.writeString(componentClass.getName());
                }

                os.writeVarInt(len);
                boolean anyTypeMismatch = false;

                for (i = 0; i < len; ++i) {
                    value = Array.get(o, i);
                    if (value == null || typeFromClass(value.getClass()) != componentType) {
                        anyTypeMismatch = true;
                        break;
                    }
                }

                os.writeBoolean(anyTypeMismatch);

                for (i = 0; i < len; ++i) {
                    encode(os, Array.get(o, i), anyTypeMismatch);
                }
                break;
            case tuple:
                Tuple<?, ?> tuple = (Tuple<?, ?>) o;
                encode(os, tuple.getA());
                encode(os, tuple.getB());
                break;
            case Block:
                encode(os, Registry.BLOCK.getKey((Block) o), false);
                break;
            case BlockPos:
                BlockPos pos = (BlockPos) o;
                os.writeInt(pos.getX());
                os.writeInt(pos.getY());
                os.writeInt(pos.getZ());
                break;
            case Boolean:
                os.writeBoolean((Boolean) o);
                break;
            case Byte:
                os.writeByte((Byte) o);
                break;
            case network_object:
                os.writeBytes(((INetworkObject) o).writePacket());
                break;
            case Character:
                os.writeChar((Character) o);
                break;
            case ChunkPos:
                ChunkPos chunkpos = (ChunkPos) o;
                os.writeInt(chunkpos.x);
                os.writeInt(chunkpos.z);
                break;
            case DataOre:
                os.writeBytes(((DataOre) o).getCustomPacket());
                break;
            case Collection:
                encode(os, ((Collection) o).toArray(), false);
                break;
            case Component:
                os.writeBytes(((AbstractComponent) o).updateComponent());
                break;
            case Double:
                os.writeDouble((Double) o);
                break;
            case Enchantment:
                encode(os, Registry.ENCHANTMENT.getKey((Enchantment) o), false);
                break;
            case Enum:
                os.writeVarInt(((Enum) o).ordinal());
                break;
            case Float:
                os.writeFloat((Float) o);
                break;
            case Fluid:
                encode(os, Registry.FLUID.getKey((Fluid) o), false);
                break;
            case FluidStack:
                FluidStack fs = (FluidStack) o;
                if (!fs.isEmpty())
                    encode(os, fs.getFluid(), false);
                else
                    encode(os, Fluids.EMPTY, false);
                os.writeInt(fs.getAmount());
                if (!fs.isEmpty())
                    encode(os, fs.getTag(), true);
                break;
            case FluidTank:
                FluidTank tank = (FluidTank) o;
                encode(os, tank.getFluid(), true);
                os.writeInt(tank.getCapacity());
                break;
            case GameProfile:
                GameProfile gp = (GameProfile) o;
                encode(os, gp.getId(), true);
                os.writeString(gp.getName());
                break;
            case Integer:
                os.writeInt((Integer) o);
                break;
            case InvSlot:
                Inventory slot = (Inventory) o;
                ItemStack[] contents = new ItemStack[slot.size()];

                for (i = 0; i < slot.size(); ++i) {
                    contents[i] = slot.get(i);
                }

                encode(os, contents, false);
                break;
            case Item:
                encode(os, Registry.ITEM.getKey((Item) o), false);
                break;
            case ItemStack:
                ItemStack stack = (ItemStack) o;
                if (ModUtils.isEmpty(stack)) {
                    os.writeByte(0);
                } else {
                    os.writeByte(ModUtils.getSize(stack));
                    encode(os, stack.getItem(), false);
                    encode(os, stack.getTag(), true);
                }
                break;
            case Long:
                os.writeLong((Long) o);
                break;

            case NBTTagCompound:
                NbtIo.write((CompoundTag) o, new ByteBufOutputStream(os));
                break;
            case Null:
                if (!withType) {
                    throw new IllegalArgumentException("o has to be non-null without types");
                }
                break;
            case Object:
                throw new IllegalArgumentException("unhandled class: " + o.getClass());
            case Potion:
                encode(os, Registry.POTION.getKey((Potion) o), false);
                break;
            case ResourceLocation:
                ResourceLocation loc = (ResourceLocation) o;
                os.writeString(loc.getNamespace());
                os.writeString(loc.getPath());
                break;
            case Short:
                os.writeShort((Short) o);
                break;
            case String:
                os.writeString((String) o);
                break;
            case TileEntity:
                BlockEntity te = (BlockEntity) o;
                encode(os, te.getLevel(), false);
                encode(os, te.getBlockPos(), false);
                break;
      /*      case BaseLevelSystem:
                BaseLevelSystem baseResearch = (BaseLevelSystem) o;
                os.writeString(baseResearch.getPlayer().getName());
                encode(os, baseResearch.write(), true);
                break;*/
            case UUID:
                UUID uuid = (UUID) o;
                os.writeLong(uuid.getMostSignificantBits());
                os.writeLong(uuid.getLeastSignificantBits());
                break;
            case Vec3:
                Vector3d v = (Vector3d) o;
                os.writeDouble(v.x);
                os.writeDouble(v.y);
                os.writeDouble(v.z);
                break;
            case World:
                os.writeResourceKey(((Level) o).dimension());
                break;
            case Vein:
                VeinBase vein = (VeinBase) o;
                os.writeBytes(vein.writePacket());
                break;
            case RecipeInfo:
                RecipeInfo recipeInfo = (RecipeInfo) o;
                os.writeBytes(recipeInfo.getPacket());
                break;
            case Radiation:
                Radiation radiation = (Radiation) o;
                os.writeBytes(radiation.writePacket());
                break;
            /* case FAKE_PLANET:
                break;
*/

            default:
                throw new IllegalArgumentException("unhandled type: " + type);
        }

    }

}
