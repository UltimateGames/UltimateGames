/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.core.webapi;

import me.ampayne2.ultimategames.core.UG;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class JettyServer {
    private Server server;
    private static final int CORE_POOL_SIZE = 2;
    private static final int MAX_CONNECTIONS = 30;
    private static final int KEEP_ALIVE_TIME = 60;

    public JettyServer(UG plugin) throws Exception {
        org.eclipse.jetty.util.log.Log.setLog(new JettyNullLogger());
        server = new Server(plugin.getConfig().getInt("APIPort"));
        server.setHandler(new JettyHandler());
        server.setSessionIdManager(new HashSessionIdManager());

        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(MAX_CONNECTIONS);

        ExecutorThreadPool pool = new ExecutorThreadPool(CORE_POOL_SIZE, MAX_CONNECTIONS, KEEP_ALIVE_TIME, TimeUnit.SECONDS, queue);
        server.setThreadPool(pool);
    }

    public JettyHandler getHandler() {
        return (JettyHandler) server.getHandler();
    }

    public void startServer() throws Exception {
        server.start();
    }

    public void stopServer() throws Exception {
        server.stop();
    }
}
