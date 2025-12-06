/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.proxy;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ModProxy<T> {
    public ModProxy<T> with(String var1, String var2);

    public ModProxy<T> withMultiplexer(Function<List<T>, T> var1);

    public ModProxy<T> withFallback(T var1);

    public T build();

    public Supplier<T> buildLazily();
}

