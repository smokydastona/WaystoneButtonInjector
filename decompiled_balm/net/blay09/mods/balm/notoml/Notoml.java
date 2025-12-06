/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.Table
 *  com.google.common.collect.Table$Cell
 */
package net.blay09.mods.balm.notoml;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.List;
import net.blay09.mods.balm.notoml.NotomlError;

public class Notoml {
    private final List<NotomlError> errors = new ArrayList<NotomlError>();
    private final Table<String, String, Object> properties = HashBasedTable.create();
    private final Table<String, String, String> comments = HashBasedTable.create();

    public Notoml() {
    }

    public Notoml(Table<String, String, Object> properties, Table<String, String, String> comments) {
        this.properties.putAll(properties);
        this.comments.putAll(comments);
    }

    public List<NotomlError> getErrors() {
        return this.errors;
    }

    public Table<String, String, Object> getProperties() {
        return this.properties;
    }

    public Table<String, String, String> getComments() {
        return this.comments;
    }

    public void setProperty(String category, String property, Object value) {
        this.properties.put((Object)category, (Object)property, value);
    }

    public void setComment(String category, String property, String comment) {
        this.comments.put((Object)category, (Object)property, (Object)comment);
    }

    public void addError(NotomlError error) {
        this.errors.add(error);
    }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    public boolean containsProperties(Notoml other) {
        for (Table.Cell cell : other.properties.cellSet()) {
            if (this.properties.contains(cell.getRowKey(), cell.getColumnKey())) continue;
            return false;
        }
        return true;
    }
}

