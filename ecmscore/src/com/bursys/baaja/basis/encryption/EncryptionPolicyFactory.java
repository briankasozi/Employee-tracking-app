package com.bursys.baaja.basis.encryption;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bursys.baaja.basis.exception.BaajaClassNotFoundException;
import com.bursys.baaja.basis.exception.BaajaException;
import com.bursys.baaja.basis.utils.BaajaConfig;

/**
 * @josephBrian
 */

/**
 * Factory for creating the EncryptionPolicy interface instances.  This class is token
 * driven and provides a mechanism for the creation of concrete instances that implement
 * EncryptionPolicy
 *
 */


public class EncryptionPolicyFactory {
    private static Log log = LogFactory.getLog(EncryptionPolicyFactory.class);
    /**
     * A default property name to utilize for the no argument create method
     */
    protected static final String DEFAULT_PROPERTY_NAME = "com.bursys.baaja.basis.encryption";
    /**
     * Default class name if the property is not found
     */
    protected static final String DEFAULT_INTERFACE_CLASS = "com.bursys.baaja.basis.encryption.MD5EncryptionPolicy";

    /**
     * Create a new instance of a Publisher
     */
    public static EncryptionPolicy createEncryptionPolicy(String initParm) throws BaajaException{
        BaajaConfig config = BaajaConfig.getInstance();
        EncryptionPolicy xface = null;
        String className = config.getProperty(DEFAULT_PROPERTY_NAME);
        xface = generateEncryptionPolicy(className);
        xface.init( initParm);
        return xface;
    }

        /**
         * Create a new instance of a EncryptionPolicy
         * @param interfaceType theclass name of the instance to create
         * @return the concreate instance
         */

        public static EncryptionPolicy createEncryptionPolicy(String interfaceType, String initParm) throws BaajaException
        {
            EncryptionPolicy xface = null;
            String clsToken = null;
            if ( interfaceType != null && !interfaceType.equals(""))
            {
                clsToken = interfaceType;
            }
            else
            {
                clsToken = DEFAULT_INTERFACE_CLASS;
            }
            xface = generateEncryptionPolicy(clsToken);
            xface.init(initParm);
            return xface;
        }


        /**
         * Generates the actual interface instance with a call to initialize the interface
         * @param className the class of the instance
         * @return the concreate instance of EncryptionPolicy
         */
        protected static EncryptionPolicy generateEncryptionPolicy(String className) throws BaajaClassNotFoundException{

            EncryptionPolicy xface = null;
            try
            {
                log.debug("instance of TT:" + className + " being created.");
 //               xface = (EncryptionPolicy) new MD5EncryptionPolicy();
                xface = (EncryptionPolicy)Class.forName( className).newInstance();
                log.debug("instance of " + xface.getClass().getName() + " created.");

            }catch (ClassNotFoundException e) {
                log.error("Could not create an instance", e);
                throw new BaajaClassNotFoundException(e.getMessage());
            } catch (IllegalAccessException e) {
                log.error("Could not create an instance", e);
                throw new BaajaClassNotFoundException(e.getMessage());
            } catch (InstantiationException e) {
                log.error("Could not create an instance", e);
                throw new BaajaClassNotFoundException(e.getMessage());
            }

            return xface;
        }
}
