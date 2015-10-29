
package com.geniusgithub.contact.util;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class ReflectLoadMethod {

    public static boolean isClassExisted(String cName) {
        Class cls = null;
        try {
            cls = Class.forName(cName);
        } catch (Exception e) {
            System.err.println(e);
            return false;
        } finally {
            if (cls != null)
                return true;
            else
                return false;
        }
    }

    public static boolean isFieldExisted(String cName, String fieldName) {
        try {
            Class cls = Class.forName(cName);
            cls.getDeclaredField(fieldName);
            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public static boolean isMethodExisted(String cName, String methodName, Class... parameterTypes) {
        try {
            Class cls = Class.forName(cName);
            cls.getDeclaredMethod(methodName, parameterTypes);
            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public static Object MethodInvoke(Object invokeObj, String cName, String methodName,
            Class[] paramTypes, Object[] argList) {
        Object retObject = null;
        try {
            Class cls = Class.forName(cName);
            Method meth = cls.getDeclaredMethod(methodName, paramTypes);
            meth.setAccessible(true);
            retObject = meth.invoke(invokeObj, argList);
        } catch (Exception e) {
            System.err.println(e);
        }
        return retObject;
    }
    
    public static Object getObject(Object invokeObj, String cName, String fieldName) {
        try {
            Class cls = Class.forName(cName);            
            Field field = cls.getDeclaredField(fieldName);  
            field.setAccessible(true);
            return field.get(invokeObj);           
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
}
