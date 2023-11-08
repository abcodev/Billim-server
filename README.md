# Billim
<img width="600" src="https://github.com/HyunjeongJang/Billim-server/assets/113197284/fd2d90ed-c350-4317-8915-31fe33ae8f61">

# 기획
1:1 물건 대여 및 결제 시스템을 제공하는 웹 사이트

<br>

# 프로젝트 구조
<!-- <img width="1210" alt="스크린샷 2023-07-06 오전 12 37 28" src="https://github.com/HyunjeongJang/Billim-server/assets/113197284/13990774-5f1a-499c-8906-55e282c42f50"> -->

<img width="1426" alt="스크린샷 2023-11-08 오전 10 30 34" src="https://github.com/HyunjeongJang/Billim-server/assets/113197284/2d740b7a-d049-4194-a26f-b4d080fe5bda">

<br>

# 구현 기능
 
|기능|상세|
|:----:|:----:|
|회원|이메일 인증 / Spring Security / Jwt / OAuth2 카카오 소셜로그인 |
|상품|상품 CRUD / 상품 카테고리 및 키워드 검색 |
|쿠폰 및 적립금| 회원 가입시 발급 / 회원 등급에 따른 적립률 적용 / 주문시 사용|
|주문 및 결제|상품 예약 시스템 / 상품 결제 및 취소 시스템 - 카카오페이|
|채팅|판매자 구매자 간 1:1 채팅 / 실시간 읽음 처리|

<br>

<!-- # 소개 -->

# Jenkins CI/CD 파이프라인 구축
<img width="1728" alt="젠킨스" src="https://github.com/HyunjeongJang/Billim-server/assets/113197284/55d05056-284f-4350-bf14-54ba0d18a7ad">


<br>

# 트러블 슈팅

## QueryDSL

### 이슈

* JPA 페이징 + 키워드 검색을 사용할 때 Count 쿼리에서 Fetch Join을 사용하면, Spring Data가 객체를 만들 때 반환값이 Page인 경우에 쿼리를 2번 날리는데, 첫 번째 Count 쿼리에도 Fetch Join이 적용되는 이슈가 발생

### Paging + Fetch Join을 했을 때 에러가 나는 이유

* N+1 문제를 해결하기 위해 Fetch Join을 사용하였다. N + 1 문제란 Lazy Loading을 사용할 때 연관된 객체를 조회하기 위해 별도의 SQL 쿼리문이 발생하는 문제를 말한다. 예를 들어, List<Member> 를 조회할 때 Member 객체들이 Product 객체들을 1:N 연관관계로 가지고 있다면, List<Member> 를 조회하는 쿼리(SELECT * FROM member WHERE ~) 한번이 날아간다. 그리고 그 연관관계를 가지는 Product 를 가져오기 위해서 List<Member> 의 size 만큼의 쿼리(N번의 쿼리)가 날아간다. 이러한 쿼리의 증가는 DB 성능에 부하를 주는 문제가 된다. 이를 해결하기 위해 Fetch Join을 사용하는데, Fetch Join은 반드시 사용해야 하는 것은 아니며 N+1 문제로 인해 DB 성능에 이슈가 되는 경우에만 적용한다.

* 두 가지를 동시에 적용해 보았다.
Page 객체를 반환하는 추상 메소드를 만들면, 쿼리가 두 번(Count 쿼리, select offset limit 쿼리) 발생한다. Fetch Join을 적용하기 위해 JPQL을 사용했고, 이 과정에서 두 쿼리에 모두 Fetch Join이 적용되었다. 이로 인해 Count 쿼리에도 Fetch Join이 적용되어 에러가 발생하였다. 이 문제를 해결하기 위해서는 Count 쿼리에는 Fetch Join을 적용하지 말고, select offset limit 쿼리에는 Fetch Join을 적용해야 하는데, Spring Data JPA나 JPQL은 Repository에 대한 구현 객체를 직접 개발하지 않고 자동으로 생성해주기 때문에 직접적인 쿼리 튜닝이 어렵다. 따라서 이를 가능하게 해주는 QueryDSL을 도입하게 되었다.


### QueryDSL을 도입하게 된 이유

* JPA를 사용하면 Spring Data JPA에서 사용하는 Spring Data Repository는 추상 메소드만 작성하면 해당 구현체를 자동으로 생성준다. JPQL을 우선적으로 사용해도 Spring Data Repository 규칙을 벗어날 수는 없다. 기존에 Spring Data JPA를 사용할 때는 쿼리를 직접 작성하지 않고 자동으로 생성되는 부분이기 때문에 카운트 쿼리만 Fetch Join을 사용하지 않도록 커스텀할 수 없다. 이에 비해 커스텀할 수 있는 QueryDSL을 사용하게 되었습니다.
* 또한, JPQL은 객체중심으로 쿼리문을 작성하게 되어서, 직접 쿼리문을 작성 하다보니 오타가 나거나 잘못된 쿼리문을 작성했을 때 컴파일 에러가 나지않고, 실행하는 시점에만 런타임 에러가 발생하기 때문에 서비스 운영측면서 치명적이다.
QueryDSL을 사용하게 되면 메소드체인 형태로 쿼리문을 작성하기 때문에 잘못된 쿼리에 대해서 런타임이 아니라 컴파일 시점에 에러를 식별할 수 있게 되었다.



### JPA + QueryDSL을 같이 사용하면서 객체지향 설계적인 측면에서 고려한점

* Service 입장에서는 Repository에 의존하면서 데이터베이스와 소통하는데, Service는 실제로 어떻게 Repository의 구현체가 생겼는지에 대한 관심이 없어야 한다. Service와 Repository 구현 객체 간의 결합도를 낮추기 위해 Repository 구현 객체의 자율성을 높인다. 이를 위해 Repository 구현 객체에 캡슐화를 적용한다.
* Repository는 변경에 자유로은 객체여야 한다. Repository 내부에서 JPA를 사용하다가 QueryDSL + JPA로 내용을 변경해도 상관없어야 하며, 아무한테도 영향을 주지 않는 것이 좋다. 
* JPA와 QueryDSL을 통해서 데이터를 가져온다는 구체적인 내용은 Service가 이 사실을 알게 되면 Service와 Repository의 결합도가 매우 높아져 변경에 취약하게 된다(구체적인것에 의존하면 변경이 전파될 확률이 높다). 따라서 추상화 타입인 Repository 인터페이스(고수준)만을 의존함으로써 변경이 전파될 확률을 낮춘다.
* 즉, 캡슐화를 적용하여 Repository가 JPA, QueryDSL을 사용하는지 외부에 감추는 객체지향적인 구조를 고려하였다.



## LazyLoading

### Open In View - false

- **리소스 측면**
Open In View true 일 경우 Api를 반환하는 시점까지 영속성 컨텍스트를 유지하고 데이터베이스 커넥션 또한 유지 된다 → 리소스 낭비 (규모가 커지면 리소스 부족으로 인해 장애가 생겼을 때 누수 지점을 찾는게 어려워진다.)
- **아키텍쳐 측면**
계층을 오갈 때 데이터를 담는걸 dto 를 이용해야 하고, 데이터 베이스에 대한 직접적인 정보를 담고있는 Entity 그대로 내리는걸 지양해야한다. 따라서 dto 로 매핑해서 반환할거면 Open In View 옵션이 필요없다.
- 즉, 필요 없는 옵션인데 리소스 낭비를 유발하므로 false 로 설정하였다.

### JPA Persistence Context의 관계

* Lazy Loading는 JPA Persistence Context가 살아있을 때만 가능한데 트랜잭션을 벗어나면 해당 컨텍스트를 없애버려서 Lazy Loading이 불가능해진다.
→ Proxy Initialize 에러발생

* 트랜잭션 범위를 통해 해결 : 트랜잭션의 범위 지정은 @Transactional 어노테이션을 이용한 선언형 방식으로 지연조회 시점까지 JPA Persistence Context를 유지한다.
* 트랜잭션 범위를 벗어나기 전에 별도 DTO로 매핑하는 과정을 통해 해결 : proxy 객체를 실체화함으로서 Lazy Loading을 해버리는 방식이 필요하다.

### Entity 객체를 DTO에 그대로 넣으면 안되는 이유

* DTO에 Entity 객체를 그대로 넣으려고하면 Proxy 에러가 발생한다. 예를들어 Product가 조회될 때 Member, Category는 Lazy Loading 설정을 했기 때문에 우선적으로 사용하지 않는다면 그 자리를 Proxy 객체가 차지하고 있다. 해당 필드를 참조하는 시점에 Proxy 객체가 실제 데이터베이스에서 정보를 가져오게 되는데 Proxy 객체가 사용자가 조회한 데이터를 반환해준다. 하지만 Proxy 객체가 없어지고 그 자리를 Entity가 차지하는게 아니기 때문에 해당 객체를 Controller에서 반환하려고 한다면, 객체를 JSON으로 파싱하는 과정에서 에러가 발생한다. 


## S3 Bucket

* 이전 프로젝트 Ec2 배포 경험으로 파일 업로드, 다운로드를 웹 서버에서 직접 관리하는것이 많은 문제가 있는것을 깨달았다.파일 업,다운로드는 디스크에 접근해야 하는데 접근 경로와 관리 비용이 많이 발생하게 된다.
* 또한 서버 사용자가 많아지면, 하나의 서버로 처리할 수 있는 요청은 제한이 있기 때문에 느려지게 된다. 대응할 수 있는 방법으로는 Scale-Out, Scale-Up이 있는데 Scale-Out을 도입했다. 이러한 문제를 해결하기위해 이미지를 Spring 서버에 직접 저장하는 것이 아닌 외부에 저장하려고 S3를도입하였다.

<br>

# Layered Architecture
<img width="1405" alt="스크린샷 2023-07-02 오후 8 10 54" src="https://github.com/HyunjeongJang/Billim-server/assets/113197284/8422d035-8b2c-4ebb-9db3-0c022c7c4471">

<br>

# Swagger API 명세서
<img width="1292" alt="스크린샷 스웨거" src="https://github.com/HyunjeongJang/Billim-server/assets/113197284/62344be5-f669-4563-b1ca-957306ab2379">


# 참여
|Back-End|Back-End|Front-End|
|:---:|:---:|:---:|
|장현정 | 염서학 | 심지현 |
| <a href="https://github.com/HyunjeongJang">@HyunjeongJang</a> | <a href="https://github.com/YEOMCODING">@YEOMCODING</a> | <a href="https://github.com/jh0neee"> @jh0neee</a> |

<br>
