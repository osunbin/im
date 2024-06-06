package com.bin.im.common.mini.json;

/**
 * Utility method to escape characters for fields used in a Json.
 */
public final class JsonEscape {

    private JsonEscape() {

    }

    public static void writeEscaped(StringBuilder target, String source) {
        target.append('"');
        int length = source.length();
        int start = 0;
        for (int index = 0; index < length; index++) {
            char[] replacement = JsonWriter.getReplacementChars(source.charAt(index));
            if (replacement != null) {
                target.append(source, start, index);
                target.append(replacement);
                start = index + 1;
            }
        }
        target.append(source, start, length);
        target.append('"');
    }

    public static void writeEscaped(StringBuilder stringBuilder, char c) {
        stringBuilder.append('"');
        char[] replacement = JsonWriter.getReplacementChars(c);
        if (replacement != null) {
            stringBuilder.append(replacement);
        } else {
            stringBuilder.append(c);
        }
        stringBuilder.append('"');
    }
}
