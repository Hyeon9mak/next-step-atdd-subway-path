# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 실습 - 단위 테스트 작성

- [x] 지하철 구간 관련 단위 테스트를 완성하세요. 
  - [x] 구간 단위 테스트 (LineTest)
  - [x] 구간 서비스 단위 테스트 with Mock (LineAddSectionServiceMockTest)
  - [x] 구간 서비스 단위 테스트 without Mock (LineAddSectionServiceTest)
- [x] 단위 테스트를 기반으로 비즈니스 로직을 리팩터링 하세요.

<br>

## 1단계 - 구간 추가 요구사항 반영

### 기능 요구사항(완료 조건)

- [x] 노선에 역 추가시 노선 가운데 추가 할 수 있다.
  - [x] 가운데 추가되는 구간의 길이는 기존 구간의 길이보다 짧아야 한다.
- [x] 노선에 역 추가시 노선 처음에 추가 할 수 있다.
  - [x] 새로 추가하려는 구간의 하행역이 노선의 하행 시작역과 같아야 한다.
- [x] 이미 등록되어있는 역은 노선에 등록될 수 없다.

<br>

## 2단계 - 구간 삭제 요구사항 반영


### 기능 요구사항(완료 조건)

- [x] 노선에 등록된 역 제거 시 해당 역이 노선 가운데 있어도 제거할 수 있다.
  - 기존에는 마지막 역 삭제만 가능했는데 위치에 상관 없이 삭제가 가능하도록 수정
  - [x] 중간역이 제거될 경우 재배치함
    - 노선에 `A - B - C` 역이 연결되어 있을 때 `B`역을 제거할 경우 `A - C`로 재배치 됨
    - 거리는 두 구간의 거리 합으로 정함. `A -3- B -4- C` -> `A -7- C`
- [x] 종점이 제거될 경우 다음으로 오던 역이 종점이 됨.
- [x] 노선에 등록된 역 제거 시 해당 역이 상행 종점역이어도 제거할 수 있다.

<br>

## 3단계 - 경로 조회 기능

### 기능 요구사항(완료 조건)

- [x] 경로 조회 기능을 추가한다.
  - [x] 출발역과 도착역을 입력받아 경로를 조회한다.
  - [x] 경로 조회 기능은 최단 경로를 조회한다.
- [ ] 출발역과 도착역이 같은 경우 에러가 발생한다.
- [ ] 출발역과 도착역이 연결이 되어 있지 않은 경우 에러가 발생한다.
- [ ] 존재하지 않은 출발역이나 도착역을 조회 할 경우 에러가 발생한다.
