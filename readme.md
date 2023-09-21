# 로컬 실행 방법

먼저 `.env.template`를 복사하여 `.env`를 생성한 뒤 환경변수를 적어 넣는다.

## IntelliJ로 개발할 경우
[EnvFile Plugin](https://plugins.jetbrains.com/plugin/7861-envfile)

인텔리제이에 위 플러그인을 설치한 뒤 실행 시 `.env`를 인식하도록 설정하면 된다.

## jar을 직접 실행할 경우
```shell
export $(grep -v '^#' .env | xargs -d '\n')
```
위 명령어로 시스템 환경 변수에 .env 변수들을 등록하고 실행하면 된다.