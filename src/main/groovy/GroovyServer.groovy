import groovy.json.JsonOutput
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.vertx.groovy.core.http.HttpServerRequest
import org.vertx.groovy.core.http.RouteMatcher
import ru.testvertx.controllers.Cotroller

/**
 * User: dmitry
 * Date: 4/21/13
 */


def server = vertx.createHttpServer()

def routeMatcher = new RouteMatcher()

Map<String, Cotroller> controllers = getControllers()
Closure controllerActionUrlMapping = { HttpServerRequest req ->
    try {
        String controllerName = req.params['controller'] + 'Controller'
        Cotroller controller = controllers[controllerName]
        if (controller == null) throw new RuntimeException("Controller ${controllerName} not found")
        String actionName = req.params['action']
        Map<String, ?> model = controller."${actionName}"(req)

        req.response.putHeader("Content-Type", "text/json;charset=utf-8")
        req.response.end(JsonOutput.toJson(model))
    } catch (Exception e) {
        req.response.statusCode = 500
        req.response.end(e.message)
    }
}

['get', 'post', 'put', 'delete'].each { String restMethod ->
    routeMatcher."${restMethod}"('/:controller/:action', controllerActionUrlMapping)
}

server.requestHandler(routeMatcher.asClosure()).listen(8080, "localhost")

private Map<String, Cotroller> getControllers() {
    ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
    Map<String, Cotroller> controllers = context.getBeansOfType(Cotroller.class);
    return controllers;
}