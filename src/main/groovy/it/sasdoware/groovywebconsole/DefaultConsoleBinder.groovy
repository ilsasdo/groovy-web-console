package it.sasdoware.groovywebconsole

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest

/**
 * Created by ilsasdo on 01/04/17.
 */
@Component
@Scope(value="request", proxyMode = ScopedProxyMode.INTERFACES)
class DefaultConsoleBinder implements ConsoleBinder {

    @Autowired
    HttpServletRequest request

    @Autowired
    ApplicationContext appContext

    @Override
    Binding createBinding () {
        return new Binding([
                session          : request.session,
                request          : request,
                ctx              : appContext
        ])
    }
}
