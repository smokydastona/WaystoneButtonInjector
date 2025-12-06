/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.balm.api.network;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.Comment;
import net.blay09.mods.balm.api.config.Config;
import net.blay09.mods.balm.api.config.ExpectedType;
import net.blay09.mods.balm.api.config.IgnoreConfig;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.Synced;
import net.blay09.mods.balm.api.config.reflection.LoadedReflectionConfig;
import net.blay09.mods.balm.api.config.reflection.NestedType;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategoryBuilder;
import net.blay09.mods.balm.api.config.schema.builder.ConfigPropertyBuilder;
import net.blay09.mods.balm.api.config.schema.builder.PropertyHolderBuilder;
import net.blay09.mods.balm.api.config.schema.impl.ConfigSchemaImpl;
import net.minecraft.resources.ResourceLocation;

public class ConfigReflection {
    public static BalmConfigSchema schemaOf(Class<?> configDataClass) {
        List<Field> rootFields = ConfigReflection.getAllFields(configDataClass);
        List<Field> rootDataFields = rootFields.stream().filter(it -> !ConfigReflection.isCategoryField(it)).toList();
        ResourceLocation identifier = ConfigReflection.getIdentifier(configDataClass);
        ConfigSchemaImpl schema = BalmConfigSchema.create(identifier);
        ConfigReflection.buildFieldsIntoSchema(schema, configDataClass, rootDataFields);
        List<Field> categoryFields = rootFields.stream().filter(ConfigReflection::isCategoryField).toList();
        for (Field categoryField : categoryFields) {
            List<Field> fields = ConfigReflection.getAllFields(categoryField.getType());
            ConfigCategoryBuilder category = schema.category(categoryField.getName());
            net.blay09.mods.balm.api.config.reflection.Comment commentAnnotation = categoryField.getAnnotation(net.blay09.mods.balm.api.config.reflection.Comment.class);
            if (commentAnnotation != null) {
                category.comment(commentAnnotation.value());
            } else {
                Comment legacyCommentAnnotation = categoryField.getAnnotation(Comment.class);
                if (legacyCommentAnnotation != null) {
                    category.comment(legacyCommentAnnotation.value());
                }
            }
            ConfigReflection.buildFieldsIntoSchema(category, categoryField.getType(), fields);
        }
        return schema;
    }

    private static void buildFieldsIntoSchema(PropertyHolderBuilder builder, Class<?> clazz, List<Field> fields) {
        Object defaults = ConfigReflection.createInstance(clazz);
        for (Field field : fields) {
            ConfigPropertyBuilder property = builder.property(field.getName());
            net.blay09.mods.balm.api.config.reflection.Comment commentAnnotation = field.getAnnotation(net.blay09.mods.balm.api.config.reflection.Comment.class);
            if (commentAnnotation != null) {
                property.comment(commentAnnotation.value());
            } else {
                Comment legacyCommentAnnotation = field.getAnnotation(Comment.class);
                if (legacyCommentAnnotation != null) {
                    property.comment(legacyCommentAnnotation.value());
                }
            }
            if (field.getAnnotation(net.blay09.mods.balm.api.config.reflection.Synced.class) != null || field.getAnnotation(Synced.class) != null) {
                property.synced();
            }
            Class<?> type = field.getType();
            Class<?> nestedType = null;
            NestedType nestedTypeAnnotation = field.getAnnotation(NestedType.class);
            if (nestedTypeAnnotation != null) {
                nestedType = nestedTypeAnnotation.value();
            } else {
                ExpectedType legacyNestedTypeAnnotation = field.getAnnotation(ExpectedType.class);
                if (legacyNestedTypeAnnotation != null) {
                    nestedType = legacyNestedTypeAnnotation.value();
                }
            }
            try {
                Object defaultValue = field.get(defaults);
                if (type == String.class) {
                    property.stringOf((String)defaultValue);
                    continue;
                }
                if (type == ResourceLocation.class) {
                    property.resourceLocationOf((ResourceLocation)defaultValue);
                    continue;
                }
                if (type == Integer.class || type == Integer.TYPE) {
                    property.intOf((Integer)defaultValue);
                    continue;
                }
                if (type == Long.class || type == Long.TYPE) {
                    property.longOf((Long)defaultValue);
                    continue;
                }
                if (type == Float.class || type == Float.TYPE) {
                    property.floatOf(((Float)defaultValue).floatValue());
                    continue;
                }
                if (type == Double.class || type == Double.TYPE) {
                    property.doubleOf((Double)defaultValue);
                    continue;
                }
                if (type == Boolean.class || type == Boolean.TYPE) {
                    property.boolOf((Boolean)defaultValue);
                    continue;
                }
                if (type.isEnum()) {
                    ConfigReflection.propertyOfEnum(property, defaultValue);
                    continue;
                }
                if (List.class.isAssignableFrom(type)) {
                    if (nestedType != null) {
                        List listValue = (List)defaultValue;
                        property.listOf(nestedType, listValue);
                        continue;
                    }
                    throw new IllegalArgumentException("List field " + field.getName() + " in class " + clazz.getName() + " is missing @NestedType annotation");
                }
                if (Set.class.isAssignableFrom(type)) {
                    if (nestedType != null) {
                        Set setValue = (Set)defaultValue;
                        property.setOf(nestedType, setValue);
                        continue;
                    }
                    throw new IllegalArgumentException("Set field " + field.getName() + " in class " + clazz.getName() + " is missing @NestedType annotation");
                }
                throw new IllegalArgumentException("Unsupported config field type " + type.getName() + " in class " + clazz.getName());
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException("Error accessing config field " + field.getName() + " in class " + clazz.getName(), e);
            }
        }
    }

    private static <T extends Enum<T>> void propertyOfEnum(ConfigPropertyBuilder property, Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof Enum)) {
            throw new IllegalArgumentException("Object must be an Enum");
        }
        Enum enumValue = (Enum)obj;
        property.enumOf(enumValue);
    }

    private static <T> T createInstance(Class<T> clazz) {
        try {
            return clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch (IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Config class " + clazz.getName() + " must have a public no-arg constructor.", e);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException("Error instantiating config class " + clazz.getName(), e);
        }
    }

    private static boolean isConfigDataField(Field field) {
        return !Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()) && field.getAnnotation(IgnoreConfig.class) == null && field.getAnnotation(net.blay09.mods.balm.api.config.reflection.IgnoreConfig.class) == null;
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        return Arrays.stream(clazz.getFields()).filter(ConfigReflection::isConfigDataField).toList();
    }

    private static boolean isCategoryField(Field field) {
        return !field.getType().isPrimitive() && !field.getType().isEnum() && field.getType() != String.class && field.getType() != List.class && field.getType() != Set.class && field.getType() != ResourceLocation.class;
    }

    public static ResourceLocation getIdentifier(Class<?> configDataClass) {
        net.blay09.mods.balm.api.config.reflection.Config configAnnotation = configDataClass.getAnnotation(net.blay09.mods.balm.api.config.reflection.Config.class);
        if (configAnnotation == null) {
            Config legacyConfigAnnotation = configDataClass.getAnnotation(Config.class);
            if (legacyConfigAnnotation == null) {
                throw new IllegalArgumentException("Class " + configDataClass.getName() + " is missing a @Config annotation");
            }
            return new ResourceLocation(legacyConfigAnnotation.value(), "common");
        }
        return new ResourceLocation(configAnnotation.value(), configAnnotation.type());
    }

    public static <T> LoadedReflectionConfig<T> of(Class<T> configDataClass, LoadedConfig loadedConfig) {
        T instance = ConfigReflection.createInstance(configDataClass);
        BalmConfigSchema schema = Balm.getConfig().getSchema(configDataClass);
        LoadedReflectionConfig<T> config = new LoadedReflectionConfig<T>(instance);
        config.applyFrom(schema, loadedConfig);
        return config;
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    public static List<Field> getSyncedFields(Class<?> clazz) {
        Field[] fields;
        ArrayList<Field> syncedFields = new ArrayList<Field>();
        for (Field field : fields = clazz.getFields()) {
            if (!ConfigReflection.isSyncedFieldOrObject(field) || !ConfigReflection.isConfigDataField(field)) continue;
            syncedFields.add(field);
        }
        return syncedFields;
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    public static boolean isSyncedFieldOrObject(Field field) {
        boolean hasSyncedAnnotation = field.getAnnotation(net.blay09.mods.balm.api.config.reflection.Synced.class) != null;
        boolean isObject = !field.getType().isPrimitive() && !field.getType().isEnum() && field.getType() != String.class && field.getType() != List.class && field.getType() != Set.class && field.getType() != ResourceLocation.class;
        return hasSyncedAnnotation || isObject;
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    public static Object deepCopy(Object from, Object to) {
        Field[] fields;
        for (Field field : fields = from.getClass().getFields()) {
            if (!ConfigReflection.isConfigDataField(field)) continue;
            Class<?> type = field.getType();
            try {
                if (String.class.isAssignableFrom(type) || ResourceLocation.class.isAssignableFrom(type) || Enum.class.isAssignableFrom(type) || type.isPrimitive()) {
                    field.set(to, field.get(from));
                    continue;
                }
                if (List.class.isAssignableFrom(type)) {
                    field.set(to, new ArrayList((Collection)field.get(from)));
                    continue;
                }
                if (Set.class.isAssignableFrom(type)) {
                    field.set(to, new HashSet((Collection)field.get(from)));
                    continue;
                }
                field.set(to, ConfigReflection.deepCopy(field.get(from), field.get(to)));
            }
            catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        return to;
    }
}

