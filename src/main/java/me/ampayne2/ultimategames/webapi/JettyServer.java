/*
 * This file is part of UltimateGames.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.webapi;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import me.ampayne2.ultimategames.UltimateGames;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;

public class JettyServer {

    private Server server;
    public JettyServer(UltimateGames plugin) throws Exception {
        org.eclipse.jetty.util.log.Log.setLog(new JettyNullLogger());
        server = new Server(plugin.getConfig().getInt("APIPort"));
        server.setHandler(new JettyHandler());
        server.setSessionIdManager(new HashSessionIdManager());

        int maxconnections = 30;
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(maxconnections);

        ExecutorThreadPool pool = new ExecutorThreadPool(2, maxconnections, 60, TimeUnit.SECONDS, queue);
        server.setThreadPool(pool);
        server.start();
        //server.join();
    }

    public JettyHandler getHandler() {
        return (JettyHandler) server.getHandler();
    }
}
