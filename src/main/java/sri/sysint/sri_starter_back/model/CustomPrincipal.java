package sri.sysint.sri_starter_back.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomPrincipal implements UserDetails {
    private Long userId;
    private String username;
    private String role;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomPrincipal(Long userId, String username, String role,
                           Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.authorities = authorities;
    }

    public Long getUserId() { return userId; }
    public String getRole() { return role; }

    @Override public String getUsername() { return username; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
    @Override public String getPassword() { return null; }
}