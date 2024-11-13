cd /home/ubuntu/Server

sudo docker-compose down
sudo docker image prune -a
sudo docker network prune

cd ../

sudo rm -rf Server