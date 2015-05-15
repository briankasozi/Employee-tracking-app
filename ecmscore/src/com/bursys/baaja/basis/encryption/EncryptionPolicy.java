package com.bursys.baaja.basis.encryption;

/**

 @author JosephBrian

 * 
 */
public interface EncryptionPolicy {
    /**
     * Initializes the interface
     * @param intialization parameter
     */
    public void init(String initParm);

	/**
	 * Method to encrypt.  Usually one way since no pass phrase is supplied
	 * @param value the value to encrypt
	 */
	public String encrypt( String value);

	/**
	 * Method to encrypt
	 * @param value the value to encrypt
	 * @param passPhrase pass phrase used for key
	 */
	public String encrypt( String value, String passPhrase);

	/**
	 * Method to decrypt
	 * @param value the value to encrypt
	 * @param passPhrase pass phrase used for key
	 */
	public String decrypt( String value, String passPhrase);
}
