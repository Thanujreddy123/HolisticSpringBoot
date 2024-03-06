package com.example.TaskManagement;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;


@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;


    @GetMapping
    public ResponseEntity<List<File>> getFiles() {
        List<File> files = fileService.getFiles();
        return ResponseEntity.ok(files);
    }

    @PostMapping("/upload")
    public ResponseEntity<File> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadedFile = fileService.uploadFile(file);
        return ResponseEntity.ok(uploadedFile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllFiles() {
        fileService.deleteAllFiles();
        return ResponseEntity.noContent().build();
    }





}