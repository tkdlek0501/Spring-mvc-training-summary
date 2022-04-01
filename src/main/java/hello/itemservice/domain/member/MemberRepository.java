package hello.itemservice.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

// Member 저장소; 저장 관련 메서드 구현
@Slf4j
@Repository
public class MemberRepository {
	// 인터페이스로 만들어서 구현체를 따로 작업하는게 더 좋은 설게
	
	private static Map<Long, Member> store = new HashMap<>(); //static 사용
	private static long sequence = 0L; //static 사용 
	
	public Member save(Member member) {
		member.setId(++sequence);
		log.info("save: member={}", member);
		store.put(member.getId(), member);
		return member;
	}
	
	public Member findById(Long id) {
		return store.get(id);
	}
	
	// Optional 에 Member 객체를 담을 수도 있고 안담길 수도 있음 (NPE를 방지)
	public Optional<Member> findByLoginId(String loginId){
//		List<Member> all = findAll();
//		for(Member m : all) {
//			if(m.getLoginId().equals(loginId)){
//				return Optional.of(m);
//			}
//		}
//		return Optional.empty();
		
		// TODO: 람다식 쓰는 예시
		// 위 코드와 동일
		return findAll().stream()
				.filter(m -> m.getLoginId().equals(loginId))
				.findFirst();
	}
	
	public List<Member> findAll(){
		log.info("store : {}", store.values());
		return new ArrayList<>(store.values());
	}
	
	public void clearStore() {
		store.clear();
	}
}
