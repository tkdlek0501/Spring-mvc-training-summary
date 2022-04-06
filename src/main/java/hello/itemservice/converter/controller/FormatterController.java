package hello.itemservice.converter.controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.Data;

// TODO: 스프링에서 제공하는 기본 포맷터 사용 

@Controller
public class FormatterController {
	
	
	@GetMapping("/formatter/edit")
	public String formatterForm(Model model) {
		Form form = new Form();
		form.setNumber(10000);
		form.setLocalDateTime(LocalDateTime.now());
		model.addAttribute("form", form);
		return "converter/formatter-form";
	}
	
	// "10,000" -> 10000
	// "2021-06-18 23:00:45" -> localDateTime 객체
	@PostMapping("/formatter/edit")
	public String formatterEdit(@ModelAttribute Form form) { 
		// model 에 담는 것 생략  (@ModelAttribute가 자동으로 담아줌)
		return "converter/formatter-view";
	}
	
	
	// 콤마 처리와 날짜 처리
	// 포맷터는 객체(숫자 포함) <-> 문자의 개념
	// @NumberFormat 등의 컨버터(컨버전 서비스)는 @ModelAttribute 등을 사용할 때 작동한다.
	@Data
	static class Form {
		@NumberFormat(pattern = "###,###")
		private Integer number;
		
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private LocalDateTime localDateTime;
	}
	
}
