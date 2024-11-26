package kr.giljabi.eip.service;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import kr.giljabi.eip.model.User;
import kr.giljabi.eip.util.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class JwtProviderService {
    private final JwtProvider jwtProvider;

    @Autowired
    public JwtProviderService(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    /**
     * session정보가 있거나 없거나 반드시 리턴해야 함
     * @param request
     * @return
     */
    public String getSessionByUserinfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Jws<Claims> claims = jwtProvider.getClaims((String) session.getAttribute("userToken"));
        return (String) claims.getBody().get("sub");
    }
}
