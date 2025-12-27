package com.ecommerce.project.controller;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.security.jwt.JwtUtils;
import com.ecommerce.project.security.request.LoginRequest;
import com.ecommerce.project.security.request.SignUpRequest;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.security.response.UserInfoResponse;
import com.ecommerce.project.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(
        name = "Authentication",
        description = "Endpoints for user authentication, registration, and session management"
)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user using username and password and returns a JWT token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(
            @RequestBody LoginRequest loginRequest) {

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        UserInfoResponse response =
                new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles);
        response.setJwtToken(jwtCookie.toString());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(response);
    }

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user account with the specified roles"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Username or email already exists")
    })
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody SignUpRequest signUpRequest) {

        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already taken!"));
        }

        User user = new User(
                signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail()
        );

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> roles.add(
                            roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"))
                    );
                    case "seller" -> roles.add(
                            roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"))
                    );
                    default -> roles.add(
                            roleRepository.findByRoleName(AppRole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"))
                    );
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @Operation(
            summary = "Get current username",
            description = "Returns the username of the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Username retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/username")
    public String currentUserName(Authentication authentication) {
        return authentication != null ? authentication.getName() : "";
    }

    @Operation(
            summary = "Get current user details",
            description = "Returns user information and roles for the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUserDetails(Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        UserInfoResponse response =
                new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Sign out user",
            description = "Logs out the current user and clears the JWT cookie"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User signed out successfully")
    })
    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser() {

        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}
