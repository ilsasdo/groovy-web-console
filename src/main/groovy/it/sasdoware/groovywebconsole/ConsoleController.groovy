package it.sasdoware.groovywebconsole

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RestController

import org.codehaus.groovy.control.CompilerConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

@RestController
class ConsoleController {

    @Autowired
    ApplicationContext appContext

    @Autowired
    ConsoleBinder consoleBinder

    @Value("\${groovywebconsole.path}")
    String groovyWebConsole

    @RequestMapping(value = "\${groovywebconsole.path}", method = RequestMethod.POST)
    Evaluation console (@RequestParam("code") String code) {
        return eval(code)
    }

    @RequestMapping(value = "\${groovywebconsole.path}", method = RequestMethod.GET)
    String consolePage () {
        InputStream inputStream = this.getClass().getResourceAsStream("/groovy-web-console.html")
        String htmlPage = inputStream.text
        inputStream.close()
        return htmlPage.replace("\${groovywebconsole.path}", groovyWebConsole)
    }

    Evaluation eval (String code) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        PrintStream out = new PrintStream(baos)

        PrintStream systemOut = System.out
        System.out = out

        Evaluation evaluation = new Evaluation()

        long startTime = System.currentTimeMillis()
        try {
            Binding binding = consoleBinder.createBinding()
            CompilerConfiguration configuration = new CompilerConfiguration()

            GroovyShell groovyShell = new GroovyShell (appContext.getClass().getClassLoader(), binding, configuration)
            evaluation.result = groovyShell.evaluate (code)
        } catch (Throwable t) {
            evaluation.exception = t
        }

        evaluation.totalTime = System.currentTimeMillis() - startTime
        System.out = systemOut

        evaluation.output = baos.toString('UTF8')

        return evaluation
    }
}