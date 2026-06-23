package net.md_5.bungee.api;

import com.google.common.base.Preconditions;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class AbstractReconnectHandler implements ReconnectHandler {

    @Override
    public ServerInfo getServer(ProxiedPlayer player) {
        ServerInfo server = getStoredServer(player);
        if (server == null) {
            server = ProxyServer.getInstance().getServerInfo(player.getPendingConnection().getListener().getDefaultServer());
            Preconditions.checkState(server != null, "Default server not defined");
        }

        return server;
    }

    protected abstract ServerInfo getStoredServer(ProxiedPlayer player);
}
