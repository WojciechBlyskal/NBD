FROM mongo:7.0.2
COPY --chown=999 --chmod=600 keyFile /etc/mongo/keyFile
RUN chmod 400 /etc/mongo/keyFile
COPY --chown=999 --chmod=600 path/etc/mongod.conf /etc/mongod.conf
