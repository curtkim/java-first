## intro
springcloud-vault를 사용하지 않고, 
vaultTemplate을 직접사용하거나
@VaultPropertySource를 사용한다.

## vault

    vault server --dev --dev-root-token-id="00000000-0000-0000-0000-000000000000"

    export export VAULT_TOKEN="00000000-0000-0000-0000-000000000000"
    export VAULT_ADDR="http://127.0.0.1:8200"

    vault kv put secret/myteam/db/mydb              username=demouser password=demopassword
    vault kv put secret/myteam/s3/servicea          accesskey=a secretkey=b
