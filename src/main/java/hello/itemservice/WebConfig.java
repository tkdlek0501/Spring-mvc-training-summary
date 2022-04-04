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
	
	// 직접 만든 resolver 등록 -> 어노테이션
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
			.excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error", "error-page/**"); // 제외할 path 설정
			// dispatcherType 설정을 여기서 가능 (error-page/** 는 인터셉터를 타지 않게)
	}
	
	// filter는 servlet 전에 호출
//	@Bean
//	public FilterRegistrationBean loginCheckFilter() {
//		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
//		filterRegistrationBean.setFilter(new LoginCheckFilter());
//		filterRegistrationBean.setOrder(1);
//		filterRegistrationBean.addUrlPatterns("/*");
//		// filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR); // 예외 처리 시 ERROR도 필터를 타도록 (필터가 2번 호출됨)
//		// -> 필터는 기본적으로 DispatcherType 이 Request라서 따로 설정 안해줘도 1번 호출 된다
//		
//		return filterRegistrationBean;
//	}
	
}
