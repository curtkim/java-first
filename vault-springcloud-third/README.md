## 개요
spring.config.import: vault://mydb?prefix=mydb., vault://s3?prefix=s3.

아직실패

## reference
https://docs.spring.io/spring-cloud-vault/docs/current/reference/html/config-data.html

## vault

    export export VAULT_TOKEN="00000000-0000-0000-0000-000000000000"
    export VAULT_ADDR="http://127.0.0.1:8200"

    vault kv put secret/third/mydb          username=demouser password=demopassword
    vault kv put secret/thrid/s3            accesskey=a secretkey=b


