#!/bin/bash

# start vault
#VAULT_UI=true vault server -dev -dev-root-token-id=root -dev-listen-address=127.0.0.1:8200

# login as root - DO NOT DO THIS IN PRODUCTION
#vault login root
VAULT_ADDR="http://127.0.0.1:8200"
VAULT_TOKEN=root

# write some secrets
vault kv put secret/test color=blue number=eleventeen

# create test policy
echo 'path "secret/*" {
  capabilities = ["list", "read"]
}' | vault policy write test -

# enable approle
vault auth enable approle

# configure approle role named "testrole"
vault write auth/approle/role/testrole \
secret_id_bound_cidrs="0.0.0.0/0","127.0.0.1/32" \
secret_id_ttl=60m \
secret_id_num_uses=5 \
enable_local_secret_ids=false \
token_bound_cidrs="0.0.0.0/0","127.0.0.1/32" \
token_num_uses=10 \
token_ttl=1h \
token_max_ttl=3h \
token_type=default \
period="" \
policies="default","test"


# Read role-id
#vault read auth/approle/role/testrole/role-id 
ROLE_ID=$(vault read -format=json auth/approle/role/testrole/role-id | jq -r '.data.role_id')

# generate secret-id
#vault write -f auth/approle/role/testrole/secret-id
SECRET_ID=$(vault write -f -format=json auth/approle/role/testrole/secret-id | jq -r '.data.secret_id')

# login with role-id + secret-id
VAULT_TOKEN=$(vault write -format=json auth/approle/login role_id=$ROLE_ID secret_id=$SECRET_ID | jq -r '.auth.client_token')
#vault write auth/approle/login role_id=$ROLE_ID secret_id=$SECRET_ID


vault kv get secret/test
