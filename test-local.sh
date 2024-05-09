#!/usr/bin/bash

# /home/camel/src/rinha-backend-2023q3/teste/gatling/deps/gatling/bin
GATLING_BIN_DIR=/home/camel/gatling/3.10.3/bin
GATLING_WORKSPACE=$(pwd)/load-test
RESULTS_WORKSPACE=$(pwd)/load-test/results

docker-compose logs > "./docker-compose.logs"

sh $GATLING_BIN_DIR/gatling.sh -rm local -s RinhaBackendSimulation \
    -rd "RinhaBackend - load-test"\
    -rf "$RESULTS_WORKSPACE" \
    -sf $GATLING_WORKSPACE/simulations \
    -rsf $GATLING_WORKSPACE/resources

SAVE_CONTAGEM="$RESULTS_WORKSPACE/contagem-pessoas-$(date +%s).log" ## WILL IT WORK? IDK
curl -v "http://localhost:9999/contagem-pessoas" > "$SAVE_CONTAGEM"
echo "resultado da contagem em $SAVE_CONTAGEM\n"
cat "$SAVE_CONTAGEM"
echo "\n"
