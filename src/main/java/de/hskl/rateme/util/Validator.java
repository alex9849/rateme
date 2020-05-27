package de.hskl.rateme.util;

import de.hskl.rateme.model.RatemeDbException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class Validator {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Required {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Regex {
        public String regex();
    }

    public static void validate(Object object) {
        for(Field field : object.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                if(field.isAnnotationPresent(Required.class) && field.get(object) == null) {
                    throw new RatemeDbException(object.getClass().getName() + "requires " + field.getName() + " to be a non-null value!");
                }
                if(field.isAnnotationPresent(Regex.class) && field.get(object) != null) {
                    if(field.getType().equals(String.class)) {
                        String string = (String) field.get(object);
                        String regex = field.getAnnotation(Regex.class).regex();
                        if(!string.matches(regex)) {
                            throw new RatemeDbException(object.getClass().getName() + "requires " + field.getName() + " to match the Regex \"" + regex + "\"!");
                        }
                    }
                }
                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                throw new RatemeDbException("Could not validate the field " + field.getName() + "on an " + object.getClass().getName() + "-Object!", e);
            }

        }
    }
}
