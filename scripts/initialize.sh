# Parameter Store에서 GitHub 자격 증명을 가져오기
GH_USERNAME=$(aws ssm get-parameter --name "/github/username" --with-decryption --query "Parameter.Value" --output text)
GH_PAT=$(aws ssm get-parameter --name "/github/token" --with-decryption --query "Parameter.Value" --output text)

# GitHub 자격 증명 파일 설정
echo "https://${GH_USERNAME}:${GH_PAT}@github.com" > ~/.git-credentials
git config --global credential.helper store


cd /home/ubuntu/Server
git pull origin master