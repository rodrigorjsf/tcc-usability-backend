package com.unicap.react.api.resources;

import com.unicap.react.api.exception.SenhaInvalidaException;
import com.unicap.react.api.models.Usuario;
import com.unicap.react.api.models.dto.CredenciaisDTO;
import com.unicap.react.api.models.dto.TokenDTO;
import com.unicap.react.api.models.dto.UsuarioDTO;
import com.unicap.react.api.repository.UsuarioRepository;
import com.unicap.react.api.security.jwt.JwtService;
import com.unicap.react.api.service.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserResource {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDTO salvar(@RequestBody @Valid Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        Usuario user = usuarioService.salvar(usuario);
        return UsuarioDTO.builder()
                .login(user.getLogin())
                .email(user.getEmail())
                .build();
    }

    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais) {
        try {
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha())
                    .build();
            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
            if (!usuarioAutenticado.getUsername().isEmpty()){
                Usuario user = usuarioRepository.findByLogin(usuarioAutenticado.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados."));
                usuario.setAdmin(user.getAdmin());
            }
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getLogin(), token, usuarioAutenticado.getAuthorities());
        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/list-all")
    public List<UsuarioDTO> listarUsuarios() {
        List<Usuario> usuarioList = usuarioRepository.findAll();
        if (usuarioList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        List<UsuarioDTO> usuarioDTOS = new ArrayList<>();
        usuarioList.stream().forEach(usuario -> {
            UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                    .login(usuario.getLogin())
                    .email(usuario.getEmail())
                    .admin(usuario.getAdmin())
                    .build();
            usuarioDTOS.add(usuarioDTO);
        });
        return usuarioDTOS;
    }

    @PutMapping("/delete/{id}")
    public void deletarUsuario(@PathVariable(value = "id") Long id) {
        usuarioRepository.deleteById(id);
    }

    @PutMapping("/delete-all")
    public void deletarTodosUsuario() {
        usuarioRepository.deleteAll();
    }
}
