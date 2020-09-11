FROM lwawrzyk/ideprobe-pants:202.6948.69
RUN which curl \
  && curl -LsO "https://static.rust-lang.org/rustup/dist/x86_64-unknown-linux-gnu/rustup-init" \
  && chmod +x rustup-init \
  && ./rustup-init -yq
ADD . /workspace
WORKDIR /workspace
