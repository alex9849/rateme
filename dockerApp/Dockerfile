FROM open-liberty
COPY --chown=1001:0 ./target/rateme.war /config/apps/rateme.war
COPY --chown=1001:0 ./src/main/liberty/config/ /config/
RUN configure.sh