package com.bursys.baaja.basis.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * This class provides generally used functions for data
 * validation such as empty string, etc.
 */
public class Validator {
    /**
     * Test for empty or null String
     * @param s String
     * @return boolean
     */
    public static boolean isNullOrEmpty(String s) {
        return ((s == null) || (s.trim().length() < 1 || s.trim().toUpperCase().equals("NULL")));
    }
    
    public static boolean isNullOrEmptyInt(Integer s) {
        return ((s == null) || (s<=0));
    }


    /**
     * Validate an String value for range, mostly used for data captured from forms
     * @param s pass in the string to be validated
     * @param from int value for starting of the range
     * @param to int value for end of the range
     * @return boolean
     */
    public static boolean isIntegerBetween(String s, int from, int to) {
        if (!Validator.isNullOrEmpty(s)) {
            try {
                Integer i = new Integer(s);
                if (i.intValue() >= from && i.intValue() <= to) {
                    return true;
                }
                else return false;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Validate an String value for range, mostly used for data captured from forms
     * @param s pass in the string to be validated
     * @return boolean
     */
    public static boolean isIntegerGreaterThanZero(String s) {
        if (!Validator.isNullOrEmpty(s)) {
            try {
                Integer i = new Integer(s);
                if (i.intValue() > 0) {
                    return true;
                }
                else return false;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isNotNullGreaterThanZero(Integer it) {
        if ((it != null) && (it.intValue() > 0)) {
            return true;
        }
        return false;
    }

    public static boolean isNotNullGreaterThanZero(Long value) {
      if ((value != null) && (value.intValue() > 0)) {
          return true;
      }
      return false;
  }

    /**
     * Validate if a string is a long value
     * @param s
     * @return
     */
    public static boolean isLong(String s) {
        if (!Validator.isNullOrEmpty(s)) {
            try {
                Long i = new Long(s);
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * Validate if a string is double value
     * @param s
     * @return
     */
    public static boolean isDouble(String s) {
        if (!Validator.isNullOrEmpty(s)) {
            try {
                double d = Double.parseDouble(s);
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    
    public static boolean isFloat(String s) {
        if (!Validator.isNullOrEmpty(s)) {
            try {
                float d = Float.parseFloat(s);
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
    /**
     * Validate is a string is a date value
     * @param s
     * @return
     */
    public static boolean isDate(String s) {
        if (!Validator.isNullOrEmpty(s)) {
            try {
                DateFormat f = DateFormat.getDateInstance(DateFormat.SHORT);
                Date d = f.parse(s);
                if (d == null) {
                    return false;
                }

            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
        return true;

    }

    /**
     * Validate if the string is a short US Zip code
     * @param s
     * @return
     */
    public static boolean isShortUSZip(String s) {
        if (!Validator.isNullOrEmpty(s)) {
            return (isIntegerBetween(s.trim(),0,99999) && s.trim().length() == 5);
        } else {
            return false;
        }
    }

}
