package com.xubingbing;

import java.util.regex.Pattern;

final class Patterns {
    static final Pattern abcdabcd = Pattern.compile("(\\d)(\\d)(\\d)(\\d)\\1\\2\\3\\4");
    static final Pattern aabbcc = Pattern.compile("(\\d)\\1((?!\\1)\\d)\\2((?!\\2)(?!\\1)\\d)\\3");
    static final Pattern abccba = Pattern.compile("(\\d)((?!\\2)\\d)((?!\\2)(?!\\3)\\d)\\3\\2\\1");
    static final Pattern a3 = Pattern.compile("([\\d])\\1{2,}");
    static final Pattern a4p = Pattern.compile("([\\d])\\1{3,}");
    static final Pattern aabb = Pattern.compile("(\\d)\\1{1}((?!\\1)\\d)\\2{1}");
    static final Pattern abcde = Pattern.compile("(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){4}\\d");
    static final Pattern edcba = Pattern.compile("^\\d(?:(?:0(?=9)|9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){3,})\\d");
    static final Pattern abcd = Pattern.compile("(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){3}\\d");
    static final Pattern less3 = Pattern.compile("^(?=(\\d)\\1*(\\d)(?:\\1|\\2)*(\\d)(?:\\1|\\2|\\3)*$)\\d{11}$");
    static final Pattern one5s = Pattern.compile("^(?=\\d*(\\d)\\d*(?:\\1\\d*){4})\\d{11}$\n");
}
