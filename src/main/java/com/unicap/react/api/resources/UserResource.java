package com.unicap.react.api.resources;

import com.unicap.react.api.exception.SenhaInvalidaException;
import com.unicap.react.api.models.Usuario;
import com.unicap.react.api.models.dto.CredenciaisDTO;
import com.unicap.react.api.models.dto.TokenDTO;
import com.unicap.react.api.models.dto.UsuarioDTO;
import com.unicap.react.api.repository.UsuarioRepository;
import com.unicap.react.api.security.jwt.JwtService;
import com.unicap.react.api.service.UsuarioServiceImpl;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
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
@Api("Api usuários")
@RequiredArgsConstructor
public class UserResource {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Criação de novo usuário")
    @ApiResponse(code = 201, message = "Cadastro efetuado com sucesso.")
    public ResponseEntity<UsuarioDTO> salvar(@RequestBody @Valid Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        Usuario user = usuarioService.salvar(usuario);
        return ResponseEntity.ok()
                .body(UsuarioDTO.builder()
                        .login(user.getLogin())
                        .email(user.getEmail())
                        .build());
    }

    @PostMapping("/auth")
    @ApiOperation("Login de um usuário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Login efetuado com sucesso."),
            @ApiResponse(code = 401, message = "Senha incorreta ou usuário não cadastrado")
    })
    public ResponseEntity<TokenDTO> autenticar(@RequestBody CredenciaisDTO credenciais) {
        try {
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha())
                    .build();
            Usuario user = usuarioRepository.findByLogin(usuario.getLogin())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados."));
            usuario.setAdmin(Objects.isNull(user.getAdmin()) ? false : user.getAdmin());
            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            return ResponseEntity.ok()
                    .body(new TokenDTO(usuario.getLogin(), token, usuarioAutenticado.getAuthorities()));
        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/list-all")
    @ApiOperation("Lista todos os usuários")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<Usuario> usuarioList = usuarioRepository.findAll();
        if (usuarioList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        try {
            List<UsuarioDTO> usuarioDTOS = new ArrayList<>();
            usuarioList.stream().forEach(usuario -> {
                if (Objects.isNull(usuario.getAdmin())) {
                    usuario.setAdmin(false);
                }
                UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                        .login(usuario.getLogin())
                        .email(usuario.getEmail())
                        .admin(usuario.getAdmin())
                        .build();
                usuarioDTOS.add(usuarioDTO);
            });
            return ResponseEntity.ok()
                    .body(usuarioDTOS);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    @ApiOperation("Deletar (logicamente) usuário")
    public ResponseEntity<String> deletarUsuario(@PathVariable(value = "id") @ApiParam("ID do Usuário") Long id) {
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok()
                .body("Usuário deletado com seucesso");
    }

//    @PutMapping("/delete-all")
//    @ApiOperation("Deletar todos os usuários")
//    public void deletarTodosUsuario() {
//        usuarioRepository.deleteAll();
//    }
}
