package edu.icet.crm.controller.auth;

import edu.icet.crm.entity.User;
import edu.icet.crm.model.AuthenticationRequest;
import edu.icet.crm.model.AuthenticationResponse;
import edu.icet.crm.model.SignupRequest;
import edu.icet.crm.model.UserDto;
import edu.icet.crm.repository.UserRepository;
import edu.icet.crm.service.auth.AuthService;
import edu.icet.crm.service.jwt.UserService;
import edu.icet.crm.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    private ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        if (authService.hasUserWithEmail(signupRequest.getEmail()))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exist with this email");
        UserDto createdUserDto = authService.signupUser(signupRequest);
        if (createdUserDto == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password");
        }
        final UserDetails userDetails = userService.userDetailService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(authenticationRequest.getEmail());
       final String jwtToken =jwtUtil.generateToken(userDetails);
       AuthenticationResponse authenticationResponse= new AuthenticationResponse();
       if (optionalUser.isPresent()) {
           authenticationResponse.setJwt(jwtToken);
           authenticationResponse.setUserId(optionalUser.get().getId());
           authenticationResponse.setUserRole(optionalUser.get().getUserRole());
       }
       return authenticationResponse;
    }
}
