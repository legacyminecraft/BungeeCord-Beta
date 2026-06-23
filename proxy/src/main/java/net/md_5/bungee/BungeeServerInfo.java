package net.md_5.bungee;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@RequiredArgsConstructor
public class BungeeServerInfo implements ServerInfo {

    @Getter
    private final String name;
    @Getter
    private final InetSocketAddress address;
    private final Collection<ProxiedPlayer> players = new ArrayList<>();
    @Getter
    private final String motd;
    @Getter
    private final boolean restricted;

    @Synchronized("players")
    public void addPlayer(ProxiedPlayer player) {
        players.add(player);
    }

    @Synchronized("players")
    public void removePlayer(ProxiedPlayer player) {
        players.remove(player);
    }

    @Synchronized("players")
    @Override
    public Collection<ProxiedPlayer> getPlayers() {
        return Collections.unmodifiableCollection(players);
    }

    @Override
    public boolean canAccess(CommandSender player) {
        Preconditions.checkNotNull(player, "player");
        return !restricted || player.hasPermission("bungeecord.server." + name);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ServerInfo) && Objects.equals(getAddress(), ((ServerInfo) obj).getAddress());
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }
}
