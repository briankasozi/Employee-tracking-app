
package com.bursys.baaja.basis.exception;
/**
 * General framework exception
 * This is a customised exception which all the other exceptions extend.
 * 
 */
public class BaajaException extends Exception  {
	/**constructor with two arguments
	 * @param message the message to be displayed to the user
	 * @param cause the cause of the exception
	 */  
	
	public BaajaException(String message, Throwable cause) {
        super(message, cause);
    }
	   /**constructor with one argument
     * @param cause the cause of the exception
     */
	
    public BaajaException(Throwable cause) {
        super(cause);
    }
    

    public BaajaException() {
    }
    /**Single argument constructor
     * @param s message to be displayed
     */
    public BaajaException(String s) {
        super(s);
    }
}
