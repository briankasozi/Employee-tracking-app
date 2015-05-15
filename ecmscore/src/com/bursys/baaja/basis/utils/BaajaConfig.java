
package com.bursys.baaja.basis.utils;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bursys.baaja.basis.constants.MiscConstants;
import com.bursys.baaja.basis.exception.BaajaException;

/**
 * 
 * This class contains all the configuration related 
 * properties .
 * @author JosephBrian
 *
 */
public class BaajaConfig {
    static Log log = LogFactory.getLog(BaajaConfig.class);

    private static BaajaConfig ourInstance = null;
  //  private Properties baajaProps = null;
    private Properties appProps = null;

    /**
     * get N Instance of the BAAJA config
     * @return
     */
    public synchronized static BaajaConfig getInstance() {
        if (ourInstance == null) {
            ourInstance = new BaajaConfig();
            loadAll();

        }
        return ourInstance;
    }

    /**
     * Loads all the application and BAAJA property files.
     * 
     */
    public synchronized static void loadAll() {
        if (ourInstance == null) {
            ourInstance = new BaajaConfig();
        }
        PropertyFileLoader pfl = new PropertyFileLoader();
      //  PropertyDBLoader pdl = new PropertyDBLoader();
      //  ourInstance.setJrdaProps(pfl.getProperties(MiscConstants.BAAJA_PROPERTIES_FILE));
        ourInstance.setAppProps(pfl.getProperties(MiscConstants.APPLICATION_PROPERTIES_FILE));
    //    if (ourInstance.getJrdaProps() == null)
    //        log.error("Failed to upload the Jrda properties");
        if (ourInstance.getAppProps() == null)
            log.error("failed to upload the application properties");

        // now we should get the properties from the database
    /*    if (ourInstance.getJrdaProperty(MiscConstants.BAAJA_PROPERTIES_DB_DS) != null
            && ourInstance.getJrdaProperty(MiscConstants.BAAJA_PROPERTIES_DB_SQL) != null){
            log.info("This is inside the request to load the properties from the database");
            try {
                ourInstance.setAppProps(pdl.getProperties(ourInstance.getJrdaProperty(MiscConstants.BAAJA_PROPERTIES_DB_DS),
                        ourInstance.getJrdaProperty(MiscConstants.BAAJA_PROPERTIES_DB_SQL),
                        ourInstance.getAppProps()));
            } catch(BaajaException e){
                log.error("Exception wile loading properties from the database:", e);
            }
        } else {
            log.info("DID not find the DS config for the proerties:" + ourInstance.getJrdaProperty(MiscConstants.BAAJA_PROPERTIES_DB_DS) + "::" + ourInstance.getJrdaProperty(MiscConstants.BAAJA_PROPERTIES_DB_SQL));
        }*/
    }

  /*  public Properties getJrdaProps() {
        return baajaProps;
    }*/

    public Properties getAppProps() {
        return appProps;
    }

  /*  public void setJrdaProps(Properties baajaProps) {
        this.baajaProps = baajaProps;
    }*/

    public void setAppProps(Properties appProps) {
        this.appProps = appProps;
    }


    private BaajaConfig() {
    }

   /* public boolean isJrdaPropsLoaded(){
        return (null == baajaProps);
    }*/

    public boolean isAppPropsLoaded(){
        return (null == appProps);
    }

  /*  public String getJrdaProperty(String name){
        return baajaProps.getProperty(name);
    }*/

    public String getAppProperty(String name){
        return appProps.getProperty(name);
    }

    public String getProperty(String name){
      /*  if (getJrdaProperty(name) != null)
            return getJrdaProperty(name);*/
        if (getAppProperty(name) != null)
            return getAppProperty(name);
        return null;
    }

    public Integer getIntegerProperty(String name){
       /* if (getJrdaProperty(name) != null)
            return new Integer(getJrdaProperty(name).trim());*/
        if (getAppProperty(name) != null)
            return new Integer(getAppProperty(name).trim());
        return null;
    }

    public int getIntProperty(String name){
      /*  if (getJrdaProperty(name) != null)
            return Integer.parseInt(getJrdaProperty(name).trim());*/
        if (getAppProperty(name) != null)
            return Integer.parseInt(getAppProperty(name).trim());
        return 0;
    }

    public long getLongProperty(String name){
     /*   if (getJrdaProperty(name) != null)
            return Long.parseLong(getJrdaProperty(name).trim());*/
        if (getAppProperty(name) != null)
            return Long.parseLong(getAppProperty(name).trim());
        return 0;
    }

    public Boolean getBooleanProperty(String name){
       /* if (getJrdaProperty(name) != null)
            return Boolean.valueOf(getJrdaProperty(name).trim());*/
        if (getAppProperty(name) != null)
            return Boolean.valueOf(getAppProperty(name).trim());
        return null;
    }

    public boolean getBoolProperty(String name){
       /* if (getJrdaProperty(name) != null)
            return Boolean.valueOf(getJrdaProperty(name).trim()).booleanValue();*/
        if (getAppProperty(name) != null)
            return Boolean.valueOf(getAppProperty(name).trim()).booleanValue();
        return false;
    }
    public Float getFloatProperty(String name){
      /*  if (getJrdaProperty(name) != null)
            return new Float(getJrdaProperty(name).trim());*/
        if (getAppProperty(name) != null)
            return new Float(getAppProperty(name).trim());
        return null;
    }
    
    public Double getDoubleProperty(String name){
       /* if (getJrdaProperty(name) != null)
            return new Double(getJrdaProperty(name).trim());*/
        if (getAppProperty(name) != null)
            return new Double(getAppProperty(name).trim());
        return null;
    }

}

