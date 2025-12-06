/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.proxy;

public interface PlatformProxy<T> {
    public PlatformProxy<T> with(String var1, String var2);

    public T build();

    default public PlatformProxy<T> withFabric(String clazzName) {
        return this.with("fabric", clazzName);
    }

    default public PlatformProxy<T> withForge(String clazzName) {
        return this.with("forge", clazzName);
    }
}

