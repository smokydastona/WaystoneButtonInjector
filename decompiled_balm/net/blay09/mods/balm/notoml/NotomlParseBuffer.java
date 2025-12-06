/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.notoml;

public class NotomlParseBuffer {
    private final String input;
    private int i = 0;
    private int line = 1;

    public NotomlParseBuffer(String input) {
        this.input = input;
    }

    public void consumeWhitespace() {
        while (this.i < this.input.length()) {
            char c = this.input.charAt(this.i);
            if (c == ' ' || c == '\t' || c == '\r') {
                ++this.i;
                continue;
            }
            if (c != '\n') break;
            ++this.i;
            ++this.line;
        }
    }

    public String peek(int i) {
        return this.input.substring(this.i, Math.min(this.i + i, this.input.length()));
    }

    public boolean next(String ... find) {
        for (String s : find) {
            if (!this.input.startsWith(s, this.i)) continue;
            return true;
        }
        return false;
    }

    public boolean nextConsume(String ... find) {
        for (String s : find) {
            if (!this.input.startsWith(s, this.i)) continue;
            this.i += s.length();
            return true;
        }
        return false;
    }

    public String readUntil(String ... find) {
        int start = this.i;
        while (this.i < this.input.length()) {
            if (this.input.charAt(this.i) == '\n') {
                ++this.line;
            }
            for (String s : find) {
                if (!this.input.startsWith(s, this.i)) continue;
                return this.input.substring(start, this.i);
            }
            ++this.i;
        }
        return this.input.substring(start);
    }

    public String readUntilConsume(String ... find) {
        int start = this.i;
        while (this.i < this.input.length()) {
            if (this.input.charAt(this.i) == '\n') {
                ++this.line;
            }
            for (String s : find) {
                if (!this.input.startsWith(s, this.i)) continue;
                String read = this.input.substring(start, this.i);
                this.i += s.length();
                return read;
            }
            ++this.i;
        }
        return this.input.substring(start);
    }

    public String readQuoted() {
        char quote = this.input.charAt(this.i);
        if (quote != '\"' && quote != '\'') {
            throw new IllegalStateException("Expected a quote character at the start of the string");
        }
        ++this.i;
        StringBuilder sb = new StringBuilder();
        while (this.i < this.input.length()) {
            char current = this.input.charAt(this.i);
            if (current == '\\') {
                char nextChar;
                if (this.i + 1 < this.input.length() && (nextChar = this.input.charAt(this.i + 1)) == quote) {
                    sb.append(nextChar);
                    this.i += 2;
                    continue;
                }
            } else {
                if (current == quote) {
                    ++this.i;
                    return sb.toString();
                }
                if (current == '\n') {
                    return sb.toString();
                }
            }
            sb.append(current);
            ++this.i;
        }
        return sb.toString();
    }

    public int getLine() {
        return this.line;
    }

    public boolean canRead() {
        return this.i < this.input.length();
    }

    public int getIndex() {
        return this.i;
    }

    public void revertTo(int index) {
        while (this.i > index) {
            --this.i;
            if (this.input.charAt(this.i) != '\n') continue;
            --this.line;
        }
    }
}

