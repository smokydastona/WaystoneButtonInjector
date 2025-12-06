/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.notoml;

import java.util.ArrayList;
import java.util.List;
import net.blay09.mods.balm.notoml.NotomlError;

public abstract class NotomlTokenConsumer {
    private String currentCategory = "";
    private String currentProperty = "";
    private List<String> currentList;
    private StringBuilder currentMultiLineString;
    private StringBuilder currentComment;

    public void emitComment(String comment) {
        if (this.currentComment == null) {
            this.currentComment = new StringBuilder();
        }
        this.currentComment.append(comment).append("\n");
    }

    public void emitCategory(String categoryName) {
        this.currentCategory = categoryName;
        if (this.currentComment != null) {
            this.onCommentParsed(this.currentCategory, this.currentProperty, this.currentComment.toString());
            this.currentComment = null;
        }
    }

    public void emitPropertyKey(String property) {
        this.currentProperty = property;
        if (this.currentComment != null) {
            this.onCommentParsed(this.currentCategory, this.currentProperty, this.currentComment.toString());
            this.currentComment = null;
        }
    }

    public void emitPropertyValue(String value) {
        if (this.currentList != null) {
            this.currentList.add(value);
        } else if (this.currentMultiLineString != null) {
            this.currentMultiLineString.append(value);
        } else {
            this.onPropertyParsed(this.currentCategory, this.currentProperty, value);
        }
    }

    public void emitListStart() {
        this.currentList = new ArrayList<String>();
    }

    public void emitListEnd() {
        this.onPropertyParsed(this.currentCategory, this.currentProperty, this.currentList);
        this.currentList = null;
    }

    public void emitMultiLineStringStart() {
        this.currentMultiLineString = new StringBuilder();
    }

    public void emitMultiLineStringEnd() {
        this.onPropertyParsed(this.currentCategory, this.currentProperty, this.currentMultiLineString.toString().trim());
        this.currentMultiLineString = null;
    }

    public void emitError(NotomlError error) {
        this.onError(error);
    }

    protected abstract void onPropertyParsed(String var1, String var2, Object var3);

    protected abstract void onCommentParsed(String var1, String var2, String var3);

    protected abstract void onError(NotomlError var1);
}

