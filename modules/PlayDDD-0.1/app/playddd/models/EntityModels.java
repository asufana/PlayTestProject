package playddd.models;

import javax.persistence.*;

import org.hibernate.proxy.*;

@MappedSuperclass
public abstract class EntityModels<T extends Models> extends Models {
    
    public boolean sameIdentityAs(final T other) {
        if (id() == null) {
            return false;
        }
        return other != null && id().equals(other.id());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null) {
            return false;
        }
        
        if (o.getClass() != this.getClass()) {
            //http://stackoverflow.com/questions/11013138/hibernate-equals-and-proxy
            if (getClassObj(o).isAssignableFrom(getClassObj(this)) == false
                    && getClassObj(this).isAssignableFrom(getClassObj(o)) == false) {
                return false;
            }
        }
        
        final T other = (T) o;
        return sameIdentityAs(other);
    }
    
    private Class getClassObj(final Object obj) {
        //http://stackoverflow.com/questions/1139611/loading-javassist-ed-hibernate-entity
        return obj instanceof HibernateProxy
                ? HibernateProxyHelper.getClassWithoutInitializingProxy(obj)
                : obj.getClass();
    }
    
    @Override
    public int hashCode() {
        return id().hashCode();
    }
}
