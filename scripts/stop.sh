cd /home/ubuntu/Server

docker-compose down
docker image prune -a

cp docker-compose.yml /home/ubuntu

cd ../

sudo rm -rf Server