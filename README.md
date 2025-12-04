# Get authorization code:
Redirect to:
```
https://ducbrick.us.auth0.com/authorize?audience=https://sa.ducbrick.com&response_type=code&client_id=wrbu1ylF5tJr1mJmSh0o4RrAT8Zzeqnk&scope=openid offline_access admin&redirect_uri=http://localhost:8081/code
```

# Exchange authorization code for access token
``` bash
curl --location 'https://ducbrick.us.auth0.com/oauth/token' \
--header 'Content-Type: application/json' \
--data '{
    "grant_type":"authorization_code",
    "client_id":"wrbu1ylF5tJr1mJmSh0o4RrAT8Zzeqnk",
    "client_secret":"R-MZhcg_-A1w2WGn5Yexla8SJgmrExBCRSYih2N1z4nxUFRwwBlAhmZwrjg-N4TQ",
    "code":"${INSERT_AUTHORIZATION_CODE_HERE}",
    "redirect_uri":"http://localhost:8081"
}'
```
