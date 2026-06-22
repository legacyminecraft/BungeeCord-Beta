package net.md_5.bungee.api.score;

import lombok.Data;
import lombok.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
public class Team {

    @NonNull
    private final String name;
    private String displayName;
    private String prefix;
    private String suffix;
    private boolean friendlyFire;
    private Set<String> players = new HashSet<>();

    public Collection<String> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public void addPlayer(String name) {
        players.add(name);
    }

    public void removePlayer(String name) {
        players.remove(name);
    }
}
