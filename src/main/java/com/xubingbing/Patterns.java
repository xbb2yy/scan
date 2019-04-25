package com.xubingbing;

import java.util.regex.Pattern;

public final class Patterns {
    public static final Pattern abcdabcd = Pattern.compile("(\\d)(\\d)(\\d)(\\d)\\1\\2\\3\\4");
}
