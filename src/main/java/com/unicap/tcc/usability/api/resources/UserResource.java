package com.unicap.tcc.usability.api.resources;

import com.unicap.tcc.usability.api.exception.SenhaInvalidaException;
import com.unicap.tcc.usability.api.models.User;
import com.unicap.tcc.usability.api.models.dto.CredentialDTO;
import com.unicap.tcc.usability.api.models.dto.TokenDTO;
import com.unicap.tcc.usability.api.models.dto.UserDTO;
import com.unicap.tcc.usability.api.repository.UserRepository;
import com.unicap.tcc.usability.api.security.jwt.JwtService;
import com.unicap.tcc.usability.api.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
@Api("User API")
@RequiredArgsConstructor
public class UserResource {

    private final UserService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Criação de novo usuário")
    @ApiResponse(code = 201, message = "Cadastro efetuado com sucesso.")
    public ResponseEntity<UserDTO> salvar(@RequestBody @Valid User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        User newUser = usuarioService.save(user);
        return ResponseEntity.ok()
                .body(UserDTO.builder()
                        .login(newUser.getLogin())
                        .email(newUser.getEmail())
                        .admin(newUser.getAdmin())
                        .build());
    }

    @PostMapping("/auth")
    @ApiOperation("Login de um usuário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Login efetuado com sucesso."),
            @ApiResponse(code = 401, message = "Senha incorreta ou usuário não cadastrado")
    })
    public ResponseEntity<TokenDTO> autenticar(@RequestBody CredentialDTO credenciais) {
        try {
            User user = User.builder()
                    .login(credenciais.getLogin())
                    .password(credenciais.getPassword())
                    .build();
            User registeredUser = userRepository.findByLogin(user.getLogin())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados."));
            user.setAdmin(!Objects.isNull(registeredUser.getAdmin()) && registeredUser.getAdmin());
            UserDetails authenticatedUser = usuarioService.auth(user);
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok()
                    .body(new TokenDTO(user.getLogin(), token, authenticatedUser.getAuthorities()));
        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/list-all")
    @ApiOperation("Lista todos os usuários")
    public ResponseEntity<List<UserDTO>> listarUsuarios() {
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

    @PutMapping("/delete/{id}")
    @ApiOperation("Deletar (logicamente) usuário")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "id") @ApiParam("ID do Usuário") Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok()
                .body("Usuário deletado com seucesso");
    }

//    @PutMapping("/delete-all")
//    @ApiOperation("Deletar todos os usuários")
//    public void deletarTodosUsuario() {
//        usuarioRepository.deleteAll();
//    }
}
