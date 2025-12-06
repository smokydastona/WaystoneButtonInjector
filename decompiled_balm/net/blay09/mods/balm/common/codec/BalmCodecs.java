/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Either
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.DataResult
 */
package net.blay09.mods.balm.common.codec;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Optional;
import java.util.function.Function;

public class BalmCodecs {
    public static <T> Codec<T> withAlternative(Codec<T> primary, Codec<? extends T> alternative) {
        return Codec.either(primary, alternative).xmap(BalmCodecs::unwrapEither, Either::left);
    }

    private static <U> U unwrapEither(Either<? extends U, ? extends U> either) {
        return (U)either.map(Function.identity(), Function.identity());
    }

    public static <E> Codec<E> stringResolver(Function<E, String> mapper, Function<String, E> reverseMapper) {
        return Codec.STRING.flatXmap(name -> Optional.ofNullable(reverseMapper.apply((String)name)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown element name:" + name)), e -> Optional.ofNullable((String)mapper.apply(e)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Element with unknown name: " + String.valueOf(e))));
    }
}

