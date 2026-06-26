package com.zenin.service;

import com.zenin.dto.request.PerfilUpdateRequest;
import com.zenin.model.User;
import com.zenin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final UserRepository userRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public User buscar(User usuario) {
        return usuario;
    }

    @Transactional
    public User atualizar(PerfilUpdateRequest request, User usuario) {
        if (request.nome() != null && !request.nome().isBlank()) {
            usuario.setName(request.nome());
        }
        if (request.preferenciaTema() != null) {
            usuario.setPreferenciaTema(request.preferenciaTema());
        }
        if (request.diaPagamento() != null) {
            usuario.setDiaPagamento(request.diaPagamento());
        }
        if (request.numeroWhatsapp() != null) {
            usuario.setNumeroWhatsapp(request.numeroWhatsapp());
        }
        return userRepository.save(usuario);
    }

    @Transactional
    public String uploadAvatar(MultipartFile arquivo, User usuario) throws IOException {
        if (arquivo.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não pode ser vazio");
        }

        String tipo = arquivo.getContentType();
        if (tipo == null || !tipo.startsWith("image/")) {
            throw new IllegalArgumentException("Arquivo deve ser uma imagem (jpg, png, webp)");
        }

        String ext = StringUtils.getFilenameExtension(arquivo.getOriginalFilename());
        String nomeArquivo = usuario.getId().toString() + "." + (ext != null ? ext : "jpg");

        Path diretorio = Paths.get(uploadDir, "avatars");
        Files.createDirectories(diretorio);
        Files.copy(arquivo.getInputStream(), diretorio.resolve(nomeArquivo), StandardCopyOption.REPLACE_EXISTING);

        String url = baseUrl + "/uploads/avatars/" + nomeArquivo;
        usuario.setUrlAvatar(url);
        userRepository.save(usuario);
        return url;
    }
}
