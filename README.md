# camunda-business-process

# Prerequisites
- Linux or WSL for Windows
- Docker

# Clone
Clone the project
```
git clone git@github.com:rahibbutt/Camunda-BPM-Demonstrator.git
```

# Run Project
To run all the services (camunda engine, frontend application etc), execute the following command:
```
./dev-tools.sh up
```

# Cleanup
To stop all the services and cleanup, run the following command:
```
./dev-tools.sh down
```

# To generate the latest War file 
In order to generate the latest war file of your changes. Note: This command will only work if the containers are up and running.
```
./dev-tools.sh build_process_application
```
