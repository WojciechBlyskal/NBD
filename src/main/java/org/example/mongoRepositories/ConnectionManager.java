package org.example.mongoRepositories;

public class ConnectionManager extends AbstractMongoRepository implements AutoCloseable {

    public ConnectionManager() {
        initDbConnection();
    }

    @Override
    public void close(){
        getMongoClient().close();
    }
}
