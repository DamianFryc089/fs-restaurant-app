package fun.kociarnia.bazy_danych_projekt;

import fun.kociarnia.bazy_danych_projekt.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

@Data
public class MyUserDetails implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final User.Role role;
    private final Collection<? extends GrantedAuthority> authorities;

    public MyUserDetails(Long id, String username, String password, User.Role role,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.authorities = authorities;
    }
}

