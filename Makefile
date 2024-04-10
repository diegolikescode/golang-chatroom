dale:
	go run src/cmd/main.go

db:
	docker-compose down --volumes --remove-orphans
	docker-compose up -d --build db 

compose:
	docker-compose down --volumes --remove-orphans
	docker-compose up -d --build

build:
	docker build -t rinha_sql .

do: build compose

t: 
	sh ./test-local.sh

