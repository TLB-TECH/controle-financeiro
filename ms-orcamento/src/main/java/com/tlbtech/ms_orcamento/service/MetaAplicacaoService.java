package com.tlbtech.ms_orcamento.service;

import com.tlbtech.ms_orcamento.dto.MetaAnualRequestDTO;
import com.tlbtech.ms_orcamento.dto.MetaAnualResponseDTO;
import com.tlbtech.ms_orcamento.dto.MetaMensalItemDTO;
import com.tlbtech.ms_orcamento.dto.MetaMensalResponseDTO;
import com.tlbtech.ms_orcamento.model.MetaAplicacaoAnual;
import com.tlbtech.ms_orcamento.model.MetaAplicacaoMensal;
import com.tlbtech.ms_orcamento.repository.MetaAplicacaoAnualRepository;
import com.tlbtech.ms_orcamento.repository.MetaAplicacaoMensalRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MetaAplicacaoService {

    private final MetaAplicacaoMensalRepository mensalRepository;
    private final MetaAplicacaoAnualRepository anualRepository;

    public MetaAplicacaoService(MetaAplicacaoMensalRepository mensalRepository,
                                 MetaAplicacaoAnualRepository anualRepository) {
        this.mensalRepository = mensalRepository;
        this.anualRepository = anualRepository;
    }

    private String getEmailAutenticado() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /** Sempre devolve os 12 meses do ano, preenchendo com zero os que ainda não foram definidos. */
    public List<MetaMensalResponseDTO> listarMensal(Integer ano) {
        String email = getEmailAutenticado();
        Map<Integer, MetaAplicacaoMensal> porMes = mensalRepository.findByUsuarioIdAndAno(email, ano).stream()
                .collect(Collectors.toMap(MetaAplicacaoMensal::getMes, m -> m));

        return IntStream.rangeClosed(1, 12)
                .mapToObj(mes -> porMes.containsKey(mes)
                        ? MetaMensalResponseDTO.fromEntity(porMes.get(mes))
                        : MetaMensalResponseDTO.vazio(mes, ano))
                .toList();
    }

    public List<MetaMensalResponseDTO> salvarMensal(Integer ano, List<MetaMensalItemDTO> itens) {
        String email = getEmailAutenticado();
        for (MetaMensalItemDTO item : itens) {
            MetaAplicacaoMensal m = mensalRepository.findByUsuarioIdAndMesAndAno(email, item.mes(), ano)
                    .orElseGet(MetaAplicacaoMensal::new);
            m.setUsuarioId(email);
            m.setMes(item.mes());
            m.setAno(ano);
            m.setValor(item.valor());
            mensalRepository.save(m);
        }
        return listarMensal(ano);
    }

    public MetaAnualResponseDTO obterAnual(Integer ano) {
        return anualRepository.findByUsuarioIdAndAno(getEmailAutenticado(), ano)
                .map(MetaAnualResponseDTO::fromEntity)
                .orElse(MetaAnualResponseDTO.vazio(ano));
    }

    public MetaAnualResponseDTO salvarAnual(Integer ano, MetaAnualRequestDTO dto) {
        String email = getEmailAutenticado();
        MetaAplicacaoAnual m = anualRepository.findByUsuarioIdAndAno(email, ano).orElseGet(MetaAplicacaoAnual::new);
        m.setUsuarioId(email);
        m.setAno(ano);
        m.setValor(dto.valor());
        return MetaAnualResponseDTO.fromEntity(anualRepository.save(m));
    }

    /** Apaga de vez todas as metas de aplicação do usuário (exclusão de conta em cascata). */
    @Transactional
    public void excluirDadosUsuario(String usuarioId) {
        mensalRepository.deleteByUsuarioId(usuarioId);
        anualRepository.deleteByUsuarioId(usuarioId);
    }
}
