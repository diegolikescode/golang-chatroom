dale:
	go run src/cmd/main.go

db:
	docker-compose down --volumes --remove-orphans
	docker-compose up -d --build db 

