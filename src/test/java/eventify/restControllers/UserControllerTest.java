package eventify.restControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import eventify.dto.UserDTO;
import eventify.mapper.Mapper;
import eventify.model.User;
import eventify.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setUsername("john");
        user.setPassword("password");
        user.setBirthDate(LocalDate.of(1990,1,1));
        user.setRole(User.Role.USER);

        userDTO = Mapper.toUserDTO(user);
    }

    @Test
    @WithMockUser
    void getUserById() throws Exception {
        Mockito.when(userService.getUserById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.firstName").value(userDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDTO.getLastName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));
    }

    @Test
    @WithMockUser
    void getUserByEmail() throws Exception {
        Mockito.when(userService.getUserByEmail("john@example.com")).thenReturn(userDTO);;

        mockMvc.perform(get("/api/users/email").param("email", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));
    }

    @Test
    @WithMockUser
    void getUserByUsername() throws Exception {
        Mockito.when(userService.getUserByUsername("john")).thenReturn(userDTO);;

        mockMvc.perform(get("/api/users/username").param("username", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser() throws Exception {
        Mockito.when(userService.save(any(UserDTO.class))).thenReturn(userDTO);;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser() throws Exception {
        Mockito.when(userService.updateUser(anyLong(), any(UserDTO.class))).thenReturn(userDTO);;

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.firstName").value(userDTO.getFirstName()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}
