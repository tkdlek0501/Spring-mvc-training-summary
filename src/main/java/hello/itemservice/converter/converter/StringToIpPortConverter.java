package hello.itemservice.converter.converter;

import org.springframework.core.convert.converter.Converter;

import hello.itemservice.converter.type.IpPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {
	
	@Override
	public IpPort convert(String source) {
		log.info("convert source={}", source);
		// String으로 받은 port -> IpPort 객체
		String[] split = source.split(":"); // 127.0.0.1:8080 을 ':' 기준으로 split
		String ip = split[0];
		int port = Integer.parseInt(split[1]);
		return new IpPort(ip, port);
	}
	
	
	
}
