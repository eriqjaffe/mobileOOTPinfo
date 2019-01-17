package com.mobileootpinfo.mobileootpinfo.util;

import com.mobsandgeeks.saripaar.annotation.ValidateUsing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ValidateUsing(ValidateHTMLPathRule.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateHTMLPath {
    public int messageResId()   default -1;                     // Mandatory attribute
    public String message()     default "Oops... too pricey";   // Mandatory attribute
    public int sequence()       default -1;                     // Mandatory attribute

    public String url();                         // Your attributes
}

