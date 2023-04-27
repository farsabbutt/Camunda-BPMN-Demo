#!/bin/bash
set -e
# targets
#==========================================
allowed_targets=(
  'up'
  'down'
  'build_process_application'
  'clear_mysql_data'
)

# base variables
#=========================================
project_name=camunda-business-process
docker_compose_base_file=docker-compose.yml

container_name_basic=basic
container_name_process_application=process-application

docker_compose_cmd="docker-compose -f $docker_compose_base_file -p $project_name"

up() {
  sh -c "$docker_compose_cmd up"
}

down() {
  echo -e "stopping the containers and removing them along with the network\n"
  $docker_compose_cmd down
}

build_process_application() {
  echo -e "Building process application..."
  $docker_compose_cmd exec -T "$container_name_process_application" mvn clean
  $docker_compose_cmd exec -T "$container_name_process_application" mvn install
  echo -e "Removing currently deployed process application from tomcat server webapps..."
  rm -rf ./camunda-engine/webapps/Camunda-IoT
  echo -e "Deploying newly built war file to tomcat webapps..."
  cp ./process-application/target/Camunda-IoT.war ./camunda-engine/webapps/
}

help() {
  echo "============================="
  echo "Allowed targets: "
  for i in ${allowed_targets[@]}; do
    echo $i
  done
}

clear_mysql_data() {
  sudo rm -rf ./data/mysql-db/*
}

#=========================================
#environment specific functions
#=========================================
os_init() {
  Linux() { :; }
  MINGW() {
    docker() {
      winpty docker "$@"
    }
    docker-compose() {
      winpty docker-compose "$@"
    }
  }
  unameOut="$(uname -s)"
  case "${unameOut}" in
  Linux*)
    machine=Linux
    Linux
    ;;
  Darwin*) machine=Mac ;;
  CYGWIN*) machine=Cygwin ;;
  MINGW*)
    machine=MinGw
    MINGW
    ;;
  *) machine="UNKNOWN:${unameOut}" ;;
  esac
}

_main() {
  os_init "$@"
  #=========================================
  # dynamically calling exposed targets
  #=========================================
  target="$1"
  if [[ ! -z "$target" ]] && [[ "${allowed_targets[@]}" =~ "$target" ]]; then
    $target "$@"
  else
    help "$@"
  fi
}
_main "$@"
