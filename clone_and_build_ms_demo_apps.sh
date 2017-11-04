#!/bin/bash

mkdir -p ~/git
cd ~/git
git clone https://github.com/jaheikki/microservice-demo-order-copy.git
git clone https://github.com/jaheikki/microservice-demo-eureka-server-copy.git
git clone https://github.com/jaheikki/microservice-demo-zuul-server-copy.git
git clone https://github.com/jaheikki/microservice-demo-customer-copy.git
git clone https://github.com/jaheikki/microservice-demo-catalog-copy.git
git clone https://github.com/jaheikki/microservice-demo-acceptance-tests-copy.git

cd ~/git/microservice-demo-order-copy && mvn clean install
cd ~/git/microservice-demo-zuul-server-copy && mvn clean install
cd ~/git/microservice-demo-eureka-server-copy && mvn clean install
cd ~/git/microservice-demo-customer-copy && mvn clean install
cd ~/git/microservice-demo-catalog-copy && mvn clean install
