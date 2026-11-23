/**
 * 11/23/2026   Class created
 */

package com.ars.vmt.DTO;

public class UserDTO {
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String joinDt;

    public UserDTO(int userId, String firstName, String lastName, String email, String joinDt) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.joinDt = joinDt;
    }

    public UserDTO() {
        userId = -1;
        firstName = null;
        lastName = null;
        email = null;
        joinDt = null;
    }

    public void setId(int userId) {
        this.userId = userId;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setJoinDt(String joinDt) {
        this.joinDt = joinDt;
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getJoinDt() {
        return joinDt;
    }

}
