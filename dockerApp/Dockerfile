FROM open-liberty
COPY --chown=1001:0 ./RatemeApp/target/rateme.war /config/apps/rateme.war
COPY --chown=1001:0 ./RatemeApp/src/main/liberty/config/ /config/
COPY --chown=1001:0 ./dockerApp/configOverrides/ /config/configDropins/overrides/
EXPOSE 9080
EXPOSE 9443
RUN configure.sh