#!/usr/bin/bash

RESULTS_WORKSPACE="$(pwd)/load-test/run-results"
GATLING_BIN_DIR=/home/camel/gatling/3.10.3/bin
GATLING_WORKSPACE="$(pwd)/load-test/user-files"

docker system prune -f

sleep 6.9

echo "iniciando e logando execução da API"
docker build --no-cache -t rinhajava .
docker-compose up -d --build
docker-compose logs > "$(pwd)/docker-compose.logs"

echo "pausa de 6 segundos para startup pra API"
sleep 6.9

echo "iniciando teste"
sh $GATLING_BIN_DIR/gatling.sh -rm local -s RinhaBackendSimulation \
    -rd $DIEGO\
    -rf "$RESULTS_WORKSPACE/$DIEGO" \
    -sf $GATLING_WORKSPACE/user-files/simulations \
    -rsf $GATLING_WORKSPACE/user-files/resources
echo "teste finalizado"
echo "fazendo request e salvando a contagem de pessoas"
SAVE_CONTAGEM="$RESULTS_WORKSPACE/$DIEGO/contagem-pessoas-$(date).log"
curl -v "http://localhost:9999/contagem-pessoas" > "$SAVE_CONTAGEM"
echo "resultado da contagem em $SAVE_CONTAGEM"
cat "$SAVE_CONTAGEM"
echo "cleaning up do docker"
docker-compose rm -f
docker-compose down
touch "$RESULTS_WORKSPACE/$DIEGO/testado"
