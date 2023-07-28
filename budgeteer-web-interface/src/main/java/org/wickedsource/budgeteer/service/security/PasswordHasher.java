package org.wickedsource.budgeteer.service.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHasher implements PasswordEncoder {
  @Override
  public String encode(CharSequence rawPassword) {
    return hash(rawPassword.toString());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return encode(rawPassword.toString()).equals(encodedPassword);
  }

  @Override
  public boolean upgradeEncoding(String encodedPassword) {
    return true;
  }

  private String hash(String plain) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-512");
      byte[] hash = digest.digest(plain.getBytes());
      return new String(Hex.encodeHex(hash));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}
