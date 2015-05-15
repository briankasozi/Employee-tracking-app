package com.bursys.baaja.basis.encryption;

import java.security.MessageDigest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  
 *@Author JosepBrian
 */
public class MD5EncryptionPolicy implements EncryptionPolicy{

    private static Log logger = LogFactory.getLog(MD5EncryptionPolicy.class);

    /**
     * Initializes the class
     * @param intialization parameter
     */
    public void init(String initParm)
    {
        return;
    }

    /**
     * Enccrypts the password, passphrase is ignored
     * @param s the string to encrypt
     * @param passphrase the passphrase for the encryption policy
     */
    public String encrypt(String s, String passphrase)
    {
        return encrypt( s);
    }

    /**
     * Enccrypts the string
     * @param s the string to encrypt
     */
    public String encrypt(String s)
    {
        String result = "";
        byte[] b = s.getBytes();
        MessageDigest currentAlgorithm = null;

        if (s.trim().length() != 0)
        {
            try
            {
                currentAlgorithm = MessageDigest.getInstance("MD5");

                currentAlgorithm.reset();
                currentAlgorithm.update(b);
                byte[] hash = currentAlgorithm.digest();

                StringBuffer hexString = new StringBuffer();
                for (int i=0; i<hash.length; i++)
                {
                    if ((0xFF & hash[i]) < 0x10)
                        hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                    else
                        hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
                result = hexString.toString();
            }
            catch (Exception e) {
                logger.debug(getClass().getName()+".encrypt() - No such algorithm");
            }
        }
        return result;
    }

    /**
     * Decrypts the password - a NOOP since MD5s are one-way
     * @param s the string to dencrypt
     */
    public String decrypt(String s)
    {
        return s;
    }

    /**
     * Decrypts the value - a NOOP since one-way
     * @param s the string to encrypt
     * @param passphrase the passphrase for the encryption policy
     */
    public String decrypt(String s, String passphrase)
    {
        return decrypt( s);
    }

}
