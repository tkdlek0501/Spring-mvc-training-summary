package hello.itemservice;


import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import hello.itemservice.web.argumentResolver.LoginMemberArgumentResolver;
import hello.itemservice.web.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	// 직접 만든 resolver 등록
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new LoginMemberArgumentResolver());
	}
	
	// interceptor는 핸들러 전에 실행
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginCheckInterceptor())
			.order(1)
			.addPathPatterns("/**") // 모든 URL 요청에 대해서 interceptor
			.excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error"); // 제외할 path 설정
	}
	
	
	// filter를 bean 등록
//	@Bean
//	public FilterRegistrationBean logFilter() {
//		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
//		filterRegistrationBean.setFilter(new LogFilter());
//		filterRegistrationBean.setOrder(1);
//		filterRegistrationBean.addUrlPatterns("/*");
//		
//		return filterRegistrationBean;
//	}

	// filter는 servlet 전에 호출
//	@Bean
//	public FilterRegistrationBean loginCheckFilter() {
//		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
//		filterRegistrationBean.setFilter(new LoginCheckFilter());
//		filterRegistrationBean.setOrder(2);
//		filterRegistrationBean.addUrlPatterns("/*");
//		
//		return filterRegistrationBean;
//	}
	
}
