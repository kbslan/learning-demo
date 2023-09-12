#!/bin/bash
trim() {
  trimmed=$1
  trimmed=${trimmed%% }
  trimmed=${trimmed## }
  echo $trimmed
}
# 工程
project=$1
ref_name=$2
# pipelines
message=$3

echo $project $ref_name $message

#commit=$(git log -1 --pretty=format:%s\  | awk 'NR==1 ')
# 获取 email
email=$(trim $(git log -1 --pretty=format:\%ae\  | awk 'NR==1 '))
echo $email
#echo $commit

# 获取Token
application='{
    "app_id":"cli_9f7ea8ad3538500b",
    "app_secret":"pJk7umF6jxM2OR66rWfVu0nkipGoA24y"
}'
token=$(curl -H "Content-Type: application/json" -X POST -d "$application" "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal/")
echo $token
token=$(echo $token | jq -r '.tenant_access_token')
echo $token

# 获取群ID
#chats=$(curl -H "Authorization: Bearer t-2c3dc5f6aaa7d9659d96500353bd4c3f5f45c36b" -X GET "https://open.feishu.cn/open-apis/chat/v4/list")
#echo $chats
# 获取用户ID
users=$(curl -H "Authorization: Bearer $token" -X GET "https://open.feishu.cn/open-apis/user/v1/batch_get_id?emails=$email")
echo $users
open_id=$(echo $users | jq -r '.["data"]["email_users"]["'$email'"][0]["open_id"]')
echo $open_id

#body='{"msg_type": "text","content": { "text": "dmall-offline-marketing ci success : '$commit'" } }'
body='{
    "msg_type": "post",
    "msg_type": "post",
    "chat_id": "oc_27f88df8e9b3b2e1e6a7eba8585d2e44",
    "email": "'$email'",
    "content": {
        "post": {
            "zh_cn": {
                "title": "'$project'/'$ref_name' CI Failure",
                "content": [
                    [
                        {
                            "tag": "text",
                            "text": "'$message': "
                        },
                        {
                            "tag": "at",
                            "user_id": "'$open_id'"
                        }
                    ]
                ]
            }
        }
    }
} '
echo $body
#curl -H "Content-Type: application/json" -H "Authorization: Bearer t-2c3dc5f6aaa7d9659d96500353bd4c3f5f45c36b" -X POST -d "$body" "https://open.feishu.cn/open-apis/bot/v2/hook/6158c5fc-3404-4282-bc20-e07a201fb661"
curl -H "Content-Type: application/json" -H "Authorization: Bearer $token" -X POST -d "$body" "https://open.feishu.cn/open-apis/message/v4/send/"
