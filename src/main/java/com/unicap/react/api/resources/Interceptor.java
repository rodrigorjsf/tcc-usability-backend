importar org.springframework.stereotype.Component; 

import javax.servlet.Filter; 
import javax.servlet.FilterChain; 
importar javax.servlet.ServletException; 
importar javax.servlet.ServletRequest; 
importar javax.servlet.ServletResponse; 
importar javax.servlet.http.HttpServletResponse; 
importar java.io.IOException; 

 @Component 
classe pública Interceptor implementa Filter { 

    public void doFilter (requisição ServletRequest, requisição ServletRequest, res ServletResponse, cadeia FilterChain) lança IOException, ServletException {
        Resposta HttpServletResponse = (HttpServletResponse) res; 
        response.setHeader ("Controle de acesso-permitir-origem", "*"); 
        response.setHeader ("Métodos de controle de acesso e permissão", "POST, PUT, GET, OPTIONS, DELETE"); 
        response.setHeader ("Access-Control-Max-Age", "3600"); 
        response.setHeader ("Acesso-controle-permitir-cabeçalhos", "tipo de conteúdo, autorização, comprimento do conteúdo, requisitado por X com"); 
        chain.doFilter (req, res); 
    } 
}