package org.wickedsource.budgeteer.persistence.user;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
public class VerificationTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String token;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity userEntity;

    private Date expiryDate;

    public VerificationTokenEntity() {
        super();
    }

    public VerificationTokenEntity(UserEntity userEntity, String token) {
        super();

        this.userEntity = userEntity;
        this.token = token;
        this.expiryDate = calculateExpiryDate(24 * 60);
    }

    /**
     * Calculates expiryTimeInMinutes on the current date.
     *
     * @param expiryTimeInMinutes after how many minutes the token should expire
     * @return new date with current date + expiryTimeInMinutes
     */
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(calendar.getTime().getTime());
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
