package com.dev.library.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dev.library.core.ResponseObject;
import com.dev.library.model.dto.resquestDTO.RegisterDTO;
import com.dev.library.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    private static final String uploadPath = "uploads/";

    @PostMapping("/register")
    public ResponseEntity<ResponseObject<?>> register(@RequestPart("user") RegisterDTO user,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest request) {
        ResponseObject<?> response = new ResponseObject<>();
        response.setStatus(true);
        response.setMessage("Register successfully.");
        userService.insertUser(user, file, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/upload/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadPath).resolve(filename).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(Files.probeContentType(filePath).contains("image") ? MediaType.IMAGE_JPEG
                                : MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<?>> deleteUser(@PathVariable Integer id) {
        ResponseObject<?> response = new ResponseObject<>();
        response.setStatus(true);
        response.setMessage("Delete user successfully.");
        return ResponseEntity.ok(response);
    }

}
