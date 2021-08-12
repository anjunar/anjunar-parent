package net.portrix.meld;

import io.undertow.Undertow;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.weld.environment.servlet.Listener;

import javax.servlet.ServletException;
import javax.ws.rs.core.Application;
import java.io.File;

public class MeldServer {

    private final UndertowJaxrsServer server = new UndertowJaxrsServer();

    public MeldServer(Integer port, String host) {
        Undertow.Builder serverBuilder = Undertow
                .builder()
                .addHttpListener(port, host);
        server.start(serverBuilder);
    }

    public static void main(String[] args) throws ServletException {
        MeldServer meldServer = new MeldServer(8080, "localhost");
        File base = new File("./");
        DeploymentInfo deploymentInfo = meldServer.deployApplication("/service", MeldApplication.class)
                .setClassLoader(MeldServer.class.getClassLoader())
                .setContextPath("/")
                .setResourceManager(new FileResourceManager(base, 1024))
                .addWelcomePage("index.html")
                .setDeploymentName("Meld")
                .addListeners(
                        Servlets.listener(Listener.class)
                );
        meldServer.deploy(deploymentInfo);
    }

    public DeploymentInfo deployApplication(String appPath, Class<? extends Application> applicationClass) {
        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.setInjectorFactoryClass("org.jboss.resteasy.cdi.CdiInjectorFactory");
        deployment.setApplicationClass(applicationClass.getName());
        return server.undertowDeployment(deployment, appPath);
    }

    public void deploy(DeploymentInfo deploymentInfo) throws ServletException {
        server.deploy(deploymentInfo);
    }
}