/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Jeison
 */
@FacesConverter(value = "ConverterStringToInt")
public class StringToInt implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        int number = Integer.parseInt(value.toString());
        return number;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object value) {
        return value.toString();
    }

}
