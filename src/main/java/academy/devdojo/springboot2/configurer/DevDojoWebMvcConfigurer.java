package academy.devdojo.springboot2.configurer;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DevDojoWebMvcConfigurer implements WebMvcConfigurer {
  
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    PageableHandlerMethodArgumentResolver pageHandler = new PageableHandlerMethodArgumentResolver();
    int page = 0;
    int size = 5;
    pageHandler.setFallbackPageable(PageRequest.of(page, size));
    resolvers.add(pageHandler);
  }
}
