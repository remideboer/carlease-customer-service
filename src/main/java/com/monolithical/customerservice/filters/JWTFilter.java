package com.monolithical.customerservice.filters;

import com.monolithical.customerservice.util.validation.JWTValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

  private final String errorUnauthorizedFormat = "Unauthorized: %s";
  private final JWTValidator validator;

  public JWTFilter(JWTValidator validator) {
    this.validator = validator;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain)
      throws ServletException, IOException {
    String header = httpServletRequest.getHeader("Authorization");
    if (header != null) {
      String[] content = httpServletRequest.getHeader("Authorization").split(" ");

      if (content[0].equals("Bearer") && validator.isValid(content[1])) {
        filterChain.doFilter(httpServletRequest, httpServletResponse);
      } else {
        httpServletResponse.sendError(
            HttpServletResponse.SC_UNAUTHORIZED,
            String.format(errorUnauthorizedFormat, "The token is not valid."));
      }
    } else {
      httpServletResponse.sendError(
          HttpServletResponse.SC_UNAUTHORIZED,
          String.format(errorUnauthorizedFormat, "Authorization header is missing."));
    }
  }
}
