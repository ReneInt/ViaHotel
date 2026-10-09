package dtos.auth;

import java.io.Serializable;
/**
 * @author Troels
 */
public record UpdatePasswordRequest(String email, String oldPassword, String newPassword) implements Serializable
{
  @Override
  public String toString()
  {
    return "UpdatePasswordRequest{" +
        "email='" + email + '\'' +
        ", oldPassword='" + oldPassword + '\'' +
        ", newPassword='" + newPassword + '\'' +
        '}';
  }
}