dale:
	go run src/cmd/main.go

compose:
	docker-compose down --volumes --remove-orphans
	docker-compose up -d --build

build:
	docker build -t rinha_sql .

db:
	docker-compose down --volumes --remove-orphans
	docker-compose up --build db pgadmin

t:
	# docker build -t rinha_sql .
	docker-compose up -d --build
	sh ./test-local.sh
	docker-compose down --volumes --remove-orphans

