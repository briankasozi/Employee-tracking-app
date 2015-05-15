package com.bursys.baaja.basis.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 */
public class PropertyFileLoader {
    private static Log log = LogFactory.getLog(PropertyFileLoader.class);

    public synchronized void loadProperties(String fileName, javax.servlet.ServletContext ctx){
        InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
        log.debug("Successfully opened the input stream for baaja properties :: " + fileName);
        
        Properties props = new Properties();
        try {
            props.load(in);
            in.close();
        } catch (IOException e){
            log.error("An error occurred while loading the baaja properties file :: " + e.getMessage(), e);
        }
        Enumeration enum1 = props.propertyNames();
        while (enum1.hasMoreElements()){
            String str = (String) enum1.nextElement();
            ctx.setAttribute(str, props.getProperty(str));
            log.debug("Setting the property :: " + str + " = " + props.getProperty(str));
        }
    }

    public synchronized Properties getProperties(String fileName){
        InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
        log.debug("Successfully opened the input stream for baaja properties :: " + fileName);
        Properties props = new Properties();
        try {
            props.load(in);
            in.close();
        } catch (IOException e){
            log.error("An error occurred while loading the baaja properties file :: " + e.getMessage(), e);
        }
        return props;
    }

}
