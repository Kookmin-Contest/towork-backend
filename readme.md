# 로컬 실행 방법

# 터미널 사용
```shell
docker-compose up --build -d
```
server가 build되고 mysql, redis와 같이 docker에 올라온다.

# intellij 사용
Run > Edit Configurations... > docker-compose.yml

docker-compose up 옆 modify에서 Build를 always로 설정