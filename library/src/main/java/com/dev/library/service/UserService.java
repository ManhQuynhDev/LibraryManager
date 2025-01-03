package com.dev.library.service;

import java.time.LocalDateTime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dev.library.core.exception.BadRequestException;
import com.dev.library.core.exception.UnknownException;
import com.dev.library.core.exception.UserAccountExitsException;
import com.dev.library.model.entity.User;
import com.dev.library.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/";

    public void insertUser(User userDTO, MultipartFile file, HttpServletRequest request)
            throws UserAccountExitsException, UnknownException, BadRequestException {

        if (userRepository.findByEmail(userDTO.getEmail()).size() > 0) {
            throw new UserAccountExitsException("Email already exists , please try again with another email");
        }

        User user = new User();
        user.setCreate_time(LocalDateTime.now());
        user.setDelflag(0);
        user.setRole("USER");
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if (file != null) {
            try {
                String fileName = file.getOriginalFilename();

                if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
                    throw new BadRequestException("Only PNG, JPG, and JPEG files are allowed");
                }

                File directory = new File(UPLOAD_DIR);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                Path path = Paths.get(UPLOAD_DIR + fileName);

                Files.write(path, file.getBytes());

                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

                String fileUrl = baseUrl + "/api/upload/image/" + fileName;

                user.setAvatar(fileUrl);
            } catch (IOException e) {
                throw new UnknownException("Error when saving file");
            }
        }

        User userSave = userRepository.save(user);
        if (userSave == null) {
            throw new UnknownException("Error when saving user");
        }
    }

    
}
