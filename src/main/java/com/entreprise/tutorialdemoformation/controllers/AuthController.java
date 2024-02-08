package com.entreprise.tutorialdemoformation.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entreprise.tutorialdemoformation.dao.RoleDAO;
import com.entreprise.tutorialdemoformation.dao.UserDAO;
import com.entreprise.tutorialdemoformation.models.ERole;
import com.entreprise.tutorialdemoformation.models.Role;
import com.entreprise.tutorialdemoformation.models.User;
import com.entreprise.tutorialdemoformation.payload.request.LoginRequest;
import com.entreprise.tutorialdemoformation.payload.request.SignupRequest;
import com.entreprise.tutorialdemoformation.payload.response.JwtResponse;
import com.entreprise.tutorialdemoformation.payload.response.MessageResponse;
import com.entreprise.tutorialdemoformation.security.jwt.JwtUtils;
import com.entreprise.tutorialdemoformation.security.service.UserDetailsImpl;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    UserDAO userDAO;

    @Autowired
    RoleDAO roleDAO;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())  
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping(path = "/signup",produces = "application/json")
    public ResponseEntity<?> registerUser (@Valid @RequestBody SignupRequest signupRequest) {
        if (userDAO.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error : UserName is already taken!"));
        }

        if(userDAO.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error : Email is already use!"));
        }

        User authModel = new User(signupRequest.getUsername(), signupRequest.getEmail(),encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null){
            Role userRole = roleDAO.findByName(ERole.ROLE_USER)
            .orElseThrow(()-> new RuntimeException("Error : Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch ((role)) {
                    case "admin":
                        Role adminRole = roleDAO.findByName(ERole.ROLE_ADMINISTRATOR)
                        .orElseThrow(()-> new RuntimeException("Error : Role is not found."));
                        roles.add(adminRole);
                        break;

                    default:
                    Role userRole = roleDAO.findByName(ERole.ROLE_USER)
                    .orElseThrow(()-> new RuntimeException("Error : Role is not found."));
                    roles.add(userRole);
                }
            });
        }

        authModel.setRoles(roles);
        userDAO.save(authModel);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }
}
