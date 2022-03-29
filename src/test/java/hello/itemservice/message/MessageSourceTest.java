package hello.itemservice.message;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

@SpringBootTest
public class MessageSourceTest {
	
	@Autowired
	MessageSource messageSource; // properties 에 등록한 빈으로 의존관계 주입
	
	@Test
	void helloMessage() {
		String result = messageSource.getMessage("hello", null, null); // code, args, locale
		Assertions.assertThat(result).isEqualTo("안녕");
		//Assertions.assertThat(messageSource.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
		//Assertions.assertThat(messageSource.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
	}
	// locale 정보가 없으면 application.properties 에서 설정한 basename 의 파일을 조회
	
	@Test
	void notFoundMessageCodeDefaultMessage() {
		String result = messageSource.getMessage("no_code", null, "기본 메시지", null);
		Assertions.assertThat(result).isEqualTo("기본 메시지");
	}
}
