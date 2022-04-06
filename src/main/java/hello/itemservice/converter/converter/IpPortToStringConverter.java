package hello.itemservice.converter.converter;

import org.springframework.core.convert.converter.Converter;

import hello.itemservice.converter.type.IpPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpPortToStringConverter implements Converter<IpPort, String>{

	@Override
	public String convert(IpPort source) {
		log.info("convert source={}", source);
		// IpPort 객체 -> "127.0.0.1:8080" 이라는 문자로 반환해야됨
		return source.getIp() + ":" + source.getPort();
	}

}
