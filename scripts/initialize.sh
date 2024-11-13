# Parameter Store에서 GitHub 자격 증명을 가져오기
GH_USERNAME=$(aws ssm get-parameter --name "/github/username" --query "Parameter.Value" --output text | jq -sRr @uri)
GH_PAT=$(aws ssm get-parameter --name "/github/token" --query "Parameter.Value" --output text | jq -sRr @uri)

# GitHub 자격 증명 파일 설정
echo "https://${GH_USERNAME}:${GH_PAT}@github.com" > ~/.git-credentials
git config --global credential.helper store


cd /home/ubuntu/Server
git pull origin master