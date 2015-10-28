/*
 * To change this template, choose Tools | Templates
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
@FacesConverter(value = "ConverterTelefone")
public class NumberTelephone implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.equals("")) {
            value = value.toString().replaceAll("[-()]", "");
            value = value.toString().replaceAll("\\s", "");
            value = value.toString().trim();
        }

        if (value.equals("")) {
            return null;
        }
        return Long.parseLong(value.toString());
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }
}
