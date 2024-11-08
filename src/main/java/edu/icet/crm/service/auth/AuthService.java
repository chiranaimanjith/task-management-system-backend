package edu.icet.crm.service.auth;

import edu.icet.crm.model.SignupRequest;
import edu.icet.crm.model.UserDto;

public interface AuthService {
    UserDto signupUser(SignupRequest signupRequest);
    boolean hasUserWithEmail(String email);
}
