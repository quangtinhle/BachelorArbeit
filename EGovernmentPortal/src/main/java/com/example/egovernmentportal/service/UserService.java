package com.example.egovernmentportal.service;

import com.example.egovernmentportal.exception.StorageException;
import com.example.egovernmentportal.model.AgeofUser;
import com.example.egovernmentportal.model.User;
import com.example.egovernmentportal.repository.UserRepository;
import lombok.val;
import org.keycloak.representations.IDToken;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.Period;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final String IMAGE_PATH = "src/main/resources/static/images/";

    public User getUserDetails(IDToken idToken) {
        String userId = idToken.getSubject();
        if (userRepository.existsByIdentityId(userId)) {
            return getUserbyIdentityId(userId);
        } else {
            User user = new User();
            user.setFirstName(idToken.getGivenName());
            user.setLastName(idToken.getFamilyName());
            user.setUserName(idToken.getPreferredUsername());
            user.setEmail(idToken.getEmail());
            user.setIdentityId(idToken.getSubject());
            userRepository.save(user);
            return user;
        }

    }

    public void saveUser(User user, MultipartFile file) {
        if(!file.isEmpty()) {
            user.setImage(store(file));
        }
        userRepository.save(user);
    }
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }
        Path destinationFile = Paths.get(IMAGE_PATH + file.getOriginalFilename());

/*			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new StorageException(
						"Cannot store file outside current directory.");
			}*/
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
        return "images/"+file.getOriginalFilename();


    }

    public User getUserbyIdentityId(String id) {
        return userRepository.findUserByIdentityId(id);
    }


    public AgeofUser validateAgeofUser(String id) {

        User user = userRepository.findUserByIdentityId(id);
        AgeofUser ageofUser = new AgeofUser();
        ageofUser.setFirstName(user.getFirstName());
        ageofUser.setLastName(user.getLastName());
        ageofUser.setImage(user.getImage());

        LocalDate now = LocalDate.now();
        LocalDate birthday = user.getBirthday();
        Period p = Period.between(birthday,now);
        boolean ageover18 = false;
        if(p.getYears()>=18) {
            if(p.getMonths()>=0) {
                if(p.getDays()>=0) {
                    ageover18 = true;
                }
            }
        }
        ageofUser.setAgeover18(ageover18);
        return ageofUser;
    }
}
