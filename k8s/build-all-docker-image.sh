#!/bin/bash

# remove all containers
docker rm -v -f $(docker ps -qa)
docker ps -qa

# classical-rag-api
./build-target.sh classical-rag-api mvn git_pull_no
# repo_name   tag   container_name
./build-docker-image.sh classical-rag-api rag-api rag-api-service

# rag-spa git_pull_yes
./build-target.sh rag-spa 'npm run build' git_pull_no
./build-docker-image.sh rag-spa rag-ui rag-ui-service

