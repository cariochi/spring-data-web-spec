package com.cariochi.spec.spel;

import java.util.regex.Pattern;

public final class SpecInfixRewriter {

    private static final Pattern AND = Pattern.compile("(?i)(\\bAND\\b|&&)");
    private static final Pattern OR = Pattern.compile("(?i)(\\bOR\\b|\\|\\|)");
    private static final Pattern NOT = Pattern.compile("(?i)\\bNOT\\s*");
    private static final Pattern BANG = Pattern.compile("!\\s*");

    public static String toArithmetic(String expr) {
        String s = expr;
        s = AND.matcher(s).replaceAll("*");
        s = OR.matcher(s).replaceAll("+");
        s = BANG.matcher(s).replaceAll("-");
        s = NOT.matcher(s).replaceAll("-");
        return s;
    }
}
