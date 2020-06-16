package com.unicap.react.api.service;


import com.unicap.react.api.exception.SenhaInvalidaException;
import com.unicap.react.api.exception.UserAlreadyRegisteredException;
import com.unicap.react.api.models.Usuario;
import com.unicap.react.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        Usuario user = usuarioRepository.findByEmail(usuario.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados."));
        if (user.getEmail().trim().equals(usuario.getEmail().trim())){
            throw new UserAlreadyRegisteredException();
        }
        return usuarioRepository.save(usuario);
    }

    public UserDetails autenticar(Usuario usuario) {
        UserDetails userDetails = loadUserByUsername(usuario.getLogin());
        Boolean senhaValida = passwordEncoder.matches(usuario.getSenha(), userDetails.getPassword());
        if (senhaValida) {
            return userDetails;
        }
        throw new SenhaInvalidaException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados."));

        String[] roles = usuario.isAdmin() ? new String[]{"USER", "ADMIN"} : new String[]{"USER"};

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }
}
