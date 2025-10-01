package eventify.security;

import eventify.model.User;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

@Getter
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
    private final Long id;

    public CustomUserDetails(User user) {
        super(user.getUsername(), user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
        this.id = user.getId();
    }

}
