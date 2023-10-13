## 유용한 example
- https://github.com/Baeldung/spring-security-oauth.git

## endpoint  
- https://backstage.forgerock.com/knowledge/kb/article/a45882528

## oauth2 by curl
0. https://medium.com/codex/manually-obtain-googleoauth2-access-token-with-your-web-browser-and-curl-fd93effe15ff
1. user authentication( by browser)

    https://accounts.google.com/o/oauth2/auth?client_id=248784500218-cirndg0qrgupp2ug7kunqhfvrln85ou5.apps.googleusercontent.com&redirect_uri=http://localhost:8080/login/oauth2/code/google&scope=profile&email&response_type=code&include_granted_scopes=true&access_type=offline&state=state_parameter_passthrough_value

    http://localhost:8080/login/oauth2/code/google?state=state_parameter_passthrough_value&code=4%2F0AfJohXkj6901qKbxJdedb1tUfclwo85ZXcRinrnt01lzCJ3gSSZ4ypqWNJNdr9jB_bf97w&scope=email+profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=consent


    https://accounts.google.com/o/oauth2/auth?client_id=248784500218-cirndg0qrgupp2ug7kunqhfvrln85ou5.apps.googleusercontent.com&redirect_uri=http://localhost:8080/login/oauth2/code/google&scope=openid&response_type=code&include_granted_scopes=true&access_type=offline&state=state_parameter_passthrough_value

		http://localhost:8080/login/oauth2/code/google?state=state_parameter_passthrough_value&code=4%2F0AfJohXnBA759qgHctAYhJ0QV0YJs3yXyqgZfDwBYQA_nsMFTnq7awDvGqCeZ_fdf-uD-tw&scope=email+profile+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile&authuser=0&prompt=consent


2. Request access token( by curl)

    curl -X POST https://oauth2.googleapis.com/token -d "code=4%2F0AfJohXnBA759qgHctAYhJ0QV0YJs3yXyqgZfDwBYQA_nsMFTnq7awDvGqCeZ_fdf-uD-tw&client_id=248784500218-cirndg0qrgupp2ug7kunqhfvrln85ou5.apps.googleusercontent.com&client_secret=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&redirect_uri=http://localhost:8080/login/oauth2/code/google&access_type=offline&grant_type=authorization_code"

		{
			"access_token": "",
			"expires_in": 3599,
			"refresh_token": "1//0ekqQgfMPvURJCgYIARAAGA4SNwF-L9IrtVQIiVUJdvp1GP5sXNXQgYzafQ-q44koAHWZ89zsist5u2ZfTEblrbGBC-5kBMUINoA",
			"scope": "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile openid",
			"token_type": "Bearer",
			"id_token": ""
		}

    jwt decoded
		{
			"iss": "https://accounts.google.com",
			"azp": 
			"aud": 
			"sub": 
			"email": 
			"email_verified": true,
			"at_hash": 
			"name": 
			"picture": 
			"given_name": 
			"family_name": 
			"locale": "en",
			"iat": 1696400377,
			"exp": 1696403977
		}
