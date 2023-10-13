## howto

    docker compose up 
    # 주의  "vault secrets disable secret; vault secrets enable -version=1 -path=secret kv" 이 적용 안되어 있을 수 있다.
 
    ./gradlew bootRun

    export export VAULT_TOKEN="00000000-0000-0000-0000-000000000000"
    export VAULT_ADDR="http://127.0.0.1:8200"
    vault kv get secret/myteam/apps/taxi
    vault kv get -format json secret/myteam/apps/taxi

## reference

    https://www.baeldung.com/jpa-attribute-converters