# syntax=docker/dockerfile:experimental
FROM openjdk:11

RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee -a /etc/apt/sources.list.d/sbt.list \
    && echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list \
    && curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add \
    && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -qq update \
    && apt-get -qq -o=Dpkg::Use-Pty=0 install libxtst6 libx11-6 libxrender1 xvfb openssh-server python3 python3-pip \
        python3-venv sbt libssl-dev pkg-config x11-apps vim imagemagick zip openjdk-11-jre openjdk-11-jdk

ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
ENV IDEPROBE_DISPLAY=xvfb
WORKDIR /ideprobe-pants
RUN --mount=type=bind,rw,source=.,target=. set -x \
 && ci/setup-consents.sh \
 && sbt projects > /dev/null 2> /dev/null
WORKDIR /root
RUN rmdir /ideprobe-pants
ADD . /workspace
WORKDIR /workspace
