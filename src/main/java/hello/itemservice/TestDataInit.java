package hello.itemservice;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import hello.itemservice.domain.product.Product;
import hello.itemservice.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
    	productRepository.save(new Product("productA", 10000, 10));
    	productRepository.save(new Product("productB", 20000, 20));
    
    	Member member = new Member();
    	member.setLoginId("test");
    	member.setPassword("test!");
    	member.setName("tester");
    	memberRepository.save(member);
    }
    
    
    
}