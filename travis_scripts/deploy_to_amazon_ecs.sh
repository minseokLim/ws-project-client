echo "Launching $BUILD_NAME IN AMAZON ECS"
ecs-cli configure profile --profile-name "default" --access-key $AWS_ACCESS_KEY --secret-key $AWS_SECRET_KEY
ecs-cli configure --cluster mslim-ws-project --default-launch-type EC2 --region ap-northeast-2 --config-name default
ecs-cli compose --file docker/common/docker-compose-aws-dev.yml up
rm -rf ~/.ecs
