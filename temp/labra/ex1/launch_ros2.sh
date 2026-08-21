#!/bin/bash
set -e

CONTAINER_NAME="embedded-mas-example"
IMAGE_NAME="maiquelb/embedded-mas-ros2:latest"

# ---------------------------------------------------------
# 1. Stop container if running
# ---------------------------------------------------------
(docker ps -q --filter "name=$CONTAINER_NAME" | grep -q . && docker stop "$CONTAINER_NAME" || true)

# ---------------------------------------------------------
# 2. Create network if doesn't exist
# ---------------------------------------------------------
(docker network inspect ros >/dev/null 2>&1 || docker network create ros)

# (opcional – mantido apenas por compatibilidade)
(docker volume inspect x11socket >/dev/null 2>&1 || docker volume create x11socket)

# ---------------------------------------------------------
# 3. Start ROS 2 container
# ---------------------------------------------------------
docker run -d \
    --rm \
    --net=ros \
    --name "$CONTAINER_NAME" \
    -p 9090:9090 \
    "$IMAGE_NAME"

echo -e "\e[1;33m**** Launching ROS 2 container. Wait 20 seconds ****\e[0m"
sleep 20

# ---------------------------------------------------------
# 4. Publish latched-like initial values
# ---------------------------------------------------------
docker exec -d "$CONTAINER_NAME" /bin/bash -ic "
    source /opt/ros/humble/setup.bash && \
    ros2 topic pub \
        --qos-durability=transient_local \
        --once \
        /value1 std_msgs/Int32 '{data: 0}'
"

docker exec -d "$CONTAINER_NAME" /bin/bash -ic "
    source /opt/ros/humble/setup.bash && \
    ros2 topic pub \
        --qos-durability=transient_local \
        --once \
        /current_time std_msgs/String '{data: \"unknown\"}'
"

echo -e "\e[1;33m**** ROS 2 container is ready. Start the JaCaMo application ****\e[0m"

