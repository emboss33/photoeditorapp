
# 팀 프로젝트 Git 협업 관리 매뉴얼

이 문서는 Git을 사용한 팀 협업 개발 시 혼선을 줄이고 효율적으로 협업하기 위한 표준 작업 가이드를 제공합니다.

---

## 1. 매일 아침 개발 시작 루틴

매일 작업을 시작하기 전 아래 루틴을 따라 최신 코드를 반영한 상태에서 개발을 시작합니다.

```bash
git checkout develop                # develop 브랜치로 이동
git pull origin develop             # 최신 코드 가져오기
git checkout -b feature/기능명      # 기능 브랜치 생성 후 개발 시작
```

---

## 2. 작업 도중 다른 사람의 변경사항 반영 방법

작업 중에도 다른 사람이 develop 브랜치를 변경했을 수 있으므로, 주기적으로 내 작업에 최신 코드를 반영합니다.

### 2-1. 변경사항이 커밋되지 않았을 경우

```bash
git stash                           # 내 변경사항 임시 저장
git pull origin develop             # 최신 코드 가져오기
git stash pop                       # 내 변경사항 복원
```

### 2-2. 변경사항을 이미 커밋한 경우

```bash
git pull --rebase origin develop    # 내 커밋을 최신 develop 위에 정렬
```

---

## 3. 기능 구현 완료 후 커밋 및 푸시

기능 개발이 완료되면 아래 절차로 커밋하고 원격 저장소로 푸시합니다.

```bash
git add .                                              # 변경사항 스테이지
git commit -m "기능 구현 설명 메시지"                  # 커밋
git pull --rebase origin develop                       # 최신 코드 반영
git push origin feature/기능명                         # 원격 저장소로 푸시
```

---

## 4. Pull Request(PR) 절차

1. GitHub에서 `feature/기능명` → `develop` 으로 PR 생성
2. 팀원에게 코드 리뷰 요청
3. 리뷰어가 승인
4. 승인 후 Merge 버튼 클릭 (merge commit 또는 squash merge 방식)
5. Merge 완료되면 `feature/기능명` 브랜치 삭제

---

## 5. 충돌 발생 시 대응 방법

다른 사람과 같은 파일을 수정했을 경우 충돌(conflict)이 발생할 수 있습니다.

### 해결 절차

```bash
# 충돌된 파일 수동 수정
git add .                         # 수정한 파일 스테이지
git commit -m "충돌 해결"        # 충돌 해결 커밋
```

---

## 6. 시나리오별 Git 명령어 요약

| 상황 | 권장 명령어 및 설명 |
|------|--------------------|
| 커밋한 상태에서 pull 필요 | `git pull --rebase origin develop` |
| 커밋 전 pull 필요 | `git stash` → `pull` → `stash pop` |
| 실수로 pull 후 충돌 발생 | 충돌 수정 후 `add` + `commit` |
| 내 작업을 버리고 최신만 반영 | `git reset --hard HEAD` → `git pull origin develop` |

---

## 7. 브랜치 네이밍 규칙 (예시)

- `feature/기능명`: 새로운 기능 추가
- `fix/버그명`: 버그 수정
- `hotfix/긴급패치`: 배포 중 이슈 해결
- `refactor/모듈명`: 리팩토링

---

## 8. 커밋 메시지 컨벤션 (예시)

- `feat: 이미지 업로드 기능 추가`
- `fix: 로그인 실패 시 예외 처리 수정`
- `refactor: user 모듈 리팩토링`
- `docs: README 수정`
- `style: 코드 포맷 정리 (세미콜론, 들여쓰기 등)`

---

## 9. Pull Request 병합 방식 원칙

- `Squash and merge` (권장): 커밋을 하나로 합쳐 깔끔한 히스토리 유지
- `Create a merge commit`: 전체 작업 흐름을 상세히 보존하고자 할 경우
- `Rebase and merge`: 커밋 선형 유지 필요 시 (숙련자 권장)

---

## 10. 커밋 단위 기준

- 커밋은 가능한 한 **하나의 의미 있는 작업 단위**로 유지
- 메시지는 간결하고, 누가 봐도 어떤 작업인지 알 수 있도록 작성

---

## 11. 기능 PR 전 체크리스트

- [ ] 모든 코드가 정상 빌드되는가?
- [ ] 테스트 또는 앱 실행 결과 이상 없는가?
- [ ] 디버깅/로그 코드가 제거되었는가?
- [ ] 변수명, 함수명은 명확한가?
- [ ] PR 제목 및 설명이 명확하게 작성되었는가?

---

## 12. 메인 브랜치 구조 설명

- `main` 또는 `master`: 실제 배포 대상 브랜치
- `develop`: 모든 기능이 통합되는 개발 브랜치
- `feature/*`: 기능 단위 작업 브랜치
- `hotfix/*`: 배포 후 발생한 이슈 긴급 수정용 브랜치

---

## 13. .gitignore 설정 예시

아래 항목들을 `.gitignore` 파일에 반드시 포함시켜 불필요한 파일이 커밋되지 않도록 합니다:

```
# macOS
.DS_Store

# Windows
Thumbs.db

# IDE
.idea/
*.iml
.vscode/

# 빌드 디렉토리
/build/
out/

# 민감 정보
*.keystore
*.pem
.env
```

---

> 이 매뉴얼은 팀원 간 Git 사용 혼선을 방지하고 협업 품질을 높이기 위한 기준입니다.
> 프로젝트 상황에 따라 유연하게 수정하여 사용하세요.
