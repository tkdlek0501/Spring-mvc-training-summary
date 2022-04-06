package hello.itemservice.converter.formatter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

import hello.itemservice.converter.converter.IpPortToStringConverter;
import hello.itemservice.converter.converter.StringToIpPortConverter;
import hello.itemservice.converter.type.IpPort;

public class FormattingConversionServiceTest {
	
	// TODO: 컨버터, 포맷터 직접 사용하기
	
	@Test
	void formattingConversionService() {
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(); // FormattingConversionService는 컨버터 + 포맷터 등록 가능
		// 컨버터 등록
		conversionService.addConverter(new StringToIpPortConverter());
		conversionService.addConverter(new IpPortToStringConverter());
		// 포맷터 등록
		conversionService.addFormatter(new CommaNumberFormatter());
		
		
		// 컨버터 사용
		IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
		Assertions.assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));
		// 포멧터 사용
		Assertions.assertThat(conversionService.convert(1000, String.class)).isEqualTo("1,000");
		Assertions.assertThat(conversionService.convert("1,000", Long.class)).isEqualTo(1000);
	}
	
}
