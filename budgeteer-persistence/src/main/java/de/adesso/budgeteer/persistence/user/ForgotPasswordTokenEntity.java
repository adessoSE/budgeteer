package de.adesso.budgeteer.persistence.user;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "FORGOT_PASSWORD_TOKEN")
public class ForgotPasswordTokenEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String token;

  @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private UserEntity userEntity;

  private LocalDateTime expiryDate;

  public ForgotPasswordTokenEntity(UserEntity userEntity, String token) {
    this.userEntity = userEntity;
    this.token = token;
    this.expiryDate = LocalDateTime.now().plusHours(24);
  }

  public UserEntity getUserEntity() {
    return userEntity;
  }

  public void setUserEntity(UserEntity userEntity) {
    this.userEntity = userEntity;
  }

  public LocalDateTime getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(LocalDateTime expiryDate) {
    this.expiryDate = expiryDate;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
