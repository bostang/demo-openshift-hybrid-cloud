package com.bni.bni.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller untuk menangani operasi file seperti upload dan download
 */
@RestController
@RequestMapping("/api/files")
public class FileController {
    
    // Direktori tempat file akan disimpan, diambil dari application.properties
    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Endpoint untuk mengupload file
     * @param file File yang akan diupload (dikirim sebagai multipart/form-data)
     * @return ResponseEntity yang berisi status, pesan, nama file, dan URL file
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try{
            // Membuat direktori upload jika belum ada
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            // Membersihkan nama file dan menyimpannya ke direktori upload
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Membuat URL untuk mengakses file yang diupload
            String fileUrl = "/api/files/" + fileName;

            // Mengembalikan response sukses
            return ResponseEntity.ok().body(
                Map.of(
                    "status", 200,
                    "message", "File uploaded successfully",
                    "fileName", fileName,
                    "fileUrl", fileUrl
                )
            );
        } catch(IOException e){
            // Mengembalikan response error jika terjadi exception
            return ResponseEntity.status(500).body(
                Map.of(
                    "status", 500,
                    "message", "Could not upload the file: " + e.getMessage()
                )
            );
        }        
    }

    /**
     * Endpoint untuk mendownload/melihat file
     * @param filename Nama file yang akan didownload/dilihat
     * @return ResponseEntity yang berisi file yang diminta
     */
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try{
            // Mendapatkan path file dan membuat Resource dari file tersebut
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // Memeriksa apakah file ada
            if (!resource.exists()){
                return ResponseEntity.notFound().build();
            }

            // Mendeteksi content type file
            String contentType = Files.probeContentType(filePath);

            // Jika content type tidak terdeteksi, gunakan default binary type
            if (contentType == null){
                contentType = "application/octet-stream";
            }

            // Mengembalikan file dengan header yang sesuai
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\""+ resource.getFilename()+"\"")
                .body(resource);
        } catch(Exception e){
            // Mengembalikan error 500 jika terjadi exception
            return ResponseEntity.status(500).build();
        }
    }    
}