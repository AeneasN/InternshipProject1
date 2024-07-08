package InternshipProj.api.spring_security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;

@OpenAPIDefinition(security = {@SecurityRequirement(name = "api-key")})

@SecurityScheme(
        name = "api-key",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER
)

public class ApiConfig{}