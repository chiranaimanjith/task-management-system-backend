package edu.icet.crm.model;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}
