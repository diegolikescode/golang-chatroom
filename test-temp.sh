#!/usr/bin/bash

docker system prune -f

# /home/camel/src/rinha-backend-2023q3/teste/gatling/deps/gatling/bin
GATLING_BIN_DIR=/home/camel/gatling/3.10.3/bin
# GATLING_WORKSPACE=$(pwd)/load-test
RESULTS_WORKSPACE=$(pwd)/load-test/results

docker build -t rinha_sql .

echo "iniciando e logando execução da API"
docker-compose up -d --build
docker-compose logs > "./docker-compose.logs"

echo "pausa de 6 segundos para startup pra API"
sleep 6
echo "iniciando teste"

sh $GATLING_BIN_DIR/gatling.sh -rm local -s RinhaBackendSimulation \
    -rd "RinhaBackend - load-test"\
    -rf "$RESULTS_WORKSPACE" \
    -sf $GATLING_WORKSPACE/rinhabackend \
    -rsf $GATLING_WORKSPACE/user-files/resources
echo "teste finalizado"
echo "fazendo request e salvando a contagem de pessoas"
SAVE_CONTAGEM="$RESULTS_WORKSPACE/contagem-pessoas-$(date).log"
curl -v "http://localhost:9999/contagem-pessoas" > "$SAVE_CONTAGEM"
echo "resultado da contagem em $SAVE_CONTAGEM"
cat "$SAVE_CONTAGEM"
echo "cleaning up do docker"
docker-compose rm -f
docker-compose down

