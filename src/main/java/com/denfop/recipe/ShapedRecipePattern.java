package com.denfop.recipe;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.chars.CharArraySet;
import it.unimi.dsi.fastutil.chars.CharSet;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class ShapedRecipePattern {
    public static final MapCodec<ShapedRecipePattern> MAP_CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, ShapedRecipePattern> STREAM_CODEC;
    /**
     * @deprecated
     */
    @Deprecated
    private static final int MAX_SIZE = 3;
    static int maxWidth = 3;
    static int maxHeight = 3;

    static {
        MAP_CODEC = ShapedRecipePattern.Data.MAP_CODEC.flatXmap(ShapedRecipePattern::unpack, (p_344423_) -> p_344423_.data.map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Cannot encode unpacked recipe")));
        STREAM_CODEC = StreamCodec.ofMember(ShapedRecipePattern::toNetwork, ShapedRecipePattern::fromNetwork);
    }

    private final int width;
    private final int height;
    private final NonNullList<Ingredient> ingredients;
    private final Optional<Data> data;
    private final int ingredientCount;
    private final boolean symmetrical;

    public ShapedRecipePattern(int p_311959_, int p_312714_, NonNullList<Ingredient> p_312761_, Optional<Data> p_312427_) {
        this.width = p_311959_;
        this.height = p_312714_;
        this.ingredients = p_312761_;
        this.data = p_312427_;
        int i = 0;
        Iterator var6 = p_312761_.iterator();

        while (var6.hasNext()) {
            Ingredient ingredient = (Ingredient) var6.next();
            if (!ingredient.isEmpty()) {
                ++i;
            }
        }

        this.ingredientCount = i;
        this.symmetrical = Util.isSymmetrical(p_311959_, p_312714_, p_312761_);
    }

    public static int getMaxWidth() {
        return maxWidth;
    }

    public static int getMaxHeight() {
        return maxHeight;
    }

    public static void setCraftingSize(int width, int height) {
        if (maxWidth < width) {
            maxWidth = width;
        }

        if (maxHeight < height) {
            maxHeight = height;
        }

    }

    public static ShapedRecipePattern of(Map<Character, Ingredient> p_312851_, String... p_312645_) {
        return of(p_312851_, List.of(p_312645_));
    }

    public static ShapedRecipePattern of(Map<Character, Ingredient> p_312370_, List<String> p_312701_) {
        Data shapedrecipepattern$data = new Data(p_312370_, p_312701_);
        return (ShapedRecipePattern) unpack(shapedrecipepattern$data).getOrThrow();
    }

    private static DataResult<ShapedRecipePattern> unpack(Data p_312037_) {
        String[] astring = shrink(p_312037_.pattern);
        int i = astring[0].length();
        int j = astring.length;
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);
        CharSet charset = new CharArraySet(p_312037_.key.keySet());

        for (int k = 0; k < astring.length; ++k) {
            String s = astring[k];

            for (int l = 0; l < s.length(); ++l) {
                char c0 = s.charAt(l);
                Ingredient ingredient = c0 == ' ' ? Ingredient.EMPTY : (Ingredient) p_312037_.key.get(c0);
                if (ingredient == null) {
                    return DataResult.error(() -> {
                        return "Pattern references symbol '" + c0 + "' but it's not defined in the key";
                    });
                }

                charset.remove(c0);
                nonnulllist.set(l + i * k, ingredient);
            }
        }

        return !charset.isEmpty() ? DataResult.error(() -> {
            return "Key defines symbols that aren't used in pattern: " + String.valueOf(charset);
        }) : DataResult.success(new ShapedRecipePattern(i, j, nonnulllist, Optional.of(p_312037_)));
    }

    @VisibleForTesting
    static String[] shrink(List<String> p_311893_) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int i1 = 0; i1 < p_311893_.size(); ++i1) {
            String s = (String) p_311893_.get(i1);
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (p_311893_.size() == l) {
            return new String[0];
        } else {
            String[] astring = new String[p_311893_.size() - l - k];

            for (int k1 = 0; k1 < astring.length; ++k1) {
                astring[k1] = ((String) p_311893_.get(k1 + k)).substring(i, j + 1);
            }

            return astring;
        }
    }

    private static int firstNonSpace(String p_312343_) {
        int i;
        for (i = 0; i < p_312343_.length() && p_312343_.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int lastNonSpace(String p_311944_) {
        int i;
        for (i = p_311944_.length() - 1; i >= 0 && p_311944_.charAt(i) == ' '; --i) {
        }

        return i;
    }

    private static ShapedRecipePattern fromNetwork(RegistryFriendlyByteBuf p_319788_) {
        int i = p_319788_.readVarInt();
        int j = p_319788_.readVarInt();
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);
        nonnulllist.replaceAll((p_319733_) -> {
            return (Ingredient) Ingredient.CONTENTS_STREAM_CODEC.decode(p_319788_);
        });
        return new ShapedRecipePattern(i, j, nonnulllist, Optional.empty());
    }

    public Optional<Data> getData() {
        return data;
    }

    public boolean matches(CraftingInput p_345063_) {
        if (p_345063_.ingredientCount() != this.ingredientCount) {
            return false;
        } else {
            if (p_345063_.width() == this.width && p_345063_.height() == this.height) {
                if (!this.symmetrical && this.matches(p_345063_, true)) {
                    return true;
                }

                if (this.matches(p_345063_, false)) {
                    return true;
                }
            }

            return false;
        }
    }

    private boolean matches(CraftingInput p_345835_, boolean p_344990_) {
        for (int i = 0; i < this.height; ++i) {
            for (int j = 0; j < this.width; ++j) {
                Ingredient ingredient;
                if (p_344990_) {
                    ingredient = (Ingredient) this.ingredients.get(this.width - j - 1 + i * this.width);
                } else {
                    ingredient = (Ingredient) this.ingredients.get(j + i * this.width);
                }

                ItemStack itemstack = p_345835_.getItem(j, i);
                if (!ingredient.test(itemstack)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void toNetwork(RegistryFriendlyByteBuf p_320098_) {
        p_320098_.writeVarInt(this.width);
        p_320098_.writeVarInt(this.height);
        Iterator var2 = this.ingredients.iterator();

        while (var2.hasNext()) {
            Ingredient ingredient = (Ingredient) var2.next();
            Ingredient.CONTENTS_STREAM_CODEC.encode(p_320098_, ingredient);
        }

    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public NonNullList<Ingredient> ingredients() {
        return this.ingredients;
    }

    public record Data(Map<Character, Ingredient> key, List<String> pattern) {
        public static final MapCodec<Data> MAP_CODEC;
        private static final Codec<List<String>> PATTERN_CODEC;
        private static final Codec<Character> SYMBOL_CODEC;

        static {
            PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap((p_312085_) -> {
                if (p_312085_.size() > ShapedRecipePattern.maxHeight) {
                    return DataResult.error(() -> {
                        return "Invalid pattern: too many rows, %s is maximum".formatted(ShapedRecipePattern.maxHeight);
                    });
                } else if (p_312085_.isEmpty()) {
                    return DataResult.error(() -> {
                        return "Invalid pattern: empty pattern not allowed";
                    });
                } else {
                    int i = ((String) p_312085_.getFirst()).length();
                    Iterator var2 = p_312085_.iterator();

                    String s;
                    do {
                        if (!var2.hasNext()) {
                            return DataResult.success(p_312085_);
                        }

                        s = (String) var2.next();
                        if (s.length() > ShapedRecipePattern.maxWidth) {
                            return DataResult.error(() -> {
                                return "Invalid pattern: too many columns, %s is maximum".formatted(ShapedRecipePattern.maxWidth);
                            });
                        }
                    } while (i == s.length());

                    return DataResult.error(() -> {
                        return "Invalid pattern: each row must be the same width";
                    });
                }
            }, Function.identity());
            SYMBOL_CODEC = Codec.STRING.comapFlatMap((p_312250_) -> {
                if (p_312250_.length() != 1) {
                    return DataResult.error(() -> {
                        return "Invalid key entry: '" + p_312250_ + "' is an invalid symbol (must be 1 character only).";
                    });
                } else {
                    return " ".equals(p_312250_) ? DataResult.error(() -> {
                        return "Invalid key entry: ' ' is a reserved symbol.";
                    }) : DataResult.success(p_312250_.charAt(0));
                }
            }, String::valueOf);
            MAP_CODEC = RecordCodecBuilder.mapCodec((p_312573_) -> {
                return p_312573_.group(ExtraCodecs.strictUnboundedMap(SYMBOL_CODEC, Ingredient.CODEC_NONEMPTY).fieldOf("key").forGetter((p_312509_) -> {
                    return p_312509_.key;
                }), PATTERN_CODEC.fieldOf("pattern").forGetter((p_312713_) -> {
                    return p_312713_.pattern;
                })).apply(p_312573_, Data::new);
            });
        }

        public Data(Map<Character, Ingredient> key, List<String> pattern) {
            this.key = key;
            this.pattern = pattern;
        }

        public Map<Character, Ingredient> key() {
            return this.key;
        }

        public List<String> pattern() {
            return this.pattern;
        }
    }
}
