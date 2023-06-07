## s3 bucket에 public-read를 설정함.

    // https://github.com/localstack/localstack/issues/406#issuecomment-516533885
    aws --endpoint-url=http://localhost:4566 s3api create-bucket --bucket shared --region us-east-1
    aws --endpoint-url=http://localhost:4566 s3api put-bucket-acl --bucket shared --acl public-read
    aws --endpoint-url=http://localhost:4566 s3 cp build.gradle.kts s3://shared
    curl -v http://localhost:4566/shared/build.gradle.kts
