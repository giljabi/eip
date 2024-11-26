package kr.giljabi.eip.auth;

import kr.giljabi.eip.model.User;
import kr.giljabi.eip.service.UserService;
import kr.giljabi.eip.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
        private RedirectStrategy redirectStratgy = new DefaultRedirectStrategy();

        @Autowired
        private final UserService userService;

        @Autowired
        private final JwtProvider jwtProvider;
        private HttpSession session;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException {
                request.getSession().invalidate();
                session = request.getSession(true);
                UserPrincipal user =  (UserPrincipal) authentication.getPrincipal();

                User userEntity = userService.selectOneByUserId(user.getUsername());
                String token = jwtProvider.generateJwtToken(userEntity);

                session.setAttribute("userToken", token);

                this.redirectStratgy.sendRedirect(request, response, "/register/quiz-list");
        }

}


