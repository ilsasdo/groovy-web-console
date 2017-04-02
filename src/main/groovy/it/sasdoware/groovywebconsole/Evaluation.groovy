package it.sasdoware.groovywebconsole;

class Evaluation {

    private Object result
    private Throwable exception
    Long totalTime
    String output

    void setException(Throwable exception) {
        this.exception = exception
    }

    void setResult (Object result) {
        this.result = result
    }

    String getResultAsString() {
         return result?.close()
    }

    String getStackTraceAsString() {
		if (exception == null)
			return null
        StringWriter sw = new StringWriter()
        new PrintWriter(sw).withWriter { exception.printStackTrace it }
        sw.toString()
    }

    Integer getExceptionLineNumber() {
        Integer scriptLine = null
        if (exception) {
            def m = stackTraceAsString =~ /at Script\d+\.run\(Script\d+\.groovy:(\d+)\)/
            if (m) {
                scriptLine = m[0][1] as Integer
            }
        }
        scriptLine
    }

}
