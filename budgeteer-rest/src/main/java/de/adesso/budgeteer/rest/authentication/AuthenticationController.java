package de.adesso.budgeteer.rest.authentication;

import de.adesso.budgeteer.rest.authentication.model.AuthInfoModel;
import de.adesso.budgeteer.rest.authentication.model.AuthenticationModel;
import de.adesso.budgeteer.rest.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public AuthInfoModel authenticate(@RequestBody @Valid AuthenticationModel authenticationModel) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationModel.getUsername(), authenticationModel.getPassword()));
        return new AuthInfoModel(jwtUtil.createToken(authentication.getName()));
    }
}
