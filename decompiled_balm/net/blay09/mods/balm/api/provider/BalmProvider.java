/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.provider;

@Deprecated(forRemoval=true, since="1.21.5")
public class BalmProvider<T> {
    private final Class<T> providerClass;
    private final T instance;

    public BalmProvider(Class<T> providerClass, T instance) {
        this.providerClass = providerClass;
        this.instance = instance;
    }

    public Class<T> getProviderClass() {
        return this.providerClass;
    }

    public T getInstance() {
        return this.instance;
    }
}

