package playddd.models;

import javax.persistence.*;

import org.hibernate.annotations.*;
import org.joda.time.*;

import play.db.jpa.*;

@MappedSuperclass
public abstract class Models extends GenericModel {
    
    public static final Integer DISABLE = 0;
    public static final Integer ENABLE = 1;
    
    @Id
    @GeneratedValue
    protected Long id;
    
    public Long id() {
        return id;
    }
    
    @Override
    public Object _key() {
        return id();
    }
    
    //Optimistic locking
    @Version
    protected Long version;
    
    @Column(nullable = false)
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    protected DateTime createDate;
    
    public DateTime createDate() {
        return createDate;
    }
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    protected DateTime modifyDate;
    
    public DateTime modifyDate() {
        return modifyDate;
    }
    
    @Column(nullable = false)
    protected int disable = DISABLE;
    
    public boolean isDisable() {
        return disable == DISABLE
                ? false
                : true;
    }
    
    public void enable() {
        disable = DISABLE;
    }
    
    public void disable() {
        disable = ENABLE;
    }
    
    @PrePersist
    protected void prePersist() {
        createDate = new DateTime();
        modifyDate = createDate();
    }
    
    @PreUpdate
    protected void preUpdate() {
        modifyDate = new DateTime();
    }
    
    @Override
    public <T extends JPABase> T save() {
        isSatisfied();
        _save();
        return (T) this;
    }
    
    public abstract void isSatisfied();
    
}
