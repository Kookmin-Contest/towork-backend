# 로컬 실행 방법

```shell
./gradlew bootJar
docker-compose up --build -d
```
gradlew를 이용하여 build하고
docker를 이용해 mysql, redis와 함께 서버를 올린다.