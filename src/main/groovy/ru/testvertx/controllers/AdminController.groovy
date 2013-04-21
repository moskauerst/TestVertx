package ru.testvertx.controllers

import org.springframework.stereotype.Component
import org.vertx.groovy.core.http.HttpServerRequest

/**
 * User: dmitry
 * Date: 4/21/13
 */
@Component
class AdminController implements Cotroller {

    def index(HttpServerRequest req) {
        req.params
    }
}
