package hello.itemservice.converter.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.itemservice.converter.type.IpPort;

@RestController
public class HelloController {
	
	@GetMapping("/converter/requestparam")
	public String converterRequestParam(@RequestParam Integer data) { // 쿼리 스트링으로 전달하는 값은 String 이지만 @RequestParam이 타입 컨버터 역할을 해줌
		//Integer intValue = Integer.valueOf(data); // 필요 없어짐
		System.out.println("data : " + data);
		return "ok";
	}
	
	@GetMapping("/ip-port")
	public String ipPort(@RequestParam IpPort ipPort) { // 쿼리 스트링으로 전달한 String 이 IpPort 객체 타입으로 변환되어 받아온다
		System.out.println("ipPort IP : " + ipPort.getIp());
		System.out.println("ipPort Port : " + ipPort.getPort());
		return "OK";
	}
	
	
}
