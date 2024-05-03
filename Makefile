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

limitless-comp:
	docker-compose -f docker-compose-limitless.yml down --volumes --remove-orphans
	docker-compose -f docker-compose-limitless.yml up -d --build

do: build compose

do-limitless: build limitless-comp

t:
	# docker build -t rinha_sql .
	docker-compose up -d --build
	sh ./test-local.sh
	docker-compose down --volumes --remove-orphans

tl: 
	# docker build -t rinha_sql .
	docker-compose -f docker-compose-limitless.yml up -d --build
	sh ./test-local.sh
	docker-compose -f docker-compose-limitless.yml down --volumes --remove-orphans

