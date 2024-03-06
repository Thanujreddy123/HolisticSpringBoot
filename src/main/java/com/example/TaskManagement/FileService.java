package com.example.TaskManagement;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Transactional(readOnly = true)
    public List<File> getFiles() {
        return fileRepository.findAll();
    }

    @Transactional
    public File uploadFile(MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        String type = file.getContentType();
        String path = saveFileToFileSystem(file);

        File newFile = new File();
        newFile.setName(name);
        newFile.setType(type);
        newFile.setPath(path);
        return fileRepository.save(newFile);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Resource> downloadFile(Long id) throws IOException {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));

        Path filePath = Paths.get(file.getPath());
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", file.getName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } else {
            throw new ResourceNotFoundException("File not found");
        }
    }
    @Transactional
    public void deleteFile(Long id) {
        Optional<File> fileOptional = fileRepository.findById(id);
        if (fileOptional.isPresent()) {
            File file = fileOptional.get();
            deleteFileFromFileSystem(file.getPath());
            fileRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("File not found");
        }
    }

    @Transactional
    public void deleteAllFiles() {
        List<File> files = fileRepository.findAll();
        for (File file : files) {
            deleteFileFromFileSystem(file.getPath());
            fileRepository.deleteById(file.getId());
        }
    }

    private String saveFileToFileSystem(MultipartFile file) throws IOException {
        String path = "uploads" + file.getOriginalFilename();
        Path targetLocation = Paths.get(path).toAbsolutePath().normalize();
        Files.createDirectories(targetLocation.getParent());
        Files.copy(file.getInputStream(), targetLocation);
        return path;
    }

    private void deleteFileFromFileSystem(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}