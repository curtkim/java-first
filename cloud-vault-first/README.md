## vault

    export export VAULT_TOKEN="00000000-0000-0000-0000-000000000000"
    export VAULT_ADDR="http://127.0.0.1:8200"

    vault kv put secret/gs-vault-config example.username=demouser example.password=demopassword
    vault kv put secret/gs-vault-config/cloud example.username=clouduser example.password=cloudpassword

## howto
    
    ./gradlew bootRun
    SPRING_PROFILES_ACTIVE=cloud ./gradlew bootRun