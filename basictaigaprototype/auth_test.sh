#!/bin/bash
# Request username and password for connecting to Taiga
#read -p "Username or email: " USERNAME
#read -p "Password: " PASSWORD

# Get AUTH_TOKEN
USER_AUTH_DETAIL=$( curl -X POST \
  -H "Content-Type: application/json" \
  -d '{
          "type": "normal",
          "username": "'admin'",
          "password": "'123123'"
      }' \
 	localhost:8000/api/v1/auth 2>/dev/null )

AUTH_TOKEN=$( echo ${USER_AUTH_DETAIL} | jq -r '.auth_token' )

# Exit if AUTH_TOKEN is not available
if [ -z ${AUTH_TOKEN} ]; then
    echo "Error: Incorrect username and/or password supplied"
    exit 1
else
    echo "auth_token is ${AUTH_TOKEN}"
fi

curl -X GET \
	-H "Content-Type: application/json" \
	-H "Authorization: Bearer ${AUTH_TOKEN}" \
	localhost:8000/api/v1/projects
curl -X GET \
	-H "Content-Type: application/json" \
	-H "Authorization: Bearer ${AUTH_TOKEN}" \
	localhost:8000/api/v1/userstories/30
