package org.springframework.web.filter;

import com.monolithical.customerservice.filters.JWTFilter;
import com.monolithical.customerservice.util.validation.JWTValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class JWTFilterTest {

  @Test
  void doFilterInternal_valid_jwt_returns_ok() throws ServletException, IOException {
    var request = new MockHttpServletRequest();
    var response = new MockHttpServletResponse();
    var chain = new MockFilterChain();
    var validator = Mockito.mock(JWTValidator.class);
    String secret = "imasecretimasecretimasecretimasecret";

    String validJWT =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.HqXboI3jgtnSoM4cT_WaWDTgQ83wMaJVL2X0pdUEvaU";
    request.addHeader("Authorization", "Bearer " + validJWT);
    Mockito.when(validator.isValid(validJWT)).thenReturn(true);
    OncePerRequestFilter filter = new JWTFilter(validator);

    filter.doFilterInternal(request, response, chain);
    assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
  }

  @Test
  void doFilterInternal_header_missing_returns_401() throws ServletException, IOException {
    var request = new MockHttpServletRequest();
    var response = new MockHttpServletResponse();
    var chain = new MockFilterChain();
    var validator = Mockito.mock(JWTValidator.class);

    OncePerRequestFilter filter = new JWTFilter(validator);

    filter.doFilterInternal(request, response, chain);
    assertThat(response.getStatus(), equalTo(HttpStatus.UNAUTHORIZED.value()));
  }

  @Test
  void doFilterInternal_proper_formatted_jwt_but_incorrect_jwt_returns_401()
      throws ServletException, IOException {
    var request = new MockHttpServletRequest();
    var response = new MockHttpServletResponse();
    var chain = new MockFilterChain();
    String secret = "imasecretimasecretimasecretimasecret";
    var validator = new JWTValidator(secret);

    String validJWT =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.dyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.HqXboI3jgtnSoM4cT_WaWDTgQ83wMaJVL2X0pdUEvaU";
    request.addHeader("Authorization", "Bearer " + validJWT);
    OncePerRequestFilter filter = new JWTFilter(validator);

    filter.doFilterInternal(request, response, chain);
    assertThat(response.getStatus(), equalTo(HttpStatus.UNAUTHORIZED.value()));
  }
}
