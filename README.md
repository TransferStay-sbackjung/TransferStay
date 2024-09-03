# TransferStay
zb 팀프로젝트 숙박 양도 서비스 어플리케이션

### Branch

- `main(master)`, `develop`, `feat/기능명` 으로 구분해서 진행
    - main : 배포될 내용의 최종본 코드
    - develop : 완성된 기능이 적용된 후, 테스트 진행 → 이상 없을 시 main으로 rebase
    - feat/기능명 : 각자 수행할 기능 별로 브랜치 생성 후 작업 → develop에 PR 보내기

### Commit

- 최대한 작은 단위로 수행하기
- 컨벤션을 지켜서 메시지 작성하기
    1. 관련 이슈 번호 남기기
    2. 어떤걸 수행했는지 알 수 있도록 명시적으로 남기기
    
    > **ex) [#1] Feat: connection openbaking api auth**
    > 
    
    | Feat | 새로운 기능 추가 |
    | --- | --- |
    | Fix | 버그/기능에 따른 코드 변경 시 |
    | Refactor | 코드의 가독성, 효율성을 위한 코드 변경 시 (기능의 문제 X) |
    | Docs | 문서 추가/수정/삭제 |
    | Chore | 설정 추가/변경 |
    | Test | 테스트 코드 추가/수정 |

### Issue

- 최소한의 기능단위와, 버그단위로 생성하기


### Pull Request

- 반드시 feat/기능 → develop으로 pull-request 생성하기
- 최대한 작은 단위로 생성하기! → 코드리뷰를 위해서
    - 리뷰어를 위해 꼭 어떤 기능을 수행했고, 어떤걸 주로 확인하면 되는지 작성 필요
- 꼭 1명 이상 리뷰 후 머지 수행
