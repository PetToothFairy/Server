cd /home/ubuntu/Server

docker-compose down
docker image prune -a

cp docker-compose.yml ../

cd ../

rm -rf Server