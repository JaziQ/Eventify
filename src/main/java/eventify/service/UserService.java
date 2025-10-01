package eventify.service;

import eventify.dto.UserDTO;
import eventify.mapper.Mapper;
import eventify.model.User;
import eventify.repository.UserRepository;
import eventify.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new CustomUserDetails(user);
    }

    public UserDTO save(UserDTO userDTO) {
        User user = Mapper.toUserEntity(userDTO);

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

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
        if (updatedUserDTO.getPassword() != null && !updatedUserDTO.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUserDTO.getPassword()));
        }
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
