#!/bin/bash

AppVersion="1.0"
DockerHubUser="mydev"
DockerHubRepoName="my-apps"
DockerHubRepository="${DockerHubUser}/${DockerHubRepoName}"
#
docker login --password my-secret-pass --username ${DockerHubUser}
###
FirstServiceName="first-service"
echo "Creating ${FirstServiceName} Image"
docker image build -f ${FirstServiceName}/Dockerfile -t ${FirstServiceName}:${AppVersion} .
docker image tag ${FirstServiceName}:${AppVersion} ${DockerHubRepository}:${FirstServiceName}-${AppVersion}
docker push ${DockerHubRepository}:${FirstServiceName}-${AppVersion}
#
###
SecondServiceName="second-service"
echo "Creating ${SecondServiceName} Image"
docker image build -f ${SecondServiceName}/Dockerfile -t ${SecondServiceName}:${AppVersion} .
docker image tag ${SecondServiceName}:${AppVersion} ${DockerHubRepository}:${SecondServiceName}-${AppVersion}
docker push ${DockerHubRepository}:${SecondServiceName}-${AppVersion}
#
###
AuthServiceName="auth-service"
echo "Creating ${AuthServiceName} Image"
docker image build -f ${AuthServiceName}/Dockerfile -t ${AuthServiceName}:${AppVersion} .
docker image tag ${AuthServiceName}:${AppVersion} ${DockerHubRepository}:${AuthServiceName}-${AppVersion}
docker push ${DockerHubRepository}:${AuthServiceName}-${AppVersion}
#
###
GatewayServiceDir="gateway-service-config"
GatewayServiceName="gateway-service"
echo "Creating ${GatewayServiceName} Image"
docker image build -f ${GatewayServiceDir}/Dockerfile -t ${GatewayServiceName}:${AppVersion} .
docker image tag ${GatewayServiceName}:${AppVersion} ${DockerHubRepository}:${GatewayServiceName}-${AppVersion}
docker push ${DockerHubRepository}:${GatewayServiceName}-${AppVersion}
#
###
EurekaServiceDir="eureka-service-discovery"
EurekaServiceName="discovery-service"
echo "Creating ${EurekaServiceName} Image"
docker image build -f ${EurekaServiceDir}/Dockerfile -t ${EurekaServiceName}:${AppVersion} .
docker image tag ${EurekaServiceName}:${AppVersion} ${DockerHubRepository}:${EurekaServiceName}-${AppVersion}
docker push ${DockerHubRepository}:${EurekaServiceName}-${AppVersion}
#
###
MonitoringServiceDir="monitoring"
MonitoringServiceName="prometheus-db"
echo "Creating ${MonitoringService} Image"
docker image build -f ${MonitoringServiceDir}/Dockerfile-embedded -t ${MonitoringServiceName}:${AppVersion} .
docker image tag ${MonitoringServiceName}:${AppVersion} ${DockerHubRepository}:${MonitoringServiceName}-${AppVersion}
docker push ${DockerHubRepository}:${MonitoringServiceName}-${AppVersion}
#
###End-Of-File###