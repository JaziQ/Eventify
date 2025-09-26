package eventify.service;

import eventify.dto.UserDTO;
import eventify.mapper.Mapper;
import eventify.model.User;
import eventify.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO save(UserDTO userDTO) {
        User user = Mapper.toUserEntity(userDTO);
        return Mapper.toUserDTO(userRepository.save(user));
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id = " + id));
        return Mapper.toUserDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with Email = " + email));
        return Mapper.toUserDTO(user);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username = " + username));
        return Mapper.toUserDTO(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO updatedUserDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id = " + id));

        existingUser.setFirstName(updatedUserDTO.getFirstName());
        existingUser.setLastName(updatedUserDTO.getLastName());
        existingUser.setEmail(updatedUserDTO.getEmail());
        existingUser.setBirthDate(updatedUserDTO.getBirthDate());
        existingUser.setUpdatedAt(LocalDateTime.now());

        return Mapper.toUserDTO(userRepository.save(existingUser));
    }

    @Transactional
    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id = " + id));
        userRepository.delete(existingUser);
    }
}
