package models.users.vos;

import javax.persistence.*;

public class MailAddress extends SingleStringValueObject {
    
    @Column(nullable = false)
    private final String mailAddress;
    
    public MailAddress(final String value) {
        super(value);
        mailAddress = value;
    }
    
    @Override
    public void isSatisfied(final String value) {
        return;
    }
    
}
