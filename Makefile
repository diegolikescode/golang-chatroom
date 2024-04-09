dale:
	mvn install
	mvn exec:java

compose:
	docker-compose down --volumes --remove-orphans
	docker-compose up --build

build:
	mvn install
	docker build --no-cache -t rinhajava .

db:
	docker-compose down --volumes --remove-orphans
	docker-compose up -d db

api1: build
	# docker-compose down --volumes --remove-orphans
	docker-compose up api1

api-alone:
	docker build -t rinhajava . 
	docker run -p 6969:6969 rinhajava:latest

do: build compose
