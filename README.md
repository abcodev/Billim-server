# Billim
<img width="600" src="https://github.com/HyunjeongJang/Billim-server/assets/113197284/fd2d90ed-c350-4317-8915-31fe33ae8f61">

# 기획
1:1 물건 공유 및 결제 시스템을 제공하는 웹 사이트

<br>

# 사용 기술
|기술|버전|
|:----:|:----:|
|JAVA|11|
|Spring Boot|2.7.12|
|Spring Security||
|JPA||
|QueryDSL-JPA||
|MYSQL|8.0.32|
|Swagger|3.0.0|

<br>

# 프로젝트 구조
<img width="1134" alt="스크린샷 2023-06-30 오후 4 51 29" src="https://github.com/HyunjeongJang/Billim-server/assets/113197284/8fbe3f67-3f28-4a78-8585-2c885c09a605">

<br>

# Layered Architecture
### DDD

<img width="480" alt="스크린샷 2023-07-01 오후 8 22 46" src="https://github.com/HyunjeongJang/Billim-server/assets/113197284/552c6192-280a-4a11-af36-3e2cb744e313">

# 구현 기능
 
|기능|상세|
|:----:|:----:|
|회원|이메일 인증 / Spring Security / Jwt 인증 및 인가 / OAuth2.0 카카오 소셜로그인 |
|상품|상품 CRUD / 상품 검색 - 카테고리 & 키워드 |
|주문 및 결제|상품 예약 날짜 조회 / 상품 예약 및 결제 시스템 - PortOne 연동|
|채팅|판매자 구매자 간 1:1 채팅 / 회원 차단|



<br>

# 트러블 슈팅

## LazyLoading

### Open In View - false

- **리소스 측면**
Open In View true 일 경우 Api를 반환하는 시점까지 영속성 컨텍스트를 유지하고 데이터베이스 커넥션 또한 유지 된다 → 리소스 낭비 (규모가 커지면 리소스 부족으로 인해 장애가 생겼을 때 누수 지점을 찾는게 어려워진다.)
- **아키텍쳐 측면**
계층을 오갈 때 데이터를 담는걸 dto 를 이용해야 하고, 데이터 베이스에 대한 직접적인 정보를 담고있는 Entity 그대로 내리는걸 지양해야한다. 따라서 dto 로 매핑해서 반환할거면 Open In View 옵션이 필요없다.
- 즉, 필요 없는 옵션인데 리소스 낭비를 유발하므로 false 로 설정하였다.

### JPA Persistence Context 의 관계

Lazy Loading은 JPA Persistence Context 가 살아있을 때만 가능한데 트랜잭션을 벗어나면 해당 컨텍스트를 없애버려서 Lazy Loading이 불가능해진다.

1. 트랜잭션 범위를 통해 해결. 트랜잭션의 범위 지정은 @Transactional 어노테이션을 이용한 선언형 방식으로 지연조회 시점까지 세션을 유지한다.
2. 트랜잭션 범위를 벗어나기 전에 별도 DTO 로 매핑하는 과정을 통해 proxy 객체를 실체화함으로서 Lazy Loading을 해버리는 방식이 필요하다.

## S3 Bucket 이미지 업로드 하는 이유

1. 이전 프로젝트 Ec2 배포 경험으로 파일 업로드, 다운로드를 웹 서버에서 직접 관리하는것이 많은 문제가 있는것을 깨달았다. 파일 업로드, 다운로드는 디스크에 접근해야 하는데 접근 경로와 관리 비용이 많이 들게 된다.
2. 서버 사용자가 많아지면, 하나의 서버로 처리할 수 있는 요청은 제한이 있기 때문에 느려지게 된다. 서버에 이미지를 저장하면서 서버 사용자가 많아지면, 세션도 httpSession 같은 경우에는컴퓨터 메모리에 저장하기 때문에 컴퓨터가 여러개 되는 환경에서 이 세션을 공유할 수가 없게 된다. 대응할 수 있는 방법으로는 Scale-Out(컴퓨터 개수 늘리기) , Scale-Up(성능 up) 이 있는데 Scale-Out 을 고려 하여 S3를 도입하였다. S3 자체가 수천 대 이상의 성능이 좋은 웹 서버로 구성되어 있어서 EC2로 구축했을 때 처럼 Auto Scaling이나 Load Balancing에 신경쓰지 않아도 된다.

<br>

# 참여
|Back-End|Back-End|Front-End|
|:---:|:---:|:---:|
|장현정 | 염서학 | 심지현 |
| <a href="https://github.com/HyunjeongJang">@HyunjeongJang</a> | <a href="https://github.com/YEOMCODING">@YEOMCODING</a> | <a href="https://github.com/jh0neee"> @jh0neee</a> |

<br>
