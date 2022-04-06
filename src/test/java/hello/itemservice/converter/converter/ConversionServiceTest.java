package hello.itemservice.converter.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import hello.itemservice.converter.type.IpPort;

// DefaultConversionService 에 커스텀한 converter를 addConverter 로 등록 후
// 참조 변수를 통해 convert 메서드 이용해서 input, output 타입을 매개변수로 써주면 끝
// 등록한 후에는 구체적인 converter를 알지 않아도 되는 장점이 있다

public class ConversionServiceTest {
	
	@Test
	void conversionService() {
		// 등록
		DefaultConversionService conversionService = new DefaultConversionService();
		conversionService.addConverter(new StringToIpPortConverter());
		conversionService.addConverter(new IpPortToStringConverter());
		
		
		// 사용
		IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
		Assertions.assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));
		
		String ipPortString = conversionService.convert(new IpPort("127.0.0.1", 8080), String.class);
		Assertions.assertThat(ipPortString).isEqualTo("127.0.0.1:8080");
	}
	
	
}
