package org.philips.hue.tdm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * HueProperties.java
 * 
 * Stores the last known connected IP Address and the last known username.
 * This facilitates automatic bridge connection. 
 *  
 * Also, as the username (for the whitelist) is a random string,  this prevents the need to 
 * pushlink every time the app is started (as the username is read from the properties file).
 *
 */
public final class HueProperties {

    private static final String LAST_CONNECTED_IP   = "LastIPAddress";
    private static final String USER_NAME           = "WhiteListUsername";
    private static final String PROPS_FILE_NAME     = "MyHue.properties";
    private static Properties props=null;
	private static Logger logger = LoggerUtil.getSdkLogger();


    private HueProperties() {
    }
    
    public static void storeLastIPAddress(String ipAddress) {
        props.setProperty(LAST_CONNECTED_IP, ipAddress);
        saveProperties();
    }

    /**
     * Stores the Username (for Whitelist usage). This is generated as a random 16 character string.
     */
    public static void storeUsername(String username) {
        props.setProperty(USER_NAME, username);
        saveProperties();
    }

    /**
     * Returns the stored Whitelist username.
     */
    public static String getUsername() {
        String username = props.getProperty(USER_NAME);        
        return username;
    }

    public static String getLastConnectedIP() {
        return props.getProperty(LAST_CONNECTED_IP);
    }
    
    /** try to load the Java Properties file containing the last connected ip address and username
     * create a new Properties file if there is no existing one yet */
    public static void loadProperties() {
        if (props==null) {
            props=new Properties();
            FileInputStream in;
            
            try {
                in = new FileInputStream(PROPS_FILE_NAME);
                props.load(in);
                in.close();
            } catch (FileNotFoundException ex) {
                saveProperties();
            } catch (IOException e) {
                logger.warning(e.getMessage());
            }
        }
    }

    public static void saveProperties() {
        try {
            FileOutputStream out = new FileOutputStream(PROPS_FILE_NAME);
            props.store(out, null);
            out.close();
        } catch (FileNotFoundException e) {
            logger.warning(e.getMessage());
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    } 

}