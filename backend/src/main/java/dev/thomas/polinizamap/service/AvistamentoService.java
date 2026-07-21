package dev.thomas.polinizamap.service;

import dev.thomas.polinizamap.dto.request.AvistamentoRequest;
import dev.thomas.polinizamap.dto.request.ValidacaoRequest;
import dev.thomas.polinizamap.dto.response.AvistamentoResponse;
import dev.thomas.polinizamap.entity.Avistamento;
import dev.thomas.polinizamap.entity.Especie;
import dev.thomas.polinizamap.entity.Regiao;
import dev.thomas.polinizamap.entity.Usuario;
import dev.thomas.polinizamap.enums.Role;
import dev.thomas.polinizamap.enums.StatusAvistamento;
import dev.thomas.polinizamap.exception.BusinessException;
import dev.thomas.polinizamap.exception.ForbiddenException;
import dev.thomas.polinizamap.exception.NotFoundException;
import dev.thomas.polinizamap.repository.AvistamentoRepository;
import dev.thomas.polinizamap.repository.AvistamentoSpec;
import dev.thomas.polinizamap.repository.EspecieRepository;
import dev.thomas.polinizamap.repository.RegiaoRepository;
import dev.thomas.polinizamap.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvistamentoService {

    private final AvistamentoRepository avistamentoRepository;
    private final EspecieRepository especieRepository;
    private final RegiaoRepository regiaoRepository;
    private final UsuarioRepository usuarioRepository;

    public AvistamentoResponse registrar(AvistamentoRequest request) {
        Usuario usuario = getUsuarioAutenticado();

        Especie especie = especieRepository.findById(request.especieId())
                .orElseThrow(() -> new NotFoundException("Espécie não encontrada"));

        Regiao regiao = regiaoRepository.findById(request.regiaoId())
                .orElseThrow(() -> new NotFoundException("Região não encontrada"));

        Avistamento avistamento = Avistamento.builder()
                .usuario(usuario)
                .especie(especie)
                .regiao(regiao)
                .latitude(request.latitude())
                .longitude(request.longitude())
                .descricao(request.descricao())
                .dataHora(request.dataHora())
                .status(StatusAvistamento.PENDENTE)
                .build();

        return AvistamentoResponse.from(avistamentoRepository.save(avistamento));
    }

    public List<AvistamentoResponse> listar(Long especieId, Long regiaoId,
                                            StatusAvistamento status,
                                            LocalDateTime inicio, LocalDateTime fim) {
        Usuario usuario = getUsuarioAutenticado();

        Specification<Avistamento> spec = Specification.allOf(
                AvistamentoSpec.porEspecie(especieId),
                AvistamentoSpec.porRegiao(regiaoId),
                AvistamentoSpec.porStatus(status),
                AvistamentoSpec.porPeriodo(inicio, fim)
        );

        if (usuario.getRole() == Role.CIDADAO) {
            spec = spec.and(AvistamentoSpec.porUsuario(usuario.getId()));
        }

        return avistamentoRepository.findAll(spec)
                .stream()
                .map(AvistamentoResponse::from)
                .toList();
    }

    public AvistamentoResponse buscarPorId(Long id) {
        Usuario usuario = getUsuarioAutenticado();
        Avistamento avistamento = avistamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Avistamento não encontrado"));

        if (usuario.getRole() == Role.CIDADAO
                && !avistamento.getUsuario().getId().equals(usuario.getId())) {
            throw new ForbiddenException("Acesso negado");
        }

        return AvistamentoResponse.from(avistamento);
    }

    public AvistamentoResponse validar(Long id, ValidacaoRequest request) {
        Avistamento avistamento = avistamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Avistamento não encontrado"));

        if (avistamento.getStatus() != StatusAvistamento.PENDENTE) {
            throw new BusinessException("Apenas avistamentos pendentes podem ser validados");
        }

        avistamento.setStatus(request.status());
        avistamento.setNotaValidacao(request.notaValidacao());

        return AvistamentoResponse.from(avistamentoRepository.save(avistamento));
    }

    private Usuario getUsuarioAutenticado() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }
}