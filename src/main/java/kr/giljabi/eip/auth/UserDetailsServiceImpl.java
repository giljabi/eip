package kr.giljabi.eip.auth;

import kr.giljabi.eip.model.User;
import kr.giljabi.eip.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
        private final UserService userService;

        @Override
        public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
                User user = userService.selectOneByUserId(userId);
                if(user == null) {
                        throw new UsernameNotFoundException("empty user");
                }
                return UserPrincipal.build(user);
        }
}
