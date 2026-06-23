package net.md_5.bungee.util;

import it.unimi.dsi.fastutil.Hash;

import java.util.Locale;

class CaseInsensitiveHashingStrategy implements Hash.Strategy<String> {

    static final CaseInsensitiveHashingStrategy INSTANCE = new CaseInsensitiveHashingStrategy();

    @Override
    public int hashCode(String o) {
        return o == null ? 0 : o.toLowerCase(Locale.ROOT).hashCode();
    }

    @Override
    public boolean equals(String a, String b) {
        return a == null ? b == null : a.equalsIgnoreCase(b);
    }
}
