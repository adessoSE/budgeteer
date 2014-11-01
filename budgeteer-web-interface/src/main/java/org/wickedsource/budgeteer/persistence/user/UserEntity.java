package org.wickedsource.budgeteer.persistence.user;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UNIQUE_USER_NAME", columnNames = {"name"})
})
public class UserEntity {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    @Column(length = 512)
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
