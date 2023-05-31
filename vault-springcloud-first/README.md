## reference
https://spring.io/guides/gs/vault-config/
https://kim-oriental.tistory.com/50
https://spring.io/blog/2020/08/14/config-file-processing-in-spring-boot-2-4

## vault

    vault server --dev --dev-root-token-id="00000000-0000-0000-0000-000000000000"

    export export VAULT_TOKEN="00000000-0000-0000-0000-000000000000"
    export VAULT_ADDR="http://127.0.0.1:8200"

    vault kv put secret/gs-vault-config         example.username=demouser example.password=demopassword
    vault kv put secret/gs-vault-config/cloud   example.username=clouduser example.password=cloudpassword


## vault location

    secret/gs-vault-config/cloud (higher priority)
    secret/gs-valut-config
    secret/application/cloud
    secret/application (lower priority)


## run default

    ./gradlew bootRun
    Vault location [secret/application] not resolvable: Not found
    ----------------------------------------
    Configuration properties
       example.username is foo
       example.password is bar
    ----------------------------------------

## run alpha

    SPRING_PROFILES_ACTIVE=alpha ./gradlew bootRun
    Vault location [secret/application/cloud] not resolvable: Not found
    Vault location [secret/application] not resolvable: Not found
    ----------------------------------------
    Configuration properties
       example.username is demouser
       example.password is demopassword
    ----------------------------------------

## run cloud

    SPRING_PROFILES_ACTIVE=cloud ./gradlew bootRun
    Vault location [secret/application/cloud] not resolvable: Not found
    Vault location [secret/application] not resolvable: Not found
    ----------------------------------------
    Configuration properties
       example.username is clouduser
       example.password is cloudpassword
    ----------------------------------------


