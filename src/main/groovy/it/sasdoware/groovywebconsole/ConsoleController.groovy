package it.sasdoware.groovywebconsole;

import javax.servlet.http.HttpServletRequest

import org.codehaus.groovy.control.CompilerConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
public class ConsoleController {
	
	@Autowired
	ApplicationContext appContext
	
	@RequestMapping(value="/console", method=RequestMethod.POST)
	public Evaluation console (HttpServletRequest request, @RequestParam("code") String code) {
		println request
		return eval(code, request);
	}
	
	Evaluation eval(String code, request) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream()
		PrintStream out = new PrintStream(baos)

		PrintStream systemOut = System.out
		System.out = out

		Evaluation evaluation = new Evaluation()

		long startTime = System.currentTimeMillis()
		try {
			Binding binding = createBinding(request, out)
			CompilerConfiguration configuration = new CompilerConfiguration()

			GroovyShell groovyShell = new GroovyShell(appContext.getClass().getClassLoader(), binding, configuration)
			evaluation.result = groovyShell.evaluate code
		} catch (Throwable t) {
			evaluation.exception = t
		}

		evaluation.totalTime = System.currentTimeMillis() - startTime
		System.out = systemOut

		evaluation.output = baos.toString('UTF8')
		evaluation
	}

	private Binding createBinding(HttpServletRequest request, PrintStream out) {
		new Binding([
			session          : request.session,
			request          : request,
			ctx              : appContext,
			out              : out
		])
	}
}
