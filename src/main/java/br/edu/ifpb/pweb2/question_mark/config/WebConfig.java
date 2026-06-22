package br.edu.ifpb.pweb2.question_mark.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.edu.ifpb.pweb2.question_mark.interceptor.AuthInteceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInteceptor authInteceptor;

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry){
        interceptorRegistry.addInterceptor(authInteceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/login", "/error", "/css/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redireciona qualquer requisição na raiz ("/") para "/login"
        registry.addViewController("/").setViewName("redirect:/login");
    }

}
