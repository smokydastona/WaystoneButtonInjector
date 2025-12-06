/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.DataResult
 *  com.mojang.serialization.DynamicOps
 *  net.minecraft.Util
 *  net.minecraft.util.ExtraCodecs
 *  net.minecraft.util.StringRepresentable
 */
package net.blay09.mods.balm.api.config;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import net.blay09.mods.balm.common.codec.BalmCodecs;
import net.minecraft.Util;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

public class LenientEnumCodecs {
    public static <T extends Enum<T>> Codec<T> fromValues(Supplier<T[]> valuesSupplier) {
        Enum[] values = (Enum[])valuesSupplier.get();
        Function nameLookup = LenientEnumCodecs.createNameLookup((Enum[])values, Function.identity());
        ToIntFunction indexLookup = Util.m_214686_(Arrays.asList(values));
        return new LenientEnumCodec(values, nameLookup, indexLookup);
    }

    private static <T extends Enum<T>> Function<String, T> createNameLookup(T[] values, Function<String, String> keyFunction) {
        if (values.length > 16) {
            Map map = Arrays.stream(values).collect(Collectors.toMap(value -> (String)keyFunction.apply(LenientEnumCodecs.getSerializedName(value).toLowerCase(Locale.ROOT)), Function.identity()));
            return name -> name == null ? null : (Enum)map.get(name.toLowerCase(Locale.ROOT));
        }
        return name -> {
            for (Enum value : values) {
                if (!((String)keyFunction.apply(LenientEnumCodecs.getSerializedName(value))).equalsIgnoreCase((String)name)) continue;
                return value;
            }
            return null;
        };
    }

    private static String getSerializedName(Enum<?> enumValue) {
        if (enumValue instanceof StringRepresentable) {
            StringRepresentable stringRepresentable = (StringRepresentable)enumValue;
            return stringRepresentable.m_7912_();
        }
        return enumValue.name().toLowerCase(Locale.ROOT);
    }

    static class LenientEnumCodec<S extends Enum<S>>
    implements Codec<S> {
        private final Codec<S> codec;

        public LenientEnumCodec(S[] values, Function<String, S> nameLookup, ToIntFunction<S> indexLookup) {
            this.codec = ExtraCodecs.m_184425_(BalmCodecs.stringResolver(LenientEnumCodecs::getSerializedName, nameLookup), (Codec)ExtraCodecs.m_184421_(indexLookup, p_304986_ -> p_304986_ >= 0 && p_304986_ < values.length ? values[p_304986_] : null, (int)-1));
        }

        public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T value) {
            return this.codec.decode(ops, value);
        }

        public <T> DataResult<T> encode(S input, DynamicOps<T> ops, T prefix) {
            return this.codec.encode(input, ops, prefix);
        }
    }
}

