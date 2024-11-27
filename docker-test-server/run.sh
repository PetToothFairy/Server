#!/bin/bash

docker-compose down
docker rmi docker-test-server-server:latest
docker-compose up -d