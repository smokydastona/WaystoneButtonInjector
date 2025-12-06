/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config;

@Deprecated(forRemoval=true, since="1.21.5")
public interface BalmConfigProperty<T> {
    public Class<T> getType();

    public Class<T> getInnerType();

    public T getValue();

    public void setValue(T var1);

    public T getDefaultValue();
}

