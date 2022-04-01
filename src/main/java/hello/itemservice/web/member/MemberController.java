package hello.itemservice.web.member;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
	
	private final MemberRepository memberRepository;
	
	// 추가 폼 화면
	@GetMapping("/add")
	public String addForm(@ModelAttribute("member") Member member) {
		return "members/addMemberForm";
	}
	
	// 추가
	@PostMapping("/add")
	public String save (@Valid @ModelAttribute Member member, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			log.info("추가 실패");
			return "members/addMemberForm";
		}
		
		memberRepository.save(member);
		log.info("추가 성공");
		return "redirect:/";
	}
	
	
}
