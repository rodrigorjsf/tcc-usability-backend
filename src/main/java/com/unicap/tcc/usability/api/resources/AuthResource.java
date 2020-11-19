package com.unicap.tcc.usability.api.resources;

import com.unicap.tcc.usability.api.exception.SenhaInvalidaException;
import com.unicap.tcc.usability.api.models.ConfirmationToken;
import com.unicap.tcc.usability.api.models.User;
import com.unicap.tcc.usability.api.models.dto.CredentialDTO;
import com.unicap.tcc.usability.api.models.dto.TokenDTO;
import com.unicap.tcc.usability.api.models.dto.UserDTO;
import com.unicap.tcc.usability.api.repository.ConfirmationTokenRepository;
import com.unicap.tcc.usability.api.repository.UserRepository;
import com.unicap.tcc.usability.api.security.jwt.JwtService;
import com.unicap.tcc.usability.api.service.MailSender;
import com.unicap.tcc.usability.api.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@Api("Auth API")
@RequiredArgsConstructor
public class AuthResource {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final MailSender mailSender;


    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Criação de novo usuário")
    @ApiResponse(code = 201, message = "Cadastro efetuado com sucesso.")
    public ResponseEntity<UserDTO> save(@RequestBody @Valid User user) {
        var oldUser = userService.findByEmail(user.getEmail());
        if (Objects.nonNull(oldUser)) {
            return ResponseEntity.noContent().build();
        }
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        User newUser = userService.save(user);
        ConfirmationToken confirmationToken = ConfirmationToken.builder().user(newUser).build();

        confirmationToken = confirmationTokenRepository.save(confirmationToken);
        var optionalToken = confirmationTokenRepository.findUidById(confirmationToken.getId());
        String token;
        if (optionalToken.isPresent()) {
            token = optionalToken.get();
            String subject = "ValidUsabilityAssessment - Complete Registration!";
            String url = "http://localhost:8084/api/auth/confirm-account?token=" + token;
            String body = "To confirm your account, please click here: "
                    + "<a href=" + "\"" + url + "\"" + ">Confirm</a>";
            mailSender.send(new String[]{user.getEmail()}, subject, body);
            return ResponseEntity.ok()
                    .body(UserDTO.builder()
                            .login(newUser.getLogin())
                            .email(newUser.getEmail())
                            .admin(newUser.getAdmin())
                            .build());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    @ApiOperation("User login")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Login successfully."),
            @ApiResponse(code = 401, message = "Incorrect password or unregistered user.")
    })
    public ResponseEntity<TokenDTO> auth(@RequestBody CredentialDTO credentialDTO) {
        try {
            User user = User.builder()
                    .login(credentialDTO.getLogin())
                    .password(credentialDTO.getPassword())
                    .build();
            User registeredUser;
            Optional<User> registeredUserLogin = userRepository.findByLogin(user.getLogin());
            Optional<User> registeredUserEmail = userRepository.findByEmail(user.getLogin());
            if (registeredUserLogin.isEmpty() && registeredUserEmail.isEmpty()) {
                throw new UsernameNotFoundException("User not found.");
            } else
                registeredUser = registeredUserLogin.orElseGet(registeredUserEmail::get);
            user.setAdmin(!Objects.isNull(registeredUser.getAdmin()) && registeredUser.getAdmin());
            UserDetails authenticatedUser = userService.auth(user);
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok()
                    .body(new TokenDTO(user.getLogin(), token, authenticatedUser.getAuthorities()));
        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/request-pass")
    @ApiOperation("Lista todos os usuários")
    public ResponseEntity<List<UserDTO>> requestPass() {
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        try {
            List<UserDTO> userDTO = new ArrayList<>();
            userList.forEach(user -> {
                if (Objects.isNull(user.getAdmin())) {
                    user.setAdmin(false);
                }
                UserDTO dto = UserDTO.builder()
                        .login(user.getLogin())
                        .email(user.getEmail())
                        .admin(user.getAdmin())
                        .build();
                userDTO.add(dto);
            });
            return ResponseEntity.ok()
                    .body(userDTO);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/reset-pass")
    @ApiOperation("Lista todos os usuários")
    public ResponseEntity<List<UserDTO>> resetPass() {
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        try {
            List<UserDTO> userDTO = new ArrayList<>();
            userList.forEach(user -> {
                if (Objects.isNull(user.getAdmin())) {
                    user.setAdmin(false);
                }
                UserDTO dto = UserDTO.builder()
                        .login(user.getLogin())
                        .email(user.getEmail())
                        .admin(user.getAdmin())
                        .build();
                userDTO.add(dto);
            });
            return ResponseEntity.ok()
                    .body(userDTO);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping(value = "/confirm-account")
    public ResponseEntity<UserDTO> confirmUserAccount(@RequestParam("token") String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByUid(UUID.fromString(confirmationToken));

        if (Objects.nonNull(token)) {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setEnabled(true);
            userRepository.save(user);
            ResponseEntity.ok().body(UserDTO.builder()
                    .admin(user.getAdmin())
                    .email(user.getEmail())
                    .isEnable(user.isEnabled())
                    .login(user.getLogin())
                    .build());
        }
        return ResponseEntity.badRequest().build();
    }
}
