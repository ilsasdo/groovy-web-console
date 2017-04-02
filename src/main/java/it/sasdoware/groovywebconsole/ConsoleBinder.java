package it.sasdoware.groovywebconsole;

import groovy.lang.Binding;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ilsasdo on 01/04/17.
 */
public interface ConsoleBinder {
    Binding createBinding ();
}
