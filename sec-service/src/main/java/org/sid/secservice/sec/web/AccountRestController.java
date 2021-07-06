package org.sid.secservice.sec.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sid.secservice.sec.entities.AppRole;
import org.sid.secservice.sec.entities.AppUser;
import org.sid.secservice.sec.service.AccountService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AccountRestController {
    private AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }
@GetMapping(path = "/users")
@PostAuthorize("hasAuthority('USER')")
    public List<AppUser> appUsers(){
     return accountService.listUser();
    }
    @PostAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path="/users")
    public AppUser saveuser(@RequestBody AppUser appUser){
        return accountService.addNewUser(appUser);
    }
    @PostMapping(path = "/roles")
    public AppRole saveroll(@RequestBody AppRole appRole){
        return accountService.addnewRole(appRole);
    }
    @PostMapping(path = "/addRoleToUser")
public void addRoleToUser(@RequestBody RoleUserForm roleUserForm){
         accountService.addRolToUser(roleUserForm.getUsername(),roleUserForm.getRolename());


    }
    @GetMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws  Exception{

String authToken= request.getHeader("Authorization");
if(authToken != null && authToken.startsWith("Bearer "))
{

    try {
        String jwt = authToken.substring(7);
        Algorithm algorithm = Algorithm.HMAC256("mysecret1234");
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
        String username = decodedJWT.getSubject();
        AppUser appUser = accountService.loadUserByusername(username);
        String jwtAccesstoken = JWT.create()
                .withSubject(appUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", appUser.getAppRoles().stream().map(gr -> gr.getRolename()).collect(Collectors.toList()))
                .sign(algorithm);
        Map<String, String> idtoken = new HashMap<>();
        idtoken.put("access-token", jwtAccesstoken);
        idtoken.put("refresh-token", jwt);
        response.setHeader("Auhthorization", jwtAccesstoken);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), idtoken);

    } catch (Exception e) {
        response.setHeader("errormsg", e.getMessage());
//response.sendError(HttpServletResponse.SC_FORBIDDEN,e.getMessage());
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
else{
    throw new RuntimeException();
    }
}
    }

