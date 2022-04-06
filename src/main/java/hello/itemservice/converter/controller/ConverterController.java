package hello.itemservice.converter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hello.itemservice.converter.type.IpPort;
import lombok.Data;

@Controller
public class ConverterController {
	
	// converter를 타임리프에서 실행하려면 '{{...}}' 이렇게 작성해야한다. 
	@GetMapping("/converter-view")
	public String converterView(Model model) {
		model.addAttribute("number", 10000);
		model.addAttribute("ipPort", new IpPort("127.0.0.1", 8080)); // 이게 String으로 표현됨
		return "converter/converter-view";
	}
	
	// form 안의 th:field 는 컨버전 서비스도 함께 적용된다.(여기서는 ipPort -> String)
	@GetMapping("/converter/edit")
	public String converterForm(Model model) {
		IpPort ipPort = new IpPort("127.0.0.1", 8080);
		Form form = new Form(ipPort); // ipPort를 필드로 가지는 객체 Form
		model.addAttribute("form", form);
		return "converter/converter-form";
	}
	
	// @ModelAttribute를 통해 문자를 객체로 받게된다. 
	@PostMapping("/converter/edit")
	public String converterEdit(@ModelAttribute Form form, Model model) {
		IpPort ipPort = form.getIpPort();
		model.addAttribute("ipPort", ipPort);
		return "converter/converter-view";
	}
	
	// TODO: 컨트롤러와 타임리프에서 컨버터 서비스 사용법 
	// 결론: 컨버전 서비스를 등록했다면 @RequestParam, @ModelAttribute 등이 자동으로 변환해줌
	// 또한 타임리프에서 자동 변환을 사용하려면 ${} 대신 ${{}} 사용
	
	
	@Data
	static class Form {
		private IpPort ipPort;
		
		public Form(IpPort ipPort) {
			this.ipPort = ipPort;
		}
	}
}

// 주의 : 
// 메시지 컨버터에는 컨버전서비스가 적용되지 않는다.
// 특히 객체를 JSON 으로 변환할 때 메시지 컨버터를 이용하면서 오해할 수 있음
// HttpMessageConverter 의 역할은 HTTP 메시지 바디 <-> 객체 의 변환이지
// 객체 간의 변환이 아니다
// 따라서 JSON 의 결과로 만들어지는 숫자나 날짜 포맷을 변경하고 싶으면  해당 라이브러리가 제공하는 설정을 통해 포맷을 지정해야한다.
// 결론: HttpMessageConverter는 컨버전 서비스와는 다르다

