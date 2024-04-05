dale:
	mvn install
	mvn exec:java

compose:
	docker-compose down --volumes --remove-orphans
	docker-compose up --build

build:
	mvn install
	docker build --no-cache -t rinhajava .
