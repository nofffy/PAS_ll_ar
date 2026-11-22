package pasweb.rest;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pasweb.controllers.AppException;
import pasweb.DTOs.ClientDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientUserDetailsService implements UserDetailsService {
    private final ClientApi clientApi;

    public ClientUserDetailsService(ClientApi clientApi) {
        this.clientApi = clientApi;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ClientDTO user;
        try {
            user = clientApi.getClientByLogin(username);
        } catch (AppException e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        if (user == null || !user.isActive()) {
            throw new UsernameNotFoundException("User not found or inactive: " + username);
        }

        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        String role = (user.getType() != null) ? user.getType().toUpperCase() : "CLIENT";
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + role));

        return new User(user.getLogin(), user.getPassword(), grantedAuths);
    }
}