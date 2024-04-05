dale:
	mvn install
	mvn exec:java

compose:
	docker-compose down --volumes --remove-orphans
	docker-compose up --build

