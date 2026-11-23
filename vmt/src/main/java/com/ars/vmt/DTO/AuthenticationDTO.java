/**
 * 11/23/2026   Class created
 */

package com.ars.vmt.DTO;

public class AuthenticationDTO {
    private int userId;
    private String email;
    private String passHash;

    public AuthenticationDTO(int userId, String email, String passHash) {
        this.userId = userId;
        this.email = email;
        this.passHash = passHash;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassHash() {
        return passHash;
    }

}
