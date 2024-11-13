cd /home/ubuntu/Server

docker-compose down
docker image prune -a

cp docker-compose.yml /home/ubuntu

cd ../

rm -rf Server