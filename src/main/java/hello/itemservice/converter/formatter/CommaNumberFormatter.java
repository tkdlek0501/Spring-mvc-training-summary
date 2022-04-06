package hello.itemservice.converter.formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import lombok.extern.slf4j.Slf4j;

// TODO: 숫자에 천단위 콤마처리
// 1000 단위 숫자 <-> 문자 포맷
// formatter 는 객체(or 숫자) <-> 문자 컨버터를 의미한다.

@Slf4j
public class CommaNumberFormatter implements Formatter<Number>{
	
	// locale정보로 나라별 다른 숫자 포맷 적용
	// NumberFormat 에서 지원
	
	// print: Number로 받은 것을 String으로
	@Override
	public String print(Number object, Locale locale) {
		log.info("object={}, locale={}", object, locale);
		return NumberFormat.getInstance(locale).format(object);
	}
	
	// parse : String으로 받은 것을 Number로 
	@Override
	public Number parse(String text, Locale locale) throws ParseException {
		log.info("text={}, locale={}", text, locale);
		// "1,000" -> 1000
		NumberFormat format = NumberFormat.getInstance(locale);
		return format.parse(text);
	}
	
	
	
}
