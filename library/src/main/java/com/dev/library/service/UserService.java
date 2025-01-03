package com.dev.library.service;

import java.time.LocalDateTime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dev.library.core.exception.BadRequestException;
import com.dev.library.core.exception.UnknownException;
import com.dev.library.core.exception.UserAccountExitsException;
import com.dev.library.core.exception.UserAccountNotFoundException;
import com.dev.library.model.dto.resquestDTO.RegisterDTO;
import com.dev.library.model.dto.resquestDTO.UpdateDTO;
import com.dev.library.model.entity.User;
import com.dev.library.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/";

    public void insertUser(RegisterDTO userDTO, MultipartFile file, HttpServletRequest request)
            throws UserAccountExitsException, UnknownException, BadRequestException {

        if (userRepository.findByEmail(userDTO.getEmail()).size() > 0) {
            throw new UserAccountExitsException("Email already exists , please try again with another email");
        }

        if (userRepository.findByPhoneNumber(userDTO.getPhoneNumber()).size() > 0) {
            throw new UserAccountExitsException(
                    "PhoneNumber already exists , please try again with another phoneNumber");
        }

        User user = new User();
        if (userDTO.getEmail() == null || userDTO.getPassword() == null || userDTO.getFullname() == null
                || userDTO.getPhoneNumber() == null) {
            throw new BadRequestException("Email and password and fullname and phoneNumber are required");
        }
        user.setEmail(userDTO.getEmail());
        user.setFullname(userDTO.getFullname());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress() == null ? null : userDTO.getAddress());
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

                String fileUrl = baseUrl + "api/user/upload/image/" + fileName;

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

    public void deleteUser(Integer id) throws UserAccountNotFoundException {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new UserAccountNotFoundException("User not found");
        }
        user.setDelflag(1);
        userRepository.save(user);
    }

    public void updateUser(Integer userId, UpdateDTO updateDTO) throws UserAccountNotFoundException {
        User foundUser = userRepository.findUserById(userId);
        if (foundUser == null) {
            throw new UserAccountNotFoundException(
                    "Found User with " + userId + " not found , please try again with another id");
        }

        foundUser.setFullname(updateDTO.getFullname() == null ? foundUser.getFullname() : updateDTO.getFullname());
        foundUser.setPhoneNumber(
                updateDTO.getPhoneNumber() == null ? foundUser.getPhoneNumber() : updateDTO.getPhoneNumber());
        foundUser.setAddress(updateDTO.getAddress() == null ? foundUser.getAddress() : updateDTO.getAddress());

        User userSave = userRepository.save(foundUser);
        if (userSave == null) {
            throw new UnknownException("Error when update user");
        }
    }

    public Page<User> searchUserByName(String fullname, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.searchUserByName(fullname, pageable);
    }

    public Page<User> searchUserByEmail(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.searchUserByEmail(email, pageable);
    }
}
