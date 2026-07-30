package dev.thomas.polinizamap;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thomas.polinizamap.config.TokenProvider;
import dev.thomas.polinizamap.entity.Usuario;
import dev.thomas.polinizamap.enums.Role;
import dev.thomas.polinizamap.repository.AvistamentoRepository;
import dev.thomas.polinizamap.repository.EspecieRepository;
import dev.thomas.polinizamap.repository.RegiaoRepository;
import dev.thomas.polinizamap.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TokenProvider tokenProvider;

    @Autowired
    protected EspecieRepository especieRepository;

    @Autowired
    protected UsuarioRepository usuarioRepository;

    @Autowired
    protected AvistamentoRepository avistamentoRepository;

    @Autowired
    protected RegiaoRepository regiaoRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected String tokenAdmin;
    protected String tokenCidadao;
    protected String tokenPesquisador;

    @BeforeEach
    void setupUsuarios() {
        avistamentoRepository.deleteAllInBatch();
        especieRepository.deleteAllInBatch();
        regiaoRepository.deleteAllInBatch();
        usuarioRepository.deleteAllInBatch();

        tokenAdmin = criarTokenParaUsuario("admin@test.com", Role.ADMIN);
        tokenCidadao = criarTokenParaUsuario("cidadao@test.com", Role.CIDADAO);
        tokenPesquisador = criarTokenParaUsuario("pesquisador@test.com", Role.PESQUISADOR);
    }

    private String criarTokenParaUsuario(String email, Role role) {
        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .nome(role.name())
                .email(email)
                .senha(passwordEncoder.encode("123456"))
                .role(role)
                .build());

        var auth = new UsernamePasswordAuthenticationToken(
                email,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
        );

        return tokenProvider.getToken(auth, role);
    }
}