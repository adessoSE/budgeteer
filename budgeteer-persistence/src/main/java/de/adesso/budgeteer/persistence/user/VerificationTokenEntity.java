package de.adesso.budgeteer.persistence.user;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "VERIFICATION_TOKEN")
public class VerificationTokenEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String token;

  @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private UserEntity userEntity;

  private LocalDateTime expiryDate;

  public VerificationTokenEntity(UserEntity userEntity, String token) {
    this.userEntity = userEntity;
    this.token = token;
    this.expiryDate = LocalDateTime.now().plusHours(24);
  }
}
