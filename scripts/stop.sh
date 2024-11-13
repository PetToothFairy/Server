cd /home/ubuntu/Server

sudo docker-compose down
sudo docker image prune -a

cp docker-compose.yml /home/ubuntu

cd ../

sudo rm -rf Server