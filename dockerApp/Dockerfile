FROM open-liberty
COPY --chown=1001:0 ./target/rateme.war /config/apps/rateme.war
COPY --chown=1001:0 ./src/main/liberty/config/ /config/
COPY --chown=1001:0 ./dockerApp/configOverrides/ /config/configDropins/overrides/
RUN configure.sh