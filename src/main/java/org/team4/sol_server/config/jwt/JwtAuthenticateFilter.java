package org.team4.sol_server.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.team4.sol_server.config.BaseResponse;
import org.team4.sol_server.config.BaseResponseStatus;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticateFilter extends GenericFilterBean {


    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            String token = jwtTokenProvider.resolveToken(httpRequest);

            if (token == null) {
                //JWT가 없으면 첫 로그인 → 성공 응답 반환
                sendSuccessResponse(httpResponse);
                return;
            }

            if (!jwtTokenProvider.validateToken(token)) {
                //JWT가 유효하지 않으면 JSON 응답 반환
                sendErrorResponse(httpResponse, BaseResponseStatus.INVALID_JWT);
                return;
            }

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }

    //JWT가 없을 때 1000 SUCCESS 응답을 반환하는 메서드
    private void sendSuccessResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK); // HTTP 200

        BaseResponse<?> successResponse = new BaseResponse<>(BaseResponseStatus.SUCCESS);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(successResponse);

        response.getWriter().write(jsonResponse);
    }

    //JWT가 유효하지 않을 때 에러 응답 반환
    private void sendErrorResponse(HttpServletResponse response, BaseResponseStatus status) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        BaseResponse<?> errorResponse = new BaseResponse<>(status);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
    }
}
