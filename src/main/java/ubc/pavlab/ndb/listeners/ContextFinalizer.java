/*
 * The ndb project
 * 
 * Copyright (c) 2015 University of British Columbia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ubc.pavlab.ndb.listeners;

import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

/**
 * 
 * Context finalization to close threads (MySQL memory leak prevention).
 * This solution combines the best techniques described in the linked Stack
 * Overflow answer.
 * 
 * @see <a href="https://stackoverflow.com/questions/11872316/tomcat-guice-jdbc-memory-leak">Tomcat Guice/JDBC Memory
 *      Leak</a>
 * 
 * @author mjacobson
 * @version $Id$
 */
public class ContextFinalizer
        implements ServletContextListener {

    private static final Logger log = Logger.getLogger( ContextFinalizer.class );

    /**
     * Information for cleaning up a thread.
     */
    private class ThreadInfo {

        /**
         * Name of the thread's initiating class.
         */
        private final String name;

        /**
         * Cue identifying the thread.
         */
        private final String cue;

        /**
         * Name of the method to stop the thread.
         */
        private final String stop;

        /**
         * Basic constructor.
         * 
         * @param n Name of the thread's initiating class.
         * @param c Cue identifying the thread.
         * @param s Name of the method to stop the thread.
         */
        ThreadInfo( final String n, final String c, final String s ) {
            this.name = n;
            this.cue = c;
            this.stop = s;
        }

        /**
         * @return the name
         */
        public String getName() {
            return this.name;
        }

        /**
         * @return the cue
         */
        public String getCue() {
            return this.cue;
        }

        /**
         * @return the stop
         */
        public String getStop() {
            return this.stop;
        }
    }

    /**
     * List of information on threads required to stop. This list may be
     * expanded as necessary.
     */
    private List<ThreadInfo> threads = Arrays.asList(
            // Special cleanup for MySQL JDBC Connector.
            new ThreadInfo(
                    "com.mysql.jdbc.AbandonedConnectionCleanupThread", //$NON-NLS-1$
                    "Abandoned connection cleanup thread", //$NON-NLS-1$
                    "shutdown" //$NON-NLS-1$
    ) );

    @Override
    public void contextInitialized( final ServletContextEvent sce ) {
        // No-op.
    }

    @Override
    public final void contextDestroyed( final ServletContextEvent sce ) {

        // Deregister all drivers.
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while ( drivers.hasMoreElements() ) {
            Driver d = drivers.nextElement();
            try {
                DriverManager.deregisterDriver( d );
                log.info(
                        String.format(
                                "Driver %s deregistered", //$NON-NLS-1$
                                d ) );
            } catch ( SQLException e ) {
                log.warn(
                        String.format(
                                "Failed to deregister driver %s", //$NON-NLS-1$
                                d ),
                        e );
            }
        }

        // Handle remaining threads.
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray( new Thread[threadSet.size()] );
        for ( Thread t : threadArray ) {
            for ( ThreadInfo i : this.threads ) {
                if ( t.getName().contains( i.getCue() ) ) {
                    synchronized ( t ) {
                        try {
                            Class<?> cls = Class.forName( i.getName() );
                            if ( cls != null ) {
                                Method mth = cls.getMethod( i.getStop() );
                                if ( mth != null ) {
                                    mth.invoke( null );
                                    log.info(
                                            String.format(
                                                    "Connection cleanup thread %s shutdown successfully.", //$NON-NLS-1$
                                                    i.getName() ) );
                                }
                            }
                        } catch ( Throwable thr ) {
                            log.warn(
                                    String.format(
                                            "Failed to shutdown connection cleanup thread %s: ", //$NON-NLS-1$
                                            i.getName(),
                                            thr.getMessage() ) );
                            thr.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
