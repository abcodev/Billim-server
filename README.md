# Billim
<img width="600" src="https://github.com/HyunjeongJang/Billim-server/assets/113197284/fd2d90ed-c350-4317-8915-31fe33ae8f61">

# 기획
1:1 물건 공유 및 결제 시스템을 제공하는 웹 사이트

<br>

# 프로젝트 구조
<img width="1210" alt="스크린샷 2023-07-06 오전 12 37 28" src="https://github.com/HyunjeongJang/Billim-server/assets/113197284/13990774-5f1a-499c-8906-55e282c42f50">



<br>

# 구현 기능
 
|기능|상세|
|:----:|:----:|
|회원|이메일 인증 / Spring Security / Jwt 인증 및 인가 / OAuth2.0 카카오 소셜로그인 |
|상품|상품 CRUD / 상품 검색 - 카테고리 & 키워드 |
|주문 및 결제|상품 예약 날짜 조회 / 상품 예약 및 결제 시스템 - PortOne 연동|
|채팅|판매자 구매자 간 1:1 채팅 / 회원 차단|

<br>

# Jenkins CI/CD 파이프라인 구축
<img width="1727" alt="스크린샷 2023-07-10 오후 1 30 40" src="https://github.com/HyunjeongJang/Billim-server/assets/113197284/19ec8f13-df37-4313-bba4-6a418c2dacd4">




<br>

# 트러블 슈팅

<!-- ## QueryDSL

### 이슈

JPA 페이징 + 키워드 검색 넘길 때 Count 쿼리에서 Fetch Join을 사용하면,  Spring Data가 객체를 만들 때 만약 반환값이 Page 라면 쿼리를 2번 날리는데 첫 번째 Count 쿼리에도 Fetch Join이 적용되는 이슈가 발생하였다.

### Paging + Fetch Join 을 했을 때 에러가 나는 이유

- Fetch Join 을 써야하는 이유?
    - “N + 1 문제” : Lazy Loading 을 사용한다. -> 그래서 발생한다?
    - JPA 연관관계 상에서 연관관계에 대한 객체를 조회하기 위한 별도의 SQL 쿼리문이 날라가는 문제
    - List<Member> 를 조회 -> Member 는 Product 를 1:N 연관관계로 가지고 있다.
    - List<Member> 를 조회하는 쿼리 1번이 날아간다. (SELECT * FROM member WHERE ~)
    - 그리고 그 연관관계를 가지는 Product 를 가져오기 위해서 List<Member> 의 size 만큼의 쿼리가 날아간다. (N번의 쿼리)
    - 결국 많은 쿼리가 발생한다는것은 곧 DB 에 가해지는 부하에 직결되는 문제
    - 이걸 해결하기 위한 방법으로 Fetch Join 을 사용함
    - 반드시 Fetch Join 을 써야하는건 아니고 N+1 문제로 인해서 DB 성능에 이슈가 되는 케이스에만 적용
- 두 가지를 동시에 적용해보았음
- Page 객체를 반환하는 추상메소드를 만들면, 쿼리가 두번 (count 쿼리, select offset limit 쿼리) 날아가는데 , Fetch Join 을 적용하기 위해서 JPQL 을 사용했고, 그 과정에서 두가지 쿼리에 모두 Fetch Join 이 적용됐다. 그 결과 count 쿼리에도 Fetch Join 이 적용됐고 거기서 에러가 발생 하였다. 이 에러를 해결하기 위해서는 count 쿼리에는 Fetch Join 을 적용하지 말아야하고, select offset limit 쿼리에는 Fetch Join 을 적용해야한다.
- 하지만, Spring Data JPA나 JPQL는 Repository에 대한 구현 객체를 내가 직접 개발하는게 아니라 자동으로 만들어주고 있기 때문에 직접적인 쿼리 튜닝이 어렵다. 따라서 그게 가능한 QueryDSL 을 적용 하게 되었다.

### QueryDSL을 도입하게 된 이유

JPA를 사용하면, Spring Data JPA에서 사용하는 Spring Data Repository는 내가 추상메소드만 만들어도 거기에 대한 구현체를 알아서 만들어준다. Spring Data 가 내가 만든 인터페이스의 추상메서드를 보고 적절한 객체를 만들어준다. JPQL을 우선적으로 사용해도 Spring Data Repository 규칙을 벗어날 수는 없다.

즉, 기존 Spring Data JPA 를 사용할땐 쿼리를 직접 만드는게 아닌, 자동으로 만들어주는 부분이라 카운트 쿼리만Fetch Join을 사용안하도록 커스텀할수가없었고, 커스텀할수있는 QueryDSL 을 사용 하게 되었다.

JPQL은 객체중심으로 쿼리문을 짜게 되어서, 직접 쿼리문을 작성 하다보니 오타가 나거나 잘못된 쿼리문을 작성했을때 컴파일 에러가 나지 않고, 실행하는 시점에만 런타임 에러가 발생하기 때문에 서비스 운영측면서 치명적이다.

QueryDSL 을 사용하게 되면 메소드 체인 형태로 쿼리문을 작성하기 때문에 잘못된 쿼리에 대해서 런타임이 아니라 컴파일 시점에 에러를 식별할 수 있게 되었다.

### JPA + QueryDSL 을 같이 사용하면서 객체지향 설계적인 측면에서 고려한점

Service 입장에서는 Repository를 의존하면서 데이터베이스와 소통을 하고 있다. Service 는 사실Repository 의 구현 객체가 어떻게 생겼는지 관심이 없어야 한다 → Service 와 Repository 구현 객체간의 결합도 낮추기. Repository 구현 객체의 자율성 → 이걸 지키기 위해서 Repository 구현 객체에 캡슐화 적용

Repository 입장에서는 자유로워야된다. Repository 내부에서 JPA 를 사용하다가 QueryDSL + JPA 로 내용을 바꾸더라도 상관없어야하고 아무한테도 영향안주는게 좋다. -> Repository 는 변경에 자유로운 객체다.

Service 에서 데이터베이스에 정보를 요청하기 위해서 Repository 객체한테 요청하게 되고, Service 입장에서 조회해오는 데이터가 JPA 를 통해서 가져온 데이터인지, QueryDSL 을 통해서 가져온 데이터인지 알아야 하는건 아니다. 단지 Service 는 Repository 한테 요청을 할 뿐이고 그 응답만 올바르게 반환되길 기대한다.

JPA, QueryDSL 을 통해서 데이터를 가져온다는 같은 구체적인 내용은  Service 가 이 사실을 알게되면 Service 와Repository 는 결합도가 매우 높아져 변경에 취약하게 된다.

( 구체적인 것에 의존하면 변경이 전파될 확률이 높다 )

그래서 추상화 타입인 Repository 인터페이스(고수준)만을 의존해서 변경이 전파될 확률을 낮춘다.

따라서 캡슐화(구체적인걸 감추는 일)를 통해 Repository 가 JPA, QueryDSL 뭘 사용하는지 외부가 모르게 만들고자객체지향적인 구조를 고려하였다.

-->


## LazyLoading

### Open In View - false

- **리소스 측면**
Open In View true 일 경우 Api를 반환하는 시점까지 영속성 컨텍스트를 유지하고 데이터베이스 커넥션 또한 유지 된다 → 리소스 낭비 (규모가 커지면 리소스 부족으로 인해 장애가 생겼을 때 누수 지점을 찾는게 어려워진다.)
- **아키텍쳐 측면**
계층을 오갈 때 데이터를 담는걸 dto 를 이용해야 하고, 데이터 베이스에 대한 직접적인 정보를 담고있는 Entity 그대로 내리는걸 지양해야한다. 따라서 dto 로 매핑해서 반환할거면 Open In View 옵션이 필요없다.
- 즉, 필요 없는 옵션인데 리소스 낭비를 유발하므로 false 로 설정하였다.

### JPA Persistence Context 의 관계

* Lazy Loading 는 JPA Persistence Context 가 살아있을 때만 가능한데 트랜잭션을 벗어나면 해당 컨텍스트를 없애버려서 Lazy Loading이 불가능해진다.
→ Proxy Initialize 에러발생

* 트랜잭션 범위를 통해 해결 : 트랜잭션의 범위 지정은 @Transactional 어노테이션을 이용한 선언형 방식으로 지연조회 시점까지 JPA Persistence Context 를 유지한다.
* 트랜잭션 범위를 벗어나기 전에 별도 DTO 로 매핑하는 과정을 통해 해결 : proxy 객체를 실체화함으로서 Lazy Loading을 해버리는 방식이 필요하다.

<!-- ### Entity 객체를 DTO에 그대로 넣으면 안되는 이유

* DTO에 Entity 객체를 그대로 넣으려고 하면 Proxy 에러가 발생한다. 예를들어 Product가 조회될 때 Member, Category 는 Lazy Loading 설정을 했기 때문에 우선적으로 사용하지 않는다면 그 자리를 Proxy 객체가 차지하고 있다. 해당 필드를 참조하는 시점에 Proxy 객체가 실제 데이터베이스에서 정보를 가져오게 되는데 Proxy 객체가 사용자가 조회한 데이터를 반환해준다. 하지만 Proxy 객체가 없어지고 그 자리를 Entity 가 차지하는게 아니기 때문에 해당 객체를 Controller 에서 반환하려고 한다면, 객체를 JSON 으로 파싱하는 과정에서 에러가 발생한다. -->


## S3 Bucket 이미지 업로드

* 이전 프로젝트 Ec2 배포 경험으로 파일 업로드, 다운로드를 웹 서버에서 직접 관리하는것이 많은 문제가 있는것을 깨달았다. 파일 업,다운로드는 디스크에 접근해야 하는데 접근 경로와 관리 비용이 많이 들게 된다.
* 또한 서버 사용자가 많아지면, 하나의 서버로 처리할 수 있는 요청은 제한이 있기 때문에 느려지게 된다. 대응할 수 있는 방법으로는 Scale-Out(컴퓨터 개수 늘리기) , Scale-Up(성능 up) 이 있는데 Scale-Out 을 도입했다. Scale-Out 도입하면서 생긴 문제로는 서버에 이미지를 저장하면서 서버 사용자가 많아졌을 때 확장된 서버들이 해당 이미지를 공유할수 없게 된다. 이러한 문제를 해결하기 위해 이미지를 Spring 서버에 직접 저장하는 것이 아닌 외부에 저장하려고 S3를도입하였다.

<br>

# Layered Architecture
<img width="1405" alt="스크린샷 2023-07-02 오후 8 10 54" src="https://github.com/HyunjeongJang/Billim-server/assets/113197284/8422d035-8b2c-4ebb-9db3-0c022c7c4471">

<br>

# Swagger API 명세서
<img width="1730" alt="스크린샷 2023-07-01 오후 8 17 47" src="https://github.com/HyunjeongJang/Billim-server/assets/113197284/eb93aa49-ce30-4c26-b636-47042a946a2a">



# 참여
|Back-End|Back-End|Front-End|
|:---:|:---:|:---:|
|장현정 | 염서학 | 심지현 |
| <a href="https://github.com/HyunjeongJang">@HyunjeongJang</a> | <a href="https://github.com/YEOMCODING">@YEOMCODING</a> | <a href="https://github.com/jh0neee"> @jh0neee</a> |

<br>
