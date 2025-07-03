package team.mjk.agent.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.mjk.agent.global.resolver.MemberIdArgumentResolver;

import java.util.List;

@Configuration
public class MemberIdArgumentResolverConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberIdArgumentResolver());
    }

}
