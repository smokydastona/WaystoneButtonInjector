/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config;

import java.lang.reflect.Field;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.BalmConfigProperty;
import net.blay09.mods.balm.api.config.ExpectedType;

@Deprecated(forRemoval=true, since="1.21.5")
public class BalmConfigPropertyImpl<T>
implements BalmConfigProperty<T> {
    private final BalmConfigData configData;
    private final Field categoryField;
    private final Field propertyField;
    private final BalmConfigData defaultConfig;

    public BalmConfigPropertyImpl(BalmConfigData configData, Field categoryField, Field propertyField, BalmConfigData defaultConfig) {
        this.configData = configData;
        this.categoryField = categoryField;
        this.propertyField = propertyField;
        this.defaultConfig = defaultConfig;
    }

    @Override
    public Class<T> getType() {
        return this.propertyField.getType();
    }

    @Override
    public Class<T> getInnerType() {
        ExpectedType expectedTypeAnnotation = this.propertyField.getAnnotation(ExpectedType.class);
        if (expectedTypeAnnotation != null) {
            return expectedTypeAnnotation.value();
        }
        return null;
    }

    @Override
    public T getValue() {
        try {
            BalmConfigData instance = this.categoryField != null ? this.categoryField.get(this.configData) : this.configData;
            return (T)this.propertyField.get(instance);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
            return this.getDefaultValue();
        }
    }

    @Override
    public void setValue(T value) {
        try {
            BalmConfigData instance = this.categoryField != null ? this.categoryField.get(this.configData) : this.configData;
            this.propertyField.set(instance, value);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T getDefaultValue() {
        try {
            BalmConfigData instance = this.categoryField != null ? this.categoryField.get(this.defaultConfig) : this.defaultConfig;
            return (T)this.propertyField.get(instance);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}

