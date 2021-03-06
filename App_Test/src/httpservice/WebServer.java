package httpservice;

import esale.frontend.common.EsaleFEConfig;
import esale.frontend.controller.HTMLController;
import esale.frontend.controller.APIController;
import me.test.controller.GetController;
import me.test.controller.HomeController;
import me.test.controller.InputInsertController;
import me.test.controller.InsertController;
import me.test.controller.UpdateController;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 *
 * @author tunm
 */
public class WebServer extends Thread {

    private static Logger logger_ = Logger.getLogger(WebServer.class);

    @Override
    public void run() {
        try {
            this.startWebServer();
        } catch (Exception ex) {
            logger_.error("Webserver error", ex);
        }
    }

    public void startWebServer() throws Exception {
        int port = Integer.valueOf(System.getProperty("zport"));
        if (port == 0) {
            System.exit(-1);
        }
        int acceptors = Integer.parseInt(EsaleFEConfig.gJettyThreadPool.getAcceptors());
        int min_threads = Integer.parseInt(EsaleFEConfig.gJettyThreadPool.getMinPool());
        int max_threads = Integer.parseInt(EsaleFEConfig.gJettyThreadPool.getMaxPool());

        if (port == 0) {
            logger_.error("zport not found");
            System.exit(-1);
        }
        logger_.info("get rest listen_port from zport=" + port);

        Server server = new Server();

        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(max_threads);
        threadPool.setMinThreads(min_threads);
        server.setThreadPool(threadPool);

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        connector.setMaxIdleTime(60000);
        connector.setStatsOn(false);
        connector.setLowResourcesConnections(20000);
        connector.setLowResourcesMaxIdleTime(5000);
        connector.setAcceptors(acceptors);
        server.setConnectors(new Connector[]{connector});

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setSessionHandler(new SessionHandler());
        server.setHandler(handler);

        handler.addServlet(HomeController.class, "/home");
        handler.addServlet(APIController.class, "/api");
        handler.addServlet(InsertController.class, "/insert");
        handler.addServlet(UpdateController.class, "/update");
        handler.addServlet(GetController.class, "/get");
        handler.addServlet(InputInsertController.class, "/input-insert");

        server.setStopAtShutdown(true);
        server.setSendServerVersion(true);
        server.start();
        server.join();
    }
}

class ShutdownThread extends Thread {

    private Server server;
    private static Logger logger_ = Logger.getLogger(ShutdownThread.class);

    public ShutdownThread(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        logger_.info("Waiting for shut down!");
        try {
            server.stop();
        } catch (Exception ex) {
            logger_.error(ex.getMessage());
        }
        logger_.info("Server shutted down!");
    }
}
