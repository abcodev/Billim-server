package com.web.billim.point.domain.service;

import com.web.billim.member.domain.Member;
import com.web.billim.point.domain.SavedPoint;
import com.web.billim.point.repository.SavedPointRepository;
import com.web.billim.point.service.PointDomainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PointDomainServiceTest {

	@Mock
	private SavedPointRepository savedPointRepository;

	@InjectMocks
	private PointDomainService pointDomainService;

	// Repository <- Service <- Controller
	//  Service 에서 Repository 객체를 모킹해서 단위테스트를 짜려면
	//  모킹되는 Repository 객체도 테스트가 작성되어 있어야 한다.
	// 테스트 코드의 우선순위 : Domain 로직 -> Service 로직 -> Repository/Controller
	// 테스트 커버리지 -> 100% 지향
	//  BDD TDD
	@Test
	public void use_SavedPoint_빈_리스트_반환하면_에러나는지_확인() {
		// given
		Member member = Member.builder().memberId(1).build();
		given(savedPointRepository.findAllNotExpired(1))
				.willReturn(Collections.emptyList());

		// when & then
		assertThrows(RuntimeException.class,
				() -> pointDomainService.use(member, 1000));
	}

	@Test
	public void use_사용가능_적립금_부족하면_에러나는지_확인() {
		// given
		Member member = Member.builder().memberId(1).build();
		int amount = 5000;
		SavedPoint point_2000 = SavedPoint.builder().availableAmount(2000).build();
		given(savedPointRepository.findAllNotExpired(1))
				.willReturn(List.of(point_2000));

		// when & then
		assertThrows(RuntimeException.class,
				() -> pointDomainService.use(member,amount));
	}

	@Test
	public void use_정상동작_확인() {
		// given
		Member member = Member.builder().memberId(1).build();
		int amount = 5000;
		SavedPoint point_1000 = SavedPoint.builder().availableAmount(1000).build();
		SavedPoint point_2000 = SavedPoint.builder().availableAmount(2000).build();
		SavedPoint point_3000 = SavedPoint.builder().availableAmount(3000).build();
		given(savedPointRepository.findAllNotExpired(1))
				.willReturn(List.of(point_1000, point_2000, point_3000));

		// when
		pointDomainService.use(member, amount);

		// then
		assertEquals(point_1000.getAvailableAmount(), 0);
		assertEquals(point_2000.getAvailableAmount(), 0);
		assertEquals(point_3000.getAvailableAmount(), 1000);
	}

	@Test
	public void use_사용된_포인트_반환값_확인() {
		// given
		Member member = Member.builder().memberId(1).build();
		int amount = 2000;
		SavedPoint point_1000 = SavedPoint.builder().availableAmount(1000).build();
		SavedPoint point_2000 = SavedPoint.builder().availableAmount(2000).build();
		SavedPoint point_3000 = SavedPoint.builder().availableAmount(3000).build();
		given(savedPointRepository.findAllNotExpired(1))
				.willReturn(List.of(point_1000, point_2000, point_3000));

		// when
		Map<SavedPoint, Integer> usedPointMap = pointDomainService.use(member, amount);

		// then
		assertEquals(usedPointMap.size(), 2);
		assertEquals(usedPointMap.get(point_1000), 1000);
		assertEquals(usedPointMap.get(point_2000), 1000);
		assertEquals(point_1000.getAvailableAmount(), 0);
		assertEquals(point_2000.getAvailableAmount(), 1000);
		assertEquals(point_3000.getAvailableAmount(), 3000);
	}

}