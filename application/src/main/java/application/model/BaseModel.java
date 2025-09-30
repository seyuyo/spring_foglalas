package application.model;

import java.lang.reflect.Field;

public class BaseModel {
    @Override
    public Object clone() throws CloneNotSupportedException {
        Object returnValue = new Object();
        Field[] fields = this.getClass().getFields();

        for(Field field : returnValue.getClass().getFields()) {
            for(Field thisField : this.getClass().getFields()) {
                if(thisField.getName().equals(field.getName())) {
                    try {
                        field.set(returnValue, thisField.get(this));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        }
        return returnValue;
    }
}

