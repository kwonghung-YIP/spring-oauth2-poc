Spring OAuth2 Authorization Server + MySQL + Redis

Use curl to test different grant types:

1) Authorization Code Grant:

Step 1)

curl -v 'http://localhost:7070/oauth/authorize?client_id=client-auth-code&redirect_uri=http://localhost:8080/login&response_type=code&state=SomeState&scope=read' \
  --user john:password

Step 2)
client_id and client_secret are passed via the Basic Auth header

curl -v 'http://localhost:7070/oauth/token' \
  --user client-auth-code:password \
  -d grant_type=authorization_code \
  -d redirect_uri=http://localhost:8080/login \
  -d code=C2JndM

client_id and client_secret are passed as form data
  
curl -v 'http://localhost:7070/oauth/token' \
  -d grant_type=authorization_code \
  -d redirect_uri=http://localhost:8080/login \
  -d client_id=client-auth-code \
  -d client_secret=password \
  -d code=4zn1zS

Step 3)
Get the userinfo with the accss_token
  
curl -v 'http://localhost:7070/oauth/userinfo' \
  -H "Authorization: Bearer 2889a30c-a46d-469c-a82b-b5c6ab6eb379"
 
2) Implicit Grant:

curl -v 'http://localhost:7070/oauth/authorize?client_id=client-implicit&redirect_uri=http://localhost:8080/login&response_type=token&state=SomeState&scope=read' \
  --user john:password
 
3) Resource Owner Credential Grant:

curl -v 'http://localhost:7070/oauth/token' \
  -d grant_type=password \
  -d client_id=client-password \
  -d client_secret=password \
  -d scope=read \
  -d username=john \
  -d password=password

4) Client Credentials Grant:

curl -v 'http://localhost:7070/oauth/token' \
  -d grant_type=client_credentials \
  -d client_id=client-client-cred \
  -d client_secret=password \
  -d scope=read
  
5) Refresh Token Grant:  

Get the new access token with the refresh_token  

curl -v 'http://localhost:7070/oauth/token' \
  --user client-auth-code:password \
  -d grant_type=refresh_token \
  -d refresh_token=ea92d197-e94a-4429-a0fd-d9ef0bb18367 \
  -d scope=read

curl -v 'http://localhost:7070/oauth/token' \
  -d grant_type=refresh_token \
  -d refresh_token=ea92d197-e94a-4429-a0fd-d9ef0bb18367 \
  -d client_id=client-auth-code \
  -d client_secret=password \
  -d scope=read
