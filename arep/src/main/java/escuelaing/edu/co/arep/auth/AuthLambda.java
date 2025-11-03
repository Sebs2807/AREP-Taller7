package escuelaing.edu.co.arep.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import escuelaing.edu.co.arep.model.Usuario;
import escuelaing.edu.co.arep.repository.UsuarioRepository;
import escuelaing.edu.co.arep.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;

public class AuthLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final UsuarioRepository usuarioRepo = null;
    private final JwtUtil jwtUtil = new JwtUtil("CLAVE_AREP_JWT");
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        try {
            Map<String, String> body = mapper.readValue(event.getBody(), Map.class);

            if (event.getPath().endsWith("/register")) {
                String username = body.get("username");
                String password = body.get("password");
                if (username == null || password == null) return error(400, "username y password requeridos");

                if (usuarioRepo.findByUsername(username) != null)
                    return error(400, "username ya existe");

                Usuario u = new Usuario();
                u.setUsername(username);
                u.setPassword(encoder.encode(password));
                Usuario saved = usuarioRepo.save(u);
                return ok(Map.of("id", saved.getId(), "username", saved.getUsername()));
            }

            if (event.getPath().endsWith("/login")) {
                String username = body.get("username");
                String password = body.get("password");

                Usuario u = usuarioRepo.findByUsername(username);
                if (u == null || !encoder.matches(password, u.getPassword()))
                    return error(401, "credenciales inválidas");

                String token = jwtUtil.generateToken(u.getId(), 1000L*60*60*24);
                return ok(Map.of("token", token, "id", u.getId(), "username", u.getUsername()));
            }

        } catch (Exception e) {
            return error(500, e.getMessage());
        }
        return error(404, "Ruta no encontrada");
    }

    private APIGatewayProxyResponseEvent ok(Object obj) throws Exception {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(mapper.writeValueAsString(obj))
                .withHeaders(Map.of("Content-Type", "application/json"));
    }

    private APIGatewayProxyResponseEvent error(int code, String msg) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(code)
                .withBody("{\"error\":\""+msg+"\"}")
                .withHeaders(Map.of("Content-Type", "application/json"));
    }
}
