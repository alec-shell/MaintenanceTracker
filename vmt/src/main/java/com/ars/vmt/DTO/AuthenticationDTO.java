/**
 * 11/23/2026   Class created
 */

package com.ars.vmt.DTO;

public class AuthenticationDTO {
    private int userId;
    private String email;

    public AuthenticationDTO(int userId, String email, String passHash) {
        this.userId = userId;
        this.email = email;
    }

    public AuthenticationDTO() {
        userId = -1;
        email = null;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Email: "); sb.append(this.email);
        sb.append("\nUserId: "); sb.append(this.userId);

        return sb.toString();
    }

}
