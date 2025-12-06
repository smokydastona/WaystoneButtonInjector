/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Comment {
    public String value();
}

